package com.apnagodam.staff.activity.`in`.ivr

import android.content.Intent
import android.view.LayoutInflater
import com.apnagodam.staff.activity.BaseActivity
import com.apnagodam.staff.databinding.ActivityQualityTaggingBinding
import com.apnagodam.staff.utils.CameraHelperImpl
import com.apnagodam.staff.utils.StringConstants

class QualityTaggingActivity : BaseActivity<ActivityQualityTaggingBinding>() {

    private val cameraHelperImpl = CameraHelperImpl(this, currentLocation)
    private var caseId: String? = null
    private var userName: String? = null
    private var inOutType: String? = null
    private var vehicleNo: String? = null
    override fun setUI() {
        setSupportActionBar(binding.tbQualityTagging)
        binding.tbQualityTagging.let {
            it.setNavigationOnClickListener {
                finish()
            }
        }
        intent?.let { value ->
            caseId = value.getStringExtra(StringConstants.CASE_ID)
            userName = value.getStringExtra(StringConstants.USER_NAME)
            inOutType = value.getStringExtra(StringConstants.IN_OUT)
            vehicleNo = value.getStringExtra(StringConstants.VEHICLE_NO)
        }
        binding.tvCaseId.text = caseId ?: ""
        binding.tvCustomerName.text = userName ?: ""
        binding.btSelectAudioFile.setOnClickListener {
            cameraHelperImpl.selectFile()
        }
        binding.btSelectIvrFile.also {
            it.setOnClickListener {
                cameraHelperImpl.capturePic()
            }
        }
        binding.btSubmit.setOnClickListener {
            if (validateFields()) {
                showToast(this, "Success")
            } else {
                pickedFile.value = null
                imageFile.value = null
            }
        }
    }

    private fun validateFields(): Boolean {
        if (pickedFile.value == null) {
            showToast(this, "Please select Audio File")
            return false
        }
        if (imageFile.value == null) {
            showToast(this, "Please select Ivr Image File")
            return false
        }

        if (binding.edNotes.text.isEmpty()) {
            binding.edNotes.error = "This Field cannot be empty"
            return false
        }
        return true;
    }

    override fun setObservers() {
        pickedFile.observe(this) {
            it?.let { file ->
                binding.btSelectAudioFile.setText(file.path)
            }
        }
        imageFile.observe(this) { file ->
            file?.let {
                binding.btSelectIvrFile.setText(it.path)
            }
        }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityQualityTaggingBinding =
        ActivityQualityTaggingBinding.inflate(layoutInflater)

    override fun callApis() {
    }

}