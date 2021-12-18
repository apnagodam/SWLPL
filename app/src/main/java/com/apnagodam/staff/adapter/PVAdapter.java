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
import com.apnagodam.staff.module.PVModel;
import com.apnagodam.staff.utils.Utility;

import java.util.ArrayList;

/**
 * Created by User on 31-07-2020.
 */

public class PVAdapter extends RecyclerView.Adapter<PVAdapter.HeaderViewHolder> {
    private Context context;
    private ArrayList<PVModel> getList;
    private OnPVCalculationListener onPVCalculationListener;

    public ArrayList<PVModel> getGetList() {
        return getList;
    }

    public PVAdapter(ArrayList<PVModel> getList, OnPVCalculationListener onPVCalculationListener, PVUploadClass pvUploadClass) {
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
        // double TotalValue = ((getList.get(position).getDhang() + getList.get(position).getDanda()) * getList.get(position).getHeight()) + getList.get(position).getPlusminus();
        //holder.total.setText(String.format("%s", getList.get(position).getTotalPV()));
    }


    @Override
    public int getItemCount() {
        return getList.size();
    }


    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private EditText dhang, danda, height, plus_minus,remark;
        private TextView serialNumberText, imgRemove, total;

        public HeaderViewHolder(View view, OnPVCalculationListener onPVCalculationListener, ArrayList<PVModel> getList) {
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

            /*dhang.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                      //  onPVCalculationListener.onTextWeight(getAdapterPosition(), Utility.getTextDouble(dhang), getList.get(getAdapterPosition()).getDanda(), getList.get(getAdapterPosition()).getHeight(), getList.get(getAdapterPosition()).getPlusminus());

                    calculateTotalPV(getAdapterPosition(),Utility.getTextDouble(dhang),Utility.getTextDouble(danda),Utility.getTextDouble(height),Utility.getTextDouble(plus_minus));
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            danda.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                  *//*  if (!charSequence.equals("") && !danda.getText().toString().equals("") && !danda.getText().toString().equals(".")) {
                        dhandaValue=0.0;
                        dhandaValue = Double.parseDouble(danda.getText().toString());
                        onPVCalculationListener.onTextWeight(getAdapterPosition(), String.valueOf(dhangValue),String.valueOf(dhandaValue),String.valueOf(heightValue),String.valueOf(plusminusValue));
                        calculateTotalPV();
                    }else {
                        dhandaValue=0.0;
                        calculateTotalPV();
                    }*//*

                   // onPVCalculationListener.onTextWeight(getAdapterPosition(), getList.get(getAdapterPosition()).getDhang(), Utility.getTextDouble(danda), getList.get(getAdapterPosition()).getHeight(), getList.get(getAdapterPosition()).getPlusminus());
                    calculateTotalPV(getAdapterPosition(),Utility.getTextDouble(dhang),Utility.getTextDouble(danda),Utility.getTextDouble(height),Utility.getTextDouble(plus_minus));

                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            height.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                   *//* if (!charSequence.equals("") && !height.getText().toString().equals("") && !height.getText().toString().equals(".")) {
                        heightValue=0.0;
                         heightValue = Double.parseDouble(height.getText().toString());
                        onPVCalculationListener.onTextWeight(getAdapterPosition(), String.valueOf(dhangValue),String.valueOf(dhandaValue),String.valueOf(heightValue),String.valueOf(plusminusValue));
                        calculateTotalPV();
                    }else {
                        heightValue=0.0;
                        calculateTotalPV();
                    }*//*

                  //  onPVCalculationListener.onTextWeight(getAdapterPosition(), getList.get(getAdapterPosition()).getDhang(), getList.get(getAdapterPosition()).getDanda(), Utility.getTextDouble(height), getList.get(getAdapterPosition()).getPlusminus());
                    calculateTotalPV(getAdapterPosition(),Utility.getTextDouble(dhang),Utility.getTextDouble(danda),Utility.getTextDouble(height),Utility.getTextDouble(plus_minus));

                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            plus_minus.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                   *//* if (!charSequence.equals("") && !plus_minus.getText().toString().equals("") && !plus_minus.getText().toString().equals(".")) {
                        plusminusValue=0.0;
                        plusminusValue = Double.parseDouble(plus_minus.getText().toString());
                        onPVCalculationListener.onTextWeight(getAdapterPosition(), String.valueOf(dhangValue),String.valueOf(dhandaValue),String.valueOf(heightValue),String.valueOf(plusminusValue));
                        calculateTotalPV();
                    }else {
                        plusminusValue=0.0;
                        calculateTotalPV();
                    }*//*

                    //onPVCalculationListener.onTextWeight(getAdapterPosition(), getList.get(getAdapterPosition()).getDhang(), getList.get(getAdapterPosition()).getDanda(), getList.get(getAdapterPosition()).getHeight(), Utility.getTextDouble(plus_minus));
                    calculateTotalPV(getAdapterPosition(),Utility.getTextDouble(dhang),Utility.getTextDouble(danda),Utility.getTextDouble(height),Utility.getTextDouble(plus_minus));

                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });*/
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
                // pvAdapter.notifyItemChanged(position);
            } catch (Exception e) {
                Log.e("Exception", e.toString());
            }


            /*  formula we use   total  = [{dhang+dhanda}*helight]+plusminus*/
            //double TotalValue = ((getList.get(poss).getDhang() + getList.get(poss).getDanda()) * getList.get(poss).getHeight()) + getList.get(poss).getPlusminus();

        }
    }
}
