package com.apnagodam.staff.activity.`in`.ivr.ivr_tagging

import android.content.Intent
import android.view.LayoutInflater
import com.apnagodam.staff.activity.BaseActivity
import com.apnagodam.staff.databinding.ActivityIvrTaggingBinding
import com.apnagodam.staff.utils.CameraHelperImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IvrTaggingActivity : BaseActivity<ActivityIvrTaggingBinding>() {

    private var cameraHelperImpl = CameraHelperImpl(this, currentLocation)
    override fun setUI() {
        setSupportActionBar(binding.tbTagging)
        binding.tbTagging.let {
            it.setNavigationOnClickListener {
                finish()
            }
        }
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

        binding.tilSecondKantaWeightKg.editText?.let {
            if (it.text.isEmpty()) {
                it.error = "This Field Cannot be empty"

                return false
            }
        }
        binding.tilSecondKantaWeightQtl.editText?.let {
            if (it.text.isEmpty()) {
                it.error = "This Field Cannot be empty"

                return false
            }

        }
        binding.tilSecondKantaBags.editText?.let {
            if (it.text.isEmpty()) {
                it.error = "This Field Cannot be empty"
                return false
            }
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
            file?.let { file ->
                binding.btSelectIvrFile.setText(file.path)
            }
        }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityIvrTaggingBinding =
        ActivityIvrTaggingBinding.inflate(layoutInflater)

    override fun callApis() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        cameraHelperImpl.onActivityResult(requestCode, resultCode, data)
    }

}