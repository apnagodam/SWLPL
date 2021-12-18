package com.apnagodam.staff.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.apnagodam.staff.Base.BaseRecyclerViewAdapter;
import com.apnagodam.staff.Base.BaseViewHolder;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.caseid.CaseIDGenerateClass;
import com.apnagodam.staff.activity.dispaldgeinventory.UploadDispladgeClass;
import com.apnagodam.staff.databinding.RowCustomerNameBinding;

import java.util.Collection;
import java.util.List;

public class CustomerDipladgeNameAdapter extends BaseRecyclerViewAdapter {
    private List<String> Leads;
    private Context context;

    public CustomerDipladgeNameAdapter(List<String> leads, UploadDispladgeClass uploadDispladgeClass) {
        this.Leads = leads;
        this.context = uploadDispladgeClass;
    }

    @Override
    public BaseViewHolder inflateLayout(ViewDataBinding view) {
        return new DefaultersTopHolder(view);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.row_customer_name;
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

    class DefaultersTopHolder extends BaseViewHolder<RowCustomerNameBinding> {
        DefaultersTopHolder(@NonNull ViewDataBinding itemBinding) {
            super(itemBinding);
        }
        @Override
        public void onBind(int position) {
            binding.customerName.setText("" + Leads.get(position));
        }

    }
}
