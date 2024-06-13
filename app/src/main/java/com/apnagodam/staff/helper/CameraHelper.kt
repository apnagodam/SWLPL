package com.apnagodam.staff.helper

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
import android.provider.MediaStore
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.github.dhaval2404.imagepicker.ImagePicker
import com.yalantis.ucrop.util.BitmapLoadUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

object CameraHelper {
    var imageFile: File? = null
    var fileUri: Uri? = null
    fun captureImage(activity: Activity) {
        ImagePicker.with(activity).cameraOnly().start();
    }

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        activity: Activity,
        currentLocation: String
    ) {

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
                thumbnail?.let {
                    bitmapToFile(it, activity)?.let { bitmapFile ->
                        compressImage(bitmapFile.path, activity)?.let { compressedImage ->
                            var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                                File(compressedImage), stampMap
                            )
                            bitmapToFile(stampedBitmap, activity)?.let { finalUri ->
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
//                if (thumbnail != null) {
//
//
//                } else if (resultCode == ImagePicker.RESULT_ERROR) {
//                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
//                }

            }
        } catch (e: Exception) {
            // showToast(this, "Please Select an Image")
        }
//        photoEasy.onActivityResult(1566, -1, object : OnPictureReady {
//            override fun onFinish(thumbnail: Bitmap?) {
//
//            }
//
//        })

    }

    fun bitmapToFile(bitmap: Bitmap, context: Context): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(context)

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


    fun getFilename(activity: Activity): String? {
        //    File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        val finalPath: String =
            activity.filesDir.toString() + File.separator + System.currentTimeMillis() + ".jpg"
        val file = File(finalPath)
        if (!file.exists()) {
            //        file.mkdirs();
            file.parentFile.mkdirs()
        }
        //    String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return file.absolutePath
    }

    fun getRealPathFromURI(contentURI: String, activity: Activity): String? {
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

    fun compressImage(imageUri: String?, activity: Activity): String? {
        val filePath: String? = getRealPathFromURI(imageUri ?: "", activity)
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
        val filename: String = getFilename(activity) ?: ""
        try {
            out = FileOutputStream(filename)
            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return filename
    }
}