package com.apnagodam.staff.attendance

import android.app.Activity
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.AttendancePostData
import com.apnagodam.staff.Network.viewmodel.AttendenceViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.BaseActivity
import com.apnagodam.staff.databinding.ActivityAttendanceBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.helper.ImageHelper
import com.apnagodam.staff.utils.Utility
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AttendanceActivity : BaseActivity<ActivityAttendanceBinding>() {
    val attandanceViewModel by viewModels<AttendenceViewModel>()


    override fun setUI() {
        getCurrentLocation()
        binding.ivAttendanceImage.setOnClickListener {
            //     captureImage()
            ImagePicker.with(this)
                .compress(1024)
                .cameraOnly()//Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    pictureResult.launch(intent)
                }

        }

        binding.btClockOut.setOnClickListener {
            if (attendanceFile.value == null) {
                showToast(this, "Please select an image!")
            } else {
                attandanceViewModel.setAttandance(
                    AttendancePostData(
                        lat.toString(),
                        long.toString(),
                        "2",
                        Utility.transferImageToBase64(attendanceFile.value),

                        )
                )

            }
        }


        binding.btClockIn.setOnClickListener {
            if (attendanceFile.value == null) {
                showToast(this, "Please select an image!")
            } else {
                attandanceViewModel.setAttandance(
                    AttendancePostData(
                        lat.toString(),
                        long.toString(),
                        "1",
                        Utility.transferImageToBase64(attendanceFile.value),
                    )
                )
            }
        }
    }

    private val pictureResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { uri ->
            val resultCode = uri.resultCode
            val data = uri.data
            val userDetails = SharedPreferencesRepository.getDataManagerInstance().user

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!


                var stampMap = hashMapOf(
                    "current_location" to "$currentLocation",
                    "emp_code" to userDetails.emp_id,
                    "emp_name" to userDetails.fname
                )
                val thumbnail = MediaStore.Images.Media.getBitmap(this.contentResolver, fileUri)
                var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                    File(compressImage(bitmapToFile(thumbnail!!).path)), stampMap
                )
                attendanceFile.value = File(bitmapToFile(stampedBitmap!!).path)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun setObservers() {
        attendanceFile.observe(this) {
            it?.let {
                val bitmap = BitmapFactory.decodeFile(it.path)
                binding.ivAttendanceImage.setImageBitmap(bitmap);
            }
        }
        attandanceViewModel.attendenceRespons.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()

                }

                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {

                    it.data?.let { data ->
                        data?.clock_status?.let {

                            if (it.toString() == "2") {

                                binding.btClockOut.isEnabled = false
                                binding.btClockOut.setBackgroundResource(R.drawable.btn_green_background_disabled)
                                binding.btClockOut.setBackgroundColor(
                                    getResources().getColor(R.color.grey)
                                )
                                binding.btClockIn.isEnabled = true
                                binding.btClockIn.setBackgroundResource(R.drawable.btn_green_background)
                                binding.btClockIn.setBackgroundColor(
                                    getResources().getColor(R.color.colorPrimary)
                                )

                            } else {
                                binding.btClockOut.isEnabled = true
                                binding.btClockOut.setBackgroundResource(R.drawable.btn_green_background)
                                binding.btClockOut.setBackgroundColor(
                                    getResources().getColor(R.color.colorPrimary)
                                )
                                binding.btClockIn.isEnabled = false
                                binding.btClockIn.setBackgroundResource(R.drawable.btn_green_background_disabled)
                                binding.btClockIn.setBackgroundColor(
                                    getResources().getColor(R.color.grey)
                                )
                            }


                        }
                    }


                }
            }
        }

        attandanceViewModel.setAttandanceResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    it.data?.let { data ->

                        data.message?.let { message ->
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        }

                        data?.status?.let { status ->
                            if (status == "1") {
                                attandanceViewModel.checkClockStatus()
                                finish()
                            }
                        }
                    }
                }
            }
        }

    }

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityAttendanceBinding =
        ActivityAttendanceBinding.inflate(layoutInflater)

    override fun callApis() {


        attandanceViewModel.checkClockStatus()
    }
}