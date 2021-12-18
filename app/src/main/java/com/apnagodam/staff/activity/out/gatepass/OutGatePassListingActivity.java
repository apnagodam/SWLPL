package com.apnagodam.staff.activity.out.gatepass;

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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.NetworkCallbackWProgress;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.GatePassPDFPrieviewClass;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.in.gatepass.GatePassListingActivity;
import com.apnagodam.staff.activity.in.gatepass.UploadGatePassClass;
import com.apnagodam.staff.activity.out.f_katha_parchi.OutFirstkanthaParchiListingActivity;
import com.apnagodam.staff.adapter.OutGatepassAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.GatePassListResponse;
import com.apnagodam.staff.module.StockDetailsPVPojo;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;


public class OutGatePassListingActivity extends BaseActivity<ActivityListingBinding> {
    private String firstkantaParchiFile, TruckImage;
    private OutGatepassAdapter outGatepassAdapter;
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
        binding.tvMoreView.setText(getResources().getString(R.string.gate_passs));
        binding.tvPhone.setText(getResources().getString(R.string.more_view_truck));
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
                AlertDialog.Builder builder = new AlertDialog.Builder(OutGatePassListingActivity.this);
                LayoutInflater inflater = ((Activity) OutGatePassListingActivity.this).getLayoutInflater();
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
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(OutGatePassListingActivity.this, LinearLayoutManager.VERTICAL));
        binding.rvDefaultersStatus.setHasFixedSize(true);
        binding.rvDefaultersStatus.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(OutGatePassListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);
        outGatepassAdapter = new OutGatepassAdapter(AllCases, OutGatePassListingActivity.this, getActivity());
        binding.rvDefaultersStatus.setAdapter(outGatepassAdapter);

    }

    private void getAllCases(String search) {
        apiService.getGatePass("10", "" + pageOffset, "OUT",search).enqueue(new NetworkCallback<GatePassListResponse>(getActivity()) {
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
                      outGatepassAdapter.notifyDataSetChanged();
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(OutGatePassListingActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = OutGatePassListingActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dilog_kanta_parchi, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = dialogView.findViewById(R.id.cancel_btn);
        TextView case_id = dialogView.findViewById(R.id.a1);
        TextView gatePass = dialogView.findViewById(R.id.a4);
        TextView lead_id = dialogView.findViewById(R.id.lead_id);
        TextView customer_name = dialogView.findViewById(R.id.customer_name);
        ImageView kanta_parchi_file = dialogView.findViewById(R.id.kanta_parchi_file);
        ImageView truck_file = dialogView.findViewById(R.id.truck_file);
        TextView notes = dialogView.findViewById(R.id.notes);
        TextView gate_pass = dialogView.findViewById(R.id.gate_pass);
        TextView user = dialogView.findViewById(R.id.user);
        TextView coldwin = dialogView.findViewById(R.id.coldwin);
        TextView purchase_details = dialogView.findViewById(R.id.purchase_details);
        TextView loan_details = dialogView.findViewById(R.id.loan_details);
        TextView selas_details = dialogView.findViewById(R.id.selas_details);
        TextView bags = dialogView.findViewById(R.id.no_of_bags);
        TextView wieight = dialogView.findViewById(R.id.total_weight);
        View view = dialogView.findViewById(R.id.view);
        LinearLayout llview = dialogView.findViewById(R.id.llview);
        LinearLayout gate_pass_extra = dialogView.findViewById(R.id.gate_pass_extra);
        LinearLayout LLStack = (LinearLayout) dialogView.findViewById(R.id.LLStack);
        TextView stack_no = (TextView) dialogView.findViewById(R.id.stack_no);
        LLStack.setVisibility(View.VISIBLE);
        LinearLayout avgweight = (LinearLayout) dialogView.findViewById(R.id.avgweight);
        avgweight.setVisibility(View.VISIBLE);
        TextView avg_weight = (TextView) dialogView.findViewById(R.id.avg_weight);
        ////
        view.setVisibility(View.GONE);
        llview.setVisibility(View.GONE);
        gate_pass_extra.setVisibility(View.VISIBLE);
        gatePass.setText(getResources().getString(R.string.gate_passs_file));
        if (AllCases.get(position).getGPCaseId() == null || AllCases.get(position).getGPCaseId().isEmpty()) {
            kanta_parchi_file.setVisibility(View.GONE);
        }
        /*displadge bags*/
        LinearLayout dispaldgebags = (LinearLayout) dialogView.findViewById(R.id.dispaldgebags);
        TextView no_of_displadge_bags = (TextView) dialogView.findViewById(R.id.no_of_displadge_bags);
        dispaldgebags.setVisibility(View.VISIBLE);
        Double TotalBags = 0.0;
        if (AllCases.get(position).getNoOfBags() != null) {
            if (AllCases.get(position).getDispledge_bags() != null) {
                TotalBags = (Double.parseDouble(AllCases.get(position).getNoOfBags())) - (Double.parseDouble(AllCases.get(position).getDispledge_bags()));
            }else {
                TotalBags =  (Double.parseDouble(AllCases.get(position).getNoOfBags()));
            }
        } else {
            TotalBags = 0.0;
        }
        no_of_displadge_bags.setText("" + ((AllCases.get(position).getDispledge_bags()) != null ? AllCases.get(position).getDispledge_bags() : "N/A"));
        bags.setText("" + ((AllCases.get(position).getNoOfBags()) != null ? "" + TotalBags : "N/A"));
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
        wieight.setText("" + ((AllCases.get(position).getTotalWeight()) != null ? AllCases.get(position).getTotalWeight() : "N/A"));
        /////
        firstkantaParchiFile = Constants.gate_pass + AllCases.get(position).getFile();
        kanta_parchi_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(GatePassPDFPrieviewClass.class,"CaseID",AllCases.get(position).getCaseId());
              //  new PhotoFullPopupWindow(OutGatePassListingActivity.this, R.layout.popup_photo_full, view, firstkantaParchiFile, null);
            }
        });
        truck_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(OutGatePassListingActivity.this, R.layout.popup_photo_full, view, TruckImage, null);
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
    public void updateGatepassData(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("user_name", AllCases.get(position).getCustFname());
        bundle.putString("case_id", AllCases.get(position).getCaseId());
        bundle.putString("terminal_name", AllCases.get(position).getTerminal_name());
        bundle.putString("vehicle_no", AllCases.get(position).getVehicleNo());
        bundle.putString("stackNo", AllCases.get(position).getStack_number());
        bundle.putString("INOUT", AllCases.get(position).getInOut());
        startActivity(OUTUpdateGatePassClass.class, bundle);
    }
    public void checkVeehicleNo(int postion) {
        Bundle bundle = new Bundle();
        bundle.putString("user_name", AllCases.get(postion).getCustFname());
        bundle.putString("case_id", AllCases.get(postion).getCaseId());
        bundle.putString("terminal_name", AllCases.get(postion).getTerminal_name());
        bundle.putString("vehicle_no", AllCases.get(postion).getVehicleNo());
        bundle.putString("stackNo", AllCases.get(postion).getStack_number());
        bundle.putString("INOUT", AllCases.get(postion).getInOut());
        startActivity(OutUploadGatePassClass.class, bundle);
    }
}
