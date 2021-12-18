package com.apnagodam.staff.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Base.BaseRecyclerViewAdapter;
import com.apnagodam.staff.Base.BaseViewHolder;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.first_kantaparchi.FirstkanthaParchiListingActivity;
import com.apnagodam.staff.activity.in.gatepass.GatePassListingActivity;
import com.apnagodam.staff.databinding.GatePassItmsUploadBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.FirstkanthaParchiListResponse;
import com.apnagodam.staff.module.GatePassListResponse;

import java.util.Collection;
import java.util.List;

public class GatepassAdapter extends BaseRecyclerViewAdapter {
    private List<GatePassListResponse.Datum> Leads;
    private Context context;
    private BaseActivity activity;
    public GatepassAdapter(List<GatePassListResponse.Datum> leads, GatePassListingActivity gatePassListingActivity, BaseActivity activity) {
        this.Leads = leads;
        this.context = gatePassListingActivity;
        this.activity = activity;
    }

    @Override
    public BaseViewHolder inflateLayout(ViewDataBinding view) {
        return new DefaultersTopHolder(view);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.gate_pass_itms_upload;
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

    class DefaultersTopHolder extends BaseViewHolder<GatePassItmsUploadBinding> {

        DefaultersTopHolder(@NonNull ViewDataBinding itemBinding) {
            super(itemBinding);
        }

        @Override
        public void onBind(int position) {

            if (position % 2 == 0) {
                binding.getRoot().setBackgroundColor(Color.parseColor("#EBEBEB"));
            } else {
                binding.getRoot().setBackgroundColor(Color.WHITE);
            }
            binding.tvId.setText("" + Leads.get(position).getCaseId());
            binding.tvName.setText(Leads.get(position).getCustFname());

            if (Leads.get(position).getGPCaseId()!=null){
                binding.tvPhone.setVisibility(View.GONE);
                binding.txtMybids.setVisibility(View.GONE);
                binding.view12.setVisibility(View.GONE);
                binding.tvPhoneDone.setVisibility(View.VISIBLE);
            }else {
                binding.tvPhone.setText(context.getResources().getString(R.string.update_gate_pass));
                binding.tvPhone.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                setAllData(binding.tvPhone,binding.tvPhoneDone,position,binding.txtMybids,binding.view12);
            }

            binding.tvId.setTextColor(Color.BLACK);
            binding.tvName.setTextColor(Color.BLACK);
            binding.tvPhone.setTextColor(Color.BLACK);

            activity.hideDialog();
            binding.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof GatePassListingActivity) {
                        ((GatePassListingActivity) context).ViewData(position);
                    }
                }
            });

            binding.txtMybids.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof GatePassListingActivity) {
                        ((GatePassListingActivity) context).updateGatepassData(position);
                    }
                }
            });
            binding.tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof GatePassListingActivity) {
                        ((GatePassListingActivity) context).checkVeehicleNo(position);
                    }
                }
            });
        }
    }
    private void setAllData(TextView tvPhone, TextView tvPhoneDone, int position, TextView updateBagWeight, View view) {
        new Thread() {
            public void run() {
                try {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          /*  new Thread() {
                                public void run() {*/
                            for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("19")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getEdit() == 1) {
                                        if (Leads.get(position).getIvr_case_id()!=null){
                                                if (Leads.get(position).getPer_gatepass_case_id()!=null){
                                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("102")) {
                                                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getEdit() == 1) {
                                                            updateBagWeight.setVisibility(View.VISIBLE);
                                                            view.setVisibility(View.GONE);
                                                            tvPhone.setVisibility(View.GONE);
                                                            tvPhoneDone.setVisibility(View.GONE);
                                                        }else {
                                                            /* pending in approve */
                                                            updateBagWeight.setVisibility(View.GONE);
                                                            tvPhone.setVisibility(View.GONE);
                                                            view.setVisibility(View.GONE);
                                                            tvPhoneDone.setVisibility(View.VISIBLE);
                                                            tvPhoneDone.setText("pending in approve...");
                                                            tvPhoneDone .setTextColor(context.getResources().getColor(R.color.yellow));
                                                        }
                                                    }
                                              /*  updateBagWeight.setVisibility(View.VISIBLE);
                                                    view.setVisibility(View.GONE);
                                                tvPhone.setVisibility(View.GONE);*/
                                            }else {
                                                tvPhone.setVisibility(View.VISIBLE);
                                                    view.setVisibility(View.GONE);
                                                updateBagWeight.setVisibility(View.GONE);
                                            }
                                        }else {
                                            updateBagWeight.setVisibility(View.GONE);
                                            tvPhone.setVisibility(View.GONE);
                                            view.setVisibility(View.GONE);
                                            tvPhoneDone.setVisibility(View.VISIBLE);
                                            tvPhoneDone.setText("Processing...");
                                            tvPhoneDone .setTextColor(context.getResources().getColor(R.color.yellow));
                                        }
                                    }else {
                                        updateBagWeight.setVisibility(View.GONE);
                                        tvPhone.setVisibility(View.GONE);
                                        view.setVisibility(View.GONE);
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
