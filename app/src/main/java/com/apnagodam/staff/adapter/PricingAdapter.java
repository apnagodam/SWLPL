package com.apnagodam.staff.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Base.BaseRecyclerViewAdapter;
import com.apnagodam.staff.Base.BaseViewHolder;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.pricing.InPricingListingActivity;
import com.apnagodam.staff.databinding.PricingDataBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.AllpricingResponse;

import java.util.Collection;
import java.util.List;

public class PricingAdapter extends RecyclerView.Adapter<PricingAdapter.CommudityResponseViewHolder> {
    private List<AllpricingResponse.Datum> Leads;
    private Context context;
    private BaseActivity activity;

    private String editPer;
    public PricingAdapter(List<AllpricingResponse.Datum> leads, InPricingListingActivity inPricingListingActivity, BaseActivity activity ) {
        this.Leads = leads;
        this.context = inPricingListingActivity;
        this.activity = activity;

    }

    @NonNull
    @Override
    public CommudityResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout file
        View CommudityResponseProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pricing_data, parent, false);
        CommudityResponseViewHolder gvh = new CommudityResponseViewHolder(CommudityResponseProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CommudityResponseViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.cell_container.setBackgroundColor(Color.parseColor("#EBEBEB"));
        } else {
            holder.cell_container.setBackgroundColor(Color.WHITE);
        }
        holder.tv_id.setText("" + Leads.get(position).getCaseId());
        holder.tv_name.setText(Leads.get(position).getCustFname());
        if (Leads.get(position).getPCaseId()!=null){
            holder.tv_action.setVisibility(View.GONE);
            holder.tv_action_done.setVisibility(View.VISIBLE);
            holder.tv_phone.setVisibility(View.GONE);
            holder.tv_phone_done.setVisibility(View.VISIBLE);
        }else {
            for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("14")) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getEdit() == 1) {
                        if (Leads.get(position).getCaseId() != null) {
                            holder.tv_phone.setVisibility(View.VISIBLE);
                            holder.tv_action.setVisibility(View.VISIBLE);
                        } else {
                            holder.tv_action.setVisibility(View.VISIBLE);
                            holder.tv_phone.setVisibility(View.GONE);
                            holder.tv_phone_done.setVisibility(View.VISIBLE);
                            holder.tv_phone_done.setText("Processing...");
                            holder.tv_phone_done.setTextColor(context.getResources().getColor(R.color.yellow));
                        }
                        break;
                    } else {
                        holder.tv_action.setVisibility(View.VISIBLE);
                        holder.tv_phone.setVisibility(View.GONE);
                        holder.tv_phone_done.setVisibility(View.VISIBLE);
                        holder.tv_phone_done.setText("In Process");
                        holder.tv_phone_done.setTextColor(context.getResources().getColor(R.color.lead_btn));
                    }
                    //setAllData(holder.tv_phone, holder.tv_phone_done, holder.tv_action,position);
            /*    for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("13")) {
                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getEdit() == 1) {
                            binding.tvPhone.setVisibility(View.VISIBLE);
                        }else {
                            binding.tvPhone.setVisibility(View.GONE);
                        }
                    }
                }*/
                }
            }
        }

        holder.tv_id.setTextColor(Color.BLACK);
        holder.tv_name.setTextColor(Color.BLACK);
        holder.tv_phone.setTextColor(Color.BLACK);
        activity.hideDialog();
        holder.viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof InPricingListingActivity) {
                    ((InPricingListingActivity) context).ViewData(position);
                }
            }
        });
        holder.tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof InPricingListingActivity) {
                    ((InPricingListingActivity) context).checkVeehicleNo(position);
                }
            }
        });
        holder.tv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof InPricingListingActivity) {
                    ((InPricingListingActivity) context).closedPricing(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Leads.size();
    }

    public enum Mode {
        TOP_100, TARRIF, LOCATION, RANGE
    }


    private void setAllData(TextView tvPhone, TextView tvPhoneDone,  TextView tvAction,int position) {
        new Thread() {
            public void run() {
                try {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          /*  new Thread() {
                                public void run() {*/

                         /*   for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("13")) {*/
                                    if (Integer.parseInt(editPer) == 1) {
                                        if (Leads.get(position).getCaseId()!=null){
                                            tvPhone.setVisibility(View.VISIBLE);
                                            tvAction.setVisibility(View.VISIBLE);
                                        }else {
                                            tvAction.setVisibility(View.VISIBLE);
                                            tvPhone.setVisibility(View.GONE);
                                            tvPhoneDone.setVisibility(View.VISIBLE);
                                            tvPhoneDone.setText("Processing...");
                                            tvPhoneDone.setTextColor(context.getResources().getColor(R.color.yellow));
                                        }
                                    }else {
                                        tvAction.setVisibility(View.VISIBLE);
                                        tvPhone.setVisibility(View.GONE);
                                        tvPhoneDone.setVisibility(View.VISIBLE);
                                        tvPhoneDone.setText("In Process");
                                        tvPhoneDone.setTextColor(context.getResources().getColor(R.color.lead_btn));
                                    }
                              /*  }
                            }*/

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
    public class CommudityResponseViewHolder extends RecyclerView.ViewHolder {
        ImageView viewData;
        TextView tv_id, tv_name, tv_phone, tv_phone_done, tv_action, tv_action_done;
        LinearLayout cell_container;
        public CommudityResponseViewHolder(View view) {
            super(view);
            tv_id = view.findViewById(R.id.tv_id);
            viewData = view.findViewById(R.id.view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_phone = view.findViewById(R.id.tv_phone);
            tv_phone_done = view.findViewById(R.id.tv_phone_done);
            tv_action = view.findViewById(R.id.tv_action);
            tv_action_done = view.findViewById(R.id.tv_action_done);
            cell_container = view.findViewById(R.id.cell_container);

        }
    }

}
