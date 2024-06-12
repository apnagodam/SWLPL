package com.apnagodam.staff.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.fondesa.kpermissions.isDenied
import com.google.android.gms.location.LocationServices
import java.util.Locale

class LocationHelper(var activity: FragmentActivity) : LocationInterface {

    var lat: Double? = null
    var long: Double? = null

    init {
       checkForPermissions()
    }

    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    var currentLocation: Location? = null
    override fun checkForPermissions() {
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
                    getLocation()
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
                    getLocation()
                } else {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
// This will take the user to a page where they have to click twice to drill down to grant the permission
                    activity.startActivity(intent)
                }

            }
        }
    }

    override fun getLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    activity, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    activity, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(activity, "Location not enabled", Toast.LENGTH_SHORT).show()

            } else {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    it?.let {
                        lat = it.latitude
                        long = it.longitude

                        val geocoder = Geocoder(activity, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(lat ?: 0.0, long ?: 0.0, 1)
                        if (addresses != null) {
                            currentLocation = it


                        }
                    }

                }
            }
        } catch (e: Exception) {
            Toast.makeText(activity, "Location not enabled", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showToast(activity: Activity, messageLString: String) =
        Toast.makeText(activity, messageLString, Toast.LENGTH_LONG).show()

}