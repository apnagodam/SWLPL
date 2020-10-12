package com.apnagodam.staff.activity.in.labourbook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.CreateLeadsPostData;
import com.apnagodam.staff.Network.Request.UploadLabourDetailsPostData;
import com.apnagodam.staff.Network.Request.UploadTruckDetailsPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.gatepass.UploadGatePassClass;
import com.apnagodam.staff.activity.in.truckbook.TruckBookListingActivity;
import com.apnagodam.staff.activity.in.truckbook.TruckUploadDetailsClass;
import com.apnagodam.staff.activity.lead.LeadGenerateClass;
import com.apnagodam.staff.activity.lead.LeadListingActivity;
import com.apnagodam.staff.databinding.ActivityGeenerteLeadsBinding;
import com.apnagodam.staff.databinding.ActivityUploadLabourDetailsBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LabourBookUploadClass extends BaseActivity<ActivityUploadLabourDetailsBinding> implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    String UserName, CaseID = "";
    private Calendar calender;
    ArrayAdapter<String> SpinnerControactorAdapter;
    String contractorsID = null;
    // droup down  of meter status
    List<String> contractorName;
    boolean checked = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_upload_labour_details;
    }

    @Override
    protected void setUp() {
        calender = Calendar.getInstance();
        Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle != null) {
            UserName = bundle.getString("user_name");
            CaseID = bundle.getString("case_id");
        }
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        clickListner();
        binding.customerName.setText(UserName);
        binding.caseId.setText(CaseID);
        contractorName = new ArrayList<>();
        contractorName.add(getResources().getString(R.string.contractor_select));
        setValueOnSpinner();
    }

    private void setValueOnSpinner() {

        for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getContractorList().size(); i++) {
            contractorName.add(SharedPreferencesRepository.getDataManagerInstance().getContractorList().get(i).getContractorName());
        }

        SpinnerControactorAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, contractorName) {
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
        SpinnerControactorAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerContractor.setAdapter(SpinnerControactorAdapter);

        binding.spinnerContractor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    contractorsID = parentView.getItemAtPosition(position).toString();
                    for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getContractorList().size(); i++) {
                        if (contractorsID.equalsIgnoreCase(SharedPreferencesRepository.getDataManagerInstance().getContractorList().get(i).getContractorName())) {
                            binding.etContractorPhone.setText(String.valueOf(SharedPreferencesRepository.getDataManagerInstance().getContractorList().get(i).getContractorPhone()));
                            binding.etLabourRate.setText(String.valueOf(SharedPreferencesRepository.getDataManagerInstance().getContractorList().get(i).getRate()));
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

    private void clickListner() {
        binding.ivClose.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.etStartDateTime.setOnClickListener(this);
        binding.lpCommiteDate.setOnClickListener(this);
        binding.checkNotRequried.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                                @Override
                                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                    if (buttonView.isChecked()) {
                                                                        // checked
                                                                        checked = true;
                                                                        binding.etLocation.setEnabled(false);
                                                                        binding.etLocation.setClickable(false);
                                                                        binding.etLocation.setFocusable(false);
                                                                        binding.etLocation.setText("");
                                                                    } else {
                                                                        // not checked
                                                                        checked = false;
                                                                        binding.etLocation.setEnabled(true);
                                                                        binding.etLocation.setClickable(true);
                                                                        binding.etLocation.setFocusable(true);
                                                                        binding.etLocation.setFocusableInTouchMode(true);
                                                                    }
                                                                }
                                                            }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.et_start_date_time:
                popUpDatePicker();
                break;
            case R.id.lp_commite_date:
                popUpDatePicker();
                break;
            case R.id.btn_login:
                Utility.showDecisionDialog(LabourBookUploadClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        if (isValid()) {
                            if (TextUtils.isEmpty(stringFromView(binding.etStartDateTime))) {
                                Toast.makeText(LabourBookUploadClass.this, getResources().getString(R.string.booking_date_val), Toast.LENGTH_LONG).show();
                            } else if (contractorsID == null) {
                                Toast.makeText(LabourBookUploadClass.this, getResources().getString(R.string.contractor_select), Toast.LENGTH_LONG).show();
                            } else {
                                apiService.uploadLabourDetails(new UploadLabourDetailsPostData(
                                        CaseID, contractorsID, stringFromView(binding.etContractorPhone), stringFromView(binding.etLocation),
                                        stringFromView(binding.etLabourRate), stringFromView(binding.etLabourTotal), stringFromView(binding.etTotalBags),
                                        stringFromView(binding.notes), stringFromView(binding.etStartDateTime))).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                                    @Override
                                    protected void onSuccess(LoginResponse body) {
                                        Toast.makeText(LabourBookUploadClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                                        startActivityAndClear(LabourBookListingActivity.class);
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
        if (checked) {

        } else {
            if (TextUtils.isEmpty(stringFromView(binding.etLocation))) {
                return Utility.showEditTextError(binding.tilLocation, R.string.location);
            }
        }
        if (TextUtils.isEmpty(stringFromView(binding.etContractorPhone))) {
            return Utility.showEditTextError(binding.tilContractorPhone, R.string.contractor_phone_val);
        } else if (TextUtils.isEmpty(stringFromView(binding.etLabourRate))) {
            return Utility.showEditTextError(binding.tilLabourRate, R.string.labour_rate_val);
        } else if (TextUtils.isEmpty(stringFromView(binding.etLabourTotal))) {
            return Utility.showEditTextError(binding.tilLabourTotal, R.string.labour_total_val);
        } else if (TextUtils.isEmpty(stringFromView(binding.etTotalBags))) {
            return Utility.showEditTextError(binding.tilTotalBags, R.string.total_bags_val);
        }
        return true;
    }

    public void popUpDatePicker() {
        DatePickerDialog dateDialog = new DatePickerDialog(this, date, calender
                .get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH));
        dateDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dateDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calender.set(Calendar.YEAR, year);
            calender.set(Calendar.MONTH, monthOfYear);
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            binding.etStartDateTime.setText(sdf.format(calender.getTime()).toString());
        }
    };


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
