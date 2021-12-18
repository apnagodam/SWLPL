package com.apnagodam.staff.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.pv.OnPVCalculationListener;
import com.apnagodam.staff.activity.in.pv.PVUploadClass;
import com.apnagodam.staff.activity.out.pv.OUTOnPVCalculationListener;
import com.apnagodam.staff.activity.out.pv.OUTPVUploadClass;
import com.apnagodam.staff.module.PVModel;
import com.apnagodam.staff.utils.Utility;

import java.util.ArrayList;

/**
 * Created by User on 31-07-2020.
 */

public class OUTPVAdapter extends RecyclerView.Adapter<OUTPVAdapter.HeaderViewHolder> {
    private Context context;
    private ArrayList<PVModel> getList;
    private OUTOnPVCalculationListener onPVCalculationListener;

    public ArrayList<PVModel> getGetList() {
        return getList;
    }

    public OUTPVAdapter(ArrayList<PVModel> getList, OUTOnPVCalculationListener onPVCalculationListener, OUTPVUploadClass pvUploadClass) {
        this.getList = getList;
        this.onPVCalculationListener = onPVCalculationListener;
        this.context = pvUploadClass;
    }

    public void addNewItem() {
        PVModel weightModel = new PVModel();
        if (!getList.isEmpty()) {
            weightModel.setsNumber(getList.get(getList.size() - 1).getsNumber() + 1);
        } else {
            weightModel.setsNumber(1);
        }
        weightModel.setDanda(0.00);
        weightModel.setDhang(0.00);
        weightModel.setHeight(0.00);
        weightModel.setPlusminus(0.00);
        weightModel.setPlusminus(0.00);
        getList.add(weightModel);
        notifyDataSetChanged();
    }

    @Override
    public HeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pv_itmes, parent, false), onPVCalculationListener, getList);
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderViewHolder holder, int position) {
        //int serialNumber = position + 1;
        holder.serialNumberText.setText("Block:-" + "  " + "" + getList.get(position).getsNumber());

        if (getList.get(position).getDhang() > 0) {
            holder.dhang.setText("" + getList.get(position).getDhang());
        } else {
            holder.dhang.getText().clear();
        }
        if (getList.get(position).getDanda() > 0) {
            holder.danda.setText("" + getList.get(position).getDanda());
        } else {
            holder.danda.getText().clear();
        }
        if (getList.get(position).getHeight() > 0) {
            holder.height.setText("" + getList.get(position).getHeight());
        } else {
            holder.height.getText().clear();
        }
        if (getList.get(position).getPlusminus() > 0) {
            holder.plus_minus.setText("" + getList.get(position).getPlusminus());
        } else {
            holder.plus_minus.getText().clear();
        }
        if (getList.get(position).getTotalPV() > 0) {
            holder.total.setText("" + getList.get(position).getTotalPV());
        } else {
            holder.total.setText("");
        }
        if (getList.size() != 1) {
            if ((getList.size() - 1) == position) {
                holder.imgRemove.setVisibility(View.VISIBLE);
            } else {
                holder.imgRemove.setVisibility(View.VISIBLE);
            }
        } else {
            holder.imgRemove.setVisibility(View.VISIBLE);
        }

        holder.imgRemove.setOnClickListener(view -> Utility.showDecisionDialoerror(context, context.getString(R.string.alert), "Are you sure you want to remove this PV Block", new Utility.AlertCallback() {
            @Override
            public void callback() {
                getList.remove(position);
                notifyDataSetChanged();

            }
        }));
    }


    @Override
    public int getItemCount() {
        return getList.size();
    }


    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private EditText dhang, danda, height, plus_minus,remark;
        private TextView serialNumberText, imgRemove, total;

        public HeaderViewHolder(View view, OUTOnPVCalculationListener onPVCalculationListener, ArrayList<PVModel> getList) {
            super(view);
            serialNumberText = view.findViewById(R.id.block_serial);
            dhang = view.findViewById(R.id.dhang);
            danda = view.findViewById(R.id.danda);
            height = view.findViewById(R.id.height);
            plus_minus = view.findViewById(R.id.plus_minus);
            total = view.findViewById(R.id.total);
            imgRemove = view.findViewById(R.id.delate_pv_sheet);
            remark = view.findViewById(R.id.remark);

            //  imgAdd = view.findViewById(R.id.imgAdd);
           /* imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAddBegsListener.onAdd(getAdapterPosition());
                }
            });*/



            danda.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    calculateTotalPV(getAdapterPosition(), Utility.getTextDouble(dhang), Utility.getTextDouble(danda), Utility.getTextDouble(height), Utility.getTextDouble(plus_minus),""+Utility.getTextRemark(remark));

                }
            });
            dhang.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    calculateTotalPV(getAdapterPosition(), Utility.getTextDouble(dhang), Utility.getTextDouble(danda), Utility.getTextDouble(height), Utility.getTextDouble(plus_minus),""+Utility.getTextRemark(remark));

                }
            });
            height.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    calculateTotalPV(getAdapterPosition(), Utility.getTextDouble(dhang), Utility.getTextDouble(danda), Utility.getTextDouble(height), Utility.getTextDouble(plus_minus),""+Utility.getTextRemark(remark));

                }
            });
            plus_minus.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    calculateTotalPV(getAdapterPosition(), Utility.getTextDouble(dhang), Utility.getTextDouble(danda), Utility.getTextDouble(height), Utility.getTextDouble(plus_minus),""+Utility.getTextRemark(remark));

                }
            });
            remark.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    calculateTotalPV(getAdapterPosition(), Utility.getTextDouble(dhang), Utility.getTextDouble(danda), Utility.getTextDouble(height), Utility.getTextDouble(plus_minus),""+Utility.getTextRemark(remark));
                }
            });
        }

        private void calculateTotalPV(int poss, double dhang, double danda, double height, double plusminus, String remark) {


            try {

                getList.get(poss).setDhang(dhang);
                getList.get(poss).setDanda(danda);
                getList.get(poss).setHeight(height);
                getList.get(poss).setPlusminus(plusminus);
                getList.get(poss).setRemark(remark);
                double TotalValue = ((dhang + danda) * height) + plusminus;
                getList.get(poss).setTotalPV((TotalValue));
                Log.e("listdata", getList.toString());
                total.setText(String.format("%s", TotalValue));
            } catch (Exception e) {
                Log.e("Exception", e.toString());
            }

        }
    }
}
