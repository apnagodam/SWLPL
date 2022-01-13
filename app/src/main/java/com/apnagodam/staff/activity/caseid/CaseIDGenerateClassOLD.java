package com.apnagodam.staff.activity.caseid;

import android.app.Dialog;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.NetworkCallbackWProgress;
import com.apnagodam.staff.Network.Request.CreateCaseIDPostData;
import com.apnagodam.staff.Network.Request.StackPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.adapter.CustomerNameAdapter;
import com.apnagodam.staff.databinding.ActivityCaseIdBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.AllUserListPojo;
import com.apnagodam.staff.module.CommudityResponse;
import com.apnagodam.staff.module.OUTComodityPojo;
import com.apnagodam.staff.module.StackListPojo;
import com.apnagodam.staff.module.TerminalListPojo;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class CaseIDGenerateClassOLD extends BaseActivity<ActivityCaseIdBinding> implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    ArrayAdapter<String> SpinnerStackAdapter, SpinnerCommudityAdapter, spinnerTeerminalAdpter, SpinnerUserListAdapter, spinnerEmployeeAdpter;
    String stackID = null, commudityID = null, TerminalID = null, selectPurpose = null,
            selectInOUt = null, seleectCoustomer = null, selectConvertOther = null;
    String UserUniqueID;
    String invWeightRequestWeight = null;
    String UserID = null;
    List<String> StackName;
    List<String> StackID;
    List<String> CommudityName;
    List<String> TerminalName;
    List<String> TerminalsID;
    List<String> CustomerName;
    List<String> CustomerID;
    List<String> LeadGenerateOtherName;
    List<String> LeadGenerateOtheID;
    List<TerminalListPojo.Datum> data;
    List<AllUserListPojo.User> Userdata;
    List<StackListPojo.Datum> Stackdata;
    private List<CommudityResponse.Category> DataCommodity;
    private List<OUTComodityPojo.Datum> outCommodityListData;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_case_id;
    }

    @Override
    protected void setUp() {
        binding.etCustomerGatepass.setEnabled(false);
        binding.etCustomerGatepass.setFocusable(false);
        binding.etCustomerGatepass.setClickable(false);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
       /* binding.RLCommodity.setVisibility(View.GONE);
        binding.RLStack.setVisibility(View.GONE);*/

        clickListner();

        StackName = new ArrayList<>();
        StackID = new ArrayList<>();
        CommudityName = new ArrayList<>();
        TerminalName = new ArrayList<>();
        TerminalsID = new ArrayList<>();
        CustomerName = new ArrayList<>();
        CustomerID = new ArrayList<>();
        LeadGenerateOtherName = new ArrayList<>();
        LeadGenerateOtheID = new ArrayList<>();

        TerminalName.add(getResources().getString(R.string.terminal_name1));
        CustomerName.add(getResources().getString(R.string.select_coustomer));
        CommudityName.add(getResources().getString(R.string.commodity_name));
        StackName.add(getResources().getString(R.string.select_stack_number));
        LeadGenerateOtherName.add("If Lead Converted By Other");

        setValueOnSpinner();

        try {
            getTerminalListLevel();
        } catch (Exception e) {
            e.getStackTrace();
        }

        binding.etCustomerWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0 && !charSequence.equals("")) {
                    double finalWeightQTl = Double.parseDouble(charSequence.toString().trim()) / 100;
                    finalWeightQTl = Utility.round(finalWeightQTl, 2);
                    if (invWeightRequestWeight != null) {
                        if (finalWeightQTl > Double.parseDouble(invWeightRequestWeight)) {
                            Utility.showAlertDialog(CaseIDGenerateClassOLD.this, getString(R.string.alert), "This Weight Must be Less  or equal to requested Weight !!", new Utility.AlertCallback() {
                                @Override
                                public void callback() {
                                    binding.etCustomerWeightQuintal.setText("" + invWeightRequestWeight);
                                    Double qtilweight = Double.parseDouble(invWeightRequestWeight) * 100.0;
                                    binding.etCustomerWeight.setText("" + qtilweight);
                                }
                            });
                        } else {
                            binding.etCustomerWeightQuintal.setText("" + finalWeightQTl);
                        }
                    } else {
                        binding.etCustomerWeightQuintal.setText("" + finalWeightQTl);
                    }
                } else {
                    binding.etCustomerWeightQuintal.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setValueOnSpinner() {

        // setAllData();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getCommudity().size(); i++) {
//                    CommudityName.add(SharedPreferencesRepository.getDataManagerInstance().getCommudity().get(i).getCategory());
//                }
//                for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().GetTerminal().size(); i++) {
//                    TerminalName.add(SharedPreferencesRepository.getDataManagerInstance().GetTerminal().get(i).getName()
//                            + "(" + SharedPreferencesRepository.getDataManagerInstance().GetTerminal().get(i).getWarehouseCode() + ")");
//                }
//                for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getuserlist().size(); i++) {
//                    CustomerName.add(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getFname());
//                }
//                for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getEmployee().size(); i++) {
//                    if (SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getDesignationId().equalsIgnoreCase("6") ||
//                            SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getDesignationId().equalsIgnoreCase("7")) {
//                        LeadGenerateOtherName.add(SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getFirstName() + " " +
//                                SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getLastName() +
//                                "(" + SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getEmpId() + ")");
//                    }
//                }
//            }
//        });

        // UserList listing

        SpinnerUserListAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, CustomerName) {
            //            //By using this method we will define how
//            // the text appears before clicking a spinner
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            //
//            //By using this method we will define
//            //how the listview appears after clicking a spinner
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
//
        SpinnerUserListAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
//        // Set Adapter in the spinner
        binding.spinnerUserName.setAdapter(SpinnerUserListAdapter);
        binding.spinnerUserName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // selected item in the list
                if (position != 0) {
                    try {
                        UserID = parentView.getItemAtPosition(position).toString();
                        String[] part = UserID.split("(?<=\\D)(?=\\d)");
                        //    System.out.println(part[0]);
                        // seleectCoustomer= String.valueOf((""+Integer.parseInt(part[1])).split("\\)"));
                        seleectCoustomer = (part[1]);
                        binding.inoutRl.setVisibility(View.VISIBLE);
                        for (int i = 0; i < Userdata.size(); i++) {
                            if (UserID.equalsIgnoreCase(Userdata.get(i).getFname() + "(" + Userdata.get(i).getPhone())) {
                                UserUniqueID = Userdata.get(i).getUserId();
                            }
                        }
                        if (seleectCoustomer == null||TerminalID == null||selectInOUt==null) {
                        } else {
                            getoutCommodity();
                            getstack();
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                // SpinnerUserListAdapter.notifyDataSetChanged();
            }

            //
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
            }
        });

      /*  // Employee listing
        new Thread() {
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Thread() {
                                public void run() {
                                    for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getEmployee().size(); i++) {
                                        if (SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getDesignationId().equalsIgnoreCase("6") ||
                                                SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getDesignationId().equalsIgnoreCase("7")) {
                                            LeadGenerateOtherName.add(SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getFirstName() + " " +
                                                    SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getLastName() +
                                                    "(" + SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getEmpId() + ")");
                                        }
                                    }
                                }
                            }.start();
                        }
                    });
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();*/
        spinnerEmployeeAdpter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, LeadGenerateOtherName) {
            //By using this method we will define how
            // the text appears before clicking a spinner
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            //By using this method we will define
            //how the listview appears after clicking a spinner
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        spinnerEmployeeAdpter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerLeadConvertOther.setAdapter(spinnerEmployeeAdpter);
        binding.spinnerLeadConvertOther.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    String EmpID = parentView.getItemAtPosition(position).toString();
                    for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getEmployee().size(); i++) {
                        if (EmpID.equalsIgnoreCase(SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getFirstName() + " " + SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getLastName() + "(" + SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getEmpId() + ")")) {
                            selectConvertOther = String.valueOf(SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getUserId());
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // commodity listing
        if (selectInOUt!=null) {
            if (selectInOUt.equalsIgnoreCase("IN")) {
                SpinnerCommudityAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, CommudityName) {
                    //By using this method we will define how
                    // the text appears before clicking a spinner
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        ((TextView) v).setTextColor(Color.parseColor("#000000"));
                        return v;
                    }

                    //By using this method we will define
                    //how the listview appears after clicking a spinner
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View v = super.getDropDownView(position, convertView, parent);
                        v.setBackgroundColor(Color.parseColor("#05000000"));
                        ((TextView) v).setTextColor(Color.parseColor("#000000"));
                        return v;
                    }
                };
                SpinnerCommudityAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                // Set Adapter in the spinner
                binding.spinnerCommudity.setAdapter(SpinnerCommudityAdapter);
                binding.spinnerCommudity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // selected item in the list
                        if (position != 0) {
                            String presentMeterStatusID = parentView.getItemAtPosition(position).toString();
                            for (int i = 0; i < DataCommodity.size(); i++) {
                                String commodityType = DataCommodity.get(i).getCommodityType();
                                if (commodityType.equalsIgnoreCase("Primary")) {
                                    commodityType = "किसानी";
                                } else {
                                    commodityType = "MTP";
                                }
                                if (presentMeterStatusID.equalsIgnoreCase(DataCommodity.get(i).getCategory() + "(" + commodityType + ")")) {
                                    commudityID = String.valueOf(DataCommodity.get(i).getId());
                                    getstack();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });
            } else {
                SpinnerCommudityAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, CommudityName) {
                    //By using this method we will define how
                    // the text appears before clicking a spinner
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        ((TextView) v).setTextColor(Color.parseColor("#000000"));
                        return v;
                    }

                    //By using this method we will define
                    //how the listview appears after clicking a spinner
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View v = super.getDropDownView(position, convertView, parent);
                        v.setBackgroundColor(Color.parseColor("#05000000"));
                        ((TextView) v).setTextColor(Color.parseColor("#000000"));
                        return v;
                    }
                };
                SpinnerCommudityAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                // Set Adapter in the spinner
                binding.spinnerCommudity.setAdapter(SpinnerCommudityAdapter);
                binding.spinnerCommudity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // selected item in the list
                        if (position != 0) {
                            String presentMeterStatusID = parentView.getItemAtPosition(position).toString();
                            for (int i = 0; i < outCommodityListData.size(); i++) {
                                String commodityType = outCommodityListData.get(i).getCommodityType();
                                if (commodityType.equalsIgnoreCase("Primary")) {
                                    commodityType = "किसानी";
                                } else {
                                    commodityType = "MTP";
                                }
                                if (presentMeterStatusID.equalsIgnoreCase(outCommodityListData.get(i).getCategory() + "(" + commodityType + ")")) {
                                    commudityID = String.valueOf(outCommodityListData.get(i).getCommodity());
                                    getstack();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });
            }
        }

        // layout Terminal Listing resource and list of items.
        spinnerTeerminalAdpter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, TerminalName) {
            //By using this method we will define how
            // the text appears before clicking a spinner
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            //By using this method we will define
            //how the listview appears after clicking a spinner
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        spinnerTeerminalAdpter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerTerminal.setAdapter(spinnerTeerminalAdpter);
        binding.spinnerTerminal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    String presentMeterStatusID = parentView.getItemAtPosition(position).toString();
                    for (int i = 0; i < data.size(); i++) {
                        if (presentMeterStatusID.contains(data.get(i).getName())) {
                            TerminalID = String.valueOf(data.get(i).getId());
                            binding.rlUser.setVisibility(View.VISIBLE);
                            if (seleectCoustomer == null||TerminalID == null||selectInOUt==null) {
                            } else {
                                getoutCommodity();
                                getstack();
                            }
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        SpinnerCommudityAdapter = new ArrayAdapter<String>(CaseIDGenerateClassOLD.this, R.layout.multiline_spinner_item, CommudityName) {
            //By using this method we will define how
            // the text appears before clicking a spinner
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            //By using this method we will define
            //how the listview appears after clicking a spinner
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        SpinnerCommudityAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerCommudity.setAdapter(SpinnerCommudityAdapter);
        // spinner purpose
        binding.spinnerPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    selectPurpose = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // can leave this empty
            }
        });


        // spinner in/out
        binding.spinnerInOut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    selectInOUt = parent.getItemAtPosition(position).toString();
                    if (selectInOUt.equalsIgnoreCase("IN")) {
                        commudityID=null;
                        CommudityName.clear();
                        if (DataCommodity != null) {
                            DataCommodity.clear();
                        }
                        CommudityName.add(getResources().getString(R.string.commodity));
                        showDialog();
                        apiService.getcommuydity_terminal_user_emp_listing("Emp").enqueue(new NetworkCallbackWProgress<CommudityResponse>(getActivity()) {
                            @Override
                            protected void onSuccess(CommudityResponse body) {
                                DataCommodity = body.getCategories();
                                for (int i = 0; i < body.getCategories().size(); i++) {
                                    String commodityType = DataCommodity.get(i).getCommodityType();
                                    if (commodityType.equalsIgnoreCase("Primary")) {
                                        commodityType = "किसानी";
                                    } else {
                                        commodityType = "MTP";
                                    }
                                    CommudityName.add(DataCommodity.get(i).getCategory() + "(" + commodityType + ")");
                                    hideDialog();
                                }
                                binding.RLCommodity.setVisibility(View.VISIBLE);
                                SpinnerCommudityAdapter = new ArrayAdapter<String>(CaseIDGenerateClassOLD.this, R.layout.multiline_spinner_item, CommudityName) {
                                    //By using this method we will define how
                                    // the text appears before clicking a spinner
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        View v = super.getView(position, convertView, parent);
                                        ((TextView) v).setTextColor(Color.parseColor("#000000"));
                                        return v;
                                    }

                                    //By using this method we will define
                                    //how the listview appears after clicking a spinner
                                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                        View v = super.getDropDownView(position, convertView, parent);
                                        v.setBackgroundColor(Color.parseColor("#05000000"));
                                        ((TextView) v).setTextColor(Color.parseColor("#000000"));
                                        return v;
                                    }
                                };
                                SpinnerCommudityAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                                // Set Adapter in the spinner
                                binding.spinnerCommudity.setAdapter(SpinnerCommudityAdapter);
                                getstack();
                            }
                        });

                    }else {
                        if (TerminalID == null) {
                            Toast.makeText(CaseIDGenerateClassOLD.this, getResources().getString(R.string.terminal_name), Toast.LENGTH_LONG).show();
                        } else if (seleectCoustomer == null) {
                            Toast.makeText(CaseIDGenerateClassOLD.this, getResources().getString(R.string.select_coustomer), Toast.LENGTH_LONG).show();
                        } else {
                            commudityID=null;
                            getoutCommodity();

                        }
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // can leave this empty
            }
        });

     /*   // spinner select other generated
        binding.spinnerLeadConvertOther.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    selectConvertOther = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // can leave this empty
            }
        });*/

      /*  binding.spinnerUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomerNamePopup(CustomerName);
            }
        });*/
        // layout Terminal Listing resource and list of items.
        SpinnerStackAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, StackName) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        SpinnerStackAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        binding.spinnerStack.setAdapter(SpinnerStackAdapter);
        binding.spinnerStack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    try {
                        stackID = String.valueOf(Stackdata.get(position - 1).getId());
                        if (selectInOUt.contains("IN")) {
                            invWeightRequestWeight = "" + Stackdata.get(position - 1).getRequestWeight();
                            binding.etCustomerVehicle.setText("" + Stackdata.get(position - 1).getVehicle_no());
                            binding.etCustomerWeightQuintal.setText("" + Stackdata.get(position - 1).getRequestWeight());
                            Double qtilweight = Double.parseDouble(Stackdata.get(position - 1).getRequestWeight()) * 100.0;
                            binding.etCustomerWeight.setText("" + qtilweight);
                            binding.etCustomerVehicle.setClickable(false);
                            binding.etCustomerVehicle.setFocusable(false);
                            binding.etCustomerVehicle.setEnabled(false);
                            binding.etCustomerVehicle.setFocusableInTouchMode(false);
                        } else {
                            invWeightRequestWeight = "" + Stackdata.get(position - 1).getRequestWeight();
                            binding.etCustomerWeightQuintal.setText("" + Stackdata.get(position - 1).getRequestWeight());
                            Double qtilweight = Double.parseDouble(Stackdata.get(position - 1).getRequestWeight()) * 100.0;
                            binding.etCustomerWeight.setText("" + qtilweight);
                            binding.etCustomerVehicle.setText("" + Stackdata.get(position - 1).getVehicle_no());
                            binding.etCustomerVehicle.setClickable(false);
                            binding.etCustomerVehicle.setFocusable(false);
                            binding.etCustomerVehicle.setEnabled(false);
                            binding.etCustomerVehicle.setFocusableInTouchMode(false);
                        }
                    } catch (IndexOutOfBoundsException e) {

                    }
                    String presentMeterStatusID = parentView.getItemAtPosition(position).toString();
                 /*   for (int i = 0; i < Stackdata.size(); i++) {
                        if (Stackdata.get(position).getStackId().equalsIgnoreCase(StackID.get(i))){*/

                         /*   break;
                        }*/
                       /* if (presentMeterStatusID.equalsIgnoreCase(Stackdata.get(i).getStackNumber())) {
                            stackID = String.valueOf(Stackdata.get(i).getId());
                            if (selectInOUt.contains("IN")) {
                                binding.etCustomerVehicle.setText("" + Stackdata.get(i).getVehicle_no());
                                binding.etCustomerVehicle.setClickable(false);
                                binding.etCustomerVehicle.setFocusable(false);
                                binding.etCustomerVehicle.setEnabled(false);
                                binding.etCustomerVehicle.setFocusableInTouchMode(false);
                            } else {
                                 invWeightRequestWeight = "" + Stackdata.get(i).getRequestWeight();
                                binding.etCustomerWeightQuintal.setText("" + Stackdata.get(i).getRequestWeight());
                                Double qtilweight = Double.parseDouble(Stackdata.get(i).getRequestWeight())*100.0;
                                binding.etCustomerWeight.setText("" +qtilweight);
                                binding.etCustomerVehicle.setText("" + Stackdata.get(i).getVehicle_no());
                                binding.etCustomerVehicle.setClickable(false);
                                binding.etCustomerVehicle.setFocusable(false);
                                binding.etCustomerVehicle.setEnabled(false);
                                binding.etCustomerVehicle.setFocusableInTouchMode(false);
                            }
                            break;
                        }*/
                    // }
                } else {
                    stackID = null;
                    binding.etCustomerVehicle.setClickable(true);
                    binding.etCustomerVehicle.setFocusable(true);
                    binding.etCustomerVehicle.setEnabled(true);
                    binding.etCustomerVehicle.setFocusableInTouchMode(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void getTerminalListLevel() {
        apiService.getTerminalListLevel().enqueue(new NetworkCallback<TerminalListPojo>(getActivity()) {
            @Override
            protected void onSuccess(TerminalListPojo body) {
                data = body.getData();
                for (int i = 0; i < data.size(); i++) {
                    TerminalName.add(data.get(i).getName() + "(" + data.get(i).getWarehouseCode() + ")");
                }
            }
        });
    }

    private void getoutCommodity() {
        showDialog();
        apiService.getOutCommodityList(TerminalID, UserUniqueID).enqueue(new NetworkCallback<OUTComodityPojo>(getActivity()) {
            @Override
            protected void onSuccess(OUTComodityPojo body) {
                if (selectInOUt.equalsIgnoreCase("IN")) {

                } else {
                    CommudityName.clear();
                    if (outCommodityListData != null) {
                        outCommodityListData.clear();
                    }
                    CommudityName.add(getResources().getString(R.string.commodity));
                    outCommodityListData = body.getData();
                    for (int i = 0; i < outCommodityListData.size(); i++) {
                        String commodityType = outCommodityListData.get(i).getCommodityType();
                        if (commodityType.equalsIgnoreCase("Primary")) {
                            commodityType = "किसानी";
                        } else {
                            commodityType = "MTP";
                        }
                        CommudityName.add(outCommodityListData.get(i).getCategory() + "(" + commodityType + ")");
                        hideDialog();
                    }
                    binding.RLCommodity.setVisibility(View.VISIBLE);
                    SpinnerCommudityAdapter = new ArrayAdapter<String>(CaseIDGenerateClassOLD.this, R.layout.multiline_spinner_item, CommudityName) {
                        //By using this method we will define how
                        // the text appears before clicking a spinner
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);
                            ((TextView) v).setTextColor(Color.parseColor("#000000"));
                            return v;
                        }

                        //By using this method we will define
                        //how the listview appears after clicking a spinner
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View v = super.getDropDownView(position, convertView, parent);
                            v.setBackgroundColor(Color.parseColor("#05000000"));
                            ((TextView) v).setTextColor(Color.parseColor("#000000"));
                            return v;
                        }
                    };
                    SpinnerCommudityAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                    // Set Adapter in the spinner
                    binding.spinnerCommudity.setAdapter(SpinnerCommudityAdapter);
                    if (selectInOUt!=null) {
                        if (selectInOUt.equalsIgnoreCase("IN")) {
                            SpinnerCommudityAdapter = new ArrayAdapter<String>(CaseIDGenerateClassOLD.this, R.layout.multiline_spinner_item, CommudityName) {
                                //By using this method we will define how
                                // the text appears before clicking a spinner
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View v = super.getView(position, convertView, parent);
                                    ((TextView) v).setTextColor(Color.parseColor("#000000"));
                                    return v;
                                }

                                //By using this method we will define
                                //how the listview appears after clicking a spinner
                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    View v = super.getDropDownView(position, convertView, parent);
                                    v.setBackgroundColor(Color.parseColor("#05000000"));
                                    ((TextView) v).setTextColor(Color.parseColor("#000000"));
                                    return v;
                                }
                            };
                            SpinnerCommudityAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                            // Set Adapter in the spinner
                            binding.spinnerCommudity.setAdapter(SpinnerCommudityAdapter);
                            binding.spinnerCommudity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                    // selected item in the list
                                    if (position != 0) {
                                        String presentMeterStatusID = parentView.getItemAtPosition(position).toString();
                                        for (int i = 0; i < DataCommodity.size(); i++) {
                                            String commodityType = DataCommodity.get(i).getCommodityType();
                                            if (commodityType.equalsIgnoreCase("Primary")) {
                                                commodityType = "किसानी";
                                            } else {
                                                commodityType = "MTP";
                                            }
                                            if (presentMeterStatusID.equalsIgnoreCase(DataCommodity.get(i).getCategory() + "(" + commodityType + ")")) {
                                                commudityID = String.valueOf(DataCommodity.get(i).getId());
                                                getstack();
                                                break;
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parentView) {
                                    // your code here
                                }
                            });
                        } else {
                            SpinnerCommudityAdapter = new ArrayAdapter<String>(CaseIDGenerateClassOLD.this, R.layout.multiline_spinner_item, CommudityName) {
                                //By using this method we will define how
                                // the text appears before clicking a spinner
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View v = super.getView(position, convertView, parent);
                                    ((TextView) v).setTextColor(Color.parseColor("#000000"));
                                    return v;
                                }

                                //By using this method we will define
                                //how the listview appears after clicking a spinner
                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    View v = super.getDropDownView(position, convertView, parent);
                                    v.setBackgroundColor(Color.parseColor("#05000000"));
                                    ((TextView) v).setTextColor(Color.parseColor("#000000"));
                                    return v;
                                }
                            };
                            SpinnerCommudityAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                            // Set Adapter in the spinner
                            binding.spinnerCommudity.setAdapter(SpinnerCommudityAdapter);
                            binding.spinnerCommudity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                    // selected item in the list
                                    if (position != 0) {
                                        String presentMeterStatusID = parentView.getItemAtPosition(position).toString();
                                        for (int i = 0; i < outCommodityListData.size(); i++) {
                                            String commodityType = outCommodityListData.get(i).getCommodityType();
                                            if (commodityType.equalsIgnoreCase("Primary")) {
                                                commodityType = "किसानी";
                                            } else {
                                                commodityType = "MTP";
                                            }
                                            if (presentMeterStatusID.equalsIgnoreCase(outCommodityListData.get(i).getCategory() + "(" + commodityType + ")")) {
                                                commudityID = String.valueOf(outCommodityListData.get(i).getCommodity());
                                                getstack();
                                                break;
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parentView) {
                                    // your code here
                                }
                            });
                        }
                    }

                }
            }
        });
    }

    private void getstack() {
        if (commudityID==null){
            Utility.showAlertDialog(CaseIDGenerateClassOLD.this, getString(R.string.alert), "please select commodity First ", new Utility.AlertCallback() {
                @Override
                public void callback() {

                }
            });
        }else {
            apiService.getStackList(new StackPostData(commudityID, TerminalID, seleectCoustomer, selectInOUt)).enqueue(new NetworkCallback<StackListPojo>(getActivity()) {
                @Override
                protected void onSuccess(StackListPojo body) {
                    StackName.clear();
                    StackID.clear();
                    stackID = null;
                    binding.spinnerStack.setPrompt("");
                    StackName.add("Stack Number");
                    StackID.add("0");
                    Stackdata = body.getData();
                    for (int i = 0; i < Stackdata.size(); i++) {
                        StackName.add(Stackdata.get(i).getStackNumber());
                        StackID.add(Stackdata.get(i).getId());
                        //SpinnerStackAdapter.notifyDataSetChanged();
                    }
                    SpinnerStackAdapter = new ArrayAdapter<String>(CaseIDGenerateClassOLD.this, R.layout.multiline_spinner_item, StackName) {
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);
                            ((TextView) v).setTextColor(Color.parseColor("#000000"));
                            return v;
                        }

                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View v = super.getDropDownView(position, convertView, parent);
                            v.setBackgroundColor(Color.parseColor("#05000000"));
                            ((TextView) v).setTextColor(Color.parseColor("#000000"));
                            return v;
                        }
                    };
                    SpinnerStackAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                    binding.spinnerStack.setAdapter(SpinnerStackAdapter);
                }
            });
        }
    }

    private void showCustomerNamePopup(List<String> getList) {
        try {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.setContentView(R.layout.customer_list_popup);
            dialog.setCanceledOnTouchOutside(false);

            RecyclerView recyclerViewCustomerPopup = dialog.findViewById(R.id.recyclerViewCustomerPopup);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerViewCustomerPopup.setLayoutManager(layoutManager);

            recyclerViewCustomerPopup.setHasFixedSize(true);
            recyclerViewCustomerPopup.setNestedScrollingEnabled(false);
//            CustomerNameAdapter customerNameAdapter = new CustomerNameAdapter(getList, this);
//            recyclerViewCustomerPopup.setAdapter(customerNameAdapter);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAllData() {
        showDialog();
        new Thread() {
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Thread() {
                                public void run() {
                                    for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getCommudity().size(); i++) {
                                        CommudityName.add(SharedPreferencesRepository.getDataManagerInstance().getCommudity().get(i).getCategory() + "(" + SharedPreferencesRepository.getDataManagerInstance().getCommudity().get(i).getCommodityType() + ")");
                                    }
                                   /* for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getuserlist().size(); i++) {
                                        CustomerName.add(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getFname() + "(" + SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getPhone());
                                    }*/
                                    hideDialog();
                                }
                            }.start();
                        }
                    });
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void getUserList() {
        apiService.getUserList().enqueue(new NetworkCallback<AllUserListPojo>(getActivity()) {
            @Override
            protected void onSuccess(AllUserListPojo body) {
                Userdata = body.getUsers();
                for (int i = 0; i < Userdata.size(); i++) {
                    CustomerName.add(Userdata.get(i).getFname() + "(" + Userdata.get(i).getPhone());
                }
            }
        });
    }

    private void clickListner() {
        binding.ivClose.setOnClickListener(this);
        binding.btnCreateeCase.setOnClickListener(this);
        binding.tvDone.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(StaffDashBoardActivity.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                startActivityAndClear(StaffDashBoardActivity.class);
                break;
            case R.id.tv_done:
                startActivityAndClear(CaseListingActivity.class);
                break;
            case R.id.btn_createe_case:
                if (isValid()) {
                          /*  for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getuserlist().size(); i++) {
                                if (UserID.equalsIgnoreCase(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getFname())) {
                                    seleectCoustomer = String.valueOf(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getPhone());
                                }
                            }*/
                    if (commudityID == null) {
                        Toast.makeText(CaseIDGenerateClassOLD.this, getResources().getString(R.string.commudity_name), Toast.LENGTH_LONG).show();
                    } else if (TerminalID == null) {
                        Toast.makeText(CaseIDGenerateClassOLD.this, getResources().getString(R.string.terminal_name), Toast.LENGTH_LONG).show();
                    } else if (selectInOUt == null) {
                        Toast.makeText(CaseIDGenerateClassOLD.this, getResources().getString(R.string.select_in_out), Toast.LENGTH_LONG).show();
                    } else if (stackID == null) {
                        Toast.makeText(CaseIDGenerateClassOLD.this, "Select Stack Number", Toast.LENGTH_LONG).show();
                    } /*else if (selectPurpose == null) {
                        Toast.makeText(CaseIDGenerateClass.this, getResources().getString(R.string.select_purposee), Toast.LENGTH_LONG).show();
                    }*/ /*else if (selectConvertOther == null) {
                        Toast.makeText(CaseIDGenerateClass.this, getResources().getString(R.string.select_employee), Toast.LENGTH_LONG).show();
                    }*/ else if (seleectCoustomer == null) {
                        Toast.makeText(CaseIDGenerateClassOLD.this, getResources().getString(R.string.select_coustomer), Toast.LENGTH_LONG).show();
                    } else {
                        Utility.showDecisionDialog(CaseIDGenerateClassOLD.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                            @Override
                            public void callback() {
                                showDialog();
                              /*  for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getuserlist().size(); i++) {
                                    if (UserID.equalsIgnoreCase(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getFname() + "(" + SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getPhone() + ")")) {
                                        seleectCoustomer = String.valueOf(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getPhone());
                                    }
                                }*/
                                UserDetails userDetails = SharedPreferencesRepository.getDataManagerInstance().getUser();
                                if (selectInOUt.equalsIgnoreCase("OUT")) {
                                    selectPurpose = "Self Withdrawal";
                                } else {
                                    selectPurpose = "For Storage";
                                }
                                apiService.doCreateCaseID(new CreateCaseIDPostData(
                                        stringFromView(binding.etCustomerGatepass), selectInOUt, stringFromView(binding.etCustomerWeight), /*stringFromView(binding.etCustomerLocation)*/  "",
                                        seleectCoustomer, commudityID, TerminalID, stringFromView(binding.etCustomerVehicle),
                                        selectPurpose, stringFromView(binding.etCustomerFpo), stackID, selectConvertOther, invWeightRequestWeight)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                                    @Override
                                    protected void onSuccess(LoginResponse body) {
                                        hideDialog();
                                        Utility.showAlertDialog(CaseIDGenerateClassOLD.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                                            @Override
                                            public void callback() {
                                                startActivityAndClear(CaseListingActivity.class);
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                }
                break;
        }
    }

    boolean isValid() {
        if (TextUtils.isEmpty(stringFromView(binding.etCustomerWeight))) {
            return Utility.showEditTextError(binding.tilCustomerWeight, R.string.weight_kg);
        } /*else if (TextUtils.isEmpty(stringFromView(binding.etCustomerGatepass))) {
            return Utility.showEditTextError(binding.tilCustomerGatepass, R.string.gate_pass);
        }*/ /*else if (TextUtils.isEmpty(stringFromView(binding.etCustomerLocation))) {
            return Utility.showEditTextError(binding.tilCustomerLocation, R.string.location);
        }*/ else if (TextUtils.isEmpty(stringFromView(binding.etCustomerVehicle))) {
            return Utility.showEditTextError(binding.tilCustomerVehicle, R.string.vehicle_no);
        }/*  else if (TextUtils.isEmpty(stringFromView(binding.etCustomerFpo))) {
            return Utility.showEditTextError(binding.tilCustomerFpo, R.string.fpo_sub_useername);
        }*/
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
