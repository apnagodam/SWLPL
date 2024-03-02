package com.apnagodam.staff.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Base.BaseRecyclerViewAdapter;
import com.apnagodam.staff.Base.BaseViewHolder;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.caseid.CaseListingActivity;
import com.apnagodam.staff.activity.in.first_kantaparchi.FirstkanthaParchiListingActivity;
import com.apnagodam.staff.activity.in.first_kantaparchi.UploadFirstkantaParchiClass;
import com.apnagodam.staff.activity.in.first_quality_reports.FirstQualityReportListingActivity;
import com.apnagodam.staff.activity.in.first_quality_reports.UploadFirstQualtityReportsClass;
import com.apnagodam.staff.activity.in.labourbook.LabourBookListingActivity;
import com.apnagodam.staff.activity.in.labourbook.LabourBookUploadClass;
import com.apnagodam.staff.activity.in.secound_kanthaparchi.SecoundkanthaParchiListingActivity;
import com.apnagodam.staff.activity.in.secound_kanthaparchi.UploadSecoundkantaParchiClass;
import com.apnagodam.staff.activity.in.secound_quality_reports.SecoundQualityReportListingActivity;
import com.apnagodam.staff.activity.in.secound_quality_reports.UploadSecoundQualtityReportsClass;
import com.apnagodam.staff.activity.in.truckbook.TruckBookListingActivity;
import com.apnagodam.staff.activity.in.truckbook.TruckUploadDetailsClass;
import com.apnagodam.staff.activity.out.f_katha_parchi.OutUploadFirrstkantaParchiClass;
import com.apnagodam.staff.activity.out.f_quailty_report.UploadOutFirstQualtityReportsClass;
import com.apnagodam.staff.activity.out.labourbook.OUTLabourBookUploadClass;
import com.apnagodam.staff.activity.out.s_katha_parchi.OutSecoundkanthaParchiListingActivity;
import com.apnagodam.staff.activity.out.s_katha_parchi.OutUploadSecoundkantaParchiClass;
import com.apnagodam.staff.activity.out.s_quaility_report.OutUploadSecoundQualtityReportsClass;
import com.apnagodam.staff.activity.out.truckbook.OUTTruckUploadDetailsClass;
import com.apnagodam.staff.databinding.LayoutTopCaseGenerateBinding;
import com.apnagodam.staff.module.AllCaseIDResponse;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CasesTopAdapter extends BaseRecyclerViewAdapter {
    private List<AllCaseIDResponse.Datum> Leads;
    private Context context;
    private BaseActivity activity;

    public CasesTopAdapter(List<AllCaseIDResponse.Datum> leads, Activity caseListingActivity) {
        this.Leads = leads;
        this.context = caseListingActivity;
        this.activity = activity;
    }

    @Override
    public BaseViewHolder inflateLayout(ViewDataBinding view) {
        return new DefaultersTopHolder(view);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.layout_top_case_generate;
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

    class DefaultersTopHolder extends BaseViewHolder<LayoutTopCaseGenerateBinding> {

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
            binding.tvId.setText("" + Leads.get(position).getCaseId());
            binding.tvName.setText(Leads.get(position).getCustFname());
            binding.tvPhone.setText(Leads.get(position).getPhone());

            binding.tvVehicle.setText(Leads.get(position).getVehicleNo());
            binding.tvId.setTextColor(Color.BLACK);
            binding.tvName.setTextColor(Color.BLACK);
            binding.tvPhone.setTextColor(Color.BLACK);
            if (Objects.equals(Leads.get(position).getInOut(), "IN")) {
                if (Leads.get(position).getTruckbook() == null) {
                    binding.tvStatus.setText("Add Truck");

                    binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, TruckUploadDetailsClass.class);
                            intent.putExtra("user_name", Leads.get(position).getCustFname());
                            intent.putExtra("case_id", Leads.get(position).getCaseId());
                            intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
                            intent.putExtra("file3", Leads.get(position).getFirstKantaFile3());
                            context.startActivity(intent);

                        }
                    });

                } else {
                    if (Leads.get(position).getLabourBook() == null) {

                        binding.tvStatus.setText("Add Labour");
                        binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, LabourBookUploadClass.class);
                                intent.putExtra("user_name", Leads.get(position).getCustFname());
                                intent.putExtra("case_id", Leads.get(position).getCaseId());
                                intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
                                intent.putExtra("file3", Leads.get(position).getFirstKantaFile3());
                                context.startActivity(intent);


                            }
                        });

                    } else {
                        if (Leads.get(position).getFirstKantaParchi() == null || Leads.get(position).getFirstKantaDharamkanta() == null) {
                            binding.tvStatus.setText("Add First Kanta Parchi");

                            binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, UploadFirstkantaParchiClass.class);
                                    intent.putExtra("user_name", Leads.get(position).getCustFname());
                                    intent.putExtra("case_id", Leads.get(position).getCaseId());
                                    intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
                                    intent.putExtra("file3", Leads.get(position).getFirstKantaFile3());
                                    context.startActivity(intent);
                                }
                            });
                        } else {
                            if (Leads.get(position).getFirstQuality() == null) {
                                binding.tvStatus.setText("Add First Quality");
                                binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, UploadFirstQualtityReportsClass.class);
                                        intent.putExtra("user_name", Leads.get(position).getCustFname());
                                        intent.putExtra("case_id", Leads.get(position).getCaseId());
                                        intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
                                        context.startActivity(intent);

                                    }
                                });

                            } else {
                                if (Leads.get(position).getSecondKantaParchi() == null || Leads.get(position).getSecondKantaDharamkanta() == null) {
                                    binding.tvStatus.setText("Add Second Kanta Parchi");

                                    binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(context, UploadSecoundkantaParchiClass.class);
                                            intent.putExtra("user_name", Leads.get(position).getCustFname());
                                            intent.putExtra("case_id", Leads.get(position).getCaseId());
                                            intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
                                            intent.putExtra("file3", Leads.get(position).getSecondKantaFile3());
                                            context.startActivity(intent);

                                        }
                                    });
                                } else {
                                    if (Leads.get(position).getSecondQualityReport() == null) {
                                        binding.tvStatus.setText("Add Second Quality Report");
                                        binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(context, UploadSecoundQualtityReportsClass.class);
                                                intent.putExtra("user_name", Leads.get(position).getCustFname());
                                                intent.putExtra("case_id", Leads.get(position).getCaseId());
                                                intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
                                                intent.putExtra("file3", Leads.get(position).getFirstKantaFile3());
                                                context.startActivity(intent);

                                            }
                                        });
                                    }
                                    else {

                                        if (Leads.get(position).getIvrReport() == null) {
                                            binding.tvStatus.setText("IVR Pending");
                                        } else {
                                            if (Leads.get(position).getCctvReport() == null) {
                                                binding.tvStatus.setText("CCTV Pending");
                                            } else {
                                                if (Leads.get(position).getGatepassReport() == null) {
                                                    binding.tvStatus.setText("Gate Pass Pending");
                                                } else {
                                                    binding.tvStatus.setText("Done");
                                                }
                                            }
                                        }

                                    }

                                }
                            }
                        }
                    }
                }
            } else {
                if (Leads.get(position).getTruckbook() == null) {
                    binding.tvStatus.setText("Add Out Truck");

                    binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, OUTTruckUploadDetailsClass.class);
                            intent.putExtra("user_name", Leads.get(position).getCustFname());
                            intent.putExtra("case_id", Leads.get(position).getCaseId());
                            intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
                            intent.putExtra("file3", Leads.get(position).getFirstKantaFile3());
                            context.startActivity(intent);

                        }
                    });

                } else {
                    if (Leads.get(position).getLabourBook() == null) {
                        binding.tvStatus.setText("Add Out Labour");

                        binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, OUTLabourBookUploadClass.class);
                                intent.putExtra("user_name", Leads.get(position).getCustFname());
                                intent.putExtra("case_id", Leads.get(position).getCaseId());
                                intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
                                intent.putExtra("file3", Leads.get(position).getFirstKantaFile3());
                                context.startActivity(intent);


                            }
                        });

                    } else {
                        if (Leads.get(position).getFirstQuality() == null) {
                            binding.tvStatus.setText("Add Out First Quality Report");

                            binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, UploadOutFirstQualtityReportsClass.class);
                                    intent.putExtra("user_name", Leads.get(position).getCustFname());
                                    intent.putExtra("case_id", Leads.get(position).getCaseId());
                                    intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
                                    intent.putExtra("file3", Leads.get(position).getFirstKantaFile3());
                                    context.startActivity(intent);

                                }
                            });
                        } else {
                            if (Leads.get(position).getFirstKantaParchi() == null || Leads.get(position).getFirstKantaDharamkanta() == null) {
                                binding.tvStatus.setText("Add Out First Kanta Parchi");
                                binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, OutUploadFirrstkantaParchiClass.class);
                                        intent.putExtra("user_name", Leads.get(position).getCustFname());
                                        intent.putExtra("case_id", Leads.get(position).getCaseId());
                                        intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
                                        intent.putExtra("file3", Leads.get(position).getFirstKantaFile3());

                                        context.startActivity(intent);

                                    }
                                });

                            } else {
                                if (Leads.get(position).getSecondQualityReport() == null) {
                                    binding.tvStatus.setText("Add Out Second Quality Report");

                                    binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(context, OutUploadSecoundQualtityReportsClass.class);
                                            intent.putExtra("user_name", Leads.get(position).getCustFname());
                                            intent.putExtra("case_id", Leads.get(position).getCaseId());
                                            intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
                                            intent.putExtra("file3", Leads.get(position).getSecondKantaFile3());
                                            context.startActivity(intent);

                                        }
                                    });
                                } else {
                                    if (Leads.get(position).getSecondKantaParchi() == null || Leads.get(position).getSecondKantaDharamkanta() == null) {
                                        binding.tvStatus.setText("Add Out Second Kanta Parchi");

                                        binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(context, OutUploadSecoundkantaParchiClass.class);
                                                intent.putExtra("user_name", Leads.get(position).getCustFname());
                                                intent.putExtra("case_id", Leads.get(position).getCaseId());
                                                intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
                                                intent.putExtra("file3", Leads.get(position).getSecondKantaFile3());
                                                context.startActivity(intent);

                                            }
                                        });
                                    }
                                    else {
                                        if (Leads.get(position).getCctvReport() == null) {
                                            binding.tvStatus.setText("CCTV Pending");
                                        } else {

                                            if (Leads.get(position).getIvrReport() == null) {
                                                binding.tvStatus.setText("IVR Pending");
                                            } else {
                                                if (Leads.get(position).getGatepassReport() == null) {
                                                    binding.tvStatus.setText("Gate Pass Pending");
                                                } else {
                                                    binding.tvStatus.setText("Done");
                                                }
                                            }
                                        }

                                    }

                                }
                            }
                        }
                    }
                }
            }


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
