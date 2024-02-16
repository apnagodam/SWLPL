package com.apnagodam.staff.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.ActivityMobileMapsBinding;
import com.apnagodam.staff.module.MobileNUmberPojo;
import com.apnagodam.staff.utils.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MobileNumberMapActivity extends BaseActivity<ActivityMobileMapsBinding> implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback{
    private GoogleMap mMap;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private GoogleMap googleMapHome;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mobile_maps;
    }

    @Override
    protected void setUp() {
        setBackBtn(binding.include.toolbar);
        binding.include.titleTxt.setText("Customer Route  Map");
        binding.include.tvDone.setVisibility(View.GONE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    if (googleMapHome != null) {
                        googleMapHome.clear();
                        stateMarkers(stringFromView(binding.etPhoneNumber));
                    }
                }
            }
        });

    }

    boolean isValid() {
        if (TextUtils.isEmpty(stringFromView(binding.etPhoneNumber))) {
            return Utility.showEditTextError(binding.tilPhoneNumber, R.string.phone_number);
        }
        return true;
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    //   Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MobileNumberMapActivity.this);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(StaffDashBoardActivity.class);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMapHome = googleMap;
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true); // false to disable
        googleMap.getUiSettings().setZoomControlsEnabled(true); // true to enable
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }


    //create marker on lat lng
    protected Marker createMarker(double latitude, double longitude, String title, String snippet, float hueOrange) {
        return googleMapHome.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(hueOrange))
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    //Booking Your Stocks to LG
    public void stateMarkers(String phone) {
        apiService.routeMap(phone).enqueue(new NetworkCallback<MobileNUmberPojo>(this) {
            @Override
            protected void onSuccess(MobileNUmberPojo body) {
                    if (body.getData() != null) {
                        if (body.getRole().contains("2")){
                            LatLng latLng = new LatLng(Double.parseDouble(body.getData().getLiveLatitude()),Double.parseDouble(body.getData().getLiveLongitude()));
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(10).build();
                            googleMapHome.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            createMarker(Double.parseDouble(body.getData().getLiveLatitude()), Double.parseDouble(body.getData().getLiveLongitude()), body.getData().getFname(), body.getData().getPhone(), BitmapDescriptorFactory.HUE_GREEN);
                        }else {

                            LatLng latLng = new LatLng(Double.parseDouble(body.getData().getLatitude()),Double.parseDouble(body.getData().getLongitude()));
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(10).build();
                            googleMapHome.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            createMarker(Double.parseDouble(body.getData().getLatitude()), Double.parseDouble(body.getData().getLongitude()), body.getData().getFname(), body.getData().getPhone(), BitmapDescriptorFactory.HUE_GREEN);
                        }
                    }

            }
        });
    }

}