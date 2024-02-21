package com.apnagodam.staff.activity.`in`.first_quality_reports

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadFirstQualityPostData
import com.apnagodam.staff.Network.Request.UploadFirstQualityPostData.CommodityData
import com.apnagodam.staff.Network.Response.QualityParamsResponse.Datum
import com.apnagodam.staff.Network.viewmodel.QualitReportViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityUpdateQualityReportBinding
import com.apnagodam.staff.module.CommodityResponseData
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
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
    override fun getLayoutResId(): Int {
        return R.layout.activity_update_quality_report
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setUp() {

        binding!!.tilAvgWeight.visibility = View.GONE
        val bundle = intent.getBundleExtra(BUNDLE)
        if (bundle != null) {
            UserName = bundle.getString("user_name")
            CaseID = bundle.getString("case_id")


            qualitReportViewModel.getCommodityParams(case_id = CaseID.toString())
            qualitReportViewModel.commodityResponse.observe(this) {
                when (it) {
                    is NetworkResult.Error -> {}
                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        if (it.data != null) {
                            listOfParams = it.data.data as ArrayList<Datum>
                            for (data in it.data.data) {
                                var textInputField =
                                    TextInputLayout(this, null, R.attr.customTextInputStyle)
                                var editText = TextInputEditText(textInputField.context)
                                editText.inputType = InputType.TYPE_CLASS_NUMBER
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

            /*    "Dead" -> binding!!.tilMoistureLevel.visibility = View.VISIBLE
                "Admixture" -> binding!!.tilTcw.visibility = View.VISIBLE
                "Damage" -> binding!!.tilDehuck.visibility = View.VISIBLE
                "Counts" -> binding!!.tilMoistureLevel.visibility = View.VISIBLE
                "Fungus" -> binding!!.tilTcw.visibility = View.VISIBLE
                "weevil" -> binding!!.tilDehuck.visibility = View.VISIBLE
                "Recovery" -> binding!!.tilMoistureLevel.visibility = View.VISIBLE
                "Danthal" -> binding!!.tilTcw.visibility = View.VISIBLE
                "Fatkan" -> binding!!.tilDehuck.visibility = View.VISIBLE
                "Black" -> binding!!.tilMoistureLevel.visibility = View.VISIBLE
                "Tikki" -> binding!!.tilTcw.visibility = View.VISIBLE
                "SMB" -> binding!!.tilDehuck.visibility = View.VISIBLE
                "Black Tip" -> binding!!.tilMoistureLevel.visibility = View.VISIBLE
                "Red Green" -> binding!!.tilTcw.visibility = View.VISIBLE
                "Potia" -> binding!!.tilDehuck.visibility = View.VISIBLE
                "KB" -> binding!!.tilMoistureLevel.visibility = View.VISIBLE
                "HL" -> binding!!.tilTcw.visibility = View.VISIBLE
                "Dana" -> binding!!.tilDehuck.visibility = View.VISIBLE
                "Oil" -> binding!!.tilMoistureLevel.visibility = View.VISIBLE
                "Moisture" -> binding!!.tilMoistureLevel.visibility = View.VISIBLE
                "Broken" -> binding!!.tilBroken.visibility = View.VISIBLE
                "Thin" -> binding!!.tilThin.visibility = View.VISIBLE
                "FM" -> binding!!.tilFmLevel.visibility = View.VISIBLE
                "TCW" -> binding!!.tilTcw.visibility = View.VISIBLE*/
            var i = 0;
            paramList.forEach {


            }
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
            startActivityAndClear(
                FirstQualityReportListingActivity::class.java
            )
        }
        binding!!.btnSubmit.setOnClickListener {

//if(listOfQualityParams.isNotEmpty()){
//    for (i in listOfQualityParams){
//        showToast(i.datum.name)
//
//    }
//}
            Utility.showDecisionDialog(
                this@UploadFirstQualtityReportsClass,
                getString(R.string.alert),
                "Are You Sure to Summit?",
                object : Utility.AlertCallback {
                    override fun callback() {
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


                       val map =  listOfParams.associateBy ({it.id},{it.name})
                        for (i in listOfParams.indices)
                        {
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
                                listOfQualityParams[editext].value = ed[editext].editText!!.text.toString()
                                isFieldEmpty = false;
                            }
                        }
                        if (!isFieldEmpty  && binding!!.etLive.text!!.isNotEmpty()) {
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
                                )
                            )

                            qualitReportViewModel.fQualityUploadResponse.observe(this@UploadFirstQualtityReportsClass) {
                                when (it) {
                                    is NetworkResult.Error -> {
                                        showToast(it.message)
                                    }

                                    is NetworkResult.Loading -> {}
                                    is NetworkResult.Success -> {
                                        Utility.showAlertDialog(
                                            this@UploadFirstQualtityReportsClass,
                                            getString(R.string.alert),
                                            it.data!!.getMessage()
                                        ) { startActivityAndClear(FirstQualityReportListingActivity::class.java) }
                                    }
                                }
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

    private fun callImageSelector(requestCamera: Int) {
        options = Options.init()
            .setRequestCode(requestCamera) //Request code for activity results
            .setCount(1) //Number of images to restict selection count
            .setFrontfacing(false) //Front Facing camera on start
            .setExcludeVideos(false) //Option to exclude videos
            .setVideoDurationLimitinSeconds(30) //Duration for video recording
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientaion
            .setPath("/apnagodam/lp/images") //Custom Path For media Storage
        Pix.start(this@UploadFirstQualtityReportsClass, options)
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
        //else {
//        qualitReportViewModel.uploadFirstQualityReport(
//            UploadFirstQualityPostData(
//                CaseID,
//                ReportImage,
//                stringFromView(
//                    binding!!.etMoistureLevel
//                ),
//                stringFromView(binding!!.etTcw),
//                stringFromView(binding!!.etFmLevel),
//                stringFromView(
//                    binding!!.etThin
//                ),
//                stringFromView(binding!!.etDehuck),
//                stringFromView(binding!!.etDiscolor),
//                stringFromView(
//                    binding!!.etBroken
//                ),
//                packagingTypeID,
//                stringFromView(binding!!.etInfested),
//                stringFromView(binding!!.etLive),
//                stringFromView(binding!!.notes),
//                CommudityFileSelectImage
//            )
//        )
//        qualitReportViewModel.fQualityUploadResponse.observe(this){
//            when(it){
//                is NetworkResult.Error -> {}
//                is NetworkResult.Loading -> {}
//                is NetworkResult.Success -> {
//                    Utility.showAlertDialog(
//                        this@UploadFirstQualtityReportsClass,
//                        getString(R.string.alert),
//                        it.data!!.getMessage()
//                    ) { startActivityAndClear(FirstQualityReportListingActivity::class.java) }
//                }
//            }
//        }


        // }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityAndClear(FirstQualityReportListingActivity::class.java)
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


    override fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_CAMERA)

        } catch (e: ActivityNotFoundException) {
            // display error state to the user
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
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (ReportsFileSelect) {
                    val imageBitmap = data?.extras?.get("data") as Bitmap

                    ReportsFileSelect = false
                    CommudityFileSelect = false
                    fileReport = File(compressImage(bitmapToFile(imageBitmap).path))
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
    }

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
}
