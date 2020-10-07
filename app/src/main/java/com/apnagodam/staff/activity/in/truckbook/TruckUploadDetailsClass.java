package com.apnagodam.staff.activity.in.truckbook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.CreatePricingSetPostData;
import com.apnagodam.staff.Network.Request.UploadTruckDetailsPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.pricing.InPricingListingActivity;
import com.apnagodam.staff.activity.lead.LeadGenerateClass;
import com.apnagodam.staff.databinding.ActivitySetPricingBinding;
import com.apnagodam.staff.databinding.ActivityUploadDetailsBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.RangeTimePickerDialog;
import com.apnagodam.staff.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TruckUploadDetailsClass extends BaseActivity<ActivityUploadDetailsBinding> implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    String UserName, vehhicleNO, CaseID = "";
    private Calendar calender;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_upload_details;
    }

    @Override
    protected void setUp() {
        calender = Calendar.getInstance();
        Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle != null) {
            UserName = bundle.getString("user_name");
            //  vehhicleNO = bundle.getString("vehicle_no");
            CaseID = bundle.getString("case_id");
        }
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        clickListner();
        binding.customerName.setText(UserName);
        binding.caseId.setText(CaseID);

    }


    private void clickListner() {
        binding.ivClose.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.etStartDateTime.setOnClickListener(this);
        binding.lpCommiteDate.setOnClickListener(this);
        binding.etEndDateTime.setOnClickListener(this);
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
            case R.id.et_start_date_time:
                popUpDatePicker();
                break;
            case R.id.lp_commite_date:
                popUpDatePicker();
                break;
            case R.id.lp_end_date:
                EnddatePicker();
                break;
            case R.id.et_end_date_time:
                EnddatePicker();
                break;
            case R.id.btn_login:
                if (isValid()) {
                    if (TextUtils.isEmpty(stringFromView(binding.etStartDateTime))) {
                        Toast.makeText(this, getResources().getString(R.string.start_date_time_val), Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(stringFromView(binding.etEndDateTime))) {
                        Toast.makeText(this, getResources().getString(R.string.end_date_time_val), Toast.LENGTH_LONG).show();
                    } else {
                        apiService.uploadTruckDetails(new UploadTruckDetailsPostData(
                                CaseID, stringFromView(binding.etTransporterName), stringFromView(binding.etVehicleNo),
                                stringFromView(binding.etDriverName), stringFromView(binding.etDriverPhoneNo), stringFromView(binding.etMinWeight),
                                stringFromView(binding.etMaxWeight), stringFromView(binding.etTurnaroundTime), stringFromView(binding.etTotalWeight),
                                stringFromView(binding.etBags), stringFromView(binding.etTotalTransCost), stringFromView(binding.etAdvancePatyment), stringFromView(binding.etStartDateTime),
                                stringFromView(binding.etFinalSettalementAmount), stringFromView(binding.etEndDateTime), stringFromView(binding.notes))).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                            @Override
                            protected void onSuccess(LoginResponse body) {
                                Toast.makeText(TruckUploadDetailsClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                                startActivityAndClear(TruckBookListingActivity.class);
                            }
                        });
                    }
                }
                break;
        }
    }

    boolean isValid() {
        if (TextUtils.isEmpty(stringFromView(binding.etTransporterName))) {
            return Utility.showEditTextError(binding.tilTransporterName, R.string.transporter_name);
        } else if (TextUtils.isEmpty(stringFromView(binding.etVehicleNo))) {
            return Utility.showEditTextError(binding.tilVehicleNo, R.string.vehicle_no);
        } else if (TextUtils.isEmpty(stringFromView(binding.etDriverName))) {
            return Utility.showEditTextError(binding.tilDriverName, R.string.driver_name_validation);
        } else if (TextUtils.isEmpty(stringFromView(binding.etDriverPhoneNo))) {
            return Utility.showEditTextError(binding.tilDriverPhoneNo, R.string.driver_phone_no_enter);
        } else if (TextUtils.isEmpty(stringFromView(binding.etTransportRate))) {
            return Utility.showEditTextError(binding.tilTransportRate, R.string.transport_rate_validation);
        } else if (TextUtils.isEmpty(stringFromView(binding.etMinWeight))) {
            return Utility.showEditTextError(binding.tilMinWeight, R.string.min_weight_val);
        } else if (TextUtils.isEmpty(stringFromView(binding.etMaxWeight))) {
            return Utility.showEditTextError(binding.tilMaxWeight, R.string.max_weight_val);
        } else if (TextUtils.isEmpty(stringFromView(binding.etTurnaroundTime))) {
            return Utility.showEditTextError(binding.tilTurnaroundTime, R.string.turnaround_time_val);
        } else if (TextUtils.isEmpty(stringFromView(binding.etTotalWeight))) {
            return Utility.showEditTextError(binding.tilTotalWeight, R.string.total_weight_val);
        } else if (TextUtils.isEmpty(stringFromView(binding.etBags))) {
            return Utility.showEditTextError(binding.tilBags, R.string.bags_val);
        } else if (TextUtils.isEmpty(stringFromView(binding.etAdvancePatyment))) {
            return Utility.showEditTextError(binding.tilAdvancePatyment, R.string.advance_patyment_val);
        } else if (TextUtils.isEmpty(stringFromView(binding.etTotalTransCost))) {
            return Utility.showEditTextError(binding.tilTotalTransCost, R.string.total_trans_cost_val);
        } else if (TextUtils.isEmpty(stringFromView(binding.etFinalSettalementAmount))) {
            return Utility.showEditTextError(binding.tilFinalSettalementAmount, R.string.settleememt_amount_val);
        } /*else if (TextUtils.isEmpty(stringFromView(binding.etStartDateTime))) {
            return Utility.showEditTextError(binding.tilStartDateTime, R.string.start_date_time_val);
        } else if (TextUtils.isEmpty(stringFromView(binding.etEndDateTime))) {
            return Utility.showEditTextError(binding.tilEndDateTime, R.string.end_date_time_val);
        }*/
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
            StarttimePicker(sdf.format(calender.getTime()).toString());
        }
    };

    private void StarttimePicker(final String date) {
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                binding.etStartDateTime.setText(date + " - " + selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    public void EnddatePicker() {
        DatePickerDialog dateDialog = new DatePickerDialog(this, dateeend, calender
                .get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH));
        dateDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dateDialog.show();
    }

    DatePickerDialog.OnDateSetListener dateeend = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calender.set(Calendar.YEAR, year);
            calender.set(Calendar.MONTH, monthOfYear);
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            binding.etEndDateTime.setText(sdf.format(calender.getTime()).toString());
            EndtimePicker(sdf.format(calender.getTime()).toString());
        }
    };

    private void EndtimePicker(final String date) {
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                binding.etEndDateTime.setText(date + " - " + selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
