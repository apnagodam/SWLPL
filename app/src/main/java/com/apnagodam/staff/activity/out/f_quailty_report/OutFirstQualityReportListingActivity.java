package com.apnagodam.staff.activity.out.f_quailty_report;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
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
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.in.first_quality_reports.UploadFirstQualtityReportsClass;
import com.apnagodam.staff.activity.out.f_katha_parchi.OutFirstkanthaParchiListingActivity;
import com.apnagodam.staff.adapter.FirstQualityReportAdapter;
import com.apnagodam.staff.adapter.OutFirstQualityReportAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.FirstQuilityReportListResponse;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;

import java.util.ArrayList;
import java.util.List;


public class OutFirstQualityReportListingActivity extends BaseActivity<ActivityListingBinding> {
    private OutFirstQualityReportAdapter outFirstQualityReportAdapter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<FirstQuilityReportListResponse.Datum> AllCases;
    private String ReportsFile, CommudityImage;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_listing;
    }

    @Override
    protected void setUp() {
        binding.pageNextPrivious.setVisibility(View.VISIBLE);
        AllCases = new ArrayList();
        setAdapter();
        setSupportActionBar(binding.toolbar);
        binding.titleHeader.setText(getResources().getString(R.string.f_quality_repots));
        binding.tvId.setText(getResources().getString(R.string.case_idd));
        binding.tvMoreView.setText(getResources().getString(R.string.more_view_truck));
        binding.tvPhone.setText(getResources().getString(R.string.quality_repots));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
     /*   binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(FirstQualityReportListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(FirstQualityReportListingActivity.this, LinearLayoutManager.VERTICAL, false);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(OutFirstQualityReportListingActivity.this);
                LayoutInflater inflater = ((Activity) OutFirstQualityReportListingActivity.this).getLayoutInflater();
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
                //  setDateTimeField();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(StaffDashBoardActivity.class);
    }

    private void setAdapter() {
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(OutFirstQualityReportListingActivity.this, LinearLayoutManager.VERTICAL));
        binding.rvDefaultersStatus.setHasFixedSize(true);
        binding.rvDefaultersStatus.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(OutFirstQualityReportListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);
        outFirstQualityReportAdapter = new OutFirstQualityReportAdapter(AllCases, OutFirstQualityReportListingActivity.this, getActivity());
        binding.rvDefaultersStatus.setAdapter(outFirstQualityReportAdapter);

    }

    private void getAllCases(String search) {
        showDialog();
        apiService.getf_qualityReportsList("10", "" + pageOffset, "OUT",search).enqueue(new NetworkCallback<FirstQuilityReportListResponse>(getActivity()) {
            @Override
            protected void onSuccess(FirstQuilityReportListResponse body) {
                binding.swipeRefresherStock.setRefreshing(false);
                AllCases.clear();
                if (body.getQuilityReport() == null) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } else {
                    AllCases.clear();
                    totalPage = body.getQuilityReport().getLastPage();
                    AllCases.addAll(body.getQuilityReport().getData());
                    outFirstQualityReportAdapter.notifyDataSetChanged();
                    // AllCases = body.getData();
                    // binding.rvDefaultersStatus.setAdapter(new FirstQualityReportAdapter(body.getData(), FirstQualityReportListingActivity.this));
                }
            }
        });
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(OutFirstQualityReportListingActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = OutFirstQualityReportListingActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.lead_view, null);
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
        LinearLayout case_extra = dialogView.findViewById(R.id.case_extra);
        case_extra.setVisibility(View.VISIBLE);
        LinearLayout price_extra = dialogView.findViewById(R.id.price_extra);
        price_extra.setVisibility(View.VISIBLE);
        LinearLayout photo_extra = dialogView.findViewById(R.id.photo_extra);
        photo_extra.setVisibility(View.VISIBLE);
        TextView gate_pass = dialogView.findViewById(R.id.gate_pass);
        TextView user = dialogView.findViewById(R.id.user);
        TextView coldwin = dialogView.findViewById(R.id.coldwin);
        TextView purchase_details = dialogView.findViewById(R.id.purchase_details);
        TextView loan_details = dialogView.findViewById(R.id.loan_details);
        TextView selas_details = dialogView.findViewById(R.id.selas_details);
        TextView converted_by = dialogView.findViewById(R.id.converted_by);
        ImageView reports_file = dialogView.findViewById(R.id.reports_file);
        ImageView commodity_file = dialogView.findViewById(R.id.commodity_file);
        /////////////////////////////////////////////
        if (AllCases.get(position).getImge() == null || AllCases.get(position).getImge().isEmpty()) {
            reports_file.setVisibility(View.GONE);
        }
        if (AllCases.get(position).getCommodity_img() == null || AllCases.get(position).getCommodity_img().isEmpty()) {
            commodity_file.setVisibility(View.GONE);
        }
        ReportsFile = Constants.First_quality + AllCases.get(position).getImge();
        CommudityImage = Constants.First_quality + AllCases.get(position).getCommodity_img();
        reports_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(OutFirstQualityReportListingActivity.this, R.layout.popup_photo_full, view, ReportsFile, null);
            }
        });
        commodity_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(OutFirstQualityReportListingActivity.this, R.layout.popup_photo_full, view, CommudityImage, null);
            }
        });
        case_id.setText(getResources().getString(R.string.case_idd));
        username.setText(getResources().getString(R.string.total_weight));
        lead_id.setText("" + AllCases.get(position).getCaseId());
        genrated_by.setText("" + ((AllCases.get(position).getTotalWeight()) != null ? AllCases.get(position).getTotalWeight() : "N/A"));
        customer_name.setText("" + AllCases.get(position).getCustFname());
        vehicle_no.setText(getResources().getString(R.string.moisture_level));
        phone_no.setText("" + ((AllCases.get(position).getMoistureLevel()) != null ? AllCases.get(position).getMoistureLevel() : "N/A"));
        processing_fees.setText(getResources().getString(R.string.tcw));
        location_title.setText("" + ((AllCases.get(position).getThousandCrownW()) != null ? AllCases.get(position).getThousandCrownW() : "N/A"));
        inteerset_rate.setText(getResources().getString(R.string.broken));
        commodity_name.setText("" + ((AllCases.get(position).getBroken()) != null ? AllCases.get(position).getBroken() : "N/A"));
        transport_rate.setText(getResources().getString(R.string.fm_level));
        est_quantity_nmae.setText("" + ((AllCases.get(position).getForeignMatter()) != null ? AllCases.get(position).getForeignMatter() : "N/A"));
        loan.setText(getResources().getString(R.string.thin));
        terminal_name.setText("" + ((AllCases.get(position).getThin()) != null ? AllCases.get(position).getThin() : "N/A"));
        price.setText(getResources().getString(R.string.dehuck));
        purpose_name.setText("" + ((AllCases.get(position).getDamage()) != null ? AllCases.get(position).getDamage() : "N/A"));
        rent.setText(getResources().getString(R.string.discolor));
        commitemate_date.setText("" + ((AllCases.get(position).getBlackSmith()) != null ? AllCases.get(position).getBlackSmith() : "N/A"));
        labour_rent.setText(getResources().getString(R.string.infested));
        create_date.setText("" + ((AllCases.get(position).getInfested()) != null ? AllCases.get(position).getInfested() : "N/A"));
        total_weight.setText(getResources().getString(R.string.live));
        converted_by.setText("" + ((AllCases.get(position).getLiveInsects()) != null ? AllCases.get(position).getLiveInsects() : "N/A"));
        notes.setText(getResources().getString(R.string.packaging));
        total_weights.setText("" + ((AllCases.get(position).getPackagingType()) != null ? AllCases.get(position).getPackagingType() : "N/A"));
        bags.setText(getResources().getString(R.string.notes));
        total_bags.setText("" + ((AllCases.get(position).getNotes()) != null ? AllCases.get(position).getNotes() : "N/A"));
        gate_pass.setText("Gaatepass/CDF Name : " + ((AllCases.get(position).getGatePassCdfUserName()) != null ? AllCases.get(position).getGatePassCdfUserName() : "N/A"));
        coldwin.setText("ColdWin Name: " + ((AllCases.get(position).getColdwinName()) != null ? AllCases.get(position).getColdwinName() : "N/A"));
        user.setText("User : " + ((AllCases.get(position).getFpoUserId()) != null ? AllCases.get(position).getFpoUserId() : "N/A"));
        purchase_details.setText("purchase Details : " + ((AllCases.get(position).getPurchaseName()) != null ? AllCases.get(position).getPurchaseName() : "N/A"));
        loan_details.setText("Loan Details : " + ((AllCases.get(position).getLoanName()) != null ? AllCases.get(position).getLoanName() : "N/A"));
        selas_details.setText("Sale Details : " + ((AllCases.get(position).getSaleName()) != null ? AllCases.get(position).getSaleName() : "N/A"));
        ///////////////////////////////////////////////////
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void checkVeehicleNo(int postion) {
        Bundle bundle = new Bundle();
        bundle.putString("user_name", AllCases.get(postion).getCustFname());
        bundle.putString("case_id", AllCases.get(postion).getCaseId());
        bundle.putString("vehicle_no", AllCases.get(postion).getVehicleNo());
        startActivity(UploadFirstQualtityReportsClass.class, bundle);
    }

}
