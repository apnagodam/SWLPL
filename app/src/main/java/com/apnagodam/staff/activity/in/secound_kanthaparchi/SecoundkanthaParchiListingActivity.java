package com.apnagodam.staff.activity.in.secound_kanthaparchi;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
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
import com.apnagodam.staff.activity.in.first_kantaparchi.UploadFirstkantaParchiClass;
import com.apnagodam.staff.activity.in.first_quality_reports.FirstQualityReportListingActivity;
import com.apnagodam.staff.adapter.FirstQualityReportAdapter;
import com.apnagodam.staff.adapter.FirstkanthaparchiAdapter;
import com.apnagodam.staff.adapter.SecoundkanthaparchiAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.FirstQuilityReportListResponse;
import com.apnagodam.staff.module.FirstkanthaParchiListResponse;
import com.apnagodam.staff.module.SecoundkanthaParchiListResponse;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;

import java.util.ArrayList;
import java.util.List;


public class SecoundkanthaParchiListingActivity extends BaseActivity<ActivityListingBinding> {
    private String firstkantaParchiFile, TruckImage;
    private SecoundkanthaparchiAdapter secoundkanthaparchiAdapter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<SecoundkanthaParchiListResponse.Datum> AllCases;
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
        binding.titleHeader.setText(getResources().getString(R.string.secoundkanta_parchi));
        binding.tvId.setText(getResources().getString(R.string.case_idd));
        binding.tvMoreView.setText(getResources().getString(R.string.more_view_truck));
        binding.tvPhone.setText(getResources().getString(R.string.kanta_parchi));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
      /*  binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(SecoundkanthaParchiListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(SecoundkanthaParchiListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);*/
        getAllCases();
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
                    getAllCases();
                }
            }
        });
        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalPage != pageOffset) {
                    pageOffset++;
                    getAllCases();
                }
            }
        });
    }
    private void setAdapter() {
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(SecoundkanthaParchiListingActivity.this, LinearLayoutManager.VERTICAL));
        binding.rvDefaultersStatus.setHasFixedSize(true);
        binding.rvDefaultersStatus.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(SecoundkanthaParchiListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);
        secoundkanthaparchiAdapter = new SecoundkanthaparchiAdapter(AllCases, SecoundkanthaParchiListingActivity.this,getActivity());
        binding.rvDefaultersStatus.setAdapter(secoundkanthaparchiAdapter);

    }
    private void getAllCases() {
        showDialog();
        apiService.getS_kanthaParchiList("25", ""+pageOffset,"IN").enqueue(new NetworkCallback<SecoundkanthaParchiListResponse>(getActivity()) {
            @Override
            protected void onSuccess(SecoundkanthaParchiListResponse body) {
                if (body.getSecoundKataParchiDatum() == null ) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } else {
                    AllCases.clear();
                    totalPage = body.getSecoundKataParchiDatum().getLastPage();
                    AllCases.addAll(body.getSecoundKataParchiDatum().getData());
                    secoundkanthaparchiAdapter.notifyDataSetChanged();
                  //  AllCases = body.getData();
                  //  binding.rvDefaultersStatus.setAdapter(new SecoundkanthaparchiAdapter(body.getData(), SecoundkanthaParchiListingActivity.this));
                }
            }
        });
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(SecoundkanthaParchiListingActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = ((Activity) SecoundkanthaParchiListingActivity.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dilog_kanta_parchi, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
        TextView case_id = (TextView) dialogView.findViewById(R.id.a1);
        TextView lead_id = (TextView) dialogView.findViewById(R.id.lead_id);
        TextView customer_name = (TextView) dialogView.findViewById(R.id.customer_name);
        ImageView kanta_parchi_file = (ImageView) dialogView.findViewById(R.id.kanta_parchi_file);
        ImageView truck_file = (ImageView) dialogView.findViewById(R.id.truck_file);
        TextView notes = (TextView) dialogView.findViewById(R.id.notes);
        TextView gate_pass = (TextView) dialogView.findViewById(R.id.gate_pass);
        TextView user = (TextView) dialogView.findViewById(R.id.user);
        TextView coldwin = (TextView) dialogView.findViewById(R.id.coldwin);
        TextView purchase_details = (TextView) dialogView.findViewById(R.id.purchase_details);
        TextView loan_details = (TextView) dialogView.findViewById(R.id.loan_details);
        TextView selas_details = (TextView) dialogView.findViewById(R.id.selas_details);
        ////
        if (AllCases.get(position).getFile() == null || AllCases.get(position).getFile().isEmpty()) {
            kanta_parchi_file.setVisibility(View.GONE);
        }
        if (AllCases.get(position).getFile2() == null || AllCases.get(position).getFile2().isEmpty()) {
            truck_file.setVisibility(View.GONE);
        }
        case_id.setText(getResources().getString(R.string.case_idd));
        lead_id.setText("" + AllCases.get(position).getCaseId());
        customer_name.setText("" + AllCases.get(position).getCustFname());
        notes.setText("" + ((AllCases.get(position).getNotes()) != null ? AllCases.get(position).getNotes() : "N/A"));
        gate_pass.setText("Gaatepass/CDF Name : " + ((AllCases.get(position).getGatePassCdfUserName()) != null ? AllCases.get(position).getGatePassCdfUserName() : "N/A"));
        coldwin.setText("ColdWin Name: " + ((AllCases.get(position).getColdwinName()) != null ? AllCases.get(position).getColdwinName() : "N/A"));
        user.setText("User : " + ((AllCases.get(position).getFpoUserId()) != null ? AllCases.get(position).getFpoUserId() : "N/A"));
        purchase_details.setText("purchase Details : " + ((AllCases.get(position).getPurchaseName()) != null ? AllCases.get(position).getPurchaseName() : "N/A"));
        loan_details.setText("Loan Details : " + ((AllCases.get(position).getLoanName()) != null ? AllCases.get(position).getLoanName() : "N/A"));
        selas_details.setText("Sale Details : " + ((AllCases.get(position).getSaleName()) != null ? AllCases.get(position).getSaleName() : "N/A"));
        /////
        firstkantaParchiFile = Constants.Secound_kata + AllCases.get(position).getFile();
        TruckImage = Constants.Secound_kata + AllCases.get(position).getFile2();
        kanta_parchi_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(SecoundkanthaParchiListingActivity.this, R.layout.popup_photo_full, view, firstkantaParchiFile, null);
            }
        });
        truck_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(SecoundkanthaParchiListingActivity.this, R.layout.popup_photo_full, view, TruckImage, null);
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

    public void checkVeehicleNo(int postion) {
        Bundle bundle = new Bundle();
        bundle.putString("user_name", AllCases.get(postion).getCustFname());
        bundle.putString("case_id", AllCases.get(postion).getCaseId());
        startActivity(UploadSecoundkantaParchiClass.class, bundle);
    }

}
