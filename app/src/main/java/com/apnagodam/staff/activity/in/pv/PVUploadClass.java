package com.apnagodam.staff.activity.in.pv;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.NetworkCallbackWProgress;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.adapter.PVAdapter;
import com.apnagodam.staff.databinding.PvInBinding;
import com.apnagodam.staff.module.GatepassDetailsPVPojo;
import com.apnagodam.staff.module.PVGatepassPojo;
import com.apnagodam.staff.module.PVModel;
import com.apnagodam.staff.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PVUploadClass extends BaseActivity<PvInBinding> implements OnPVCalculationListener {
    private ArrayList<PVModel> getList;
    private PVAdapter pvAdapter;

    ArrayAdapter<String> SpinnergatepassAdapter;
    String gatepass_no = null;
    List<String> GatepassName;
    String CaseID = "", terminalID = "", commodityID = "", stackNo = "", userID = "";
    private List<PVGatepassPojo.Datum> GatepassData;

    @Override
    protected int getLayoutResId() {
        return R.layout.pv_in;
    }

    @Override
    protected void setUp() {
        GatepassName = new ArrayList<>();
        getList = new ArrayList<PVModel>();
        GatepassName.add("Select Gate Pass");
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.pvlisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(PVListingActivityClass.class);
            }
        });
        binding.adPvSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onAdd(0);
                pvAdapter.addNewItem();
            }
        });
        binding.priviousLoanStatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (pvAdapter != null && pvAdapter.getGetList() != null && !pvAdapter.getGetList().isEmpty()) {
                        JSONObject no = new JSONObject();
                        JSONObject dg = new JSONObject();
                        JSONObject da = new JSONObject();
                        JSONObject h = new JSONObject();
                        JSONObject pm = new JSONObject();
                        JSONObject to = new JSONObject();
                        JSONObject re = new JSONObject();
                        for (int i = 0; i < pvAdapter.getGetList().size(); i++) {
                            int num = i + 1;
                            PVModel pvModel = pvAdapter.getGetList().get(i);
                            no.put(String.valueOf(num), pvModel.getsNumber() + "");
                            dg.put(String.valueOf(num), pvModel.getDhang() + "");
                            da.put(String.valueOf(num), pvModel.getDanda() + "");
                            h.put(String.valueOf(num), pvModel.getHeight() + "");
                            pm.put(String.valueOf(num), pvModel.getPlusminus() + "");
                            //   to.put(String.valueOf(num), pvModel.getTotalPV() + "");
                            re.put(String.valueOf(num), pvModel.getRemark() + "");
                        }
                        apiService.createPv(commodityID, CaseID, userID, stackNo, terminalID, no, dg, da, h, pm, re).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                            @Override
                            protected void onSuccess(LoginResponse body) {
                                Utility.showAlertDialog(PVUploadClass.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                                    @Override
                                    public void callback() {
                                        startActivityAndClear(PVListingActivityClass.class);
                                    }
                                });
                            }
                        });
                    }
                    //Log.e("JsonData==> ",createJson().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    /*    binding.priviousLoanStatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject noObj = new JSONObject();
                    JSONObject dgObj = new JSONObject();
                    JSONObject daObj = new JSONObject();
                    JSONObject hObj = new JSONObject();
                    JSONObject pmObj = new JSONObject();
                    JSONObject reObj = new JSONObject();
                    if (pvAdapter != null && pvAdapter.getGetList() != null && !pvAdapter.getGetList().isEmpty()) {
                        JSONArray no = new JSONArray();
                        JSONArray dg = new JSONArray();
                        JSONArray da = new JSONArray();
                        JSONArray h = new JSONArray();
                        JSONArray pm = new JSONArray();
                        JSONArray to = new JSONArray();
                        JSONArray re = new JSONArray();
                        for (int i = 0; i < pvAdapter.getGetList().size(); i++) {
                            int num = i + 1;
                            PVModel pvModel = pvAdapter.getGetList().get(i);
                            noObj.put(String.valueOf(num), pvModel.getsNumber());
                            no.put(noObj);


                            dgObj.put(String.valueOf(num), pvModel.getDhang());
                            dg.put(dgObj);


                            daObj.put(String.valueOf(num), pvModel.getDanda());
                            da.put(daObj);


                            hObj.put(String.valueOf(num), pvModel.getHeight());
                            h.put(hObj);


                            pmObj.put(String.valueOf(num), pvModel.getPlusminus());
                            pm.put(pmObj);

                            JSONObject toObj = new JSONObject();
                            toObj.put(String.valueOf(num), pvModel.getTotalPV());
                            to.put(toObj);


                            reObj.put(String.valueOf(num), pvModel.getRemark());
                            re.put(reObj);
                        }
                       *//* pvPojo.setData(noObj);
                        pvPojo.setDhang(dgObj);
                        pvPojo.setDanda(daObj);
                        pvPojo.setHeight(hObj);
                        pvPojo.setPlus_minus(pmObj);
                        pvPojo.setRemark(reObj);
                        jsonObject.put("block_no", noObj);
                        jsonObject.put("dhang", dgObj);
                        jsonObject.put("danda", daObj);
                        jsonObject.put("height", hObj);
                        jsonObject.put("plus_minus", pmObj);
                        //  jsonObject.put("total", to);
                        jsonObject.put("remark", reObj);*//*
                    }
                   // Log.e("JsonData==> ", createJsonOBject().toString());
                    apiService.uploadPVStocksIN(new PVPojo(CaseID,terminalID,commodityID,userID,stackNo,noObj,dgObj,daObj,hObj,pmObj,reObj)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                        @Override
                        protected void onSuccess(LoginResponse body) {
                            Toast.makeText(getApplicationContext(), body.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                 *//*   apiService.uploadPVStocksIN(createJson()).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                        @Override
                        protected void onSuccess(LoginResponse body) {
                            Toast.makeText(getApplicationContext(), body.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });*//*
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        setUpAdapter();
        try {
            setValueOnSpinner();
        } catch (Exception e) {
            e.getStackTrace();
        }
        apiService.getallGatepass("IN").enqueue(new NetworkCallbackWProgress<PVGatepassPojo>(getActivity()) {
            @Override
            protected void onSuccess(PVGatepassPojo body) {
                try {
                    GatepassData = body.getData();
                    for (int i = 0; i < body.getData().size(); i++) {
                        GatepassName.add(body.getData().get(i).getGatePass());
                    }
                    SpinnergatepassAdapter = new ArrayAdapter<String>(PVUploadClass.this, R.layout.multiline_spinner_item_pv, GatepassName) {
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);
                            /*   ((TextView) v).setTextColor(Color.parseColor("#000000"));*/
                            return v;
                        }

                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View v = super.getDropDownView(position, convertView, parent);
                           /* v.setBackgroundColor(Color.parseColor("#05000000"));
                            ((TextView) v).setTextColor(Color.parseColor("#000000"));*/
                            return v;
                        }
                    };
                    SpinnergatepassAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                    binding.spinnerGatepass.setAdapter(SpinnergatepassAdapter);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        });
    }

    private void setValueOnSpinner() {
        SpinnergatepassAdapter = new ArrayAdapter<String>(PVUploadClass.this, R.layout.multiline_spinner_item_pv, GatepassName) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                //  ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
               /* v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));*/
                return v;
            }
        };
        SpinnergatepassAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        binding.spinnerGatepass.setAdapter(SpinnergatepassAdapter);
        binding.spinnerGatepass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // selected item in the list
                if (position != 0) {
                    try {
                        gatepass_no = parentView.getItemAtPosition(position).toString();
                        CaseID = GatepassData.get(position - 1).getCaseId();
                        binding.stackNo.setVisibility(View.VISIBLE);
                        binding.secoundtb.setVisibility(View.VISIBLE);
                        apiService.getGatepassDetails(CaseID).enqueue(new NetworkCallbackWProgress<GatepassDetailsPVPojo>(getActivity()) {
                            @Override
                            protected void onSuccess(GatepassDetailsPVPojo body) {
                                try {
                                    terminalID = "" + body.getData().getTerminalId();
                                    userID = "" + body.getData().getCustomerUid();
                                    commodityID = "" + body.getData().getCommodityId();
                                    stackNo = "" + body.getData().getStackNumber();
                                    binding.stackNo.setText("Stack No:- " + " " + body.getData().getStackNumber());
                                    binding.terminalName.setText("Terminal Name:- " + " " + body.getData().getTermianlName());
                                    binding.userName.setText("UserName:- " + " " + body.getData().getFname());
                                    binding.commodityName.setText("Commodity :- " + " " + body.getData().getCategory() + "(" + body.getData().getCommodityType() + ")");
                                } catch (Exception e) {
                                    e.getStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    binding.stackNo.setVisibility(View.GONE);
                    binding.secoundtb.setVisibility(View.GONE);
                    gatepass_no = null;
                }
                // SpinnerUserListAdapter.notifyDataSetChanged();
            }

            //
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
            }
        });
    }

    @Override
    public void onAdd(int position) {
        PVModel weightModel = new PVModel();
        if (!getList.isEmpty()) {
            weightModel.setsNumber(getList.get(getList.size() - 1).getsNumber() + 1);
        } else {
            weightModel.setsNumber(1);
        }
        weightModel.setDhang(0.0);
        weightModel.setDanda(0.0);
        weightModel.setHeight(0.0);
        weightModel.setPlusminus(0.0);
        weightModel.setRemark("N/A");
        getList.add(weightModel);
        pvAdapter.notifyDataSetChanged();
    }

    private void setUpAdapter() {
        binding.fieldStockList.setLayoutManager(new LinearLayoutManager(this));
        binding.fieldStockList.setNestedScrollingEnabled(false);
        pvAdapter = new PVAdapter(getList, this, PVUploadClass.this);
        binding.fieldStockList.setAdapter(pvAdapter);
    }

    @Override
    public void onRemove(int position) {
        getList.remove(position);
        pvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTextWeight(int position, double dhang, double danda, double height, double plusminus, String remark) {
        try {

            getList.get(position).setDhang(dhang);
            getList.get(position).setDanda(danda);
            getList.get(position).setHeight(height);
            getList.get(position).setPlusminus(plusminus);
            getList.get(position).setRemark(remark);
            Double TotalValue = ((dhang + danda) * height) + plusminus;
            getList.get(position).setTotalPV((TotalValue));
            Log.e("listdata", getList.toString());
            // pvAdapter.notifyItemChanged(position);
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

    }

   /* private JSONObject createJson() throws JSONException {
        PVPojo pvPojo = new PVPojo();
        pvPojo.setCase_ids(CaseID);
        pvPojo.setTerminal_id(terminalID);
        pvPojo.setCommodities_id(commodityID);
        pvPojo.setUser_id(userID);
        pvPojo.setStack_no(stackNo);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("case_ids", CaseID);
        jsonObject.put("terminal_id", terminalID);
        jsonObject.put("commodities_id", commodityID);
        jsonObject.put("user_id", userID);
        jsonObject.put("stack_no", stackNo);

        if (pvAdapter != null && pvAdapter.getGetList() != null && !pvAdapter.getGetList().isEmpty()) {
            JSONArray no = new JSONArray();
            JSONArray dg = new JSONArray();
            JSONArray da = new JSONArray();
            JSONArray h = new JSONArray();
            JSONArray pm = new JSONArray();
            JSONArray to = new JSONArray();
            JSONArray re = new JSONArray();
            JSONObject noObj = new JSONObject();
            JSONObject dgObj = new JSONObject();
            JSONObject daObj = new JSONObject();
            JSONObject hObj = new JSONObject();
            JSONObject pmObj = new JSONObject();
            JSONObject reObj = new JSONObject();
            for (int i = 0; i < pvAdapter.getGetList().size(); i++) {
                int num = i + 1;
                PVModel pvModel = pvAdapter.getGetList().get(i);
                noObj.put(String.valueOf(num), pvModel.getsNumber());
                no.put(noObj);


                dgObj.put(String.valueOf(num), pvModel.getDhang());
                dg.put(dgObj);


                daObj.put(String.valueOf(num), pvModel.getDanda());
                da.put(daObj);


                hObj.put(String.valueOf(num), pvModel.getHeight());
                h.put(hObj);


                pmObj.put(String.valueOf(num), pvModel.getPlusminus());
                pm.put(pmObj);

                JSONObject toObj = new JSONObject();
                toObj.put(String.valueOf(num), pvModel.getTotalPV());
                to.put(toObj);


                reObj.put(String.valueOf(num), pvModel.getRemark());
                re.put(reObj);
            }
            pvPojo.setData(noObj);
            pvPojo.setDhang(dgObj);
            pvPojo.setDanda(daObj);
            pvPojo.setHeight(hObj);
            pvPojo.setPlus_minus(pmObj);
            pvPojo.setRemark(reObj);
            jsonObject.put("block_no", noObj);
            jsonObject.put("dhang", dgObj);
            jsonObject.put("danda", daObj);
            jsonObject.put("height", hObj);
            jsonObject.put("plus_minus", pmObj);
            //  jsonObject.put("total", to);
            jsonObject.put("remark", reObj);
        }
        return jsonObject;
    }

    private PVPojo createJsonOBject() throws JSONException {
        PVPojo pvPojo = new PVPojo();
        pvPojo.setCase_ids(CaseID);
        pvPojo.setTerminal_id(terminalID);
        pvPojo.setCommodities_id(commodityID);
        pvPojo.setUser_id(userID);
        pvPojo.setStack_no(stackNo);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("case_ids", CaseID);
        jsonObject.put("terminal_id", terminalID);
        jsonObject.put("commodities_id", commodityID);
        jsonObject.put("user_id", userID);
        jsonObject.put("stack_no", stackNo);

        if (pvAdapter != null && pvAdapter.getGetList() != null && !pvAdapter.getGetList().isEmpty()) {
            JSONArray no = new JSONArray();
            JSONArray dg = new JSONArray();
            JSONArray da = new JSONArray();
            JSONArray h = new JSONArray();
            JSONArray pm = new JSONArray();
            JSONArray to = new JSONArray();
            JSONArray re = new JSONArray();
            JSONObject noObj = new JSONObject();
            JSONObject dgObj = new JSONObject();
            JSONObject daObj = new JSONObject();
            JSONObject hObj = new JSONObject();
            JSONObject pmObj = new JSONObject();
            JSONObject reObj = new JSONObject();
            for (int i = 0; i < pvAdapter.getGetList().size(); i++) {
                int num = i + 1;
                PVModel pvModel = pvAdapter.getGetList().get(i);
                noObj.put(String.valueOf(num), pvModel.getsNumber());
                no.put(noObj);


                dgObj.put(String.valueOf(num), pvModel.getDhang());
                dg.put(dgObj);


                daObj.put(String.valueOf(num), pvModel.getDanda());
                da.put(daObj);


                hObj.put(String.valueOf(num), pvModel.getHeight());
                h.put(hObj);


                pmObj.put(String.valueOf(num), pvModel.getPlusminus());
                pm.put(pmObj);

                JSONObject toObj = new JSONObject();
                toObj.put(String.valueOf(num), pvModel.getTotalPV());
                to.put(toObj);


                reObj.put(String.valueOf(num), pvModel.getRemark());
                re.put(reObj);
            }
            pvPojo.setData(noObj);
            pvPojo.setDhang(dgObj);
            pvPojo.setDanda(daObj);
            pvPojo.setHeight(hObj);
            pvPojo.setPlus_minus(pmObj);
            pvPojo.setRemark(reObj);
            jsonObject.put("block_no", noObj);
            jsonObject.put("dhang", dgObj);
            jsonObject.put("danda", daObj);
            jsonObject.put("height", hObj);
            jsonObject.put("plus_minus", pmObj);
            //  jsonObject.put("total", to);
            jsonObject.put("remark", reObj);
        }
        return pvPojo;
    }*/
}
