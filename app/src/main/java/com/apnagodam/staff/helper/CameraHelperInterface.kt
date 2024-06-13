package com.apnagodam.staff.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.FragmentActivity
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.fondesa.kpermissions.isDenied
import com.github.dhaval2404.imagepicker.ImagePicker
import com.yalantis.ucrop.util.BitmapLoadUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

interface CameraHelperInterface {
     fun capturePic()

     fun selectPic()

     fun selectFile()

     fun checkForPermission(activity: FragmentActivity): Boolean

     fun bitmapToFile(bitmap: Bitmap): Uri

     fun compressImage(imageUri: String?): String

     fun getRealPathFromURI(contentURI: String): String?

     fun getFilename(): String?

     fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}