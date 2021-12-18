package com.apnagodam.staff.activity.in.secound_quality_reports;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.UploadSecoundQualityPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.ActivityUpdateQualityReportBinding;
import com.apnagodam.staff.utils.CallBackPermission;
import com.apnagodam.staff.utils.CameraUtils;
import com.apnagodam.staff.utils.Permission;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.RunPermissions;
import com.apnagodam.staff.utils.Utility;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UploadSecoundQualtityReportsClass extends BaseActivity<ActivityUpdateQualityReportBinding> implements CallBackPermission {
    String packagingTypeID = null;
    // role of image
    String UserName, CaseID = "";
    public File fileReport, fileCommudity, fileVideo;
    boolean ReportsFileSelect = false;
    boolean CommudityFileSelect = false;
    boolean CommudityFileVideoSelect = false;
    private String reportFile, commudityFile;
    Options options;
    private String imageStoragePath, quailtyVideo = "";
    private RunPermissions runPermissions;

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
        runPermissions = new RunPermissions(this);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.customerName.setText(UserName);
        binding.caseId.setText(CaseID);
        clickListner();
        // spinner purpose
        binding.rlpackgingType.setVisibility(View.GONE);
        binding.videocard.setVisibility(View.VISIBLE);
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
                            if (fileCommudity == null) {
                                Toast.makeText(getApplicationContext(), R.string.upload_commodity_file, Toast.LENGTH_LONG).show();
                            } else if (quailtyVideo == null || quailtyVideo.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Upload Quality Video ", Toast.LENGTH_LONG).show();
                            } else {
                                onNext();
                            }
                   /* if (fileReport == null) {
                        Toast.makeText(getApplicationContext(), R.string.upload_reports_file, Toast.LENGTH_LONG).show();
                    }
                    else if (packagingTypeID == null) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.packaging), Toast.LENGTH_LONG).show();
                    } else {*/

                            // }
                        }
                    }
                });
            }
        });
        /**
         * Record video button click event
         */
        binding.uploadvideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showVideoChooserDialog();
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
      /*  if (quailtyVideo == null || quailtyVideo.isEmpty()) {
           binding.videoRl.setClickable(false);
            binding.videoRl.setEnabled(false);
            binding.videoRl.setFocusable(false);
        }else {
            binding.videoRl.setClickable(true);
            binding.videoRl.setEnabled(true);
            binding.videoRl.setFocusable(true);
        }*/
        binding.videoRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewVideo();
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
    public void viewVideo() {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(UploadSecoundQualtityReportsClass.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = ((Activity) UploadSecoundQualtityReportsClass.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.video_priview_fullscreen, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = (ImageView) dialogView.findViewById(R.id.cancel_btn);
        VideoView videoView = (VideoView) dialogView.findViewById(R.id.videoView);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        videoView.setVideoPath(imageStoragePath);
        videoView.start();
        alertDialog.show();
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

    private void showVideoChooserDialog() {

        final CharSequence[] options = {"From Camera", "From Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("From Camera")) {
                    runPermissions.permissionMulti(UploadSecoundQualtityReportsClass.this, Permission.CAMERA);


                } else if (options[item].equals("From Gallery")) {

                    runPermissions.permissionMulti(UploadSecoundQualtityReportsClass.this, Permission.GELLERY);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

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
        quailtyVideo = attachedFile;
        Log.e("Base64", attachedFile);
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
                , stringFromView(binding.etLive), stringFromView(binding.notes), CommudityFileSelectImage, quailtyVideo)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
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
                    } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
                        if (CommudityFileVideoSelect) {
                            ReportsFileSelect = false;
                            CommudityFileSelect = false;
                            CommudityFileVideoSelect = false;
                            fileVideo = new File((returnValue.get(0).toString()));
                            Uri uri = Uri.fromFile(fileVideo);
                           /* if (uri.getPath() != null) {
                                binding.videoPreview.setVideoPath(uri.getPath());
                                // start playing
                                binding.videoPreview.start();
                            }*/
                            String qualityVideoFile = String.valueOf(uri);
                        }
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                try {
                    videoToBase64(imageStoragePath);
                    binding.videoView.setVideoPath(imageStoragePath);
                    binding.videoView.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == 2) {
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
                    imageStoragePath=videoPath;
                    videoToBase64(imageStoragePath);
                    binding.videoView.setVideoPath(imageStoragePath);
                    binding.videoView.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onGranted(boolean success, @NotNull Permission code) {
        if (success) {
            if (code == Permission.GELLERY) {
                gallery();
            } else if (code == Permission.CAMERA) {
                camera();
            }
        }
    }

    private void camera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File imageFile = CameraUtils.getOutputMediaFile(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        imageStoragePath = imageFile.getAbsolutePath();
        Uri fileUri = CameraUtils.getOutputMediaFileUri(UploadSecoundQualtityReportsClass.this, imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, 1);
    }

    private void gallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");

        startActivityForResult(intent, 2);
    }
}
