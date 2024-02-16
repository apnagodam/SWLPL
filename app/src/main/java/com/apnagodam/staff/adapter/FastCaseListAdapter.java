package com.apnagodam.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Base.BaseRecyclerViewAdapter;
import com.apnagodam.staff.Base.BaseViewHolder;
import com.apnagodam.staff.Network.Response.ResponseFastcaseList;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.truckbook.TruckBookListingActivity;
import com.apnagodam.staff.databinding.ItemFastcaseListBinding;
import com.apnagodam.staff.databinding.PricingDataBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.AllTruckBookListResponse;

import java.util.Collection;
import java.util.List;


public class FastCaseListAdapter extends BaseRecyclerViewAdapter {

    private List<ResponseFastcaseList.Data> data;
    private Context context;
    private BaseActivity activity;

    public FastCaseListAdapter(List<ResponseFastcaseList.Data> data, Context context) {
        this.data = data;
        this.context = context;
        //    this.activity = activity;
    }

    @Override
    public BaseViewHolder inflateLayout(ViewDataBinding view) {
        return new DefaultersTopHolder(view);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_fastcase_list;
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
        return data.size();
    }

    class DefaultersTopHolder extends BaseViewHolder<ItemFastcaseListBinding> {

        DefaultersTopHolder(@NonNull ViewDataBinding itemBinding) {
            super(itemBinding);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBind(int position) {

//            if (position % 2 == 0) {
//                binding.getRoot().setBackgroundColor(Color.parseColor("#EBEBEB"));
//            } else {
//                binding.getRoot().setBackgroundColor(Color.WHITE);
//            }

            binding.txtCaseId.setText("Fast Case Id : " + data.get(position).getFast_case_id());
            binding.txtCustomerName.setText("Customer Name : " + data.get(position).getCustomer_name());
            binding.txtTerminal.setText("Terminal : " + data.get(position).getTerminal_name());
            binding.txtCommodity.setText("Commodity : " + data.get(position).getCommodity());
            binding.txtVehicleNo.setText("Vehicle No. : " + data.get(position).getVichel_number());
            binding.txtStackNo.setText("Stack No. : " + data.get(position).getStack_no());

            if (data.get(position).getToken_number().equals("null"))
                binding.txtTokenNo.setText("Token No. : - ");
            else
                binding.txtTokenNo.setText("Token No. : " + data.get(position).getToken_number());

        }
    }

}
