package com.apnagodam.staff.activity.out.deliveryorder

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadReleaseOrderlsPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.viewmodel.LoginViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityUploadReleaseOrderBinding
import com.apnagodam.staff.utils.Utility
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID
@AndroidEntryPoint
class OUTDeliverdUpoladOrderClass : BaseActivity<ActivityUploadReleaseOrderBinding?>(), View.OnClickListener {
    var UserName: String? = null
    var CaseID: String? = ""
    var fileBiltyImage: File? = null
    var BiltyImageFile = false
    private var BiltyFile: String? = null
    var options: Options? = null

    val loginViewModel by viewModels<LoginViewModel>()
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
        binding!!.headfile.text = "Delivered Order File"
        binding!!.headfileselect.text = "Click Here To Upload to Delivered Order File"
        binding!!.heading.text = "Update Delivered Order"
    }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener(this)
        binding!!.btnLogin.setOnClickListener(this)
        binding!!.uploadTruck.setOnClickListener {
            BiltyImageFile = true
            dispatchTakePictureIntent()
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
        Pix.start(this@OUTDeliverdUpoladOrderClass, options)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                val imageBitmap = data?.extras?.get("data") as Bitmap

                if (requestCode == REQUEST_CAMERA) {
                    if (BiltyImageFile) {
                        BiltyImageFile = false
                        fileBiltyImage = File(compressImage(bitmapToFile(imageBitmap).path))
                        val uri = Uri.fromFile(fileBiltyImage)
                        BiltyFile = uri.toString()
                        binding!!.TruckImage.setImageURI(uri)
                    }
                }
            }
        }
    }






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
        file = File(file,"${UUID.randomUUID()}.jpg")

        try{
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, options)
                } else {
                    dispatchTakePictureIntent()
                    Toast.makeText(this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityAndClear(OUTDeliverdOrderListingActivity::class.java)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_close -> startActivityAndClear(OUTDeliverdOrderListingActivity::class.java)
            R.id.btn_login -> if (binding!!.notes.text.toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(this@OUTDeliverdUpoladOrderClass, "Enter Notes Here!!", Toast.LENGTH_LONG).show()
            } else if (fileBiltyImage == null) {
                Toast.makeText(this@OUTDeliverdUpoladOrderClass, "Upload to Delivered Order File", Toast.LENGTH_LONG).show()
            } else {
                Utility.showDecisionDialog(this@OUTDeliverdUpoladOrderClass, getString(R.string.alert), "Are You Sure to Summit?") { callApi() }
            }
        }
    }

    private fun callApi() {
        var BiltyImage = ""
        if (fileBiltyImage != null) {
            BiltyImage = "" + Utility.transferImageToBase64(fileBiltyImage)
        }
        loginViewModel.uploadDeliveredOrder(UploadReleaseOrderlsPostData(CaseID, stringFromView(binding!!.notes), "", BiltyImage))
        loginViewModel.uploadDeliveryResponse.observe(this){
            when(it){
                is NetworkResult.Error -> hideDialog()
                is NetworkResult.Loading -> showDialog()
                is NetworkResult.Success -> {

                    Utility.showAlertDialog(this@OUTDeliverdUpoladOrderClass, getString(R.string.alert), it.data!!.message) { startActivityAndClear(OUTDeliverdOrderListingActivity::class.java) }

                }
            }
        }

    }
}
