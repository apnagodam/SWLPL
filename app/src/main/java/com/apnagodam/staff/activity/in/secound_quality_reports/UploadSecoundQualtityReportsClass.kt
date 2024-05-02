package com.apnagodam.staff.activity.`in`.secound_quality_reports

import android.Manifest
import android.R.attr.src
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadFirstQualityPostData
import com.apnagodam.staff.Network.Request.UploadSecoundQualityPostData
import com.apnagodam.staff.Network.Response.QualityParamsResponse
import com.apnagodam.staff.Network.viewmodel.QualitReportViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityUpdateQualityReportBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.utils.ImageHelper
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.allGranted
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import kotlin.math.roundToInt


@AndroidEntryPoint
class UploadSecoundQualtityReportsClass : BaseActivity<ActivityUpdateQualityReportBinding?>() {
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
    var listOfQualityParams = ArrayList<UploadFirstQualityPostData.CommodityData>()
    var isFieldEmpty = true;
    var listOfParams = arrayListOf<QualityParamsResponse.Datum>()
    val qualitReportViewModel by viewModels<QualitReportViewModel>()
    var skpBags = 0;
    var skpWeight = 0;
    var lat = 0.0
    var long = 0.0
    var avgWeight = "";

    lateinit var photoEasy: PhotoEasy
    var currentLocation = ""
    override fun getLayoutResId(): Int {
        return R.layout.activity_update_quality_report
    }

    override fun setUp() {
        photoEasy = PhotoEasy.builder().setActivity(this)
            .build()
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this,"Location not enabled",Toast.LENGTH_SHORT).show()
        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                it?.let {
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
        val bundle = intent.getBundleExtra(BUNDLE)
        binding!!.tvTitle.setText("Upload Second Quality Report")
        UserName = intent?.getStringExtra("user_name")
        CaseID = intent?.getStringExtra("case_id")
        if(intent.getStringExtra("skp_avg_weight")==null){
            avgWeight = "0"
        }
        else{
            avgWeight = intent?.getStringExtra("skp_avg_weight").toString();

        }
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.customerName.text = UserName
        binding!!.caseId.text = CaseID
        binding!!.tilExtraClaim.visibility = View.VISIBLE

        binding!!.etAvgWeight.setText(avgWeight)
        showDialog()
        qualitReportViewModel.getCommodityParams(case_id = CaseID.toString())
        qualitReportViewModel.commodityResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog()
                }

                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    if (it.data != null) {
                        listOfParams = it.data.data as ArrayList<QualityParamsResponse.Datum>
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

    fun validData(bundle: Bundle?): Boolean {
        return bundle != null
    }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener {
            finish()

        }
        binding!!.btnSubmit.setOnClickListener {
            if (packagingTypeID != null) {
                onNext()
            } else {
                Utility.showAlertDialog(
                    this@UploadSecoundQualtityReportsClass,
                    getString(R.string.alert),
                    resources.getString(R.string.packaging)
                ) {

                }
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
                this@UploadSecoundQualtityReportsClass,
                R.layout.popup_photo_full,
                view,
                reportFile,
                null
            )
        }
        binding!!.CommudityImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@UploadSecoundQualtityReportsClass,
                R.layout.popup_photo_full,
                view,
                commudityFile,
                null
            )
        }
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
                    var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                        File(compressImage(bitmapToFile(thumbnail).path)),
                        stampMap
                    )
                    if (ReportsFileSelect) {
                        ReportsFileSelect = false
                        CommudityFileSelect = false

                        fileReport = File(compressImage(bitmapToFile(stampedBitmap).path))
                        val uri = Uri.fromFile(fileReport)
                        reportFile = uri.toString()
                        binding!!.ReportsImage.setImageURI(uri)
                    } else if (CommudityFileSelect) {
                        val imageBitmap = data?.extras?.get("data") as Bitmap
                        ReportsFileSelect = false
                        CommudityFileSelect = false
                        fileCommudity = File(compressImage(bitmapToFile(imageBitmap).path))
                        val uri = Uri.fromFile(fileCommudity)
                        commudityFile = uri.toString()
                        binding!!.CommudityImage.setImageURI(uri)
                    }
                }
            }

        })


    }


    override fun dispatchTakePictureIntent() {
        val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            permissionsBuilder(Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION).build().send() {
                if(it.allGranted()){
                    photoEasy.startActivityForResult(this)
                }
                else{
                    Toast.makeText(
                        this,
                        "Location or Camera Permissions Denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        }
        else{
            Toast.makeText(
                this,
                "GPS Not Enabled",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

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


    fun onNext() {
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


        for (i in listOfParams.indices) {
            var datum =
                UploadFirstQualityPostData.CommodityData(
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
        if (!isFieldEmpty && binding!!.etLive.text!!.isNotEmpty() && binding!!.etExtraClaim.text!!.isNotEmpty() && fileReport != null) {

            showDialog()
            qualitReportViewModel.uploadSecondQualityReport(
                UploadSecoundQualityPostData(
                    CaseID,
                    ReportImage,
                    listOfQualityParams,
                    packagingTypeID,
                    "1",
                    stringFromView(binding!!.etLive),
                    stringFromView(binding!!.notes),
                    CommudityFileSelectImage,
                    binding!!.etExtraClaim.text!!.toString(),"2",""
                ),"IN"
            )

            qualitReportViewModel.sQualityUploadResponse.observe(this@UploadSecoundQualtityReportsClass) {
                when (it) {
                    is NetworkResult.Error -> {
                        hideDialog()
                        Toast.makeText(this,it.message,Toast.LENGTH_SHORT)

                    }

                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        if (it.data!!.status == "1") {
                            Toast.makeText(this,it.data.message,Toast.LENGTH_SHORT)

                            finish()
                        } else {
                            Utility.showAlertDialog(
                                this@UploadSecoundQualtityReportsClass,
                                getString(R.string.alert),
                                it.data!!.getMessage()
                            ) {

                            }
                        }
                        hideDialog()

                    }
                }
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
