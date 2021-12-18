package com.apnagodam.staff.activity.caseid;

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
import com.apnagodam.staff.R;
import com.apnagodam.staff.adapter.CasesTopAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.AllCaseIDResponse;

import java.util.ArrayList;
import java.util.List;


public class CaseListingActivity extends BaseActivity<ActivityListingBinding> {

    private CasesTopAdapter casesTopAdapter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<AllCaseIDResponse.Datum> AllCases;

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
        binding.titleHeader.setText(getResources().getString(R.string.case_list));
        binding.tvId.setText(getResources().getString(R.string.case_idd));
        binding.tvMoreView.setText(getResources().getString(R.string.status_title));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
                startActivityAndClear(CaseIDGenerateClass.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CaseListingActivity.this);
                LayoutInflater inflater = ((Activity) CaseListingActivity.this).getLayoutInflater();
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
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(CaseListingActivity.this, LinearLayoutManager.VERTICAL));
        binding.rvDefaultersStatus.setHasFixedSize(true);
        binding.rvDefaultersStatus.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(CaseListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);
        casesTopAdapter = new CasesTopAdapter(AllCases, CaseListingActivity.this, getActivity());
        binding.rvDefaultersStatus.setAdapter(casesTopAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(CaseIDGenerateClass.class);
    }

    private void getAllCases(String search) {
        showDialog();
        apiService.getAllCase("15", pageOffset, "1", search).enqueue(new NetworkCallback<AllCaseIDResponse>(getActivity()) {
            @Override
            protected void onSuccess(AllCaseIDResponse body) {
                binding.swipeRefresherStock.setRefreshing(false);
                AllCases.clear();
                if (body.getaCase() == null) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } else {
                    AllCases.clear();
                    totalPage = body.getaCase().getLastPage();
                    AllCases.addAll(body.getaCase().getData());
                    casesTopAdapter.notifyDataSetChanged();
                    //  AllCases=body.getCases();
                    // binding.rvDefaultersStatus.setAdapter(new CasesTopAdapter(body.getCases(), CaseListingActivity.this));
                }
            }
        });
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(CaseListingActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = ((Activity) CaseListingActivity.this).getLayoutInflater();
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
        TextView sub_user = (TextView) dialogView.findViewById(R.id.a10);
        LinearLayout case_extra = (LinearLayout) dialogView.findViewById(R.id.case_extra);
        case_extra.setVisibility(View.VISIBLE);
        TextView converted_by = (TextView) dialogView.findViewById(R.id.converted_by);
        TextView vehicle_no = (TextView) dialogView.findViewById(R.id.vehicle_no);
        TextView in_out = (TextView) dialogView.findViewById(R.id.in_out);
        LinearLayout staclLL = (LinearLayout) dialogView.findViewById(R.id.staclLL);
        View stackView = (View) dialogView.findViewById(R.id.stackView);
        staclLL.setVisibility(View.VISIBLE);
        stackView.setVisibility(View.VISIBLE);
        TextView stack_no = (TextView) dialogView.findViewById(R.id.stack_no);
        converted_by.setText("" + AllCases.get(position).getLeadConvFname()+" "+AllCases.get(position).getLeadConvLname());
        vehicle_no.setText("" + AllCases.get(position).getVehicleNo());
        in_out.setText("" + AllCases.get(position).getInOut());
        case_id.setText(getResources().getString(R.string.case_idd));
        sub_user.setText(getResources().getString(R.string.sub_user));
        lead_id.setText("" + AllCases.get(position).getCaseId());
        genrated_by.setText("" + AllCases.get(position).getLeadGenFname()+" "+AllCases.get(position).getLeadGenLname());
        customer_name.setText("" + AllCases.get(position).getCustFname());
        location_title.setText("" + AllCases.get(position).getLocation());
        phone_no.setText("" + AllCases.get(position).getPhone());
        commodity_name.setText(AllCases.get(position).getCateName() + "(" + AllCases.get(position).getCommodityType() + ")");
        est_quantity_nmae.setText("" + AllCases.get(position).getTotalWeight());
        terminal_name.setText("" + AllCases.get(position).getTerminalName() + " " + AllCases.get(position).getWarehouseCode());
        purpose_name.setText("" + AllCases.get(position).getPurpose());
        commitemate_date.setText((AllCases.get(position).getFpoUsers()));
        create_date.setText("" + AllCases.get(position).getCreatedAt());
        stack_no.setText("" + AllCases.get(position).getStack_number());

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
