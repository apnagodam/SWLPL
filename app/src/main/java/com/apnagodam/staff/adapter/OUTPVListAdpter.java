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
import com.apnagodam.staff.activity.in.pv.PVListingActivityClass;
import com.apnagodam.staff.activity.out.pv.OUTPVListingActivityClass;
import com.apnagodam.staff.module.PVGetListPojo;

import java.util.List;


/**
 * Created by Raju Singh on 01/08/2020.
 */

public class OUTPVListAdpter extends RecyclerView.Adapter<OUTPVListAdpter.CommudityResponseViewHolder> {
    private List<PVGetListPojo.Datum> commudityResponseList;
    private Context context;
    private BaseActivity activity;

    public OUTPVListAdpter(List<PVGetListPojo.Datum> body, OUTPVListingActivityClass myCommudityListActivity, BaseActivity activity) {

        this.commudityResponseList = body;
        this.context = myCommudityListActivity;
        this.activity = activity;
    }

    @Override
    public CommudityResponseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View CommudityResponseProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pv_listing_itmes, parent, false);
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
        if (commudityResponseList.get(position).getEmpFname() != null) {
            holder.tv_auditor.setText("" + commudityResponseList.get(position).getEmpFname() + " " + commudityResponseList.get(position).getEmpLname() + "(" + commudityResponseList.get(position).getEmpPhone() + ")");
        } else {
            holder.tv_auditor.setText("N/A");
        }
        if (commudityResponseList.get(position).getName() != null) {
            holder.tv_terminal.setText("" + commudityResponseList.get(position).getName() + "(" + commudityResponseList.get(position).getWarehouseCode() + ")");
        } else {
            holder.tv_terminal.setText("N/A");
        }
        if (commudityResponseList.get(position).getFname() != null) {
            holder.tv_name.setText("" + commudityResponseList.get(position).getFname() + "(" + commudityResponseList.get(position).getPhone() + ")");
        } else {
            holder.tv_name.setText("N/A");
        }
        if (commudityResponseList.get(position).getCategory() != null) {
            holder.tv_commodity.setText("" + commudityResponseList.get(position).getCategory());
        } else {
            holder.tv_commodity.setText("N/A");
        }


        if (commudityResponseList.get(position).getStackNo() != null) {
            holder.tv_stackno.setText("" + commudityResponseList.get(position).getStackNo());
        } else {
            holder.tv_stackno.setText("N/A");
        }
        if (commudityResponseList.get(position).getUpdatedAt() != null) {
            holder.tv_date.setText("" + commudityResponseList.get(position).getUpdatedAt());
        } else {
            holder.tv_date.setText("N/A");
        }
        holder.tv_auditor.setTextColor(Color.BLACK);
        holder.tv_name.setTextColor(Color.BLACK);
        holder.tv_terminal.setTextColor(Color.BLACK);
        holder.tv_commodity.setTextColor(Color.BLACK);
        holder.tv_stackno.setTextColor(Color.BLACK);
        holder.tv_date.setTextColor(Color.BLACK);
        holder.view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof OUTPVListingActivityClass) {
                    ((OUTPVListingActivityClass) context).ViewData(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commudityResponseList.size();
    }

    public class CommudityResponseViewHolder extends RecyclerView.ViewHolder {
        TextView tv_auditor, tv_terminal, tv_name, tv_commodity, tv_stackno, tv_date;
        LinearLayout cell_container;
        ImageView view1;


        public CommudityResponseViewHolder(View view) {
            super(view);
            tv_auditor = view.findViewById(R.id.tv_auditor);
            tv_terminal = view.findViewById(R.id.tv_terminal);
            tv_name = view.findViewById(R.id.tv_user_name);
            tv_commodity = view.findViewById(R.id.tv_commodity);
            tv_stackno = view.findViewById(R.id.tv_stackno);
            tv_date = view.findViewById(R.id.tv_date);
            view1 = view.findViewById(R.id.view_stock_details);
            cell_container = view.findViewById(R.id.cell_container);
        }
    }
}