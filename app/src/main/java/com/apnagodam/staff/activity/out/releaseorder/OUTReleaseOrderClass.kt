package com.apnagodam.staff.activity.out.releaseorder

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadReleaseOrderlsPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.viewmodel.OrdersViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityUploadReleaseOrderBinding
import com.apnagodam.staff.utils.Utility
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class OUTReleaseOrderClass : BaseActivity<ActivityUploadReleaseOrderBinding?>(),
    View.OnClickListener {
    var UserName: String? = null
    var CaseID: String? = ""
    var fileBiltyImage: File? = null
    var BiltyImageFile = false
    private var BiltyFile: String? = null
    var options: Options? = null
    val ordersViewModel by viewModels<OrdersViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_upload_release_order
    }

    override fun setUp() {
        val bundle = intent.getBundleExtra(BUNDLE)
        if (bundle != null) {
            UserName = bundle.getString("user_name")
            CaseID = bundle.getString("case_id")
        }
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        clickListner()
        binding!!.customerName.text = UserName
        binding!!.caseId.text = CaseID
    }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener(this)
        binding!!.btnLogin.setOnClickListener(this)
        binding!!.uploadTruck.setOnClickListener {
            BiltyImageFile = true
            callImageSelector(REQUEST_CAMERA)
        }
    }

    private fun callImageSelector(requestCamera: Int) {
        options = Options.init()
            .setRequestCode(requestCamera) //Request code for activity results
            .setCount(1) //Number of images to restict selection count
            .setFrontfacing(false) //Front Facing camera on start
            .setExcludeVideos(false) //Option to exclude videos
            .setVideoDurationLimitinSeconds(30) //Option to exclude videos
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientaion
            .setPath("/apnagodam/lp/images") //Custom Path For media Storage
        Pix.start(this@OUTReleaseOrderClass, options)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                if (data!!.hasExtra(Pix.IMAGE_RESULTS)) {
                    val returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)!!
                    Log.e("getImageesValue", returnValue[0].toString())
                    if (requestCode == REQUEST_CAMERA) {
                        if (BiltyImageFile) {
                            BiltyImageFile = false
                            fileBiltyImage = File(compressImage(returnValue[0].toString()))
                            val uri = Uri.fromFile(fileBiltyImage)
                            BiltyFile = uri.toString()
                            binding!!.TruckImage.setImageURI(uri)
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityAndClear(OUTRelaseOrderListingActivity::class.java)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_close ->onBackPressedDispatcher.onBackPressed()
            R.id.btn_login -> if (binding!!.notes.text.toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(this@OUTReleaseOrderClass, "Enter Notes Here!!", Toast.LENGTH_LONG)
                    .show()
            } else if (fileBiltyImage == null) {
                Toast.makeText(
                    this@OUTReleaseOrderClass,
                    "Upload to Release Order File",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Utility.showDecisionDialog(
                    this@OUTReleaseOrderClass,
                    getString(R.string.alert),
                    "Are You Sure to Summit?"
                ) { callApi() }
            }
        }
    }

    private fun callApi() {
        var BiltyImage = ""
        if (fileBiltyImage != null) {
            BiltyImage = "" + Utility.transferImageToBase64(fileBiltyImage)
        }
        ordersViewModel.uploadReleaseOrders(
            UploadReleaseOrderlsPostData(
                CaseID,
                stringFromView(binding!!.notes),
                BiltyImage,
                ""
            )
        )
        ordersViewModel.uploadReleaseOrdersResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    if (it.data != null) {
                        if (it.data.status == "1") {
                            onBackPressedDispatcher.onBackPressed()
                        } else {
                            showToast(it.data.message)
                        }
                    }
                }
            }
        }

    }
}
