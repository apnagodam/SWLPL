package com.apnagodam.staff.activity.out.s_quaility_report

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.Request.UploadSecoundQualityPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityUpdateQualityReportBinding
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class OutUploadSecoundQualtityReportsClass : BaseActivity<ActivityUpdateQualityReportBinding?>() {
    var fileReport: File? = null
    var fileCommudity: File? = null
    var packagingTypeID: String? = null

    // role of image
    var UserName: String? = null
    var CaseID: String? = ""
    var ReportsFileSelect = false
    var CommudityFileSelect = false
    var options: Options? = null
    private var reportFile: String? = null
    private var commudityFile: String? = null
    override fun getLayoutResId(): Int {
        return R.layout.activity_update_quality_report
    }

    override fun setUp() {
        val bundle = intent.getBundleExtra(BUNDLE)
        if (bundle != null) {
            UserName = bundle.getString("user_name")
            CaseID = bundle.getString("case_id")
        }
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.customerName.text = UserName
        binding!!.caseId.text = CaseID
        clickListner()
        // spinner purpose
        binding!!.rlpackgingType.visibility = View.GONE
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityAndClear(OutSecoundQualityReportListingActivity::class.java)
    }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener {
            startActivityAndClear(
                OutSecoundQualityReportListingActivity::class.java
            )
        }
        binding!!.btnSubmit.setOnClickListener {
            Utility.showDecisionDialog(
                this@OutUploadSecoundQualtityReportsClass,
                getString(R.string.alert),
                "Are You Sure to Summit?",
                object : Utility.AlertCallback {
                    override fun callback() {
                        if (isValid) {

                            /* if (fileReport == null) {
                            Toast.makeText(getApplicationContext(), R.string.upload_reports_file, Toast.LENGTH_LONG).show();
                        } else if (fileCommudity == null) {
                            Toast.makeText(getApplicationContext(), R.string.upload_commodity_file, Toast.LENGTH_LONG).show();
                        } else if (packagingTypeID == null) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.packaging), Toast.LENGTH_LONG).show();
                        } else {*/
                            onNext()
                            // }
                        }
                    }
                })
        }
        binding!!.uploadReport.setOnClickListener {
            ReportsFileSelect = true
            CommudityFileSelect = false
            callImageSelector(REQUEST_CAMERA)
        }
        binding!!.uploadCommudity.setOnClickListener {
            ReportsFileSelect = false
            CommudityFileSelect = true
            callImageSelector(REQUEST_CAMERA)
        }
        binding!!.ReportsImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@OutUploadSecoundQualtityReportsClass,
                R.layout.popup_photo_full,
                view,
                reportFile,
                null
            )
        }
        binding!!.CommudityImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@OutUploadSecoundQualtityReportsClass,
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
        Pix.start(this@OutUploadSecoundQualtityReportsClass, options)
    }

    // update file
    fun onNext() {
        var KanthaImage = ""
        var CommudityFileSelectImage = ""
        if (fileReport != null) {
            KanthaImage = "" + Utility.transferImageToBase64(fileReport)
        }
        if (fileCommudity != null) {
            CommudityFileSelectImage = "" + Utility.transferImageToBase64(fileCommudity)
        }
        //else {


        apiService.uploadSecoundQualityReports(
            UploadSecoundQualityPostData(
                CaseID,
                KanthaImage,
                stringFromView(
                    binding!!.etMoistureLevel
                ),
                stringFromView(binding!!.etTcw),
                stringFromView(binding!!.etFmLevel),
                stringFromView(
                    binding!!.etThin
                ),
                stringFromView(binding!!.etDehuck),
                stringFromView(binding!!.etDiscolor),
                stringFromView(
                    binding!!.etBroken
                ),
                stringFromView(binding!!.etInfested),
                stringFromView(binding!!.etLive),
                stringFromView(binding!!.notes),
                CommudityFileSelectImage
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {body->
                Utility.showAlertDialog(
                    this@OutUploadSecoundQualtityReportsClass,
                    getString(R.string.alert),
                    body.message
                ) { startActivityAndClear(OutSecoundQualityReportListingActivity::class.java) }
            }
            .subscribe()




        // }
    }

    val isValid: Boolean
        get() = if (TextUtils.isEmpty(stringFromView(binding!!.etMoistureLevel))) {
            Utility.showEditTextError(
                binding!!.tilMoistureLevel,
                R.string.moisture_level
            )
        } else true

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
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                if (data!!.hasExtra(Pix.IMAGE_RESULTS)) {
                    val returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)!!
                    Log.e("getImageesValue", returnValue[0])
                    if (requestCode == REQUEST_CAMERA) {
                        if (ReportsFileSelect) {
                            ReportsFileSelect = false
                            CommudityFileSelect = false
                            fileReport = File(compressImage(returnValue[0]))
                            val uri = Uri.fromFile(fileReport)
                            reportFile = uri.toString()
                            binding!!.ReportsImage.setImageURI(uri)
                        } else if (CommudityFileSelect) {
                            ReportsFileSelect = false
                            CommudityFileSelect = false
                            fileCommudity = File(compressImage(returnValue[0]))
                            val uri = Uri.fromFile(fileCommudity)
                            commudityFile = uri.toString()
                            binding!!.CommudityImage.setImageURI(uri)
                        }
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
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, options)
                } else {
                    callImageSelector(REQUEST_CAMERA)
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
