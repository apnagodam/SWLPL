package com.apnagodam.staff.activity.`in`.first_kantaparchi

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
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadFirstkantaParchiPostData
import com.apnagodam.staff.Network.viewmodel.KantaParchiViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.KanthaParchiUploadBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.FirstkanthaParchiListResponse
import com.apnagodam.staff.utils.ImageHelper
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.fxn.pix.Options
import com.google.android.gms.location.LocationServices
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import com.thorny.photoeasy.OnPictureReady
import com.thorny.photoeasy.PhotoEasy
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class UploadFirstkantaParchiClass : BaseActivity<KanthaParchiUploadBinding?>() {
    // role of image
    var UserName: String? = null
    var CaseID: String? = ""
    var fileKantha: File? = null
    var fileKantaOld: File? = null
    var fileTruck: File? = null
    var fileTruck2: File? = null
    var oldKantaParchiFile: File? = null
    var firstKanthaFile = false
    var firstOldKantaFile = false;

    var isOldKantaParchiFile = false;
    var truckImage = false
    var truckImage2 = false;
    private var firstkantaParchiFile: String? = null
    private var firstOldKantaParchiFile: String? = null
    private var TruckImage: String? = null
    private var TruckImage2: String? = null
    private var oldKantaImage: String? = null
    lateinit var searchableSpinner: SearchableSpinner
    var options: Options? = null
    val kantaParchiViewModel by viewModels<KantaParchiViewModel>()
    lateinit var allCases: FirstkanthaParchiListResponse.Datum
    lateinit var dharamKantas: ArrayList<FirstkanthaParchiListResponse.DharemKanta>
    var listOfKantaNames = arrayListOf<String>()
    var kantaId = 0
    var kantaName = "";
    var isFirstUpload = true;
    var lat = 0.0
    var long = 0.0
    var file3: String? = null
    lateinit var photoEasy: PhotoEasy
    var currentLocation = ""
    override fun getLayoutResId(): Int {
        return R.layout.kantha_parchi_upload
    }

    override fun setUp() {
        val bundle = intent.getBundleExtra(BUNDLE)

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
                if (it != null) {
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
        binding!!.tvTitle.setText("Upload First Kanta Parchi")
        UserName = intent?.getStringExtra("user_name")
        CaseID = intent?.getStringExtra("case_id")
        file3 = intent?.getStringExtra("file3")
        if (validData(bundle)) {

        }


//        allCases = intent.getSerializableExtra("all_cases") as FirstkanthaParchiListResponse.Datum
        dharamKantas = arrayListOf()
        searchableSpinner = SearchableSpinner(this)

        getKantaList("")

        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.customerName.text = UserName
        binding!!.caseId.text = CaseID
        binding!!.llOldBags.visibility = View.VISIBLE
        binding!!.llBags.visibility = View.GONE

        if (file3 == null) {
            isFirstUpload = true

        } else {
            isFirstUpload = false;

        }


        if (isFirstUpload) {
            binding!!.llKanta.visibility = View.GONE
            binding!!.llOldBags.visibility = View.GONE
            binding!!.cardTruck2.visibility = View.VISIBLE

        } else {
            binding!!.llKanta.visibility = View.VISIBLE
            binding!!.llOldBags.visibility = View.VISIBLE
            binding!!.cardTruck2.visibility = View.GONE
        }


        clickListner()


    }

    fun validData(bundle: Bundle?): Boolean {
        return bundle != null
    }

    private fun getKantaList(search: String) {
        kantaParchiViewModel.getKantaParchiListing("10", "0", "IN", search)
        kantaParchiViewModel.kantaParchiResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> hideDialog()
                is NetworkResult.Loading -> showDialog()
                is NetworkResult.Success -> {

                    if (it.data != null) {
                        if (it.data.dharemKantas != null) {
                            searchableSpinner.windowTitle = "Select Kanta"

                            dharamKantas.clear()
                            for (i in it.data.dharemKantas) {
                                listOfKantaNames.add(i.name)
                            }
                            searchableSpinner.setSpinnerListItems(listOfKantaNames)

                            searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
                                override fun setOnItemSelectListener(
                                    position: Int,
                                    selectedString: String
                                ) {
                                    binding!!.tilKantaParchi.setText(selectedString)
                                    kantaId = it.data.dharemKantas[position].id
                                }
                            }
                        }

//            //Setting Visibility for views in SearchableSpinner
//            searchableSpinner.searchViewVisibility = SearchableSpinner.SpinnerView.GONE
//            searchableSpinner.negativeButtonVisibility = SearchableSpinner.SpinnerView.GONE
//            searchableSpinner.windowTitleVisibility = SearchableSpinner.SpinnerView.GONE
                        //Setting up list items for spinner

                    }
                }
            }
        }


    }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener {
            kantaParchiViewModel.getKantaParchiListing("10", "1", "IN", "")

            onBackPressedDispatcher.onBackPressed()
        }
        binding!!.tilKantaParchi.setOnClickListener {
            searchableSpinner.show()
        }
        binding!!.btnLogin.setOnClickListener {
            onNext()
        }
        binding!!.uploadKantha.setOnClickListener {
            isOldKantaParchiFile = false
            firstOldKantaFile = false
            firstKanthaFile = true
            truckImage = false
            truckImage2 = false
            dispatchTakePictureIntent()

            //  onImageSelected()
            // callImageSelector(REQUEST_CAMERA);
        }
        binding!!.uploadKanthaOld.setOnClickListener {
            isOldKantaParchiFile = false;
            firstOldKantaFile = true
            firstKanthaFile = false
            truckImage = false
            truckImage2 = false
            dispatchTakePictureIntent()

        }
        binding!!.uploadTruck.setOnClickListener {
            isOldKantaParchiFile = false
            firstKanthaFile = false
            firstKanthaFile = false
            truckImage = true
            truckImage2 = false
            dispatchTakePictureIntent()
            // onImageSelected()
            //   callImageSelector(REQUEST_CAMERA);
        }
        binding!!.uploadTruck2.setOnClickListener {
            isOldKantaParchiFile = false
            firstKanthaFile = false
            firstOldKantaFile = false
            truckImage = false
            truckImage2 = true
            dispatchTakePictureIntent()
        }
        binding!!.uploadOldKanta.setOnClickListener {
            isOldKantaParchiFile = true
            firstKanthaFile = false
            firstOldKantaFile = false
            truckImage = false
            truckImage2 = false
            dispatchTakePictureIntent()
        }
        binding!!.oldKantaImage.setOnClickListener {
            PhotoFullPopupWindow(
                this@UploadFirstkantaParchiClass,
                R.layout.popup_photo_full,
                it,
                oldKantaImage,
                null
            )
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
        binding!!.KanthaImageOld.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@UploadFirstkantaParchiClass,
                R.layout.popup_photo_full,
                view,
                firstOldKantaParchiFile,
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
        binding!!.truckImage2.setOnClickListener {
            PhotoFullPopupWindow(
                this@UploadFirstkantaParchiClass,
                R.layout.popup_photo_full,
                it,
                TruckImage2,
                null
            )
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
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
        var oldKanthaImage = ""
        var truckImageImage = ""
        var truck2Image = ""
        var oldKantaImage = ""
        if (fileKantha != null) {
            KanthaImage = "" + Utility.transferImageToBase64(fileKantha)
        }
        if (fileTruck != null) {
            truckImageImage = "" + Utility.transferImageToBase64(fileTruck)
        }
        if (fileTruck2 != null) {
            truck2Image = "" + Utility.transferImageToBase64(fileTruck2)
        }
        if (fileKantaOld != null) {
            oldKanthaImage = "" + Utility.transferImageToBase64(fileKantaOld)
        }
        if (oldKantaParchiFile != null) {
            oldKantaImage = "" + Utility.transferImageToBase64(oldKantaParchiFile)
        }
        //else {
        if (isFirstUpload) {
            if (fileTruck2 != null) {
                showDialog()
                kantaParchiViewModel.uploadFirstKantaParchi(
                    UploadFirstkantaParchiPostData(
                        CaseID,
                        stringFromView(
                            binding!!.notes
                        ),
                        KanthaImage,
                        truckImageImage,
                        truck2Image,
                        kantaId.toString(),
                        binding!!.etKantaParchiNum.text.toString(),
                        oldKanthaImage,
                        binding!!.etKantaOldParchiNum.text.toString(),
                        "0",
                        binding!!.etOldNoOfBags.text.toString(),
                        "", "", "", "", ""

                    ), "IN"
                )
                kantaParchiViewModel.uploadFirstKantaParchiResponse.observe(this) {
                    when (it) {
                        is NetworkResult.Error -> {
                            hideDialog()
                            showToast(it.message)
                        }

                        is NetworkResult.Loading -> {}
                        is NetworkResult.Success -> {
                            if (it.data!!.status == "0") {
                                Utility.showAlertDialog(
                                    this@UploadFirstkantaParchiClass,
                                    getString(R.string.alert),
                                    it.data!!.getMessage()
                                ) { }
                            } else {
                                showToast(it.data.message)
                                finish()
                            }
                            hideDialog()

                        }
                    }
                }
            }
            else {
                Toast.makeText(this,"Please select truck image!",Toast.LENGTH_SHORT).show()
            }
        } else {
            if (binding!!.tilKantaParchi.text!!.equals("Select Dharam Kanta")) {
                showToast("Please select kanta name")
            } else if (binding!!.etKantaParchiNum.text!!.isEmpty()) {
                binding!!.etKantaParchiNum.setError("This field can't be empty")
            } else if (fileKantha == null) {
                Toast.makeText(this, "please select kanta parchi image!", Toast.LENGTH_SHORT).show()
            } else if (fileTruck == null) {
                Toast.makeText(this, "Please select truck image!", Toast.LENGTH_SHORT).show()
            } else {
                showDialog()
                kantaParchiViewModel.uploadFirstKantaParchi(
                    UploadFirstkantaParchiPostData(
                        CaseID,
                        stringFromView(
                            binding!!.notes
                        ),
                        KanthaImage,
                        truckImageImage,
                        truck2Image,
                        kantaId.toString(),
                        binding!!.etKantaParchiNum.text.toString(),
                        oldKantaImage,
                        binding!!.etKantaOldParchiNum.text.toString(),
                        "0",
                        binding!!.etOldNoOfBags.text.toString(),
                        binding!!.etKantaOldParchiNum.text.toString(),
                        binding!!.etKantaOldName.text.toString(),
                        binding!!.etKantaOldNetWeight.text.toString(),
                        binding!!.etKantaOldTare.text.toString(),
                        binding!!.etKantaOldGrossWeight.text.toString()
                    ),
                    "IN"
                )
                kantaParchiViewModel.uploadFirstKantaParchiResponse.observe(this) {
                    when (it) {
                        is NetworkResult.Error -> {
                            hideDialog()
                            showToast(it.message)
                        }

                        is NetworkResult.Loading -> {}
                        is NetworkResult.Success -> {
                            if (it.data!!.status == "0") {
                                Utility.showAlertDialog(
                                    this@UploadFirstkantaParchiClass,
                                    getString(R.string.alert),
                                    it.data!!.getMessage()
                                ) { }
                            } else {
                                showToast(it.data.message)
                                finish()
                            }
                            hideDialog()
                        }
                    }
                }
            }
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

                    } else if (isOldKantaParchiFile) {
                        var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                            File(compressImage(bitmapToFile(thumbnail!!).path)),
                            stampMap
                        )

                        firstKanthaFile = false
                        truckImage = false
                        truckImage2 = false
                        isOldKantaParchiFile = false
                        oldKantaParchiFile =
                            File(compressImage(bitmapToFile(stampedBitmap).toString()))
                        val uri = Uri.fromFile(oldKantaParchiFile)
                        firstOldKantaParchiFile = uri.toString()
                        binding!!.oldKantaImage.setImageURI(uri)
                    } else if (firstOldKantaFile) {

                        var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                            File(compressImage(bitmapToFile(thumbnail!!).path)),
                            stampMap
                        )
                        firstOldKantaFile = false
                        firstKanthaFile = false
                        truckImage = false
                        truckImage2 = false
                        fileKantha = File(compressImage(bitmapToFile(stampedBitmap).toString()))
                        val uri = Uri.fromFile(fileKantha)
                        firstOldKantaParchiFile = uri.toString()
                        binding!!.KanthaImageOld.setImageURI(uri)

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
                        firstOldKantaFile = false
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


    override fun dispatchTakePictureIntent() {

        permissionsBuilder(Manifest.permission.CAMERA).build().send() {
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

}
