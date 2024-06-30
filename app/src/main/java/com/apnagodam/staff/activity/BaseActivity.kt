package com.apnagodam.staff.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.exifinterface.media.ExifInterface
import androidx.viewbinding.ViewBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.utils.CustomProgressDialog
import com.apnagodam.staff.helper.ImageHelper
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.fondesa.kpermissions.isDenied
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.LocationServices
import com.yalantis.ucrop.util.BitmapLoadUtils.calculateInSampleSize
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: VB
    protected var mCustomProgressDialog: CustomProgressDialog? = null
    protected var lat: Double? = null
    protected var long: Double? = null
    protected var currentLocation = ""
    protected var imageFile: File? = null

    protected var fileUri: Uri? = null
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

    protected fun checkForPermission(action: () -> Unit) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsBuilder(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,

                ).build().send() {
                var isPermissionDenied = false;

                it.forEach {
                    if (it.isDenied()) {
                        showToast(this, "please grant ${it.permission} from settings")
                        isPermissionDenied = true;
                    } else {
                        isPermissionDenied = false;

                    }
                }
                if (!isPermissionDenied) {
                    action.invoke()
                } else {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
// This will take the user to a page where they have to click twice to drill down to grant the permission
                    startActivity(intent)

                }

            }
        } else {
            permissionsBuilder(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,

                ).build().send() {
                var isPermissionDenied = false;

                it.forEach {
                    if (it.isDenied()) {
                        showToast(this, "please grant ${it.permission} from settings")
                        isPermissionDenied = true;
                    } else {
                        isPermissionDenied = false;

                    }
                }
                if (!isPermissionDenied) {
                    action.invoke()
                } else {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
// This will take the user to a page where they have to click twice to drill down to grant the permission
                    startActivity(intent)
                }

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
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
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

    open fun compressImage(imageUri: String?): String? {
        val filePath: String? = getRealPathFromURI(imageUri ?: "")
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

//      max Height and width values of the compressed image is taken as 816x612
        val maxHeight = 916.0f
        val maxWidth = 712.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

//      width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG)
        )

//      check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath ?: "")
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap!!, 0, 0, scaledBitmap!!.width, scaledBitmap!!.height, matrix, true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var out: FileOutputStream? = null
        val filename: String = getFilename() ?: ""
        try {
            out = FileOutputStream(filename)
            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return filename
    }

    protected fun captureImage() {
        ImagePicker.with(this).cameraOnly().start();

    }

    var photResult = registerForActivityResult(ActivityResultContracts.GetContent()){
        try {
            val userDetails = SharedPreferencesRepository.getDataManagerInstance().user
            it?.let {uri->
                var stampMap = mapOf(
                    "current_location" to "$currentLocation",
                    "emp_code" to userDetails.emp_id,
                    "emp_name" to userDetails.fname
                )
                val thumbnail = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                thumbnail?.let {
                    bitmapToFile(it)?.let { bitmapFile ->
                        compressImage(bitmapFile.path)?.let { compressedImage ->
                            var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                                File(compressedImage), stampMap
                            )
                            bitmapToFile(stampedBitmap)?.let { finalUri ->
                                finalUri?.let { finalImage ->
                                    finalImage.path?.let {
                                        imageFile = File(it)
                                        fileUri = Uri.fromFile(imageFile)
                                    }

                                }

                            }

                        }

                    }

                }
            }


//                if (thumbnail != null) {
//
//
//                } else if (resultCode == ImagePicker.RESULT_ERROR) {
//                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
//                }
        } catch (e: Exception) {
            showToast(this, "Please Select an Image")
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


//        photoEasy.onActivityResult(1566, -1, object : OnPictureReady {
//            override fun onFinish(thumbnail: Bitmap?) {
//
//            }
//
//        })

    }

    protected fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }

    //    public String getFilename() {
    //        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
    //        if (!file.exists()) {
    //            file.mkdirs();
    //        }
    //        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
    //        return uriSting;
    //    }
    protected fun getFilename(): String? {
        //    File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        val finalPath: String =
            this.filesDir.toString() + File.separator + System.currentTimeMillis() + ".jpg"
        val file = File(finalPath)
        if (!file.exists()) {
            //        file.mkdirs();
            file.parentFile.mkdirs()
        }
        //    String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return file.absolutePath
    }

    protected fun getRealPathFromURI(contentURI: String): String? {
        val contentUri = Uri.parse(contentURI)
        val cursor = contentResolver.query(contentUri, null, null, null, null)
        return if (cursor == null) {
            contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(index)
        }
    }

}