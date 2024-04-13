package com.apnagodam.staff.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.apnagodam.staff.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationUtils extends LocationCallback {
    private static long INTERVAL = 30000L;
    private static long FASTEST_INTERVAL = 5000L;
    private LocationRequest mLocationRequest;
    private Context context;
    private int REQUEST_CODE;
    private Address geolocation;
    private LocationListener locationListener;
    private boolean continuousUpdate = true;

    public LocationUtils(Context context, LocationListener locationListener, int REQUEST_CODE) {
        this.context = context;
        this.locationListener = locationListener;
        this.REQUEST_CODE = REQUEST_CODE;
        this.askForPermission();
    }

    public LocationUtils(Context context, LocationListener locationListener, int REQUEST_CODE, boolean continuousUpdate) {
        this.context = context;
        this.locationListener = locationListener;
        this.REQUEST_CODE = REQUEST_CODE;
        this.continuousUpdate = continuousUpdate;
        this.askForPermission();
    }

    public LocationUtils(Context context, long interval, long fastest_interval, LocationListener locationListener, int REQUEST_CODE) {
        this.context = context;
        INTERVAL = interval;
        FASTEST_INTERVAL = fastest_interval;
        this.locationListener = locationListener;
        this.REQUEST_CODE = REQUEST_CODE;
        this.askForPermission();
    }

    public LocationUtils(Context context, long interval, long fastest_interval, LocationListener locationListener, int REQUEST_CODE, boolean continuousUpdate) {
        this.context = context;
        INTERVAL = interval;
        FASTEST_INTERVAL = fastest_interval;
        this.locationListener = locationListener;
        this.REQUEST_CODE = REQUEST_CODE;
        this.continuousUpdate = continuousUpdate;
        this.askForPermission();
    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(R.string.need_permission);
        builder.setMessage(R.string.msg_permission);
        builder.setPositiveButton(R.string.permission_alert, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        Uri uri = Uri.fromParts("package", this.context.getPackageName(), (String) null);
        intent.setData(uri);
        ((Activity) this.context).startActivityForResult(intent, 101);
    }

    private void askForPermission() {
        Dexter.withActivity((Activity) this.context).withPermission("android.permission.ACCESS_FINE_LOCATION").withListener(new PermissionListener() {
            public void onPermissionGranted(PermissionGrantedResponse response) {
                setGoogleClient();
            }

            public void onPermissionDenied(PermissionDeniedResponse response) {
                if (response.isPermanentlyDenied()) {
                    showSettingsDialog();
                }

            }

            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void setGoogleClient() {
        this.mLocationRequest = LocationRequest.create();
        this.mLocationRequest.setPriority(100);
        this.mLocationRequest.setInterval(INTERVAL);
        this.mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        com.google.android.gms.location.LocationSettingsRequest.Builder builder = (new com.google.android.gms.location.LocationSettingsRequest.Builder()).addLocationRequest(this.mLocationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this.context).checkLocationSettings(builder.build());
        result.addOnFailureListener((Activity) this.context, new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                if (statusCode == 6) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult((Activity) context, REQUEST_CODE);
                    } catch (IntentSender.SendIntentException error) {
                        error.printStackTrace();
                    }
                }

            }
        });
        result.addOnSuccessListener((Activity) this.context, new OnSuccessListener<LocationSettingsResponse>() {
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this.context, "android.permission.ACCESS_FINE_LOCATION") == 0 && ActivityCompat.checkSelfPermission(this.context, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            LocationServices.getFusedLocationProviderClient(this.context).requestLocationUpdates(this.mLocationRequest, this, (Looper) null);
            Log.d("LocationRequestService", "Location update started ..............: ");
        }
    }

    private void stopLocationUpdates() {
        LocationServices.getFusedLocationProviderClient(this.context).removeLocationUpdates(this);
        Log.d("LocationRequestService", "Location update stopped .......................");
    }

    public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);
        int temp = locationResult.getLocations().size();
        --temp;
        Log.e("Lat", " " + String.valueOf(((Location) locationResult.getLocations().get(temp)).getLatitude()));
        Log.e("Long", " " + String.valueOf(((Location) locationResult.getLocations().get(temp)).getLongitude()));
        if (!continuousUpdate) this.stopLocationUpdates();
        LocationAddress locationAddress = new LocationAddress();
        this.geolocation = locationAddress.getAddressFromLocation(((Location) locationResult.getLocations().get(temp)).getLatitude(), ((Location) locationResult.getLocations().get(temp)).getLongitude(), this.context);
        Log.e("geolocation", " " + this.geolocation);
        this.locationListener.onLocationUpdate((Location) locationResult.getLocations().get(temp), this.geolocation);
    }

    public void onLocationAvailability(LocationAvailability locationAvailability) {
        super.onLocationAvailability(locationAvailability);
        Log.e("tag", "isavail " + locationAvailability.isLocationAvailable());
    }

    public interface LocationListener {
        void onLocationUpdate(Location var1, Address var2);
    }

    public static class LocationAddress {
        private static final String TAG = "LocationAddress";
        Address address;

        public LocationAddress() {
        }

        public Address getAddressFromLocation(double latitude, double longitude, Context context) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            try {
                List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                if (addressList != null && addressList.size() > 0) {
                    this.address = (Address) addressList.get(0);
                }
            } catch (IOException var8) {
                Log.e("LocationAddress", "Unable connect to Geocoder", var8);
            }

            return this.address;
        }
    }

}