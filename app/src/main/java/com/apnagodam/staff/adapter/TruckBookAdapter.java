package com.apnagodam.staff.adapter;

import android.annotation.SuppressLint;
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
import com.apnagodam.staff.activity.in.pricing.InPricingListingActivity;
import com.apnagodam.staff.activity.in.truckbook.TruckBookListingActivity;
import com.apnagodam.staff.databinding.PricingDataBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.AllTruckBookListResponse;
import com.apnagodam.staff.module.AllpricingResponse;

import java.util.Collection;
import java.util.List;

public class TruckBookAdapter extends BaseRecyclerViewAdapter {

    private List<AllTruckBookListResponse.Datum> Leads;
    private Context context;
    private BaseActivity activity;

    public TruckBookAdapter(List<AllTruckBookListResponse.Datum> leads, TruckBookListingActivity
            truckBookListingActivity, BaseActivity activity) {
        this.Leads = leads;
        this.context = truckBookListingActivity;
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

    class DefaultersTopHolder extends BaseViewHolder<PricingDataBinding> {

        DefaultersTopHolder(@NonNull ViewDataBinding itemBinding) {
            super(itemBinding);
        }

        @SuppressLint("SetTextI18n")
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
            if (Leads.get(position).getTBCaseId()!=null){
                binding.tvAction.setVisibility(View.GONE);
                binding.tvActionDone.setVisibility(View.GONE);
                binding.tvPhone.setVisibility(View.GONE);
                binding.tvPhoneDone.setVisibility(View.VISIBLE);
            }else {
                binding.tvAction.setVisibility(View.GONE);
                binding.tvPhone.setText(context.getResources().getString(R.string.upload_details));
                binding.tvPhone.setBackgroundColor(context.getResources().getColor(R.color.yellow));
               setAllData(binding.tvPhone,binding.tvPhoneDone,position);
             /*    for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                     if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("15")) {
                         if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getEdit() == 1) {
                             if (Leads.get(position).getP_case_id()!=null){
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
                 }*/
            }
            binding.tvId.setTextColor(Color.BLACK);
            binding.tvName.setTextColor(Color.BLACK);
            binding.tvPhone.setTextColor(Color.BLACK);
            activity.hideDialog();
            binding.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof TruckBookListingActivity) {
                        ((TruckBookListingActivity) context).ViewData(position);
                    }
                }
            });
            binding.tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof TruckBookListingActivity) {
                        ((TruckBookListingActivity) context).checkVeehicleNo(position);
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
                                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("15")) {
                                            if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getEdit() == 1) {
                                                if (Leads.get(position).getP_case_id()!=null){
                                                    tvPhone.setVisibility(View.VISIBLE);
                                                }else {
                                                    tvPhone.setVisibility(View.GONE);
                                                    tvPhoneDone.setVisibility(View.VISIBLE);
                                                    tvPhoneDone.setText("Processing...");
                                                    tvPhoneDone.setTextColor(context.getResources().getColor(R.color.yellow));
                                                }
                                            }else {
                                                tvPhone.setVisibility(View.GONE);
                                                tvPhoneDone.setVisibility(View.VISIBLE);
                                                tvPhoneDone.setText("In Process");
                                                tvPhoneDone.setTextColor(context.getResources().getColor(R.color.lead_btn));
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
