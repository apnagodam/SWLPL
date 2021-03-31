package com.apnagodam.staff.activity.in.gatepass;

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
import com.apnagodam.staff.activity.GatePassPDFPrieviewClass;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.adapter.GatepassAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.GatePassListResponse;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;

import java.util.ArrayList;
import java.util.List;


public class GatePassListingActivity extends BaseActivity<ActivityListingBinding> {
    private String firstkantaParchiFile, TruckImage;
    private GatepassAdapter gatepassAdapter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<GatePassListResponse.Datum> AllCases;

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
        binding.titleHeader.setText(getResources().getString(R.string.gate_pass_list));
        binding.tvId.setText(getResources().getString(R.string.case_idd));
        binding.tvMoreView.setText(getResources().getString(R.string.more_view_truck));
        binding.tvPhone.setText(getResources().getString(R.string.gate_passs));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.swipeRefresherStock.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllCases("");
            }
        });
       /* binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(GatePassListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(GatePassListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);*/
        getAllCases("");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(GatePassListingActivity.this);
                LayoutInflater inflater = ((Activity) GatePassListingActivity.this).getLayoutInflater();
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

    private void setAdapter() {
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(GatePassListingActivity.this, LinearLayoutManager.VERTICAL));
        binding.rvDefaultersStatus.setHasFixedSize(true);
        binding.rvDefaultersStatus.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(GatePassListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);
        gatepassAdapter = new GatepassAdapter(AllCases, GatePassListingActivity.this, getActivity());
        binding.rvDefaultersStatus.setAdapter(gatepassAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(StaffDashBoardActivity.class);
    }

    private void getAllCases(String search) {
        apiService.getGatePass("10", "" + pageOffset, "IN", search).enqueue(new NetworkCallback<GatePassListResponse>(getActivity()) {
            @Override
            protected void onSuccess(GatePassListResponse body) {
                binding.swipeRefresherStock.setRefreshing(false);
                AllCases.clear();
                if (body.getGatePassData() == null) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } else {
                    AllCases.clear();
                    totalPage = body.getGatePassData().getLastPage();
                    AllCases.addAll(body.getGatePassData().getData());
                    gatepassAdapter.notifyDataSetChanged();
                    // AllCases = body.getData();
                    //  binding.rvDefaultersStatus.setAdapter(new GatepassAdapter(body.getData(), GatePassListingActivity.this));
                }
            }
        });
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(GatePassListingActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = ((Activity) GatePassListingActivity.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dilog_kanta_parchi, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
        TextView case_id = (TextView) dialogView.findViewById(R.id.a1);
        TextView gatePass = (TextView) dialogView.findViewById(R.id.a4);
        TextView lead_id = (TextView) dialogView.findViewById(R.id.lead_id);
        TextView customer_name = (TextView) dialogView.findViewById(R.id.customer_name);
        ImageView kanta_parchi_file = (ImageView) dialogView.findViewById(R.id.kanta_parchi_file);
        ImageView truck_file = (ImageView) dialogView.findViewById(R.id.truck_file);
        TextView notes = (TextView) dialogView.findViewById(R.id.notes);
        TextView gate_pass = (TextView) dialogView.findViewById(R.id.gate_pass);
        TextView user = (TextView) dialogView.findViewById(R.id.user);
        TextView coldwin = (TextView) dialogView.findViewById(R.id.coldwin);
        TextView purchase_details = (TextView) dialogView.findViewById(R.id.purchase_details);
        TextView loan_details = (TextView) dialogView.findViewById(R.id.loan_details);
        TextView selas_details = (TextView) dialogView.findViewById(R.id.selas_details);
        TextView bags = (TextView) dialogView.findViewById(R.id.no_of_bags);
        TextView wieight = (TextView) dialogView.findViewById(R.id.total_weight);
        View view = (View) dialogView.findViewById(R.id.view);
        LinearLayout llview = (LinearLayout) dialogView.findViewById(R.id.llview);
        LinearLayout gate_pass_extra = (LinearLayout) dialogView.findViewById(R.id.gate_pass_extra);
        LinearLayout LLStack = (LinearLayout) dialogView.findViewById(R.id.LLStack);
        TextView stack_no = (TextView) dialogView.findViewById(R.id.stack_no);
        LinearLayout avgweight = (LinearLayout) dialogView.findViewById(R.id.avgweight);
        avgweight.setVisibility(View.VISIBLE);
        TextView avg_weight = (TextView) dialogView.findViewById(R.id.avg_weight);
        ////
        view.setVisibility(View.GONE);
        llview.setVisibility(View.GONE);
        gate_pass_extra.setVisibility(View.VISIBLE);
        LLStack.setVisibility(View.VISIBLE);
        gatePass.setText(getResources().getString(R.string.gate_passs_file));
        if (AllCases.get(position).getGPCaseId() == null || AllCases.get(position).getGPCaseId().isEmpty()) {
            kanta_parchi_file.setVisibility(View.GONE);
        }

        case_id.setText(getResources().getString(R.string.case_idd));
        lead_id.setText("" + AllCases.get(position).getCaseId());
        customer_name.setText("" + AllCases.get(position).getCustFname());
        avg_weight.setText("" + ((AllCases.get(position).getGatepass_avgWeight()) != null ? AllCases.get(position).getGatepass_avgWeight() : "N/A"));
        stack_no.setText("" + ((AllCases.get(position).getStack_number()) != null ? AllCases.get(position).getStack_number() : "N/A"));
        notes.setText("" + ((AllCases.get(position).getNotes()) != null ? AllCases.get(position).getNotes() : "N/A"));
        gate_pass.setText("Gaatepass/CDF Name : " + ((AllCases.get(position).getGatePassCdfUserName()) != null ? AllCases.get(position).getGatePassCdfUserName() : "N/A"));
        coldwin.setText("ColdWin Name: " + ((AllCases.get(position).getColdwinName()) != null ? AllCases.get(position).getColdwinName() : "N/A"));
        user.setText("User : " + ((AllCases.get(position).getFpoUserId()) != null ? AllCases.get(position).getFpoUserId() : "N/A"));
        purchase_details.setText("purchase Details : " + ((AllCases.get(position).getPurchaseName()) != null ? AllCases.get(position).getPurchaseName() : "N/A"));
        loan_details.setText("Loan Details : " + ((AllCases.get(position).getLoanName()) != null ? AllCases.get(position).getLoanName() : "N/A"));
        selas_details.setText("Sale Details : " + ((AllCases.get(position).getSaleName()) != null ? AllCases.get(position).getSaleName() : "N/A"));
        bags.setText("" + ((AllCases.get(position).getNoOfBags()) != null ? AllCases.get(position).getNoOfBags() : "N/A"));
        wieight.setText("" + ((AllCases.get(position).getTotalWeight()) != null ? AllCases.get(position).getTotalWeight() : "N/A"));
        /////
        firstkantaParchiFile = Constants.gate_pass + AllCases.get(position).getFile();
        kanta_parchi_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(GatePassPDFPrieviewClass.class, "CaseID", AllCases.get(position).getCaseId());
                //new PhotoFullPopupWindow(GatePassListingActivity.this, R.layout.popup_photo_full, view, firstkantaParchiFile, null);
            }
        });
        truck_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(GatePassListingActivity.this, R.layout.popup_photo_full, view, TruckImage, null);
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

    public void checkVeehicleNo(int postion) {
        Bundle bundle = new Bundle();
        bundle.putString("user_name", AllCases.get(postion).getCustFname());
        bundle.putString("case_id", AllCases.get(postion).getCaseId());
        bundle.putString("terminal_name", AllCases.get(postion).getTerminal_name());
        bundle.putString("vehicle_no", AllCases.get(postion).getVehicleNo());
        bundle.putString("stackNo", AllCases.get(postion).getStack_number());
        bundle.putString("INOUT", AllCases.get(postion).getInOut());
        startActivity(UploadGatePassClass.class, bundle);
    }
}
