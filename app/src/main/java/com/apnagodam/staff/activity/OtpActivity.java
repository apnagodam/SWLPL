package com.apnagodam.staff.activity;

import android.annotation.TargetApi;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.LoginPostData;
import com.apnagodam.staff.Network.Request.OTPData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.Network.Response.OTPvarifedResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.truckbook.TruckBookListingActivity;
import com.apnagodam.staff.adapter.TruckBookAdapter;
import com.apnagodam.staff.databinding.ActivityOtpBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.AllTruckBookListResponse;
import com.apnagodam.staff.module.AllUserPermissionsResultListResponse;
import com.apnagodam.staff.reciever.SMSReceiver;
import com.apnagodam.staff.utils.Utility;
import com.google.android.gms.auth.api.phone.SmsRetriever;

public class OtpActivity extends BaseActivity<ActivityOtpBinding> implements SMSReceiver.OTPReceiveListener {
    private SMSReceiver smsReceiver;
    String mobileNumbewr,empID,setting = "";
    public int counter;
    private OTPvarifedResponse userInfo;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_otp;
    }
    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void setUp() {
        Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        // To retrieve object in second Activity
        if (bundle != null) {
            mobileNumbewr = bundle.getString("mobile");
            setting = bundle.getString("setting");
            empID = bundle.getString("empID");
        }
        binding.txtHead.setText(this.getString(R.string.hint_otp)+" :- " + mobileNumbewr);
        smsReceiver = new SMSReceiver();
        smsReceiver.setOTPListener((SMSReceiver.OTPReceiveListener) this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        this.registerReceiver(smsReceiver, intentFilter);
        binding.btnVerfyOtp.setOnClickListener(v -> getvarifedOtp());
        binding.btnResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etOtpNumber.setText(null);
                getRersendOTP();
                binding.timer.setVisibility(View.VISIBLE);
                countDownTime();
            }
        });
        countDownTime();
        binding.etOtpNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>5)
                    getvarifedOtp();
               /* if (getvarifedOtp()){

                }
                if (s.toString().equals("1234")) {
                    Toast.makeText(OtpActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } else if (s.length() == "1234".length()) {
                    Toast.makeText(OtpActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
                    binding.etOtpNumber.setText(null);
                }*/
            }
        });
    }

    private void countDownTime() {
        new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText("0."+String.valueOf(counter)+" sec");
                counter++;
            }
            @Override
            public void onFinish() {
                binding.btnResendOtp.setVisibility(View.VISIBLE);
                binding.timer.setVisibility(View.GONE);
            }
        }.start();
    }

    private void getvarifedOtp() {
        if (isValid()) {
            /// apply otp varifed api here
            varifedOTP();
        }
    }

    private void getRersendOTP() {
        /// apply resend otp api here
        ResendOTP();
    }

    private void ResendOTP() {
        apiService.doLogin(new LoginPostData(empID, "Emp")).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
            @Override
            protected void onSuccess(LoginResponse body) {
                Toast.makeText(OtpActivity.this, body.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    boolean isValid() {
        if (TextUtils.isEmpty(stringFromView(binding.etOtpNumber))) {
            return Utility.showEditTextError(binding.tilOtpNumber, R.string.error_validateotp);
        }
        return true;
    }

    void varifedOTP() {
        apiService.doOTPVerify(new OTPData(stringFromView(binding.etOtpNumber), mobileNumbewr)).enqueue(new NetworkCallback<OTPvarifedResponse>(getActivity()) {
            @Override
            protected void onSuccess(OTPvarifedResponse body) {
                userInfo =body;
                SharedPreferencesRepository.getDataManagerInstance().saveSessionToken(body.getAccessToken());
                SharedPreferencesRepository.getDataManagerInstance().setBankNameList(body.getBanks());
                SharedPreferencesRepository.getDataManagerInstance().saveUserData(body.getUserDetails());
                Toast.makeText(OtpActivity.this, body.getMessage(), Toast.LENGTH_LONG).show();
                SharedPreferencesRepository.getDataManagerInstance().setIsLoggedIn(true);
                if (setting.equalsIgnoreCase("changeMobileNumber")) {
                    startActivityAndClear(SettingActivity.class);
                }
                else {
                    // Toast.makeText(OtpActivity.this, "Coming Soon....", Toast.LENGTH_LONG).show();
                    startActivityAndClear(StaffDashBoardActivity.class);
                }
               // getAllPermission();
            }
        });
    }
    private void getAllPermission() {
        apiService.getPermission(userInfo.getUserDetails().getRole_id(),userInfo.getUserDetails().getLevel_id()).enqueue(new NetworkCallback<AllUserPermissionsResultListResponse>(getActivity()) {
            @Override
            protected void onSuccess(AllUserPermissionsResultListResponse body) {
                if (body.getUserPermissionsResult() == null || body.getUserPermissionsResult().isEmpty()) {

                } else {
                    SharedPreferencesRepository.getDataManagerInstance().saveUserPermissionData(body.getUserPermissionsResult());
                    if (setting.equalsIgnoreCase("changeMobileNumber")) {
                        startActivityAndClear(SettingActivity.class);
                    }
                    else {
                        // Toast.makeText(OtpActivity.this, "Coming Soon....", Toast.LENGTH_LONG).show();
                        startActivityAndClear(StaffDashBoardActivity.class);
                    }
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }


    @Override
    public void onOTPReceived(String otp) {
      //  Toast.makeText(this, "Otp Received " + otp, Toast.LENGTH_LONG).show();
        binding.etOtpNumber.setText(otp);
        /*if (isValid()) {
            /// apply otp varifed api here
            varifedOTP();
        }*/
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
            smsReceiver = null;
        }
    }

    @Override
    public void onOTPTimeOut() {
        Toast.makeText(this, "Time out, please resend", Toast.LENGTH_LONG).show();
        binding.btnResendOtp.setVisibility(View.VISIBLE);
        binding.timer.setVisibility(View.GONE);
    }

    @Override
    public void onOTPReceivedError(String error) {

    }
}
