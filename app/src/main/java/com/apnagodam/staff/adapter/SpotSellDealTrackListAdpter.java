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
import com.apnagodam.staff.activity.SpotDealTrackListActivity;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.CommudityResponse;
import com.apnagodam.staff.module.SpotSellDealTrackPojo;
import com.apnagodam.staff.module.UserDetails;

import java.util.List;

/**
 * Created by Raju Singh on 01/08/2020.
 */

public class SpotSellDealTrackListAdpter extends RecyclerView.Adapter<SpotSellDealTrackListAdpter.CommudityResponseViewHolder> {
    private List<SpotSellDealTrackPojo.Datum> commudityResponseList;
    private Context context;
    private OnItemClickListener listener;
    private ImageView productImage;
    // for commudity image  callback
    private CallbackInterface mCallback;
    private String profileImgUrl, productUrl;
    private BaseActivity activity;
    private String idds = "";

    public interface CallbackInterface {

        /**
         * Callback invoked when clicked
         *
         * @param position  - the position
         * @param Imageview - the text to pass back
         */
        void onHandleSelection(int position, ImageView Imageview);
    }

    public SpotSellDealTrackListAdpter(String commudityID, List<SpotSellDealTrackPojo.Datum> body, SpotDealTrackListActivity myCommudityListActivity, BaseActivity activity) {
        this.idds = commudityID;
        this.commudityResponseList = body;
        this.context = myCommudityListActivity;
        this.activity = activity;
    }

    @Override
    public CommudityResponseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View CommudityResponseProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.commudity_sell, parent, false);
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
        if (commudityResponseList.get(position).getCategory() != null) {
            holder.tv_gatepass.setText("" + commudityResponseList.get(position).getCategory());
        } else {
            holder.tv_gatepass.setText("0.0");
        }
        if (commudityResponseList.get(position).getQuantity() != null) {
            holder.tv_name.setText(" " + commudityResponseList.get(position).getQuantity());
        } else {
            holder.tv_name.setText("0.0");
        }
        holder.tv_gatepass.setTextColor(Color.BLACK);
        holder.tv_name.setTextColor(Color.BLACK);
        activity.hideDialog();
        UserDetails userDetails = SharedPreferencesRepository.getDataManagerInstance().getUser();
        if (commudityResponseList.get(position).getStatus().equalsIgnoreCase("2")) {
        } else if (commudityResponseList.get(position).getStatus().equalsIgnoreCase("0")) {
        } else {
            holder.tv_action.setText(context.getResources().getString(R.string.contract_form));
        }

        holder.tv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof SpotDealTrackListActivity) {
                    ((SpotDealTrackListActivity) context).wantToSell(position);
                }
                /*if (context instanceof SpotDealTrackListActivity) {
                    if (commudityResponseList.get(position).getStatus().equalsIgnoreCase("2")) {
                        if ((userDetails.getUserId().equalsIgnoreCase(commudityResponseList.get(position).getSellerId()))) {
                            if (commudityResponseList.get(position).getFinalPrice().equalsIgnoreCase("0")) {
                                // update Quantity
                                ((SpotDealTrackListActivity) context).wantToUpdatePrice(position);
                            }
                        } else {
                            if (commudityResponseList.get(position).getFinalPrice().equalsIgnoreCase("0")) {

                            } else {
                                //for final approve
                                ((SpotDealTrackListActivity) context).wantToSell(position);
                            }
                        }
                    }
                }*/
            }
        });

        holder.view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof SpotDealTrackListActivity) {
                    ((SpotDealTrackListActivity) context).ViewData(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commudityResponseList.size();
    }

    public class CommudityResponseViewHolder extends RecyclerView.ViewHolder {
        TextView tv_gatepass, tv_apply, tv_name, tv_loan_done, txt_mybids, tv_action;
        LinearLayout cell_container;
        ImageView view1;
        View view12;

        public CommudityResponseViewHolder(View view) {
            super(view);
            tv_gatepass = view.findViewById(R.id.tv_gatepass);
            tv_name = view.findViewById(R.id.tv_name);
            view1 = view.findViewById(R.id.view);
            view12 = view.findViewById(R.id.view12);
            cell_container = view.findViewById(R.id.cell_container);
            tv_action = view.findViewById(R.id.tv_action);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(List<CommudityResponse> commudityResponse);
    }
}