package com.apnagodam.staff.activity.lead;

import android.content.Intent;
import android.graphics.Rect;
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
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.adapter.LeadsTopAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.AllLeadsResponse;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.Utility;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class LeadListingActivity extends BaseActivity<ActivityListingBinding> {

    private LeadsTopAdapter leadsTopAdapter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<AllLeadsResponse.Datum> AllCases;

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);
       /* binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(LeadListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(LeadListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);*/
        binding.TitleWaititngEdititng.setVisibility(View.VISIBLE);
//        binding.layoutLoader.setVisibility(View.GONE);
        getLeadsListing();
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
                    getLeadsListing();
                }
            }
        });
        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalPage != pageOffset) {
                    pageOffset++;
                    getLeadsListing();
                }
            }
        });
    }
    private void setAdapter() {
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(LeadListingActivity.this, LinearLayoutManager.VERTICAL));
        binding.rvDefaultersStatus.setHasFixedSize(true);
        binding.rvDefaultersStatus.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(LeadListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);
        leadsTopAdapter = new LeadsTopAdapter(AllCases, LeadListingActivity.this,getActivity());
        binding.rvDefaultersStatus.setAdapter(leadsTopAdapter);

    }
    private void getLeadsListing() {
        apiService.getAllLeads("25", pageOffset,"IN").enqueue(new NetworkCallback<AllLeadsResponse>(getActivity()) {
            @Override
            protected void onSuccess(AllLeadsResponse body) {

                if (body.getLeads() == null) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } else {
                    AllCases.clear();
                    totalPage = body.getLeads().getLastPage();
                    AllCases.addAll(body.getLeads().getData());
                    leadsTopAdapter.notifyDataSetChanged();
                 //   binding.rvDefaultersStatus.setAdapter(new LeadsTopAdapter(body.getLeads(), LeadListingActivity.this));
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
        lead_id.setText(""+AllCases.get(position).getId());
        genrated_by.setText(""+AllCases.get(position).getFirstName()+" "+AllCases.get(position).getLastName());
        customer_name.setText(""+AllCases.get(position).getCustomerName());
        location_title.setText(""+AllCases.get(position).getLocation());
        phone_no.setText(""+AllCases.get(position).getPhone());
        commodity_name.setText(AllCases.get(position).getCateName()+"("+AllCases.get(position).getCommodityType()+")");
        est_quantity_nmae.setText(""+AllCases.get(position).getQuantity());
        terminal_name.setText(""+AllCases.get(position).getTerminalName());
        purpose_name.setText(""+AllCases.get(position).getPurpose());
        commitemate_date.setText(Utility.timeFromdateTime(AllCases.get(position).getCommodityDate()));
        create_date.setText(""+AllCases.get(position).getCreatedAt());

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
   /* public void editLead(int postion) {
        Intent intent=new Intent();
        intent.putExtra(Constants.LeadListData,  AllCases.get(postion));
        setResult(2,intent);
        finish();//finishing activity
    }*/
    public void editLeads(AllLeadsResponse.Datum lead){
        Intent intent=new Intent();
        intent.putExtra(Constants.LeadListData, (Serializable) lead);
        setResult(2,intent);
        finish();//finishing activity
    }
  /*  public void editLead(AllLeadsResponse.Lead lead){
        Intent intent=new Intent();
        intent.putExtra(Constants.LeadListData, (Serializable) lead);
        setResult(2,intent);
        finish();//finishing activity
    }*/
}
