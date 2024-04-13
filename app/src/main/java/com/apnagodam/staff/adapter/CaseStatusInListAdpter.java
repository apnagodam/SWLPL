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
import com.apnagodam.staff.activity.casestatus.CaseStatusINListClass;
import com.apnagodam.staff.activity.convancy_voachar.MyConveyanceListClass;
import com.apnagodam.staff.module.AllConvancyList;
import com.apnagodam.staff.module.AllTruckBookListResponse;
import com.apnagodam.staff.module.CaseStatusINPojo;

import java.util.List;

/**
 * Created by Raju Singh on 01/08/2020.
 */

public class CaseStatusInListAdpter extends RecyclerView.Adapter<CaseStatusInListAdpter.CommudityResponseViewHolder> {
    private List<CaseStatusINPojo.Datum> commudityResponseList;
    private Context context;
    private BaseActivity activity;

    public CaseStatusInListAdpter(List<CaseStatusINPojo.Datum> body, CaseStatusINListClass myConveyanceListClass, BaseActivity activity) {
        this.commudityResponseList = body;
        this.context = myConveyanceListClass;
        this.activity = activity;
    }

    @Override
    public CommudityResponseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View CommudityResponseProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.conv_list_items, parent, false);
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
        if (commudityResponseList.get(position).getCaseId() != null) {
            holder.tv_gatepass.setText("" + commudityResponseList.get(position).getCaseId());
        } else {
            holder.tv_gatepass.setText("-");
        }
        if (commudityResponseList.get(position).getCustFname() != null) {
            holder.tv_name.setText("" + commudityResponseList.get(position).getCustFname());
        } else {
            holder.tv_name.setText("-");
        }
        if (commudityResponseList.get(position).getCommodityId() != null) {
            holder.tv_date.setText("" + (commudityResponseList.get(position).getCateName()+"("+commudityResponseList.get(position).getCommodityType()+")"));
        } else {
            holder.tv_date.setText("-");
        }
        holder.tv_action.setVisibility(View.GONE);
        holder.view12.setVisibility(View.GONE);
        holder.tv_gatepass.setTextColor(Color.BLACK);
        holder.tv_name.setTextColor(Color.BLACK);
        holder.tv_date.setTextColor(Color.BLACK);
        activity.hideDialog();

        holder.view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof CaseStatusINListClass) {
                    ((CaseStatusINListClass) context).ViewData(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commudityResponseList.size();
    }

    public class CommudityResponseViewHolder extends RecyclerView.ViewHolder {
        TextView tv_gatepass, tv_apply, tv_name, tv_date, tv_rejected, tv_action;
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
        }
    }


}