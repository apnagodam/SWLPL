package com.apnagodam.staff.activity.`in`.gatepass

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkCallbackWProgress
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.DharmaKanthaPostData
import com.apnagodam.staff.Network.Request.OTPGatePassData
import com.apnagodam.staff.Network.Request.OTPVerifyGatePassData
import com.apnagodam.staff.Network.Request.UploadGatePassPostDataNew
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.viewmodel.GatePassViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityGatePassBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.CheckInventory
import com.apnagodam.staff.module.GatePassListResponse
import com.apnagodam.staff.module.UserDetails
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
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
class UploadGatePassClass() : BaseActivity<ActivityGatePassBinding?>(),
        RadioGroup.OnCheckedChangeListener {
    // role of image
    var UserName: String? = null
    var CaseID: String? = ""
    var TerminalNmae: String? = null
    var VehicleNo: String? = null
    var stackNo: String? = null
    var inout: String? = null
    var fileGatePass: File? = null
    var GatePassFileSelect = false
    private var GatePassFile: String? = null
    var options: Options? = null
    var InTrackType: String? = "null"
    var InTrackID = "0"
    var InBardhanaType: String? = "null"
    var InBardhanaID = "0"
    lateinit var SpinnerKanthaAdapter: ArrayAdapter<String>
    lateinit var allCases: GatePassListResponse.Datum
    var kanthaID: String? = null
    lateinit var kanthaName: ArrayList<String>
    lateinit var data: ArrayList<DharmaKanthaPostData.Datum>
    private var TotalBag = 0
    private var TOtalWeight: Double? = null

    val gatePassViewModel by viewModels<GatePassViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_gate_pass
    }

    override fun setUp() {
        kanthaName = ArrayList()
        kanthaName.add("Select Dharam Kanta")
        val bundle = intent.getBundleExtra(BUNDLE)
        allCases = intent.getSerializableExtra("all_case") as GatePassListResponse.Datum
        UserName = allCases.custFname
        CaseID = allCases.caseId
        TerminalNmae = allCases.terminal_name
        VehicleNo = allCases.vehicleNo
        stackNo = allCases.stackNo
        inout = allCases.inOut

        binding!!.etBags.setText("${allCases.noOfBags}")
        binding!!.etWeightKg.setText(allCases.totalWeight)
        binding!!.etAvgWeight.setText(allCases.gatepass_avgWeight)
        binding!!.etBags.setText(allCases.noOfBags)
        binding!!.etAvgWeight.setText(allCases.gatepass_avgWeight)
        showDialog()
        kanthaList
        if (inout.equals("OUT", ignoreCase = true)) {
            apiService.checkWeightBag(CaseID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { body ->
                        try {
                            TotalBag = body.bagsTotal
                            TOtalWeight = body.weightTotal
                        } catch (e: Exception) {
                            e.stackTrace
                        }
                    }
                    .subscribe()

        }
        binding!!.etWeightKg.onFocusChangeListener = object : OnFocusChangeListener {
            override fun onFocusChange(v: View, hasFocus: Boolean) {
                if (!hasFocus) {
                    if ((binding!!.etWeightKg.text.toString()
                                    .trim { it <= ' ' } != null) && !binding!!.etWeightKg.text.toString()
                                    .trim { it <= ' ' }
                                    .isEmpty() && inout.equals("OUT", ignoreCase = true)
                    ) {
                        val weight: Double =
                                (binding!!.etWeightKg.text.toString().trim { it <= ' ' }
                                        .toDouble())
                        if (weight > (TOtalWeight)!!) {
                            Utility.showAlertDialog(
                                    this@UploadGatePassClass,
                                    getString(R.string.alert),
                                    "Weight must be Less or equal to Total weight !!! ",
                                    object : Utility.AlertCallback {
                                        override fun callback() {
                                            binding!!.etWeightKg.setText("")
                                        }
                                    })
                        } else {
                        }
                    }
                } else {
                }
            }
        }
        binding!!.etBags.onFocusChangeListener = object : OnFocusChangeListener {
            override fun onFocusChange(v: View, hasFocus: Boolean) {
                if (!hasFocus) {
                    if ((binding!!.etBags.text.toString()
                                    .trim { it <= ' ' } != null) && !binding!!.etBags.text.toString()
                                    .trim { it <= ' ' }
                                    .isEmpty() && inout.equals("OUT", ignoreCase = true)
                    ) {
                        val bags: Double =
                                (binding!!.etBags.text.toString().trim { it <= ' ' }.toDouble())
                        if (bags > TotalBag) {
                            Utility.showAlertDialog(
                                    this@UploadGatePassClass,
                                    getString(R.string.alert),
                                    "Bags must be Less or equal to Total Bags !!! ",
                                    object : Utility.AlertCallback {
                                        override fun callback() {
                                            binding!!.etBags.setText("")
                                        }
                                    })
                        } else {
                        }
                    }
                } else {
                }
            }
        }
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.customerName.text = "Customer Name:-  $UserName"
        binding!!.caseId.text = "Case ID:- $CaseID"
        binding!!.terminalName.text = "Terminal Name:- $TerminalNmae"
        binding!!.vehicleNo.text = "Vehicle No:- ${allCases.vehicleNo}"
        binding!!.etStackNo.setText(" ${allCases.stackNo}")
        clickListner()
        binding!!.etWeightKg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length != 0 && charSequence != "") {
                    val finalWeightQTl = charSequence.toString().trim { it <= ' ' }.toDouble() / 100
                    binding!!.etWeightQt.setText("" + finalWeightQTl)
                    //binding.etWeightKg.setText("" + Utility.round(Double.parseDouble(charSequence.toString().trim()), 2));
                } else {
                    binding!!.etWeightQt.setText("")
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        binding!!.etBags.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length != 0 && charSequence != "") {
                    if (binding!!.etWeightKg.text.toString()
                                    .trim { it <= ' ' } != null && !binding!!.etWeightKg.text.toString()
                                    .trim { it <= ' ' }
                                    .isEmpty()
                    ) {
                        val wrightkg =
                                binding!!.etWeightKg.text.toString().trim { it <= ' ' }.toDouble()
                        val bags = binding!!.etBags.text.toString().trim { it <= ' ' }.toDouble()
                        var finalWeightQTl = wrightkg / bags
                        finalWeightQTl = Utility.round(finalWeightQTl, 2)
                        binding!!.etAvgWeight.setText("" + finalWeightQTl)
                    }
                } else {
                    binding!!.etAvgWeight.setText("0.0")
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        binding!!.etWeightKg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length != 0 && charSequence != "") {
                    if (binding!!.etBags.text.toString()
                                    .trim { it <= ' ' } != null && !binding!!.etBags.text.toString()
                                    .trim { it <= ' ' }
                                    .isEmpty()
                    ) {
                        val wrightkg =
                                binding!!.etWeightKg.text.toString().trim { it <= ' ' }.toDouble()
                        val bags = binding!!.etBags.text.toString().trim { it <= ' ' }.toDouble()
                        var finalWeightQTl = wrightkg / bags
                        finalWeightQTl = Utility.round(finalWeightQTl, 2)
                        binding!!.etAvgWeight.setText("" + Utility.round(finalWeightQTl, 2))
                        //   binding.etWeightKg.setText("" + Utility.round(Double.parseDouble(charSequence.toString().trim()), 2));
                    } else {
                        //  binding.etWeightKg.setText("" + Utility.round(Double.parseDouble(charSequence.toString().trim()), 2));
                    }
                } else {
                    binding!!.etAvgWeight.setText("0.0")
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        // layout Terminal Listing resource and list of items.
        SpinnerKanthaAdapter = ArrayAdapter(this, R.layout.multiline_spinner_item, kanthaName)

        SpinnerKanthaAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)

        // Set Adapter in the spinner
        binding!!.spinnerDharma.adapter = SpinnerKanthaAdapter
        binding!!.spinnerDharma.setSelection(SpinnerKanthaAdapter.getPosition(allCases.terminal_name)
        )
        binding!!.spinnerDharma.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                            parentView: AdapterView<*>,
                            selectedItemView: View,
                            position: Int,
                            id: Long
                    ) {
                        // selected item in the list
                        if (position != 0) {
                            val presentMeterStatusID: String =
                                    parentView.getItemAtPosition(position).toString()
                            for (i in data!!.indices) {
                                if (presentMeterStatusID.contains(data!!.get(i).name)) {
                                    kanthaID = data!!.get(i).id.toString()
                                }
                            }
                        }
                    }

                    override fun onNothingSelected(parentView: AdapterView<*>?) {
                        // your code here
                    }
                }


    }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener(View.OnClickListener {
            startActivityAndClear(
                    GatePassListingActivity::class.java
            )
        })
        binding!!.sendOtp.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (isOTPValid) {
                    if (Utility.isNetworkAvailable(this@UploadGatePassClass)) {
                        if ((stringFromView(binding!!.sendOtp)).contains("Verify")) {
                            if (TextUtils.isEmpty(stringFromView(binding!!.etOtp))) {
                                Toast.makeText(
                                        applicationContext,
                                        "please Enter Mobile Number OTP Here!",
                                        Toast.LENGTH_LONG
                                ).show()
                            } else {
                                gatePassViewModel.verifyGatePassOtp(
                                        OTPVerifyGatePassData(
                                                "" + CaseID, (stringFromView(
                                                binding!!.etOtp
                                        ))
                                        )
                                )

                                gatePassViewModel.gatePassOtpResponse.observe(this@UploadGatePassClass) {
                                    when (it) {
                                        is NetworkResult.Error -> {}
                                        is NetworkResult.Loading -> {}
                                        is NetworkResult.Success -> {
                                            Utility.showAlertDialog(
                                                    this@UploadGatePassClass,
                                                    getString(R.string.alert),
                                                    it.data!!.getMessage(),
                                                    object : Utility.AlertCallback {
                                                        override fun callback() {
                                                            binding!!.ll.visibility = View.GONE
                                                            binding!!.etDriverPhone.isEnabled = false
                                                            binding!!.etDriverPhone.isClickable = false
                                                            binding!!.etDriverPhone.isFocusable = false
                                                            binding!!.etOldDriverName.isEnabled = false
                                                            binding!!.etOldDriverName.isClickable = false
                                                            binding!!.etOldDriverName.isFocusable = false
                                                            binding!!.btnSubmit.visibility = View.VISIBLE
                                                            binding!!.etOldDriverName.setBackgroundColor(
                                                                    resources.getColor(R.color.lightgray)
                                                            )
                                                            binding!!.etDriverPhone.setBackgroundColor(
                                                                    resources.getColor(R.color.lightgray)
                                                            )
                                                        }
                                                    })
                                        }
                                    }
                                }


                            }
                        } else {
                            val userDetails: UserDetails =
                                    SharedPreferencesRepository.getDataManagerInstance().getUser()
                            if ((stringFromView(binding!!.etDriverPhone)).equals(
                                            userDetails.getPhone(),
                                            ignoreCase = true
                                    )
                            ) {
                                Toast.makeText(
                                        applicationContext,
                                        "please Enter Valid Mobile Number",
                                        Toast.LENGTH_LONG
                                ).show()
                            } else if ((stringFromView(binding!!.etDriverPhone)).length < 10 || (stringFromView(
                                            binding!!.etDriverPhone
                                    )).length > 10
                            ) {
                                Toast.makeText(
                                        applicationContext,
                                        "please Enter Valid Mobile Number",
                                        Toast.LENGTH_LONG
                                ).show()
                            } else {
                                apiService.doGatePassOTP(
                                        OTPGatePassData(
                                                stringFromView(binding!!.etDriverPhone),
                                                CaseID
                                        )
                                ).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnNext { body ->
                                            Utility.showAlertDialog(
                                                    this@UploadGatePassClass,
                                                    getString(R.string.alert),
                                                    body.getMessage(),
                                                    object : Utility.AlertCallback {
                                                        override fun callback() {
                                                            binding!!.tilOtp.visibility = View.VISIBLE
                                                            binding!!.reSendOtp.visibility =
                                                                    View.VISIBLE
                                                            binding!!.sendOtp.text = "Verify OTP"
                                                            binding!!.etDriverPhone.setBackgroundColor(
                                                                    resources.getColor(R.color.lightgray)
                                                            )
                                                            binding!!.etOldDriverName.setBackgroundColor(
                                                                    resources.getColor(R.color.lightgray)
                                                            )
                                                            binding!!.etDriverPhone.isEnabled = false
                                                            binding!!.etDriverPhone.isClickable = false
                                                            binding!!.etDriverPhone.isFocusable = false
                                                            binding!!.etOldDriverName.isEnabled = false
                                                            binding!!.etOldDriverName.isClickable =
                                                                    false
                                                            binding!!.etOldDriverName.isFocusable =
                                                                    false
                                                        }
                                                    })
                                        }
                                        .subscribe()


                            }
                        }
                    } else {
                        Utility.showAlertDialog(
                                this@UploadGatePassClass,
                                getString(R.string.alert),
                                getString(R.string.no_internet_connection)
                        )
                    }
                }
            }
        })
        binding!!.reSendOtp.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (isOTPValid) {
                    if (Utility.isNetworkAvailable(this@UploadGatePassClass)) {
                        apiService.doGatePassOTP(
                                OTPGatePassData(
                                        (stringFromView(binding!!.etDriverPhone)),
                                        CaseID
                                )
                        ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext { body ->
                                    Toast.makeText(
                                            this@UploadGatePassClass,
                                            body.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show()
                                    binding!!.etOtp.visibility = View.VISIBLE
                                    binding!!.reSendOtp.visibility = View.VISIBLE
                                    binding!!.sendOtp.text = "Verify OTP"
                                    binding!!.etDriverPhone.setBackgroundColor(resources.getColor(R.color.lightgray))
                                    binding!!.etOldDriverName.setBackgroundColor(
                                            resources.getColor(
                                                    R.color.lightgray
                                            )
                                    )
                                    binding!!.etDriverPhone.isEnabled = false
                                    binding!!.etDriverPhone.isClickable = false
                                    binding!!.etDriverPhone.isFocusable = false
                                    binding!!.etOldDriverName.isEnabled = false
                                    binding!!.etOldDriverName.isClickable = false
                                    binding!!.etOldDriverName.isFocusable = false
                                }
                                .subscribe()


                    } else {
                        Utility.showAlertDialog(
                                this@UploadGatePassClass,
                                getString(R.string.alert),
                                getString(R.string.no_internet_connection)
                        )
                    }
                }
            }
        })
        binding!!.btnSubmit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (isValid) {
                    Utility.showDecisionDialog(
                            this@UploadGatePassClass,
                            getString(R.string.alert),
                            "Are You Sure to Summit?",
                            object : Utility.AlertCallback {
                                override fun callback() {
                                    if (InTrackType == null) {
                                        Toast.makeText(
                                                applicationContext,
                                                "क्या Inward में, ट्रक रस्सा एवं त्रिपाल होकर आया ?",
                                                Toast.LENGTH_LONG
                                        ).show()
                                    } else if (InBardhanaType == null) {
                                        Toast.makeText(
                                                applicationContext,
                                                "क्या Inward में, कुछ बोरिया भीगी हुई थी ?",
                                                Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        onNext()
                                    }
                                }
                            })
                }
            }
        })
        binding!!.uploadGatePass.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                GatePassFileSelect = true
                dispatchTakePictureIntent()
            }
        })
        binding!!.GatePassImage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                PhotoFullPopupWindow(
                        this@UploadGatePassClass,
                        R.layout.popup_photo_full,
                        view,
                        GatePassFile,
                        null
                )
            }
        })
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                val imageBitmap = data?.extras?.get("data") as Bitmap

                if (GatePassFileSelect) {
                    GatePassFileSelect = false
                    fileGatePass = File(compressImage(bitmapToFile(imageBitmap).toString()))
                    val uri = Uri.fromFile(fileGatePass)
                    GatePassFile = uri.toString()
                    binding!!.GatePassImage.setImageURI(uri)
                }

            }
        }
    }

    override fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var path = this.filesDir.absolutePath
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

    private fun callImageSelector(requestCamera: Int) {
        options = Options.init()
                .setRequestCode(requestCamera) //Request code for activity results
                .setCount(1)
                .setFrontfacing(false)
                .setExcludeVideos(false) //Option to exclude videos
                .setVideoDurationLimitinSeconds(30) //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientation
                .setPath("/apnagodam/lp/images") //Custom Path For media Storage
        Pix.start(this@UploadGatePassClass, options)
    }

    private val kanthaList: Unit
        private get() {
            apiService.dharmaKanthaListLevel.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { body ->
                        data = body.getData() as ArrayList<DharmaKanthaPostData.Datum>
                        for (i in data!!.indices) {
                            kanthaName!!.add(data!![i].name)
                        }
                    }
                    .subscribe()


        }

    // update file
    fun onNext() {
        val Weightkg = "" + Utility.round(
                stringFromView(
                        binding!!.etWeightKg
                ).toDouble(), 2
        )
        apiService.uploadGatePassNew(
                UploadGatePassPostDataNew(
                        CaseID,
                        Weightkg,
                        stringFromView(
                                binding!!.etBags
                        ),
                        stringFromView(binding!!.notes),
                        stringFromView(binding!!.etStackNo),
                        stringFromView(binding!!.etTransportotName),
                        stringFromView(
                                binding!!.etTranPhone
                        ),
                        stringFromView(binding!!.etAvgWeight),
                        stringFromView(binding!!.etKathaParchiNo),
                        InTrackID,
                        InBardhanaID,
                        stringFromView(binding!!.etOldKathaParchiNo),
                        stringFromView(
                                binding!!.etOldTotalWeight
                        ),
                        stringFromView(binding!!.etOldOriginalWeight),
                        stringFromView(binding!!.etOldKanthaName),
                        stringFromView(
                                binding!!.etOldDriverName
                        ),
                        stringFromView(binding!!.etDriverPhone),
                        kanthaID
                )
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { body ->
                    Utility.showAlertDialog(
                            this@UploadGatePassClass,
                            getString(R.string.alert),
                            body.getMessage(),
                            object : Utility.AlertCallback {
                                override fun callback() {
                                    startActivityAndClear(GatePassListingActivity::class.java)
                                }
                            })
                }
                .subscribe()


    }

    val isOTPValid: Boolean
        get() {
            if (TextUtils.isEmpty(stringFromView(binding!!.etOldDriverName))) {
                return Utility.showEditTextError(binding!!.tilOldDriverName, "Enter Driver Name")
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etDriverPhone))) {
                return Utility.showEditTextError(binding!!.tilDriverPhone, "Enter Driver Phone")
            }
            return true
        }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityAndClear(GatePassListingActivity::class.java)
    }

    val isValid: Boolean
        get() {
            /* if (TextUtils.isEmpty(stringFromView(binding.etStackNo))) {
            return Utility.showEditTextError(binding.tilStackNo, "Enter Stack No");
        } else */
            if (TextUtils.isEmpty(stringFromView(binding!!.etWeightKg))) {
                return Utility.showEditTextError(binding!!.tilWeightKg, R.string.weight_kg)
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etBags))) {
                return Utility.showEditTextError(binding!!.tilBags, R.string.bags_gatepass)
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etTransportotName))) {
                return Utility.showEditTextError(
                        binding!!.tilTransportotName,
                        "Enter Transporter Name"
                )
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etTranPhone))) {
                return Utility.showEditTextError(binding!!.tilTranPhone, "Enter Transporter Phone")
            } else if ((stringFromView(binding!!.etTranPhone)).length < 10 || (stringFromView(
                            binding!!.etTranPhone
                    )).length > 10
            ) {
                return Utility.showEditTextError(
                        binding!!.tilTranPhone,
                        "please Enter Valid Mobile Number"
                )
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etAvgWeight))) {
                return Utility.showEditTextError(
                        binding!!.tilAvgWeight,
                        "Enter Average Weight (KG)"
                )
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etKathaParchiNo))) {
                return Utility.showEditTextError(binding!!.tilKathaParchiNo, "Kanta Parchi No")
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etOldKathaParchiNo))) {
                return Utility.showEditTextError(
                        binding!!.tilOldKathaParchiNo,
                        "Enter Old Kanta Parchi"
                )
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etOldTotalWeight))) {
                return Utility.showEditTextError(
                        binding!!.tilOldTotalWeight,
                        "Enter Old Total Weight"
                )
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etOldOriginalWeight))) {
                return Utility.showEditTextError(
                        binding!!.tilOldOriginalWeight,
                        "Old Original Weight"
                )
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etOldKanthaName))) {
                return Utility.showEditTextError(binding!!.tilOldKanthaName, "Old Kanta Name")
            }
            return true
        }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

    override fun onCheckedChanged(radioGroup: RadioGroup, i: Int) {
        if (radioGroup.checkedRadioButtonId == R.id.radioSeller) {
            InTrackType = "Yes"
            InTrackID = "1"
        } else if (radioGroup.checkedRadioButtonId == R.id.radioBuyer) {
            InTrackType = "No"
            InTrackID = "0"
        } else if (radioGroup.checkedRadioButtonId == R.id.radioyes) {
            InBardhanaType = "Yes"
            InBardhanaID = "1"
        } else if (radioGroup.checkedRadioButtonId == R.id.radiono) {
            InBardhanaType = "No"
            InBardhanaID = "0"
        }
    }
}
