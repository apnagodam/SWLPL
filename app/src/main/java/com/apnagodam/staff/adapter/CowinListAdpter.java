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
import com.apnagodam.staff.activity.CoWinVactionClass;
import com.apnagodam.staff.activity.convancy_voachar.MyConveyanceListClass;
import com.apnagodam.staff.module.AllConvancyList;
import com.apnagodam.staff.module.CowinPojo;

import java.util.List;

/**
 * Created by Raju Singh on 01/08/2020.
 */

public class CowinListAdpter extends RecyclerView.Adapter<CowinListAdpter.CommudityResponseViewHolder> {
    private List<CowinPojo.Center> commudityResponseList;
    private Context context;

    public CowinListAdpter(List<CowinPojo.Center> body, CoWinVactionClass myConveyanceListClass) {
        this.commudityResponseList = body;
        this.context = myConveyanceListClass;
    }

    @Override
    public CommudityResponseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View CommudityResponseProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cowin_list_items, parent, false);
        CommudityResponseViewHolder gvh = new CommudityResponseViewHolder(CommudityResponseProductView);
        return gvh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CommudityResponseViewHolder holder, final int position) {
        if (commudityResponseList.get(position).getName() != null) {
            holder.tv_gatepass.setText("" + commudityResponseList.get(position).getName());
        } else {
            holder.tv_gatepass.setText("-");
        }
        if (commudityResponseList.get(position).getAddress() != null) {
            holder.tv_name.setText(" " + commudityResponseList.get(position).getAddress()+","+ commudityResponseList.get(position).getBlockName()+","+ commudityResponseList.get(position).getDistrictName()+","+ commudityResponseList.get(position).getStateName());
        } else {
            holder.tv_name.setText("-");
        }
        if (commudityResponseList.get(position).getSessions() != null) {
            holder.tv_date.setText("Eligible:   " + (commudityResponseList.get(position).getSessions().get(position).getMinAgeLimit()+"+"));
        } else {
            holder.tv_date.setText("-");
        }
        if (commudityResponseList.get(position).getSessions() != null) {
            holder.tv_slotes.setText("slots:   " + (commudityResponseList.get(position).getSessions().get(position).getAvailableCapacity()));
        } else {
            holder.tv_slotes.setText("-");
        }
    }

    @Override
    public int getItemCount() {
        return commudityResponseList.size();
    }

    public class CommudityResponseViewHolder extends RecyclerView.ViewHolder {
        TextView tv_gatepass, tv_slotes, tv_name, tv_date, tv_rejected, tv_action;
        LinearLayout cell_container;
        ImageView view1;
        View view12;

        public CommudityResponseViewHolder(View view) {
            super(view);
            tv_gatepass = view.findViewById(R.id.tv_name);
            tv_name = view.findViewById(R.id.tv_address_block_district_state);
            tv_slotes = view.findViewById(R.id.tv_slotes);
            view12 = view.findViewById(R.id.view12);
            tv_rejected = view.findViewById(R.id.tv_rejected);
            tv_date = view.findViewById(R.id.tv_eligble);
            cell_container = view.findViewById(R.id.cell_container);
            tv_action = view.findViewById(R.id.tv_action);
        }
    }


}