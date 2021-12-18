package com.apnagodam.staff.activity.out.pv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.NetworkCallbackWProgress;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.adapter.OUTPVListAdpter;
import com.apnagodam.staff.adapter.PVListAdpter;
import com.apnagodam.staff.databinding.ActivityListingPvBinding;
import com.apnagodam.staff.module.PVGetListPojo;
import com.apnagodam.staff.module.StockDetailsPVPojo;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;


public class OUTPVListingActivityClass extends BaseActivity<ActivityListingPvBinding> {
    private OUTPVListAdpter pvListAdpter;
    private int pageOffset = 1;
    private int totalPage = 0;
    private List<PVGetListPojo.Datum> AllCases;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_listing_pv;
    }

    @Override
    protected void setUp() {
        AllCases = new ArrayList();
        setAdapter();
        setSupportActionBar(binding.toolbar);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(OUTPVListingActivityClass.this);
                LayoutInflater inflater = ((Activity) OUTPVListingActivityClass.this).getLayoutInflater();
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

    private void setAdapter() {
        binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(OUTPVListingActivityClass.this, LinearLayoutManager.VERTICAL));
        binding.rvDefaultersStatus.setHasFixedSize(true);
        binding.rvDefaultersStatus.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(OUTPVListingActivityClass.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);
        pvListAdpter = new OUTPVListAdpter(AllCases, OUTPVListingActivityClass.this, getActivity());
        binding.rvDefaultersStatus.setAdapter(pvListAdpter);

    }

    private void getAllCases(String search) {
        showDialog();
        apiService.getallGatepass().enqueue(new NetworkCallback<PVGetListPojo>(getActivity()) {
            @Override
            protected void onSuccess(PVGetListPojo body) {
                binding.swipeRefresherStock.setRefreshing(false);
                AllCases.clear();
                if (body.getData() == null) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.rvDefaultersStatus.setVisibility(View.GONE);
                    binding.pageNextPrivious.setVisibility(View.GONE);
                } else {
                    AllCases.clear();
                    if (body.getData().size() > 10) {
                        binding.pageNextPrivious.setVisibility(View.GONE);
                    }
                    // totalPage = body.getFirstKataParchiData().getLastPage();
                    AllCases.addAll(body.getData());
                    pvListAdpter.notifyDataSetChanged();
                }
            }
        });
    }

    public void ViewData(int position) {
        BottomSheetDialog dialog = new BottomSheetDialog(OUTPVListingActivityClass.this);
        dialog.setContentView(R.layout.pv_stock_details);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        TextView totalpv = (TextView) dialog.findViewById(R.id.totalpv);
        RecyclerView rv_defaulters_status = (RecyclerView) dialog.findViewById(R.id.rv_defaulters_status);
        dialog.setCancelable(false);
        dialog.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        apiService.getstockdetails("" + AllCases.get(position).getId()).enqueue(new NetworkCallbackWProgress<StockDetailsPVPojo>(getActivity()) {
            @Override
            protected void onSuccess(StockDetailsPVPojo body) {
                Double totalpvValue = 0.0;
                for (int i = 0; i < body.getData().size(); i++) {
                    totalpvValue = totalpvValue+Double.valueOf(body.getData().get(i).getTotal());
                }
                totalpv.setText("" + totalpvValue);
                rv_defaulters_status.addItemDecoration(new DividerItemDecoration(OUTPVListingActivityClass.this, LinearLayoutManager.VERTICAL));
                rv_defaulters_status.setHasFixedSize(true);
                rv_defaulters_status.setNestedScrollingEnabled(false);
                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(OUTPVListingActivityClass.this, LinearLayoutManager.VERTICAL, false);
                rv_defaulters_status.setLayoutManager(horizontalLayoutManager);
                OUTPVListingActivityClass.PVStockDetailsAdpter adpter = new OUTPVListingActivityClass.PVStockDetailsAdpter(body.getData(), OUTPVListingActivityClass.this);
                rv_defaulters_status.setAdapter(adpter);
            }
        });
    }

    public class PVStockDetailsAdpter extends RecyclerView.Adapter<PVStockDetailsAdpter.CommudityResponseViewHolder> {
        private List<StockDetailsPVPojo.Datum> commudityResponseList;
        private Context context;
        private BaseActivity activity;

        public PVStockDetailsAdpter(List<StockDetailsPVPojo.Datum> body, OUTPVListingActivityClass pvListingActivityClass) {
            this.commudityResponseList = body;
            this.context = pvListingActivityClass;
        }

        @Override
        public CommudityResponseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflate the layout file
            View CommudityResponseProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pv_stock_deetails_itmes, parent, false);
            CommudityResponseViewHolder gvh = new CommudityResponseViewHolder(CommudityResponseProductView);
            return gvh;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(CommudityResponseViewHolder holder, final int position) {
            if (position % 2 == 0) {
                holder.cell_container.setBackgroundColor(Color.parseColor("#EBEBEB"));
            } else {
                holder.cell_container.setBackgroundColor(Color.WHITE);
            }
            if (commudityResponseList.get(position).getBlockNo() != null) {
                holder.tv_block.setText("" + commudityResponseList.get(position).getBlockNo());
            } else {
                holder.tv_block.setText("N/A");
            }
            if (commudityResponseList.get(position).getDhang() != null) {
                holder.tv_dhang.setText("" + commudityResponseList.get(position).getDhang());
            } else {
                holder.tv_dhang.setText("N/A");
            }
            if (commudityResponseList.get(position).getDanda() != null) {
                holder.tv_dhanda.setText("" + commudityResponseList.get(position).getDanda());
            } else {
                holder.tv_dhanda.setText("N/A");
            }
            if (commudityResponseList.get(position).getHeight() != null) {
                holder.tv_height.setText("" + commudityResponseList.get(position).getHeight());
            } else {
                holder.tv_height.setText("N/A");
            }
            if (commudityResponseList.get(position).getPlusMinus() != null) {
                holder.tv_plusminus.setText("" + commudityResponseList.get(position).getPlusMinus());
            } else {
                holder.tv_plusminus.setText("N/A");
            }
            if (commudityResponseList.get(position).getTotal() != null) {
                holder.tv_total.setText("" + commudityResponseList.get(position).getTotal());
            } else {
                holder.tv_total.setText("N/A");
            }
            if (commudityResponseList.get(position).getRemark() != null) {
                holder.tv_remark.setText("" + commudityResponseList.get(position).getRemark());
            } else {
                holder.tv_remark.setText("N/A");
            }
            holder.tv_block.setTextColor(Color.BLACK);
            holder.tv_dhang.setTextColor(Color.BLACK);
            holder.tv_dhanda.setTextColor(Color.BLACK);
            holder.tv_height.setTextColor(Color.BLACK);
            holder.tv_plusminus.setTextColor(Color.BLACK);
            holder.tv_total.setTextColor(Color.BLACK);
            holder.tv_remark.setTextColor(Color.BLACK);

        }

        @Override
        public int getItemCount() {
            return commudityResponseList.size();
        }

        public class CommudityResponseViewHolder extends RecyclerView.ViewHolder {
            TextView tv_block, tv_dhang, tv_dhanda, tv_height, tv_plusminus, tv_total, tv_remark;
            LinearLayout cell_container;

            public CommudityResponseViewHolder(View view) {
                super(view);
                tv_block = view.findViewById(R.id.tv_block);
                tv_dhang = view.findViewById(R.id.tv_dhang);
                tv_dhanda = view.findViewById(R.id.tv_dhanda);
                tv_height = view.findViewById(R.id.tv_height);
                tv_plusminus = view.findViewById(R.id.tv_plusminus);
                tv_total = view.findViewById(R.id.tv_total);
                tv_remark = view.findViewById(R.id.tv_remark);
                cell_container = view.findViewById(R.id.cell_container);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(StaffDashBoardActivity.class);
    }
}
