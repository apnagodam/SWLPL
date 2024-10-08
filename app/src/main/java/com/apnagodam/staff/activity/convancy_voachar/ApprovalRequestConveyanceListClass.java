package com.apnagodam.staff.activity.convancy_voachar;

import android.app.Activity;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.ApprovedConveyancePOst;
import com.apnagodam.staff.Network.Request.ApprovedRejectConveyancePOst;
import com.apnagodam.staff.Network.Request.ApprovedRejectVendorConveyancePOst;
import com.apnagodam.staff.Network.Request.ApprovedVendorConveyancePOst;
import com.apnagodam.staff.Network.Request.SelfRejectConveyancePOst;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.vendorPanel.ApprovalRequestVendorVoacherListClass;
import com.apnagodam.staff.adapter.ApprovalRequestConvancyListAdpter;
import com.apnagodam.staff.adapter.ConvancyListAdpter;
import com.apnagodam.staff.databinding.ConvencyListBinding;
import com.apnagodam.staff.module.AllConvancyList;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class ApprovalRequestConveyanceListClass extends BaseActivity<ConvencyListBinding> {
    private ApprovalRequestConvancyListAdpter approvalRequestConvancyListAdpter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<AllConvancyList.Datum> getOrdersList;
    private List<AllConvancyList.Datum> tempraryList;
    private String inventory_id = "";
    private String startMeterImage, endMeterImage,otherImaage;
    @Override
    protected int getLayoutResId() {
        return R.layout.convency_list;
    }

    @Override
    protected void setUp() {
        getOrdersList = new ArrayList();
        tempraryList = new ArrayList();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.titleHeader.setText("All Conveyance Request");
        binding.activityMain.setVisibility(View.GONE);
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(StaffDashBoardActivity.class);
            }
        });
        setDataAdapter();
        getApprovalRequestConvencyList("");
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(UploadConveyanceVoacharClass.class);
            }
        });
        binding.tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageOffset != 1) {
                    pageOffset--;
                    getApprovalRequestConvencyList("");
                }
            }
        });
        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalPage != pageOffset) {
                    pageOffset++;
                    getApprovalRequestConvencyList("");
                }
            }
        });

        binding.swipeRefresherStock.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getApprovalRequestConvencyList("");
            }
        });
        binding.filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalRequestConveyanceListClass.this);
                LayoutInflater inflater = ((Activity) ApprovalRequestConveyanceListClass.this).getLayoutInflater();
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
                             getApprovalRequestConvencyList(notes.getText().toString().trim());
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Fill Text", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    private void getApprovalRequestConvencyList(String SerachKey) {
        showDialog();
        apiService.getApprovalRequestConvancyList("15", pageOffset, SerachKey).enqueue(new NetworkCallback<AllConvancyList>(getActivity()) {
            @Override
            protected void onSuccess(AllConvancyList body) {
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
                    totalPage = body.getData().getLastPage();
                    getOrdersList.addAll(body.getData().getDataa());
                    tempraryList.addAll(getOrdersList);
                    approvalRequestConvancyListAdpter.notifyDataSetChanged();
                } else {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.fieldStockList.setVisibility(View.VISIBLE);
                    binding.pageNextPrivious.setVisibility(View.VISIBLE);
                    totalPage = body.getData().getLastPage();
                    getOrdersList.addAll(body.getData().getDataa());
                    tempraryList.addAll(getOrdersList);
                    approvalRequestConvancyListAdpter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setDataAdapter() {
        binding.fieldStockList.addItemDecoration(new DividerItemDecoration(ApprovalRequestConveyanceListClass.this, LinearLayoutManager.HORIZONTAL));
        binding.fieldStockList.setHasFixedSize(true);
        binding.fieldStockList.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ApprovalRequestConveyanceListClass.this, LinearLayoutManager.VERTICAL, false);
        binding.fieldStockList.setLayoutManager(horizontalLayoutManager);
        approvalRequestConvancyListAdpter = new ApprovalRequestConvancyListAdpter(getOrdersList, ApprovalRequestConveyanceListClass.this, getActivity());
        binding.fieldStockList.setAdapter(approvalRequestConvancyListAdpter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(MyConveyanceListClass.class);
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalRequestConveyanceListClass.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = ApprovalRequestConveyanceListClass.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.con_view, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = dialogView.findViewById(R.id.cancel_btn);
        TextView lead_id = dialogView.findViewById(R.id.lead_id);
        TextView genrated_by = dialogView.findViewById(R.id.genrated_by);
        TextView customer_name = dialogView.findViewById(R.id.customer_name);
        TextView phone_no = dialogView.findViewById(R.id.phone_no);
        TextView location_title = dialogView.findViewById(R.id.location_title);
        TextView commodity_name = dialogView.findViewById(R.id.commodity_name);
        TextView est_quantity_nmae = dialogView.findViewById(R.id.est_quantity_nmae);
        TextView terminal_name = dialogView.findViewById(R.id.terminal_name);
        TextView purpose_name = dialogView.findViewById(R.id.purpose_name);
        TextView commitemate_date = dialogView.findViewById(R.id.commitemate_date);
        TextView create_date = dialogView.findViewById(R.id.create_date);
        TextView converted_by = dialogView.findViewById(R.id.converted_by);
        TextView vehicle_no = dialogView.findViewById(R.id.vehicle_no);
        TextView in_out = dialogView.findViewById(R.id.in_out);
        TextView gate_passs = dialogView.findViewById(R.id.gate_passs);
        TextView total_trans_cost = dialogView.findViewById(R.id.total_trans_cost);
        TextView advance_patyment = dialogView.findViewById(R.id.advance_patyment);
        TextView start_date_time = dialogView.findViewById(R.id.start_date_time);
        TextView end_date_time = dialogView.findViewById(R.id.end_date_time);
        TextView settleememt_amount = dialogView.findViewById(R.id.settleememt_amount);
        ImageView reports_file = dialogView.findViewById(R.id.reports_file);
        ImageView commodity_file = dialogView.findViewById(R.id.commodity_file);
        View viewOther = dialogView.findViewById(R.id.viewOther);
        LinearLayout LlOther = dialogView.findViewById(R.id.llOther);
        ImageView OtherFile = dialogView.findViewById(R.id.other_file);
        viewOther.setVisibility(View.VISIBLE);
        LlOther.setVisibility(View.VISIBLE);
        otherImaage=Constants.conveyance + getOrdersList.get(position).getOther_charge_img();
        startMeterImage = Constants.conveyance + getOrdersList.get(position).getImage();
        endMeterImage = Constants.conveyance + getOrdersList.get(position).getImage2();
        OtherFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(ApprovalRequestConveyanceListClass.this, R.layout.popup_photo_full, view, otherImaage, null);
            }
        });
        reports_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(ApprovalRequestConveyanceListClass.this, R.layout.popup_photo_full, view, startMeterImage, null);
            }
        });
        commodity_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(ApprovalRequestConveyanceListClass.this, R.layout.popup_photo_full, view, endMeterImage, null);
            }
        });
        inventory_id = "" + getOrdersList.get(position).getId();
        lead_id.setText("CV - " + ((getOrdersList.get(position).getUniqueId()) != null ? getOrdersList.get(position).getUniqueId() : "N/A"));
        genrated_by.setText("" + ((getOrdersList.get(position).getDate()) != null ? getOrdersList.get(position).getDate() : "N/A"));
        customer_name.setText("" + ((getOrdersList.get(position).getVehicleNo()) != null ? getOrdersList.get(position).getVehicleNo() : "N/A"));
        commodity_name.setText("" + ((getOrdersList.get(position).getPurpose()) != null ? getOrdersList.get(position).getPurpose() : "N/A"));
        phone_no.setText("" + ((getOrdersList.get(position).getFromPlace()) != null ? getOrdersList.get(position).getFromPlace() : "N/A"));
        location_title.setText("" + ((getOrdersList.get(position).getToPlace()) != null ? getOrdersList.get(position).getToPlace() : "N/A"));
        est_quantity_nmae.setText("" + ((getOrdersList.get(position).getLocation()) != null ? (getOrdersList.get(position).getLocation()) : "N/A"));
        terminal_name.setText(" " + ((getOrdersList.get(position).getStartReading()) != null ? getOrdersList.get(position).getStartReading() : "N/A"));
        purpose_name.setText("" + ((getOrdersList.get(position).getEndReading()) != null ? getOrdersList.get(position).getEndReading() : "N/A"));
        commitemate_date.setText("" + ((getOrdersList.get(position).getKms()) != null ? getOrdersList.get(position).getKms() : "N/A"));
        create_date.setText("" + ((getOrdersList.get(position).getCharges()) != null ? getOrdersList.get(position).getCharges() : "N/A"));
        converted_by.setText("" + ((getOrdersList.get(position).getOtherExpense()) != null ? getOrdersList.get(position).getOtherExpense() : "N/A"));
        vehicle_no.setText("" + ((getOrdersList.get(position).getTotal()) != null ? getOrdersList.get(position).getTotal() : "N/A"));
        in_out.setText("" + ((getOrdersList.get(position).getFinalPrize()) != null ? (getOrdersList.get(position).getFinalPrize()) : "N/A"));
        gate_passs.setText("" + ((getOrdersList.get(position).getNotes()) != null ? getOrdersList.get(position).getNotes() : "N/A"));
        total_trans_cost.setText("" + ((getOrdersList.get(position).getFirstName() + " " + getOrdersList.get(position).getLastName() + "(" + getOrdersList.get(position).getEmpId() + ")") != null ? ((getOrdersList.get(position).getFirstName() + " " + getOrdersList.get(position).getLastName() + "(" + getOrdersList.get(position).getEmpId() + ")")) : "N/A"));
        if (getOrdersList.get(position).getStatus().equalsIgnoreCase("1")) {
            // show button self rejected
            advance_patyment.setText(getResources().getString(R.string.reject));
            advance_patyment.setTextColor(getResources().getColor(R.color.darkYellow));
        } else if (getOrdersList.get(position).getStatus().equalsIgnoreCase("0")) {
            // rejected by self or approved from high authority
            advance_patyment.setText(getResources().getString(R.string.rejected));
            advance_patyment.setTextColor(getResources().getColor(R.color.red));
        } else if (getOrdersList.get(position).getStatus().equalsIgnoreCase("2")) {
            //approved from high authority
            advance_patyment.setText(getResources().getString(R.string.approved));
            advance_patyment.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        ///////////////////////////////////////////////////////
        if (getOrdersList.get(position).getPayment().equalsIgnoreCase("1")) {
            // show button self rejected
            end_date_time.setText(getResources().getString(R.string.pending));
            end_date_time.setTextColor(getResources().getColor(R.color.darkYellow));
        } else if (getOrdersList.get(position).getPayment().equalsIgnoreCase("0")) {
            // rejected by self or approved from high authority
            end_date_time.setText(getResources().getString(R.string.rejected));
            end_date_time.setTextColor(getResources().getColor(R.color.red));
        } else if (getOrdersList.get(position).getPayment().equalsIgnoreCase("2")) {
            //approved from high authority
            end_date_time.setText(getResources().getString(R.string.done));
            end_date_time.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////
        if (getOrdersList.get(position).getVerify().equalsIgnoreCase("1")) {
            // show button self rejected
            settleememt_amount.setText(getResources().getString(R.string.pending));
            settleememt_amount.setTextColor(getResources().getColor(R.color.darkYellow));
        } else if (getOrdersList.get(position).getVerify().equalsIgnoreCase("0")) {
            // rejected by self or approved from high authority
            settleememt_amount.setText(getResources().getString(R.string.rejected));
            settleememt_amount.setTextColor(getResources().getColor(R.color.red));
        } else if (getOrdersList.get(position).getVerify().equalsIgnoreCase("2")) {
            //approved from high authority
            settleememt_amount.setText(getResources().getString(R.string.verify_conv));
            settleememt_amount.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        start_date_time.setText("" + ((getOrdersList.get(position).getUpdatedAt()) != null ? getOrdersList.get(position).getUpdatedAt() : "N/A"));
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void cancelConv(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalRequestConveyanceListClass.this);
        LayoutInflater inflater = ((Activity) ApprovalRequestConveyanceListClass.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_seell_price_diloag, null);
        EditText reson = (EditText) dialogView.findViewById(R.id.reson);
        EditText amount = (EditText) dialogView.findViewById(R.id.amount);
        TextView submit = (TextView) dialogView.findViewById(R.id.btn_submit);
        ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
        amount.setVisibility(View.GONE);
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
                if (reson.getText().toString().trim() == null || reson.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Reject Reason", Toast.LENGTH_LONG).show();
                } /*else if (amount.getText().toString().trim() == null || amount.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Final Amount", Toast.LENGTH_LONG).show();
                }*/ else {
                    Utility.showDecisionDialog(ApprovalRequestConveyanceListClass.this, getString(R.string.alert), "Are you Sure? Do you want to Reject this Conveyance!", new Utility.AlertCallback() {
                        @Override
                        public void callback() {
                            inventory_id = "" + getOrdersList.get(position).getId();
                            apiService.RejectConveyanceDelate(new ApprovedRejectConveyancePOst(inventory_id, reson.getText().toString().trim())).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                                @Override
                                protected void onSuccess(LoginResponse body) {
                                    alertDialog.dismiss();
                                    Toast.makeText(ApprovalRequestConveyanceListClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                                    getApprovalRequestConvencyList("");
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    public void ApprovedConv(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalRequestConveyanceListClass.this);
        LayoutInflater inflater = ((Activity) ApprovalRequestConveyanceListClass.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_seell_price_diloag, null);
        EditText reson = (EditText) dialogView.findViewById(R.id.reson);
        EditText amount = (EditText) dialogView.findViewById(R.id.amount);
        TextView submit = (TextView) dialogView.findViewById(R.id.btn_submit);
        TextView heading = (TextView) dialogView.findViewById(R.id.heading);
        ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
        heading.setText("Expense Approve Notes");
        reson.setHint("Notes");
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
                if (reson.getText().toString().trim() == null || reson.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter  Reason", Toast.LENGTH_LONG).show();
                } else if (amount.getText().toString().trim() == null || amount.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Final Amount", Toast.LENGTH_LONG).show();
                } else {
                    Utility.showDecisionDialog(ApprovalRequestConveyanceListClass.this, getString(R.string.alert), "Are you Sure? Do you want to Approve this Conveyance!", new Utility.AlertCallback() {
                        @Override
                        public void callback() {
                            inventory_id = "" + getOrdersList.get(position).getId();
                            apiService.ApproveConveyanceDelate(new ApprovedConveyancePOst(inventory_id, amount.getText().toString().trim(), reson.getText().toString().trim())).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                                @Override
                                protected void onSuccess(LoginResponse body) {
                                    alertDialog.dismiss();
                                    Toast.makeText(ApprovalRequestConveyanceListClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                                    getApprovalRequestConvencyList("");
                                }
                            });
                        }
                    });
                }
            }
        });

    }
}
