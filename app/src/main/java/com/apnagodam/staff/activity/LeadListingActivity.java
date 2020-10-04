package com.apnagodam.staff.activity;

import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.R;
import com.apnagodam.staff.adapter.CaseLeadsTopAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.AllLeadsResponse;

import java.util.ArrayList;
import java.util.List;


public class LeadListingActivity extends BaseActivity<ActivityListingBinding> {
    private CaseLeadsTopAdapter caseLeadsTopAdapter;

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
                if (body.getLeads() == null || body.getLeads().isEmpty()) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                }else {
                    binding.rvDefaultersStatus.setAdapter(new CaseLeadsTopAdapter(body.getLeads(), LeadListingActivity.this));
                }
            }
        });
    }
}
