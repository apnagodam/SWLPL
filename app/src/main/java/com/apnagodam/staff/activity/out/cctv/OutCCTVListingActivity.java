package com.apnagodam.staff.activity.out.cctv;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.apnagodam.staff.activity.out.f_katha_parchi.OutUploadFirrstkantaParchiClass;
import com.apnagodam.staff.adapter.OutCCTVAdapter;
import com.apnagodam.staff.adapter.OutFirstkanthaparchiAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.CCTVListPojo;
import com.apnagodam.staff.module.FirstkanthaParchiListResponse;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;

import java.util.ArrayList;
import java.util.List;


public class OutCCTVListingActivity extends BaseActivity<ActivityListingBinding> {
    private OutCCTVAdapter outFirstkanthaparchiAdapter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<CCTVListPojo.Datum> AllCases;
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
        binding.titleHeader.setText("CCTV List");
        binding.tvId.setText(getResources().getString(R.string.case_idd));
        binding.tvMoreView.setText(getResources().getString(R.string.more_view_truck));
        binding.tvPhone.setText("CCTV");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(OutCCTVListingActivity.this);
                LayoutInflater inflater = ((Activity) OutCCTVListingActivity.this).getLayoutInflater();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(StaffDashBoardActivity.class);
    }

    private void setAdapter() {
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(OutCCTVListingActivity.this, LinearLayoutManager.VERTICAL));
        binding.rvDefaultersStatus.setHasFixedSize(true);
        binding.rvDefaultersStatus.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(OutCCTVListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);
        outFirstkanthaparchiAdapter = new OutCCTVAdapter(AllCases, OutCCTVListingActivity.this, getActivity());
        binding.rvDefaultersStatus.setAdapter(outFirstkanthaparchiAdapter);
    }

    private void getAllCases(String search) {
        showDialog();
        apiService.getCCTVList("10", "" + pageOffset, "OUT",search).enqueue(new NetworkCallback<CCTVListPojo>(getActivity()) {
            @Override
            protected void onSuccess(CCTVListPojo body) {
                binding.swipeRefresherStock.setRefreshing(false);
                AllCases.clear();
                if (body.getData() == null) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } else {
                    AllCases.clear();
                    totalPage = body.getData().getLastPage();
                    AllCases.addAll(body.getData().getData());
                    outFirstkanthaparchiAdapter.notifyDataSetChanged();
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(OutCCTVListingActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = OutCCTVListingActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dilog_kanta_parchi, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = dialogView.findViewById(R.id.cancel_btn);
        TextView case_id = dialogView.findViewById(R.id.a1);
        TextView CCTVfile1 = dialogView.findViewById(R.id.a4);
        TextView CCTVfile2 = dialogView.findViewById(R.id.a5);

        TextView lead_id = dialogView.findViewById(R.id.lead_id);
        TextView customer_name = dialogView.findViewById(R.id.customer_name);
        ImageView kanta_parchi_file = dialogView.findViewById(R.id.kanta_parchi_file);
        ImageView truck_file = dialogView.findViewById(R.id.truck_file);
        TextView notes = dialogView.findViewById(R.id.notes);
        TextView gate_pass = dialogView.findViewById(R.id.gate_pass);
        TextView user = dialogView.findViewById(R.id.user);
        TextView coldwin = dialogView.findViewById(R.id.coldwin);
        TextView purchase_details = dialogView.findViewById(R.id.purchase_details);
        TextView loan_details = dialogView.findViewById(R.id.loan_details);
        TextView selas_details = dialogView.findViewById(R.id.selas_details);
        ////
        if (AllCases.get(position).getFile() == null || AllCases.get(position).getFile().isEmpty()) {
            kanta_parchi_file.setVisibility(View.GONE);
        }
        if (AllCases.get(position).getFile2() == null || AllCases.get(position).getFile2().isEmpty()) {
            truck_file.setVisibility(View.GONE);
        }
        case_id.setText(getResources().getString(R.string.case_idd));
        CCTVfile1.setText("CCTV File");
        CCTVfile2.setText("CCTV File 2");
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
        firstkantaParchiFile = Constants.CCTV + AllCases.get(position).getFile();
        TruckImage = Constants.CCTV + AllCases.get(position).getFile2();
        kanta_parchi_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstkantaParchiFile!=null)
                new PhotoFullPopupWindow(OutCCTVListingActivity.this, R.layout.popup_photo_full, view, firstkantaParchiFile, null);
            }
        });
        truck_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TruckImage!=null)
                new PhotoFullPopupWindow(OutCCTVListingActivity.this, R.layout.popup_photo_full, view, TruckImage, null);
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
        startActivity(OutUploadCCTVFileClass.class, bundle);
    }

}
