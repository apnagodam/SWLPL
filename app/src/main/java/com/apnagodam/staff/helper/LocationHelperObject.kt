package com.apnagodam.staff.helper

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.location.LocationRequest
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.fondesa.kpermissions.isDenied
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import java.util.Locale

object LocationHelperObject {


    fun checkForLocationPermissions(
        activity: FragmentActivity, action: (Boolean) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            activity.permissionsBuilder(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ).build().send() {
                var isPermissionDenied = false;
                it.forEach {
                    if (it.isDenied()) {
                        action.invoke(false)

                        isPermissionDenied = true;
                    } else {
                        action.invoke(true)

                        isPermissionDenied = false;
                    }
                }
                if (!isPermissionDenied) {
                    action.invoke(true)
                } else {
                    action.invoke(false)

                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
// This will take the user to a page where they have to click twice to drill down to grant the permission
                    activity.startActivity(intent)

                }

            }
        } else {
            activity.permissionsBuilder(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ).build().send() {
                var isPermissionDenied = false;

                it.forEach {
                    if (it.isDenied()) {
                        action.invoke(false)

                        isPermissionDenied = true;
                    } else {
                        action.invoke(true)

                        isPermissionDenied = false;

                    }
                }
                if (!isPermissionDenied) {
                    action.invoke(true)

                } else {
                    action.invoke(false)
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
// This will take the user to a page where they have to click twice to drill down to grant the permission
                    activity.startActivity(intent)
                }

            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(activity: Activity): Location? {
        var location: Location? = null
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationProviderClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY,
            object :
                CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                    return CancellationTokenSource().token
                }

                override fun isCancellationRequested(): Boolean {
                    return false
                }

            }).addOnSuccessListener {
            location = it
        }

        return location

    }

    fun getAddress(activity: FragmentActivity, lat: Double, long: Double): String {
        var currentLocation = "";
        val geocoder = Geocoder(activity, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat ?: 0.0, long ?: 0.0, 1)
        if (addresses != null) {
            currentLocation =
                "${addresses.first().featureName},${addresses.first().subAdminArea}, ${addresses.first().locality}, ${
                    addresses.first().adminArea
                }"

        }
        return currentLocation
    }
}