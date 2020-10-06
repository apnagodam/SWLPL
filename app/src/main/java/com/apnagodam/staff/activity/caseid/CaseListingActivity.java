package com.apnagodam.staff.activity.caseid;

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
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.adapter.CasesTopAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.AllCaseIDResponse;

import java.util.List;


public class CaseListingActivity extends BaseActivity<ActivityListingBinding> {

    private List<AllCaseIDResponse.Case> AllCases;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_listing;
    }

    @Override
    protected void setUp() {
        setSupportActionBar(binding.toolbar);
        binding.titleHeader.setText(getResources().getString(R.string.case_list));
        binding.tvId.setText(getResources().getString(R.string.case_idd));
        binding.tvMoreView.setText(getResources().getString(R.string.status_title));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(CaseListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(CaseListingActivity.this, LinearLayoutManager.VERTICAL, false);
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
        apiService.getAllCase().enqueue(new NetworkCallback<AllCaseIDResponse>(getActivity()) {
            @Override
            protected void onSuccess(AllCaseIDResponse body) {
                if (body.getCases() == null || body.getCases().isEmpty()) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                }else {
                    AllCases=body.getCases();
                    binding.rvDefaultersStatus.setAdapter(new CasesTopAdapter(body.getCases(), CaseListingActivity.this));
                }
            }
        });
    }
    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(CaseListingActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = ((Activity) CaseListingActivity.this).getLayoutInflater();
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
        LinearLayout case_extra = (LinearLayout) dialogView.findViewById(R.id.case_extra);
        case_extra.setVisibility(View.VISIBLE);
        TextView converted_by = (TextView) dialogView.findViewById(R.id.converted_by);
        TextView vehicle_no = (TextView) dialogView.findViewById(R.id.vehicle_no);
        TextView in_out = (TextView) dialogView.findViewById(R.id.in_out);
        converted_by.setText(""+AllCases.get(position).getLeadGenFname());
        vehicle_no.setText(""+AllCases.get(position).getVehicleNo());
        in_out.setText(""+AllCases.get(position).getInOut());
        case_id.setText(getResources().getString(R.string.case_idd));
        sub_user.setText(getResources().getString(R.string.sub_user));
        lead_id.setText(""+AllCases.get(position).getCaseId());
        genrated_by.setText(""+AllCases.get(position).getLeadGenFname());
        customer_name.setText(""+AllCases.get(position).getCustFname());
        location_title.setText(""+AllCases.get(position).getLocation());
        phone_no.setText(""+AllCases.get(position).getPhone());
        commodity_name.setText(AllCases.get(position).getCateName()+"("+AllCases.get(position).getCommodityType()+")");
        est_quantity_nmae.setText(""+AllCases.get(position).getTotalWeight());
        terminal_name.setText(""+AllCases.get(position).getTerminalName()+" "+AllCases.get(position).getWarehouseCode());
        purpose_name.setText(""+AllCases.get(position).getPurpose());
        commitemate_date.setText((AllCases.get(position).getFpoUsers()));
        create_date.setText(""+AllCases.get(position).getCreatedAt());
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
