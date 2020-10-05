package com.apnagodam.staff.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.CreateLeadsPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.ActivityGeenerteLeadsBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.Utility;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LeadGenerateClass extends BaseActivity<ActivityGeenerteLeadsBinding> implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    ArrayAdapter<String> SpinnerCommudityAdapter, spinnerTeerminalAdpter;
    String commudityID = null, TerminalID = null, selectPurpose = null;
    // droup down  of meter status
    List<String> CommudityName;
    List<String> CommudityID;
    List<String> TerminalName;
    List<String> TerminalsID;
    private Calendar calender;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_geenerte_leads;
    }

    @Override
    protected void setUp() {
        calender = Calendar.getInstance();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        clickListner();
        CommudityName = new ArrayList<>();
        CommudityID = new ArrayList<>();
        TerminalName = new ArrayList<>();
        TerminalsID = new ArrayList<>();
        CommudityName.add(getResources().getString(R.string.commodity_name));
        TerminalName.add(getResources().getString(R.string.terminal_name1));
        setValueOnSpinner();
    }

    private void setValueOnSpinner() {
        // spinner meter obj
        // layout resource and list of items.
       // commudityID = "" + SharedPreferencesRepository.getDataManagerInstance().getCommudity().get(0).getId();
     //   TerminalID = "" + SharedPreferencesRepository.getDataManagerInstance().GetTerminal().get(0).getId();

        for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getCommudity().size(); i++) {
            CommudityName.add(SharedPreferencesRepository.getDataManagerInstance().getCommudity().get(i).getCategory());
        }
        for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().GetTerminal().size(); i++) {
            TerminalName.add(SharedPreferencesRepository.getDataManagerInstance().GetTerminal().get(i).getName() + "(" + SharedPreferencesRepository.getDataManagerInstance().GetTerminal().get(i).getWarehouseCode() + ")");
        }
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
        // layout resource and list of items.
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
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
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

    }

    private void clickListner() {
        binding.ivClose.setOnClickListener(this);
        binding.lpCommiteDate.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
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
                startActivity(LeadListingActivity.class);
                break;
            case R.id.lp_commite_date:
                popUpDatePicker();
                break;
            case R.id.btn_login:
                if (isValid()) {
                    if (commudityID == null) {
                        Toast.makeText(LeadGenerateClass.this, getResources().getString(R.string.commudity_name), Toast.LENGTH_LONG).show();
                    } else if (TerminalID == null) {
                        Toast.makeText(LeadGenerateClass.this,  getResources().getString(R.string.terminal_name), Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(stringFromView(binding.userCommitmentDate))) {
                        Toast.makeText(LeadGenerateClass.this, "Select Commitment Date", Toast.LENGTH_LONG).show();
                    } else if (selectPurpose == null) {
                        Toast.makeText(LeadGenerateClass.this,  getResources().getString(R.string.select_purposee), Toast.LENGTH_LONG).show();
                    } else {
                        UserDetails userDetails = SharedPreferencesRepository.getDataManagerInstance().getUser();
                        apiService.doCreateLeads(new CreateLeadsPostData(
                                userDetails.getUserId(), stringFromView(binding.etCustomerName), stringFromView(binding.etCustomerQuantity),
                                stringFromView(binding.etCustomerLocation), stringFromView(binding.etCustomerNumber), commudityID, TerminalID, stringFromView(binding.userCommitmentDate),
                                selectPurpose)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                            @Override
                            protected void onSuccess(LoginResponse body) {
                                Toast.makeText(LeadGenerateClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                                startActivity(LeadListingActivity.class);
                            }
                        });
                    }
                }
                break;
        }
    }

    boolean isValid() {
        if (TextUtils.isEmpty(stringFromView(binding.etCustomerName))) {
            return Utility.showEditTextError(binding.tilCustomerName, R.string.coustomer_name);
        } else if (TextUtils.isEmpty(stringFromView(binding.etCustomerNumber))) {
            return Utility.showEditTextError(binding.tilCustomerNumber, R.string.mobile_no);
        } else if (TextUtils.isEmpty(stringFromView(binding.etCustomerQuantity))) {
            return Utility.showEditTextError(binding.tilCustomerQuantity, R.string.quanity);
        } else if (TextUtils.isEmpty(stringFromView(binding.etCustomerLocation))) {
            return Utility.showEditTextError(binding.tilCustomerLocation, R.string.location);
        }
        return true;
    }

    public void popUpDatePicker() {
        DatePickerDialog dateDialog = new DatePickerDialog(LeadGenerateClass.this, date, calender
                .get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH));
        dateDialog.getDatePicker().setMinDate(System.currentTimeMillis());
      /*  long now = System.currentTimeMillis() - 1000;
        dateDialog.getDatePicker().setMaxDate(now+(1000*60*60*24*900)); //After 900 Days from Now*/
        dateDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calender.set(Calendar.YEAR, year);
            calender.set(Calendar.MONTH, monthOfYear);
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            binding.userCommitmentDate.setText(sdf.format(calender.getTime()).toString());
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
