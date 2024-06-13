package com.apnagodam.staff.activity.`in`.first_kantaparchi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadFirstkantaParchiPostData
import com.apnagodam.staff.Network.viewmodel.KantaParchiViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.BaseActivity
import com.apnagodam.staff.databinding.KanthaParchiUploadBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.FirstkanthaParchiListResponse
import com.apnagodam.staff.helper.ImageHelper
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import com.fxn.pix.Options
import com.github.dhaval2404.imagepicker.ImagePicker
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import com.thorny.photoeasy.PhotoEasy
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class UploadFirstkantaParchiClass : BaseActivity<KanthaParchiUploadBinding>() {
    // role of image
    private var UserName: String? = null
    private var CaseID: String? = ""
    private var fileKantha: File? = null
    private var fileKantaOld: File? = null
    private var fileTruck: File? = null
    private var fileTruck2: File? = null
    private var oldKantaParchiFile: File? = null
    private var firstKanthaFile = false
    private var firstOldKantaFile = false;

    private var isOldKantaParchiFile = false;
    private var truckImage = false
    private var truckImage2 = false;
    private var firstkantaParchiFile: String? = null
    private var firstOldKantaParchiFile: String? = null
    private var TruckImage: String? = null
    private var TruckImage2: String? = null
    private var oldKantaImage: String? = null
    private lateinit var searchableSpinner: SearchableSpinner
    private var options: Options? = null
    private val kantaParchiViewModel by viewModels<KantaParchiViewModel>()
    private lateinit var allCases: FirstkanthaParchiListResponse.Datum
    private lateinit var dharamKantas: ArrayList<FirstkanthaParchiListResponse.DharemKanta>
    private var listOfKantaNames = arrayListOf<String>()
    private var kantaId = 0
    private var kantaName = "";
    private var isFirstUpload = true;

    override fun setUI() {
        photoEasy = PhotoEasy.builder().setActivity(this).build()
        getCurrentLocation()



        binding.tvTitle.setText("Upload First Kanta Parchi")
        UserName = intent?.getStringExtra("user_name")
        CaseID = intent?.getStringExtra("case_id")
        file3 = intent?.getStringExtra("file3")


//        allCases = intent.getSerializableExtra("all_cases") as FirstkanthaParchiListResponse.Datum
        dharamKantas = arrayListOf()
        searchableSpinner = SearchableSpinner(this)


        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.customerName.text = UserName
        binding.caseId.text = CaseID
        binding.llOldBags.visibility = View.VISIBLE
        binding.llBags.visibility = View.GONE

        if (file3 == null) {
            isFirstUpload = true

        } else {
            isFirstUpload = false;

        }


        if (isFirstUpload) {
            binding.llKanta.visibility = View.GONE
            binding.llOldBags.visibility = View.GONE
            binding.cardTruck2.visibility = View.VISIBLE

        } else {
            binding.llKanta.visibility = View.VISIBLE
            binding.llOldBags.visibility = View.VISIBLE
            binding.cardTruck2.visibility = View.GONE
        }


        clickListner()

    }

    override fun setObservers() {
        kantaParchiViewModel.kantaParchiResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> hideDialog(
                    this
                )

                is NetworkResult.Loading -> showDialog(this)
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
                                    position: Int, selectedString: String
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
        kantaParchiViewModel.uploadFirstKantaParchiResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog(this)
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                }

                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    if (it.data!!.status == "0") {
                        Utility.showAlertDialog(
                            this@UploadFirstkantaParchiClass,
                            getString(R.string.alert),
                            it.data.getMessage()
                        ) { }
                    } else {
                        Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()

                        finish()
                    }
                    hideDialog(this)

                }
            }
        }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): KanthaParchiUploadBinding =
        KanthaParchiUploadBinding.inflate(layoutInflater)

    override fun callApis() {
        getKantaList("")

    }

    var file3: String? = null
    lateinit var photoEasy: PhotoEasy


    fun validData(bundle: Bundle?): Boolean {
        return bundle != null
    }

    private fun getKantaList(search: String) {
        kantaParchiViewModel.getKantaParchiListing("10", "0", "IN", search)


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
            checkForPermission {
                ImagePicker.with(this@UploadFirstkantaParchiClass).cameraOnly().start();
            }
            //  onImageSelected()
            // callImageSelector(REQUEST_CAMERA);
        }
        binding!!.uploadKanthaOld.setOnClickListener {
            isOldKantaParchiFile = false;
            firstOldKantaFile = true
            firstKanthaFile = false
            truckImage = false
            truckImage2 = false
            checkForPermission {
                ImagePicker.with(this@UploadFirstkantaParchiClass).cameraOnly().start();
            }

        }
        binding!!.uploadTruck.setOnClickListener {
            isOldKantaParchiFile = false
            firstKanthaFile = false
            firstKanthaFile = false
            truckImage = true
            truckImage2 = false
            checkForPermission {
                ImagePicker.with(this@UploadFirstkantaParchiClass).cameraOnly().start();
            }
            // onImageSelected()
            //   callImageSelector(REQUEST_CAMERA);
        }
        binding!!.uploadTruck2.setOnClickListener {
            isOldKantaParchiFile = false
            firstKanthaFile = false
            firstOldKantaFile = false
            truckImage = false
            truckImage2 = true
            checkForPermission { ImagePicker.with(this@UploadFirstkantaParchiClass).start(); }

        }
        binding!!.uploadOldKanta.setOnClickListener {
            isOldKantaParchiFile = true
            firstKanthaFile = false
            firstOldKantaFile = false
            truckImage = false
            truckImage2 = false
            checkForPermission {
                ImagePicker.with(this@UploadFirstkantaParchiClass).cameraOnly().start();
            }
        }
        binding!!.oldKantaImage.setOnClickListener {
            PhotoFullPopupWindow(
                this@UploadFirstkantaParchiClass, R.layout.popup_photo_full, it, oldKantaImage, null
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
                this@UploadFirstkantaParchiClass, R.layout.popup_photo_full, view, TruckImage, null
            )
        }
        binding!!.truckImage2.setOnClickListener {
            PhotoFullPopupWindow(
                this@UploadFirstkantaParchiClass, R.layout.popup_photo_full, it, TruckImage2, null
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
                showDialog(this)
                kantaParchiViewModel.uploadFirstKantaParchi(
                    UploadFirstkantaParchiPostData(
                        CaseID,
                        binding!!.notes.text.toString(),
                        KanthaImage,
                        truckImageImage,
                        truck2Image,
                        kantaId.toString(),
                        binding!!.etKantaParchiNum.text.toString(),
                        oldKanthaImage,
                        binding!!.etKantaOldParchiNum.text.toString(),
                        "0",
                        binding!!.etOldNoOfBags.text.toString(),
                        "",
                        "",
                        "",
                        "",
                        "",
                        binding!!.etKantaOldLocation.text.toString()

                    ), "IN"
                )

            } else {
                Toast.makeText(this, "Please select truck image!", Toast.LENGTH_SHORT).show()
            }
        } else {
            if (binding!!.tilKantaParchi.text!!.equals("Select Dharam Kanta")) {
                Toast.makeText(this, "Please select kanta name", Toast.LENGTH_SHORT).show();

            } else if (binding!!.etKantaParchiNum.text!!.isEmpty()) {
                binding!!.etKantaParchiNum.setError("This field can't be empty")
            } else if (fileKantha == null) {
                Toast.makeText(this, "please select kanta parchi image!", Toast.LENGTH_SHORT).show()
            } else if (fileTruck == null) {
                Toast.makeText(this, "Please select truck image!", Toast.LENGTH_SHORT).show()
            } else {
                showDialog(this)
                kantaParchiViewModel.uploadFirstKantaParchi(
                    UploadFirstkantaParchiPostData(
                        CaseID,
                        binding!!.notes.text.toString(),
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
                        binding!!.etKantaOldGrossWeight.text.toString(),
                        binding!!.etKantaOldLocation.text.toString()
                    ), "IN"
                )


            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (requestCode == Activity.RESULT_OK || requestCode == 2404) {
                val userDetails = SharedPreferencesRepository.getDataManagerInstance().user
                val uri: Uri = data?.data!!

                var stampMap = mapOf(
                    "current_location" to "$currentLocation",
                    "emp_code" to userDetails.emp_id,
                    "emp_name" to userDetails.fname
                )
                val thumbnail = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                if (thumbnail != null) {
                    if (firstKanthaFile) {

                        var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                            File(compressImage(bitmapToFile(thumbnail!!).path)), stampMap
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
                            File(compressImage(bitmapToFile(thumbnail!!).path)), stampMap
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
                            File(compressImage(bitmapToFile(thumbnail!!).path)), stampMap
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
                            File(compressImage(bitmapToFile(thumbnail!!).path)), stampMap
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
                            File(compressImage(bitmapToFile(thumbnail!!).path)), stampMap
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

                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }

            }
        } catch (e: Exception) {
            showToast(this, "Please Select an Image")
        }
//        photoEasy.onActivityResult(1566, -1, object : OnPictureReady {
//            override fun onFinish(thumbnail: Bitmap?) {
//
//            }
//
//        })

    }


    fun dispatchTakePictureIntent() {


    }





}
