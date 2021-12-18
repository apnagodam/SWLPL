package com.apnagodam.staff.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.apnagodam.staff.utils.SmsBroadcastReceiver;
import com.apnagodam.staff.utils.Utility;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpActivity extends BaseActivity<ActivityOtpBinding>{
   // private SMSReceiver smsReceiver;
   String mobileNumbewr,empID,setting = "";
    public int counter;
    private OTPvarifedResponse userInfo;

    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    @Override
    protected void onStart() {
        super.onStart();
        // isDashboardRunning = true;
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            binding.etOtpNumber.setText(matcher.group(0));
        }
    }

    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }

                    @Override
                    public void onFailure() {

                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }
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
       /* smsReceiver = new SMSReceiver();
        smsReceiver.setOTPListener((SMSReceiver.OTPReceiveListener) this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        this.registerReceiver(smsReceiver, intentFilter);*/
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
                startSMSListener();

            }
        });
    }
    public void startSMSListener() {
        try {
            SmsRetrieverClient client = SmsRetriever.getClient(this);
            client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    }


   /* @Override
    public void onOTPReceived(String otp) {
      //  Toast.makeText(this, "Otp Received " + otp, Toast.LENGTH_LONG).show();
        binding.etOtpNumber.setText(otp);
        *//*if (isValid()) {
            /// apply otp varifed api here
            varifedOTP();
        }*//*
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

    }*/
}
