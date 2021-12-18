package com.apnagodam.staff.activity.kra;

import android.view.View;
import android.view.animation.Animation;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.adapter.KRAHistoryListAdpter;
import com.apnagodam.staff.adapter.UploadKRAListAdpter;
import com.apnagodam.staff.databinding.UploadKraBinding;
import com.apnagodam.staff.module.AllConvancyList;

import java.util.ArrayList;
import java.util.List;

public class UploadMyKRAClass extends BaseActivity<UploadKraBinding> {
    private UploadKRAListAdpter uploadKRAListAdpter;
    private List<AllConvancyList.Datum> getOrdersList;
    private List<AllConvancyList.Datum> tempraryList;
    @Override
    protected int getLayoutResId() {
        return R.layout.upload_kra;
    }

    @Override
    protected void setUp() {
        getOrdersList = new ArrayList();
        tempraryList = new ArrayList();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(MyKRAHistoryListClass.class);
            }
        });
        setDataAdapter();
        getConvencyList("");
    }

    private void getConvencyList(String SerachKey) {
        showDialog();
        apiService.getConvancyList("15", 1, SerachKey).enqueue(new NetworkCallback<AllConvancyList>(getActivity()) {
            @Override
            protected void onSuccess(AllConvancyList body) {
                tempraryList.clear();
                getOrdersList.clear();
                if (body.getData().getDataa().size() == 0) {
                    binding.fieldStockList.setVisibility(View.GONE);
                } else {
                    binding.fieldStockList.setVisibility(View.VISIBLE);
                    getOrdersList.clear();
                    tempraryList.clear();
                    getOrdersList.addAll(body.getData().getDataa());
                    tempraryList.addAll(getOrdersList);
                    uploadKRAListAdpter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setDataAdapter() {
        binding.fieldStockList.addItemDecoration(new DividerItemDecoration(UploadMyKRAClass.this, LinearLayoutManager.HORIZONTAL));
        binding.fieldStockList.setHasFixedSize(true);
        binding.fieldStockList.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(UploadMyKRAClass.this, LinearLayoutManager.VERTICAL, false);
        binding.fieldStockList.setLayoutManager(horizontalLayoutManager);
        uploadKRAListAdpter = new UploadKRAListAdpter(getOrdersList, UploadMyKRAClass.this, getActivity());
        binding.fieldStockList.setAdapter(uploadKRAListAdpter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(MyKRAHistoryListClass.class);
    }

}
