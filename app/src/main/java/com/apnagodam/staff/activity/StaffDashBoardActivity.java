package com.apnagodam.staff.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.AttendancePostData;
import com.apnagodam.staff.Network.Response.AttendanceResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.caseid.CaseIDGenerateClass;
import com.apnagodam.staff.activity.in.first_kantaparchi.FirstkanthaParchiListingActivity;
import com.apnagodam.staff.activity.in.labourbook.LabourBookListingActivity;
import com.apnagodam.staff.activity.in.pricing.InPricingListingActivity;
import com.apnagodam.staff.activity.in.truckbook.TruckBookListingActivity;
import com.apnagodam.staff.activity.lead.LeadGenerateClass;
import com.apnagodam.staff.adapter.NavigationAdapter;
import com.apnagodam.staff.databinding.StaffDashboardBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.interfaces.OnProfileClickListener;
import com.apnagodam.staff.module.DashBoardData;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.RecyclerItemClickListener;
import com.apnagodam.staff.utils.Utility;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StaffDashBoardActivity extends BaseActivity<StaffDashboardBinding> implements View.OnClickListener, OnProfileClickListener, RecyclerItemClickListener.OnItemClickListener, AdapterView.OnItemSelectedListener,
        DrawerLayout.DrawerListener {
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    boolean doubleBackToExitPressedOnce = false;
    private static final int REQUEST_CODE = 101;
    // for location
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;
    boolean OnOfffAttendance;
    String attendanceINOUTStatus = "2";
    public File fileSelfie;

    @Override
    protected int getLayoutResId() {
        return R.layout.staff_dashboard;
    }

    @Override
    protected void setUp() {
        // setBackBtn(binding.mainHeader.toolbar);
        setSupportActionBar(binding.mainHeader.toolbar);
        UserDetails userDetails = SharedPreferencesRepository.getDataManagerInstance().getUser();
        binding.drawerLayout.addDrawerListener(this);
        binding.mainHeader.toogleIcon.setOnClickListener(this);
        toggle = new ActionBarDrawerToggle(StaffDashBoardActivity.this, binding.drawerLayout, toolbar,
                0, 0);
        toggle.setDrawerIndicatorEnabled(false);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.menuList.setLayoutManager(new LinearLayoutManager(this));
        binding.menuList.setAdapter(new NavigationAdapter(getMenuList(), userDetails, this));
        NavigationAdapter navigationVLCAdapter = new NavigationAdapter(getMenuList(), userDetails, this);
        navigationVLCAdapter.setOnProfileClickInterface(StaffDashBoardActivity.this);
        binding.menuList.setAdapter(navigationVLCAdapter);


        binding.menuList.addOnItemTouchListener(new RecyclerItemClickListener(StaffDashBoardActivity.this, this));
        binding.mainHeader.tvIvr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singlePerm();
            }
        });
        binding.mainHeader.attendanceOnOff.setOnClickListener(v -> TakeAttendance(OnOfffAttendance));
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fileSelfie = null;
                    binding.selfieImage.setImageBitmap(null);
                    binding.cardAttandance.setVisibility(View.GONE);
                    binding.WelcomeMsg.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        binding.UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageSelected();
            }
        });
        binding.clockInOut.setOnClickListener(v -> callServer());
        getAttendanceStatus();
    }

    private void getAttendanceStatus() {
        apiService.getattendanceStatus().enqueue(new NetworkCallback<AttendanceResponse>(getActivity()) {
            @Override
            protected void onSuccess(AttendanceResponse body) {
                // Toast.makeText(getApplicationContext(),body.getMessage(),Toast.LENGTH_SHORT).show();
                attendanceINOUTStatus = "" + body.getClock_status();
                if (attendanceINOUTStatus.equalsIgnoreCase("1")) {
                    OnOfffAttendance = true;
                    binding.mainHeader.attendanceOnOff.setImageResource(R.drawable.out);
                } else {
                    binding.mainHeader.attendanceOnOff.setImageResource(R.drawable.in);
                    OnOfffAttendance = false;
                }
                getdashboardData();
            }
        });
    }

    private void getdashboardData() {
        apiService.getDashboardData().enqueue(new NetworkCallback<DashBoardData>(getActivity()) {
            @Override
            protected void onSuccess(DashBoardData body) {
               if (body!=null){
                   binding.incase.setText(""+body.getIn_case());
                   binding.outcase.setText(""+body.getOut_case());
                   binding.trotalAttend.setText(""+body.getAtten_month_data());
                   SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                   Date date = new Date();
                   binding.date.setText(""+formatter.format(date));

               }
            }
        });
    }

    private void callServer() {
        String EmployeeImage = "";
        /*HashMap<String,String>map =new HashMap<>();
        map.put("lat",""+latitude);
        map.put("long",""+longitude);
        map.put("clock_status",""+attendanceINOUTStatus);
        map.put("image", Utility.transferImageToBase64(file));*/
        if (fileSelfie != null) {
            EmployeeImage = "" + Utility.transferImageToBase64(fileSelfie);
            apiService.attendance(new AttendancePostData("" + latitude, "" + longitude, "" + attendanceINOUTStatus, EmployeeImage)).enqueue(new NetworkCallback<AttendanceResponse>(getActivity()) {
                @Override
                protected void onSuccess(AttendanceResponse body) {
                    Toast.makeText(StaffDashBoardActivity.this, body.getMessage(), Toast.LENGTH_LONG).show();
                    fileSelfie = null;
                    startActivityAndClear(StaffDashBoardActivity.class);
                    if (body.getClock_status().equalsIgnoreCase("1")) {
                        binding.mainHeader.attendanceOnOff.setImageResource(R.drawable.out);
                        OnOfffAttendance = true;
                    } else {
                        binding.mainHeader.attendanceOnOff.setImageResource(R.drawable.in);
                        OnOfffAttendance = false;
                    }
                }
            });
        } else {
            Toast.makeText(StaffDashBoardActivity.this, "Take your selfie please!", Toast.LENGTH_LONG).show();
        }

    }

    private void TakeAttendance(boolean OnOfffAttendance) {
        OnOfffAttendance = !OnOfffAttendance;
        setFunctional(OnOfffAttendance);
    }

    private void setFunctional(boolean flag) {
        if (flag) {
            attendanceINOUTStatus = "1";
            binding.cardAttandance.setVisibility(View.VISIBLE);
            binding.WelcomeMsg.setVisibility(View.GONE);
            locationget();
          /*  binding.mainHeader.attendanceOnOff.setImageResource(R.drawable.out);
            OnOfffAttendance = true;*/
        } else {
            binding.cardAttandance.setVisibility(View.VISIBLE);
            binding.WelcomeMsg.setVisibility(View.GONE);
            binding.clockIn.setText(getResources().getString(R.string.clock_out));
            binding.clockInOut.setText(getResources().getString(R.string.clock_out));
            locationget();
            attendanceINOUTStatus = "2";
           /* binding.mainHeader.attendanceOnOff.setImageResource(R.drawable.in);
            OnOfffAttendance = false;*/
        }
    }

    private void locationget() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                StaffDashBoardActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                StaffDashBoardActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                //  Toast.makeText(this, "Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void singlePerm() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
