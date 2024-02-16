package com.apnagodam.staff.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Response.ResponseFastcaseList;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.truckbook.TruckBookListingActivity;
import com.apnagodam.staff.adapter.FastCaseListAdapter;
import com.apnagodam.staff.adapter.TruckBookAdapter;
import com.apnagodam.staff.databinding.ActivityFastCaseBinding;
import com.apnagodam.staff.databinding.ActivityFastCaseListBinding;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.AllCaseIDResponse;

public class FastCaseListActivity extends BaseActivity<ActivityFastCaseListBinding> {

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fast_case_list;
    }

    @Override
    protected void setUp() {

        mContext = FastCaseListActivity.this;

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        getFastCaseList();

    }


    private void getFastCaseList() {
        showDialog();
        apiService.getFastCaseList().enqueue(new NetworkCallback<ResponseFastcaseList>(getActivity()) {
            @Override
            protected void onSuccess(ResponseFastcaseList body) {
                //        binding.swipeRefresherStock.setRefreshing(false);

                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                binding.rvFastCaseList.setLayoutManager(horizontalLayoutManager);
                FastCaseListAdapter fastCaseListAdapter = new FastCaseListAdapter(body.getData(), mContext);
                binding.rvFastCaseList.setAdapter(fastCaseListAdapter);

            }
        });
    }

}