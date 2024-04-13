package com.apnagodam.staff.activity.`in`.secound_kanthaparchi

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadSecoundkantaParchiPostData
import com.apnagodam.staff.Network.viewmodel.KantaParchiViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.KanthaParchiUploadBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.SecoundkanthaParchiListResponse
import com.apnagodam.staff.utils.ImageHelper
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import com.apnagodam.staff.utils.Validationhelper
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.fxn.pix.Options
import com.google.android.gms.location.LocationServices
import com.thorny.photoeasy.OnPictureReady
import com.thorny.photoeasy.PhotoEasy
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.DecimalFormat
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class UploadSecoundkantaParchiClass : BaseActivity<KanthaParchiUploadBinding?>(),
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
    var InTrackID = 0
    val kantaParchiViewModel by viewModels<KantaParchiViewModel>()
    var InBardhanaType: String? = "null"
    var InBardhanaID = 1
    var kantaId = 0;
    var kantaName = ""
    var kantaParchiNumber = ""
    var isFirstUpload = true;
    var lat = 0.0
    var long = 0.0

    lateinit var photoEasy: PhotoEasy
    var currentLocation = ""
    override fun getLayoutResId(): Int {
        return R.layout.kantha_parchi_upload
    }

    val dtime = DecimalFormat("#.##")

    override fun setUp() {
        binding!!.tilOldWeightQt.visibility = View.VISIBLE
        binding!!.etKantaOldLocation.visibility= View.GONE
        photoEasy = PhotoEasy.builder().setActivity(this)
            .build()
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                lat = it.latitude
                long = it.longitude

                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(lat, long, 1)
                if (addresses != null) {
                    currentLocation =
                        "${addresses.first().featureName},${addresses.first().subAdminArea}, ${addresses.first().locality}, ${
                            addresses.first().adminArea
                        }"

                }
            }

        }
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
                        Toast.makeText(this,it.data.message,Toast.LENGTH_SHORT).show()
                    } else {
                        Utility.showAlertDialog(
                            this@UploadSecoundkantaParchiClass,
                            getString(R.string.alert),
                            it.data!!.getMessage()
                        ) {

                        }
                    }
                    hideDialog()

                }
            }
        }
        kantaParchiViewModel.getKantaDetails(CaseID.toString())

        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.customerName.text = UserName
        binding!!.caseId.text = CaseID
        clickListner()
    }

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
            dispatchTakePictureIntent()
            // callImageSelector(REQUEST_CAMERA);
        }
        binding!!.uploadTruck.setOnClickListener {
            firstKanthaFile = false
            truckImage = true
            truckImage2 = false
            dispatchTakePictureIntent()
            //    callImageSelector(REQUEST_CAMERA);
        }
        binding!!.uploadTruck2.setOnClickListener {
            firstKanthaFile = false
            truckImage = false
            truckImage2 = true
            dispatchTakePictureIntent()
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
                showDialog()
                kantaParchiViewModel.uploadSecondKantaParchi(
                    UploadSecoundkantaParchiPostData(
                        CaseID,
                        stringFromView(
                            binding!!.notes
                        ),
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
                        InTrackID,
                        InBardhanaID
                    ), "IN"
                )


            }
            else {
                Toast.makeText(this,"Please select Kanta/Warehouse Image",Toast.LENGTH_SHORT)
            }
        } else {
            if (validateFields()) {
                showDialog()
                kantaParchiViewModel.uploadSecondKantaParchi(
                    UploadSecoundkantaParchiPostData(
                        CaseID,
                        stringFromView(
                            binding!!.notes
                        ),
                        KanthaImage, truckImageImage, truck2Image,
                        binding!!.etNoOfBags.text.toString(),
                        binding!!.etWeight.text.toString(),
                        binding!!.etAvgWeight.text.toString(),
                        binding!!.etOldWeightQt.text.toString(),
                        binding!!.etNoOfDispleasedBags.text.toString(),
                        kantaId, kantaName, kantaParchiNumber, InTrackID, InBardhanaID
                    ), "IN"
                )


            }

        }


    }

    override fun dispatchTakePictureIntent() {
        permissionsBuilder(Manifest.permission.CAMERA).build().send {
            when (it.first()) {
                is PermissionStatus.Denied.Permanently -> {}
                is PermissionStatus.Denied.ShouldShowRationale -> {}
                is PermissionStatus.Granted -> {
                    photoEasy.startActivityForResult(this)

                }

                is PermissionStatus.RequestRequired -> {
                    photoEasy.startActivityForResult(this)

                }
            }

        }


    }

    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(applicationContext)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        photoEasy.onActivityResult(1566, -1, object : OnPictureReady {
            override fun onFinish(thumbnail: Bitmap?) {

                val userDetails = SharedPreferencesRepository.getDataManagerInstance().user

                var stampMap = mapOf(
                    "current_location" to "$currentLocation",
                    "emp_code" to userDetails.emp_id, "emp_name" to userDetails.fname
                )
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

        })

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
            Toast.makeText(this,"Please upload kanta file",Toast.LENGTH_SHORT);
            return false
        }
        if (fileTruck == null) {
            Toast.makeText(this,"Please upload truck file",Toast.LENGTH_SHORT)
            return false
        }


        return true
    }

    override fun onCheckedChanged(radioGroup: RadioGroup, i: Int) {
        if (radioGroup.checkedRadioButtonId == R.id.radioSeller) {
            InTrackType = "Yes"
            InTrackID = 1
        } else if (radioGroup.checkedRadioButtonId == R.id.radioBuyer) {
            InTrackType = "No"
            InTrackID = 0
        } else if (radioGroup.checkedRadioButtonId == R.id.radioyes) {
            InBardhanaType = "Yes"
            InBardhanaID = 1
        } else if (radioGroup.checkedRadioButtonId == R.id.radiono) {
            InBardhanaType = "No"
            InBardhanaID = 0
        }
    }


}
