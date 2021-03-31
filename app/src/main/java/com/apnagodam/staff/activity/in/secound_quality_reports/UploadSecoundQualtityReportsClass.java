package com.apnagodam.staff.activity.in.secound_quality_reports;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.UploadSecoundQualityPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.ActivityUpdateQualityReportBinding;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import java.io.File;
import java.util.ArrayList;

public class UploadSecoundQualtityReportsClass extends BaseActivity<ActivityUpdateQualityReportBinding> {
    String packagingTypeID = null;
    // role of image
    String UserName, CaseID = "";
    public File fileReport, fileCommudity;
    boolean ReportsFileSelect = false;
    boolean CommudityFileSelect = false;
    private String reportFile, commudityFile;
    Options options;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_update_quality_report;
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
        // spinner purpose
        binding.rlpackgingType.setVisibility(View.GONE);
        binding.spinnerPackagingtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    packagingTypeID = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // can leave this empty
            }
        });
    }

    private void clickListner() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(SecoundQualityReportListingActivity.class);
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showDecisionDialog(UploadSecoundQualtityReportsClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        if (isValid()) {

                   /* if (fileReport == null) {
                        Toast.makeText(getApplicationContext(), R.string.upload_reports_file, Toast.LENGTH_LONG).show();
                    } else if (fileCommudity == null) {
                        Toast.makeText(getApplicationContext(), R.string.upload_commodity_file, Toast.LENGTH_LONG).show();
                    } else if (packagingTypeID == null) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.packaging), Toast.LENGTH_LONG).show();
                    } else {*/
                            onNext();
                            // }
                        }
                    }
                });
            }
        });
        binding.uploadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportsFileSelect = true;
                CommudityFileSelect = false;
                callImageSelector(REQUEST_CAMERA);
            }
        });
        binding.uploadCommudity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportsFileSelect = false;
                CommudityFileSelect = true;
                callImageSelector(REQUEST_CAMERA);
            }
        });
        binding.ReportsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(UploadSecoundQualtityReportsClass.this, R.layout.popup_photo_full, view, reportFile, null);
            }
        });
        binding.CommudityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(UploadSecoundQualtityReportsClass.this, R.layout.popup_photo_full, view, commudityFile, null);
            }
        });

    }

    private void callImageSelector(int requestCamera) {
        options = Options.init()
                .setRequestCode(requestCamera)                                                 //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setExcludeVideos(false)                                            //Option to exclude videos
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/apnagodam/lp/images");                                       //Custom Path For media Storage
        Pix.start(UploadSecoundQualtityReportsClass.this, options);
    }


    // update file
    public void onNext() {
        String KanthaImage = "", CommudityFileSelectImage = "";
        if (fileReport != null) {
            KanthaImage = "" + Utility.transferImageToBase64(fileReport);
        }
        if (fileCommudity != null) {
            CommudityFileSelectImage = "" + Utility.transferImageToBase64(fileCommudity);
        }
        //else {
        apiService.uploadSecoundQualityReports(new UploadSecoundQualityPostData(CaseID, KanthaImage, stringFromView(binding.etMoistureLevel), stringFromView(binding.etTcw), stringFromView(binding.etFmLevel), stringFromView(binding.etThin)
                , stringFromView(binding.etDehuck), stringFromView(binding.etDiscolor), stringFromView(binding.etBroken), stringFromView(binding.etInfested)
                , stringFromView(binding.etLive), stringFromView(binding.notes), CommudityFileSelectImage)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
            @Override
            protected void onSuccess(LoginResponse body) {
                Utility.showAlertDialog(UploadSecoundQualtityReportsClass.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        startActivityAndClear(SecoundQualityReportListingActivity.class);
                    }
                });
            }
        });
        // }
    }

    boolean isValid() {
        if (TextUtils.isEmpty(stringFromView(binding.etMoistureLevel))) {
            return Utility.showEditTextError(binding.tilMoistureLevel, R.string.moisture_level);
        }
       /* else if (TextUtils.isEmpty(stringFromView(binding.etTcw))) {
            return Utility.showEditTextError(binding.tilTcw, R.string.tcw);
        } else if (TextUtils.isEmpty(stringFromView(binding.etBroken))) {
            return Utility.showEditTextError(binding.tilBroken, R.string.broken);
        } else if (TextUtils.isEmpty(stringFromView(binding.etFmLevel))) {
            return Utility.showEditTextError(binding.tilFmLevel, R.string.fm_level);
        } else if (TextUtils.isEmpty(stringFromView(binding.etThin))) {
            return Utility.showEditTextError(binding.tilThin, R.string.thin);
        } else if (TextUtils.isEmpty(stringFromView(binding.etDehuck))) {
            return Utility.showEditTextError(binding.tilDehuck, R.string.dehuck);
        } else if (TextUtils.isEmpty(stringFromView(binding.etDiscolor))) {
            return Utility.showEditTextError(binding.tilDiscolor, R.string.discolor);
        } else if (TextUtils.isEmpty(stringFromView(binding.etInfested))) {
            return Utility.showEditTextError(binding.tilInfested, R.string.infested);
        } else if (TextUtils.isEmpty(stringFromView(binding.etLive))) {
            return Utility.showEditTextError(binding.tilLive, R.string.live_count);
        }*/
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra(Pix.IMAGE_RESULTS)) {
                    ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    assert returnValue != null;
                    Log.e("getImageesValue", returnValue.get(0).toString());
                    if (requestCode == REQUEST_CAMERA) {
                        if (ReportsFileSelect) {
                            ReportsFileSelect = false;
                            CommudityFileSelect = false;
                            fileReport = new File(compressImage(returnValue.get(0).toString()));
                            Uri uri = Uri.fromFile(fileReport);
                            reportFile = String.valueOf(uri);
                            binding.ReportsImage.setImageURI(uri);
                        } else if (CommudityFileSelect) {
                            ReportsFileSelect = false;
                            CommudityFileSelect = false;
                            fileCommudity = new File(compressImage(returnValue.get(0).toString()));
                            Uri uri = Uri.fromFile(fileCommudity);
                            commudityFile = String.valueOf(uri);
                            binding.CommudityImage.setImageURI(uri);
                        }
                    }
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
                    Pix.start(this, options);
                } else {
                    callImageSelector(REQUEST_CAMERA);
                    Toast.makeText(this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(SecoundQualityReportListingActivity.class);
    }
}
