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
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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

    lateinit var photoEasy: PhotoEasy
    var currentLocation = ""
    override fun getLayoutResId(): Int {
        return R.layout.activity_update_quality_report
    }

    override fun setUp() {
        photoEasy = PhotoEasy.builder().setActivity(this)
            .build()
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
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

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }
        val bundle = intent.getBundleExtra(BUNDLE)
        binding!!.tvTitle.setText("Upload Second Quality Report")
        if (validData(bundle)) {
            UserName = bundle?.getString("user_name")
            CaseID = bundle?.getString("case_id")

            if (bundle?.getString("skp_bags") != null && bundle.getString("skp_weight") != null) {
                skpBags = bundle?.getString("skp_bags").toString().toInt()
                skpWeight = bundle?.getString("skp_weight").toString().toInt()
                binding!!.etAvgWeight.setText((skpWeight * 100 / skpBags).toString())
            } else {
                skpBags = 0;
                skpWeight = 0;
                binding!!.etAvgWeight.setText("N/A")

            }

        }
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.customerName.text = UserName
        binding!!.caseId.text = CaseID
        binding!!.tilExtraClaim.visibility = View.VISIBLE

        qualitReportViewModel.getCommodityParams(case_id = CaseID.toString())
        qualitReportViewModel.commodityResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {}
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
            startActivityAndClear(
                SecoundQualityReportListingActivity::class.java
            )
        }
        binding!!.btnSubmit.setOnClickListener {
            Utility.showDecisionDialog(
                this@UploadSecoundQualtityReportsClass,
                getString(R.string.alert),
                "Are You Sure to Summit?",
                object : Utility.AlertCallback {
                    override fun callback() {
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
                })
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

    private fun callImageSelector(requestCamera: Int) {
        options = Options.init()
            .setRequestCode(requestCamera) //Request code for activity results
            .setCount(1) //Number of images to restict selection count
            .setFrontfacing(false) //Front Facing camera on start
            .setExcludeVideos(false) //Option to exclude videos
            .setVideoDurationLimitinSeconds(30) //Duration for video recording
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientaion
            .setPath("/apnagodam/lp/images") //Custom Path For media Storage
        Pix.start(this@UploadSecoundQualtityReportsClass, options)
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


               if(thumbnail!=null){
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
        photoEasy.startActivityForResult(this)
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

    private fun drawTextToBitmap(bitmap: Bitmap, textSize: Int = 78, text: String): Bitmap {

        val canvas = Canvas(bitmap)

        // new antialised Paint - empty constructor does also work
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLACK

        // text size in pixels
        val scale = resources.displayMetrics.density
        paint.textSize = (textSize * scale).roundToInt().toFloat()

        //custom fonts or a default font
        val fontFace =
            ResourcesCompat.getFont(this@UploadSecoundQualtityReportsClass, R.style.TextStyleNormal)
        paint.typeface = Typeface.create(fontFace, Typeface.NORMAL)
        val w: Float = bitmap.width.toFloat()
        val h: Float = bitmap.height.toFloat()

        // draw text to the Canvas center
        val bounds = Rect()
        //draw the text
        paint.getTextBounds(text, 0, text.length, bounds)

        //x and y defines the position of the text, starting in the top left corner
        canvas.drawText(text, 0.0f, h, paint)
        return bitmap
    }

    // update file
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
                    binding!!.etExtraClaim.text!!.toString()
                )
            )

            qualitReportViewModel.sQualityUploadResponse.observe(this@UploadSecoundQualtityReportsClass) {
                when (it) {
                    is NetworkResult.Error -> {
                        showToast(it.message)
                    }

                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        if (it.data!!.status == 1) {
                            showToast(it.data.message)
                            startActivityAndClear(
                                SecoundQualityReportListingActivity::class.java
                            )
                        } else {
                            Utility.showAlertDialog(
                                this@UploadSecoundQualtityReportsClass,
                                getString(R.string.alert),
                                it.data!!.getMessage()
                            ) {

                            }
                        }

                    }
                }
            }
        }


    }

//    val isValid: Boolean
//        get() = if (TextUtils.isEmpty(stringFromView(binding!!.etMoistureLevel))) {
//            Utility.showEditTextError(
//                binding!!.tilMoistureLevel,
//                R.string.moisture_level
//            )
//        } else true

    /* else if (TextUtils.isEmpty(stringFromView(binding.etTcw))) {
  return Utility.showEditTextError(binding.tilTcw, R.string.tcw);
} else if (TextUtils.isEmpty(stringFromView(binding.etBroken))) {
  return Utility.showEditTextError(binding.tilBroken, R.string.broken);
} else if (TextUtils.isEmpty(stringFromView(binding.etFmLevel))) {
  return Utility.showEditTextError(binding.tilFmLevel, R.string.fm_level);
} else if (TextUtils.isEmpty(stringFromView(binding.etThin))) {
  return Utility.showEditTextError(binding.tilThin, R.string.thin);
} else if (TextUtils.isEmpty(stringFromView(binding.etDehuck))) {
  return Utility.showEditTextError(binding.tilDehuck, R.string.dehuck);
} else if (TextUtils.isEmpty(stringFromView(binding.etDiscolor))) {
  return Utility.showEditTextError(binding.tilDiscolor, R.string.discolor);
} else if (TextUtils.isEmpty(stringFromView(binding.etInfested))) {
  return Utility.showEditTextError(binding.tilInfested, R.string.infested);
} else if (TextUtils.isEmpty(stringFromView(binding.etLive))) {
  return Utility.showEditTextError(binding.tilLive, R.string.live_count);
}*/


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityAndClear(SecoundQualityReportListingActivity::class.java)
    }
}
