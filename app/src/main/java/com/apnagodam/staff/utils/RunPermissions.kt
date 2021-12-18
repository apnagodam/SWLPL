package com.apnagodam.staff.utils


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*


class RunPermissions(context: Context) : PermissionListener {
    var mContext: Context;

    init {
        this.mContext = context;
    }

    lateinit var CallBackPermission: CallBackPermission;
    val galleryPermission = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val cameraPermission = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        Toast.makeText(mContext, "onPermissionGranted", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionRationaleShouldBeShown(
            permission: PermissionRequest?,
            token: PermissionToken
    ) {
        token.continuePermissionRequest()
        Toast.makeText(mContext, "onPermissionRationaleShouldBeShown", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse) {
        if (response.isPermanentlyDenied()) {
            Toast.makeText(mContext, "isPermanentlyDenied", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(mContext, "onPermissionDenied", Toast.LENGTH_LONG).show()
        }
    }

    fun requestCameraPermissions() {
        ActivityCompat.requestPermissions(
                mContext as Activity,
                cameraPermission,
                1000
        )
    }

    fun permissionSingle() {
        Dexter.withActivity(mContext as Activity)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(this).check();
    }

    fun permissionMulti(callBackPermission: CallBackPermission, permission: Permission) {
        Dexter.withActivity(mContext as Activity)
                .withPermissions(
                        getPermission(permission)
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        /* if (report.areAllPermissionsGranted()) {
                             // do you work now

                         }*/

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog()
                            // permission is denied permenantly, navigate user to app settings
                        } else {
                            callBackPermission.onGranted(true, permission)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            permissions: List<PermissionRequest>,
                            token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
    }

    private fun cameraPermissions(): List<String> {
        var dataList: MutableList<String> = ArrayList()
        dataList.add(Manifest.permission.CAMERA)
        dataList.add(Manifest.permission.RECORD_AUDIO)
        dataList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        dataList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return dataList
    }

    private fun gelleryPermissions(): List<String> {
        var dataList: MutableList<String> = ArrayList()
        dataList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        dataList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return dataList
    }

    private fun locationPermissions(): List<String> {
        var dataList: MutableList<String> = ArrayList()
        dataList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        dataList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return dataList
    }

    private fun getPermission(permission: Permission): List<String> {
        return if (permission == Permission.CAMERA) cameraPermissions() else gelleryPermissions()
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS",
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                    openSettings()
                })
        builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()

    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", mContext.getPackageName(), null)
        intent.setData(uri)
        (mContext as Activity).startActivityForResult(intent, 1110)
    }

}