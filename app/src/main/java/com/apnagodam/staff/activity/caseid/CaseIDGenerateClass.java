package com.apnagodam.staff.activity.caseid;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.CreateCaseIDPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.in.secound_quality_reports.UploadSecoundQualtityReportsClass;
import com.apnagodam.staff.databinding.ActivityCaseIdBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.GetPassID;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class CaseIDGenerateClass extends BaseActivity<ActivityCaseIdBinding> implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    ArrayAdapter<String> SpinnerCommudityAdapter, spinnerTeerminalAdpter, SpinnerUserListAdapter, spinnerEmployeeAdpter;
    String commudityID = null, TerminalID = null, selectPurpose = null,
            selectInOUt = null, seleectCoustomer = null, selectConvertOther = null;
    // droup down
    List<String> CommudityName;
    List<String> CommudityID;
    List<String> TerminalName;
    List<String> TerminalsID;
    List<String> CustomerName;
    List<String> CustomerID;
    List<String> LeadGenerateOtherName;
    List<String> LeadGenerateOtheID;

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
        clickListner();
        CommudityName = new ArrayList<>();
        CommudityID = new ArrayList<>();
        TerminalName = new ArrayList<>();
        TerminalsID = new ArrayList<>();
        CustomerName = new ArrayList<>();
        CustomerID = new ArrayList<>();
        LeadGenerateOtherName = new ArrayList<>();
        LeadGenerateOtheID = new ArrayList<>();
        CommudityName.add(getResources().getString(R.string.commodity_name));
        TerminalName.add(getResources().getString(R.string.terminal_name1));
        CustomerName.add(getResources().getString(R.string.select_coustomer));
        LeadGenerateOtherName.add(getResources().getString(R.string.select_employee));
        setValueOnSpinner();
        binding.etCustomerWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0 && !charSequence.equals("")) {
                    double finalWeightQTl = Double.parseDouble(charSequence.toString().trim()) / 100;
                    binding.etCustomerWeightQuintal.setText("" + finalWeightQTl);
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
        setAllData();
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
        SpinnerUserListAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerUserName.setAdapter(SpinnerUserListAdapter);
        binding.spinnerUserName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    String UserID = parentView.getItemAtPosition(position).toString();
                    for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getuserlist().size(); i++) {
                        if (UserID.equalsIgnoreCase(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getFname())) {
                            seleectCoustomer = String.valueOf(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getPhone());
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // Employee listing
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
                    for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getCommudity().size(); i++) {
                        if (presentMeterStatusID.equalsIgnoreCase(SharedPreferencesRepository.getDataManagerInstance().getCommudity().get(i).getCategory())) {
                            commudityID = String.valueOf(SharedPreferencesRepository.getDataManagerInstance().getCommudity().get(i).getId());
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
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
                    for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().GetTerminal().size(); i++) {
                        if (presentMeterStatusID.contains(SharedPreferencesRepository.getDataManagerInstance().GetTerminal().get(i).getName())) {
                            TerminalID = String.valueOf(SharedPreferencesRepository.getDataManagerInstance().GetTerminal().get(i).getId());
                            getGatepass(TerminalID);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
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
                if (position != 0)
                    selectInOUt = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // can leave this empty
            }
        });

        // spinner select other generated
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
        });

    }

    private void setAllData() {
        new Thread() {
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Thread() {
                                public void run() {
                                    for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getCommudity().size(); i++) {
                                        CommudityName.add(SharedPreferencesRepository.getDataManagerInstance().getCommudity().get(i).getCategory());
                                    }
                                    for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().GetTerminal().size(); i++) {
                                        TerminalName.add(SharedPreferencesRepository.getDataManagerInstance().GetTerminal().get(i).getName()
                                                + "(" + SharedPreferencesRepository.getDataManagerInstance().GetTerminal().get(i).getWarehouseCode() + ")");
                                    }
                                    for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getuserlist().size(); i++) {
                                        CustomerName.add(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getFname());
                                    }
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
        }.start();
    }

    private void getGatepass(String terminalID) {
        apiService.getGatePass(terminalID).enqueue(new NetworkCallback<GetPassID>(getActivity()) {
            @Override
            protected void onSuccess(GetPassID body) {
                binding.etCustomerGatepass.setText("" + body.getGate_pass());
                binding.etCustomerGatepass.setEnabled(true);
                binding.etCustomerGatepass.setFocusable(true);
                binding.etCustomerGatepass.setClickable(true);
                binding.etCustomerGatepass.setFocusableInTouchMode(true);
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
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_done:
                startActivityAndClear(CaseListingActivity.class);
                break;
            case R.id.btn_createe_case:
                Utility.showDecisionDialog(CaseIDGenerateClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                if (isValid()) {
                    if (commudityID == null) {
                        Toast.makeText(CaseIDGenerateClass.this, getResources().getString(R.string.commudity_name), Toast.LENGTH_LONG).show();
                    } else if (TerminalID == null) {
                        Toast.makeText(CaseIDGenerateClass.this, getResources().getString(R.string.terminal_name), Toast.LENGTH_LONG).show();
                    } else if (selectInOUt == null) {
                        Toast.makeText(CaseIDGenerateClass.this, getResources().getString(R.string.select_in_out), Toast.LENGTH_LONG).show();
                    } else if (selectPurpose == null) {
                        Toast.makeText(CaseIDGenerateClass.this, getResources().getString(R.string.select_purposee), Toast.LENGTH_LONG).show();
                    } /*else if (selectConvertOther == null) {
                        Toast.makeText(CaseIDGenerateClass.this, getResources().getString(R.string.select_employee), Toast.LENGTH_LONG).show();
                    }*/ else if (seleectCoustomer == null) {
                        Toast.makeText(CaseIDGenerateClass.this, getResources().getString(R.string.select_coustomer), Toast.LENGTH_LONG).show();
                    } else {
                        UserDetails userDetails = SharedPreferencesRepository.getDataManagerInstance().getUser();
                        apiService.doCreateCaseID(new CreateCaseIDPostData(
                                stringFromView(binding.etCustomerGatepass), selectInOUt, stringFromView(binding.etCustomerWeight), stringFromView(binding.etCustomerLocation),
                                seleectCoustomer, commudityID, TerminalID, stringFromView(binding.etCustomerVehicle),
                                selectPurpose, stringFromView(binding.etCustomerFpo))).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                            @Override
                            protected void onSuccess(LoginResponse body) {
                                Toast.makeText(CaseIDGenerateClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                                startActivity(StaffDashBoardActivity.class);
                            }
                        });
                    }
                }
                    }
                });
                break;
        }
    }

    boolean isValid() {
        if (TextUtils.isEmpty(stringFromView(binding.etCustomerWeight))) {
            return Utility.showEditTextError(binding.tilCustomerWeight, R.string.weight_kg);
        } else if (TextUtils.isEmpty(stringFromView(binding.etCustomerGatepass))) {
            return Utility.showEditTextError(binding.tilCustomerGatepass, R.string.gate_pass);
        }else if (TextUtils.isEmpty(stringFromView(binding.etCustomerLocation))) {
            return Utility.showEditTextError(binding.tilCustomerLocation, R.string.location);
        }
       /* else if (TextUtils.isEmpty(stringFromView(binding.etCustomerVehicle))) {
            return Utility.showEditTextError(binding.tilCustomerVehicle, R.string.vehicle_no);
        }  else if (TextUtils.isEmpty(stringFromView(binding.etCustomerFpo))) {
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
