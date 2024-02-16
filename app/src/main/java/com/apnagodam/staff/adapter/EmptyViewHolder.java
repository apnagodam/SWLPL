package com.apnagodam.staff.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.apnagodam.staff.Base.BaseViewHolder;
import com.apnagodam.staff.databinding.LayoutEmptyBinding;


public class EmptyViewHolder extends BaseViewHolder<LayoutEmptyBinding> {
    public EmptyViewHolder(@NonNull ViewDataBinding itemBinding) {
        super(itemBinding);
    }

    @Override
    public void onBind(int position) {

    }
}
