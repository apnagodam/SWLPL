package com.apnagodam.staff.activity.in.truckbook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.UploadTruckDetailsPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.ActivityUploadDetailsBinding;
import com.apnagodam.staff.module.TransporterDetailsPojo;
import com.apnagodam.staff.module.TransporterListPojo;
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

public class TruckUploadDetailsClass extends BaseActivity<ActivityUploadDetailsBinding> implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    String UserName, CaseID = "";
    private Calendar calender;
    boolean checked = false;
    String spinnerRateType = null;
    public File fileBiltyImage;
    boolean BiltyImageFile = false;
    private String BiltyFile;
    Options options;
    List<TransporterListPojo.Datum> data;
    List<String> TransporterName;
    ArrayAdapter<String> spinnerTransporterAdpter;
    String TransporterID = null;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_upload_details;
    }

    @Override
    protected void setUp() {
        TransporterName = new ArrayList<>();
        TransporterName.add("Select");
        getTransporterList();
        calender = Calendar.getInstance();
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
        binding.spinnerRatetYpe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    spinnerRateType = parent.getItemAtPosition(position).toString();
                    binding.tilTransportRate.setVisibility(View.VISIBLE);
                } else {
                    binding.tilTransportRate.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // can leave this empty
            }
        });

        binding.spinnerTransporterName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    String presentMeterStatusID = parentView.getItemAtPosition(position).toString();
                    for (int i = 0; i < data.size(); i++) {
                        if (presentMeterStatusID.contains(data.get(i).getTransporterName() + "(" + data.get(i).getTransporterUniqueId() + ")")) {
                            TransporterID = String.valueOf(data.get(i).getId());
                            break;
                        }
                    }
                    getTransporterDetails(TransporterID);
                    if (presentMeterStatusID.contains("Client")) {
                        Checked();
                    } else {
                        NotChecked();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    /*    binding.etAdvancePatyment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etAdvancePatyment.getText().toString().trim() != null && !binding.etAdvancePatyment.getText().toString().trim().isEmpty() && !binding.etMaxWeight.getText().toString().trim().isEmpty() && !binding.etTransportRate.getText().toString().trim().isEmpty()) {
                        Double etAdvancePatyment = (Double.parseDouble(binding.etAdvancePatyment.getText().toString().trim()));
                        Double maxWeight = (Double.parseDouble(binding.etMaxWeight.getText().toString().trim()));
                        Double TransportRate = (Double.parseDouble(binding.etTransportRate.getText().toString().trim()));
                        Double Advancement = maxWeight * TransportRate;
                        if (etAdvancePatyment > Advancement) {
                            Utility.showAlertDialog(TruckUploadDetailsClass.this, getString(R.string.alert), "Advance Payment  Must be Less than or equal to " + Advancement + " RS", new Utility.AlertCallback() {
                                @Override
                                public void callback() {
                                    binding.etAdvancePatyment.setText("");
                                }
                            });
                        } else {
                            binding.etAdvancePatyment.setText("" + Advancement);
                        }
                    }
                } else {
                }
            }
        });*/
        binding.etMaxWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etAdvancePatyment.getText().toString().trim() != null && !binding.etAdvancePatyment.getText().toString().trim().isEmpty() && !binding.etMaxWeight.getText().toString().trim().isEmpty() && !binding.etTransportRate.getText().toString().trim().isEmpty()) {
                        Double etAdvancePatyment = (Double.parseDouble(binding.etAdvancePatyment.getText().toString().trim()));
                        Double maxWeight = (Double.parseDouble(binding.etMaxWeight.getText().toString().trim()));
                        Double TransportRate = (Double.parseDouble(binding.etTransportRate.getText().toString().trim()));
                        Double Advancement = maxWeight * TransportRate;
                        if (etAdvancePatyment > Advancement) {
                            Utility.showAlertDialog(TruckUploadDetailsClass.this, getString(R.string.alert), "Advance Payment  Must be Less than or equal to " + Advancement + " RS", new Utility.AlertCallback() {
                                @Override
                                public void callback() {
                                    binding.etAdvancePatyment.setText("");
                                }
                            });
                        } else {
                            binding.etAdvancePatyment.setText("" + Advancement);
                        }
                    }
                } else {
                }
            }
        });
        binding.etTransportRate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etAdvancePatyment.getText().toString().trim() != null && !binding.etAdvancePatyment.getText().toString().trim().isEmpty() && !binding.etMaxWeight.getText().toString().trim().isEmpty() && !binding.etTransportRate.getText().toString().trim().isEmpty()) {
                        Double etAdvancePatyment = (Double.parseDouble(binding.etAdvancePatyment.getText().toString().trim()));
                        Double maxWeight = (Double.parseDouble(binding.etMaxWeight.getText().toString().trim()));
                        Double TransportRate = (Double.parseDouble(binding.etTransportRate.getText().toString().trim()));
                        Double Advancement = maxWeight * TransportRate;
                        if (etAdvancePatyment > Advancement) {
                            Utility.showAlertDialog(TruckUploadDetailsClass.this, getString(R.string.alert), "Advance Payment  Must be Less than or equal to " + Advancement + " RS", new Utility.AlertCallback() {
                                @Override
                                public void callback() {
                                    binding.etAdvancePatyment.setText("");
                                }
                            });
                        } else {
                           // binding.etAdvancePatyment.setText("" + Advancement);
                        }
                    }
                } else {
                }
            }
        });
      /*  binding.etStartDateTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etStartDateTime.getText().toString().trim() != null && !binding.etStartDateTime.getText().toString().trim().isEmpty() && !binding.etTurnaroundTime.getText().toString().trim().isEmpty()) {
                        Double maxWeight = (Double.parseDouble(binding.etStartDateTime.getText().toString().trim()));
                        Double TransportRate = (Double.parseDouble(binding.etTurnaroundTime.getText().toString().trim()));
                        Double Advancement = maxWeight * TransportRate;
                        binding.etEndDateTime.setText("" + Advancement);
                    }
                    // Toast.makeText(MainActivity.this, "focus loosed", Toast.LENGTH_LONG).show();
                } else {
                    /// Toast.makeText(MainActivity.this, "focused", Toast.LENGTH_LONG).show();
                }
            }
        });*/
        // check advance payment
       /* binding.etMaxWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etMaxWeight.getText().toString().trim() != null && !binding.etMaxWeight.getText().toString().trim().isEmpty() && !binding.etTransportRate.getText().toString().trim().isEmpty()) {
                        Double maxWeight = (Double.parseDouble(binding.etMaxWeight.getText().toString().trim()));
                        Double TransportRate = (Double.parseDouble(binding.etTransportRate.getText().toString().trim()));
                        Double Advancement = maxWeight * TransportRate;
                        binding.etAdvancePatyment.setText("" + Advancement);
                    }
                    // Toast.makeText(MainActivity.this, "focus loosed", Toast.LENGTH_LONG).show();
                } else {
                    /// Toast.makeText(MainActivity.this, "focused", Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.etTransportRate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etTransportRate.getText().toString().trim() != null && !binding.etTransportRate.getText().toString().trim().isEmpty() && !binding.etMaxWeight.getText().toString().trim().isEmpty()) {
                        Double maxWeight = (Double.parseDouble(binding.etMaxWeight.getText().toString().trim()));
                        Double TransportRate = (Double.parseDouble(binding.etTransportRate.getText().toString().trim()));
                        Double Advancepayment = maxWeight * TransportRate;
                        binding.etAdvancePatyment.setText("" + Advancepayment);
                    }
                } else {
                    /// Toast.makeText(MainActivity.this, "focused", Toast.LENGTH_LONG).show();
                }
            }
        });*/


    }

    private void getTransporterDetails(String transporterID) {
        apiService.getTransporterDetails(transporterID).enqueue(new NetworkCallback<TransporterDetailsPojo>(getActivity()) {
            @Override
            protected void onSuccess(TransporterDetailsPojo body) {
                binding.etVehicleNo.setText("" + body.getData().getVehicleNo());
                binding.etDriverName.setText("" + body.getData().getTransporterName());
                binding.etDriverPhoneNo.setText("" + body.getData().getTransporterPhoneNo());
            }
        });
    }

    private void getTransporterList() {
        apiService.getTransporterList().enqueue(new NetworkCallback<TransporterListPojo>(getActivity()) {
            @Override
            protected void onSuccess(TransporterListPojo body) {
                data = body.getData();
                for (int i = 0; i < data.size(); i++) {
                    TransporterName.add(data.get(i).getTransporterName() + "(" + data.get(i).getTransporterUniqueId() + ")");
                }
                spinnerTransporterAdpter = new ArrayAdapter<String>(TruckUploadDetailsClass.this, R.layout.multiline_spinner_item, TransporterName) {
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
                spinnerTransporterAdpter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                // Set Adapter in the spinner
                binding.spinnerTransporterName.setAdapter(spinnerTransporterAdpter);
            }
        });

    }

    private void clickListner() {
        binding.ivClose.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.etStartDateTime.setOnClickListener(this);
        binding.lpCommiteDate.setOnClickListener(this);
        binding.lpEndDate.setOnClickListener(this);
        binding.uploadTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiltyImageFile = true;
                callImageSelector(REQUEST_CAMERA);
            }
        });
        binding.checkNotRequried.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                                @Override
                                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                    if (buttonView.isChecked()) {
                                                                        // checked
                                                                        Checked();
                                                                    } else {
                                                                        // not checked
                                                                        NotChecked();
                                                                    }
                                                                }
                                                            }
        );
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
        Pix.start(TruckUploadDetailsClass.this, options);
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

    private void Checked() {
        checked = true;
      /*  binding.etTransporterName.setEnabled(false);
        binding.etTransporterName.setClickable(false);
        binding.etTransporterName.setFocusable(false);
        binding.etTransporterName.setText("");*/
        binding.etTransporterName.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.etVehicleNo.setEnabled(false);
        binding.etVehicleNo.setClickable(false);
        binding.etVehicleNo.setFocusable(false);
        binding.etVehicleNo.setFocusableInTouchMode(false);
        binding.etVehicleNo.setText("");
        binding.etVehicleNo.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.etDriverName.setEnabled(false);
        binding.etDriverName.setClickable(false);
        binding.etDriverName.setFocusable(false);
        binding.etDriverName.setText("");
        binding.etDriverName.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.etDriverPhoneNo.setEnabled(false);
        binding.etDriverPhoneNo.setClickable(false);
        binding.etDriverPhoneNo.setFocusable(false);
        binding.etDriverPhoneNo.setText("");
        binding.etDriverPhoneNo.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.etTransportRate.setEnabled(false);
        binding.etTransportRate.setClickable(false);
        binding.etTransportRate.setFocusable(false);
        binding.etTransportRate.setText("");
        binding.etTransportRate.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.etMinWeight.setEnabled(false);
        binding.etMinWeight.setClickable(false);
        binding.etMinWeight.setFocusable(false);
        binding.etMinWeight.setText("");
        binding.etMinWeight.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.etMaxWeight.setEnabled(false);
        binding.etMaxWeight.setClickable(false);
        binding.etMaxWeight.setFocusable(false);
        binding.etMaxWeight.setText("");
        binding.etMaxWeight.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.etTurnaroundTime.setEnabled(false);
        binding.etTurnaroundTime.setClickable(false);
        binding.etTurnaroundTime.setFocusable(false);
        binding.etTurnaroundTime.setText("");
        binding.etTurnaroundTime.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.etTurnaroundTime.setEnabled(false);
        binding.etTotalWeight.setClickable(false);
        binding.etTotalWeight.setFocusable(false);
        binding.etTotalWeight.setText("");
        binding.etTotalWeight.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.etBags.setEnabled(false);
        binding.etBags.setClickable(false);
        binding.etBags.setFocusable(false);
        binding.etBags.setText("");
        binding.etBags.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.etTotalTransCost.setEnabled(false);
        binding.etTotalTransCost.setClickable(false);
        binding.etTotalTransCost.setFocusable(false);
        binding.etTotalTransCost.setText("");
        binding.etTotalTransCost.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.etAdvancePatyment.setEnabled(false);
        binding.etAdvancePatyment.setClickable(false);
        binding.etAdvancePatyment.setFocusable(false);
        binding.etAdvancePatyment.setText("");
        binding.etAdvancePatyment.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.etFinalSettalementAmount.setEnabled(false);
        binding.etFinalSettalementAmount.setClickable(false);
        binding.etFinalSettalementAmount.setFocusable(false);
        binding.etFinalSettalementAmount.setText("");
        binding.etFinalSettalementAmount.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.etStartDateTime.setEnabled(false);
        binding.etStartDateTime.setClickable(false);
        binding.etStartDateTime.setFocusable(false);
        binding.etStartDateTime.setText("");
        binding.etStartDateTime.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.etEndDateTime.setEnabled(false);
        binding.etEndDateTime.setClickable(false);
        binding.etEndDateTime.setFocusable(false);
        binding.etEndDateTime.setText("");
        binding.etEndDateTime.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.lpCommiteDate.setEnabled(false);
        binding.lpCommiteDate.setClickable(false);
        binding.lpCommiteDate.setFocusable(false);
        binding.lpCommiteDate.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.lpEndDate.setEnabled(false);
        binding.lpEndDate.setClickable(false);
        binding.lpEndDate.setFocusable(false);
        binding.lpEndDate.setBackgroundColor(getResources().getColor(R.color.lightgray));
        binding.spinnerRatetYpe.setEnabled(false);
        binding.spinnerRatetYpe.setClickable(false);
        binding.uploadTruck.setEnabled(false);
        binding.uploadTruck.setClickable(false);
    }

    private void NotChecked() {
        checked = false;
      /*  binding.etTransporterName.setEnabled(true);
        binding.etTransporterName.setClickable(true);
        binding.etTransporterName.setFocusable(true);
        binding.etTransporterName.setFocusableInTouchMode(true);*/
        binding.uploadTruck.setEnabled(true);
        binding.uploadTruck.setClickable(true);
        binding.spinnerRatetYpe.setEnabled(true);
        binding.spinnerRatetYpe.setClickable(true);
        binding.etTransportRate.setEnabled(true);
        binding.etTransportRate.setClickable(true);
        binding.etTransportRate.setFocusable(true);
        binding.etTransportRate.setFocusableInTouchMode(true);
        binding.etMinWeight.setEnabled(true);
        binding.etMinWeight.setClickable(true);
        binding.etMinWeight.setFocusable(true);
        binding.etMinWeight.setFocusableInTouchMode(true);
        binding.etMaxWeight.setEnabled(true);
        binding.etMaxWeight.setClickable(true);
        binding.etMaxWeight.setFocusable(true);
        binding.etMaxWeight.setFocusableInTouchMode(true);
        binding.etTurnaroundTime.setEnabled(true);
        binding.etTurnaroundTime.setClickable(true);
        binding.etTurnaroundTime.setFocusable(true);
        binding.etTurnaroundTime.setFocusableInTouchMode(true);
        binding.etTotalWeight.setEnabled(true);
        binding.etTotalWeight.setClickable(true);
        binding.etTotalWeight.setFocusable(true);
        binding.etTotalWeight.setFocusableInTouchMode(true);
        binding.etBags.setEnabled(true);
        binding.etBags.setClickable(true);
        binding.etBags.setFocusable(true);
        binding.etBags.setFocusableInTouchMode(true);
        binding.etTotalTransCost.setEnabled(true);
        binding.etTotalTransCost.setClickable(true);
        binding.etTotalTransCost.setFocusable(true);
        binding.etTotalTransCost.setFocusableInTouchMode(true);
        binding.etAdvancePatyment.setEnabled(true);
        binding.etAdvancePatyment.setClickable(true);
        binding.etAdvancePatyment.setFocusable(true);
        binding.etAdvancePatyment.setFocusableInTouchMode(true);
        binding.etFinalSettalementAmount.setEnabled(true);
        binding.etFinalSettalementAmount.setClickable(true);
        binding.etFinalSettalementAmount.setFocusable(true);
        binding.etFinalSettalementAmount.setFocusableInTouchMode(true);
        binding.etStartDateTime.setEnabled(true);
        binding.etStartDateTime.setClickable(true);
        binding.etStartDateTime.setFocusable(true);
        binding.etStartDateTime.setFocusableInTouchMode(true);
        binding.etEndDateTime.setEnabled(true);
        binding.etEndDateTime.setClickable(true);
        binding.etEndDateTime.setFocusable(true);
        binding.etEndDateTime.setFocusableInTouchMode(true);
        binding.lpEndDate.setEnabled(true);
        binding.lpEndDate.setClickable(true);
        binding.lpEndDate.setFocusable(true);
        binding.lpEndDate.setFocusableInTouchMode(true);
        binding.lpCommiteDate.setEnabled(true);
        binding.lpCommiteDate.setClickable(true);
        binding.lpCommiteDate.setFocusable(true);
        binding.lpCommiteDate.setFocusableInTouchMode(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(TruckBookListingActivity.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                startActivityAndClear(TruckBookListingActivity.class);
                break;
            case R.id.et_start_date_time:
                popUpDatePicker();
                break;
            case R.id.lp_commite_date:
                popUpDatePicker();
                break;
            case R.id.lp_end_date:
                EnddatePicker();
                break;
            case R.id.et_end_date_time:
                EnddatePicker();
                break;
            case R.id.btn_login:
                if (isValid()) {
                    if (checked) {
                        if (binding.notes.getText().toString().trim().isEmpty()) {
                            Toast.makeText(TruckUploadDetailsClass.this, "Enter Notes Here!!", Toast.LENGTH_LONG).show();
                        }else if (TransporterID==null){
                            Toast.makeText(TruckUploadDetailsClass.this, "Select Transport Name!!", Toast.LENGTH_LONG).show();
                        } else {
                            Utility.showDecisionDialog(TruckUploadDetailsClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                                @Override
                                public void callback() {
                                    callApi();
                                }
                            });
                        }
                    }else if (TransporterID==null){
                        Toast.makeText(TruckUploadDetailsClass.this, "Select Transport Name!!", Toast.LENGTH_LONG).show();
                    }  else if (TextUtils.isEmpty(stringFromView(binding.etStartDateTime))) {

                        Toast.makeText(TruckUploadDetailsClass.this, getResources().getString(R.string.start_date_time_val), Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(stringFromView(binding.etEndDateTime))) {
                        Toast.makeText(TruckUploadDetailsClass.this, getResources().getString(R.string.end_date_time_val), Toast.LENGTH_LONG).show();
                    } else if (fileBiltyImage == null) {
                        Toast.makeText(TruckUploadDetailsClass.this, "Upload to Bilty Image", Toast.LENGTH_LONG).show();
                    } else {
                        Utility.showDecisionDialog(TruckUploadDetailsClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                            @Override
                            public void callback() {
                                callApi();
                            }
                        });
                    }
                }
                break;
        }
    }

    private void callApi() {
        String BiltyImage = "";
        if (fileBiltyImage != null) {
            BiltyImage = "" + Utility.transferImageToBase64(fileBiltyImage);
        }
        apiService.uploadTruckDetails(new UploadTruckDetailsPostData(
                CaseID, TransporterID, stringFromView(binding.etVehicleNo),
                stringFromView(binding.etDriverName), stringFromView(binding.etDriverPhoneNo), stringFromView(binding.etMinWeight),
                stringFromView(binding.etMaxWeight), stringFromView(binding.etTurnaroundTime), stringFromView(binding.etTotalWeight),
                stringFromView(binding.etBags), stringFromView(binding.etTotalTransCost), stringFromView(binding.etAdvancePatyment), stringFromView(binding.etStartDateTime),
                stringFromView(binding.etFinalSettalementAmount), stringFromView(binding.etEndDateTime), stringFromView(binding.notes), TransporterID, BiltyImage, spinnerRateType, stringFromView(binding.etRealteCaseid))).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
            @Override
            protected void onSuccess(LoginResponse body) {
                Utility.showAlertDialog(TruckUploadDetailsClass.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        startActivityAndClear(TruckBookListingActivity.class);
                    }
                });
            }
        });
    }

    boolean isValid() {
        if (checked) {

        } else {
            /*if (TextUtils.isEmpty(stringFromView(binding.etTransporterName))) {
                return Utility.showEditTextError(binding.tilTransporterName, R.string.transporter_name);
            } else*/
           /* if (TextUtils.isEmpty(stringFromView(binding.etVehicleNo))) {
                return Utility.showEditTextError(binding.tilVehicleNo, R.string.vehicle_no);
            } else if (TextUtils.isEmpty(stringFromView(binding.etDriverName))) {
                return Utility.showEditTextError(binding.tilDriverName, R.string.driver_name_validation);
            } else if (TextUtils.isEmpty(stringFromView(binding.etDriverPhoneNo))) {
                return Utility.showEditTextError(binding.tilDriverPhoneNo, R.string.driver_phone_no_enter);
            } else*/
             if (spinnerRateType == null) {
                Toast.makeText(TruckUploadDetailsClass.this, "Select Rate Type", Toast.LENGTH_LONG).show();
            }
             else if (TextUtils.isEmpty(stringFromView(binding.etTransportRate))) {
                return Utility.showEditTextError(binding.tilTransportRate, R.string.transport_rate_validation);
            } else if (TextUtils.isEmpty(stringFromView(binding.etMinWeight))) {
                return Utility.showEditTextError(binding.tilMinWeight, R.string.min_weight_val);
            } else if (TextUtils.isEmpty(stringFromView(binding.etMaxWeight))) {
                return Utility.showEditTextError(binding.tilMaxWeight, R.string.max_weight_val);
            } else if (TextUtils.isEmpty(stringFromView(binding.etTurnaroundTime))) {
                return Utility.showEditTextError(binding.tilTurnaroundTime, R.string.turnaround_time_val);
            } else if (TextUtils.isEmpty(stringFromView(binding.etTotalWeight))) {
                return Utility.showEditTextError(binding.tilTotalWeight, R.string.total_weight_val);
            } else if (TextUtils.isEmpty(stringFromView(binding.etBags))) {
                return Utility.showEditTextError(binding.tilBags, R.string.bags_val);
            } else if (TextUtils.isEmpty(stringFromView(binding.etAdvancePatyment))) {
                return Utility.showEditTextError(binding.tilAdvancePatyment, R.string.advance_patyment_val);
            } else if (TextUtils.isEmpty(stringFromView(binding.etTotalTransCost))) {
                return Utility.showEditTextError(binding.tilTotalTransCost, R.string.total_trans_cost_val);
            } else if (TextUtils.isEmpty(stringFromView(binding.etFinalSettalementAmount))) {
                return Utility.showEditTextError(binding.tilFinalSettalementAmount, R.string.settleememt_amount_val);
            } else if (TextUtils.isEmpty(stringFromView(binding.etRealteCaseid))) {
                return Utility.showEditTextError(binding.etRealteCaseid, "Enter Related CaseId");
            }
        }
        return true;
    }

    public void popUpDatePicker() {
        DatePickerDialog dateDialog = new DatePickerDialog(this, date, calender
                .get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH));
        dateDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dateDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calender.set(Calendar.YEAR, year);
            calender.set(Calendar.MONTH, monthOfYear);
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            binding.etStartDateTime.setText(sdf.format(calender.getTime()).toString());
            StarttimePicker(sdf.format(calender.getTime()).toString());
        }
    };

    private void StarttimePicker(final String date) {
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                binding.etStartDateTime.setText(date + " - " + selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    public void EnddatePicker() {
        DatePickerDialog dateDialog = new DatePickerDialog(this, dateeend, calender
                .get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH));
        dateDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dateDialog.show();
    }

    DatePickerDialog.OnDateSetListener dateeend = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calender.set(Calendar.YEAR, year);
            calender.set(Calendar.MONTH, monthOfYear);
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            binding.etEndDateTime.setText(sdf.format(calender.getTime()).toString());
            EndtimePicker(sdf.format(calender.getTime()).toString());
        }
    };

    private void EndtimePicker(final String date) {
        Log.d("Al JSon Data", "" + date);
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                binding.etEndDateTime.setText(date + " - " + selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
