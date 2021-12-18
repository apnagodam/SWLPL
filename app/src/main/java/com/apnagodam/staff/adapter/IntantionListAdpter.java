package com.apnagodam.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.IntantionApprovalListClass;
import com.apnagodam.staff.activity.convancy_voachar.MyConveyanceListClass;
import com.apnagodam.staff.module.AllConvancyList;
import com.apnagodam.staff.module.AllIntantionList;

import java.util.List;

/**
 * Created by Raju Singh on 01/08/2020.
 */

public class IntantionListAdpter extends RecyclerView.Adapter<IntantionListAdpter.CommudityResponseViewHolder> {
    private List<AllIntantionList.Datum> commudityResponseList;
    private Context context;
    private BaseActivity activity;

    public IntantionListAdpter(List<AllIntantionList.Datum> body, IntantionApprovalListClass intantionApprovalListClass, BaseActivity activity) {
        this.commudityResponseList = body;
        this.context = intantionApprovalListClass;
        this.activity = activity;
    }

    @Override
    public CommudityResponseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View CommudityResponseProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.intantion_list_items, parent, false);
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
        if (commudityResponseList.get(position).getFname() != null) {
            holder.tv_gatepass.setText("" + commudityResponseList.get(position).getFname());
        } else {
            holder.tv_gatepass.setText("-");
        }
        if (commudityResponseList.get(position).getWarehouseName() != null) {
            holder.tv_name.setText(" " + commudityResponseList.get(position).getWarehouseName());
        } else {
            holder.tv_name.setText("-");
        }
        if (commudityResponseList.get(position).getUpdatedAt() != null) {
            holder.tv_date.setText(" " + (commudityResponseList.get(position).getUpdatedAt()));
        } else {
            holder.tv_date.setText("-");
        }
        if (commudityResponseList.get(position).getStatus() != null) {
            if (commudityResponseList.get(position).getStatus().equalsIgnoreCase("1")) {
                // show button self rejected
                holder.tv_reject.setVisibility(View.VISIBLE);
                holder.tv_action.setVisibility(View.VISIBLE);
                holder.tv_rejected.setVisibility(View.GONE);
                holder.view12.setVisibility(View.VISIBLE);
            } else if (commudityResponseList.get(position).getStatus().equalsIgnoreCase("0")) {
                // rejected by self or approved from high authority
                holder.tv_rejected.setVisibility(View.VISIBLE);
                holder.tv_action.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
                holder.view12.setVisibility(View.GONE);
                holder.tv_rejected.setText(context.getResources().getString(R.string.rejected));
                holder.tv_rejected.setTextColor(context.getResources().getColor(R.color.red));
            } else if (commudityResponseList.get(position).getStatus().equalsIgnoreCase("2")) {
                //approved from high authority
                holder.tv_rejected.setVisibility(View.VISIBLE);
                holder.tv_reject.setVisibility(View.GONE);
                holder.tv_action.setVisibility(View.GONE);
                holder.view12.setVisibility(View.GONE);
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
                if (context instanceof IntantionApprovalListClass) {
                    ((IntantionApprovalListClass) context).ApproveIntantion(position);
                }
            }
        });
        holder.view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof IntantionApprovalListClass) {
                    ((IntantionApprovalListClass) context).ViewData(position);
                }
            }
        });
        holder.tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof IntantionApprovalListClass) {
                    ((IntantionApprovalListClass) context).cancelIntantion(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commudityResponseList.size();
    }

    public class CommudityResponseViewHolder extends RecyclerView.ViewHolder {
        TextView tv_gatepass, tv_apply, tv_name, tv_date, tv_rejected, tv_action,tv_reject;
        LinearLayout cell_container;
        ImageView view1;
        View view12;

        public CommudityResponseViewHolder(View view) {
            super(view);
            tv_gatepass = view.findViewById(R.id.tv_gatepass);
            tv_name = view.findViewById(R.id.tv_name);
            view1 = view.findViewById(R.id.view);
            view12 = view.findViewById(R.id.view12);
            tv_rejected = view.findViewById(R.id.tv_rejected);
            tv_date = view.findViewById(R.id.tv_date);
            cell_container = view.findViewById(R.id.cell_container);
            tv_action = view.findViewById(R.id.tv_action);
            tv_reject = view.findViewById(R.id.tv_reject);
        }
    }


}