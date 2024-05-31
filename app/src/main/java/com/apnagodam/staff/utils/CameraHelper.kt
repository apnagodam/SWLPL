package com.apnagodam.staff.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.fragment.app.FragmentActivity

abstract class CameraHelper {
    abstract fun capturePic()
    abstract fun selectPic()

    abstract fun selectFile()

    abstract fun checkForPermission(activity: FragmentActivity) :Boolean

    abstract fun bitmapToFile(bitmap: Bitmap):Uri

    abstract fun compressImage(imageUri: String?):String

    abstract  fun getRealPathFromURI(contentURI: String):String?
    abstract fun getFilename(): String?

    abstract fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

}