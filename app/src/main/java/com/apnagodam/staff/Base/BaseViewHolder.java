package com.apnagodam.staff.Base;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    protected T binding;

    public BaseViewHolder(@NonNull ViewDataBinding itemBinding) {
        super(itemBinding.getRoot());
        this.binding = (T) itemBinding;
        itemBinding.executePendingBindings();
        itemView.setOnClickListener(this);
    }


    public abstract void onBind(int position);


    protected void onItemClick(int position) {
    }

    protected void onViewClick(View view, int position) {
    }

    public void onTextChange(TextView editText, int position, String text) {
    }

    protected void onCheckChange(CompoundButton button, int position, boolean isChecked) {
    }

    public void registerForTextChange(TextView textView) {
        textView.addTextChangedListener(new MyTextWatcher(textView));
    }

    @Override
    public void onClick(View v) {
        if (v == itemView) {
            onItemClick(getAdapterPosition());
        } else {
            onViewClick(v, getAdapterPosition());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        onCheckChange(buttonView, getAdapterPosition(), isChecked);
    }

    class MyTextWatcher implements TextWatcher {

        TextView textView;

        public MyTextWatcher(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onTextChange(textView, getAdapterPosition(), s != null ? s.toString() : "");
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
