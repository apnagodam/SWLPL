package com.apnagodam.staff.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.apnagodam.staff.ApnaGodamApp;
import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.BuildConfig;
import com.apnagodam.staff.Network.NetworkCallbackWProgress;
import com.apnagodam.staff.Network.Response.VersionCodeResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.ActivitySplashBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.CommudityResponse;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.Utility;


public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    String market_uri = "https://play.google.com/store/apps/details?id=com.apnagodam.staff&hl=en";
    private static final int REQUEST_CAMERA_CODE = 120;

    @Override
    protected int getLayoutResId() {
        Utility.changeLanguage(this, SharedPreferencesRepository.getDataManagerInstance().getSelectedLanguage());
        return R.layout.activity_splash;
    }

    @Override
    protected void setUp() {
        //getappVersion();
        getCommditityList();
        // nextMClass();
    }

    private void nextMClass() {
        if (requestPermissions()) {
            afterpermissionNext();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_CODE
            );
        }

    }

    private void afterpermissionNext() {
        UserDetails userDetails = SharedPreferencesRepository.getDataManagerInstance().getUser();
        boolean name = SharedPreferencesRepository.getDataManagerInstance().isUSerName();
        if (name) {
            startActivityAndClear(StaffDashBoardActivity.class);
        } else if (userDetails != null && (userDetails.getFname() != null) && (!userDetails.getFname().isEmpty())) {
            startActivityAndClear(StaffDashBoardActivity.class);
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.putExtra("setting", "");
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }
    }

    private Boolean requestPermissions() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writeExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        );
        int readExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE
        );
        return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalPermission == PackageManager.PERMISSION_GRANTED
                && readExternalPermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions();
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]))
                                return;
                        } else {
                            //  requestPermissions();
                            afterpermissionNext();
                        }
                    }
                    // open dialog
                }
        }
    }
   /* private void nextMClass() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserDetails userDetails = SharedPreferencesRepository.getDataManagerInstance().getUser();
                boolean name = SharedPreferencesRepository.getDataManagerInstance().isUSerName();
                if (name) {
                    startActivityAndClear(StaffDashBoardActivity.class);
                } else if (userDetails!=null&&(userDetails.getFname() != null) && (!userDetails.getFname().isEmpty())) {
                    startActivityAndClear(StaffDashBoardActivity.class);
                }  else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.putExtra("setting", "");
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }
            }
        }, 3000);
    }*/

    private void getappVersion() {
        apiService.getversionCode("Emp").enqueue(new NetworkCallbackWProgress<VersionCodeResponse>(getActivity()) {
            @Override
            protected void onSuccess(VersionCodeResponse body) {
                if (BuildConfig.APPLICATION_ID != null) {
                    if (Integer.parseInt(body.getVersion()) > (BuildConfig.VERSION_CODE)) {
                        showUpdateDialogue();
                    } else {
                        getCommditityList();
                    }
                }
            }
        });
    }

    private void showUpdateDialogue() {
        Dialog dialogue = new Dialog(this);
        dialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogue.setContentView(R.layout.dialogue_update_apk);
        dialogue.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        dialogue.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogue.setCancelable(false);
        dialogue.setCanceledOnTouchOutside(false);
        dialogue.setTitle(null);
        ApnaGodamApp.getApp().showUpdate = false;
        Button updateBtn = dialogue.findViewById(R.id.btn_update);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(market_uri));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                        .FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        ContextCompat.checkSelfPermission(
                                SplashActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        SplashActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(SplashActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            5);
                } else {
                    dialogue.dismiss();
                    ApnaGodamApp.getInstance().showUpdate = false;
                    downloadApk();
                }*/
            }
        });
        dialogue.show();
    }

    private void getCommditityList() {
        showDialog();
        apiService.getcommuydity_terminal_user_emp_listing("Emp").enqueue(new NetworkCallbackWProgress<CommudityResponse>(getActivity()) {
            @Override
            protected void onSuccess(CommudityResponse body) {
                if (BuildConfig.APPLICATION_ID != null) {
                    if (Integer.parseInt(body.getVersion()) > (BuildConfig.VERSION_CODE)) {
                        showUpdateDialogue();
                    } else {
//                for (int i = 0; i < body.getCategories().size(); i++) {
                        SharedPreferencesRepository.getDataManagerInstance().setCommdity(body.getCategories());
//                }
//                for (int i = 0; i < body.getTerminals().size(); i++) {
                      //  SharedPreferencesRepository.getDataManagerInstance().SETTerminal(body.getTerminals());
//                }
//                for (int i = 0; i < body.getEmployee().size(); i++) {
                        SharedPreferencesRepository.getDataManagerInstance().setEmployee(body.getEmployee());
//                }
//                for (int i = 0; i < body.getUsers().size(); i++) {
                      //  SharedPreferencesRepository.getDataManagerInstance().setUser(body.getUsers());
//                }
                        SharedPreferencesRepository.getDataManagerInstance().setContractor(body.getContractor_result());
                        hideDialog();
                        nextMClass();
                    }
                }
            }
        });
    }


}
