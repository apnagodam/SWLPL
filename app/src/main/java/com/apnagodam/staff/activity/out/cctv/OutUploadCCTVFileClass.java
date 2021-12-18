package com.apnagodam.staff.activity.out.cctv;

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
import com.apnagodam.staff.Network.Request.UploadCCTVPostData;
import com.apnagodam.staff.Network.Request.UploadFirstkantaParchiPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.cctv.INUploadCCTVFileClass;
import com.apnagodam.staff.activity.out.f_katha_parchi.OutFirstkanthaParchiListingActivity;
import com.apnagodam.staff.databinding.CctvUploadBinding;
import com.apnagodam.staff.databinding.KanthaParchiUploadBinding;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import java.io.File;
import java.util.ArrayList;


public class OutUploadCCTVFileClass extends BaseActivity<CctvUploadBinding> {

    public File fileCCTV, fileCCTV2;
    // role of image
    String UserName, CaseID = "";
    boolean firstFileCCTV = false;
    boolean secoundFileCCTV = false;
    Options options;
    private String firstcctvfileValue, secoundcctvfileValue;

    @Override
    protected int getLayoutResId() {
        return R.layout.cctv_upload;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(OutCCTVListingActivity.class);
    }

    private void clickListner() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(OutCCTVListingActivity.class);
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showDecisionDialog(OutUploadCCTVFileClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        if (fileCCTV == null) {
                            Toast.makeText(getApplicationContext(),"Upload CCTV File 1", Toast.LENGTH_LONG).show();
                        } else if (fileCCTV2 == null) {
                            Toast.makeText(getApplicationContext(),"Upload CCTV File 2", Toast.LENGTH_LONG).show();
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
                firstFileCCTV = true;
                secoundFileCCTV = false;
                //   onImageSelected();
                callImageSelector(REQUEST_CAMERA);
            }
        });
        binding.uploadTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstFileCCTV = false;
                secoundFileCCTV = true;
                //  onImageSelected();
                callImageSelector(REQUEST_CAMERA);
            }
        });
        binding.KanthaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstcctvfileValue==null){

                }else {
                    new PhotoFullPopupWindow(OutUploadCCTVFileClass.this, R.layout.popup_photo_full, view, firstcctvfileValue, null);
                }

            }
        });
        binding.TruckImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (secoundcctvfileValue==null){

                }else {
                    new PhotoFullPopupWindow(OutUploadCCTVFileClass.this, R.layout.popup_photo_full, view, secoundcctvfileValue, null);
                }
            }
        });
    }
    private void callImageSelector(int requestCamera) {
        options = Options.init()
                .setRequestCode(requestCamera)                                                 //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setExcludeVideos(false)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(30)                                   //Option to exclude videos
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/apnagodam/lp/images");                                       //Custom Path For media Storage
        Pix.start(OutUploadCCTVFileClass.this, options);
    }
   /* private void callImageSelector(int requestCamera) {
        options = Options.init()
                .setRequestCode(requestCamera)                                                 //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setExcludeVideos(false)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(30)                                   //Option to exclude videos
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/apnagodam/lp/images");                                       //Custom Path For media Storage
        Pix.start(OutUploadFirrstkantaParchiClass.this, options);
    }*/


    // update file
    public void onNext() {
        String KanthaImage = "", secoundFileCCTVImage = "";
        if (fileCCTV != null) {
            KanthaImage = "" + Utility.transferImageToBase64(fileCCTV);
        }
        if (fileCCTV2 != null) {
            secoundFileCCTVImage = "" + Utility.transferImageToBase64(fileCCTV2);
        }
        //else {
        apiService.uploadCCTVFile(new UploadCCTVPostData(CaseID, stringFromView(binding.notes), KanthaImage, secoundFileCCTVImage)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
            @Override
            protected void onSuccess(LoginResponse body) {
                Utility.showAlertDialog(OutUploadCCTVFileClass.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        startActivityAndClear(OutCCTVListingActivity.class);
                    }
                });
            }
        });
        // }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_CAMERA_PICTURE) {
                    if (this.camUri != null) {
                        if (firstFileCCTV) {
                            firstFileCCTV = false;
                            secoundFileCCTV = false;
                            fileCCTV = new File(compressImage(this.camUri.getPath().toString()));
                            Uri uri = Uri.fromFile(fileCCTV);
                            firstcctvfileValue = String.valueOf(uri);
                            binding.KanthaImage.setImageURI(uri);
                        }else if (secoundFileCCTV){
                            firstFileCCTV = false;
                            secoundFileCCTV = false;
                            fileCCTV2 = new File(compressImage(this.camUri.getPath().toString()));
                            Uri uri = Uri.fromFile(fileCCTV2);
                            secoundcctvfileValue = String.valueOf(uri);
                            binding.TruckImage.setImageURI(uri);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra(Pix.IMAGE_RESULTS)) {
                    ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    assert returnValue != null;
                    Log.e("getImageesValue", returnValue.get(0));
                    if (requestCode == REQUEST_CAMERA) {
                        if (firstFileCCTV) {
                            firstFileCCTV = false;
                            secoundFileCCTV = false;
                            fileCCTV = new File(compressImage(returnValue.get(0)));
                            Uri uri = Uri.fromFile(fileCCTV);
                            firstcctvfileValue = String.valueOf(uri);
                            binding.KanthaImage.setImageURI(uri);
                        } else if (secoundFileCCTV) {
                            firstFileCCTV = false;
                            secoundFileCCTV = false;
                            fileCCTV2 = new File(compressImage(returnValue.get(0)));
                            Uri uri = Uri.fromFile(fileCCTV2);
                            secoundcctvfileValue = String.valueOf(uri);
                            binding.TruckImage.setImageURI(uri);
                        }
                    }
                }
            }
        }
    }

    @Override
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
    }
}
