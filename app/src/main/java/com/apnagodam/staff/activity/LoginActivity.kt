package com.apnagodam.staff.activity

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.LoginPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.viewmodel.LoginViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityLoginBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.reciever.SMSReceiver
import com.apnagodam.staff.utils.LocationUtils
import com.apnagodam.staff.utils.Utility
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
@AndroidEntryPoint
class LoginActivity() : BaseActivity<ActivityLoginBinding?>() {
    private var smsReceiver: SMSReceiver? = null
    private var RESOLVE_HINT = 2
    var mGoogleApiClient: GoogleApiClient? = null
    private var locationUtils: LocationUtils? = null
    private var REQUEST_CODE = 1000
    private var lat: String? = null
    private var Long: String? = null
    private var settingScreen = ""
    val loginViewModel : LoginViewModel by viewModels<LoginViewModel>()
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            locationUtils!!.startLocationUpdates()
        } else if (requestCode == LANGUAGE_SELECT) {
            if (resultCode == RESULT_OK) recreate() else finish()
        }
    }


    override fun getLayoutResId(): Int {
        return  R.layout.activity_login
    }

     override fun setUp() {
         binding!!.btnLogin.setOnClickListener { v -> login }


     }

    private fun fofrlang() {
        startActivityForResult(
            Intent(this@LoginActivity, LanguageActivity::class.java)
                .putExtra(
                    LanguageActivity::class.java.simpleName,
                    LanguageActivity.CALLED_FROM.FROM_SPLASH
                ), LANGUAGE_SELECT
        )
    }


    val login: Unit
        get() {
            if (isValid) {
                login()
            }
        }

    fun login() {
        if (Utility.isNetworkAvailable(this)) {
            var sharedPrefences = SharedPreferencesRepository.getDataManagerInstance();
            sharedPrefences.savelat(lat)
            sharedPrefences.savelong(Long)

            loginViewModel.doLogin(  LoginPostData(
                    stringFromView(binding!!.etPhoneNumber).toString(),
                    "Emp"
            ))
            loginViewModel.response.observe(this)
            {
                when(it){
                    is NetworkResult.Error -> {}
                    is NetworkResult.Loading ->{
                        showDialog()
                    }
                    is NetworkResult.Success ->{

                        hideDialog()
                        Toast.makeText(this@LoginActivity, it.data!!.getMessage(), Toast.LENGTH_LONG)
                                .show()
                        // SMS Listener for listing auto read message lsitner
                        startSMSListener(it.data.getPhone())
                    }
                }
            }


        } else {
            Utility.showAlertDialog(
                this,
                this.getString(R.string.alert),
                this.getString(R.string.no_internet_connection)
            )
        }
    }

    fun startSMSListener(phone: String?) {
        try {
            val client: SmsRetrieverClient = SmsRetriever.getClient(this)
            val task: Task<Void> = client.startSmsRetriever()

            task.addOnSuccessListener {
                val bundle = Bundle()
                bundle.putString("mobile", phone)
                bundle.putString("empID", stringFromView(binding!!.etPhoneNumber))
                bundle.putString("setting", settingScreen)
                startActivity(OtpActivity::class.java, bundle)
            }.addOnFailureListener {
                it.printStackTrace()
                val bundle = Bundle()
                bundle.putString("mobile", phone)
                bundle.putString("empID", stringFromView(binding!!.etPhoneNumber))
                bundle.putString("setting", settingScreen)
                startActivity(OtpActivity::class.java, bundle)
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val isValid: Boolean
        get() {
            if (TextUtils.isEmpty(stringFromView(binding!!.etPhoneNumber))) {
                return Utility.showEditTextError(binding!!.tilPhoneNumber, R.string.emp_id)
            } else if (stringFromView(binding!!.etPhoneNumber).length >= 7) {
                return Utility.showEditTextError(
                    binding!!.tilPhoneNumber,
                    R.string.error_validateempChar
                )
            }
            return true
        }

    internal enum class ACTIVITY_STATE {
        INITIAL,
        VERIFY_OTP,
        LOGIN_WITH_PASSWORD
    }

    companion object {
        private const val LANGUAGE_SELECT = 977
        val TAG = LoginActivity::class.java.simpleName
    }
}
