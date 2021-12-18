package com.apnagodam.staff.activity.dispaldgeinventory;

import android.app.Activity;
import android.graphics.Rect;
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
import com.apnagodam.staff.Network.Request.ApprovedRejectVendorConveyancePOst;
import com.apnagodam.staff.Network.Request.ApprovedVendorConveyancePOst;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.convancy_voachar.UploadConveyanceVoacharClass;
import com.apnagodam.staff.activity.vendorPanel.MyVendorVoacherListClass;
import com.apnagodam.staff.adapter.ApprovalRequestDispladgeListAdpter;
import com.apnagodam.staff.adapter.ApprovalRequestVendorVoacherListAdpter;
import com.apnagodam.staff.databinding.AddedDispladgeListBinding;
import com.apnagodam.staff.databinding.ConvencyListBinding;
import com.apnagodam.staff.module.AllVendorConvancyList;
import com.apnagodam.staff.module.DispladgePojo;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApprovalRequestDispladgeListClass extends BaseActivity<AddedDispladgeListBinding> {
    private ApprovalRequestDispladgeListAdpter approvalRequestDispladgeListAdpter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<DispladgePojo.Datum> getOrdersList;
    private List<DispladgePojo.Datum> tempraryList;
    private String inventory_id = "";
    private String firstkantaParchiFile;

    @Override
    protected int getLayoutResId() {
        return R.layout.added_displadge_list;
    }

    @Override
    protected void setUp() {
        getOrdersList = new ArrayList();
        tempraryList = new ArrayList();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.titleHeader.setText("All Displedge Request");
        binding.activityMain.setVisibility(View.GONE);
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(MyAddedDispladgeBagsListClass.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalRequestDispladgeListClass.this);
                LayoutInflater inflater = ((Activity) ApprovalRequestDispladgeListClass.this).getLayoutInflater();
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
        apiService.getDispladgeRequestedList("15", pageOffset, SerachKey).enqueue(new NetworkCallback<DispladgePojo>(getActivity()) {
            @Override
            protected void onSuccess(DispladgePojo body) {
                tempraryList.clear();
                getOrdersList.clear();
                binding.swipeRefresherStock.setRefreshing(false);
                if (body.getData().getDataa().size() == 0) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.fieldStockList.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } else if (body.getData().getLastPage() == 1) {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.fieldStockList.setVisibility(View.VISIBLE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                    totalPage = body.getData().getLastPage();
                    getOrdersList.addAll(body.getData().getDataa());
                    tempraryList.addAll(getOrdersList);
                    approvalRequestDispladgeListAdpter.notifyDataSetChanged();
                } else {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.fieldStockList.setVisibility(View.VISIBLE);
                    binding.pageNextPrivious.setVisibility(View.VISIBLE);
                    totalPage = body.getData().getLastPage();
                    getOrdersList.addAll(body.getData().getDataa());
                    tempraryList.addAll(getOrdersList);
                    approvalRequestDispladgeListAdpter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setDataAdapter() {
        binding.fieldStockList.addItemDecoration(new DividerItemDecoration(ApprovalRequestDispladgeListClass.this, LinearLayoutManager.HORIZONTAL));
        binding.fieldStockList.setHasFixedSize(true);
        binding.fieldStockList.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ApprovalRequestDispladgeListClass.this, LinearLayoutManager.VERTICAL, false);
        binding.fieldStockList.setLayoutManager(horizontalLayoutManager);
        approvalRequestDispladgeListAdpter = new ApprovalRequestDispladgeListAdpter(getOrdersList, ApprovalRequestDispladgeListClass.this, getActivity());
        binding.fieldStockList.setAdapter(approvalRequestDispladgeListAdpter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(MyAddedDispladgeBagsListClass.class);
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalRequestDispladgeListClass.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = ApprovalRequestDispladgeListClass.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.displadge_view, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = dialogView.findViewById(R.id.cancel_btn);
        TextView lead_id = dialogView.findViewById(R.id.lead_id);
        TextView genrated_by = dialogView.findViewById(R.id.genrated_by);
        TextView customer_name = dialogView.findViewById(R.id.customer_name);
        TextView phone_no = dialogView.findViewById(R.id.phone_no);
        TextView location_title = dialogView.findViewById(R.id.location_title);
        TextView commodity_name = dialogView.findViewById(R.id.commodity_name);
        TextView total_trans_cost = dialogView.findViewById(R.id.total_trans_cost);
        TextView advance_patyment = dialogView.findViewById(R.id.advance_patyment);
        TextView start_date_time = dialogView.findViewById(R.id.start_date_time);
        TextView a18 = dialogView.findViewById(R.id.a18);
        TextView notes = dialogView.findViewById(R.id.notes);
        TextView displadge_notes = dialogView.findViewById(R.id.displadge_notes);
        ImageView displadge_file = (ImageView) dialogView.findViewById(R.id.displadge_file);
        if (getOrdersList.get(position).getDispledge_image() == null || getOrdersList.get(position).getDispledge_image().isEmpty()) {
            displadge_file.setVisibility(View.GONE);
        }
        firstkantaParchiFile = Constants.DispladgeImage + getOrdersList.get(position).getDispledge_image();
        displadge_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(ApprovalRequestDispladgeListClass.this, R.layout.popup_photo_full, view, firstkantaParchiFile, null);
            }
        });
        displadge_notes.setText("" + ((getOrdersList.get(position).getEmp_displege_notes()) != null ? getOrdersList.get(position).getEmp_displege_notes() : "N/A"));
        inventory_id = "" + getOrdersList.get(position).getId();
        a18.setText("Request For");
        lead_id.setText("" + ((getOrdersList.get(position).getUserName()) != null ? getOrdersList.get(position).getUserName() + "(" + getOrdersList.get(position).getUserPhoneNo() + ")" : "N/A"));
        genrated_by.setText("" + ((getOrdersList.get(position).getTerminalName()) != null ? getOrdersList.get(position).getTerminalName() + "(" + getOrdersList.get(position).getWarehouseCode() + ")" : "N/A"));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = inputFormatter.parse((getOrdersList.get(position).getUpdatedAt()));
                DateFormat outputFormatter = new SimpleDateFormat("dd-MM-yyyy");
                String output = outputFormatter.format(date); // Output : 01/20/2012
                start_date_time.setText("" + ((getOrdersList.get(position).getUpdatedAt()) != null ? output : "N/A"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String commodityType = getOrdersList.get(position).getCommodityType();
        if (commodityType.equalsIgnoreCase("Primary")) {
            commodityType = "किसानी";
        } else {
            commodityType = "MTP";
        }
        customer_name.setText("" + ((getOrdersList.get(position).getCategory()) != null ? getOrdersList.get(position).getCategory() + "(" + commodityType + ")" : "N/A"));
        commodity_name.setText("" + ((getOrdersList.get(position).getBags()) != null ? getOrdersList.get(position).getBags() : "N/A"));
        phone_no.setText("" + ((getOrdersList.get(position).getStackNumber()) != null ? getOrdersList.get(position).getStackNumber() : "N/A"));
        total_trans_cost.setText("" + (getOrdersList.get(position).getFirstName() != null ? ((getOrdersList.get(position).getFirstName() + " " + getOrdersList.get(position).getLastName() + "(" + getOrdersList.get(position).getEmpId() + ")")) : "N/A"));
        location_title.setText("" + ((getOrdersList.get(position).getNetWeight()) != null ? getOrdersList.get(position).getNetWeight() : "N/A"));
        notes.setText("" + ((getOrdersList.get(position).getNotes()) != null ? getOrdersList.get(position).getNotes() : "N/A"));
        if (getOrdersList.get(position).getStatus() == 1) {
            // show button self rejected
            advance_patyment.setText(getResources().getString(R.string.pending));
            advance_patyment.setTextColor(getResources().getColor(R.color.darkYellow));
        } else if (getOrdersList.get(position).getStatus() == 0) {
            // rejected by self or approved from high authority
            advance_patyment.setText(getResources().getString(R.string.rejected));
            advance_patyment.setTextColor(getResources().getColor(R.color.red));
        } else if (getOrdersList.get(position).getStatus() == 2) {
            //approved from high authority
            advance_patyment.setText(getResources().getString(R.string.approved));
            advance_patyment.setTextColor(getResources().getColor(R.color.colorGreen));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalRequestDispladgeListClass.this);
        LayoutInflater inflater = ((Activity) ApprovalRequestDispladgeListClass.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_seell_price_diloag, null);
        EditText reson = (EditText) dialogView.findViewById(R.id.reson);
        EditText amount = (EditText) dialogView.findViewById(R.id.amount);
        TextView submit = (TextView) dialogView.findViewById(R.id.btn_submit);
        TextView heading = (TextView) dialogView.findViewById(R.id.heading);
        ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
        heading.setText("Displedge Reject Notes");
        amount.setVisibility(View.GONE);
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
                if (reson.getText().toString().trim() == null || reson.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Reject Reason", Toast.LENGTH_LONG).show();
                } /*else if (amount.getText().toString().trim() == null || amount.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Final Amount", Toast.LENGTH_LONG).show();
                }*/ else {
                    Utility.showDecisionDialog(ApprovalRequestDispladgeListClass.this, getString(R.string.alert), "Are you Sure? Do you want to Reject this Displedge!", new Utility.AlertCallback() {
                        @Override
                        public void callback() {
                            inventory_id = "" + getOrdersList.get(position).getId();
                            apiService.rejectDispladge(inventory_id, reson.getText().toString().trim()).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                                @Override
                                protected void onSuccess(LoginResponse body) {
                                    alertDialog.dismiss();
                                    Toast.makeText(ApprovalRequestDispladgeListClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                                    getApprovalRequestConvencyList("");
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    public void ApprovedConv(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalRequestDispladgeListClass.this);
        LayoutInflater inflater = ((Activity) ApprovalRequestDispladgeListClass.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_seell_price_diloag, null);
        EditText reson = (EditText) dialogView.findViewById(R.id.reson);
        EditText amount = (EditText) dialogView.findViewById(R.id.amount);
        TextView submit = (TextView) dialogView.findViewById(R.id.btn_submit);
        TextView heading = (TextView) dialogView.findViewById(R.id.heading);
        ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
        heading.setText("Displedge Approve Notes");
        reson.setHint("Notes");
        amount.setVisibility(View.GONE);
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
                if (reson.getText().toString().trim() == null || reson.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter  Reason", Toast.LENGTH_LONG).show();
                } /*else if (amount.getText().toString().trim() == null || amount.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Final Amount", Toast.LENGTH_LONG).show();
                } */else {
                    Utility.showDecisionDialog(ApprovalRequestDispladgeListClass.this, getString(R.string.alert), "Are you Sure? Do you want to Approve this Displedge!", new Utility.AlertCallback() {
                        @Override
                        public void callback() {
                            inventory_id = "" + getOrdersList.get(position).getId();
                            apiService.approveDispladge(inventory_id,  reson.getText().toString().trim()).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                                @Override
                                protected void onSuccess(LoginResponse body) {
                                    alertDialog.dismiss();
                                    Toast.makeText(ApprovalRequestDispladgeListClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                                    getApprovalRequestConvencyList("");
                                }
                            });
                        }
                    });
                }
            }
        });

    }
}
