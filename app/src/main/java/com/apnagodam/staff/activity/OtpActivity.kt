package com.apnagodam.staff.activity

import android.annotation.TargetApi
import android.content.IntentFilter
import android.os.Build
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.LoginPostData
import com.apnagodam.staff.Network.Request.OTPData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.Response.OTPvarifedResponse
import com.apnagodam.staff.Network.viewmodel.LoginViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.databinding.ActivityOtpBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.reciever.SMSReceiver
import com.apnagodam.staff.reciever.SMSReceiver.OTPReceiveListener
import com.apnagodam.staff.utils.Utility
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class OtpActivity : BaseActivity<ActivityOtpBinding?>(), OTPReceiveListener {
    private var smsReceiver: SMSReceiver? = null
    var mobileNumbewr: String? = null
    var empID: String? = null
    var setting: String? = ""
    var counter = 0
    private var userInfo: OTPvarifedResponse? = null

    private val loginViewModel by viewModels<LoginViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_otp
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun setUp() {
        val bundle = intent.getBundleExtra(BUNDLE)
        // To retrieve object in second Activity
        if (bundle != null) {
            mobileNumbewr = bundle.getString("mobile")
            setting = bundle.getString("setting")
            empID = bundle.getString("empID")
        }
        smsReceiver = SMSReceiver()
        smsReceiver!!.setOTPListener(this as OTPReceiveListener)
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        this.registerReceiver(smsReceiver, intentFilter, RECEIVER_NOT_EXPORTED)
        binding!!.txtHead.text = this.getString(R.string.hint_otp) + " :- " + mobileNumbewr

        binding!!.btnVerfyOtp.setOnClickListener { v: View? -> getvarifedOtp() }
        binding!!.btnResendOtp.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                binding!!.etOtpNumber.setText(null)
                rersendOTP
                binding!!.timer.visibility = View.VISIBLE
                countDownTime()
            }
        })
        countDownTime()
        binding!!.etOtpNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length > 5) getvarifedOtp()
                /* if (getvarifedOtp()){

                }
                if (s.toString().equals("1234")) {
                    Toast.makeText(OtpActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } else if (s.length() == "1234".length()) {
                    Toast.makeText(OtpActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
                    binding.etOtpNumber.setText(null);
                }*/
            }
        })
    }

    private fun countDownTime() {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding!!.timer.text = "0.$counter sec"
                counter++
            }

            override fun onFinish() {
                binding!!.btnResendOtp.visibility = View.VISIBLE
                binding!!.timer.visibility = View.GONE
            }
        }.start()
    }

    private fun getvarifedOtp() {
        if (isValid) {
            /// apply otp varifed api here
            varifedOTP()
        }
    }

    private val rersendOTP: Unit
        private get() {
            /// apply resend otp api here
            ResendOTP()
        }

    private fun ResendOTP() {
        loginViewModel.doLogin(LoginPostData(empID, "Emp"))
        loginViewModel.response.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(this@OtpActivity, it.data!!.message, Toast.LENGTH_LONG).show()
                    hideDialog()
                }

                is NetworkResult.Loading -> {
                    showDialog()
                }

                is NetworkResult.Success -> {
                   if(it.data!=null){
                       Toast.makeText(this@OtpActivity, it.data!!.message, Toast.LENGTH_LONG).show()
                   }

                }
            }
        }
    }

    val isValid: Boolean
        get() = if (TextUtils.isEmpty(stringFromView(binding!!.etOtpNumber))) {
            Utility.showEditTextError(binding!!.tilOtpNumber, R.string.error_validateotp)
        } else true

    fun varifedOTP() {
        loginViewModel.dpVerifyOtp(OTPData(stringFromView(binding!!.etOtpNumber), mobileNumbewr))
        loginViewModel.Otpresponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> hideDialog()
                is NetworkResult.Loading -> showDialog()
                is NetworkResult.Success -> {
                   if(it.data!=null){
                       userInfo = it.data
                       SharedPreferencesRepository.saveSessionToken(it.data!!.accessToken)
                       SharedPreferencesRepository.getDataManagerInstance().bankNameList = it.data.banks
                       SharedPreferencesRepository.getDataManagerInstance().saveUserData(it.data.userDetails)
                       Toast.makeText(this@OtpActivity, it.data.message, Toast.LENGTH_LONG).show()
                       SharedPreferencesRepository.getDataManagerInstance().setIsLoggedIn(true)
                       if (setting.equals("changeMobileNumber", ignoreCase = true)) {
                           startActivityAndClear(SettingActivity::class.java)
                       } else {
                           // Toast.makeText(OtpActivity.this, "Coming Soon....", Toast.LENGTH_LONG).show();
                           startActivityAndClear(StaffDashBoardActivity::class.java)
                       }
                   }
                }
            }
        }

    }

    private val allPermission: Unit
        private get() {
//
//        apiService.getPermission(userInfo.getUserDetails().getRole_id(),userInfo.getUserDetails().getLevel_id()).enqueue(new NetworkCallback<AllUserPermissionsResultListResponse>(getActivity()) {
//            @Override
//            protected void onSuccess(AllUserPermissionsResultListResponse body) {
//                if (body.getUserPermissionsResult() == null || body.getUserPermissionsResult().isEmpty()) {
//
//                } else {
//                    SharedPreferencesRepository.getDataManagerInstance().saveUserPermissionData(body.getUserPermissionsResult());
//                    if (setting.equalsIgnoreCase("changeMobileNumber")) {
//                        startActivityAndClear(SettingActivity.class);
//                    }
//                    else {
//                        // Toast.makeText(OtpActivity.this, "Coming Soon....", Toast.LENGTH_LONG).show();
//                        startActivityAndClear(StaffDashBoardActivity.class);
//                    }
//                }
//            }
//        });
        }

    override fun onDestroy() {
        super.onDestroy()
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver)
        }
    }

    override fun onOTPReceived(otp: String) {
        //  Toast.makeText(this, "Otp Received " + otp, Toast.LENGTH_LONG).show();
        binding!!.etOtpNumber.setText(otp)
        /*if (isValid()) {
            /// apply otp varifed api here
            varifedOTP();
        }*/if (smsReceiver != null) {
            unregisterReceiver(smsReceiver)
            smsReceiver = null
        }
    }

    override fun onOTPTimeOut() {
        Toast.makeText(this, "Time out, please resend", Toast.LENGTH_LONG).show()
        binding!!.btnResendOtp.visibility = View.VISIBLE
        binding!!.timer.visibility = View.GONE
    }

    override fun onOTPReceivedError(error: String) {}
}
