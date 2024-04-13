package com.apnagodam.staff.activity.`in`.first_quality_reports

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadFirstQualityPostData
import com.apnagodam.staff.Network.Request.UploadFirstQualityPostData.CommodityData
import com.apnagodam.staff.Network.Response.QualityParamsResponse.Datum
import com.apnagodam.staff.Network.viewmodel.QualitReportViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityUpdateQualityReportBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.utils.ImageHelper
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.thorny.photoeasy.OnPictureReady
import com.thorny.photoeasy.PhotoEasy
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class UploadFirstQualtityReportsClass : BaseActivity<ActivityUpdateQualityReportBinding?>() {
    var packagingTypeID: String? = null

    // role of image
    var UserName: String? = null
    var CaseID: String? = ""
    var fileReport: File? = null
    var fileCommudity: File? = null
    var ReportsFileSelect = false
    var CommudityFileSelect = false
    private var reportFile: String? = null
    private var commudityFile: String? = null
    var options: Options? = null
    var paramList = arrayListOf<String>()
    var ed = arrayListOf<TextInputLayout>()
    var listOfQualityParams = ArrayList<CommodityData>()
    var isFieldEmpty = true;
    val qualitReportViewModel by viewModels<QualitReportViewModel>()
    var listOfParams = arrayListOf<Datum>()
    var lat = 0.0
    var long = 0.0

    lateinit var photoEasy: PhotoEasy
    var currentLocation = ""
    override fun getLayoutResId(): Int {
        return R.layout.activity_update_quality_report
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setUp() {


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
                if(it!=null){
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
        }

        binding!!.tvTitle.setText("Upload First Quality Report")

        binding!!.tilAvgWeight.visibility = View.GONE
        UserName = intent.getStringExtra("user_name")
        CaseID = intent.getStringExtra("case_id")
        qualitReportViewModel.commodityResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog()
                }
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    if (it.data != null) {
                        listOfParams = it.data.data as ArrayList<Datum>
                        for (data in it.data.data) {
                            var textInputField =
                                TextInputLayout(this, null, R.attr.customTextInputStyle)
                            var editText = TextInputEditText(textInputField.context)
                            editText.inputType =
                                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                            editText.filters = arrayOf(InputFilter.LengthFilter(5))
                            textInputField.setHint(data.name)
                            textInputField.addView(editText)

                            ed.add(textInputField)
                            binding!!.llDynamic.addView(textInputField)
//                               var data = Datum()
//                                listOfQualityParams.add(Datum())

                        }


                    }
                    hideDialog()

                }
            }
        }
        showDialog()
        qualitReportViewModel.getCommodityParams(case_id = CaseID.toString())

        qualitReportViewModel.fQualityUploadResponse.observe(this@UploadFirstQualtityReportsClass) {
            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    if (it.data!!.status == "1") {
                        Toast.makeText(this@UploadFirstQualtityReportsClass,it.message,Toast.LENGTH_SHORT).show()
                        finish()

                    } else {
                        Utility.showAlertDialog(
                            this@UploadFirstQualtityReportsClass,
                            getString(R.string.alert),
                            it.data!!.getMessage()
                        ) { }
                    }

                }
            }
        }

        var i = 0;
        paramList.forEach {


        }
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.customerName.text = UserName
        binding!!.caseId.text = CaseID
        clickListner()
        // spinner purpose
        binding!!.spinnerPackagingtype.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) packagingTypeID =
                        parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // can leave this empty
                }
            }
    }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            qualitReportViewModel.getFirstQualityListing("10", "1", "IN", search = "")
        }
        binding!!.btnSubmit.setOnClickListener {

            if (packagingTypeID == null) {
                Utility.showAlertDialog(
                    this@UploadFirstQualtityReportsClass,
                    getString(R.string.alert),
                    resources.getString(R.string.packaging)
                ) {

                }
            } else {
                onNext()
            }
            var ReportImage = ""
            var CommudityFileSelectImage = ""
            if (fileReport != null) {
                ReportImage = "" + Utility.transferImageToBase64(fileReport)
            }
            if (fileCommudity != null) {
                CommudityFileSelectImage =
                    "" + Utility.transferImageToBase64(fileCommudity)
            }
            listOfQualityParams.clear()


            val map = listOfParams.associateBy({ it.id }, { it.name })
            for (i in listOfParams.indices) {
                var datum =
                    CommodityData(
                        listOfParams[i].id,
                        listOfParams[i].name,
                        "",
                        listOfParams[i].min,
                        listOfParams[i].max
                    )
                listOfQualityParams.add(datum)

            }

            for (editext in ed.indices) {
                if (ed[editext].editText!!.text.isEmpty()) {
                    isFieldEmpty = true;
                    ed[editext].error = "This field cannot be empty!"
                } else {
                    listOfQualityParams[editext].value =
                        ed[editext].editText!!.text.toString()
                    isFieldEmpty = false;
                }
            }
            if (!isFieldEmpty ) {
                qualitReportViewModel.uploadFirstQualityReport(
                    UploadFirstQualityPostData(
                        CaseID,
                        ReportImage,
                        listOfQualityParams,
                        packagingTypeID,
                        "1",
                        stringFromView(binding!!.etLive),
                        stringFromView(binding!!.notes),
                        CommudityFileSelectImage
                    ),"IN"
                )


            }



        }
        binding!!.uploadReport.setOnClickListener {
            ReportsFileSelect = true
            CommudityFileSelect = false
            dispatchTakePictureIntent()
        }
        binding!!.uploadCommudity.setOnClickListener {
            ReportsFileSelect = false
            CommudityFileSelect = true
            dispatchTakePictureIntent()
        }
        binding!!.ReportsImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@UploadFirstQualtityReportsClass,
                R.layout.popup_photo_full,
                view,
                reportFile,
                null
            )
        }
        binding!!.CommudityImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@UploadFirstQualtityReportsClass,
                R.layout.popup_photo_full,
                view,
                commudityFile,
                null
            )
        }
    }


    // update file
    fun onNext() {
        var ReportImage = ""
        var CommudityFileSelectImage = ""
        if (fileReport != null) {
            ReportImage = "" + Utility.transferImageToBase64(fileReport)
        }
        if (fileCommudity != null) {
            CommudityFileSelectImage = "" + Utility.transferImageToBase64(fileCommudity)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
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

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        photoEasy.onActivityResult(1566, -1, object : OnPictureReady {
            override fun onFinish(thumbnail: Bitmap?) {

                val userDetails = SharedPreferencesRepository.getDataManagerInstance().user

                var stampMap = mapOf(
                    "current_location" to "$currentLocation",
                    "emp_code" to userDetails.emp_id, "emp_name" to userDetails.fname
                )
                if (thumbnail != null) {
                    if (ReportsFileSelect) {

                        var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                            File(compressImage(bitmapToFile(thumbnail!!).path)),
                            stampMap
                        )
                        ReportsFileSelect = false
                        CommudityFileSelect = false
                        fileReport = File(compressImage(bitmapToFile(stampedBitmap).path))
                        val uri = Uri.fromFile(fileReport)
                        reportFile = uri.toString()
                        binding!!.ReportsImage.setImageURI(uri)
                    } else if (CommudityFileSelect) {

                        var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                            File(compressImage(bitmapToFile(thumbnail!!).path)),
                            stampMap
                        )
                        ReportsFileSelect = false
                        CommudityFileSelect = false
                        fileCommudity = File(compressImage(bitmapToFile(stampedBitmap).path))
                        val uri = Uri.fromFile(fileCommudity)
                        commudityFile = uri.toString()
                        binding!!.CommudityImage.setImageURI(uri)
                    }
                }
            }

        })
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, options)
                } else {
                    dispatchTakePictureIntent()
                    Toast.makeText(
                        this,
                        "Approve permissions to open Pix ImagePicker",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }
}
