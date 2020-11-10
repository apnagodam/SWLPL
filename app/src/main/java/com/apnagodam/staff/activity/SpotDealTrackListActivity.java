package com.apnagodam.staff.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.apnagodam.staff.R;
import com.apnagodam.staff.adapter.SpotSellDealTrackListAdpter;
import com.apnagodam.staff.databinding.MyCommudityListBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.SpotSellDealTrackPojo;
import com.apnagodam.staff.utils.Utility;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SpotDealTrackListActivity extends BaseActivity<MyCommudityListBinding> {
    private SpotSellDealTrackListAdpter myCommudityListAdpter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<SpotSellDealTrackPojo.Datum> getOrdersList;
    private List<SpotSellDealTrackPojo.Datum> tempraryList;
    private String inventory_id = "";
    private String ids = "";
    List<SpotSellDealTrackPojo.Datum> addInventoryResponse;

    @Override
    protected int getLayoutResId() {
        return R.layout.my_commudity_list;
    }

    @Override
    protected void setUp() {
        binding.titleHeader.setText(getResources().getString(R.string.spot_sell));
        binding.tvPhone.setText(getResources().getString(R.string.view));
        binding.tvName.setText(getResources().getString(R.string.net_Weight));
        binding.tvId.setText(getResources().getString(R.string.commodity));
        binding.pageNextPrivious.setVisibility(View.VISIBLE);
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
        getBidsList("");
        binding.tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageOffset != 1) {
                    pageOffset--;
                    getBidsList("");
                }
            }
        });
        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalPage != pageOffset) {
                    pageOffset++;
                    getBidsList("");
                }
            }
        });

        binding.swipeRefresherStock.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBidsList("");
            }
        });
        binding.filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SpotDealTrackListActivity.this);
                LayoutInflater inflater = ((Activity) SpotDealTrackListActivity.this).getLayoutInflater();
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
                            getBidsList(notes.getText().toString().trim());
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

    private void getBidsList(String SerachKey) {
        showDialog();
        apiService.getSpotSellDealTrackList("15", pageOffset, SerachKey).enqueue(new NetworkCallback<SpotSellDealTrackPojo>(getActivity()) {
            @Override
            protected void onSuccess(SpotSellDealTrackPojo body) {
                tempraryList.clear();
                getOrdersList.clear();
                addInventoryResponse = body.getDeals().getData();
                binding.swipeRefresherStock.setRefreshing(false);
                if (body.getDeals().getData().size() == 0) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.fieldStockList.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } else if (body.getDeals().getLastPage() == 1) {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.fieldStockList.setVisibility(View.VISIBLE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                    getOrdersList.clear();
                    tempraryList.clear();
                    totalPage = body.getDeals().getLastPage();
                    getOrdersList.addAll(body.getDeals().getData());
                    tempraryList.addAll(getOrdersList);
                    myCommudityListAdpter.notifyDataSetChanged();
                } else {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.fieldStockList.setVisibility(View.VISIBLE);
                    binding.pageNextPrivious.setVisibility(View.VISIBLE);
                    getOrdersList.clear();
                    tempraryList.clear();
                    totalPage = body.getDeals().getLastPage();
                    getOrdersList.addAll(body.getDeals().getData());
                    tempraryList.addAll(getOrdersList);
                    myCommudityListAdpter.notifyDataSetChanged();
                }

            }
        });
    }

    private void setDataAdapter() {
        binding.fieldStockList.addItemDecoration(new DividerItemDecoration(SpotDealTrackListActivity.this, LinearLayoutManager.HORIZONTAL));
        binding.fieldStockList.setHasFixedSize(true);
        binding.fieldStockList.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(SpotDealTrackListActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.fieldStockList.setLayoutManager(horizontalLayoutManager);
        myCommudityListAdpter = new SpotSellDealTrackListAdpter(ids, getOrdersList, SpotDealTrackListActivity.this, getActivity());
        binding.fieldStockList.setAdapter(myCommudityListAdpter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(StaffDashBoardActivity.class);
    }


    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(SpotDealTrackListActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = SpotDealTrackListActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.lead_view_new, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = dialogView.findViewById(R.id.cancel_btn);
        TextView gate_pass_no = dialogView.findViewById(R.id.gate_pass_no);
        TextView terminal_name = dialogView.findViewById(R.id.terminal_name);
        TextView location_name = dialogView.findViewById(R.id.location_name);
        TextView commodity_name = dialogView.findViewById(R.id.commodity_name);
        TextView wieght = dialogView.findViewById(R.id.wieght);
        TextView quality_name = dialogView.findViewById(R.id.quality_name);
        TextView creadte_date_name = dialogView.findViewById(R.id.creadte_date_name);
        TextView a1 = dialogView.findViewById(R.id.a1);
        TextView a2 = dialogView.findViewById(R.id.a2);
        TextView a6 = dialogView.findViewById(R.id.a6);
        a2.setText(getResources().getString(R.string.buyer_seller));
        a1.setText(getResources().getString(R.string.type));
        a6.setText(getResources().getString(R.string.total_weight_qtl));
        inventory_id = "" + getOrdersList.get(position).getId();
        if (SharedPreferencesRepository.getDataManagerInstance().getUser().getUserId().equalsIgnoreCase(getOrdersList.get(position).getSellerId())) {
            gate_pass_no.setText("Sell");
            terminal_name.setText("" + ((getOrdersList.get(position).getFname()) != null ? getOrdersList.get(position).getFname() : "N/A"));
        } else {
            gate_pass_no.setText("Buy");
            terminal_name.setText("" + ((getOrdersList.get(position).getSellerName()) != null ? getOrdersList.get(position).getSellerName() : "N/A"));
        }

        location_name.setText("" + ((getOrdersList.get(position).getLocation()) != null ? getOrdersList.get(position).getLocation() : "N/A"));
        commodity_name.setText("" + ((getOrdersList.get(position).getCategory()) != null ? getOrdersList.get(position).getCategory() : "N/A"));
        wieght.setText("" + ((getOrdersList.get(position).getQuantity()) != null ? getOrdersList.get(position).getQuantity() : "N/A"));
        quality_name.setText("" + ((getOrdersList.get(position).getPrice()) != null ? getOrdersList.get(position).getPrice() : "N/A"));
        creadte_date_name.setText("" + ((getOrdersList.get(position).getUpdatedAt()) != null ? (getOrdersList.get(position).getUpdatedAt()) : "N/A"));
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void wantToSell(int position) {
        inventory_id = "" + getOrdersList.get(position).getId();
        Intent intent = new Intent(SpotDealTrackListActivity.this, ContractFormBillPrintClass.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("serialzable", (Serializable) addInventoryResponse.get(position));
        startActivity(intent);
    }
}