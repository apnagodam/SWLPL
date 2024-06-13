package com.apnagodam.staff.activity.out.f_katha_parchi

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
class OutUploadFirrstkantaParchiClass : BaseActivity<KanthaParchiUploadBinding>() {
    var UserName: String? = null
    var CaseID: String? = ""
    var fileKantha: File? = null
    var fileKantaOld: File? = null
    var fileTruck: File? = null
    var fileTruck2: File? = null
    var firstKanthaFile = false
    var firstOldKantaFile = false;
    var truckImage = false
    var truckImage2 = false;
    private var firstkantaParchiFile: String? = null
    private var firstOldKantaParchiFile: String? = null
    private var TruckImage: String? = null
    private var TruckImage2: String? = null
    lateinit var searchableSpinner: SearchableSpinner
    var options: Options? = null
    val kantaParchiViewModel by viewModels<KantaParchiViewModel>()
    lateinit var allCases: FirstkanthaParchiListResponse.Datum
    lateinit var dharamKantas: ArrayList<FirstkanthaParchiListResponse.DharemKanta>
    var listOfKantaNames = arrayListOf<String>()
    var kantaId = 0
    var kantaName = "";
    var isFirstUpload = true;

    lateinit var photoEasy: PhotoEasy
    override fun setUI() {
        binding!!.llTraupline.visibility = View.GONE
        binding!!.llOldBags.visibility = View.GONE
        binding!!.etKantaOldLocation.visibility == View.GONE
        photoEasy = PhotoEasy.builder().setActivity(this)
            .build()

        getCurrentLocation()




        binding!!.tvTitle.setText("Upload First Kanta Parchi")
        UserName = intent.getStringExtra("user_name")
        CaseID = intent.getStringExtra("case_id")
        dharamKantas = arrayListOf()
        searchableSpinner = SearchableSpinner(this)


        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.customerName.text = UserName
        binding!!.caseId.text = CaseID
        binding!!.llOldBags.visibility = View.VISIBLE
        binding!!.llBags.visibility = View.GONE

        isFirstUpload = intent.getStringExtra("file3") == null
        binding!!.llOldBags.visibility = View.GONE


        if (isFirstUpload) {
            binding!!.llKanta.visibility = View.GONE
//                       binding!!.llOldBags.visibility = View.GONE
            binding!!.cardTruck2.visibility = View.VISIBLE

        } else {
            binding!!.llKanta.visibility = View.VISIBLE
//            binding!!.llOldBags.visibility = View.VISIBLE
            binding!!.cardTruck2.visibility = View.GONE
        }

        binding!!.cardOldKantaParchi.visibility = View.GONE

        clickListner()

    }