//Single Permission is granted
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:+91-7733901154"));
                        startActivity(intent);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
// check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public void openSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StaffDashBoardActivity.this);
        builder.setTitle("Permissions Required");
        builder.setMessage("Permission is required for using this app. Please enable them in app settings.");
        builder.setPositiveButton("Go to SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                showsettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void showsettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toogleIcon:
                toggleDrawer();
                break;
        }
    }

    public void toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.exist_app, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 3000);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void onItemClick(View view, int position) {
        binding.drawerLayout.postDelayed(() -> {
            toggleDrawer();
            switch (position) {
                case 0:
                    break;
                case 1:
                    startActivityAndClear(StaffDashBoardActivity.class);
                    break;
                case 2:
                    startActivity(InPricingListingActivity.class);
                    /*String phone = SharedPreferencesRepository.getDataManagerInstance().getUser().getPhone();
                    String sharedUrl = "click here for download to Farmer App:- https://play.google.com/store/apps/details?id=com.apnagodam&hl=en&referrer=" + phone;
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sharedUrl);
                    sendIntent.setType("text/plain");
                    Intent.createChooser(sendIntent, "Share via");
                    startActivity(sendIntent);
                    Log.e("refer url :", "" + sharedUrl);*/
                    break;
                case 3:
                    startActivity(LanguageActivity.class);
                    //   startActivity(OrderListClass.class);
                    break;
                case 4:
                    startActivity(LeadGenerateClass.class);
                    break;
                case 5:
                    startActivity(CaseIDGenerateClass.class);
                    break;
                case 6:
                    startActivity(TruckBookListingActivity.class);
                    break;
                case 7:
                    startActivity(LabourBookListingActivity.class);
                    break;
                case 8:
                    startActivity(FirstkanthaParchiListingActivity.class);
                    break;
                case 9:
                    //call logout api
                    logout((getResources().getString(R.string.logout_alert)), "Logout");
                    break;
            }
        }, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == UCrop.REQUEST_CROP) {
                    final Uri resultUri = UCrop.getOutput(data);
                    if (resultUri != null) {
                        fileSelfie = new File((compressImage("" + resultUri)));
                    }
                    loadImage(resultUri, binding.selfieImage);
                } else if (requestCode == REQUEST_CAMERA_PICTURE) {
                    File camFile;
                    if (this.camUri != null) {
                        camFile = new File(this.camUri.getPath());
                        this.mediaUri = Utility.getImageContentUri(this, camFile);
                        startCropActivity(mediaUri);
                    }
                }
            }
            if (resultCode == UCrop.RESULT_ERROR) {
                handleCropError(data);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onProfileImgClick() {
        Intent intent = new Intent(this, StaffProfileActivity.class);
        startActivity(intent);
    }
}
