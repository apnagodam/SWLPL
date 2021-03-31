package com.apnagodam.staff.activity.in.gatepass;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.NetworkCallbackWProgress;
import com.apnagodam.staff.Network.Request.DharmaKanthaPostData;
import com.apnagodam.staff.Network.Request.OTPGatePassData;
import com.apnagodam.staff.Network.Request.OTPVerifyGatePassData;
import com.apnagodam.staff.Network.Request.UploadGatePassPostDataNew;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.ActivityGatePassBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.CheckInventory;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadGatePassClass extends BaseActivity<ActivityGatePassBinding> implements RadioGroup.OnCheckedChangeListener {

    // role of image
    String UserName, CaseID = "", TerminalNmae, VehicleNo, stackNo, inout;
    public File fileGatePass;
    boolean GatePassFileSelect = false;
    private String GatePassFile;
    Options options;
    String InTrackType = "null";
    String InTrackID = "0";
    String InBardhanaType = "null";
    String InBardhanaID = "0";
    ArrayAdapter<String> SpinnerKanthaAdapter;
    String kanthaID = null;
    List<String> kanthaName;
    List<DharmaKanthaPostData.Datum> data;
    private int TotalBag;
    private Double TOtalWeight;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_gate_pass;
    }

    @Override
    protected void setUp() {
        kanthaName = new ArrayList<>();
        kanthaName.add("Select Dharam Kanta");
        Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle != null) {
            UserName = bundle.getString("user_name");
            CaseID = bundle.getString("case_id");
            TerminalNmae = bundle.getString("terminal_name");
            VehicleNo = bundle.getString("vehicle_no");
            stackNo = bundle.getString("stackNo");
            inout = bundle.getString("INOUT");
        }
        showDialog();
        getKanthaList();
        if (inout.equalsIgnoreCase("OUT")) {
            apiService.checkWeightBag(CaseID).enqueue(new NetworkCallbackWProgress<CheckInventory>(getActivity()) {
                @Override
                protected void onSuccess(CheckInventory body) {
                    try {
                        TotalBag = body.getBagsTotal();
                        TOtalWeight = body.getWeightTotal();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
            });
        } else {

        }

        binding.etWeightKg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etWeightKg.getText().toString().trim() != null && !binding.etWeightKg.getText().toString().trim().isEmpty() && inout.equalsIgnoreCase("OUT")) {
                        Double weight = (Double.parseDouble(binding.etWeightKg.getText().toString().trim()));
                        if (weight > TOtalWeight) {
                            Utility.showAlertDialog(UploadGatePassClass.this, getString(R.string.alert), "Weight must be Less or equal to Total weight !!! ", new Utility.AlertCallback() {
                                @Override
                                public void callback() {
                                    binding.etWeightKg.setText("");
                                }
                            });
                        } else {

                        }
                    }
                } else {
                }
            }
        });
        binding.etBags.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etBags.getText().toString().trim() != null && !binding.etBags.getText().toString().trim().isEmpty() && inout.equalsIgnoreCase("OUT")) {
                        Double bags = (Double.parseDouble(binding.etBags.getText().toString().trim()));
                        if (bags > TotalBag) {
                            Utility.showAlertDialog(UploadGatePassClass.this, getString(R.string.alert), "Bags must be Less or equal to Total Bags !!! ", new Utility.AlertCallback() {
                                @Override
                                public void callback() {
                                    binding.etBags.setText("");
                                }
                            });
                        } else {

                        }
                    }
                } else {
                }
            }
        });
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.customerName.setText("Customer Name:-  " + UserName);
        binding.caseId.setText("Case ID:- " + CaseID);
        binding.terminalName.setText("Terminal Name:- " + TerminalNmae);
        binding.vehicleNo.setText("Vehicle No:- " + VehicleNo);
        binding.etStackNo.setText(" " + stackNo);
        clickListner();
        binding.etWeightKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0 && !charSequence.equals("")) {
                    double finalWeightQTl = Double.parseDouble(charSequence.toString().trim()) / 100;
                    binding.etWeightQt.setText("" + finalWeightQTl);
                    //binding.etWeightKg.setText("" + Utility.round(Double.parseDouble(charSequence.toString().trim()), 2));
                } else {
                    binding.etWeightQt.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.etBags.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0 && !charSequence.equals("")) {
                    if (binding.etWeightKg.getText().toString().trim() != null && !binding.etWeightKg.getText().toString().trim().isEmpty()) {
                        double wrightkg = Double.parseDouble(binding.etWeightKg.getText().toString().trim());
                        double bags = Double.parseDouble(binding.etBags.getText().toString().trim());
                        double finalWeightQTl = wrightkg / bags;
                        finalWeightQTl = Utility.round(finalWeightQTl,2);
                        binding.etAvgWeight.setText("" + finalWeightQTl);
                    }

                } else {
                    binding.etAvgWeight.setText("0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.etWeightKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0 && !charSequence.equals("")) {
                    if (binding.etBags.getText().toString().trim() != null && !binding.etBags.getText().toString().trim().isEmpty()) {
                        double wrightkg = Double.parseDouble(binding.etWeightKg.getText().toString().trim());
                        double bags = Double.parseDouble(binding.etBags.getText().toString().trim());
                        double finalWeightQTl = wrightkg / bags;
                         finalWeightQTl = Utility.round(finalWeightQTl,2);
                        binding.etAvgWeight.setText("" + Utility.round(finalWeightQTl, 2));
                     //   binding.etWeightKg.setText("" + Utility.round(Double.parseDouble(charSequence.toString().trim()), 2));
                    }else {
                      //  binding.etWeightKg.setText("" + Utility.round(Double.parseDouble(charSequence.toString().trim()), 2));
                    }

                } else {
                    binding.etAvgWeight.setText("0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // layout Terminal Listing resource and list of items.
        SpinnerKanthaAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, kanthaName) {
            //By using this method we will define how
            // the text appears before clicking a spinner
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            //By using this method we will define
            //how the listview appears after clicking a spinner
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        SpinnerKanthaAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerDharma.setAdapter(SpinnerKanthaAdapter);
        binding.spinnerDharma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    String presentMeterStatusID = parentView.getItemAtPosition(position).toString();
                    for (int i = 0; i < data.size(); i++) {
                        if (presentMeterStatusID.contains(data.get(i).getName())) {
                            kanthaID = String.valueOf(data.get(i).getId());
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void clickListner() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(GatePassListingActivity.class);
            }
        });
        binding.sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOTPValid()) {
                    if (Utility.isNetworkAvailable(UploadGatePassClass.this)) {
                        if ((stringFromView(binding.sendOtp)).contains("Verify")) {
                            if (TextUtils.isEmpty(stringFromView(binding.etOtp))) {
                                Toast.makeText(getApplicationContext(), "please Enter Mobile Number OTP Here!", Toast.LENGTH_LONG).show();
                            } else {
                                apiService.doVerifyGatePassOTP(new OTPVerifyGatePassData("" + CaseID, (stringFromView(binding.etOtp)))).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                                    @Override
                                    protected void onSuccess(LoginResponse body) {
                                        Utility.showAlertDialog(UploadGatePassClass.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                                            @Override
                                            public void callback() {
                                                binding.ll.setVisibility(View.GONE);
                                                binding.etDriverPhone.setEnabled(false);
                                                binding.etDriverPhone.setClickable(false);
                                                binding.etDriverPhone.setFocusable(false);
                                                binding.etOldDriverName.setEnabled(false);
                                                binding.etOldDriverName.setClickable(false);
                                                binding.etOldDriverName.setFocusable(false);
                                                binding.btnSubmit.setVisibility(View.VISIBLE);
                                                binding.etOldDriverName.setBackgroundColor(getResources().getColor(R.color.lightgray));
                                                binding.etDriverPhone.setBackgroundColor(getResources().getColor(R.color.lightgray));
                                            }
                                        });
                                    }
                                });
                            }
                        } else {
                            UserDetails userDetails = SharedPreferencesRepository.getDataManagerInstance().getUser();
                            if ((stringFromView(binding.etDriverPhone)).equalsIgnoreCase(userDetails.getPhone())) {
                                Toast.makeText(getApplicationContext(), "please Enter Valid Mobile Number", Toast.LENGTH_LONG).show();
                            } else if ((stringFromView(binding.etDriverPhone)).length() < 10 || (stringFromView(binding.etDriverPhone)).length() > 10) {
                                Toast.makeText(getApplicationContext(), "please Enter Valid Mobile Number", Toast.LENGTH_LONG).show();
                            } else {
                                apiService.doGatePassOTP(new OTPGatePassData(stringFromView(binding.etDriverPhone), CaseID)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                                    @Override
                                    protected void onSuccess(LoginResponse body) {
                                        Utility.showAlertDialog(UploadGatePassClass.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                                            @Override
                                            public void callback() {
                                                binding.tilOtp.setVisibility(View.VISIBLE);
                                                binding.reSendOtp.setVisibility(View.VISIBLE);
                                                binding.sendOtp.setText("Verify OTP");
                                                binding.etDriverPhone.setBackgroundColor(getResources().getColor(R.color.lightgray));
                                                binding.etOldDriverName.setBackgroundColor(getResources().getColor(R.color.lightgray));
                                                binding.etDriverPhone.setEnabled(false);
                                                binding.etDriverPhone.setClickable(false);
                                                binding.etDriverPhone.setFocusable(false);
                                                binding.etOldDriverName.setEnabled(false);
                                                binding.etOldDriverName.setClickable(false);
                                                binding.etOldDriverName.setFocusable(false);
                                            }
                                        });

                                    }
                                });
                            }
                        }
                    } else {
                        Utility.showAlertDialog(UploadGatePassClass.this, getString(R.string.alert), getString(R.string.no_internet_connection));
                    }
                }
            }
        });
        binding.reSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOTPValid()) {
                    if (Utility.isNetworkAvailable(UploadGatePassClass.this)) {
                        apiService.doGatePassOTP(new OTPGatePassData((stringFromView(binding.etDriverPhone)), CaseID)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                            @Override
                            protected void onSuccess(LoginResponse body) {
                                Toast.makeText(UploadGatePassClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                                binding.etOtp.setVisibility(View.VISIBLE);
                                binding.reSendOtp.setVisibility(View.VISIBLE);
                                binding.sendOtp.setText("Verify OTP");
                                binding.etDriverPhone.setBackgroundColor(getResources().getColor(R.color.lightgray));
                                binding.etOldDriverName.setBackgroundColor(getResources().getColor(R.color.lightgray));
                                binding.etDriverPhone.setEnabled(false);
                                binding.etDriverPhone.setClickable(false);
                                binding.etDriverPhone.setFocusable(false);
                                binding.etOldDriverName.setEnabled(false);
                                binding.etOldDriverName.setClickable(false);
                                binding.etOldDriverName.setFocusable(false);

                            }
                        });
                    } else {
                        Utility.showAlertDialog(UploadGatePassClass.this, getString(R.string.alert), getString(R.string.no_internet_connection));
                    }
                }
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    Utility.showDecisionDialog(UploadGatePassClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                        @Override
                        public void callback() {
                            if (InTrackType == null) {
                                Toast.makeText(getApplicationContext(), "क्या Inward में, ट्रक रस्सा एवं त्रिपाल होकर आया ?", Toast.LENGTH_LONG).show();
                            } else if (InBardhanaType == null) {
                                Toast.makeText(getApplicationContext(), "क्या Inward में, कुछ बोरिया भीगी हुई थी ?", Toast.LENGTH_LONG).show();
                            }
                            /*if (fileGatePass == null) {
                                Toast.makeText(getApplicationContext(), R.string.upload_gatepass_file, Toast.LENGTH_LONG).show();
                            }*/
                            else {
                                onNext();
                            }
                        }
                    });
                }
            }
        });
        binding.uploadGatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GatePassFileSelect = true;
                callImageSelector(REQUEST_CAMERA);
            }
        });
        binding.GatePassImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(UploadGatePassClass.this, R.layout.popup_photo_full, view, GatePassFile, null);
            }
        });
    }

    private void callImageSelector(int requestCamera) {
        options = Options.init()
                .setRequestCode(requestCamera)                                           //Request code for activity results
                .setCount(1)
                .setFrontfacing(false)
                .setExcludeVideos(false)                                   //Option to exclude videos
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientation
                .setPath("/apnagodam/lp/images");                                       //Custom Path For media Storage
        Pix.start(UploadGatePassClass.this, options);
    }

    private void getKanthaList() {
        apiService.getDharmaKanthaListLevel().enqueue(new NetworkCallback<DharmaKanthaPostData>(getActivity()) {
            @Override
            protected void onSuccess(DharmaKanthaPostData body) {
                data = body.getData();
                for (int i = 0; i < data.size(); i++) {
                    kanthaName.add(data.get(i).getName());
                }
            }
        });
    }

    // update file
    public void onNext() {
       String Weightkg =  "" + Utility.round(Double.parseDouble(stringFromView(binding.etWeightKg)), 2);
        apiService.uploadGatePassNew(new UploadGatePassPostDataNew(CaseID, Weightkg, stringFromView(binding.etBags), stringFromView(binding.notes)
                , stringFromView(binding.etStackNo), stringFromView(binding.etTransportotName), stringFromView(binding.etTranPhone), stringFromView(binding.etAvgWeight)
                , stringFromView(binding.etKathaParchiNo), InTrackID, InBardhanaID, stringFromView(binding.etOldKathaParchiNo), stringFromView(binding.etOldTotalWeight)
                , stringFromView(binding.etOldOriginalWeight), stringFromView(binding.etOldKanthaName), stringFromView(binding.etOldDriverName), stringFromView(binding.etDriverPhone), kanthaID)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
            @Override
            protected void onSuccess(LoginResponse body) {
                Utility.showAlertDialog(UploadGatePassClass.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        startActivityAndClear(GatePassListingActivity.class);
                    }
                });
            }
        });
    }

    boolean isOTPValid() {
        if (TextUtils.isEmpty(stringFromView(binding.etOldDriverName))) {
            return Utility.showEditTextError(binding.tilOldDriverName, "Enter Driver Name");
        } else if (TextUtils.isEmpty(stringFromView(binding.etDriverPhone))) {
            return Utility.showEditTextError(binding.tilDriverPhone, "Enter Driver Phone");
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(GatePassListingActivity.class);
    }

    boolean isValid() {
       /* if (TextUtils.isEmpty(stringFromView(binding.etStackNo))) {
            return Utility.showEditTextError(binding.tilStackNo, "Enter Stack No");
        } else */
        if (TextUtils.isEmpty(stringFromView(binding.etWeightKg))) {
            return Utility.showEditTextError(binding.tilWeightKg, R.string.weight_kg);
        } else if (TextUtils.isEmpty(stringFromView(binding.etBags))) {
            return Utility.showEditTextError(binding.tilBags, R.string.bags_gatepass);
        } else if (TextUtils.isEmpty(stringFromView(binding.etTransportotName))) {
            return Utility.showEditTextError(binding.tilTransportotName, "Enter Transporter Name");
        } else if (TextUtils.isEmpty(stringFromView(binding.etTranPhone))) {
            return Utility.showEditTextError(binding.tilTranPhone, "Enter Transporter Phone");
        } else if ((stringFromView(binding.etTranPhone)).length() < 10 || (stringFromView(binding.etTranPhone)).length() > 10) {
            return Utility.showEditTextError(binding.tilTranPhone, "please Enter Valid Mobile Number");
        } else if (TextUtils.isEmpty(stringFromView(binding.etAvgWeight))) {
            return Utility.showEditTextError(binding.tilAvgWeight, "Enter Average Weight (KG)");
        } else if (TextUtils.isEmpty(stringFromView(binding.etKathaParchiNo))) {
            return Utility.showEditTextError(binding.tilKathaParchiNo, "Kanta Parchi No");
        } else if (TextUtils.isEmpty(stringFromView(binding.etOldKathaParchiNo))) {
            return Utility.showEditTextError(binding.tilOldKathaParchiNo, "Enter Old Kanta Parchi");
        } else if (TextUtils.isEmpty(stringFromView(binding.etOldTotalWeight))) {
            return Utility.showEditTextError(binding.tilOldTotalWeight, "Enter Old Total Weight");
        } else if (TextUtils.isEmpty(stringFromView(binding.etOldOriginalWeight))) {
            return Utility.showEditTextError(binding.tilOldOriginalWeight, "Old Original Weight");
        } else if (TextUtils.isEmpty(stringFromView(binding.etOldKanthaName))) {
            return Utility.showEditTextError(binding.tilOldKanthaName, "Old Kanta Name");
        }
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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup.getCheckedRadioButtonId() == R.id.radioSeller) {
            InTrackType = "Yes";
            InTrackID = "1";
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioBuyer) {
            InTrackType = "No";
            InTrackID = "0";
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioyes) {
            InBardhanaType = "Yes";
            InBardhanaID = "1";
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radiono) {
            InBardhanaType = "No";
            InBardhanaID = "0";
        }
    }
}
