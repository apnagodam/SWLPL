package com.apnagodam.staff.activity.in.first_kantaparchi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.KanthaParchiUploadBinding;
import com.apnagodam.staff.utils.Utility;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UploadFirstkantaParchiClass extends BaseActivity<KanthaParchiUploadBinding> {

    // role of image

    String UserName, CaseID = "";
    public File fileKantha, fileTruck;
    boolean firstKanthaFile = false;
    boolean truckImage = false;

    String selectedVehicleType = null, selectedFuelType = null;

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

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileKantha == null) {
                    Toast.makeText(getApplicationContext(), R.string.upload_kanta_parchi_file, Toast.LENGTH_LONG).show();
                } else if (fileTruck == null) {
                    Toast.makeText(getApplicationContext(), R.string.upload_truck_image, Toast.LENGTH_LONG).show();
                } else {
                    onNext();
                }

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

        Pix.start(UploadFirstkantaParchiClass.this, options.setRequestCode(REQUEST_CAMERA_PICTURE));
    }


    // update LP profile
    public void onNext() {
        String KanthaImage = "", truckImageImage = "";
        if (fileKantha != null) {
            KanthaImage = "" + Utility.transferImageToBase64(fileKantha);
        }
        if (fileTruck != null) {
            truckImageImage = "" + Utility.transferImageToBase64(fileTruck);
        } else {
           /* apiService.buildFarmerProfile(new UpdateKYCPostData(
                    selectedVehicleType, stringFromView(binding.etVehicleMinCapicity), stringFromView(binding.etVehicleMaxCapicity),
                    stringFromView(binding.etVehicleMilage), selectedFuelType,
                    hiddenPassport, hiddenadhar, hiddenpancard, hiddendl, hiddenrc, hiddeninsurance, hiddenpassbook)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                @Override
                protected void onSuccess(LoginResponse body) {
                    Toast.makeText(LogisticPartnerProfileActivity.this, body.getMessage(), Toast.LENGTH_LONG).show();
                    startActivity(LogisticPartnerDashboardScreen.class);
                }
            });*/
        }
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
                    binding.KanthaImage.setImageURI(uri);
                } else if (truckImage) {
                    firstKanthaFile = false;
                    truckImage = false;
                    fileTruck = new File(compressImage(returnValue.get(0).toString()));
                    Uri uri = Uri.fromFile(fileTruck);
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
