package com.apnagodam.staff.activity.out.s_katha_parchi;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.UploadSecoundkantaParchiPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.secound_kanthaparchi.SecoundkanthaParchiListingActivity;
import com.apnagodam.staff.activity.out.s_quaility_report.OutSecoundQualityReportListingActivity;
import com.apnagodam.staff.activity.out.s_quaility_report.OutUploadSecoundQualtityReportsClass;
import com.apnagodam.staff.databinding.KanthaParchiUploadBinding;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import java.io.File;
import java.util.ArrayList;

public class OutUploadSecoundkantaParchiClass extends BaseActivity<KanthaParchiUploadBinding> {
    public File fileKantha, fileTruck;
    // role of image
    String UserName, CaseID = "";
    boolean firstKanthaFile = false;
    boolean truckImage = false;
    Options options;
    private String firstkantaParchiFile, TruckImage;

    @Override
    protected int getLayoutResId() {
        return R.layout.kantha_parchi_upload;
    }


    @Override
    protected void setUp() {
        Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle != null) {
            UserName = bundle.getString("user_name");
            CaseID = bundle.getString("case_id");
        }
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.customerName.setText(UserName);
        binding.caseId.setText(CaseID);
        clickListner();
    }

    private void clickListner() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(OutSecoundkanthaParchiListingActivity.class);
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showDecisionDialog(OutUploadSecoundkantaParchiClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        if (fileKantha == null) {
                            Toast.makeText(getApplicationContext(), R.string.upload_kanta_parchi_file, Toast.LENGTH_LONG).show();
                        } else if (fileTruck == null) {
                            Toast.makeText(getApplicationContext(), R.string.upload_truck_image, Toast.LENGTH_LONG).show();
                        } else {
                            onNext();
                        }
                    }
                });
            }
        });
        binding.uploadKantha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstKanthaFile = true;
                truckImage = false;
                onImageSelected();
              //  callImageSelector(REQUEST_CAMERA);
            }
        });
        binding.uploadTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstKanthaFile = false;
                truckImage = true;
                onImageSelected();
              //  callImageSelector(REQUEST_CAMERA);
            }
        });
        binding.KanthaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(OutUploadSecoundkantaParchiClass.this, R.layout.popup_photo_full, view, firstkantaParchiFile, null);
            }
        });
        binding.TruckImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(OutUploadSecoundkantaParchiClass.this, R.layout.popup_photo_full, view, TruckImage, null);
            }
        });

    }

   /* private void callImageSelector(int requestCamera) {
        options = Options.init()
                .setRequestCode(requestCamera)                                                 //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setExcludeVideos(false)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/apnagodam/lp/images");                                       //Custom Path For media Storage
        Pix.start(OutUploadSecoundkantaParchiClass.this, options);
    }
*/

    // update file
    public void onNext() {
        String KanthaImage = "", truckImageImage = "";
        if (fileKantha != null) {
            KanthaImage = "" + Utility.transferImageToBase64(fileKantha);
        }
        if (fileTruck != null) {
            truckImageImage = "" + Utility.transferImageToBase64(fileTruck);
        }
        //else {
        apiService.uploadSecoundkantaParchi(new UploadSecoundkantaParchiPostData(CaseID, stringFromView(binding.notes), KanthaImage, truckImageImage)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
            @Override
            protected void onSuccess(LoginResponse body) {
                Utility.showAlertDialog(OutUploadSecoundkantaParchiClass.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        startActivityAndClear(OutSecoundkanthaParchiListingActivity.class);
                    }
                });
            }
        });
        // }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(OutSecoundkanthaParchiListingActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_CAMERA_PICTURE) {
                    if (this.camUri != null) {
                        if (firstKanthaFile) {
                            firstKanthaFile = false;
                            truckImage = false;
                            fileKantha = new File(compressImage(this.camUri.getPath().toString()));
                            Uri uri = Uri.fromFile(fileKantha);
                            firstkantaParchiFile = String.valueOf(uri);
                            binding.KanthaImage.setImageURI(uri);
                        }else if (truckImage){
                            firstKanthaFile = false;
                            truckImage = false;
                            fileTruck = new File(compressImage(this.camUri.getPath().toString()));
                            Uri uri = Uri.fromFile(fileTruck);
                            TruckImage = String.valueOf(uri);
                            binding.TruckImage.setImageURI(uri);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }
  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra(Pix.IMAGE_RESULTS)) {
                    ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    assert returnValue != null;
                    Log.e("getImageesValue", returnValue.get(0));
                    if (requestCode == REQUEST_CAMERA) {
                        if (firstKanthaFile) {
                            firstKanthaFile = false;
                            truckImage = false;
                            fileKantha = new File(compressImage(returnValue.get(0)));
                            Uri uri = Uri.fromFile(fileKantha);
                            firstkantaParchiFile = String.valueOf(uri);
                            binding.KanthaImage.setImageURI(uri);
                        } else if (truckImage) {
                            firstKanthaFile = false;
                            truckImage = false;
                            fileTruck = new File(compressImage(returnValue.get(0)));
                            Uri uri = Uri.fromFile(fileTruck);
                            TruckImage = String.valueOf(uri);
                            binding.TruckImage.setImageURI(uri);
                        }
                    }
                }
            }
        }
    }*/

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, options);
                } else {
                    callImageSelector(REQUEST_CAMERA);
                    Toast.makeText(this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }*/
}
