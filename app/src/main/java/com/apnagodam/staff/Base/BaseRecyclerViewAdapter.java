package com.apnagodam.staff.Base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.apnagodam.staff.ApnaGodamApp;
import com.apnagodam.staff.R;
import com.apnagodam.staff.adapter.EmptyViewHolder;


import java.util.Collection;

public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {


    private boolean isEmpty = false;

    public abstract BaseViewHolder inflateLayout(ViewDataBinding view);

    public abstract int getLayoutId(int viewType);

    public abstract Collection getCollection();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isEmpty) {
            return new EmptyViewHolder(DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.layout_empty, parent, false
            ));
        } else {
            ViewDataBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    getLayoutId(viewType), parent, false
            );
            return inflateLayout(binding);
        }

    }

    public int getColor(@ColorRes int color) {
        return ApnaGodamApp.getApp().getResources().getColor(color);
    }

    @Override
    public int getItemCount() {
        if (getCollection() == null) {
            return 0;
        }
        if (getCollection() == null || getCollection().size() == 0) {
            isEmpty = true;
            return 1;
        } else {
            isEmpty = false;
            return getCollection().size();
        }
    }

}
