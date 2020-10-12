package com.apnagodam.staff.activity.in.secound_kanthaparchi;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.UploadFirstkantaParchiPostData;
import com.apnagodam.staff.Network.Request.UploadSecoundkantaParchiPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.first_kantaparchi.FirstkanthaParchiListingActivity;
import com.apnagodam.staff.activity.in.labourbook.LabourBookUploadClass;
import com.apnagodam.staff.databinding.KanthaParchiUploadBinding;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import java.io.File;
import java.util.ArrayList;

public class UploadSecoundkantaParchiClass extends BaseActivity<KanthaParchiUploadBinding> {
    // role of image
    String UserName, CaseID = "";
    public File fileKantha, fileTruck;
    boolean firstKanthaFile = false;
    boolean truckImage = false;
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
                startActivityAndClear(SecoundkanthaParchiListingActivity.class);
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showDecisionDialog(UploadSecoundkantaParchiClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
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
                callImageSelector();
            }
        });
        binding.uploadTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstKanthaFile = false;
                truckImage = true;
                callImageSelector();
            }
        });
        binding.KanthaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(UploadSecoundkantaParchiClass.this, R.layout.popup_photo_full, view, firstkantaParchiFile, null);
            }
        });
        binding.TruckImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(UploadSecoundkantaParchiClass.this, R.layout.popup_photo_full, view, TruckImage, null);
            }
        });

    }

    private void callImageSelector() {
        Options options = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setExcludeVideos(true)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/apnagodam/lp/images");                                       //Custom Path For media Storage

        Pix.start(UploadSecoundkantaParchiClass.this, options.setRequestCode(REQUEST_CAMERA_PICTURE));
    }


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
                    Toast.makeText(UploadSecoundkantaParchiClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                    startActivityAndClear(SecoundkanthaParchiListingActivity.class);
                }
            });
       // }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            assert returnValue != null;
            Log.e("getImageesValue", returnValue.get(0).toString());
            if (requestCode == REQUEST_CAMERA_PICTURE) {
                if (firstKanthaFile) {
                    firstKanthaFile = false;
                    truckImage = false;
                    fileKantha = new File(compressImage(returnValue.get(0).toString()));
                    Uri uri = Uri.fromFile(fileKantha);
                    firstkantaParchiFile = String.valueOf(uri);
                    binding.KanthaImage.setImageURI(uri);
                } else if (truckImage) {
                    firstKanthaFile = false;
                    truckImage = false;
                    fileTruck = new File(compressImage(returnValue.get(0).toString()));
                    Uri uri = Uri.fromFile(fileTruck);
                    TruckImage = String.valueOf(uri);
                    binding.TruckImage.setImageURI(uri);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, Options.init().setRequestCode(100));
                } else {
                    Toast.makeText(this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
