package com.apnagodam.staff.interfaces;

/**
 * Created on : july 29, 2020
 * Author     : raju singh
 */
public interface OtpReceivedInterface {
  void onOtpReceived(String otp);
  void onOtpTimeout();
  void onOTPReceivedError(String error);
}
