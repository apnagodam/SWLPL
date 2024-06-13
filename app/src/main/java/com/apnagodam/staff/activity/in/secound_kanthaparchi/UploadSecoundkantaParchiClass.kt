package com.apnagodam.staff.activity.`in`.secound_kanthaparchi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadSecoundkantaParchiPostData
import com.apnagodam.staff.Network.viewmodel.KantaParchiViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.BaseActivity
import com.apnagodam.staff.databinding.KanthaParchiUploadBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.SecoundkanthaParchiListResponse
import com.apnagodam.staff.helper.ImageHelper
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import com.apnagodam.staff.utils.Validationhelper
import com.fxn.pix.Options
import com.github.dhaval2404.imagepicker.ImagePicker
import com.thorny.photoeasy.PhotoEasy
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.DecimalFormat

@AndroidEntryPoint
class UploadSecoundkantaParchiClass : BaseActivity<KanthaParchiUploadBinding>(),
    RadioGroup.OnCheckedChangeListener {
    // role of image
    var UserName: String? = null
    var CaseID: String? = ""
    var fileKantha: File? = null
    var fileTruck: File? = null
    var fileTruck2: File? = null
    var firstKanthaFile = false
    var truckImage = false
    var truckImage2 = false;
    private var firstkantaParchiFile: String? = null
    private var TruckImage: String? = null
    private var TruckImage2: String? = null
    var options: Options? = null
    lateinit var allCases: SecoundkanthaParchiListResponse.Datum
    var InTrackType: String? = "null"
    var truckFacilityId = 0
    val kantaParchiViewModel by viewModels<KantaParchiViewModel>()
    var InBardhanaType: String? = "null"
    var bagsFacilityID = 0
    var kantaId = 0;
    var kantaName = ""
    var kantaParchiNumber = ""
    var isFirstUpload = true;


    lateinit var photoEasy: PhotoEasy
    override fun setUI() {
        binding!!.tilOldWeightQt.visibility = View.VISIBLE
        binding!!.etKantaOldLocation.visibility = View.GONE
        binding!!.genderRadio.setOnCheckedChangeListener(this)
        binding!!.gender.setOnCheckedChangeListener(
            this
        )
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.customerName.text = UserName
        binding!!.caseId.text = CaseID
        clickListner()

        photoEasy = PhotoEasy.builder().setActivity(this)
            .build()
        getCurrentLocation()

        binding!!.etNoOfDispleasedBags.setText("0")

        binding!!.tvTitle.setText("Upload Second Kanta Parchi")
        binding!!.etKantaParchiNum.isEnabled = false
        binding!!.llOldBags.visibility = View.GONE
        binding!!.llBags.visibility = View.VISIBLE
        binding!!.etWeightKg.doOnTextChanged { text, start, before, count ->
            if (binding!!.etWeightKg.text!!.isNotEmpty() && text != null && text != "0" && binding!!.etWeightKg.text!!.isNotEmpty()) {
                try {
                    var bagCal = (binding!!.etWeightKg.text.toString().toDouble() / 100)
                    binding!!.etWeight.setText(
                        dtime.format(bagCal)
                    )
                } catch (e: Exception) {
                    binding!!.etWeight.setText("0")

                }

            } else {
                binding!!.etWeight.setText("0")
            }

        }
        binding!!.etNoOfBags.doOnTextChanged { text, start, before, count ->


            if (binding!!.etWeight.text!!.isNotEmpty() && text != null && text != "0" && binding!!.etNoOfBags.text!!.isNotEmpty()) {
                try {
                    var bagCal =
                        (binding!!.etWeight.text.toString().toDouble() * 100) / text.toString()
                            .toDouble()
                    binding!!.etAvgWeight.setText(
                        dtime.format(bagCal)
                    )
                } catch (e: Exception) {

                }

            } else {
                binding!!.etAvgWeight.setText("0")
            }
        }

        CaseID = intent.getStringExtra("case_id")
        UserName = intent.getStringExtra("user_name")


        if (intent.getStringExtra("file3") == null) {

        } else {
            isFirstUpload = false;

        }


        if (isFirstUpload == true) {
            binding!!.llKanta.visibility = View.GONE
            binding!!.cardTruck2.visibility = View.VISIBLE
        } else {
            binding!!.cardTruck2.visibility = View.GONE
            binding!!.llKanta.visibility = View.VISIBLE
            binding!!.llBags.visibility = View.VISIBLE
        }


    }

    override fun setObservers() {
        kantaParchiViewModel.dharamKantaResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    if (it.data != null) {
                        binding!!.tilKantaParchi.setText(it.data.data.kantaName.toString())
                        binding!!.etKantaParchiNum.setText(it.data.data.kantaParchiNumber.toString())

                        kantaParchiNumber = it.data.data.kantaParchiNumber
                        kantaName = it.data.data.kantaName
                        kantaId = it.data.data.kantaId

                    }
                }
            }
        }
        kantaParchiViewModel.uploadSecondKantaParchiResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {

                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    if (it.data!!.status == "1") {
                        finish()
                        Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Utility.showAlertDialog(
                            this@UploadSecoundkantaParchiClass,
                            getString(R.string.alert),
                            it.data!!.getMessage()
                        ) {

                        }
                    }
                    hideDialog(
                        this
                    )

                }
            }
        }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): KanthaParchiUploadBinding =
        KanthaParchiUploadBinding.inflate(layoutInflater)

    override fun callApis() {
        kantaParchiViewModel.getKantaDetails(CaseID.toString())

    }


    val dtime = DecimalFormat("#.##")


    private fun clickListner() {
        binding!!.ivClose.setOnClickListener {
            finish()
        }
        binding!!.btnLogin.setOnClickListener {
            onNext()
        }
        binding!!.uploadKantha.setOnClickListener {
            firstKanthaFile = true
            truckImage = false
            truckImage2 = false


            dispatchTakePictureIntent(false)
            // callImageSelector(REQUEST_CAMERA);
        }
        binding!!.uploadTruck.setOnClickListener {
            firstKanthaFile = false
            truckImage = true
            truckImage2 = false
            dispatchTakePictureIntent(true)
            //    callImageSelector(REQUEST_CAMERA);
        }
        binding!!.uploadTruck2.setOnClickListener {
            firstKanthaFile = false
            truckImage = false
            truckImage2 = true
            dispatchTakePictureIntent(false)
        }
        binding!!.KanthaImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@UploadSecoundkantaParchiClass,
                R.layout.popup_photo_full,
                view,
                firstkantaParchiFile,
                null
            )
        }
        binding!!.TruckImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@UploadSecoundkantaParchiClass,
                R.layout.popup_photo_full,
                view,
                TruckImage,
                null
            )
        }
        binding!!.truckImage2.setOnClickListener {
            PhotoFullPopupWindow(
                this@UploadSecoundkantaParchiClass,
                R.layout.popup_photo_full,
                it,
                TruckImage2,
                null
            )
        }
    }


    fun onNext() {
        var KanthaImage = ""
        var truckImageImage = ""
        var truck2Image = ""
        if (fileKantha != null) {
            KanthaImage = "" + Utility.transferImageToBase64(fileKantha)
        }
        if (fileTruck != null) {
            truckImageImage = "" + Utility.transferImageToBase64(fileTruck)
        }
        if (fileTruck2 != null) {
            truck2Image = "" + Utility.transferImageToBase64(fileTruck2)
        }


        if (isFirstUpload) {
            if (fileTruck2 != null && fileTruck2!!.path.isNotEmpty()) {
                showDialog(this)
                kantaParchiViewModel.uploadSecondKantaParchi(
                    UploadSecoundkantaParchiPostData(
                        CaseID,
                        binding!!.notes.text.toString(),
                        KanthaImage,
                        truckImageImage,
                        truck2Image,
                        binding!!.etNoOfBags.text.toString(),
                        binding!!.etWeight.text.toString(),
                        binding!!.etAvgWeight.text.toString(),
                        binding!!.etOldWeightQt.text.toString(),
                        binding!!.etNoOfDispleasedBags.text.toString(),
                        kantaId,
                        kantaName,
                        kantaParchiNumber,
                        truckFacilityId,
                        bagsFacilityID
                    ), "IN"
                )


            } else {
                Toast.makeText(this, "Please select Kanta/Warehouse Image", Toast.LENGTH_SHORT)
            }
        } else {
            if (validateFields()) {
                showDialog(this)
                kantaParchiViewModel.uploadSecondKantaParchi(
                    UploadSecoundkantaParchiPostData(
                        CaseID,
                        binding!!.notes.text.toString(),
                        KanthaImage, truckImageImage, truck2Image,
                        binding!!.etNoOfBags.text.toString(),
                        binding!!.etWeight.text.toString(),
                        binding!!.etAvgWeight.text.toString(),
                        binding!!.etOldWeightQt.text.toString(),
                        binding!!.etNoOfDispleasedBags.text.toString(),
                        kantaId, kantaName, kantaParchiNumber, truckFacilityId, bagsFacilityID
                    ), "IN"
                )


            }

        }


    }

    fun dispatchTakePictureIntent(isKantaParchi: Boolean) {
        if (isKantaParchi) {
            checkForPermission { ImagePicker.with(this).start(); }

        } else {
            checkForPermission { ImagePicker.with(this).cameraOnly().start(); }
        }


    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == Activity.RESULT_OK || requestCode == 2404) {
                val userDetails = SharedPreferencesRepository.getDataManagerInstance().user
                val uri: Uri = data?.data!!
                var stampMap = mapOf(
                    "current_location" to "$currentLocation",
                    "emp_code" to userDetails.emp_id,
                    "emp_name" to userDetails.fname
                )
                val thumbnail = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                if (thumbnail != null) {
                    if (firstKanthaFile) {

                        var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                            File(compressImage(bitmapToFile(thumbnail!!).path)),
                            stampMap
                        )
                        firstKanthaFile = false
                        truckImage = false
                        truckImage2 = false
                        fileKantha = File(compressImage(bitmapToFile(stampedBitmap).toString()))
                        val uri = Uri.fromFile(fileKantha)
                        firstkantaParchiFile = uri.toString()
                        binding!!.KanthaImage.setImageURI(uri)

                    } else if (truckImage) {

                        var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                            File(compressImage(bitmapToFile(thumbnail!!).path)),
                            stampMap
                        )

                        firstKanthaFile = false
                        truckImage = false
                        truckImage2 = false
                        fileTruck = File(compressImage(bitmapToFile(stampedBitmap).toString()))
                        val uri = Uri.fromFile(fileTruck)
                        TruckImage = uri.toString()
                        binding!!.TruckImage.setImageURI(uri)
                    } else if (truckImage2) {

                        var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                            File(compressImage(bitmapToFile(thumbnail!!).path)),
                            stampMap
                        )
                        firstKanthaFile = false
                        truckImage = false
                        truckImage2 = false
                        fileTruck2 = File(compressImage(bitmapToFile(stampedBitmap).toString()))
                        val uri = Uri.fromFile(fileTruck2)
                        TruckImage2 = uri.toString()
                        binding!!.truckImage2.setImageURI(uri)
                    }
                }
            }
        } catch (e: NullPointerException) {
            showToast(this, "Please Select an image")
        }


    }


    fun validateFields(): Boolean {
        if (Validationhelper().fieldEmpty(binding!!.tilWeight)) {
            binding!!.tilWeight.error = "This Field cannot be empty"
            return false
        }
        if (Validationhelper().fieldEmpty(binding!!.tilNoOfBags)) {
            binding!!.tilNoOfBags.error = "This Field cannot be empty"
            return false
        }
        if (fileKantha == null) {
            Toast.makeText(this, "Please upload kanta file", Toast.LENGTH_SHORT);
            return false
        }
        if (fileTruck == null) {
            Toast.makeText(this, "Please upload truck file", Toast.LENGTH_SHORT)
            return false
        }


        return true
    }

    override fun onCheckedChanged(radioGroup: RadioGroup, i: Int) {
        when (radioGroup.checkedRadioButtonId) {
            R.id.radioSeller -> {
                InTrackType = "Yes"
                truckFacilityId = 1
            }

            R.id.radioBuyer -> {
                InTrackType = "No"
                truckFacilityId = 0
            }

            R.id.radioyes -> {
                InBardhanaType = "Yes"
                bagsFacilityID = 1
            }

            R.id.radiono -> {
                InBardhanaType = "No"
                bagsFacilityID = 0
            }

            else -> {

            }
        }

    }


}
