package com.apnagodam.staff.adapter;

import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.R;
import com.apnagodam.staff.helper.LocaleHelper;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {
    List<Pair<String, String>> languageList = LocaleHelper.getLanguageList();

    int[] picArray = {R.drawable.english, R.drawable.hindi};

    String[] colorArray = {
            "#145FF4", "#E52027"
    };

    BaseActivity baseActivity;

    String currentSelected;

    public LanguageAdapter(BaseActivity activity) {
        this.baseActivity = activity;
        this.currentSelected = LocaleHelper.getLanguage(activity);
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LanguageViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_language, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        String[] language = languageList.get(position).second.split(" ");


        String lang;
        if (position != 0)
            lang = language[0] + " " + language[1];
        else
            lang = language[0];

        holder.card.setCardBackgroundColor(Color.parseColor(colorArray[position]));

        holder.iv.setImageResource(picArray[position]);

        holder.tvLanguage.setText(lang);
        if (currentSelected.equals(languageList.get(position).first)) {
            holder.tvLanguage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_success, 0);
        } else {
            holder.tvLanguage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSelected = languageList.get(position).first;
                if (position==0){
                    ((LanguageChangeCallback) baseActivity).onLanguageChange("en");
                }else {
                    ((LanguageChangeCallback) baseActivity).onLanguageChange("hi");
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    public interface LanguageChangeCallback {
        void onLanguageChange(String selected);
    }

    public class LanguageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLanguage;
        private CardView card;
        private ImageView iv;

        public LanguageViewHolder(View itemView) {
            super(itemView);
            tvLanguage = itemView.findViewById(R.id.tv_language);
            card = itemView.findViewById(R.id.card);
            iv = itemView.findViewById(R.id.iv);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentSelected = languageList.get(getAdapterPosition()).first;
                    ((LanguageChangeCallback) baseActivity).onLanguageChange(languageList.get(getAdapterPosition()).first);
                    notifyDataSetChanged();
                }
            });*/
        }
    }


}