    override fun setObservers() {
        kantaParchiViewModel.kantaParchiResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> hideDialog(this)
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
        kantaParchiViewModel.uploadFirstKantaParchiResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
                }

                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    if (it.data!!.status == "0") {
                        Utility.showAlertDialog(
                            this@OutUploadFirrstkantaParchiClass,
                            getString(R.string.alert),
                            it.data!!.getMessage()
                        ) { }
                    } else {
                        finish()
                        Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT)
                    }

                }
            }
        }

    }

    override fun inflateLayout(layoutInflater: LayoutInflater): KanthaParchiUploadBinding =
        KanthaParchiUploadBinding.inflate(layoutInflater)

    override fun callApis() {
        getKantaList("")

    }


    private fun getKantaList(search: String) {
        kantaParchiViewModel.getKantaParchiListing("10", "0", "OUT", search)


    }


    private fun clickListner() {
        binding!!.ivClose.setOnClickListener {
            finish()
        }
        binding!!.tilKantaParchi.setOnClickListener {
            searchableSpinner.show()
        }
        binding!!.btnLogin.setOnClickListener {
            onNext()
        }
        binding!!.uploadKantha.setOnClickListener {
            firstOldKantaFile = false
            firstKanthaFile = true
            truckImage = false
            truckImage2 = false
            dispatchTakePictureIntent(false)

            //  onImageSelected()
            // callImageSelector(REQUEST_CAMERA);
        }
        binding!!.uploadKanthaOld.setOnClickListener {

            firstOldKantaFile = true
            firstKanthaFile = false
            truckImage = false
            truckImage2 = false
            dispatchTakePictureIntent(false)

        }
        binding!!.uploadTruck.setOnClickListener {
            firstKanthaFile = false
            firstKanthaFile = false
            truckImage = true
            truckImage2 = false
            dispatchTakePictureIntent(false)
            // onImageSelected()
            //   callImageSelector(REQUEST_CAMERA);
        }
        binding!!.uploadTruck2.setOnClickListener {
            firstKanthaFile = false
            firstOldKantaFile = false
            truckImage = false
            truckImage2 = true
            dispatchTakePictureIntent(false)
        }
        binding!!.KanthaImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@OutUploadFirrstkantaParchiClass,
                R.layout.popup_photo_full,
                view,
                firstkantaParchiFile,
                null
            )
        }
        binding!!.KanthaImageOld.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@OutUploadFirrstkantaParchiClass,
                R.layout.popup_photo_full,
                view,
                firstOldKantaParchiFile,
                null
            )
        }
        binding!!.TruckImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@OutUploadFirrstkantaParchiClass,
                R.layout.popup_photo_full,
                view,
                TruckImage,
                null
            )
        }
        binding!!.truckImage2.setOnClickListener {
            PhotoFullPopupWindow(
                this@OutUploadFirrstkantaParchiClass,
                R.layout.popup_photo_full,
                it,
                TruckImage2,
                null
            )
        }
    }


    fun onNext() {
        var KanthaImage = ""
        var oldKanthaImage = ""
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
        if (fileKantaOld != null) {
            oldKanthaImage = "" + Utility.transferImageToBase64(fileKantaOld)
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
                        binding!!.etOldWeightQt.text.toString(),
                        binding!!.etOldNoOfBags.text.toString(),
                        "",
                        "",
                        "",
                        "",
                        "",
                        binding!!.etKantaOldLocation.text.toString()
                    ), "OUT"
                )

            } else {
                Toast.makeText(this, "Please upload Kanta/Warehouse image!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            if (binding!!.tilKantaParchi.text!!.equals("Select Dharam Kanta")) {
                Toast.makeText(this, "please select Kanta Name", Toast.LENGTH_SHORT)

            } else if (binding!!.etKantaParchiNum.text!!.isEmpty()) {
                binding!!.etKantaParchiNum.setError("This field can't be empty")
            } else if (fileKantha == null) {
                Toast.makeText(this, "please select kanta parchi image!", Toast.LENGTH_SHORT).show()
            } else if (fileTruck == null) {
                Toast.makeText(this, "Please select truck image!", Toast.LENGTH_SHORT).show()
            } else {
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
                        binding!!.etOldWeightQt.text.toString(),
                        binding!!.etOldNoOfBags.text.toString(),
                        "",
                        "",
                        "",
                        "",
                        "",
                        binding!!.etKantaOldLocation.text.toString()
                    ), "OUT"
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
        } catch (e: Exception) {
            showToast(this, "Please select an image")
        }
//        photoEasy.onActivityResult(1566, -1, object : OnPictureReady {
//            override fun onFinish(thumbnail: Bitmap?) {
//
//
//            }
//
//        })
    }


    fun dispatchTakePictureIntent(isKantaParchi: Boolean) {
        if (isKantaParchi) {
            checkForPermission(ImagePicker.with(this)::start)

        } else {
            checkForPermission(ImagePicker.with(this).cameraOnly()::start)
        }


    }


    /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra(Pix.IMAGE_RESULTS)) {
                    ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    assert returnValue != null;
                    Log.e("getImageesValue", returnValue.get(0));
                    if (requestCode == REQUEST_CAMERA) {
                        if (firstKanthaFile) {
                            firstKanthaFile = false;
                            truckImage = false;
                            fileKantha = new File(compressImage(returnValue.get(0)));
                            Uri uri = Uri.fromFile(fileKantha);
                            firstkantaParchiFile = String.valueOf(uri);
                            binding.KanthaImage.setImageURI(uri);
                        } else if (truckImage) {
                            firstKanthaFile = false;
                            truckImage = false;
                            fileTruck = new File(compressImage(returnValue.get(0)));
                            Uri uri = Uri.fromFile(fileTruck);
                            TruckImage = String.valueOf(uri);
                            binding.TruckImage.setImageURI(uri);
                        }
                    }
                }
            }
        }
    }*/
    /*    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
