package com.apnagodam.staff.activity.in.ivr;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.UploadFirstkantaParchiPostData;
import com.apnagodam.staff.Network.Request.UploadIVRPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.secound_quality_reports.UploadSecoundQualtityReportsClass;
import com.apnagodam.staff.databinding.CctvUploadBinding;
import com.apnagodam.staff.databinding.IvrUploadBinding;
import com.apnagodam.staff.utils.CallBackPermission;
import com.apnagodam.staff.utils.Permission;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.RunPermissions;
import com.apnagodam.staff.utils.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class INUploadIVRFileClass extends BaseActivity<IvrUploadBinding> implements CallBackPermission {

    public File  fileCCTV2;
    // role of image
   private String UserName, CaseID = "",audiofile="";
    private String firstcctvfileValue;
    private RunPermissions runPermissions;
    @Override
    protected int getLayoutResId() {
        return R.layout.ivr_upload;
    }

    @Override
    protected void setUp() {
        Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle != null) {
            UserName = bundle.getString("user_name");
            CaseID = bundle.getString("case_id");
        }
        runPermissions = new RunPermissions(this);
        binding.cctv1.setText("IVR Taggging Audio File");
        binding.cctvclick.setText("Click Here to upload IVR Taggging Audio File");
        binding.cctv2.setVisibility(View.GONE);
        binding.cctv2card.setVisibility(View.GONE);
        binding.llWeightBag.setVisibility(View.VISIBLE);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.customerName.setText(UserName);
        binding.caseId.setText(CaseID);
        clickListner();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(INIVRListingActivity.class);
    }

    private void clickListner() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(INIVRListingActivity.class);
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showDecisionDialog(INUploadIVRFileClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        if (isValid()) {
                            if (audiofile == null) {
                                Toast.makeText(getApplicationContext(), "IVR Taggging Audio File", Toast.LENGTH_LONG).show();
                            } else {
                                onNext();
                            }
                        }
                    }
                });
            }
        });
        binding.uploadKantha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runPermissions.permissionMulti(INUploadIVRFileClass.this, Permission.GELLERY);
            }
        });
    }
    boolean isValid() {
        if (TextUtils.isEmpty(stringFromView(binding.etCustomerWeight))) {
            return Utility.showEditTextError(binding.tilCustomerWeight, "Enter second kanta weight");
        } else  if (TextUtils.isEmpty(stringFromView(binding.etCustomerBags))) {
            return Utility.showEditTextError(binding.tilCustomerBags, "Enter secound kanta bags");
        }
        return true;
    }
    // update file
    public void onNext() {
        apiService.uploadIVRFile(new UploadIVRPostData(CaseID, stringFromView(binding.notes), audiofile,stringFromView(binding.etCustomerWeight),stringFromView(binding.etCustomerBags))).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
            @Override
            protected void onSuccess(LoginResponse body) {
                Utility.showAlertDialog(INUploadIVRFileClass.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        startActivityAndClear(INIVRListingActivity.class);
                    }
                });
            }
        });
    }
    private void videoToBase64(String path) throws IOException {
        InputStream inputStream = null;//You can get an inputStream using any IO API
        inputStream = new FileInputStream(path);
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        output64.close();

        String attachedFile = output.toString();
        audiofile = attachedFile;
        Log.e("Base64", attachedFile);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == 2) {
                        Uri selectedImage = data.getData();
                        String[] filePath = {MediaStore.Video.Media.DATA};
                        Cursor c = getContentResolver().query(selectedImage, filePath,
                                null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        String videoPath = c.getString(columnIndex);
                        c.close();
                        Log.d("SelectedVideoPath", videoPath);
                        try {
                            firstcctvfileValue = videoPath;
                            videoToBase64(firstcctvfileValue);
                            binding.KanthaImage.setText("" + firstcctvfileValue);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        /*if (firstFileCCTV) {
                            firstFileCCTV = false;
                            fileCCTV = new File(compressImage(this.camUri.getPath().toString()));
                            Uri uri = Uri.fromFile(fileCCTV);
                            firstcctvfileValue = String.valueOf(uri);
                            binding.KanthaImage.setText(""+uri);
                        }*/

                }
            }
        } catch (Exception e) {
        }
    }
    @Override
    public void onGranted(boolean success, @NotNull Permission code) {
        if (success) {
                gallery();
        }
    }
    private void gallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.setType("audio/*");

        startActivityForResult(intent, 2);
    }
}
