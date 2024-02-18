package com.apnagodam.staff.activity.`in`.first_kantaparchi

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadFirstkantaParchiPostData
import com.apnagodam.staff.Network.viewmodel.KantaParchiViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.KanthaParchiUploadBinding
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import com.fxn.pix.Options
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

@AndroidEntryPoint
class UploadFirstkantaParchiClass : BaseActivity<KanthaParchiUploadBinding?>() {
    // role of image
    var UserName: String? = null
    var CaseID: String? = ""
    var fileKantha: File? = null
    var fileTruck: File? = null
    var firstKanthaFile = false
    var truckImage = false
    private var firstkantaParchiFile: String? = null
    private var TruckImage: String? = null
    var options: Options? = null
    val kantaParchiViewModel by viewModels<KantaParchiViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.kantha_parchi_upload
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
    }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener {
            startActivityAndClear(
                    FirstkanthaParchiListingActivity::class.java
            )
        }
        binding!!.btnLogin.setOnClickListener {
            Utility.showDecisionDialog(
                    this@UploadFirstkantaParchiClass,
                    getString(R.string.alert),
                    "Are You Sure to Summit?"
            ) {
                if (fileKantha == null) {
                    Toast.makeText(
                            applicationContext,
                            R.string.upload_kanta_parchi_file,
                            Toast.LENGTH_LONG
                    ).show()
                } else if (fileTruck == null) {
                    Toast.makeText(
                            applicationContext,
                            R.string.upload_truck_image,
                            Toast.LENGTH_LONG
                    ).show()
                } else {
                    onNext()
                }
            }
        }
        binding!!.uploadKantha.setOnClickListener {
            firstKanthaFile = true
            truckImage = false
            dispatchTakePictureIntent()

            //  onImageSelected()
            // callImageSelector(REQUEST_CAMERA);
        }
        binding!!.uploadTruck.setOnClickListener {
            firstKanthaFile = false
            truckImage = true
            dispatchTakePictureIntent()
            // onImageSelected()
            //   callImageSelector(REQUEST_CAMERA);
        }
        binding!!.KanthaImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                    this@UploadFirstkantaParchiClass,
                    R.layout.popup_photo_full,
                    view,
                    firstkantaParchiFile,
                    null
            )
        }
        binding!!.TruckImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                    this@UploadFirstkantaParchiClass,
                    R.layout.popup_photo_full,
                    view,
                    TruckImage,
                    null
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityAndClear(FirstkanthaParchiListingActivity::class.java)
    }

    /*private void callImageSelector(int requestCamera) {
        options = Options.init()
                .setRequestCode(requestCamera)                                                 //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setExcludeVideos(false)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(30)                                   //Option to exclude videos
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/apnagodam/lp/images");                                       //Custom Path For media Storage
        Pix.start(UploadFirstkantaParchiClass.this, options);
    }*/
    // update file
    fun onNext() {
        var KanthaImage = ""
        var truckImageImage = ""
        if (fileKantha != null) {
            KanthaImage = "" + Utility.transferImageToBase64(fileKantha)
        }
        if (fileTruck != null) {
            truckImageImage = "" + Utility.transferImageToBase64(fileTruck)
        }
        //else {

        kantaParchiViewModel.uploadFirstKantaParchi(
                UploadFirstkantaParchiPostData(
                        CaseID, stringFromView(
                        binding!!.notes
                ), KanthaImage, truckImageImage
                )
        )
        kantaParchiViewModel.uploadFirstKantaParchiResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    Utility.showAlertDialog(
                            this@UploadFirstkantaParchiClass,
                            getString(R.string.alert),
                            it.data!!.getMessage()
                    ) { startActivityAndClear(FirstkanthaParchiListingActivity::class.java) }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CAMERA) {
                    if (firstKanthaFile) {
                        val imageBitmap = data?.extras?.get("data") as Bitmap

                        firstKanthaFile = false
                        truckImage = false
                        fileKantha = File(compressImage(bitmapToFile(imageBitmap).toString()))
                        val uri = Uri.fromFile(fileKantha)
                        firstkantaParchiFile = uri.toString()
                        binding!!.KanthaImage.setImageURI(uri)

                    } else if (truckImage) {
                        val imageBitmap = data?.extras?.get("data") as Bitmap
                        firstKanthaFile = false
                        truckImage = false
                        fileTruck = File(compressImage(bitmapToFile(imageBitmap).toString()))
                        val uri = Uri.fromFile(fileTruck)
                        TruckImage = uri.toString()
                        binding!!.TruckImage.setImageURI(uri)
                    }
                }
            }
        } catch (e: Exception) {
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
    /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra(Pix.IMAGE_RESULTS)) {
                    ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    assert returnValue != null;
                    Log.e("getImageesValue", returnValue.get(0).toString());
                    if (requestCode == REQUEST_CAMERA) {
                        if (firstKanthaFile) {
                            firstKanthaFile = false;
                            truckImage = false;
                            fileKantha = new File(compressImage(returnValue.get(0).toString()));
                            Uri uri = Uri.fromFile(fileKantha);
                            firstkantaParchiFile = String.valueOf(uri);
                            binding.KanthaImage.setImageURI(uri);
                        } else if (truckImage) {
                            firstKanthaFile = false;
                            truckImage = false;
                            fileTruck = new File(compressImage(returnValue.get(0).toString()));
                            Uri uri = Uri.fromFile(fileTruck);
                            TruckImage = String.valueOf(uri);
                            binding.TruckImage.setImageURI(uri);
                        }
                    }
                }
            }
        }
    }*/
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, options);
                } else {
                    callImageSelector(REQUEST_CAMERA);
                    Toast.makeText(this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }*/
}
