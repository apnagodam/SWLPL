package com.apnagodam.staff.activity.out.deliveryorder;

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
import com.apnagodam.staff.Network.Request.UploadReleaseOrderlsPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.ActivityUploadReleaseOrderBinding;
import com.apnagodam.staff.utils.Utility;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import java.io.File;
import java.util.ArrayList;

public class OUTDeliverdUpoladOrderClass extends BaseActivity<ActivityUploadReleaseOrderBinding> implements View.OnClickListener {
    String UserName, CaseID = "";
    public File fileBiltyImage;
    boolean BiltyImageFile = false;
    private String BiltyFile;
    Options options;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_upload_release_order;
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
        clickListner();
        binding.customerName.setText(UserName);
        binding.caseId.setText(CaseID);
        binding.headfile.setText("Delivered Order File");
        binding.headfileselect.setText("Click Here To Upload to Delivered Order File");
        binding.heading.setText("Update Delivered Order");
    }

    private void clickListner() {
        binding.ivClose.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.uploadTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiltyImageFile = true;
                callImageSelector(REQUEST_CAMERA);
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
        Pix.start(OUTDeliverdUpoladOrderClass.this, options);
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
                        if (BiltyImageFile) {
                            BiltyImageFile = false;
                            fileBiltyImage = new File(compressImage(returnValue.get(0).toString()));
                            Uri uri = Uri.fromFile(fileBiltyImage);
                            BiltyFile = String.valueOf(uri);
                            binding.TruckImage.setImageURI(uri);
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
        startActivityAndClear(OUTDeliverdOrderListingActivity.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                startActivityAndClear(OUTDeliverdOrderListingActivity.class);
                break;
            case R.id.btn_login:
                if (binding.notes.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OUTDeliverdUpoladOrderClass.this, "Enter Notes Here!!", Toast.LENGTH_LONG).show();
                } else if (fileBiltyImage == null) {
                    Toast.makeText(OUTDeliverdUpoladOrderClass.this, "Upload to Delivered Order File", Toast.LENGTH_LONG).show();
                } else {
                    Utility.showDecisionDialog(OUTDeliverdUpoladOrderClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                        @Override
                        public void callback() {
                            callApi();
                        }
                    });
                }

                break;
        }
    }

    private void callApi() {
        String BiltyImage = "";
        if (fileBiltyImage != null) {
            BiltyImage = "" + Utility.transferImageToBase64(fileBiltyImage);
        }
        apiService.uploadDeliveredOrder(new UploadReleaseOrderlsPostData(CaseID, stringFromView(binding.notes), "",BiltyImage)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
            @Override
            protected void onSuccess(LoginResponse body) {
                Utility.showAlertDialog(OUTDeliverdUpoladOrderClass.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        startActivityAndClear(OUTDeliverdOrderListingActivity.class);
                    }
                });
            }
        });
    }
}
