package com.apnagodam.staff.activity;

import android.app.Activity;
import android.graphics.Rect;
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
import com.apnagodam.staff.adapter.CasesTopAdapter;
import com.apnagodam.staff.adapter.PricingAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.AllCaseIDResponse;
import com.apnagodam.staff.module.AllpricingResponse;

import java.util.List;


public class InPricingListingActivity extends BaseActivity<ActivityListingBinding> {
    private List<AllpricingResponse.CaseGen> AllCases;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_listing;
    }

    @Override
    protected void setUp() {
        setSupportActionBar(binding.toolbar);
        binding.titleHeader.setText(getResources().getString(R.string.pricing_title));
        binding.tvId.setText(getResources().getString(R.string.case_idd));
        binding.tvMoreView.setText(getResources().getString(R.string.action));
        binding.tvPhone.setText(getResources().getString(R.string.set_pricing));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(InPricingListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(InPricingListingActivity.this, LinearLayoutManager.VERTICAL, false);
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
        apiService.getAllpricingList().enqueue(new NetworkCallback<AllpricingResponse>(getActivity()) {
            @Override
            protected void onSuccess(AllpricingResponse body) {
                if (body.getCases() == null || body.getCases().isEmpty()) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                }else {
                    AllCases=body.getCases();
                    binding.rvDefaultersStatus.setAdapter(new PricingAdapter(body.getCases(), InPricingListingActivity.this));
                }
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
        TextView sub_user = (TextView) dialogView.findViewById(R.id.a10);
        TextView username = (TextView) dialogView.findViewById(R.id.a2);
        TextView processing_fees = (TextView) dialogView.findViewById(R.id.a5);
        TextView inteerset_rate = (TextView) dialogView.findViewById(R.id.a6);
        TextView loan = (TextView) dialogView.findViewById(R.id.a8);
        TextView price = (TextView) dialogView.findViewById(R.id.a9);
        TextView rent = (TextView) dialogView.findViewById(R.id.a10);
        TextView labour_rent = (TextView) dialogView.findViewById(R.id.a11);
        TextView notes = (TextView) dialogView.findViewById(R.id.a12);
        LinearLayout case_extra = (LinearLayout) dialogView.findViewById(R.id.case_extra);
        case_extra.setVisibility(View.VISIBLE);
        TextView converted_by = (TextView) dialogView.findViewById(R.id.converted_by);
        TextView vehicle_no = (TextView) dialogView.findViewById(R.id.vehicle_no);
        TextView in_out = (TextView) dialogView.findViewById(R.id.in_out);
       converted_by.setText(""+((AllCases.get(position).getNotes())!=null?AllCases.get(position).getNotes():"N/A"));
        vehicle_no.setText(""+AllCases.get(position).getVehicleNo());
        in_out.setText(""+AllCases.get(position).getInOut());
        case_id.setText(getResources().getString(R.string.case_idd));
        notes.setText(getResources().getString(R.string.notes));
        rent.setText(getResources().getString(R.string.labour));
        labour_rent.setText(getResources().getString(R.string.labour_rent));
        loan.setText(getResources().getString(R.string.price));
        price.setText(getResources().getString(R.string.commodity_price));
        processing_fees.setText(getResources().getString(R.string.processing_fees));
        inteerset_rate.setText(getResources().getString(R.string.inteerset_rate));
        username.setText(getResources().getString(R.string.trans_type));
        sub_user.setText(""+((AllCases.get(position).getRent())!=null?AllCases.get(position).getRent():"N/A"));
        lead_id.setText(""+AllCases.get(position).getCaseId());
        genrated_by.setText(""+((AllCases.get(position).getTransactionType())!=null?AllCases.get(position).getTransactionType():"N/A"));
        customer_name.setText(""+AllCases.get(position).getCustFname());
        location_title.setText(""+((AllCases.get(position).getProcessingFees())!=null?AllCases.get(position).getProcessingFees():"N/A"));
        phone_no.setText(""+AllCases.get(position).getPhone());
       commodity_name.setText(""+((AllCases.get(position).getInterestRate())!=null?AllCases.get(position).getInterestRate():"N/A"));
        est_quantity_nmae.setText(""+AllCases.get(position).getTotalWeight());
       terminal_name.setText(""+((AllCases.get(position).getLoanTPerAmount())!=null?AllCases.get(position).getLoanTPerAmount():"N/A"));
        purpose_name.setText(""+((AllCases.get(position).getPrice())!=null?AllCases.get(position).getPrice():"N/A"));

        commitemate_date.setText((AllCases.get(position).getFpoUsers()));
        create_date.setText(""+((AllCases.get(position).getLabourRate())!=null?AllCases.get(position).getLabourRate():"N/A"));
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
