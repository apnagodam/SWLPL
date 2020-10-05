package com.apnagodam.staff.activity;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.R;
import com.apnagodam.staff.adapter.LeadsTopAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.interfaces.LeadsEditClickListener;
import com.apnagodam.staff.module.AllLeadsResponse;
import com.apnagodam.staff.utils.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class LeadListingActivity extends BaseActivity<ActivityListingBinding> {
    private LeadsTopAdapter leadsTopAdapter;
    private List<AllLeadsResponse.Lead> LeadListData;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_listing;
    }

    @Override
    protected void setUp() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(LeadListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(LeadListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);
        getLeadsListing();
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(StaffDashBoardActivity.class);
            }
        });
    }

    private void getLeadsListing() {
        apiService.getAllLeads().enqueue(new NetworkCallback<AllLeadsResponse>(getActivity()) {
            @Override
            protected void onSuccess(AllLeadsResponse body) {
                LeadListData = body.getLeads();

                if (body.getLeads() == null || body.getLeads().isEmpty()) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                } else {
                    binding.rvDefaultersStatus.setAdapter(new LeadsTopAdapter(body.getLeads(), LeadListingActivity.this));
                }
            }
        });
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(LeadListingActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = LeadListingActivity.this.getLayoutInflater();
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
        TextView create_date = dialogView.findViewById(R.id.create_date);
        lead_id.setText(""+LeadListData.get(position).getId());
        genrated_by.setText(""+LeadListData.get(position).getFirstName()+" "+LeadListData.get(position).getLastName());
        customer_name.setText(""+LeadListData.get(position).getCustomerName());
        location_title.setText(""+LeadListData.get(position).getLocation());
        phone_no.setText(""+LeadListData.get(position).getPhone());
        commodity_name.setText(LeadListData.get(position).getCateName()+"("+LeadListData.get(position).getCommodityType()+")");
        est_quantity_nmae.setText(""+LeadListData.get(position).getQuantity());
        terminal_name.setText(""+LeadListData.get(position).getTerminalName());
        purpose_name.setText(""+LeadListData.get(position).getPurpose());
        commitemate_date.setText(Utility.timeFromdateTime(LeadListData.get(position).getCommodityDate()));
        create_date.setText(""+LeadListData.get(position).getCreatedAt());

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
