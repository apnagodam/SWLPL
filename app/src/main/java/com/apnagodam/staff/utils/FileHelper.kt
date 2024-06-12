package com.apnagodam.staff.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class FileHelper(private var activity: Activity) {
    var FILE_REQUEST_CODE = 50
    fun chooseMedia() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        //launch picker screen
        activity.startActivityForResult(intent, FILE_REQUEST_CODE)
    }

    /**
     * Get the extension of the file the provided uri points to
     **/
    fun getFileType(uri: Uri): String? {
        val r = activity.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(r.getType(uri))
    }

    /**
     * Copy contents from input stream to file
     **/
    @Throws(IOException::class)
    fun copyInputStreamToFile(inputStream: InputStream, file: File) {
        try {
            FileOutputStream(file, false).use { outputStream ->
                var read: Int
                val bytes = ByteArray(DEFAULT_BUFFER_SIZE)
                while (inputStream.read(bytes).also { read = it } != -1) {
                    outputStream.write(bytes, 0, read)
                }
            }
        } catch (e: IOException) {
            Log.e("Failed to load file: ", e.message.toString())
        }
    }


}