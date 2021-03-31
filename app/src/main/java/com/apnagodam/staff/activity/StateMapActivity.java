package com.apnagodam.staff.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.ActivityStateMapsBinding;
import com.apnagodam.staff.module.StateMapModel;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class StateMapActivity extends BaseActivity<ActivityStateMapsBinding> implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, AdapterView.OnItemSelectedListener {
    private GoogleMap mMap;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private GoogleMap googleMapHome;
    private String[] stateArray;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_state_maps;
    }

    @Override
    protected void setUp() {
        setBackBtn(binding.include.toolbar);
        binding.include.titleTxt.setText("State Map");
        binding.include.tvDone.setVisibility(View.GONE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        stateArray = getResources().getStringArray(R.array.state);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnState.setAdapter(dataAdapter);
        binding.spnState.setOnItemSelectedListener(this);
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
                    supportMapFragment.getMapAsync(StateMapActivity.this);
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
    public void stateMarkers(String name) {
        apiService.apnaEmpStatewiseUserHeatmap(name).enqueue(new NetworkCallback<StateMapModel>(this) {
            @Override
            protected void onSuccess(StateMapModel body) {
                binding.txtShowCount.setVisibility(View.GONE);
                if (body.getStatus() == 1) {
                    if (!body.data.isEmpty()) {
                        LatLng latLng = new LatLng(body.data.get(0).latitude, body.data.get(0).longitude);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(10).build();
                        googleMapHome.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        for (StateMapModel.Datum warehouse : body.data) {
                            createMarker(warehouse.latitude, warehouse.longitude, warehouse.name, "", BitmapDescriptorFactory.HUE_RED);
                        }
                    }
                    if (!body.userDetail.isEmpty()) {
                        if (body.data.isEmpty()) {
                            LatLng latLng = new LatLng(body.userDetail.get(0).latitude, body.userDetail.get(0).longitude);
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(10).build();
                            googleMapHome.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                        for (StateMapModel.UserDetail userDetail : body.userDetail) {
                            String name = userDetail.fname;
                            if (userDetail.lname != null && !userDetail.lname.isEmpty()) {
                                name = name + " " + userDetail.lname;
                            }
                            createMarker(userDetail.latitude, userDetail.longitude, name, userDetail.phone, BitmapDescriptorFactory.HUE_GREEN);
                        }
                    }
                }
                if (!body.data.isEmpty() || !body.userDetail.isEmpty()) {
                    binding.txtShowCount.setVisibility(View.VISIBLE);
                    binding.txtShowCount.setText(String.format("User = %s   Warehouse  = %s",  body.userDetail.size(),body.data.size()));
                }
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getSelectedItem().toString();
        if (googleMapHome != null) {
            googleMapHome.clear();
            setTestMap(text);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void setTestMap(String name) {
        binding.txtShowCount.setVisibility(View.GONE);
        if (name.equalsIgnoreCase(stateArray[1])) {
            stateMarkers("Bihar");
        } else if (name.equalsIgnoreCase(stateArray[2])) {
            stateMarkers("Punjab");
        } else if (name.equalsIgnoreCase(stateArray[3])) {
            stateMarkers("Haryana");
        } else if (name.equalsIgnoreCase(stateArray[4])) {
            stateMarkers("Uttar pradesh");
        }else if (name.equalsIgnoreCase(stateArray[5])) {
            stateMarkers("Rajasthan");
        }
    }

}