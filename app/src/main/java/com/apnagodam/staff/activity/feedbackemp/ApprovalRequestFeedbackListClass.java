package com.apnagodam.staff.activity.feedbackemp;

import android.app.Activity;
import android.graphics.Rect;
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
import com.apnagodam.staff.Network.Request.ApprovedConveyancePOst;
import com.apnagodam.staff.Network.Request.ApprovedRejectConveyancePOst;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.convancy_voachar.MyConveyanceListClass;
import com.apnagodam.staff.activity.convancy_voachar.UploadConveyanceVoacharClass;
import com.apnagodam.staff.adapter.ApprovalRequestConvancyListAdpter;
import com.apnagodam.staff.adapter.ApprovalRequestFeedbackListAdpter;
import com.apnagodam.staff.databinding.ConvencyListBinding;
import com.apnagodam.staff.module.AllConvancyList;
import com.apnagodam.staff.module.FeedbackApprovalLisPojo;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApprovalRequestFeedbackListClass extends BaseActivity<ConvencyListBinding> {
    private ApprovalRequestFeedbackListAdpter approvalRequestFeedbackListAdpter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<FeedbackApprovalLisPojo.Datum> getOrdersList;
    private List<FeedbackApprovalLisPojo.Datum> tempraryList;
    private String inventory_id = "";
    @Override
    protected int getLayoutResId() {
        return R.layout.convency_list;
    }

    @Override
    protected void setUp() {
        getOrdersList = new ArrayList();
        tempraryList = new ArrayList();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.titleHeader.setText("All Suggestion Request");
        binding.tvId.setText("Feedback To");
        binding.tvName.setText("Designation");
        binding.activityMain.setVisibility(View.GONE);
        binding.filterIcon.setVisibility(View.GONE);
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(UploadEmpSuggestionClass.class);
            }
        });
        setDataAdapter();
        getApprovalRequestConvencyList("");
        binding.tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageOffset != 1) {
                    pageOffset--;
                    getApprovalRequestConvencyList("");
                }
            }
        });
        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalPage != pageOffset) {
                    pageOffset++;
                    getApprovalRequestConvencyList("");
                }
            }
        });

        binding.swipeRefresherStock.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getApprovalRequestConvencyList("");
            }
        });
        binding.filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalRequestFeedbackListClass.this);
                LayoutInflater inflater = ((Activity) ApprovalRequestFeedbackListClass.this).getLayoutInflater();
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
                             getApprovalRequestConvencyList(notes.getText().toString().trim());
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Fill Text", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    private void getApprovalRequestConvencyList(String SerachKey) {
        showDialog();
        apiService.getAllRequestList().enqueue(new NetworkCallback<FeedbackApprovalLisPojo>(getActivity()) {
            @Override
            protected void onSuccess(FeedbackApprovalLisPojo body) {
                tempraryList.clear();
                getOrdersList.clear();
                binding.swipeRefresherStock.setRefreshing(false);
                if (body.getData().size() == 0) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.fieldStockList.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                }  else {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.fieldStockList.setVisibility(View.VISIBLE);
                    binding.pageNextPrivious.setVisibility(View.VISIBLE);
                    getOrdersList.addAll(body.getData());
                    tempraryList.addAll(getOrdersList);
                    approvalRequestFeedbackListAdpter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setDataAdapter() {
        binding.fieldStockList.addItemDecoration(new DividerItemDecoration(ApprovalRequestFeedbackListClass.this, LinearLayoutManager.HORIZONTAL));
        binding.fieldStockList.setHasFixedSize(true);
        binding.fieldStockList.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ApprovalRequestFeedbackListClass.this, LinearLayoutManager.VERTICAL, false);
        binding.fieldStockList.setLayoutManager(horizontalLayoutManager);
        approvalRequestFeedbackListAdpter = new ApprovalRequestFeedbackListAdpter(getOrdersList, ApprovalRequestFeedbackListClass.this, getActivity());
        binding.fieldStockList.setAdapter(approvalRequestFeedbackListAdpter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(MyConveyanceListClass.class);
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalRequestFeedbackListClass.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = ApprovalRequestFeedbackListClass.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.feedback_view, null);
     /*   dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));*/
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = dialogView.findViewById(R.id.cancel_btn);
        TextView lead_id = dialogView.findViewById(R.id.lead_id);
        TextView genrated_by = dialogView.findViewById(R.id.genrated_by);
        TextView customer_name = dialogView.findViewById(R.id.customer_name);
        TextView location_title = dialogView.findViewById(R.id.location_title);
        TextView phone_no = dialogView.findViewById(R.id.phone_no);
        TextView commodity_name = dialogView.findViewById(R.id.commodity_name);
        TextView a4 = dialogView.findViewById(R.id.a4);
a4.setText("Feeedback To");

        customer_name.setText("" + ((getOrdersList.get(position).getProcessSolution()) != null ? getOrdersList.get(position).getProcessSolution() : "N/A"));
        lead_id.setText("" + ((getOrdersList.get(position).getDesignation()) != null ? getOrdersList.get(position).getDesignation() : "N/A"));
        genrated_by.setText("" + ((getOrdersList.get(position).getProcessProblem()) != null ? getOrdersList.get(position).getProcessProblem() : "N/A"));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = inputFormatter.parse((getOrdersList.get(position).getUpdatedAt()));
                DateFormat outputFormatter = new SimpleDateFormat("dd-MM-yyyy");
                String output = outputFormatter.format(date); // Output : 01/20/2012
                location_title.setText("" + ((getOrdersList.get(position).getUpdatedAt()) != null ? output : "N/A"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        phone_no.setText("" + ((getOrdersList.get(position).getFirstName()) != null ? (getOrdersList.get(position).getFirstName() + " " + getOrdersList.get(position).getLastName() + "(" + getOrdersList.get(position).getEmpId() + ")") : "N/A"));
        if (getOrdersList.get(position).getStatus() == 1) {
            // show button self rejected
            commodity_name.setText(getResources().getString(R.string.pending));
            commodity_name.setTextColor(getResources().getColor(R.color.darkYellow));
        } else if (getOrdersList.get(position).getStatus() == 0) {
            // rejected by self or approved from high authority
            commodity_name.setText(getResources().getString(R.string.rejected));
            commodity_name.setTextColor(getResources().getColor(R.color.red));
        } else if (getOrdersList.get(position).getStatus() == 2) {
            //approved from high authority
            commodity_name.setText(getResources().getString(R.string.approved));
            commodity_name.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void cancelConv(int position) {
        Utility.showDecisionDialog(ApprovalRequestFeedbackListClass.this, getString(R.string.alert), "Are you Sure? Do you want to Reject this Request!", new Utility.AlertCallback() {
            @Override
            public void callback() {
                inventory_id = "" + getOrdersList.get(position).getId();
                apiService.getcancelapproveFeedback(inventory_id, "0").enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                    @Override
                    protected void onSuccess(LoginResponse body) {
                        Toast.makeText(ApprovalRequestFeedbackListClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                        getApprovalRequestConvencyList("");
                    }
                });
            }
        });

    }

    public void ApprovedConv(int position) {
        Utility.showDecisionDialog(ApprovalRequestFeedbackListClass.this, getString(R.string.alert), "Are you Sure? Do you want to Approve  this Request!", new Utility.AlertCallback() {
            @Override
            public void callback() {
                inventory_id = "" + getOrdersList.get(position).getId();
                apiService.getcancelapproveFeedback(inventory_id, "2").enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                    @Override
                    protected void onSuccess(LoginResponse body) {
                        Toast.makeText(ApprovalRequestFeedbackListClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                        getApprovalRequestConvencyList("");
                    }
                });
            }
        });

    }
}
