package com.apnagodam.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.dispaldgeinventory.ApprovalRequestDispladgeListClass;
import com.apnagodam.staff.activity.vendorPanel.ApprovalRequestVendorVoacherListClass;
import com.apnagodam.staff.module.AllVendorConvancyList;
import com.apnagodam.staff.module.DispladgePojo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Raju Singh on 01/08/2020.
 */

public class ApprovalRequestDispladgeListAdpter extends RecyclerView.Adapter<ApprovalRequestDispladgeListAdpter.CommudityResponseViewHolder> {
    private List<DispladgePojo.Datum> commudityResponseList;
    private Context context;
    private BaseActivity activity;

    public ApprovalRequestDispladgeListAdpter(List<DispladgePojo.Datum> getOrdersList, ApprovalRequestDispladgeListClass approvalRequestDispladgeListClass, BaseActivity activity) {
        this.commudityResponseList = getOrdersList;
        this.context = approvalRequestDispladgeListClass;
        this.activity = activity;
    }

    @Override
    public CommudityResponseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View CommudityResponseProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.approval_request_conv_list_items, parent, false);
        CommudityResponseViewHolder gvh = new CommudityResponseViewHolder(CommudityResponseProductView);
        return gvh;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CommudityResponseViewHolder holder, final int position) {
        if (position % 2 == 0) {
            holder.cell_container.setBackgroundColor(Color.parseColor("#EBEBEB"));
        } else {
            holder.cell_container.setBackgroundColor(Color.WHITE);
        }
        if (commudityResponseList.get(position).getUserName() != null) {
            holder.tv_gatepass.setText("" + commudityResponseList.get(position).getUserName()+"("+commudityResponseList.get(position).getUserPhoneNo()+")");
        } else {
            holder.tv_gatepass.setText("-");
        }
        if (commudityResponseList.get(position).getTerminalName() != null) {
            holder.tv_name.setText(" " + commudityResponseList.get(position).getTerminalName()+"("+commudityResponseList.get(position).getWarehouseCode()+")");
        } else {
            holder.tv_name.setText("-");
        }
        if (commudityResponseList.get(position).getCategory() != null) {
            String commodityType = commudityResponseList.get(position).getCommodityType();
            if (commodityType.equalsIgnoreCase("Primary")) {
                commodityType = "किसानी";
            } else {
                commodityType = "MTP";
            }
            holder.tv_date.setText(" " + commudityResponseList.get(position).getCategory()+"("+commodityType+")");
        } else {
            holder.tv_date.setText("-");
        }
        if (commudityResponseList.get(position).getStatus() != null) {
            if (commudityResponseList.get(position).getStatus()==1) {
                // show button self rejected
                holder.tv_rejected.setVisibility(View.GONE);
                holder.tv_action.setVisibility(View.VISIBLE);
                holder.view12.setVisibility(View.VISIBLE);
                holder.tv_action_apprved.setVisibility(View.VISIBLE);
            } else if (commudityResponseList.get(position).getStatus()==0) {
                // rejected by self or approved from high authority
                holder.tv_rejected.setVisibility(View.VISIBLE);
                holder.tv_action.setVisibility(View.GONE);
                holder.view12.setVisibility(View.GONE);
                holder.tv_action_apprved.setVisibility(View.GONE);
                holder.tv_rejected.setText(context.getResources().getString(R.string.rejected));
                holder.tv_rejected.setTextColor(context.getResources().getColor(R.color.red));
            } else if (commudityResponseList.get(position).getStatus()==2) {
                //approved from high authority
                holder.tv_rejected.setVisibility(View.VISIBLE);
                holder.tv_action.setVisibility(View.GONE);
                holder.view12.setVisibility(View.GONE);
                holder.tv_action_apprved.setVisibility(View.GONE);
                holder.tv_rejected.setText(context.getResources().getString(R.string.approved));
                holder.tv_rejected.setTextColor(context.getResources().getColor(R.color.colorGreen));
            }
        }
        holder.tv_gatepass.setTextColor(Color.BLACK);
        holder.tv_name.setTextColor(Color.BLACK);
        holder.tv_date.setTextColor(Color.BLACK);
        activity.hideDialog();
        holder.tv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ApprovalRequestDispladgeListClass) {
                    ((ApprovalRequestDispladgeListClass) context).cancelConv(position);
                }
            }
        });
        holder.tv_action_apprved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ApprovalRequestDispladgeListClass) {
                    ((ApprovalRequestDispladgeListClass) context).ApprovedConv(position);
                }
            }
        });
        holder.view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof ApprovalRequestDispladgeListClass) {
                    ((ApprovalRequestDispladgeListClass) context).ViewData(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commudityResponseList.size();
    }

    public class CommudityResponseViewHolder extends RecyclerView.ViewHolder {
        TextView tv_gatepass, tv_rejected, tv_name, tv_date, tv_action_apprved, tv_action;
        LinearLayout cell_container;
        ImageView view1;
        View view12;

        public CommudityResponseViewHolder(View view) {
            super(view);
            tv_gatepass = view.findViewById(R.id.tv_gatepass);
            tv_name = view.findViewById(R.id.tv_name);
            view1 = view.findViewById(R.id.view);
            view12 = view.findViewById(R.id.view12);
            tv_action_apprved = view.findViewById(R.id.tv_action_apprved);
            tv_date = view.findViewById(R.id.tv_date);
            tv_rejected = view.findViewById(R.id.tv_rejected);
            cell_container = view.findViewById(R.id.cell_container);
            tv_action = view.findViewById(R.id.tv_action);
        }
    }


}