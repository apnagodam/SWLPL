package com.apnagodam.staff.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar

class ImageHelper {


    fun createTimeStampinBitmap(file: File, map: Map<String, String>): Bitmap {


        var options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inDither = false
        val src = BitmapFactory.decodeFile(
            file!!.path,
            options

        ) // the original file is cuty.jpg i added in resources

        val mutableBitmap: Bitmap = Bitmap.createScaledBitmap(src, src.width, src.height, false)


        val dest = mutableBitmap.copy(Bitmap.Config.ARGB_8888, true)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateTime =
            sdf.format(Calendar.getInstance().time) // reading local time in the system


        val cs = Canvas(dest)
        val tPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        tPaint.textSize = 32f
        tPaint.color = Color.WHITE
        tPaint.style = Paint.Style.FILL
        cs.drawBitmap(src, 0f, 0f, null)
        val height = tPaint.measureText("yY")
        cs.drawText(dateTime, 0f, dest.height.toFloat() - 5f, tPaint)
        cs.drawText(map.get("current_location").toString(), 0f, dest.height.toFloat() - 60f, tPaint)
        cs.drawText(map.get("emp_name").toString(), 0f, dest.height.toFloat() - 120f, tPaint)

        cs.drawText(map.get("emp_code").toString(), 0f, dest.height.toFloat() - 150f, tPaint)


        try {
            dest.compress(
                Bitmap.CompressFormat.PNG,
                100,
                FileOutputStream(File("/sdcard/timeStampedImage.jpg"))
            )
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return dest
    }
}