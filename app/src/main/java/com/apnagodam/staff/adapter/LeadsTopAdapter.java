package com.apnagodam.staff.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Base.BaseRecyclerViewAdapter;
import com.apnagodam.staff.Base.BaseViewHolder;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.lead.LeadListingActivity;
import com.apnagodam.staff.databinding.LayoutTopCaseGenerateBinding;
import com.apnagodam.staff.module.AllLeadsResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LeadsTopAdapter extends BaseRecyclerViewAdapter {
    private List<AllLeadsResponse.Datum> Leads;
    private Context context;
    private List<AllLeadsResponse.Lead> Lead;
    private BaseActivity activity;
    public LeadsTopAdapter(List<AllLeadsResponse.Datum> leads, LeadListingActivity leadListingActivity,BaseActivity activity) {
        this.Leads = leads;
        this.context = leadListingActivity;
        this.activity = activity;
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

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh", Locale.ENGLISH);
            Date strDate = null;
            try {
                strDate = sdf.parse(Leads.get(position).getCreatedAt());

                String previousDate = format.format(strDate);
                String currentDate = format.format(new Date());

//                String timePrevious = timeFormat.format(strDate);
//                String timeCurrent = timeFormat.format(new Date());
//                Integer timePreviousInt,timeCurrentInt;
//                timePreviousInt= Integer.valueOf(timePrevious);
//                timeCurrentInt= Integer.valueOf(timeCurrent);

                if (currentDate.compareTo(previousDate) == 0) {
                    long difference = strDate.getTime() - new Date().getTime();
                    int days = (int) (difference / (1000*60*60*24));
                    int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                    hours = (hours < 0 ? -hours : hours);
                    if (hours<=1){
                        binding.update.setVisibility(View.VISIBLE);
                    }else {
                        binding.update.setVisibility(View.GONE);
                    }
                }else {
                    binding.update.setVisibility(View.GONE);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            binding.tvId.setTextColor(Color.BLACK);
            binding.tvName.setTextColor(Color.BLACK);
            binding.tvPhone.setTextColor(Color.BLACK);
            activity.hideDialog();
            binding.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof LeadListingActivity) {
                        ((LeadListingActivity) context).ViewData(position);
                    }
                }
            });
            binding.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof LeadListingActivity) {
                  //      ((LeadListingActivity) context).editLeads(Lead.get(position).getData());
                    }
                }
            });

        }
        //}
    }

}
