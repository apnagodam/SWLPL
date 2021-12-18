package com.apnagodam.staff.activity.in.ivr;

import android.app.Activity;
import android.graphics.Rect;
import android.media.MediaPlayer;
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
import com.apnagodam.staff.Network.Request.EmpFeedbackRequest;
import com.apnagodam.staff.Network.Request.EmpUpdateGartepassRequest;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.dispaldgeinventory.ApprovalRequestDispladgeListClass;
import com.apnagodam.staff.activity.out.ivr.OutUploadIVRFileClass;
import com.apnagodam.staff.adapter.INIVRVAdapter;
import com.apnagodam.staff.adapter.OutIVRVAdapter;
import com.apnagodam.staff.databinding.ActivityListingBinding;
import com.apnagodam.staff.module.FirstkanthaParchiListResponse;
import com.apnagodam.staff.module.IVRListPojo;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class INIVRListingActivity extends BaseActivity<ActivityListingBinding> {
    private INIVRVAdapter inivrvAdapter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<IVRListPojo.Datum> AllCases;
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
        binding.titleHeader.setText(R.string.ivr_tagging);
        binding.tvId.setText(getResources().getString(R.string.case_idd));
        binding.tvMoreView.setText(getResources().getString(R.string.more_view_truck));
        binding.tvPhone.setText(R.string.ivr_upload);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(INIVRListingActivity.this);
                LayoutInflater inflater = ((Activity) INIVRListingActivity.this).getLayoutInflater();
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
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(INIVRListingActivity.this, LinearLayoutManager.VERTICAL));
        binding.rvDefaultersStatus.setHasFixedSize(true);
        binding.rvDefaultersStatus.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(INIVRListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);
        inivrvAdapter = new INIVRVAdapter(AllCases, INIVRListingActivity.this, getActivity());
        binding.rvDefaultersStatus.setAdapter(inivrvAdapter);
    }

    private void getAllCases(String search) {
        showDialog();
        apiService.getIVRList("10", "" + pageOffset, "IN",search).enqueue(new NetworkCallback<IVRListPojo>(getActivity()) {
            @Override
            protected void onSuccess(IVRListPojo body) {
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
                    inivrvAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(INIVRListingActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = INIVRListingActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dilog_kanta_parchi, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = dialogView.findViewById(R.id.cancel_btn);
        TextView case_id = dialogView.findViewById(R.id.a1);
        TextView IVRfile = dialogView.findViewById(R.id.a4);
        TextView lead_id = dialogView.findViewById(R.id.lead_id);
        TextView customer_name = dialogView.findViewById(R.id.customer_name);
        ImageView kanta_parchi_file = dialogView.findViewById(R.id.kanta_parchi_file);
        TextView notes = dialogView.findViewById(R.id.notes);
        TextView gate_pass = dialogView.findViewById(R.id.gate_pass);
        TextView user = dialogView.findViewById(R.id.user);
        TextView coldwin = dialogView.findViewById(R.id.coldwin);
        TextView purchase_details = dialogView.findViewById(R.id.purchase_details);
        TextView loan_details = dialogView.findViewById(R.id.loan_details);
        TextView selas_details = dialogView.findViewById(R.id.selas_details);
        View view = dialogView.findViewById(R.id.view);
        LinearLayout llview = dialogView.findViewById(R.id.llview);
        LinearLayout gate_pass_extra = dialogView.findViewById(R.id.gate_pass_extra);
        TextView a8 = dialogView.findViewById(R.id.a8);
        TextView a10 = dialogView.findViewById(R.id.a10);
        TextView no_of_bags = dialogView.findViewById(R.id.no_of_bags);
        TextView total_weight = dialogView.findViewById(R.id.total_weight);
        ////
        if (AllCases.get(position).getFile() == null || AllCases.get(position).getFile().isEmpty()) {
            kanta_parchi_file.setVisibility(View.GONE);
        }
        gate_pass_extra.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
        llview.setVisibility(View.GONE);
        case_id.setText(getResources().getString(R.string.case_idd));
        IVRfile.setText("IVR Tagging Audio File");
        a8.setText("Secound Kanta bags");
        a10.setText("Secound Kanta weight (Qtl.)");
        lead_id.setText("" + AllCases.get(position).getCaseId());
        customer_name.setText("" + AllCases.get(position).getCustFname());
        no_of_bags.setText("" + ((AllCases.get(position).getS_k_bags()) != null ? AllCases.get(position).getS_k_bags() : "N/A"));
        total_weight.setText("" + ((AllCases.get(position).getS_k_weight()) != null ? AllCases.get(position).getS_k_weight() : "N/A"));
        notes.setText("" + ((AllCases.get(position).getNotes()) != null ? AllCases.get(position).getNotes() : "N/A"));
        gate_pass.setText("Gaatepass/CDF Name : " + ((AllCases.get(position).getGatePassCdfUserName()) != null ? AllCases.get(position).getGatePassCdfUserName() : "N/A"));
        coldwin.setText("ColdWin Name: " + ((AllCases.get(position).getColdwinName()) != null ? AllCases.get(position).getColdwinName() : "N/A"));
        user.setText("User : " + ((AllCases.get(position).getFpoUserId()) != null ? AllCases.get(position).getFpoUserId() : "N/A"));
        purchase_details.setText("purchase Details : " + ((AllCases.get(position).getPurchaseName()) != null ? AllCases.get(position).getPurchaseName() : "N/A"));
        loan_details.setText("Loan Details : " + ((AllCases.get(position).getLoanName()) != null ? AllCases.get(position).getLoanName() : "N/A"));
        selas_details.setText("Sale Details : " + ((AllCases.get(position).getSaleName()) != null ? AllCases.get(position).getSaleName() : "N/A"));
        /////
        firstkantaParchiFile = Constants.IVRTEGGING + AllCases.get(position).getFile();

        kanta_parchi_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AllCases.get(position).getFile()!=null){
                    audioPlayer(Constants.IVRTEGGING,AllCases.get(position).getFile());
                  /*  MediaPlayer mp=new MediaPlayer();
                    try{
                        mp.setDataSource(firstkantaParchiFile);//Write your location here
                        mp.prepare();
                        mp.start();

                    }
                    catch(Exception e){
                        e.printStackTrace();}*/

                }
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
    public void audioPlayer(String path, String fileName){
        //set up MediaPlayer
        MediaPlayer mp = new MediaPlayer();
        try {
            try {
                mp.setDataSource(path+fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mp.start();
    }
    public void checkVeehicleNo(int postion) {
        Bundle bundle = new Bundle();
        bundle.putString("user_name", AllCases.get(postion).getCustFname());
        bundle.putString("case_id", AllCases.get(postion).getCaseId());
        startActivity(INUploadIVRFileClass.class, bundle);
    }

    public void editBagWeightPopup(int postion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(INIVRListingActivity.this);
        LayoutInflater inflater = ((Activity) INIVRListingActivity.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_ivr_tagging_weight_bag, null);
        TextView caseid = (TextView) dialogView.findViewById(R.id.caseid);
        EditText weight = (EditText) dialogView.findViewById(R.id.weight);
        EditText bags = (EditText) dialogView.findViewById(R.id.bags);
        Button submit = (Button) dialogView.findViewById(R.id.btn_submit);
        Button cancel_btn = (Button) dialogView.findViewById(R.id.cancel_btn);

        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        if (((AllCases.get(postion).getS_k_bags()) != null)){
            bags.setText(""+AllCases.get(postion).getS_k_bags());

        }
        if (((AllCases.get(postion).getS_k_weight()) != null)){
            weight.setText(""+AllCases.get(postion).getS_k_weight());

        }
        caseid.setText("CaseID:-  "+AllCases.get(postion).getCaseId());
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weight.getText().toString().trim() == null || weight.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter second kanta weight", Toast.LENGTH_LONG).show();
                } else if (bags.getText().toString().trim() == null || bags.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter second kanta bags", Toast.LENGTH_LONG).show();
                } else {
                    Utility.showDecisionDialog(INIVRListingActivity.this, getString(R.string.alert), "Are you Sure? ", new Utility.AlertCallback() {
                        @Override
                        public void callback() {
                           String inventory_id = "" + AllCases.get(postion).getCaseId();
                                    apiService.doCreateEmpGatepasssUpdate(new EmpUpdateGartepassRequest(inventory_id,  weight.getText().toString().trim(), bags.getText().toString().trim())).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                                        @Override
                                        protected void onSuccess(LoginResponse body) {
                                    alertDialog.dismiss();
                                    Toast.makeText(INIVRListingActivity.this, body.getMessage(), Toast.LENGTH_LONG).show();
                                    getAllCases("");
                                }
                            });
                        }
                    });
                }
            }
        });

    }
}
