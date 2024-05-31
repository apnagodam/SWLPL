package com.apnagodam.staff.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.fondesa.kpermissions.isDenied
import com.google.android.gms.location.LocationServices

class LocationHelper {

    fun checkForPermission(activity: FragmentActivity, action: () -> Unit) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.permissionsBuilder(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ).build().send() {
                var isPermissionDenied = false;

                it.forEach {
                    if (it.isDenied()) {
                        showToast(activity, "please grant ${it.permission} from settings")
                        isPermissionDenied = true;
                    } else {
                        isPermissionDenied = false;

                    }
                }
                if (!isPermissionDenied) {
                    action.invoke()
                } else {
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
                        showToast(activity, "please grant ${it.permission} from settings")
                        isPermissionDenied = true;
                    } else {
                        isPermissionDenied = false;

                    }
                }
                if (!isPermissionDenied) {
                    action.invoke()
                } else {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
// This will take the user to a page where they have to click twice to drill down to grant the permission
                    activity.startActivity(intent)
                }

            }
        }


    @SuppressLint("MissingPermission")
    fun locationProvider(activity: Activity, action: (location: Location) -> Unit) =
        LocationServices.getFusedLocationProviderClient(activity).lastLocation.addOnSuccessListener {
            it?.let {
                action.invoke(it)
            }
        }


    fun showToast(activity: Activity, messageLString: String) =
        Toast.makeText(activity, messageLString, Toast.LENGTH_LONG).show()
}