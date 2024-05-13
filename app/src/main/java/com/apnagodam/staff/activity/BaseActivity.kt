package com.apnagodam.staff.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.apnagodam.staff.R
import com.apnagodam.staff.utils.CustomProgressDialog
import com.apnagodam.staff.utils.Utility
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

abstract class BaseActivity<VB : ViewBinding> :
    AppCompatActivity() {
    protected lateinit var binding: VB
    protected var mCustomProgressDialog: CustomProgressDialog? = null
    protected var lat: Double? = null
    protected var long: Double? = null
    protected var currentLocation = ""
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateLayout(layoutInflater)
        setContentView(binding.root)
        setUI()
        setObservers()
        callApis()
    }

    abstract fun setUI();
    abstract fun setObservers();
    abstract fun inflateLayout(layoutInflater: LayoutInflater): VB
    abstract fun callApis();

    protected fun showToast(activity: Activity, messageLString: String) =
        Toast.makeText(activity, messageLString, Toast.LENGTH_LONG).show()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    protected fun checkForPermission(action: () -> Unit) = permissionsBuilder(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ).build().send() {
        if (it.allGranted()) {
            action.invoke()
        }

    }


    protected fun showDialog(activity: Activity) {
        mCustomProgressDialog = CustomProgressDialog(activity)
        mCustomProgressDialog?.let {
            it.show()
        }
    }

    protected fun getCurrentLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Location not enabled", Toast.LENGTH_SHORT).show()

            } else {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    it?.let {
                        lat = it.latitude
                        long = it.longitude

                        val geocoder = Geocoder(this, Locale.getDefault())
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
            Toast.makeText(this, "Location not enabled", Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * Hide Progress Dialog
     */
    protected fun hideDialog(activity: Activity) {
        mCustomProgressDialog = CustomProgressDialog(activity)
        mCustomProgressDialog?.let {
            it.dismiss()
        }
    }

    private fun getCurrentDate() =
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

    private fun getCurrentTime() = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
}