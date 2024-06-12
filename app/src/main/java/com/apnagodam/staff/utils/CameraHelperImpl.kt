package com.apnagodam.staff.utils

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

class CameraHelperImpl(
    private var activity: FragmentActivity,
    private var currentLocation: String
) : CameraHelper() {

    private val fileHelper = FileHelper(activity);

    private val FILE_REQUEST_CODE = 50

    init {
        checkForPermission(activity)
    }

    override fun capturePic() {
        ImagePicker.with(activity).cameraOnly().start();
    }

    override fun selectPic() {
        ImagePicker.with(activity).galleryOnly().start();

    }

    override fun selectFile() {
        fileHelper.chooseMedia()
    }

    override fun checkForPermission(activity: FragmentActivity): Boolean {
        var isPermissionDenied = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.permissionsBuilder(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,

                ).build().send() {

                it.forEach {
                    if (it.isDenied()) {
                        Toast.makeText(
                            activity,
                            "please grant ${it.permission} from settings",
                            Toast.LENGTH_SHORT
                        ).show()
                        isPermissionDenied = true;
                    } else {
                        isPermissionDenied = false;

                    }
                }
                if (!isPermissionDenied) {
                    //action.invoke()
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
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,

                ).build().send() {
                isPermissionDenied = false;

                it.forEach {
                    if (it.isDenied()) {
                        Toast.makeText(
                            activity,
                            "please grant ${it.permission} from settings",
                            Toast.LENGTH_SHORT
                        ).show()

                        isPermissionDenied = true;
                    } else {
                        isPermissionDenied = false;

                    }
                }
                if (!isPermissionDenied) {
                    //action.invoke()
                } else {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
// This will take the user to a page where they have to click twice to drill down to grant the permission
                    activity.startActivity(intent)
                }

            }
        }
        return isPermissionDenied
    }

    override fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(activity.applicationContext)

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

    override fun compressImage(imageUri: String?): String {
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
        options.inSampleSize =
            BitmapLoadUtils.calculateInSampleSize(options, actualWidth, actualHeight)

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
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
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
                scaledBitmap!!, 0, 0,
                scaledBitmap!!.width, scaledBitmap!!.height, matrix,
                true
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

    override fun getRealPathFromURI(contentURI: String): String? {
        val contentUri = Uri.parse(contentURI)
        val cursor = activity.contentResolver.query(contentUri, null, null, null, null)
        return if (cursor == null) {
            contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(index)
        }
    }

    override fun getFilename(): String? {
        //    File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        val finalPath: String = activity.filesDir
            .toString() + File.separator + System.currentTimeMillis() + ".jpg"
        val file = File(finalPath)
        if (!file.exists()) {
            //        file.mkdirs();
            file.parentFile?.mkdirs()
        }
        //    String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return file.absolutePath
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            //Get URI pointing to the file that was selected from the user
            data.data?.let { uri ->

                //Get file extension e.g mp4, flv, pdf...
                val fileType = FileHelper(activity).getFileType(uri)

                //Temporary file to hold content of actual file
                val file = File.createTempFile("vid", fileType)

                // pickedFile.value = file
                //Copy content from actual file to Temporary file using Input Stream
                fileHelper.copyInputStreamToFile(
                    inputStream = activity.contentResolver.openInputStream(uri)!!,
                    file = file
                )
                //At this point the Temporary file object is ready, you can upload or use as needed

            }
        }
        try {
            if (requestCode == Activity.RESULT_OK || requestCode == 2404) {
                val userDetails = SharedPreferencesRepository.getDataManagerInstance().user
                val uri: Uri = data?.data!!

                var stampMap = mapOf(
                    "current_location" to "$currentLocation",
                    "emp_code" to userDetails.emp_id,
                    "emp_name" to userDetails.fname
                )
                val thumbnail = MediaStore.Images.Media.getBitmap(activity.contentResolver, uri)
                if (thumbnail != null) {
                    var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                        File(compressImage(bitmapToFile(thumbnail).path)), stampMap
                    )

                    //imageFile.value = File(compressImage(bitmapToFile(stampedBitmap).path))


                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }

            }
        } catch (e: Exception) {
            Toast.makeText(activity, "Please select Image", Toast.LENGTH_SHORT).show()
        }


    }
}