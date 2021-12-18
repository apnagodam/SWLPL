package com.apnagodam.staff.activity.vendorPanel;

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
import com.apnagodam.staff.Network.Request.SelfRejectVendorConveyancePOst;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.convancy_voachar.ApprovalRequestConveyanceListClass;
import com.apnagodam.staff.activity.convancy_voachar.UploadConveyanceVoacharClass;
import com.apnagodam.staff.adapter.VendorVoacherListAdpter;
import com.apnagodam.staff.databinding.ConvencyListBinding;
import com.apnagodam.staff.databinding.VendorConvencyListBinding;
import com.apnagodam.staff.module.AllConvancyList;
import com.apnagodam.staff.module.AllVendorConvancyList;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyVendorVoacherListClass extends BaseActivity<VendorConvencyListBinding> {
    private VendorVoacherListAdpter convancyListAdpter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<AllVendorConvancyList.Datum> getOrdersList;
    private List<AllVendorConvancyList.Datum> tempraryList;
    private String inventory_id = "";
    private String startMeterImage, endMeterImage;
    // for FB button
    private Animation fabOpenAnimation;
    private Animation fabCloseAnimation;
    private boolean isFabMenuOpen = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.vendor_convency_list;
    }

    @Override
    protected void setUp() {
        binding.tvId.setText("Unique ID");
        getOrdersList = new ArrayList();
        tempraryList = new ArrayList();
        setSupportActionBar(binding.toolbar);
        binding.titleHeader.setText("Vendor Conveyance List");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(StaffDashBoardActivity.class);
            }
        });
        setDataAdapter();
        getConvencyList("");
      /*  binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(UploadConveyanceVoacharClass.class);
            }
        });*/
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MyVendorVoacherListClass.this);
                LayoutInflater inflater = ((Activity) MyVendorVoacherListClass.this).getLayoutInflater();
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
            startActivity(UploadVendorVoacherClass.class);
        }
        public void onShareFabClick(View view) {
            startActivityAndClear(ApprovalRequestVendorVoacherListClass.class);
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

    private void getConvencyList(String SerachKey) {
        showDialog();
        apiService.getVendorConvancyList("15", pageOffset, SerachKey).enqueue(new NetworkCallback<AllVendorConvancyList>(getActivity()) {
            @Override
            protected void onSuccess(AllVendorConvancyList body) {
                    binding.shareLabelTextView.setText("Approval Request " + "(" + body.getRequest_count() + ")");
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
                    getOrdersList.clear();
                    tempraryList.clear();
                    totalPage = body.getData().getLastPage();
                    getOrdersList.addAll(body.getData().getDataa());
                    tempraryList.addAll(getOrdersList);
                    convancyListAdpter.notifyDataSetChanged();
                } else {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.fieldStockList.setVisibility(View.VISIBLE);
                    binding.pageNextPrivious.setVisibility(View.VISIBLE);
                    getOrdersList.clear();
                    tempraryList.clear();
                    totalPage = body.getData().getLastPage();
                    getOrdersList.addAll(body.getData().getDataa());
                    tempraryList.addAll(getOrdersList);
                    convancyListAdpter.notifyDataSetChanged();
                }

            }
        });
    }

    private void setDataAdapter() {
        binding.fieldStockList.addItemDecoration(new DividerItemDecoration(MyVendorVoacherListClass.this, LinearLayoutManager.HORIZONTAL));
        binding.fieldStockList.setHasFixedSize(true);
        binding.fieldStockList.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MyVendorVoacherListClass.this, LinearLayoutManager.VERTICAL, false);
        binding.fieldStockList.setLayoutManager(horizontalLayoutManager);
        convancyListAdpter = new VendorVoacherListAdpter(getOrdersList, MyVendorVoacherListClass.this, getActivity());
        binding.fieldStockList.setAdapter(convancyListAdpter);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(MyVendorVoacherListClass.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = MyVendorVoacherListClass.this.getLayoutInflater();
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
                new PhotoFullPopupWindow(MyVendorVoacherListClass.this, R.layout.popup_photo_full, view, startMeterImage, null);
            }
        });
        commodity_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(MyVendorVoacherListClass.this, R.layout.popup_photo_full, view, endMeterImage, null);
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
        customer_name.setText("" + ((getOrdersList.get(position).getFirstName()) != null ? getOrdersList.get(position).getFirstName()+" "+getOrdersList.get(position).getLastName()+"("+getOrdersList.get(position).getEmpId()+")" : "N/A"));
        commodity_name.setText("" + ((getOrdersList.get(position).getExpensesName()) != null ? getOrdersList.get(position).getExpensesName() : "N/A"));
        phone_no.setText("" + ((getOrdersList.get(position).getVendorFirstName()) != null ? getOrdersList.get(position).getVendorFirstName()+" "+getOrdersList.get(position).getVendorLastName()+"("+getOrdersList.get(position).getVendorUniqueId()+")" : "N/A"));
        location_title.setText("" + ((getOrdersList.get(position).getWarehouseName()) != null ? getOrdersList.get(position).getWarehouseName()+"("+getOrdersList.get(position).getWarehouseCode()+")" : "N/A"));
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
        Utility.showDecisionDialog(MyVendorVoacherListClass.this, getString(R.string.alert), getResources().getString(R.string.alert_conv_reject), new Utility.AlertCallback() {
            @Override
            public void callback() {
                inventory_id = "" + getOrdersList.get(position).getId();
                apiService.vendorConveyanceDelate(new SelfRejectVendorConveyancePOst(inventory_id)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                    @Override
                    protected void onSuccess(LoginResponse body) {
                        Toast.makeText(MyVendorVoacherListClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                        getConvencyList("");
                    }
                });
            }
        });
    }
}
