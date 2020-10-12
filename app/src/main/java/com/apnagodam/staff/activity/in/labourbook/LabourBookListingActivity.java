package com.apnagodam.staff.activity.in.labourbook;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.in.truckbook.TruckUploadDetailsClass;
import com.apnagodam.staff.adapter.LaabourBookAdapter;
import com.apnagodam.staff.adapter.TruckBookAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.AllLabourBookListResponse;
import com.apnagodam.staff.module.AllTruckBookListResponse;

import java.util.List;


public class LabourBookListingActivity extends BaseActivity<ActivityListingBinding> {
    private List<AllLabourBookListResponse.CurrentPageCollection> AllCases;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_listing;
    }

    @Override
    protected void setUp() {
        setSupportActionBar(binding.toolbar);
        binding.titleHeader.setText(getResources().getString(R.string.labour_book));
        binding.tvId.setText(getResources().getString(R.string.case_idd));
        binding.tvMoreView.setText(getResources().getString(R.string.more_view_truck));
        binding.tvPhone.setText(getResources().getString(R.string.labour_book));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(LabourBookListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(LabourBookListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);
        getAllCases();
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(StaffDashBoardActivity.class);
            }
        });
    }

    private void getAllCases() {
        apiService.getLabourBookList("25", "1","IN").enqueue(new NetworkCallback<AllLabourBookListResponse>(getActivity()) {
            @Override
            protected void onSuccess(AllLabourBookListResponse body) {
                if (body.getCurrentPageCollection() == null || body.getCurrentPageCollection().isEmpty()) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                } else {
                    AllCases = body.getCurrentPageCollection();
                    binding.rvDefaultersStatus.setAdapter(new LaabourBookAdapter(body.getCurrentPageCollection(), LabourBookListingActivity.this));
                }
            }
        });
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(LabourBookListingActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = ((Activity) LabourBookListingActivity.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.lead_view, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
        TextView case_id = (TextView) dialogView.findViewById(R.id.a1);
        TextView lead_id = (TextView) dialogView.findViewById(R.id.lead_id);
        TextView username = (TextView) dialogView.findViewById(R.id.a2);
        TextView genrated_by = (TextView) dialogView.findViewById(R.id.genrated_by);
        TextView customer_name = (TextView) dialogView.findViewById(R.id.customer_name);
        TextView vehicle_no = (TextView) dialogView.findViewById(R.id.a4);
        TextView phone_no = (TextView) dialogView.findViewById(R.id.phone_no);
        TextView location_title = (TextView) dialogView.findViewById(R.id.location_title);
        TextView inteerset_rate = (TextView) dialogView.findViewById(R.id.a6);
        TextView commodity_name = (TextView) dialogView.findViewById(R.id.commodity_name);
        TextView transport_rate = (TextView) dialogView.findViewById(R.id.a7);
        TextView est_quantity_nmae = (TextView) dialogView.findViewById(R.id.est_quantity_nmae);
        TextView loan = (TextView) dialogView.findViewById(R.id.a8);
        TextView terminal_name = (TextView) dialogView.findViewById(R.id.terminal_name);
        TextView price = (TextView) dialogView.findViewById(R.id.a9);
        TextView purpose_name = (TextView) dialogView.findViewById(R.id.purpose_name);
        TextView rent = (TextView) dialogView.findViewById(R.id.a10);
        TextView commitemate_date = (TextView) dialogView.findViewById(R.id.commitemate_date);
        LinearLayout llcreate = (LinearLayout) dialogView.findViewById(R.id.llcreate);
        View viewcreate = (View) dialogView.findViewById(R.id.viewcreate);
        llcreate.setVisibility(View.GONE);
        viewcreate.setVisibility(View.GONE);
        LinearLayout price_extra = (LinearLayout) dialogView.findViewById(R.id.price_extra);
        price_extra.setVisibility(View.VISIBLE);
        TextView gate_pass = (TextView) dialogView.findViewById(R.id.gate_pass);
        TextView user = (TextView) dialogView.findViewById(R.id.user);
        TextView coldwin = (TextView) dialogView.findViewById(R.id.coldwin);
        TextView purchase_details = (TextView) dialogView.findViewById(R.id.purchase_details);
        TextView loan_details = (TextView) dialogView.findViewById(R.id.loan_details);
        TextView selas_details = (TextView) dialogView.findViewById(R.id.selas_details);

        ////
        case_id.setText(getResources().getString(R.string.case_idd));
        lead_id.setText("" + AllCases.get(position).getCaseId());
        username.setText(getResources().getString(R.string.labour_contractor));
        genrated_by.setText("" + ((AllCases.get(position).getLabourContractor()) != null ? AllCases.get(position).getLabourContractor() : "N/A"));
        vehicle_no.setText(getResources().getString(R.string.contractor_phone));
        phone_no.setText("" + ((AllCases.get(position).getContractorNo()) != null ? AllCases.get(position).getContractorNo() : "N/A"));
        customer_name.setText("" + AllCases.get(position).getCustFname());
        location_title.setText("" + ((AllCases.get(position).getLocation()) != null ? AllCases.get(position).getLocation() : "N/A"));
        inteerset_rate.setText(getResources().getString(R.string.labour_rate));
        commodity_name.setText("" + ((AllCases.get(position).getLabourRatePerBags()) != null ? AllCases.get(position).getLabourRatePerBags() : "N/A"));
        transport_rate.setText(getResources().getString(R.string.labour_total));
        est_quantity_nmae.setText("" + ((AllCases.get(position).getTotalLabour()) != null ? AllCases.get(position).getTotalLabour() : "N/A"));
        loan.setText(getResources().getString(R.string.booking_date));
        terminal_name.setText("" + ((AllCases.get(position).getBookingDate()) != null ? AllCases.get(position).getBookingDate() : "N/A"));
        price.setText(getResources().getString(R.string.total_bags));
        purpose_name.setText("" + ((AllCases.get(position).getTotalBags()) != null ? AllCases.get(position).getTotalBags() : "N/A"));
        rent.setText(getResources().getString(R.string.notes));
        commitemate_date.setText("" + ((AllCases.get(position).getNotes()) != null ? AllCases.get(position).getNotes() : "N/A"));
        gate_pass.setText("Gaatepass/CDF Name : " + ((AllCases.get(position).getGatePassCdfUserName()) != null ? AllCases.get(position).getGatePassCdfUserName() : "N/A"));
        coldwin.setText("ColdWin Name: " + ((AllCases.get(position).getColdwinName()) != null ? AllCases.get(position).getColdwinName() : "N/A"));
        user.setText("User : " + ((AllCases.get(position).getFpoUserId()) != null ? AllCases.get(position).getFpoUserId() : "N/A"));
        purchase_details.setText("purchase Details : " + ((AllCases.get(position).getPurchaseName()) != null ? AllCases.get(position).getPurchaseName() : "N/A"));
        loan_details.setText("Loan Details : " + ((AllCases.get(position).getLoanName()) != null ? AllCases.get(position).getLoanName() : "N/A"));
        selas_details.setText("Sale Details : " + ((AllCases.get(position).getSaleName()) != null ? AllCases.get(position).getSaleName() : "N/A"));
        /////
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
        startActivity(LabourBookUploadClass.class, bundle);
    }

}
