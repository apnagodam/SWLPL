package com.apnagodam.staff.activity.in.pricing;

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
import com.apnagodam.staff.Network.Request.ClosedCasesPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.caseid.CaseListingActivity;
import com.apnagodam.staff.activity.convancy_voachar.MyConveyanceListClass;
import com.apnagodam.staff.activity.in.labourbook.LabourBookListingActivity;
import com.apnagodam.staff.adapter.CasesTopAdapter;
import com.apnagodam.staff.adapter.ConvancyListAdpter;
import com.apnagodam.staff.adapter.PricingAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.AllCaseIDResponse;
import com.apnagodam.staff.module.AllpricingResponse;

import java.util.ArrayList;
import java.util.List;


public class InPricingListingActivity extends BaseActivity<ActivityListingBinding> {
    private PricingAdapter pricingAdapter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<AllpricingResponse.Datum> AllCases;
    private List<AllpricingResponse.Datum> tempraryList;
    String  editView = "";
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_listing;
    }

    @Override
    protected void setUp() {
       /* Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle != null) {
            editView = bundle.getString("edit");
        }*/
        AllCases = new ArrayList();
        tempraryList = new ArrayList();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setAdapter();
        binding.titleHeader.setText(getResources().getString(R.string.pricing_title));
        binding.tvId.setText(getResources().getString(R.string.case_idd));
        binding.tvMoreView.setText(getResources().getString(R.string.action));
        binding.tvPhone.setText(getResources().getString(R.string.set_pricing));
     /*   binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(InPricingListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(InPricingListingActivity.this, LinearLayoutManager.VERTICAL, false);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(InPricingListingActivity.this);
                LayoutInflater inflater = ((Activity) InPricingListingActivity.this).getLayoutInflater();
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
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(InPricingListingActivity.this, LinearLayoutManager.HORIZONTAL));
        binding.rvDefaultersStatus.setHasFixedSize(true);
        binding.rvDefaultersStatus.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(InPricingListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);
        pricingAdapter = new PricingAdapter(AllCases, InPricingListingActivity.this,getActivity());
        binding.rvDefaultersStatus.setAdapter(pricingAdapter);

    }
    private void getAllCases(String search) {
        showDialog();
        apiService.getAllpricingList("10",pageOffset,"IN",search).enqueue(new NetworkCallback<AllpricingResponse>(getActivity()) {
            @Override
            protected void onSuccess(AllpricingResponse body) {
                binding.swipeRefresherStock.setRefreshing(false);
                AllCases.clear();
                tempraryList.clear();
                if (body.getPricing().getData().size() == 0) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } else if (body.getPricing().getLastPage() == 1) {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.rvDefaultersStatus.setVisibility(View.VISIBLE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                    AllCases.clear();
                    tempraryList.clear();
                    totalPage = body.getPricing().getLastPage();
                    AllCases.addAll(body.getPricing().getData());
                    tempraryList.addAll(AllCases);
                    pricingAdapter.notifyDataSetChanged();
                } else {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.rvDefaultersStatus.setVisibility(View.VISIBLE);
                    binding.pageNextPrivious.setVisibility(View.VISIBLE);
                    AllCases.clear();
                    tempraryList.clear();
                    totalPage = body.getPricing().getLastPage();
                    AllCases.addAll(body.getPricing().getData());
                    tempraryList.addAll(AllCases);
                    pricingAdapter.notifyDataSetChanged();
                }

               /* if (body.getPricing() == null ) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } else {
                    AllCases.clear();
                    tempraryList.clear();
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    totalPage = body.getPricing().getLastPage();
                    AllCases.addAll(body.getPricing().getData());
                    tempraryList.addAll(AllCases);
                    pricingAdapter.notifyDataSetChanged();
                   // AllCases = body.getCases();
                 ///   binding.rvDefaultersStatus.setAdapter(new PricingAdapter(body.getCases(), InPricingListingActivity.this));
                }*/
            }
        });
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(InPricingListingActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = ((Activity) InPricingListingActivity.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.lead_view, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
        TextView lead_id = (TextView) dialogView.findViewById(R.id.lead_id);
        TextView genrated_by = (TextView) dialogView.findViewById(R.id.genrated_by);
        TextView customer_name = (TextView) dialogView.findViewById(R.id.customer_name);
        TextView phone_no = (TextView) dialogView.findViewById(R.id.phone_no);
        TextView location_title = (TextView) dialogView.findViewById(R.id.location_title);
        TextView commodity_name = (TextView) dialogView.findViewById(R.id.commodity_name);
        TextView est_quantity_nmae = (TextView) dialogView.findViewById(R.id.est_quantity_nmae);
        TextView terminal_name = (TextView) dialogView.findViewById(R.id.terminal_name);
        TextView purpose_name = (TextView) dialogView.findViewById(R.id.purpose_name);
        TextView commitemate_date = (TextView) dialogView.findViewById(R.id.commitemate_date);
        TextView create_date = (TextView) dialogView.findViewById(R.id.create_date);
        TextView case_id = (TextView) dialogView.findViewById(R.id.a1);

        TextView username = (TextView) dialogView.findViewById(R.id.a2);
        TextView processing_fees = (TextView) dialogView.findViewById(R.id.a5);
        TextView inteerset_rate = (TextView) dialogView.findViewById(R.id.a6);
        TextView loan = (TextView) dialogView.findViewById(R.id.a8);
        TextView price = (TextView) dialogView.findViewById(R.id.a9);
        TextView rent = (TextView) dialogView.findViewById(R.id.a10);
        TextView labour_rent = (TextView) dialogView.findViewById(R.id.a11);
        TextView notes = (TextView) dialogView.findViewById(R.id.a12);
        LinearLayout price_extra = (LinearLayout) dialogView.findViewById(R.id.price_extra);
        price_extra.setVisibility(View.VISIBLE);
        TextView Notes = (TextView) dialogView.findViewById(R.id.notes);
        TextView view = (TextView) dialogView.findViewById(R.id.a12);
        View view12 = (View) dialogView.findViewById(R.id.view12);
        LinearLayout ll12 = (LinearLayout) dialogView.findViewById(R.id.ll12);
        ll12.setVisibility(View.VISIBLE);
        view12.setVisibility(View.VISIBLE);
        TextView converted_by = (TextView) dialogView.findViewById(R.id.converted_by);
        TextView gate_pass = (TextView) dialogView.findViewById(R.id.gate_pass);
        TextView user = (TextView) dialogView.findViewById(R.id.user);
        TextView coldwin = (TextView) dialogView.findViewById(R.id.coldwin);
        TextView purchase_details = (TextView) dialogView.findViewById(R.id.purchase_details);
        TextView loan_details = (TextView) dialogView.findViewById(R.id.loan_details);
        TextView selas_details = (TextView) dialogView.findViewById(R.id.selas_details);
        Notes.setText("" + ((AllCases.get(position).getNotes()) != null ? AllCases.get(position).getNotes() : "N/A"));
        gate_pass.setText("Gaatepass/CDF Name : " + ((AllCases.get(position).getGatePassCdfUserName()) != null ? AllCases.get(position).getGatePassCdfUserName() : "N/A"));
        coldwin.setText("ColdWin Name: " + ((AllCases.get(position).getColdwinName()) != null ? AllCases.get(position).getColdwinName() : "N/A"));
        user.setText("User : " + ((AllCases.get(position).getFpoUserId()) != null ? AllCases.get(position).getFpoUserId() : "N/A"));
        purchase_details.setText("purchase Details : " + ((AllCases.get(position).getPurchaseName()) != null ? AllCases.get(position).getPurchaseName() : "N/A"));
        loan_details.setText("Loan Details : " + ((AllCases.get(position).getLoanName()) != null ? AllCases.get(position).getLoanName() : "N/A"));
        selas_details.setText("Sale Details : " + ((AllCases.get(position).getSaleName()) != null ? AllCases.get(position).getSaleName() : "N/A"));
        case_id.setText(getResources().getString(R.string.case_idd));
        notes.setText(getResources().getString(R.string.notes));
        rent.setText(getResources().getString(R.string.rent1));
        labour_rent.setText(getResources().getString(R.string.labour_rent));
        loan.setText(getResources().getString(R.string.loan));
        price.setText(getResources().getString(R.string.price));
        processing_fees.setText(getResources().getString(R.string.processing_fees));
        inteerset_rate.setText(getResources().getString(R.string.inteerset_rate));
        username.setText(getResources().getString(R.string.trans_type));
        lead_id.setText("" + AllCases.get(position).getCaseId());
        genrated_by.setText("" + ((AllCases.get(position).getTransactionType()) != null ? AllCases.get(position).getTransactionType() : "N/A"));
        customer_name.setText("" + AllCases.get(position).getCustFname());
        location_title.setText("" + ((AllCases.get(position).getProcessingFees()) != null ? AllCases.get(position).getProcessingFees() : "N/A"));
        phone_no.setText("" + AllCases.get(position).getPhone());
        commodity_name.setText("" + ((AllCases.get(position).getInterestRate()) != null ? AllCases.get(position).getInterestRate() : "N/A"));
        est_quantity_nmae.setText("" + AllCases.get(position).getTotalWeight());
        terminal_name.setText("" + ((AllCases.get(position).getLoanTPerAmount()) != null ? AllCases.get(position).getLoanTPerAmount() : "N/A"));
        purpose_name.setText("" + ((AllCases.get(position).getPrice()) != null ? AllCases.get(position).getPrice() : "N/A"));
        commitemate_date.setText("" + ((AllCases.get(position).getRent()) != null ? AllCases.get(position).getRent() : "N/A"));
        create_date.setText("" + ((AllCases.get(position).getLabourRate()) != null ? AllCases.get(position).getLabourRate() : "N/A"));
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
        startActivity(SetPricingClass.class,bundle);
       /* apiService.cheeckvehiclePricicng(AllCases.get(postion).getCaseId()).enqueue(new NetworkCallback<VehcilePricingCheeck>(getActivity()) {
            @Override
            protected void onSuccess(VehcilePricingCheeck body) {
                if (body.getVichel_status().equalsIgnoreCase("1")) {
                    Toast.makeText(getApplicationContext(), body.getMessage(), Toast.LENGTH_LONG).show();
                } else {

                }
            }
        });*/
    }

    public void closedPricing(int postion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(InPricingListingActivity.this);
        LayoutInflater inflater = ((Activity) InPricingListingActivity.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.close_pricicng_diloag, null);
        TextView title = (TextView) dialogView.findViewById(R.id.name);
        EditText notes = (EditText) dialogView.findViewById(R.id.notes);
        Button submit = (Button) dialogView.findViewById(R.id.btn_submit);
        ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        title.setText("Case ID: " + AllCases.get(postion).getCaseId());

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
                    ClosedPricing(alertDialog, AllCases.get(postion).getCaseId(), notes.getText().toString().trim());
                } else {
                    Toast.makeText(getApplicationContext(), "Please Fill notes", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void ClosedPricing(AlertDialog alertDialog, String caseId, String notes) {
        apiService.doClosedCase(new ClosedCasesPostData(caseId, notes)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
            @Override
            protected void onSuccess(LoginResponse body) {
                Toast.makeText(InPricingListingActivity.this, body.getMessage(), Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
                alertDialog.cancel();
                startActivityAndClear(InPricingListingActivity.class);
            }
        });
    }

}
