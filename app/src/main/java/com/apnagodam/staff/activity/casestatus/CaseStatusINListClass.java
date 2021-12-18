package com.apnagodam.staff.activity.casestatus;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
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
import com.apnagodam.staff.Network.NetworkCallbackWProgress;
import com.apnagodam.staff.Network.Request.SelfRejectConveyancePOst;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.in.truckbook.TruckUploadDetailsClass;
import com.apnagodam.staff.adapter.CaseStatusInListAdpter;
import com.apnagodam.staff.adapter.TruckBookAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.databinding.ConvencyListBinding;
import com.apnagodam.staff.module.AllConvancyList;
import com.apnagodam.staff.module.AllLevelEmpListPojo;
import com.apnagodam.staff.module.AllTruckBookListResponse;
import com.apnagodam.staff.module.CaseStatusINPojo;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class CaseStatusINListClass extends BaseActivity<ActivityListingBinding> {
    private CaseStatusInListAdpter truckBookAdapter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<CaseStatusINPojo.Datum> AllCases;
    private String TruckImage;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_listing;
    }

    @Override
    protected void setUp() {
        AllCases = new ArrayList();
        setAdapter();
        setSupportActionBar(binding.toolbar);
        binding.titleHeader.setText(getResources().getString(R.string.truck_book));
        binding.tvId.setText(getResources().getString(R.string.case_idd));
        binding.tvMoreView.setText(getResources().getString(R.string.more_view_truck));
        binding.tvPhone.setText(getResources().getString(R.string.truck_book));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
       /* binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(CaseStatusINListClass.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(CaseStatusINListClass.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);*/
        getAllCases("");
        binding.swipeRefresherStock.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllCases("");
            }
        });
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(StaffDashBoardActivity.class);
            }
        });
        binding.tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageOffset != 1) {
                    pageOffset--;
                    getAllCases("");
                }
            }
        });
        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalPage != pageOffset) {
                    pageOffset++;
                    getAllCases("");
                }
            }
        });
        binding.filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CaseStatusINListClass.this);
                LayoutInflater inflater = ((Activity) CaseStatusINListClass.this).getLayoutInflater();
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
                            getAllCases(notes.getText().toString().trim());
                            //     ClosedPricing(alertDialog, AllCases.get(postion).getCaseId(), notes.getText().toString().trim());
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Fill Text", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(StaffDashBoardActivity.class);
    }

    private void setAdapter() {
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(CaseStatusINListClass.this, LinearLayoutManager.HORIZONTAL));
        binding.rvDefaultersStatus.setHasFixedSize(true);
        binding.rvDefaultersStatus.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(CaseStatusINListClass.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);
        truckBookAdapter = new CaseStatusInListAdpter(AllCases, CaseStatusINListClass.this, getActivity());
        binding.rvDefaultersStatus.setAdapter(truckBookAdapter);
    }

    private void getAllCases(String search) {
        showDialog();
        apiService.getCaseStatusList("10",  search).enqueue(new NetworkCallbackWProgress<CaseStatusINPojo>(getActivity()) {
            @Override
            protected void onSuccess(CaseStatusINPojo body) {
                binding.swipeRefresherStock.setRefreshing(false);
                AllCases.clear();
                if (body.getData() == null) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } else {
                    AllCases.clear();
                  //  totalPage = body.getData().getl();
                    AllCases.addAll(body.getData());
                    truckBookAdapter.notifyDataSetChanged();
//                    binding.rvDefaultersStatus.setAdapter(new TruckBookAdapter(body.getTruckBookCollection(), CaseStatusINListClass.this));
                }
            }
        });
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(CaseStatusINListClass.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = CaseStatusINListClass.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.case_status_view, null);
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
        TextView total_weights = dialogView.findViewById(R.id.vehicle_no);
        TextView total_bags = dialogView.findViewById(R.id.in_out);
        TextView create_date = dialogView.findViewById(R.id.create_date);
        TextView case_id = dialogView.findViewById(R.id.a1);
        TextView username = dialogView.findViewById(R.id.a2);
        TextView vehicle_no = dialogView.findViewById(R.id.a4);
        TextView processing_fees = dialogView.findViewById(R.id.a5);
        TextView inteerset_rate = dialogView.findViewById(R.id.a6);
        TextView transport_rate = dialogView.findViewById(R.id.a7);
        TextView loan = dialogView.findViewById(R.id.a8);
        TextView price = dialogView.findViewById(R.id.a9);
        TextView rent = dialogView.findViewById(R.id.a10);
        TextView labour_rent = dialogView.findViewById(R.id.a11);
        TextView total_weight = dialogView.findViewById(R.id.a12);
        TextView notes = dialogView.findViewById(R.id.a13);
        TextView bags = dialogView.findViewById(R.id.a14);
        LinearLayout price_extra = dialogView.findViewById(R.id.price_extra);
        price_extra.setVisibility(View.VISIBLE);
        LinearLayout case_extra = dialogView.findViewById(R.id.case_extra);
        case_extra.setVisibility(View.VISIBLE);
        LinearLayout truck_extra = dialogView.findViewById(R.id.truck_extra);
        truck_extra.setVisibility(View.VISIBLE);
        TextView converted_by = dialogView.findViewById(R.id.converted_by);
        TextView gate_pass = dialogView.findViewById(R.id.gate_pass);
        TextView user = dialogView.findViewById(R.id.user);
        TextView coldwin = dialogView.findViewById(R.id.coldwin);
        TextView purchase_details = dialogView.findViewById(R.id.purchase_details);
        TextView loan_details = dialogView.findViewById(R.id.loan_details);
        TextView selas_details = dialogView.findViewById(R.id.selas_details);
        TextView gate_passs = dialogView.findViewById(R.id.gate_passs);
        TextView total_trans_cost = dialogView.findViewById(R.id.total_trans_cost);
        TextView advance_patyment = dialogView.findViewById(R.id.advance_patyment);
        TextView start_date_time = dialogView.findViewById(R.id.start_date_time);
        TextView end_date_time = dialogView.findViewById(R.id.end_date_time);
        TextView settleememt_amount = dialogView.findViewById(R.id.settleememt_amount);
        gate_passs.setText("" + ((AllCases.get(position).getGatePass()) != null ? AllCases.get(position).getGatePass() : "N/A"));
        total_trans_cost.setText("" + ((AllCases.get(position).getLabourPaymentFile()) != null ? AllCases.get(position).getLabourPaymentFile() : "N/A"));
        advance_patyment.setText("" + ((AllCases.get(position).getKantaParchiFile()) != null ? AllCases.get(position).getKantaParchiFile() : "N/A"));
        start_date_time.setText("" + ((AllCases.get(position).getCustLname()) != null ? AllCases.get(position).getCustLname() : "N/A"));
        end_date_time.setText("" + ((AllCases.get(position).getLabourPaymentUpdateTime()) != null ? AllCases.get(position).getLabourPaymentUpdateTime() : "N/A"));
        settleememt_amount.setText("" + ((AllCases.get(position).getCancelNotes()) != null ? AllCases.get(position).getCancelNotes() : "N/A"));
        total_bags.setText("" + ((AllCases.get(position).getNoOfBags()) != null ? AllCases.get(position).getNoOfBags() : "N/A"));
        total_weights.setText("" + ((AllCases.get(position).getVehicleNo()) != null ? AllCases.get(position).getVehicleNo() : "N/A"));
        converted_by.setText("" + ((AllCases.get(position).getTotalWeight()) != null ? AllCases.get(position).getTotalWeight() : "N/A"));
        gate_pass.setText("GaatePass/CDF Name : " + ((AllCases.get(position).getGatePassCdfUserName()) != null ? AllCases.get(position).getGatePassCdfUserName() : "N/A"));
        coldwin.setText("ColdWin Name: " + ((AllCases.get(position).getColdwinName()) != null ? AllCases.get(position).getColdwinName() : "N/A"));
        user.setText("User : " + ((AllCases.get(position).getFpoUserId()) != null ? AllCases.get(position).getFpoUserId() : "N/A"));
        purchase_details.setText("purchase Details : " + ((AllCases.get(position).getPurchaseName()) != null ? AllCases.get(position).getPurchaseName() : "N/A"));
        loan_details.setText("Loan Details : " + ((AllCases.get(position).getLoanName()) != null ? AllCases.get(position).getLoanName() : "N/A"));
        selas_details.setText("Sale Details : " + ((AllCases.get(position).getSaleName()) != null ? AllCases.get(position).getSaleName() : "N/A"));
        total_weight.setText(getResources().getString(R.string.total_weight));
        bags.setText(getResources().getString(R.string.bags));
        case_id.setText(getResources().getString(R.string.case_idd));
        notes.setText(getResources().getString(R.string.notes));
        rent.setText(getResources().getString(R.string.turnaround_time));
        labour_rent.setText(getResources().getString(R.string.commodity));
        loan.setText(getResources().getString(R.string.min_weight));
        transport_rate.setText(getResources().getString(R.string.transport_rate));
        price.setText(getResources().getString(R.string.max_weight));
        vehicle_no.setText(getResources().getString(R.string.vehicle_noa));
        processing_fees.setText(getResources().getString(R.string.driver_name));
        inteerset_rate.setText(getResources().getString(R.string.driver_phone_no));
        username.setText(getResources().getString(R.string.transporter));
        lead_id.setText("" + AllCases.get(position).getCaseId());
        genrated_by.setText("" + ((AllCases.get(position).getTerminalName()) != null ? AllCases.get(position).getTerminalName() : "N/A"));
        customer_name.setText("" + AllCases.get(position).getCustFname());
        location_title.setText("" + ((AllCases.get(position).getCateName()) != null ? AllCases.get(position).getCaseInventoryUpdateTime() : "N/A"));
        phone_no.setText("" + ((AllCases.get(position).getCustFname()) != null ? AllCases.get(position).getCateName() : "N/A"));
        commodity_name.setText("" + ((AllCases.get(position).getDriverPhone()) != null ? AllCases.get(position).getDriverPhone() : "N/A"));
        est_quantity_nmae.setText("" + ((AllCases.get(position).getCustFname()) != null ? AllCases.get(position).getCateName() : "N/A"));
        terminal_name.setText("" + ((AllCases.get(position).getAccountsCaseId()) != null ? AllCases.get(position).getAccountsCaseId() : "N/A"));
        purpose_name.setText("" + ((AllCases.get(position).getPaymentReceivedUpdateTime()) != null ? AllCases.get(position).getAccountsCaseId() : "N/A"));
        commitemate_date.setText("" + ((AllCases.get(position).getTerminalName()) != null ? AllCases.get(position).getCateName() : "N/A"));
        create_date.setText("" + ((AllCases.get(position).getCommodityId()) != null ? AllCases.get(position).getCommodityId() : "N/A"));
        ImageView truck_file = (ImageView) dialogView.findViewById(R.id.Bilty_Image);
        LinearLayout biltyLL = (LinearLayout) dialogView.findViewById(R.id.biltyLL);
        biltyLL.setVisibility(View.VISIBLE);
        if (AllCases.get(position).getKantaParchiFile() == null || AllCases.get(position).getKantaParchiFile().isEmpty()) {
            truck_file.setVisibility(View.GONE);
        }
        TruckImage = Constants.Truck_bilty_photo + AllCases.get(position).getKantaParchiFile();
        truck_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(CaseStatusINListClass.this, R.layout.popup_photo_full, view, TruckImage, null);
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
