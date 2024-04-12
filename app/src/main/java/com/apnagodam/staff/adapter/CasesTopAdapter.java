package com.apnagodam.staff.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Base.BaseRecyclerViewAdapter;
import com.apnagodam.staff.Base.BaseViewHolder;
import com.apnagodam.staff.Network.ApiService;
import com.apnagodam.staff.Network.Request.UploadFirstQualityPostData;
import com.apnagodam.staff.Network.Request.UploadSecoundQualityPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.GatePassPDFPrieviewClass;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.caseid.CaseListingActivity;
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
import com.apnagodam.staff.databinding.LayoutTopCaseGenerateBinding;
import com.apnagodam.staff.module.AllCaseIDResponse;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CasesTopAdapter extends BaseRecyclerViewAdapter {
    private List<AllCaseIDResponse.Datum> Leads;
    private Context context;
    private BaseActivity activity;
    private ApiService apiService;

    public CasesTopAdapter(List<AllCaseIDResponse.Datum> leads, Activity caseListingActivity, ApiService apiService) {
        this.Leads = leads;
        this.context = caseListingActivity;
        this.activity = activity;
        this.apiService = apiService;
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
            if (context instanceof StaffDashBoardActivity) {
                binding.llCaseId.setVisibility(View.VISIBLE);
                binding.tvCaseId.setText("" + Leads.get(position).getCaseId());
                binding.tvId.setText(Leads.get(position).getStack_number());

            } else {
                binding.llCaseId.setVisibility(View.GONE);
                binding.tvId.setText(Leads.get(position).getCaseId());
            }
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
                                if (Leads.get(position).getFirstQualityTagging() == null) {
                                    binding.tvStatus.setText("IVR Quality Tagging Pending");
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
                                        if (Leads.get(position).getSecondQualityReport() == null || Leads.get(position).getSendToLab() == null) {
                                            binding.tvStatus.setText("Add Second Quality Report");

                                            binding.divider.setVisibility(View.VISIBLE);
                                            binding.tvLab.setVisibility(View.VISIBLE);
                                            ArrayList<UploadFirstQualityPostData.CommodityData> commodityData = new ArrayList<>();

                                            binding.tvLab.setOnClickListener(view -> {

                                                Dialog dialog = new Dialog(context);
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                dialog.setCancelable(false);
                                                dialog.setContentView(R.layout.dialog_lab_report);

                                                TextView btYes = dialog.findViewById(R.id.btYes);

                                                TextView btNo = dialog.findViewById(R.id.btNo);
                                                btYes.setOnClickListener((v) -> {
                                                    apiService.uploadLabreport(new UploadSecoundQualityPostData(
                                                                    Leads.get(position).getCaseId(),
                                                                    "", commodityData, "", "", "", "", "", "0", "1"
                                                            ), "OUT").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new Observer<LoginResponse>() {
                                                                @Override
                                                                public void onSubscribe(Disposable d) {

                                                                }

                                                                @Override
                                                                public void onNext(LoginResponse loginResponse) {
                                                                    if (context instanceof StaffDashBoardActivity) {
                                                                        ((StaffDashBoardActivity) context).getdashboardData();
                                                                    }
                                                                    dialog.dismiss();

                                                                }

                                                                @Override
                                                                public void onError(Throwable e) {
                                                                    dialog.dismiss();

                                                                }

                                                                @Override
                                                                public void onComplete() {
                                                                    dialog.dismiss();

                                                                }
                                                            });
                                                });
                                                btNo.setOnClickListener((v) -> {
                                                    dialog.dismiss();
                                                });


                                                dialog.show();

                                            });
                                            binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(context, UploadSecoundQualtityReportsClass.class);
                                                    intent.putExtra("user_name", Leads.get(position).getCustFname());
                                                    intent.putExtra("case_id", Leads.get(position).getCaseId());
                                                    intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
                                                    intent.putExtra("file3", Leads.get(position).getFirstKantaFile3());
                                                    intent.putExtra("skp_avg_weight", Leads.get(position).getAvgWeight());

                                                    context.startActivity(intent);

                                                }
                                            });
                                        } else {
                                            if (Leads.get(position).getCctvReport() == null) {
                                                binding.tvStatus.setText("CCTV Pending");
                                            } else {
                                                if (Leads.get(position).getIvrReport() == null) {
                                                    binding.tvStatus.setText("IVR Pending");
                                                } else {
                                                    if (Leads.get(position).getGatepassReport() == null) {
                                                        binding.tvStatus.setText("Gate Pass Pending");
                                                    } else {
                                                        binding.tvStatus.setText("View");
                                                        binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Intent intent = new Intent(context, GatePassPDFPrieviewClass.class);
                                                                intent.putExtra("CaseID", Leads.get(position).getCaseId());
                                                                context.startActivity(intent);
                                                            }
                                                        });
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
//                        if (Leads.get(position).getFirstQuality() == null) {
//                            binding.tvStatus.setText("Add Out First Quality Report");
//
//                            binding.tvStatus.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, UploadOutFirstQualtityReportsClass.class);
//                                    intent.putExtra("user_name", Leads.get(position).getCustFname());
//                                    intent.putExtra("case_id", Leads.get(position).getCaseId());
//                                    intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
//                                    intent.putExtra("file3", Leads.get(position).getFirstKantaFile3());
//                                    context.startActivity(intent);
//
//                                }
//                            });
//                        } else {
//
//                        }
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
                                binding.tvStatus.setText("Add Out Quality Report");

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
                                } else {
                                    if (Leads.get(position).getCctvReport() == null || Leads.get(position).getCctvReport().isEmpty()) {
                                        binding.tvStatus.setText("CCTV Pending");
                                    } else {
                                        if (Leads.get(position).getIvrReport() == null) {
                                            binding.tvStatus.setText("IVR Pending");
                                        } else {
                                            if (Leads.get(position).getGatepassReport() == null) {
                                                binding.tvStatus.setText("Gate Pass Pending");
                                            } else {
                                                binding.tvStatus.setText("View");
                                                binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(context, GatePassPDFPrieviewClass.class);
                                                        intent.putExtra("CaseID", Leads.get(position).getCaseId());
                                                        intent.putExtra("in_out", Leads.get(position).getInOut());
                                                        intent.putExtra("bags", Leads.get(position).getNoOfBags());
                                                        context.startActivity(intent);

                                                    }
                                                });
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


        }
        //}
    }

}
