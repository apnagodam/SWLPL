package com.apnagodam.staff.smsOtp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.apnagodam.staff.interfaces.OtpReceivedInterface;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

/**
 * Created on : july 29, 2020
 * Author     : raju
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {
  private static final String TAG = "SmsBroadcastReceiver";
  OtpReceivedInterface otpReceiveInterface = null;
  SmsBroadcastReceiverListener smsBroadcastReceiverListener;
  public interface SmsBroadcastReceiverListener {
    void onSuccess(Intent intent);
    void onFailure();
  }
  public void setOnOtpListeners(OtpReceivedInterface otpReceiveInterface) {
    this.otpReceiveInterface = otpReceiveInterface;
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(TAG, "onReceive: ");
    if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
      Bundle extras = intent.getExtras();
      Status mStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

      switch (mStatus.getStatusCode()) {
        case CommonStatusCodes.SUCCESS:
          // Get SMS message contents'
          String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
          Log.d(TAG, "onReceive: failure " + message);
          if (otpReceiveInterface != null) {
           //  message = "<#> Your ApnaGodam OTP is: 123456 AZ+6cTRzJEG";

            String otp = message.replace("<#> Your ApnaGodam OTP is: ", "").split(" ")[0];

            Log.e("otp: ", otp);
            String otpMessage =  message.replace("<#> Your ApnaGodam OTP is: ", "").split(" ")[0];
            otpReceiveInterface.onOtpReceived(otpMessage);
          }
          break;
        case CommonStatusCodes.TIMEOUT:
          // Waiting for SMS timed out (5 minutes)
          Log.d(TAG, "onReceive: failure");
          if (otpReceiveInterface != null) {
            otpReceiveInterface.onOtpTimeout();
          }
          break;
        case CommonStatusCodes.API_NOT_CONNECTED:

          if (otpReceiveInterface != null) {
            otpReceiveInterface.onOTPReceivedError("API NOT CONNECTED");
          }

          break;

        case CommonStatusCodes.NETWORK_ERROR:

          if (otpReceiveInterface != null) {
            otpReceiveInterface.onOTPReceivedError("NETWORK ERROR");
          }

          break;

        case CommonStatusCodes.ERROR:

          if (otpReceiveInterface != null) {
            otpReceiveInterface.onOTPReceivedError("SOME THING WENT WRONG");
          }
          break;
      }
    }
  }
}