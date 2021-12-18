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
import com.apnagodam.staff.activity.feedbackemp.UploadEmpSuggestionClass;
import com.apnagodam.staff.module.FeedbackListPojo;

import java.util.List;

/**
 * Created by Raju Singh on 01/08/2020.
 */

public class EmpSuggestionListAdpter extends RecyclerView.Adapter<EmpSuggestionListAdpter.CommudityResponseViewHolder> {
    private List<FeedbackListPojo.Datum> commudityResponseList;
    private Context context;
    private BaseActivity activity;


    public EmpSuggestionListAdpter(List<FeedbackListPojo.Datum> body, UploadEmpSuggestionClass uploadEmpSuggestionClass, BaseActivity activity) {
        this.commudityResponseList = body;
        this.context = uploadEmpSuggestionClass;
        this.activity = activity;
    }

    @Override
    public CommudityResponseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View CommudityResponseProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_itmes, parent, false);
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
        if (commudityResponseList.get(position).getDesignation() != null) {
            holder.tv_department_name.setText("" + commudityResponseList.get(position).getDesignation());
        } else {
            holder.tv_department_name.setText("N/A");
        }
        if (commudityResponseList.get(position).getStatus() == 1) {
            // show button self rejected
            holder.tv_problem.setText(context.getResources().getString(R.string.pending));
            holder.tv_problem.setTextColor(context.getResources().getColor(R.color.darkYellow));
        } else if (commudityResponseList.get(position).getStatus() == 0) {
            // rejected by self or approved from high authority
            holder.tv_problem.setText(context.getResources().getString(R.string.rejected));
            holder.tv_problem.setTextColor(context.getResources().getColor(R.color.red));
        } else if (commudityResponseList.get(position).getStatus() == 2) {
            //approved from high authority
            holder.tv_problem.setText(context.getResources().getString(R.string.approved));
            holder.tv_problem.setTextColor(context.getResources().getColor(R.color.colorGreen));
        }

        if (commudityResponseList.get(position).getApproveBy() != null) {
            holder.tv_solution.setText("" + commudityResponseList.get(position).getApproveBy());
        } else {
            holder.tv_solution.setText("N/A");
        }
        holder.tv_department_name.setTextColor(Color.BLACK);
        holder.tv_solution.setTextColor(Color.BLACK);
        activity.hideDialog();

        holder.view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof UploadEmpSuggestionClass) {
                    ((UploadEmpSuggestionClass) context).ViewData(position);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return commudityResponseList.size();
    }

    public class CommudityResponseViewHolder extends RecyclerView.ViewHolder {
        TextView tv_department_name, tv_problem, tv_solution;
        LinearLayout cell_container;
        ImageView view1;

        public CommudityResponseViewHolder(View view) {
            super(view);
            tv_department_name = view.findViewById(R.id.tv_department_name);
            tv_problem = view.findViewById(R.id.tv_problem);
            view1 = view.findViewById(R.id.view);
            tv_solution = view.findViewById(R.id.tv_solution);
            cell_container = view.findViewById(R.id.cell_container);
        }
    }
}