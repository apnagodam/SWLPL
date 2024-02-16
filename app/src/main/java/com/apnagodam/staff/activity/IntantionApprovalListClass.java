package com.apnagodam.staff.activity;

import android.app.Activity;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.ApprovedIntantionPOst;
import com.apnagodam.staff.Network.Request.SelfRejectConveyancePOst;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.convancy_voachar.ApprovalRequestConveyanceListClass;
import com.apnagodam.staff.activity.convancy_voachar.UploadConveyanceVoacharClass;
import com.apnagodam.staff.activity.in.pricing.InPricingListingActivity;
import com.apnagodam.staff.adapter.IntantionListAdpter;
import com.apnagodam.staff.databinding.ConvencyListBinding;
import com.apnagodam.staff.databinding.IntantionListBinding;
import com.apnagodam.staff.module.AllConvancyList;
import com.apnagodam.staff.module.AllIntantionList;
import com.apnagodam.staff.module.AllLevelEmpListPojo;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class IntantionApprovalListClass extends BaseActivity<IntantionListBinding> {
    private IntantionListAdpter intantionListAdpter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<AllIntantionList.Datum> getOrdersList;
    private List<AllIntantionList.Datum> tempraryList;
    private String inventory_id = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.intantion_list;
    }

    @Override
    protected void setUp() {
        getOrdersList = new ArrayList();
        tempraryList = new ArrayList();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(StaffDashBoardActivity.class);
            }
        });
        setDataAdapter();
        getConvencyList("");
        binding.tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageOffset != 1) {
                    pageOffset--;
                    getConvencyList("");
                }
            }
        });
        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalPage != pageOffset) {
                    pageOffset++;
                    getConvencyList("");
                }
            }
        });

        binding.swipeRefresherStock.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getConvencyList("");
            }
        });
        binding.filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IntantionApprovalListClass.this);
                LayoutInflater inflater = ((Activity) IntantionApprovalListClass.this).getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.fiter_diloag, null);
                EditText notes = (EditText) dialogView.findViewById(R.id.notes);
                Button submit = (Button) dialogView.findViewById(R.id.btn_submit);
                ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
                builder.setView(dialogView);
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (notes.getText().toString().trim() != null && !notes.getText().toString().trim().isEmpty()) {
                            alertDialog.dismiss();
                            pageOffset = 1;
                            getConvencyList(notes.getText().toString().trim());
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Fill Text", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void getConvencyList(String SerachKey) {
        showDialog();
        apiService.getintentionList("15", pageOffset, SerachKey).enqueue(new NetworkCallback<AllIntantionList>(getActivity()) {
            @Override
            protected void onSuccess(AllIntantionList body) {
                tempraryList.clear();
                getOrdersList.clear();
                binding.swipeRefresherStock.setRefreshing(false);
                if (body.getData().getDataa().size() == 0) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.fieldStockList.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } else if (body.getData().getLastPage() == 1) {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.fieldStockList.setVisibility(View.VISIBLE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                    getOrdersList.clear();
                    tempraryList.clear();
                    totalPage = body.getData().getLastPage();
                    getOrdersList.addAll(body.getData().getDataa());
                    tempraryList.addAll(getOrdersList);
                    intantionListAdpter.notifyDataSetChanged();
                } else {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.fieldStockList.setVisibility(View.VISIBLE);
                    binding.pageNextPrivious.setVisibility(View.VISIBLE);
                    getOrdersList.clear();
                    tempraryList.clear();
                    totalPage = body.getData().getLastPage();
                    getOrdersList.addAll(body.getData().getDataa());
                    tempraryList.addAll(getOrdersList);
                    intantionListAdpter.notifyDataSetChanged();
                }

            }
        });
    }

    private void setDataAdapter() {
        binding.fieldStockList.addItemDecoration(new DividerItemDecoration(IntantionApprovalListClass.this, LinearLayoutManager.HORIZONTAL));
        binding.fieldStockList.setHasFixedSize(true);
        binding.fieldStockList.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(IntantionApprovalListClass.this, LinearLayoutManager.VERTICAL, false);
        binding.fieldStockList.setLayoutManager(horizontalLayoutManager);
        intantionListAdpter = new IntantionListAdpter(getOrdersList, IntantionApprovalListClass.this, getActivity());
        binding.fieldStockList.setAdapter(intantionListAdpter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(StaffDashBoardActivity.class);

    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(IntantionApprovalListClass.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = IntantionApprovalListClass.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.intantion_view, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = dialogView.findViewById(R.id.cancel_btn);
        TextView date = dialogView.findViewById(R.id.genrated_by);
        TextView Terminal = dialogView.findViewById(R.id.customer_name);
        TextView customer_name = dialogView.findViewById(R.id.phone_no);
        TextView mobile = dialogView.findViewById(R.id.location_title);
        TextView coustomer_address = dialogView.findViewById(R.id.commodity_name);
        TextView commodity_name = dialogView.findViewById(R.id.est_quantity_nmae);
        TextView packingType = dialogView.findViewById(R.id.terminal_name);
        TextView totalBags = dialogView.findViewById(R.id.purpose_name);
        TextView weightQtl = dialogView.findViewById(R.id.commitemate_date);
        TextView sellingPrice = dialogView.findViewById(R.id.create_date);
        TextView quality_grade = dialogView.findViewById(R.id.converted_by);
        TextView gst = dialogView.findViewById(R.id.vehicle_no);
        TextView coustomer_gsst_no = dialogView.findViewById(R.id.in_out);
        TextView finalPprice = dialogView.findViewById(R.id.gate_passs);
        TextView paymentstatus = dialogView.findViewById(R.id.start_date_time);
        inventory_id = "" + getOrdersList.get(position).getId();
        date.setText("" + ((getOrdersList.get(position).getUpdatedAt()) != null ? getOrdersList.get(position).getUpdatedAt() : "N/A"));
        Terminal.setText("" + ((getOrdersList.get(position).getWarehouseName()) != null ? getOrdersList.get(position).getWarehouseName() : "N/A"));
        customer_name.setText("" + ((getOrdersList.get(position).getFname()) != null ? getOrdersList.get(position).getFname() : "N/A"));
        mobile.setText("" + ((getOrdersList.get(position).getPhone()) != null ? getOrdersList.get(position).getPhone() : "N/A"));
        coustomer_address.setText("" + ((getOrdersList.get(position).getArea_vilage()) != null ? getOrdersList.get(position).getArea_vilage() + " " + getOrdersList.get(position).getCity() + " " + getOrdersList.get(position).getState() : "N/A"));
        commodity_name.setText("" + ((getOrdersList.get(position).getCategory()) != null ? getOrdersList.get(position).getCategory() : "N/A"));
        packingType.setText("" + ((getOrdersList.get(position).getPacking()) != null ? getOrdersList.get(position).getPacking() : "N/A"));
        totalBags.setText("" + ((getOrdersList.get(position).getTotalBags()) != null ? getOrdersList.get(position).getTotalBags() : "N/A"));
        weightQtl.setText("" + ((getOrdersList.get(position).getWeight()) != null ? getOrdersList.get(position).getWeight() : "N/A"));
        sellingPrice.setText("" + ((getOrdersList.get(position).getSellPrice()) != null ? getOrdersList.get(position).getSellPrice() : "N/A"));
        quality_grade.setText("" + ((getOrdersList.get(position).getQualityGrade()) != null ? getOrdersList.get(position).getQualityGrade() : "N/A"));
        gst.setText("" + ((getOrdersList.get(position).getGstType()) != null ? "With " + getOrdersList.get(position).getGstType() : " Without GST "));
        coustomer_gsst_no.setText("" + ((getOrdersList.get(position).getGstNo()) != null ? getOrdersList.get(position).getGstNo() : "N/A"));
        finalPprice.setText("" + ((getOrdersList.get(position).getTotalAmount()) != null ? getOrdersList.get(position).getTotalAmount() : "N/A"));
        paymentstatus.setText("" + ((getOrdersList.get(position).getPaymentMode()) != null ? getOrdersList.get(position).getPaymentMode() : "N/A"));

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void cancelIntantion(int position) {
        Utility.showDecisionDialog(IntantionApprovalListClass.this, getString(R.string.alert), getResources().getString(R.string.alert_reject_int), new Utility.AlertCallback() {
            @Override
            public void callback() {
                inventory_id = "" + getOrdersList.get(position).getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(IntantionApprovalListClass.this);
                LayoutInflater inflater = ((Activity) IntantionApprovalListClass.this).getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.close_pricicng_diloag, null);
                TextView title = (TextView) dialogView.findViewById(R.id.name);
                TextView title_header = (TextView) dialogView.findViewById(R.id.title_header);
                EditText notes = (EditText) dialogView.findViewById(R.id.notes);
                Button submit = (Button) dialogView.findViewById(R.id.btn_submit);
                ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
                builder.setView(dialogView);
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                title.setVisibility(View.GONE);
                title_header.setText("Reject Intention");
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (notes.getText().toString().trim() != null && !notes.getText().toString().trim().isEmpty()) {
                            apiService.rejeectIntantion(new ApprovedIntantionPOst(inventory_id,notes.getText().toString().trim())).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                                @Override
                                protected void onSuccess(LoginResponse body) {
                                    Toast.makeText(IntantionApprovalListClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                                    alertDialog.dismiss();
                                    getConvencyList("");
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Fill notes", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    public void ApproveIntantion(int position) {
        Utility.showDecisionDialog(IntantionApprovalListClass.this, getString(R.string.alert), getResources().getString(R.string.alert_appprove_int), new Utility.AlertCallback() {
            @Override
            public void callback() {
                inventory_id = "" + getOrdersList.get(position).getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(IntantionApprovalListClass.this);
                LayoutInflater inflater = ((Activity) IntantionApprovalListClass.this).getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.close_pricicng_diloag, null);
                TextView title = (TextView) dialogView.findViewById(R.id.name);
                TextView title_header = (TextView) dialogView.findViewById(R.id.title_header);
                EditText notes = (EditText) dialogView.findViewById(R.id.notes);
                Button submit = (Button) dialogView.findViewById(R.id.btn_submit);
                ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
                builder.setView(dialogView);
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                title.setVisibility(View.GONE);
                title_header.setText("Approve Intention");
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        apiService.ApproveIntantion(new ApprovedIntantionPOst(inventory_id,notes.getText().toString().trim())).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                            @Override
                            protected void onSuccess(LoginResponse body) {
                                Toast.makeText(IntantionApprovalListClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();
                                getConvencyList("");
                            }
                        });
                        /*if (notes.getText().toString().trim() != null && !notes.getText().toString().trim().isEmpty()) {
                            ApproveRejectIntantion(alertDialog, inventory_id, notes.getText().toString().trim());
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Fill notes", Toast.LENGTH_LONG).show();
                        }*/
                    }
                });
            }
        });
    }

    private void ApproveRejectIntantion(AlertDialog alertDialog, String inventory_id, String trim) {
//        apiService.empyConveyanceDelate(new SelfRejectConveyancePOst(inventory_id))
//
//                .enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
//            @Override
//            protected void onSuccess(LoginResponse body) {
//                Toast.makeText(IntantionApprovalListClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
//                alertDialog.dismiss();
//                getConvencyList("");
//            }
//        });
    }
}
