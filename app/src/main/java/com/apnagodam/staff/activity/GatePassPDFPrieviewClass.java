package com.apnagodam.staff.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallbackWProgress;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.ActivityGatePassPdfFileBinding;
import com.apnagodam.staff.module.GatePassPDFPojo;
import com.apnagodam.staff.utils.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GatePassPDFPrieviewClass extends BaseActivity<ActivityGatePassPdfFileBinding> {
    private Bitmap bitmap;
    private String caseiD = "";
    private static final int REQUEST_CAMERA_CODE = 120;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_gate_pass_pdf_file;
    }

    @Override
    protected void setUp() {
        Intent intent = getIntent();
        caseiD = intent.getExtras().getString("CaseID");
        showDialog();
        apiService.getGatePassPDf(caseiD).enqueue(new NetworkCallbackWProgress<GatePassPDFPojo>(getActivity()) {
            @Override
            protected void onSuccess(GatePassPDFPojo body) {
                try {
                    binding.gatepassNo.setText("No :" + body.getData().getGatePass());
                    binding.date.setText("Date :" + body.getData().getGatepassDate());
                    binding.terminalName.setText(body.getData().getTerminalName());
                    binding.caseId.setText(body.getData().getCaseId());
                    binding.buyerName.setText(body.getData().getCustFname());
                    binding.kathaParchiNo.setText("" + body.getData().getKantaParchiNo());
                    binding.truckNo.setText(body.getData().getVehicleNo());
                    binding.dharmKathaName.setText(body.getData().getDharamKantaName());
                    binding.stackNo.setText(body.getData().getStackNo());
                    binding.bags.setText("" + body.getData().getNoOfBags());
                    binding.avgWweight.setText(body.getData().getTotalWeight()+ " "+"QTL.");
                    binding.commodityName.setText(body.getData().getCommodityName());
                    binding.transporterName.setText(body.getData().getTransporterName());
                    binding.tansporterMobileNo.setText(body.getData().getTransporterPhoneNo());
/****************************************************************************************************************/
                    if (body.getData().getTruckFacility() != null) {
                        binding.facilityTruck.setText(Integer.parseInt(body.getData().getTruckFacility()) == 1 ? "YES" : "No");
                    } else if (body.getData().getTruckFacility() != null) {
                        binding.facilityBardhna.setText(Integer.parseInt(body.getData().getBagsFacility()) == 1 ? "YES" : "No");
                    }
                    binding.oldKathaParchiNo.setText(body.getData().getOldKantaParchi());
                    binding.oldKathaParchiTotalWeight.setText(body.getData().getOldTotalWeight());
                    binding.oldKathaParchiAvgWeight.setText(body.getData().getOldOriginalWeight());
                    binding.kathaName.setText(body.getData().getOldKantaName());
/****************************************************************************************************************/
                    binding.driverName.setText("चालक का नाम: " + body.getData().getDriverName());
                    binding.driverMobile.setText("चालक का मोबाइल न.: " + body.getData().getDriverPhone());
                    binding.whsName.setText("सुपरवाइजर का नाम : " + body.getData().getFirstName() + " " + body.getData().getLastName());
                    binding.whsMobile.setText("सुपरवाइजर का  मोबाइल न.: " + body.getData().getPhone());
                    binding.comment.setText(body.getData().getNotes());
                    hideDialog();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        });
        binding.tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("size", " " + binding.scrollLL.getWidth() + "  " + binding.scrollLL.getWidth());
                bitmap = loadBitmapFromView(binding.scrollLL, binding.scrollLL.getWidth(), binding.scrollLL.getHeight());
                nextMClass();
                // createPdf();
            }
        });
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    private void createPdf() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = 3508;
        float width = 2500/*displaymetrics.widthPixels*/;
        int convertHighet = (int) hight, convertWidth = (int) width;
//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // write the document content
      /*  String targetPdf = "/sdcard/pdffromScroll.pdf";
        File filePath;
        filePath = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/sdcard/pdffromScroll.pdf");*/
        File camFile = Utility.getOutpuPDFtMediaFile(this, "pdf");
        if (camFile.exists()) {
            camFile.delete();
        }
        camFile = Utility.getOutpuPDFtMediaFile(this, "pdf");
        try {
            document.writeTo(new FileOutputStream(camFile));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        //  Toast.makeText(this, "PDF of Scroll is created!!!", Toast.LENGTH_SHORT).show();

        openGeneratedPDF(camFile);

    }

    private Boolean requestPermissions() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writeExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        );
        int readExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE
        );
        return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalPermission == PackageManager.PERMISSION_GRANTED
                && readExternalPermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions();
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]))
                                return;
                        } else {
                            createPdf();
                        }
                    }
                    // open dialog
                }
        }
    }

    private void openGeneratedPDF(File camFile) {
        // File camFile = Utility.getOutpuPDFtMediaFile(this, "pdf");
       /* if (camFile.exists()) {
            camFile.delete();
        }
        camFile = Utility.getOutpuPDFtMediaFile(this, "pdf");*/
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", camFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        // intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent targetIntent = Intent.createChooser(intent, "Open File");
        try {
            startActivity(targetIntent);
            //  finish();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(GatePassPDFPrieviewClass.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
        }
    }

    private void nextMClass() {
        if (requestPermissions()) {
            createPdf();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_CODE
            );
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
