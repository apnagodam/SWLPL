package com.apnagodam.staff.activity.in.gatepass;

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
import com.apnagodam.staff.Network.Request.UploadGatePassPostData;
import com.apnagodam.staff.Network.Request.UploadSecoundQualityPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.first_quality_reports.FirstQualityReportListingActivity;
import com.apnagodam.staff.activity.in.secound_quality_reports.SecoundQualityReportListingActivity;
import com.apnagodam.staff.databinding.ActivityGatePassBinding;
import com.apnagodam.staff.databinding.ActivityUpdateQualityReportBinding;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import java.io.File;
import java.util.ArrayList;

public class UploadGatePassClass extends BaseActivity<ActivityGatePassBinding> {

    // role of image
    String UserName, CaseID = "";
    public File fileGatePass;
    boolean GatePassFileSelect = false;
    private String GatePassFile;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_gate_pass;
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
                startActivityAndClear(GatePassListingActivity.class);
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    if (fileGatePass == null) {
                        Toast.makeText(getApplicationContext(), R.string.upload_gatepass_file, Toast.LENGTH_LONG).show();
                    } else {
                        onNext();
                    }
                }
            }
        });
        binding.uploadGatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GatePassFileSelect = true;
                callImageSelector();
            }
        });

        binding.GatePassImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(UploadGatePassClass.this, R.layout.popup_photo_full, view, GatePassFile, null);
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

        Pix.start(UploadGatePassClass.this, options.setRequestCode(REQUEST_CAMERA_PICTURE));
    }
    // update file
    public void onNext() {
        String gatePassImage = "";
        if (fileGatePass != null) {
            gatePassImage = "" + Utility.transferImageToBase64(fileGatePass);
        }
        apiService.uploadGatePass(new UploadGatePassPostData(CaseID, gatePassImage, stringFromView(binding.etWeightKg), stringFromView(binding.etBags), stringFromView(binding.notes))).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
            @Override
            protected void onSuccess(LoginResponse body) {
                Toast.makeText(UploadGatePassClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                startActivityAndClear(GatePassListingActivity.class);
            }
        });
    }

    boolean isValid() {
        if (TextUtils.isEmpty(stringFromView(binding.etWeightKg))) {
            return Utility.showEditTextError(binding.tilWeightKg, R.string.weight_kg);
        } else if (TextUtils.isEmpty(stringFromView(binding.etBags))) {
            return Utility.showEditTextError(binding.tilBags, R.string.bags_gatepass);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            assert returnValue != null;
            Log.e("getImageesValue", returnValue.get(0).toString());
            if (requestCode == REQUEST_CAMERA_PICTURE) {
                if (GatePassFileSelect) {
                    GatePassFileSelect = false;
                    fileGatePass = new File(compressImage(returnValue.get(0).toString()));
                    Uri uri = Uri.fromFile(fileGatePass);
                    GatePassFile = String.valueOf(uri);
                    binding.GatePassImage.setImageURI(uri);
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
