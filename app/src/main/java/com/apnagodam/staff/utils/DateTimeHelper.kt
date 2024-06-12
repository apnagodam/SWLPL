package com.apnagodam.staff.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateTimeHelper {


    fun date(): Date = Date()


    fun compareDate(createdDate: String?): String {
        var compareDate = ""
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata")) // (GMT
        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.ENGLISH
        )
        try {

            val time = SimpleDateFormat(
                "EEE MMM dd HH:mm:ss ZZZZZ yyyy",
                Locale.ENGLISH
            ).parse(Date().toString())
            val oldTime = dateFormat.parse(createdDate.toString())
            val diff = time.time - oldTime.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            if (minutes > 60) {
                compareDate =
                    "$hours hours and ${minutes - (60 * hours)} minutes"

            } else {
                compareDate =
                    "${minutes} minutes"
            }


            // Log.e("toyBornTime", "" + toyBornTime);
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return compareDate;
    }

    fun getCurrentDateTime():String{
        var compareDate = ""
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata")) // (GMT
        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd h:mm:ss", Locale.ENGLISH
        )
        try {

            val time = SimpleDateFormat(
                "EEE MMM dd h:mm:ss ZZZZZ yyyy",
                Locale.ENGLISH
            ).parse(Date().toString())
          compareDate=time.toString()


            // Log.e("toyBornTime", "" + toyBornTime);
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return compareDate;
    }

    fun <T> timeDifference(createdDate: T): Long {
        var compareDate = Date()
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata")) // (GMT
        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.ENGLISH
        )
        val time = SimpleDateFormat(
            "EEE MMM dd HH:mm:ss ZZZZZ yyyy",
            Locale.ENGLISH
        ).parse(Date().toString())
        val oldTime = dateFormat.parse(createdDate.toString())
        val diff = time.time - oldTime.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        return minutes
    }


    fun formatDate(dateString: String): String {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/India")) // (GMT+11:00)
        val locale = Locale.ENGLISH
        val time =
            SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", locale).parse(dateString)?.time
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale).format(time)
    }
}