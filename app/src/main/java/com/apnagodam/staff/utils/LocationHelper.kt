package com.apnagodam.staff.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import java.util.Locale

object LocationHelper {
    var lat: Double? = null
    var long: Double? = null
    var currentLocation = ""
    fun getCurrentLocation(activity: Activity) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
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
                            currentLocation =
                                "${addresses.first().featureName},${addresses.first().subAdminArea}, ${addresses.first().locality}, ${
                                    addresses.first().adminArea
                                }"

                        }
                    }


                }
            }
        } catch (e: Exception) {
            Toast.makeText(activity, "Location not enabled", Toast.LENGTH_SHORT).show()
        }

    }
}