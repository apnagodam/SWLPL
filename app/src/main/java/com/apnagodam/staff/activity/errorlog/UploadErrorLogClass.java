package com.apnagodam.staff.activity.errorlog;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.CreateErrorLogPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.ActivityEmpErrorLogBinding;
import com.apnagodam.staff.module.AllLevelEmpListPojo;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadErrorLogClass extends BaseActivity<ActivityEmpErrorLogBinding> {
    // role of image
    String UserName, CaseID = "";
    public File fileReport, fileCommudity;
    boolean ReportsFileSelect = false;
    boolean CommudityFileSelect = false;
    private String reportFile, commudityFile;
    /*approve List */
    List<String> approveName;
    List<String> approveID;
    ArrayAdapter<String> SpinnerApproveByAdapter;
    String SelectedApproveIDIs = null;


    /*error log  List */
    List<String> ErrorName;
    List<String> ErrorID;
    ArrayAdapter<String> SpinnererrorAdapter;
    String SelectedErrorIDIs = null;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_emp_error_log;
    }

    @Override
    protected void setUp() {
        // approved for
        approveName = new ArrayList<>();
        approveID = new ArrayList<>();
        approveName.add(getResources().getString(R.string.approved_by));
        approveID.add("0");

        ErrorName = new ArrayList<>();
        ErrorID = new ArrayList<>();
        ErrorName.add("Select");
        ErrorID.add("0");

        binding.tvDone.setVisibility(View.GONE);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // get approve person  list
        apiService.getlevelwiselist("error_log").enqueue(new NetworkCallback<AllLevelEmpListPojo>(getActivity()) {
            @Override
            protected void onSuccess(AllLevelEmpListPojo body) {
                for (int i = 0; i < body.getData().size(); i++) {
                    if (body.getRequest_count() > 0) {
                        binding.tvDone.setClickable(true);
                        binding.tvDone.setEnabled(true);
                        binding.tvDone.setText("Approval Request " + "(" + body.getRequest_count() + ")");
                    }
                    approveID.add(body.getData().get(i).getUserId());
                    approveName.add(body.getData().get(i).getFirstName() + " " + body.getData().get(i).getLastName() + "(" + body.getData().get(i).getEmpId() + ")");
                }
                for (int i = 0; i < body.getErrorName().size(); i++) {
                    ErrorID.add(body.getErrorName().get(i).getId());
                    ErrorName.add(body.getErrorName().get(i).getError_name());
                }

            }
        });
        clickListner();
        SpinnererrorAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, ErrorName) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        SpinnererrorAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerConvType.setAdapter(SpinnererrorAdapter);
        binding.spinnerConvType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    SelectedErrorIDIs = parent.getItemAtPosition(position).toString();
                    /*for (int i = 0; i < ErrorName.size(); i++) {
                        if (packagingTypeID.equalsIgnoreCase(ErrorName.get(position))) {
                            SelectedErrorIDIs = ErrorID.get(position);
                            break;
                        }
                    }*/
                } else {
                    SelectedErrorIDIs = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // can leave this empty
            }
        });

        // Approved listing
        SpinnerApproveByAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, approveName) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        SpinnerApproveByAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerApprovedBy.setAdapter(SpinnerApproveByAdapter);
        binding.spinnerApprovedBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    String EmpID = parentView.getItemAtPosition(position).toString();
                    for (int i = 0; i < approveName.size(); i++) {
                        if (EmpID.equalsIgnoreCase(approveName.get(position))) {
                            SelectedApproveIDIs = approveID.get(position);
                            break;
                        }
                    }
                } else {
                    SelectedApproveIDIs = null;
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
                startActivityAndClear(MyErrorLogListClass.class);
            }
        });
        binding.tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(ApprovalRequestErroorListClass.class);
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showDecisionDialog(UploadErrorLogClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        if (isValid()) {
                            if (SelectedErrorIDIs == null) {
                                Toast.makeText(getApplicationContext(), "Select Error Name ", Toast.LENGTH_LONG).show();
                            } else if (SelectedApproveIDIs == null) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.approved_by), Toast.LENGTH_LONG).show();
                            } else {
                                onNext();
                            }
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
                callImageSelector();
            }
        });

        binding.uploadCommudity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportsFileSelect = false;
                CommudityFileSelect = true;
                callImageSelector();
            }
        });

        binding.ReportsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(UploadErrorLogClass.this, R.layout.popup_photo_full, view, reportFile, null);
            }
        });
        binding.CommudityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(UploadErrorLogClass.this, R.layout.popup_photo_full, view, commudityFile, null);
            }
        });

    }


    private void callImageSelector() {
        Options options = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restrict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setExcludeVideos(true)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientation
                .setPath("/apnagodam/lp/images");                                       //Custom Path For media Storage
        Pix.start(UploadErrorLogClass.this, options.setRequestCode(REQUEST_CAMERA_PICTURE));
    }

    // update file
    public void onNext() {
        String KanthaImage = "", CommudityFileSelectImage = "", otherFileImage = "";
        if (fileReport != null) {
            KanthaImage = "" + Utility.transferImageToBase64(fileReport);
        }
        if (fileCommudity != null) {
            CommudityFileSelectImage = "" + Utility.transferImageToBase64(fileCommudity);
        }

        apiService.doCreateErrorLog(new CreateErrorLogPostData(SelectedErrorIDIs, stringFromView(binding.notes), stringFromView(binding.notesPupouse)
                , SelectedApproveIDIs, KanthaImage, CommudityFileSelectImage)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
            @Override
            protected void onSuccess(LoginResponse body) {
                Utility.showAlertDialog(UploadErrorLogClass.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        startActivityAndClear(MyErrorLogListClass.class);
                    }
                });
            }
        });
    }

    boolean isValid() {
        if (TextUtils.isEmpty(stringFromView(binding.notes))) {
            return Utility.showEditTextError(binding.notes, R.string.error_1);
        } else if (TextUtils.isEmpty(stringFromView(binding.notesPupouse))) {
            return Utility.showEditTextError(binding.notesPupouse, R.string.error_2);
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
