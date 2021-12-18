package com.apnagodam.staff.activity.errorlog;

import android.app.Activity;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.SelfRejectConveyancePOst;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.adapter.ErrorLogListAdpter;
import com.apnagodam.staff.databinding.ErrorlogListBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.AllConvancyList;
import com.apnagodam.staff.module.AllLevelEmpListPojo;
import com.apnagodam.staff.module.ErrorLogListPojo;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class MyErrorLogListClass extends BaseActivity<ErrorlogListBinding> {
    private ErrorLogListAdpter errorLogListAdpter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<ErrorLogListPojo.Datum> getOrdersList;
    private List<ErrorLogListPojo.Datum> tempraryList;
    private String inventory_id = "";
    private String startMeterImage, endMeterImage;
    // for FB button
    private Animation fabOpenAnimation;
    private Animation fabCloseAnimation;
    private boolean isFabMenuOpen = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.errorlog_list;
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
                startActivityAndClear(StaffDashBoardActivity.class);
            }
        });
        setDataAdapter();
        getConvencyList("");

        binding.tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageOffset != 1) {
                    pageOffset--;
                    getConvencyList("");
                }
            }
        });
        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalPage != pageOffset) {
                    pageOffset++;
                    getConvencyList("");
                }
            }
        });

        binding.swipeRefresherStock.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getConvencyList("");
            }
        });
        binding.filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyErrorLogListClass.this);
                LayoutInflater inflater = ((Activity) MyErrorLogListClass.this).getLayoutInflater();
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
                            getConvencyList(notes.getText().toString().trim());
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Fill Text", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        binding.setFabHandler(new FabHandler());
        getAnimations();

    }

    public class FabHandler {
        public void onBaseFabClick(View view) {
            if (isFabMenuOpen)
                collapseFabMenu();
            else
                expandFabMenu();
        }

        public void onCreateFabClick(View view) {
            for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("43")) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getEdit() == 1) {
                        startActivity(UploadErrorLogClass.class);
                    }
                }
            }
        }

        public void onShareFabClick(View view) {
            startActivityAndClear(ApprovalRequestErroorListClass.class);
        }
    }

    private void expandFabMenu() {
        ViewCompat.animate(binding.fab).rotation(45.0F).withLayer().setDuration(300).setInterpolator(new OvershootInterpolator(10.0F)).start();
        binding.createLayout.startAnimation(fabOpenAnimation);
        binding.shareLayout.startAnimation(fabOpenAnimation);
        binding.createFab.setClickable(true);
        binding.shareFab.setClickable(true);
        isFabMenuOpen = true;
    }

    private void collapseFabMenu() {
        ViewCompat.animate(binding.fab).rotation(0.0F).withLayer().setDuration(300).setInterpolator(new OvershootInterpolator(10.0F)).start();
        binding.createLayout.startAnimation(fabCloseAnimation);
        binding.shareLayout.startAnimation(fabCloseAnimation);
        binding.createFab.setClickable(false);
        binding.shareFab.setClickable(false);
        isFabMenuOpen = false;
    }

    private void getAnimations() {
        fabOpenAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_close);
    }

    private void getapproveCount() {
        apiService.getlevelwiselist("error_log").enqueue(new NetworkCallback<AllLevelEmpListPojo>(getActivity()) {
            @Override
            protected void onSuccess(AllLevelEmpListPojo body) {
                for (int i = 0; i < body.getData().size(); i++) {
                    if (body.getRequest_count() > 0) {
                        binding.shareLabelTextView.setClickable(true);
                        binding.shareLabelTextView.setEnabled(true);
                        binding.shareFab.setClickable(true);
                        binding.shareFab.setEnabled(true);
                        binding.shareLabelTextView.setText("Approval Request " + "(" + body.getRequest_count() + ")");
                    } else {
                        binding.shareLabelTextView.setClickable(false);
                        binding.shareFab.setClickable(false);
                        binding.shareLabelTextView.setEnabled(false);
                        binding.shareFab.setEnabled(false);
                    }
                }
            }

        });
    }

    private void getConvencyList(String SerachKey) {
        showDialog();
        apiService.getErrorList("15", pageOffset, SerachKey).enqueue(new NetworkCallback<ErrorLogListPojo>(getActivity()) {
            @Override
            protected void onSuccess(ErrorLogListPojo body) {
                getapproveCount();
                tempraryList.clear();
                getOrdersList.clear();
                binding.swipeRefresherStock.setRefreshing(false);
                if (body.getData().size() == 0) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.fieldStockList.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } /*else if (body.getData().getLastPage() == 1) {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.fieldStockList.setVisibility(View.VISIBLE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                    getOrdersList.clear();
                    tempraryList.clear();
                    totalPage = body.getData().getLastPage();
                    getOrdersList.addAll(body.getData().getDataa());
                    tempraryList.addAll(getOrdersList);
                    errorLogListAdpter.notifyDataSetChanged();
                } */else {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.fieldStockList.setVisibility(View.VISIBLE);
                    binding.pageNextPrivious.setVisibility(View.VISIBLE);
                    getOrdersList.clear();
                    tempraryList.clear();
                   // totalPage = body.getData().getLastPage();
                    getOrdersList.addAll(body.getData());
                    tempraryList.addAll(getOrdersList);
                    errorLogListAdpter.notifyDataSetChanged();
                }

            }
        });
    }

    private void setDataAdapter() {
        binding.fieldStockList.addItemDecoration(new DividerItemDecoration(MyErrorLogListClass.this, LinearLayoutManager.HORIZONTAL));
        binding.fieldStockList.setHasFixedSize(true);
        binding.fieldStockList.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MyErrorLogListClass.this, LinearLayoutManager.VERTICAL, false);
        binding.fieldStockList.setLayoutManager(horizontalLayoutManager);
        errorLogListAdpter = new ErrorLogListAdpter(getOrdersList, MyErrorLogListClass.this, getActivity());
        binding.fieldStockList.setAdapter(errorLogListAdpter);
    }


    @Override
    public void onBackPressed() {
        if (isFabMenuOpen)
            collapseFabMenu();
        else {
            super.onBackPressed();
            startActivity(StaffDashBoardActivity.class);
        }
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(MyErrorLogListClass.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = MyErrorLogListClass.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.error_view, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = dialogView.findViewById(R.id.cancel_btn);
        TextView commitemate_date = dialogView.findViewById(R.id.commitemate_date);
        TextView create_date = dialogView.findViewById(R.id.create_date);
        TextView converted_by = dialogView.findViewById(R.id.converted_by);
        TextView vehicle_no = dialogView.findViewById(R.id.vehicle_no);
        TextView in_out = dialogView.findViewById(R.id.in_out);
        TextView gate_passs = dialogView.findViewById(R.id.gate_passs);
        TextView total_trans_cost = dialogView.findViewById(R.id.total_trans_cost);
        TextView advance_patyment = dialogView.findViewById(R.id.advance_patyment);
        TextView settleememt_amount = dialogView.findViewById(R.id.settleememt_amount);
        TextView db_changes = dialogView.findViewById(R.id.db_changes);
        TextView start_date_time = dialogView.findViewById(R.id.start_date_time);
        ImageView reports_file = dialogView.findViewById(R.id.reports_file);
        ImageView commodity_file = dialogView.findViewById(R.id.commodity_file);
        startMeterImage = Constants.error_log_photo + getOrdersList.get(position).getErrorImg1();
        endMeterImage = Constants.error_log_photo + getOrdersList.get(position).getErrorImg2();
        reports_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(MyErrorLogListClass.this, R.layout.popup_photo_full, view, startMeterImage, null);
            }
        });
        commodity_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(MyErrorLogListClass.this, R.layout.popup_photo_full, view, endMeterImage, null);
            }
        });
        commitemate_date.setText("" + ((getOrdersList.get(position).getFirstName()) != null ? getOrdersList.get(position).getFirstName()+" " +getOrdersList.get(position).getLastName()+"("+getOrdersList.get(position).getEmpId()+")": "N/A"));
        create_date.setText("" + ((getOrdersList.get(position).getLog()) != null ? getOrdersList.get(position).getLog() : "N/A"));
        converted_by.setText("" + ((getOrdersList.get(position).getError()) != null ? getOrdersList.get(position).getError() : "N/A"));
        vehicle_no.setText("" + ((getOrdersList.get(position).getCorrection()) != null ? getOrdersList.get(position).getCorrection() : "N/A"));
        in_out.setText("" + ((getOrdersList.get(position).getFutureCorrection()) != null ? (getOrdersList.get(position).getFutureCorrection()) : "N/A"));
        gate_passs.setText("" + ((getOrdersList.get(position).getFinalConclusion()) != null ? getOrdersList.get(position).getFinalConclusion() : "N/A"));
        total_trans_cost.setText("" + ((getOrdersList.get(position).getFname() + " " + getOrdersList.get(position).getLname() + "(" + getOrdersList.get(position).getEmpID() + ")") != null ? ((getOrdersList.get(position).getFname() + " " + getOrdersList.get(position).getLastName() + "(" + getOrdersList.get(position).getEmpID() + ")")) : "N/A"));
        if (getOrdersList.get(position).getStatus().equalsIgnoreCase("1")) {
            // show button self rejected
            advance_patyment.setText(getResources().getString(R.string.pending));
            advance_patyment.setTextColor(getResources().getColor(R.color.darkYellow));
        } else if (getOrdersList.get(position).getStatus().equalsIgnoreCase("0")) {
            // rejected by self or approved from high authority
            advance_patyment.setText(getResources().getString(R.string.rejected));
            advance_patyment.setTextColor(getResources().getColor(R.color.red));
        } else if (getOrdersList.get(position).getStatus().equalsIgnoreCase("2")) {
            //approved from high authority
            advance_patyment.setText(getResources().getString(R.string.approved));
            advance_patyment.setTextColor(getResources().getColor(R.color.colorGreen));
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////
        if (getOrdersList.get(position).getVerify().equalsIgnoreCase("1")) {
            // show button self rejected
            settleememt_amount.setText(getResources().getString(R.string.pending));
            settleememt_amount.setTextColor(getResources().getColor(R.color.darkYellow));
        } else if (getOrdersList.get(position).getVerify().equalsIgnoreCase("0")) {
            // rejected by self or approved from high authority
            settleememt_amount.setText(getResources().getString(R.string.rejected));
            settleememt_amount.setTextColor(getResources().getColor(R.color.red));
        } else if (getOrdersList.get(position).getVerify().equalsIgnoreCase("2")) {
            //approved from high authority
            settleememt_amount.setText(getResources().getString(R.string.verify_conv));
            settleememt_amount.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////
        if (getOrdersList.get(position).getIt_action().equalsIgnoreCase("1")) {
            // show button self rejected
            db_changes.setText(getResources().getString(R.string.pending));
            db_changes.setTextColor(getResources().getColor(R.color.darkYellow));
        } else if (getOrdersList.get(position).getIt_action().equalsIgnoreCase("0")) {
            // rejected by self or approved from high authority
            db_changes.setText(getResources().getString(R.string.rejected));
            db_changes.setTextColor(getResources().getColor(R.color.red));
        } else if (getOrdersList.get(position).getIt_action().equalsIgnoreCase("2")) {
            //approved from high authority
            db_changes.setText(getResources().getString(R.string.done));
            db_changes.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        start_date_time.setText("" + ((getOrdersList.get(position).getUpdatedAt()) != null ? getOrdersList.get(position).getUpdatedAt() : "N/A"));
        inventory_id = "" + getOrdersList.get(position).getId();
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void cancelConv(int position) {
        Utility.showDecisionDialog(MyErrorLogListClass.this, getString(R.string.alert), "Are you Sure? Do you want to Delete this Error log!", new Utility.AlertCallback() {
            @Override
            public void callback() {
                inventory_id = "" + getOrdersList.get(position).getId();
                apiService.ErrorCancel(inventory_id).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                    @Override
                    protected void onSuccess(LoginResponse body) {
                        Toast.makeText(MyErrorLogListClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                        getConvencyList("");
                    }
                });
            }
        });
    }
}
