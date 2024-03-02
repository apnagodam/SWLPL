package com.apnagodam.staff.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Base.BaseRecyclerViewAdapter;
import com.apnagodam.staff.Base.BaseViewHolder;
import com.apnagodam.staff.Network.Response.StackRequestResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.caseid.CaseIDGenerateClass;
import com.apnagodam.staff.activity.in.first_kantaparchi.UploadFirstkantaParchiClass;
import com.apnagodam.staff.activity.in.first_quality_reports.UploadFirstQualtityReportsClass;
import com.apnagodam.staff.activity.in.labourbook.LabourBookUploadClass;
import com.apnagodam.staff.activity.in.secound_kanthaparchi.UploadSecoundkantaParchiClass;
import com.apnagodam.staff.activity.in.secound_quality_reports.UploadSecoundQualtityReportsClass;
import com.apnagodam.staff.activity.in.truckbook.TruckUploadDetailsClass;
import com.apnagodam.staff.activity.out.f_katha_parchi.OutUploadFirrstkantaParchiClass;
import com.apnagodam.staff.activity.out.f_quailty_report.UploadOutFirstQualtityReportsClass;
import com.apnagodam.staff.activity.out.labourbook.OUTLabourBookUploadClass;
import com.apnagodam.staff.activity.out.s_katha_parchi.OutUploadSecoundkantaParchiClass;
import com.apnagodam.staff.activity.out.s_quaility_report.OutUploadSecoundQualtityReportsClass;
import com.apnagodam.staff.activity.out.truckbook.OUTTruckUploadDetailsClass;
import com.apnagodam.staff.databinding.LayoutStackRequestBinding;
import com.apnagodam.staff.databinding.LayoutTopCaseGenerateBinding;
import com.apnagodam.staff.module.AllCaseIDResponse;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class StackRequestAdapter extends BaseRecyclerViewAdapter {
    private List<StackRequestResponse.InwardRequestDatum> Leads;
    private Context context;
    private BaseActivity activity;

    public StackRequestAdapter(List<StackRequestResponse.InwardRequestDatum> leads, Activity caseListingActivity) {
        this.Leads = leads;
        this.context = caseListingActivity;
        this.activity = activity;
    }

    @Override
    public BaseViewHolder inflateLayout(ViewDataBinding view) {
        return new StackRequestAdapter.DefaultersTopHolder(view);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.layout_stack_request;
    }

    @Override
    public Collection<?> getCollection() {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return Leads.size();
    }

    public enum Mode {
        TOP_100, TARRIF, LOCATION, RANGE
    }

    class DefaultersTopHolder extends BaseViewHolder<LayoutStackRequestBinding> {

        DefaultersTopHolder(@NonNull ViewDataBinding itemBinding) {
            super(itemBinding);
        }

        @Override
        public void onBind(int position) {
            //  binding.moreView.setVisibility(View.GONE);
            binding.moreCase.setVisibility(View.VISIBLE);
           /* if (position==0){

            }else {*/
            if (position % 2 == 0) {
                binding.getRoot().setBackgroundColor(Color.parseColor("#EBEBEB"));
            } else {
                binding.getRoot().setBackgroundColor(Color.WHITE);
            }
            binding.tvId.setText("" + Leads.get(position).getStackId());
            binding.tvName.setText(Leads.get(position).getUserName());
            binding.tvPhone.setText(Leads.get(position).getDriverNumber());
            binding.tvCommodity.setText(Leads.get(position).getCommodity());
            binding.tvVehicle.setText(Leads.get(position).getVehicleNumber());
            binding.tvStatus.setText(Leads.get(position).getStackNumber());

            binding.tvId.setTextColor(Color.BLACK);
            binding.tvName.setTextColor(Color.BLACK);
            binding.tvPhone.setTextColor(Color.BLACK);

            binding.cellContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CaseIDGenerateClass.class);
                    intent.putExtra("inwards_stack", Leads.get(position));
                    context.startActivity(intent);
                }
            });


//            activity.hideDialog();
//            binding.viewCase.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (context instanceof CaseListingActivity) {
//                        ((CaseListingActivity) context).ViewData(position);
//                    }
//                }
//            });

        }
        //}
    }

}