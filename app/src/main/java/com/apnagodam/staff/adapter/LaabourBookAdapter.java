package com.apnagodam.staff.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.apnagodam.staff.Base.BaseRecyclerViewAdapter;
import com.apnagodam.staff.Base.BaseViewHolder;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.labourbook.LabourBookListingActivity;
import com.apnagodam.staff.activity.in.truckbook.TruckBookListingActivity;
import com.apnagodam.staff.databinding.PricingDataBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.AllLabourBookListResponse;
import com.apnagodam.staff.module.AllTruckBookListResponse;

import java.util.Collection;
import java.util.List;

public class LaabourBookAdapter extends BaseRecyclerViewAdapter {
    private List<AllLabourBookListResponse.CurrentPageCollection> Leads;
    private Context context;

    public LaabourBookAdapter(List<AllLabourBookListResponse.CurrentPageCollection> leads, LabourBookListingActivity labourBookListingActivity) {
        this.Leads = leads;
        this.context = labourBookListingActivity;
    }

    @Override
    public BaseViewHolder inflateLayout(ViewDataBinding view) {
        return new DefaultersTopHolder(view);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.pricing_data;
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

    class DefaultersTopHolder extends BaseViewHolder<PricingDataBinding> {

        DefaultersTopHolder(@NonNull ViewDataBinding itemBinding) {
            super(itemBinding);
        }

        @Override
        public void onBind(int position) {
            binding.tvActionDone.setVisibility(View.GONE);
            binding.tvAction.setVisibility(View.GONE);
           /* if (position==0){

            }else {*/
            if (position % 2 == 0) {
                binding.getRoot().setBackgroundColor(Color.parseColor("#EBEBEB"));
            } else {
                binding.getRoot().setBackgroundColor(Color.WHITE);
            }
            binding.tvId.setText("" + Leads.get(position).getCaseId());
            binding.tvName.setText(Leads.get(position).getCustFname());
            if (Leads.get(position).getLBCaseId()!=null){
                binding.tvAction.setVisibility(View.GONE);
                binding.tvActionDone.setVisibility(View.GONE);
                binding.tvPhone.setVisibility(View.GONE);
                binding.tvPhoneDone.setVisibility(View.VISIBLE);
            }else {
                binding.tvAction.setVisibility(View.GONE);
                binding.tvPhone.setText(context.getResources().getString(R.string.upload_labour));
                binding.tvPhone.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("16")) {
                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getEdit() == 1) {
                            if (Leads.get(position).getTrukBookCaseId()!=null){
                                binding.tvPhone.setVisibility(View.VISIBLE);
                            }else {
                                binding.tvPhone.setVisibility(View.GONE);
                                binding.tvPhoneDone.setVisibility(View.VISIBLE);
                                binding.tvPhoneDone.setText("Processing...");
                                binding.tvPhoneDone .setTextColor(context.getResources().getColor(R.color.yellow));
                            }
                        }else {
                            binding.tvPhone.setVisibility(View.GONE);
                            binding.tvPhoneDone.setVisibility(View.VISIBLE);
                            binding.tvPhoneDone.setText("In Process");
                            binding.tvPhoneDone .setTextColor(context.getResources().getColor(R.color.lead_btn));
                        }
                    }
                }
            }
            binding.tvId.setTextColor(Color.BLACK);
            binding.tvName.setTextColor(Color.BLACK);
            binding.tvPhone.setTextColor(Color.BLACK);
            binding.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof LabourBookListingActivity) {
                        ((LabourBookListingActivity) context).ViewData(position);
                    }
                }
            });
            binding.tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof LabourBookListingActivity) {
                        ((LabourBookListingActivity) context).checkVeehicleNo(position);
                    }
                }
            });
        }
    }

}
