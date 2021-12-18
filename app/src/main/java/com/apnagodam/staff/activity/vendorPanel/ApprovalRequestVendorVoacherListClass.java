package com.apnagodam.staff.activity.vendorPanel;

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
import com.apnagodam.staff.Network.Request.SelfRejectConveyancePOst;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.convancy_voachar.UploadConveyanceVoacharClass;
import com.apnagodam.staff.adapter.ApprovalRequestVendorVoacherListAdpter;
import com.apnagodam.staff.databinding.ConvencyListBinding;
import com.apnagodam.staff.module.AllConvancyList;
import com.apnagodam.staff.module.AllVendorConvancyList;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApprovalRequestVendorVoacherListClass extends BaseActivity<ConvencyListBinding> {
    private ApprovalRequestVendorVoacherListAdpter approvalRequestVendorVoacherListAdpter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<AllVendorConvancyList.Datum> getOrdersList;
    private List<AllVendorConvancyList.Datum> tempraryList;
    private String inventory_id = "";
    private String startMeterImage, endMeterImage;

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
        binding.titleHeader.setText("All Conveyance Request");
        binding.activityMain.setVisibility(View.GONE);
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(StaffDashBoardActivity.class);
            }
        });
        setDataAdapter();
        getApprovalRequestConvencyList("");
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(UploadConveyanceVoacharClass.class);
            }
        });
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalRequestVendorVoacherListClass.this);
                LayoutInflater inflater = ((Activity) ApprovalRequestVendorVoacherListClass.this).getLayoutInflater();
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
        apiService.getVendorApprovalRequestConvancyList("15", pageOffset, SerachKey).enqueue(new NetworkCallback<AllVendorConvancyList>(getActivity()) {
            @Override
            protected void onSuccess(AllVendorConvancyList body) {
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
                    approvalRequestVendorVoacherListAdpter.notifyDataSetChanged();
                } else {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.fieldStockList.setVisibility(View.VISIBLE);
                    binding.pageNextPrivious.setVisibility(View.VISIBLE);
                    totalPage = body.getData().getLastPage();
                    getOrdersList.addAll(body.getData().getDataa());
                    tempraryList.addAll(getOrdersList);
                    approvalRequestVendorVoacherListAdpter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setDataAdapter() {
        binding.fieldStockList.addItemDecoration(new DividerItemDecoration(ApprovalRequestVendorVoacherListClass.this, LinearLayoutManager.HORIZONTAL));
        binding.fieldStockList.setHasFixedSize(true);
        binding.fieldStockList.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ApprovalRequestVendorVoacherListClass.this, LinearLayoutManager.VERTICAL, false);
        binding.fieldStockList.setLayoutManager(horizontalLayoutManager);
        approvalRequestVendorVoacherListAdpter = new ApprovalRequestVendorVoacherListAdpter(getOrdersList, ApprovalRequestVendorVoacherListClass.this, getActivity());
        binding.fieldStockList.setAdapter(approvalRequestVendorVoacherListAdpter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(MyVendorVoacherListClass.class);
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalRequestVendorVoacherListClass.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = ApprovalRequestVendorVoacherListClass.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.vendor_con_view, null);
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
        TextView est_quantity_nmae = dialogView.findViewById(R.id.est_quantity_nmae);
        TextView terminal_name = dialogView.findViewById(R.id.terminal_name);
        TextView purpose_name = dialogView.findViewById(R.id.voacher_purpose);
        TextView gate_passs = dialogView.findViewById(R.id.gate_passs);
        TextView total_trans_cost = dialogView.findViewById(R.id.total_trans_cost);
        TextView advance_patyment = dialogView.findViewById(R.id.advance_patyment);
        TextView start_date_time = dialogView.findViewById(R.id.start_date_time);
        TextView end_date_time = dialogView.findViewById(R.id.end_date_time);
        TextView settleememt_amount = dialogView.findViewById(R.id.settleememt_amount);
        ImageView reports_file = dialogView.findViewById(R.id.reports_file);
        ImageView commodity_file = dialogView.findViewById(R.id.commodity_file);
        startMeterImage = Constants.vendors_voucher + getOrdersList.get(position).getExpImage1();
        endMeterImage = Constants.vendors_voucher + getOrdersList.get(position).getExpImage2();
        reports_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(ApprovalRequestVendorVoacherListClass.this, R.layout.popup_photo_full, view, startMeterImage, null);
            }
        });
        commodity_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(ApprovalRequestVendorVoacherListClass.this, R.layout.popup_photo_full, view, endMeterImage, null);
            }
        });
        inventory_id = "" + getOrdersList.get(position).getId();
        lead_id.setText("" + ((getOrdersList.get(position).getUniqueId()) != null ? getOrdersList.get(position).getUniqueId() : "N/A"));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = inputFormatter.parse((getOrdersList.get(position).getDate()));
                DateFormat outputFormatter = new SimpleDateFormat("dd-MM-yyyy");
                String output = outputFormatter.format(date); // Output : 01/20/2012
                genrated_by.setText("" + ((getOrdersList.get(position).getDate()) != null ?output : "N/A"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        customer_name.setText("" + ((getOrdersList.get(position).getFirstName()) != null ? getOrdersList.get(position).getFirstName() + " " + getOrdersList.get(position).getLastName() + "(" + getOrdersList.get(position).getEmpId() + ")" : "N/A"));
        commodity_name.setText("" + ((getOrdersList.get(position).getExpensesName()) != null ? getOrdersList.get(position).getExpensesName() : "N/A"));
        phone_no.setText("" + ((getOrdersList.get(position).getVendorFirstName()) != null ? getOrdersList.get(position).getVendorFirstName() + " " + getOrdersList.get(position).getVendorLastName() + "(" + getOrdersList.get(position).getVendorUniqueId() + ")" : "N/A"));
        location_title.setText("" + ((getOrdersList.get(position).getWarehouseName()) != null ? getOrdersList.get(position).getWarehouseName() + "(" + getOrdersList.get(position).getWarehouseCode() + ")" : "N/A"));
        est_quantity_nmae.setText("" + ((getOrdersList.get(position).getAmount()) != null ? (getOrdersList.get(position).getAmount()) : "N/A"));
        terminal_name.setText(" " + ((getOrdersList.get(position).getFinalPrice()) != null ? getOrdersList.get(position).getFinalPrice() : "N/A"));
        purpose_name.setText("" + ((getOrdersList.get(position).getPurpose()) != null ? getOrdersList.get(position).getPurpose() : "N/A"));
        gate_passs.setText("" + ((getOrdersList.get(position).getNotes()) != null ? getOrdersList.get(position).getNotes() : "N/A"));
        total_trans_cost.setText("" + ((getOrdersList.get(position).getApprovelFname() + " " + getOrdersList.get(position).getApprovelLname() + "(" + getOrdersList.get(position).getApprovelEmpID() + ")") != null ? ((getOrdersList.get(position).getApprovelFname() + " " + getOrdersList.get(position).getApprovelLname() + "(" + getOrdersList.get(position).getApprovelEmpID() + ")")) : "N/A"));
        if (getOrdersList.get(position).getStatus().equalsIgnoreCase("1")) {
            // show button self rejected
            advance_patyment.setText(getResources().getString(R.string.reject));
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
        ///////////////////////////////////////////////////////
        if (getOrdersList.get(position).getPaymentStatus().equalsIgnoreCase("1")) {
            // show button self rejected
            end_date_time.setText(getResources().getString(R.string.pending));
            end_date_time.setTextColor(getResources().getColor(R.color.darkYellow));
        } else if (getOrdersList.get(position).getPaymentStatus().equalsIgnoreCase("0")) {
            // rejected by self or approved from high authority
            end_date_time.setText(getResources().getString(R.string.rejected));
            end_date_time.setTextColor(getResources().getColor(R.color.red));
        } else if (getOrdersList.get(position).getPaymentStatus().equalsIgnoreCase("2")) {
            //approved from high authority
            end_date_time.setText(getResources().getString(R.string.done));
            end_date_time.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////
        if (getOrdersList.get(position).getVerfiyStatus().equalsIgnoreCase("1")) {
            // show button self rejected
            settleememt_amount.setText(getResources().getString(R.string.pending));
            settleememt_amount.setTextColor(getResources().getColor(R.color.darkYellow));
        } else if (getOrdersList.get(position).getVerfiyStatus().equalsIgnoreCase("0")) {
            // rejected by self or approved from high authority
            settleememt_amount.setText(getResources().getString(R.string.rejected));
            settleememt_amount.setTextColor(getResources().getColor(R.color.red));
        } else if (getOrdersList.get(position).getVerfiyStatus().equalsIgnoreCase("2")) {
            //approved from high authority
            settleememt_amount.setText(getResources().getString(R.string.verify_conv));
            settleememt_amount.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        start_date_time.setText("" + ((getOrdersList.get(position).getUpdatedAt()) != null ? getOrdersList.get(position).getUpdatedAt() : "N/A"));
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void cancelConv(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalRequestVendorVoacherListClass.this);
        LayoutInflater inflater = ((Activity) ApprovalRequestVendorVoacherListClass.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_seell_price_diloag, null);
        EditText reson = (EditText) dialogView.findViewById(R.id.reson);
        EditText amount = (EditText) dialogView.findViewById(R.id.amount);
        TextView submit = (TextView) dialogView.findViewById(R.id.btn_submit);
        ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
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
                    Utility.showDecisionDialog(ApprovalRequestVendorVoacherListClass.this, getString(R.string.alert), "Are you Sure? Do you want to Reject this Conveyance!", new Utility.AlertCallback() {
                        @Override
                        public void callback() {
                            inventory_id = "" + getOrdersList.get(position).getId();
                            apiService.vendorRejectConveyanceDelate(new ApprovedRejectVendorConveyancePOst(inventory_id, reson.getText().toString().trim())).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                                @Override
                                protected void onSuccess(LoginResponse body) {
                                    alertDialog.dismiss();
                                    Toast.makeText(ApprovalRequestVendorVoacherListClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalRequestVendorVoacherListClass.this);
        LayoutInflater inflater = ((Activity) ApprovalRequestVendorVoacherListClass.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_seell_price_diloag, null);
        EditText reson = (EditText) dialogView.findViewById(R.id.reson);
        EditText amount = (EditText) dialogView.findViewById(R.id.amount);
        TextView submit = (TextView) dialogView.findViewById(R.id.btn_submit);
        TextView heading = (TextView) dialogView.findViewById(R.id.heading);
        ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
        heading.setText("Other Expense Approve Notes");
        reson.setHint("Notes");
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
                } else if (amount.getText().toString().trim() == null || amount.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Final Amount", Toast.LENGTH_LONG).show();
                } else {
                    Utility.showDecisionDialog(ApprovalRequestVendorVoacherListClass.this, getString(R.string.alert), "Are you Sure? Do you want to Approve this Conveyance!", new Utility.AlertCallback() {
                        @Override
                        public void callback() {
                            inventory_id = "" + getOrdersList.get(position).getId();
                            apiService.vendorApproveConveyanceDelate(new ApprovedVendorConveyancePOst(inventory_id, amount.getText().toString().trim(), reson.getText().toString().trim())).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                                @Override
                                protected void onSuccess(LoginResponse body) {
                                    alertDialog.dismiss();
                                    Toast.makeText(ApprovalRequestVendorVoacherListClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
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
