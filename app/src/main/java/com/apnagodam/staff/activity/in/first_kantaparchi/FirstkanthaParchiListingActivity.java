package com.apnagodam.staff.activity.in.first_kantaparchi;

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
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.caseid.CaseListingActivity;
import com.apnagodam.staff.activity.in.labourbook.LabourBookListingActivity;
import com.apnagodam.staff.activity.in.labourbook.LabourBookUploadClass;
import com.apnagodam.staff.adapter.FirstkanthaparchiAdapter;
import com.apnagodam.staff.adapter.LaabourBookAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.AllLabourBookListResponse;
import com.apnagodam.staff.module.FirstkanthaParchiListResponse;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;

import java.util.ArrayList;
import java.util.List;


public class FirstkanthaParchiListingActivity extends BaseActivity<ActivityListingBinding> {
    private FirstkanthaparchiAdapter firstkanthaparchiAdapter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<FirstkanthaParchiListResponse.Datum> AllCases;
    private String firstkantaParchiFile, TruckImage;

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
        binding.titleHeader.setText(getResources().getString(R.string.firstkanta_parchi_title));
        binding.tvId.setText(getResources().getString(R.string.case_idd));
        binding.tvMoreView.setText(getResources().getString(R.string.more_view_truck));
        binding.tvPhone.setText(getResources().getString(R.string.kanta_parchi));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
      /*  binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(FirstkanthaParchiListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(FirstkanthaParchiListingActivity.this, LinearLayoutManager.VERTICAL, false);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(FirstkanthaParchiListingActivity.this);
                LayoutInflater inflater = ((Activity) FirstkanthaParchiListingActivity.this).getLayoutInflater();
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
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(FirstkanthaParchiListingActivity.this, LinearLayoutManager.VERTICAL));
        binding.rvDefaultersStatus.setHasFixedSize(true);
        binding.rvDefaultersStatus.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(FirstkanthaParchiListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);
        firstkanthaparchiAdapter = new FirstkanthaparchiAdapter(AllCases, FirstkanthaParchiListingActivity.this,getActivity());
        binding.rvDefaultersStatus.setAdapter(firstkanthaparchiAdapter);

    }
    private void getAllCases(String search) {
        showDialog();
        apiService.getf_kanthaParchiList("10", ""+pageOffset,"IN",search).enqueue(new NetworkCallback<FirstkanthaParchiListResponse>(getActivity()) {
            @Override
            protected void onSuccess(FirstkanthaParchiListResponse body) {
                binding.swipeRefresherStock.setRefreshing(false);
                AllCases.clear();
                if (body.getFirstKataParchiData() == null ) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } else {
                    AllCases.clear();
                    totalPage = body.getFirstKataParchiData().getLastPage();
                    AllCases.addAll(body.getFirstKataParchiData().getData());
                    firstkanthaparchiAdapter.notifyDataSetChanged();
                 //   AllCases = body.getFirstKataParchiData();
                  //  binding.rvDefaultersStatus.setAdapter(new FirstkanthaparchiAdapter(body.getFirstKataParchiData(), FirstkanthaParchiListingActivity.this));
                }
            }
        });
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(FirstkanthaParchiListingActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = ((Activity) FirstkanthaParchiListingActivity.this).getLayoutInflater();
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
        firstkantaParchiFile = Constants.First_kata + AllCases.get(position).getFile();
        TruckImage = Constants.First_kata + AllCases.get(position).getFile2();
        kanta_parchi_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(FirstkanthaParchiListingActivity.this, R.layout.popup_photo_full, view, firstkantaParchiFile, null);
            }
        });
        truck_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(FirstkanthaParchiListingActivity.this, R.layout.popup_photo_full, view, TruckImage, null);
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
        startActivity(UploadFirstkantaParchiClass.class, bundle);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(StaffDashBoardActivity.class);
    }
}
