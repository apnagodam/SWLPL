package com.apnagodam.staff.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.apnagodam.staff.Base.BaseRecyclerViewAdapter;
import com.apnagodam.staff.Base.BaseViewHolder;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.LeadListingActivity;
import com.apnagodam.staff.databinding.LayoutTopCaseGenerateBinding;
import com.apnagodam.staff.module.AllLeadsResponse;

import java.util.Collection;
import java.util.List;

public class LeadsTopAdapter extends BaseRecyclerViewAdapter {
    private List<AllLeadsResponse.Lead> Leads;
    private Context context;
    public LeadsTopAdapter(List<AllLeadsResponse.Lead> leads, LeadListingActivity leadListingActivity) {
        this.Leads = leads;
        this.context = leadListingActivity;
    }

    @Override
    public BaseViewHolder inflateLayout(ViewDataBinding view) {
        return new DefaultersTopHolder(view);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.layout_top_case_generate;
    }

    @Override
    public Collection<?> getCollection() {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return Leads.size();
    }

    public enum Mode {
        TOP_100, TARRIF, LOCATION, RANGE
    }

    class DefaultersTopHolder extends BaseViewHolder<LayoutTopCaseGenerateBinding> {

        DefaultersTopHolder(@NonNull ViewDataBinding itemBinding) {
            super(itemBinding);
        }

        @Override
        public void onBind(int position) {
           /* if (position==0){

            }else {*/
                if (position % 2 == 0) {
                    binding.getRoot().setBackgroundColor(Color.parseColor("#EBEBEB"));
                } else {
                    binding.getRoot().setBackgroundColor(Color.WHITE);
                }
                binding.tvId.setText("" + Leads.get(position).getId());
                binding.tvName.setText(Leads.get(position).getCustomerName());
                binding.tvPhone.setText(Leads.get(position).getPhone());
                binding.tvId.setTextColor(Color.BLACK);
                binding.tvName.setTextColor(Color.BLACK);
                binding.tvPhone.setTextColor(Color.BLACK);
                binding.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (context instanceof LeadListingActivity) {
                            ((LeadListingActivity) context).ViewData(position);
                        }
                    }
                });

            }
        //}
    }

}
