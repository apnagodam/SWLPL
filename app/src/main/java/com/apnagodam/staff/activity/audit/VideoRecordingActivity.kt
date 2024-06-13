package com.apnagodam.staff.activity.audit


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.AuditViewModel
import com.apnagodam.staff.activity.BaseActivity
import com.apnagodam.staff.databinding.ActivityVideoRecordingBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.CommudityResponse
import com.google.gson.Gson
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


@AndroidEntryPoint
class VideoRecordingActivity : BaseActivity<ActivityVideoRecordingBinding>() {
    private var filePath = "";
    lateinit var searchableSpinner: SearchableSpinner
    private val listOfTerminals = arrayListOf<String>()
    private var terminalList = arrayListOf<CommudityResponse.Terminals>()
    val auditViewModel by viewModels<AuditViewModel>()
    private var selectedTerminal = ""

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun setUI() {
        setSupportActionBar(binding.tbVideoRecording)
        getTerminalsFromPreferences()
        checkForPermission {

        }
        binding.btCaptureVideo.setOnClickListener {
            captureVideo()
        }

        binding.btSubmit.setOnClickListener {
            val file = File(filePath)

            if (selectedTerminal.isEmpty()) {
                showToast(this, "Please Select Terminal")

            } else if (binding.edNotes.text.isEmpty()) {
                binding.edNotes.setError("This field is required")
                showToast(this, "Please Enter notes")

            } else if (filePath.isEmpty()) {
                showToast(this, "Please record video")

            } else {
                showDialog(this)
                val multipartBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("terminal_id", selectedTerminal)
                    .addFormDataPart(
                        "commodity_file",
                        file.name,
                        file.asRequestBody()
                    )
                    .build()
                auditViewModel.postVideoResponse(
                    multipartBody
                )
            }
        }
    }

    override fun setObservers() {

        auditViewModel.postVideoResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog(this)
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    hideDialog(this)
                    it.data?.let { response ->
                        response.message?.let { message ->
                            showToast(this, message)
                        }
                        response.status?.let { status ->
                            if (status.equals("1")) {
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityVideoRecordingBinding =
        ActivityVideoRecordingBinding.inflate(layoutInflater)

    override fun callApis() {
    }

    private fun captureVideo() {
        val intent = Intent("android.media.action.VIDEO_CAPTURE")
        intent.putExtra("android.intent.extra.durationLimit", 120)
        startActivityForResult(intent, 12345)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            12345 -> {
                filePath = "";
                data?.let { dataUri ->
                    dataUri.data?.let {
                        val vid: Uri = it
                        filePath = getRealPathFromURI(vid)


                    }
                }

            }

            else -> {}
        }
    }

    private fun getTerminalsFromPreferences() {
        searchableSpinner = SearchableSpinner(this)

        SharedPreferencesRepository.getDataManagerInstance().user.let { userDetails ->
            terminalList.clear()
            listOfTerminals.clear();
            terminalList = SharedPreferencesRepository.getDataManagerInstance().terminals

            searchableSpinner.windowTitle = "Select Terminal"

            for (i in terminalList) {
                listOfTerminals.add(i.name)
            }


            searchableSpinner.setSpinnerListItems(listOfTerminals)

            searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
                override fun setOnItemSelectListener(
                    position: Int,
                    selectedString: String
                ) {
                    binding.tvTerminal.text = selectedString
                    selectedTerminal = terminalList[position].id

                }
            }
            binding.tvTerminal.setOnClickListener { searchableSpinner.show() }
        }

    }

    fun getRealPathFromURI(contentUri: Uri?): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = managedQuery(contentUri, proj, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()

        return cursor.getString(column_index)
    }

}