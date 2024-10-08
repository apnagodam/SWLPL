package com.apnagodam.staff.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Base.BaseRecyclerViewAdapter;
import com.apnagodam.staff.Base.BaseViewHolder;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.first_quality_reports.FirstQualityReportListingActivity;
import com.apnagodam.staff.activity.in.truckbook.TruckBookListingActivity;
import com.apnagodam.staff.databinding.PricingDataBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.AllTruckBookListResponse;
import com.apnagodam.staff.module.FirstQuilityReportListResponse;

import java.util.Collection;
import java.util.List;

public class FirstQualityReportAdapter extends BaseRecyclerViewAdapter {
    private List<FirstQuilityReportListResponse.Datum> Leads;
    private Context context;
    private BaseActivity activity;
    public FirstQualityReportAdapter(List<FirstQuilityReportListResponse.Datum> leads, FirstQualityReportListingActivity firstQualityReportListingActivity, BaseActivity activity) {
        this.Leads = leads;
        this.context = firstQualityReportListingActivity;
        this.activity = activity;
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
            if (Leads.get(position).getQRCaseId()!=null){
                binding.tvAction.setVisibility(View.GONE);
                binding.tvActionDone.setVisibility(View.GONE);
                binding.tvPhone.setVisibility(View.GONE);
                binding.tvPhoneDone.setVisibility(View.VISIBLE);
            }else {
                binding.tvAction.setVisibility(View.GONE);
                binding.tvPhone.setText(context.getResources().getString(R.string.update_quality));
                binding.tvPhone.setBackgroundColor(context.getResources().getColor(R.color.lead_btn));
                setAllData(binding.tvPhone,binding.tvPhoneDone,position);

            }
            binding.tvId.setTextColor(Color.BLACK);
            binding.tvName.setTextColor(Color.BLACK);
            binding.tvPhone.setTextColor(Color.BLACK);
            activity.hideDialog();
            binding.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof FirstQualityReportListingActivity) {
                        ((FirstQualityReportListingActivity) context).ViewData(position);
                    }
                }
            });
            binding.tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof FirstQualityReportListingActivity) {
                        ((FirstQualityReportListingActivity) context).checkVeehicleNo(position);
                    }
                }
            });
        }
    }
    private void setAllData(TextView tvPhone, TextView tvPhoneDone, int position) {
        new Thread() {
            public void run() {
                try {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          /*  new Thread() {
                                public void run() {*/
                            for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("28")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getEdit() == 1) {
                                        if (Leads.get(position).getFKPCaseId()!=null){
                                            tvPhone.setVisibility(View.VISIBLE);
                                        }else {
                                            tvPhone.setVisibility(View.GONE);
                                            tvPhoneDone.setVisibility(View.VISIBLE);
                                            tvPhoneDone.setText("Processing...");
                                            tvPhoneDone .setTextColor(context.getResources().getColor(R.color.yellow));
                                        }
                                    }else {
                                        tvPhone.setVisibility(View.GONE);
                                        tvPhoneDone.setVisibility(View.VISIBLE);
                                        tvPhoneDone.setText("In Process");
                                        tvPhoneDone .setTextColor(context.getResources().getColor(R.color.lead_btn));
                                    }
                                }
                            }

                            /*    }
                            }.start();*/
                        }
                    });
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
