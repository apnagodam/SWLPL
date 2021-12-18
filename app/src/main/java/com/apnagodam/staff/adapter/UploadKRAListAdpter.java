package com.apnagodam.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.kra.MyKRAHistoryListClass;
import com.apnagodam.staff.activity.kra.UploadMyKRAClass;
import com.apnagodam.staff.module.AllConvancyList;

import java.util.List;

/**
 * Created by Raju Singh on 01/08/2020.
 */

public class UploadKRAListAdpter extends RecyclerView.Adapter<UploadKRAListAdpter.CommudityResponseViewHolder> {
    private List<AllConvancyList.Datum> commudityResponseList;
    private Context context;
    private BaseActivity activity;

    public UploadKRAListAdpter(List<AllConvancyList.Datum> body, UploadMyKRAClass uploadMyKRAClass, BaseActivity activity) {
        this.commudityResponseList = body;
        this.context = uploadMyKRAClass;
        this.activity = activity;
    }

    @Override
    public CommudityResponseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View CommudityResponseProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.kra_upload_itmes, parent, false);
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
        int serialNumber = position + 1;
        holder.tv_serial_no.setText("" + serialNumber);
        if (commudityResponseList.get(position).getUniqueId() != null) {
            holder.tv_activites.setText("" + commudityResponseList.get(position).getUniqueId());
        } else {
            holder.tv_activites.setText("N/A");
        }
        if (commudityResponseList.get(position).getLocation() != null) {
            holder.tv_details.setText(" " + commudityResponseList.get(position).getLocation());
        } else {
            holder.tv_details.setText("N/A");
        }
        if (commudityResponseList.get(position).getLocation() != null) {
            holder.tv_max_score.setText(" " + commudityResponseList.get(position).getLocation());
        } else {
            holder.tv_max_score.setText("N/A");
        }
        holder.tv_serial_no.setTextColor(Color.BLACK);
        holder.tv_activites.setTextColor(Color.BLACK);
        holder.tv_details.setTextColor(Color.BLACK);
        holder.tv_max_score.setTextColor(Color.BLACK);
        activity.hideDialog();

        holder.view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof MyKRAHistoryListClass) {
                    ((MyKRAHistoryListClass) context).ViewData(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commudityResponseList.size();
    }

    public class CommudityResponseViewHolder extends RecyclerView.ViewHolder {
        TextView tv_serial_no,tv_activites,  tv_details, tv_max_score;
        LinearLayout cell_container;
        ImageView view1;
        EditText total_score;
       
        public CommudityResponseViewHolder(View view) {
            super(view);
            tv_serial_no = view.findViewById(R.id.tv_serial_no);
            tv_activites = view.findViewById(R.id.tv_activites);
            tv_details = view.findViewById(R.id.tv_details);
            view1 = view.findViewById(R.id.view);
            tv_max_score = view.findViewById(R.id.tv_max_score);
            total_score = view.findViewById(R.id.total_score);
            cell_container = view.findViewById(R.id.cell_container);
        }
    }


}