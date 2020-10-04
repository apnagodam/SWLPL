package com.apnagodam.staff.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.LoginPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.ActivityLoginBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.CommudityResponse;
import com.apnagodam.staff.module.TerminalResponse;
import com.apnagodam.staff.reciever.SMSReceiver;
import com.apnagodam.staff.smsOtp.AppSignatureHashHelper;
import com.apnagodam.staff.utils.LocationUtils;
import com.apnagodam.staff.utils.Utility;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class LoginActivity extends BaseActivity<ActivityLoginBinding>
{
    private SMSReceiver smsReceiver;
    private int RESOLVE_HINT = 2;
    private static final int LANGUAGE_SELECT = 977;
    GoogleApiClient mGoogleApiClient;
    private LocationUtils locationUtils;
    private int REQUEST_CODE = 1000;
    private String lat, Long;
    ACTIVITY_STATE currentState = ACTIVITY_STATE.INITIAL;
    public static final String TAG = LoginActivity.class.getSimpleName();
    private String settingScreen="";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    public void getHintPhoneNumber() {
        HintRequest hintRequest =
                new HintRequest.Builder()
                        .setPhoneNumberIdentifierSupported(true)
                        .build();
        PendingIntent mIntent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(mIntent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result if we want hint number
      /*  if (requestCode == RESOLVE_HINT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    // credential.getId();  <-- will need to process phone number string
                    binding.etPhoneNumber.setText(Utility.getCorrectFormatMobileNo(credential.getId()));
                }
            }
        } else*/
            if (requestCode == REQUEST_CODE) {
            locationUtils.startLocationUpdates();
          /*  binding.etPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        getHintPhoneNumber();
                    }
                }
            });*/
        } else if (requestCode == LANGUAGE_SELECT) {
            if (resultCode == RESULT_OK) recreate();
            else finish();
        }
    }

    @Override
    protected void setUp() {
        //from settings screen
        Intent intent = getIntent();
        settingScreen = intent.getExtras().getString("setting");
        if (settingScreen.equalsIgnoreCase("changeMobileNumber")) {
            binding.tvBack.setText(" ");
        }
        // This code requires one time to get Hash keys do comment and share key
      /*  AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);
        Log.i(TAG, "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));*/
        // get mobile number from phone
        //getHintPhoneNumber();
        // get latitude and getLongitude of farmer
        locationUtils = new LocationUtils(getActivity(), 1000, 2000, (location, address) -> {
            //   showToast(location.getLatitude() + " : " + location.getLongitude());
            Log.d("location", "" + location.getLatitude() + "," + "" + location.getLongitude());
            lat = "" + location.getLatitude();
            Long = "" + location.getLongitude();
            LocationUtils.LocationAddress locationAddress = new LocationUtils.LocationAddress();
            String geolocation = String.valueOf(locationAddress.getAddressFromLocation(Double.parseDouble(lat),Double.parseDouble(Long), LoginActivity.this));
            Log.e("geolocationLogin", " " + geolocation);
        }, REQUEST_CODE);

        //for again going language screen
        if (settingScreen.equalsIgnoreCase("changeMobileNumber")) {
            binding.tvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } else {
            binding.tvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fofrlang();
                }
            });
        }
        binding.btnLogin.setOnClickListener(v -> getLogin());

    }

    private void fofrlang() {
        startActivityForResult(new Intent(LoginActivity.this, LanguageActivity.class)
                .putExtra(LanguageActivity.class.getSimpleName(), LanguageActivity.CALLED_FROM.FROM_SPLASH), LANGUAGE_SELECT);
    }

    @Override
    public void onBackPressed() {
       finish();
    }

    void getLogin() {
        if (isValid()) {
           // startActivity(VLCDashboardScreen.class);
            // Call server API for requesting OTP and when you got success start
             login();
        }
    }

    void login() {
        if (Utility.isNetworkAvailable(this)) {
            SharedPreferencesRepository.getDataManagerInstance().savelat(lat);
            SharedPreferencesRepository.getDataManagerInstance().savelong(Long);
            apiService.doLogin(new LoginPostData((stringFromView(binding.etPhoneNumber)), "Emp")).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                @Override
                protected void onSuccess(LoginResponse body) {
                    Toast.makeText(LoginActivity.this, body.getMessage(), Toast.LENGTH_LONG).show();
                    // SMS Listener for listing auto read message lsitner
                    startSMSListener(body.getPhone());

                }
            });
        }
        else {
            Utility.showAlertDialog(this, this.getString(R.string.alert), this.getString(R.string.no_internet_connection));
        }
    }

    public void startSMSListener(String phone) {
        try {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Bundle bundle = new Bundle();
                bundle.putString("mobile", phone);
                bundle.putString("empID", (stringFromView(binding.etPhoneNumber)));
                bundle.putString("setting", settingScreen);
                startActivity(OtpActivity.class, bundle);
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Fail to start API
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    boolean isValid() {
        if (TextUtils.isEmpty(stringFromView(binding.etPhoneNumber))) {
            return Utility.showEditTextError(binding.tilPhoneNumber, R.string.emp_id);
        } else if (!(stringFromView(binding.etPhoneNumber).length()<7)) {
            return Utility.showEditTextError(binding.tilPhoneNumber, R.string.error_validateempChar);
        }
        return true;
    }


    enum ACTIVITY_STATE {
        INITIAL, VERIFY_OTP, LOGIN_WITH_PASSWORD
    }

}
