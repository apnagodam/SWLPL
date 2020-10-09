package com.apnagodam.staff.activity.in.gatepass;

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
import com.apnagodam.staff.activity.in.first_kantaparchi.UploadFirstkantaParchiClass;
import com.apnagodam.staff.adapter.FirstkanthaparchiAdapter;
import com.apnagodam.staff.adapter.GatepassAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.FirstkanthaParchiListResponse;
import com.apnagodam.staff.module.GatePassListResponse;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;

import java.util.List;


public class GatePassListingActivity extends BaseActivity<ActivityListingBinding> {
    private List<GatePassListResponse.GatePassData> AllCases;
    private String firstkantaParchiFile, TruckImage;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_listing;
    }

    @Override
    protected void setUp() {
        setSupportActionBar(binding.toolbar);
        binding.titleHeader.setText(getResources().getString(R.string.gate_pass_list));
        binding.tvId.setText(getResources().getString(R.string.case_idd));
        binding.tvMoreView.setText(getResources().getString(R.string.more_view_truck));
        binding.tvPhone.setText(getResources().getString(R.string.gate_passs));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(GatePassListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(GatePassListingActivity.this, LinearLayoutManager.VERTICAL, false);
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
        apiService.getGatePass("15", "1").enqueue(new NetworkCallback<GatePassListResponse>(getActivity()) {
            @Override
            protected void onSuccess(GatePassListResponse body) {
                if (body.getData() == null || body.getData().isEmpty()) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                } else {
                    AllCases = body.getData();
                    binding.rvDefaultersStatus.setAdapter(new GatepassAdapter(body.getData(), GatePassListingActivity.this));
                }
            }
        });
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(GatePassListingActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = ((Activity) GatePassListingActivity.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dilog_kanta_parchi, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
        TextView case_id = (TextView) dialogView.findViewById(R.id.a1);
        TextView gatePass = (TextView) dialogView.findViewById(R.id.a4);
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
        TextView bags = (TextView) dialogView.findViewById(R.id.no_of_bags);
        TextView wieight = (TextView) dialogView.findViewById(R.id.total_weight);
        View view = (View) dialogView.findViewById(R.id.view);
        LinearLayout llview = (LinearLayout) dialogView.findViewById(R.id.llview);
        LinearLayout gate_pass_extra = (LinearLayout) dialogView.findViewById(R.id.gate_pass_extra);
        ////
        view.setVisibility(View.GONE);
        llview.setVisibility(View.GONE);
        gate_pass_extra.setVisibility(View.VISIBLE);
        gatePass.setText(getResources().getString(R.string.gate_passs_file));
        if (AllCases.get(position).getFile() == null || AllCases.get(position).getFile().isEmpty()) {
            kanta_parchi_file.setVisibility(View.GONE);
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
        bags.setText("" + ((AllCases.get(position).getNoOfBags()) != null ? AllCases.get(position).getNoOfBags() : "N/A"));
        wieight.setText("" + ((AllCases.get(position).getTotalWeight()) != null ? AllCases.get(position).getTotalWeight() : "N/A"));
        /////
        firstkantaParchiFile = Constants.gate_pass + AllCases.get(position).getFile();
        kanta_parchi_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(GatePassListingActivity.this, R.layout.popup_photo_full, view, firstkantaParchiFile, null);
            }
        });
        truck_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(GatePassListingActivity.this, R.layout.popup_photo_full, view, TruckImage, null);
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
        startActivity(UploadGatePassClass.class, bundle);
    }

}
