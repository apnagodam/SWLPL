package com.apnagodam.staff.activity.`in`.secound_kanthaparchi

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadFirstkantaParchiPostData
import com.apnagodam.staff.Network.Request.UploadSecoundkantaParchiPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.viewmodel.KantaParchiViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.KanthaParchiUploadBinding
import com.apnagodam.staff.module.FirstkanthaParchiListResponse
import com.apnagodam.staff.module.SecoundkanthaParchiListResponse
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import com.fxn.pix.Options
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
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
    var InBardhanaID =1
    var kantaId = 0;
    var kantaName = ""
    var kantaParchiNumber = ""
    override fun getLayoutResId(): Int {
        return R.layout.kantha_parchi_upload
    }

    override fun setUp() {
        binding!!.etKantaParchiNum.isEnabled = false
        binding!!.llOldBags.visibility = View.GONE
        binding!!.llBags.visibility = View.VISIBLE
        allCases = intent.getSerializableExtra("all_cases") as SecoundkanthaParchiListResponse.Datum
        CaseID = allCases.caseId
        val bundle = intent.getBundleExtra(BUNDLE)
        if (bundle != null) {
            UserName = bundle.getString("user_name")

        }

        if (allCases.file3 == null) {
            binding!!.llKanta.visibility = View.GONE
            binding!!.cardTruck2.visibility = View.VISIBLE


        } else {
            binding!!.cardTruck2.visibility = View.GONE
            binding!!.llKanta.visibility = View.VISIBLE
            binding!!.llBags.visibility = View.VISIBLE

        }


        kantaParchiViewModel.getKantaDetails(CaseID.toString())
        kantaParchiViewModel.dharamKantaResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    if (it.data != null) {
                        binding!!.etKantaParchi.setText(it.data.data.kantaName.toString())
                        binding!!.etKantaParchiNum.setText(it.data.data.kantaParchiNumber.toString())

                        kantaParchiNumber = it.data.data.kantaParchiNumber
                        kantaName = it.data.data.kantaName
                        kantaId = it.data.data.kantaId

                    }
                }
            }
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
                SecoundkanthaParchiListingActivity::class.java
            )
        }
        binding!!.btnLogin.setOnClickListener {
            Utility.showDecisionDialog(
                this@UploadSecoundkantaParchiClass,
                getString(R.string.alert),
                "Are You Sure to Summit?"
            ) {
                if (fileKantha == null && fileTruck==null) {
                    Toast.makeText(
                        applicationContext,
                        R.string.upload_kanta_parchi_file,
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

    /* private void callImageSelector(int requestCamera) {
        options = Options.init()
                .setRequestCode(requestCamera)                                                 //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setExcludeVideos(false)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/apnagodam/lp/images");                                       //Custom Path For media Storage
        Pix.start(UploadSecoundkantaParchiClass.this, options);
    }*/
    // update file
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

        if (binding!!.etWeight.text!!.isNotEmpty() && binding!!.etNoOfBags.text!!.isNotEmpty() && binding!!.etNoOfDispleasedBags.text!!.isNotEmpty()) {
            kantaParchiViewModel.uploadSecondKantaParchi(
                UploadSecoundkantaParchiPostData(
                    CaseID,
                    stringFromView(
                        binding!!.notes
                    ),
                    KanthaImage, truckImageImage, truck2Image,
                    binding!!.etNoOfBags.text.toString(),
                    binding!!.etWeightQt.text.toString(),
                    binding!!.etNoOfDispleasedBags.text.toString(),
                    kantaId, kantaName, kantaParchiNumber,InTrackID,InBardhanaID
                )
            )

            kantaParchiViewModel.uploadSecondKantaParchiResponse.observe(this) {
                when (it) {
                    is NetworkResult.Error -> {

                    }

                    is NetworkResult.Loading -> {

                    }

                    is NetworkResult.Success -> {
                        Utility.showAlertDialog(
                            this@UploadSecoundkantaParchiClass,
                            getString(R.string.alert),
                            it.data!!.getMessage()
                        ) { startActivityAndClear(SecoundkanthaParchiListingActivity::class.java) }
                    }
                }
            }
        } else {
            if (binding!!.etWeightQt.text!!.isEmpty())
                binding!!.etWeightQt.setError("Field cant be empty")

            if (binding!!.etNoOfBags.text!!.isEmpty())
                binding!!.etNoOfBags.setError("Field cant be empty")
            if (binding!!.etNoOfDispleasedBags.text!!.isEmpty())
                binding!!.etNoOfDispleasedBags.setError("Field cant be empty")
        }
//        if (allCases.file3 != null) {
//
//
//
//        } else {
//            kantaParchiViewModel.uploadSecondKantaParchi(
//                UploadSecoundkantaParchiPostData(
//                    CaseID,
//                    stringFromView(
//                        binding!!.notes
//                    ),
//                    KanthaImage,
//                    truckImageImage,
//                    truck2Image,
//                    binding!!.etNoOfBags.text.toString(),
//                    binding!!.etWeightQt.text.toString(),
//                    binding!!.etNoOfDispleasedBags.text.toString(),
//                    kantaId,
//                    kantaName,
//                    kantaParchiNumber,
//                    InTrackID,InBardhanaID
//
//                )
//            )
//
//            kantaParchiViewModel.uploadSecondKantaParchiResponse.observe(this) {
//                when (it) {
//                    is NetworkResult.Error -> {
//
//                    }
//
//                    is NetworkResult.Loading -> {
//
//                    }
//
//                    is NetworkResult.Success -> {
//                        Utility.showAlertDialog(
//                            this@UploadSecoundkantaParchiClass,
//                            getString(R.string.alert),
//                            it.data!!.getMessage()
//                        ) { startActivityAndClear(SecoundkanthaParchiListingActivity::class.java) }
//                    }
//                }
//            }
//        }

        //else {

        // }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CAMERA) {
                    if (firstKanthaFile) {
                        val imageBitmap = data?.extras?.get("data") as Bitmap

                        firstKanthaFile = false
                        truckImage = false
                        truckImage2 = false
                        fileKantha = File(compressImage(bitmapToFile(imageBitmap).toString()))
                        val uri = Uri.fromFile(fileKantha)
                        firstkantaParchiFile = uri.toString()
                        binding!!.KanthaImage.setImageURI(uri)

                    } else if (truckImage) {
                        val imageBitmap = data?.extras?.get("data") as Bitmap
                        firstKanthaFile = false
                        truckImage = false
                        truckImage2 = false
                        fileTruck = File(compressImage(bitmapToFile(imageBitmap).toString()))
                        val uri = Uri.fromFile(fileTruck)
                        TruckImage = uri.toString()
                        binding!!.TruckImage.setImageURI(uri)
                    } else if (truckImage2) {
                        val imageBitmap = data?.extras?.get("data") as Bitmap
                        firstKanthaFile = false
                        truckImage = false
                        truckImage2 = false
                        fileTruck2 = File(compressImage(bitmapToFile(imageBitmap).toString()))
                        val uri = Uri.fromFile(fileTruck2)
                        TruckImage2 = uri.toString()
                        binding!!.truckImage2.setImageURI(uri)
                    }
                }
            }
        } catch (e: Exception) {
            showToast(e.message)
        }
    }

    /*  @Override
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
    }

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
    }
*/
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityAndClear(SecoundkanthaParchiListingActivity::class.java)
    }
}
