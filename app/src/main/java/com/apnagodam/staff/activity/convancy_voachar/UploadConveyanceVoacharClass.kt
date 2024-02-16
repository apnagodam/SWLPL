package com.apnagodam.staff.activity.convancy_voachar

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.Request.CreateConveyancePostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityEmpConveyanceBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.AllLevelEmpListPojo
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UploadConveyanceVoacharClass : BaseActivity<ActivityEmpConveyanceBinding?>() {
    var packagingTypeID: String? = null

    // role of image
    var UserName: String? = null
    var CaseID = ""
    var fileReport: File? = null
    var fileCommudity: File? = null
    var FileOther: File? = null
    var ReportsFileSelect = false
    var CommudityFileSelect = false
    var OtherFileSelect = false
    private var reportFile: String? = null
    private var commudityFile: String? = null
    private var OtherFile: String=""
    private lateinit var calender: Calendar

    // approved for
    var approveName: ArrayList<String> = arrayListOf()
    var approveID: ArrayList<String> = arrayListOf()
    lateinit var SpinnerApproveByAdapter: ArrayAdapter<String>
    var SelectedApproveIDIs: String? = null

    // Terminal Name
    lateinit var TerminalName: ArrayList<String>
    var TerminalID: ArrayList<String> = arrayListOf()
    lateinit var SpinnerTerminalAdapter: ArrayAdapter<String>
    var SelectedTerminalIDIs: String? = null
    override fun getLayoutResId(): Int {
        return R.layout.activity_emp_conveyance
    }

    override fun setUp() {
        calender = Calendar.getInstance()
        // approved for
        approveName = ArrayList()
        approveID = ArrayList()
        approveName.add(resources.getString(R.string.approved_by))
        approveID.add("0")
        // terminal for
        TerminalName = ArrayList()
        TerminalID = ArrayList()
        TerminalName.add(resources.getString(R.string.terminal_name))
        TerminalID.add("0")
        binding!!.tvDone.visibility = View.GONE
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        // get approve person  list
        apiService.getlevelwiselist()

            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {body->
                for (i in body.data.indices) {
                    if (body.request_count > 0) {
                        binding!!.tvDone.isClickable = true
                        binding!!.tvDone.isEnabled = true
                        binding!!.tvDone.text = "Approval Request " + "(" + body.request_count + ")"
                    }
                    approveID.add(body.data[i].userId)
                    approveName.add(body.data[i].firstName + " " + body.data[i].lastName + "(" + body.data[i].empId + ")")
                }
                for (i in body.warehouse_name.indices) {
                    TerminalID.add(body.warehouse_name[i].id)
                    TerminalName.add(body.warehouse_name[i].name + "(" + body.warehouse_name[i].warehouse_code + ")")
                }
            }
            .subscribe()

        clickListner()
        binding!!.tilStartReading.visibility = View.GONE
        binding!!.tilEndReading.visibility = View.GONE
        binding!!.tilKms.visibility = View.GONE
        binding!!.tilCharges.visibility = View.GONE
        binding!!.tilOtherExpense.visibility = View.GONE
        binding!!.etEndReading.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (binding!!.etStartReading.text.toString()
                        .trim { it <= ' ' } != null && !binding!!.etStartReading.text.toString()
                        .trim { it <= ' ' }
                        .isEmpty() && !binding!!.etEndReading.text.toString().trim { it <= ' ' }
                        .isEmpty()
                ) {
                    val startReadingLenth =
                        binding!!.etStartReading.text.toString().trim { it <= ' ' }
                            .toDouble()
                    val endReadingLenth = binding!!.etEndReading.text.toString().trim { it <= ' ' }
                        .toDouble()
                    if (endReadingLenth > startReadingLenth) {
                        finalAmountCalculation("End")
                    } else {
                        Utility.showAlertDialog(
                            this@UploadConveyanceVoacharClass,
                            getString(R.string.alert),
                            "End Reading Must be Greater than Start reading"
                        ) {
                            binding!!.etEndReading.setText("")
                            binding!!.etTotal.setText("")
                        }
                    }
                }
                // Toast.makeText(MainActivity.this, "focus loosed", Toast.LENGTH_LONG).show();
            } else {
                /// Toast.makeText(MainActivity.this, "focused", Toast.LENGTH_LONG).show();
            }
        }
        binding!!.etStartReading.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (binding!!.etEndReading.text.toString()
                        .trim { it <= ' ' } != null && !binding!!.etEndReading.text.toString()
                        .trim { it <= ' ' }
                        .isEmpty() && !binding!!.etStartReading.text.toString().trim { it <= ' ' }
                        .isEmpty()
                ) {
                    val startReadingLenth =
                        binding!!.etStartReading.text.toString().trim { it <= ' ' }
                            .toDouble()
                    val endReadingLenth = binding!!.etEndReading.text.toString().trim { it <= ' ' }
                        .toDouble()
                    if (endReadingLenth > startReadingLenth) {
                        finalAmountCalculation("End")
                    } else {
                        Utility.showAlertDialog(
                            this@UploadConveyanceVoacharClass,
                            getString(R.string.alert),
                            "Start Reading Must be less than End reading"
                        ) {
                            binding!!.etStartReading.setText("")
                            binding!!.etTotal.setText("")
                        }
                    }
                }
            } else {
                /// Toast.makeText(MainActivity.this, "focused", Toast.LENGTH_LONG).show();
            }
        }
        // spinner Conveyance type
        binding!!.etEndReading.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                /* if (charSequence.length() != 0 && !charSequence.equals("")) {
                    finalAmountCalculation("End");
                } else {
                    Log.d("printchecxk", "For Testing Now");
                }*/
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.isNotEmpty()) {
                    finalAmountCalculation("End")
                } else {
                    binding!!.etKms.setText("")
                    Log.d("printchecxk", "For Testing NowEnd")
                }
            }
        })
        binding!!.etStartReading.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length != 0 && charSequence != "") {
                    finalAmountCalculation("Start")
                } else {
                    binding!!.etKms.setText("")
                    Log.d("printchecxk", "For Testing Now..")
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        binding!!.etOtherExpense.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length != 0 && charSequence != "") {
                    finalAmountExpensionCalculation()
                } else {
                    binding!!.etTotal.setText("0")
                    finalAmountCalculation("")
                    Log.d("printchecxk", "For Testing Now")
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        binding!!.spinnerConvType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) {
                        packagingTypeID = parent.getItemAtPosition(position).toString()
                        binding!!.tilStartReading.visibility = View.VISIBLE
                        binding!!.tilEndReading.visibility = View.VISIBLE
                        binding!!.tilKms.visibility = View.VISIBLE
                        binding!!.tilCharges.visibility = View.VISIBLE
                        binding!!.tilOtherExpense.visibility = View.VISIBLE
                        val userDetails = SharedPreferencesRepository.getDataManagerInstance().user
                        if (packagingTypeID.equals("Two-wheeler", ignoreCase = true)) {
                            if (userDetails.two_wheeler_rate != null) {
                                binding!!.etCharges.setText(userDetails.two_wheeler_rate)
                            } else {
                                binding!!.etCharges.setText("0")
                            }
                            finalAmountCalculation("")
                        } else {
                            if (userDetails.two_wheeler_rate != null) {
                                binding!!.etCharges.setText(userDetails.four_wheeler_rate)
                            } else {
                                binding!!.etCharges.setText("0")
                            }
                            finalAmountCalculation("")
                        }
                        if (position == 3) {
                            binding!!.tilStartReading.visibility = View.GONE
                            binding!!.tilEndReading.visibility = View.GONE
                            binding!!.tilKms.visibility = View.GONE
                            binding!!.tilCharges.visibility = View.GONE
                            binding!!.tilOtherExpense.visibility = View.VISIBLE
                            binding!!.etStartReading.setText("")
                            binding!!.etEndReading.setText("")
                            binding!!.etKms.setText("")
                            binding!!.etCharges.setText("")
                            binding!!.etTotal.setText("")
                            if (!binding!!.etOtherExpense.text.toString().trim { it <= ' ' }
                                    .isEmpty() && binding!!.etOtherExpense.text.toString()
                                    .trim { it <= ' ' } != null && binding!!.etOtherExpense.text.toString()
                                    .trim { it <= ' ' }
                                    .toDouble() > 0) {
                                finalAmountExpensionCalculation()
                            } else {
                                binding!!.etTotal.setText("0")
                                finalAmountCalculation("")
                            }
                        }
                    } else {
                        binding!!.tilStartReading.visibility = View.GONE
                        binding!!.tilEndReading.visibility = View.GONE
                        binding!!.tilKms.visibility = View.GONE
                        binding!!.tilCharges.visibility = View.GONE
                        binding!!.tilOtherExpense.visibility = View.GONE
                        binding!!.etStartReading.setText("")
                        binding!!.etEndReading.setText("")
                        binding!!.etKms.setText("")
                        binding!!.etCharges.setText("")
                        binding!!.etTotal.setText("")
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // can leave this empty
                }
            }

        // Approved listing
        SpinnerApproveByAdapter = ArrayAdapter(this, R.layout.multiline_spinner_item, approveName)

        SpinnerApproveByAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)
        // Set Adapter in the spinner
        binding!!.spinnerApprovedBy.adapter = SpinnerApproveByAdapter
        binding!!.spinnerApprovedBy.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    // selected item in the list
                    if (position != 0) {
                        val EmpID = parentView.getItemAtPosition(position).toString()
                        for (i in approveName.indices) {
                            if (EmpID.equals(approveName.get(position), ignoreCase = true)) {
                                SelectedApproveIDIs = approveID.get(position)
                                break
                            }
                        }
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }

        // Terminal listing
        SpinnerTerminalAdapter = ArrayAdapter(this, R.layout.multiline_spinner_item, TerminalName)

        SpinnerTerminalAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)
        // Set Adapter in the spinner
        binding!!.spinnerTerminal.adapter = SpinnerTerminalAdapter
        binding!!.spinnerTerminal.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    // selected item in the list
                    if (position != 0) {
                        val EmpID = parentView.getItemAtPosition(position).toString()
                        for (i in TerminalName.indices) {
                            if (EmpID.equals(TerminalName.get(position), ignoreCase = true)) {
                                SelectedTerminalIDIs = TerminalID.get(position)
                                break
                            }
                        }
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }
    }

    fun finalAmountCalculation(action: String?) {
        try {
            if (binding!!.etStartReading.text.toString() != "" && binding!!.etEndReading.text.toString() != "" && binding!!.etStartReading.text.toString()
                    .toDouble() > 0 && binding!!.etEndReading.text.toString().toDouble() > 0
            ) {
                val startNormal = binding!!.etStartReading.text.toString().toDouble()
                val endActual = binding!!.etEndReading.text.toString().toDouble()
                if (endActual > startNormal) {
                    val actualReading = endActual - startNormal
                    binding!!.etKms.setText("" + actualReading)
                    var totalamount = 0.0
                    totalamount =
                        if (binding!!.etOtherExpense.text.toString() != null && !binding!!.etOtherExpense.text.toString()
                                .isEmpty()
                        ) {
                            binding!!.etKms.text.toString()
                                .toDouble() * binding!!.etCharges.text.toString()
                                .toDouble() + binding!!.etOtherExpense.text.toString()
                                .toDouble()
                        } else {
                            binding!!.etKms.text.toString()
                                .toDouble() * binding!!.etCharges.text.toString().toDouble()
                        }
                    val roundedNumber = Utility.round(totalamount, 2)
                    binding!!.etTotal.setText("" + roundedNumber)
                } else {
                    binding!!.etKms.setText("")

                    /*if (action.equalsIgnoreCase("End")) {
                        binding.etEndReading.setText("0");
                        Toast.makeText(UploadConveyanceVoacharClass.this, "End Reading Must be Greater than Start reading", Toast.LENGTH_SHORT).show();
                    } else if (action.equalsIgnoreCase("Start")) {
                        binding.etStartReading.setText("0");
                        Toast.makeText(UploadConveyanceVoacharClass.this, "Start Reading Must be less than End reading", Toast.LENGTH_SHORT).show();
                    }*/
                }
            } else {
                // binding.etEndReading.setText("0.0");
            }
        } catch (e: Exception) {
            Log.e("getNotification", e.toString() + "")
        }
    }

    fun finalAmountExpensionCalculation() {
        try {
            if (binding!!.etOtherExpense.text.toString() != "" && !binding!!.etOtherExpense.text.toString()
                    .isEmpty()
            ) {
                val expansionAmount = binding!!.etOtherExpense.text.toString().toDouble()
                if (binding!!.etTotal.text.toString() != null && !binding!!.etTotal.text.toString()
                        .isEmpty()
                ) {
                    val totalAmount = binding!!.etTotal.text.toString().toDouble()
                    val roundedNumber = Utility.round(expansionAmount, 2)
                    binding!!.etTotal.setText("" + roundedNumber)
                } else {
                    val roundedNumber = Utility.round(expansionAmount, 2)
                    binding!!.etTotal.setText("" + roundedNumber)
                }
            } else {
                binding!!.etOtherExpense.setText("")
                // binding.etEndReading.setText("0.0");
            }
            finalAmountCalculation("")
        } catch (e: Exception) {
            Log.e("getNotification", e.toString() + "")
        }
    }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener { startActivityAndClear(MyConveyanceListClass::class.java) }
        binding!!.tvDone.setOnClickListener {
            startActivityAndClear(
                ApprovalRequestConveyanceListClass::class.java
            )
        }
        binding!!.btnSubmit.setOnClickListener {
            Utility.showDecisionDialog(
                this@UploadConveyanceVoacharClass,
                getString(R.string.alert),
                "Are You Sure to Summit?",
                object : Utility.AlertCallback {
                    override fun callback() {
                        if (isValid) {
                            if (TextUtils.isEmpty(stringFromView(binding!!.userCommitmentDate))) {
                                Toast.makeText(
                                    this@UploadConveyanceVoacharClass,
                                    "Select Date",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (packagingTypeID == null) {
                                Toast.makeText(
                                    applicationContext,
                                    resources.getString(R.string.slect_conv_type),
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (fileReport == null) {
                                Toast.makeText(
                                    applicationContext,
                                    R.string.start_meter_image,
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (fileCommudity == null) {
                                Toast.makeText(
                                    applicationContext,
                                    R.string.end_meter_image,
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (SelectedApproveIDIs == null) {
                                Toast.makeText(
                                    applicationContext,
                                    resources.getString(R.string.approved_by),
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (SelectedTerminalIDIs == null) {
                                Toast.makeText(
                                    applicationContext,
                                    resources.getString(R.string.terminal_name),
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (packagingTypeID.equals(
                                    "Two-wheeler",
                                    ignoreCase = true
                                ) || packagingTypeID.equals("Four-wheeler", ignoreCase = true)
                            ) {
                                if (TextUtils.isEmpty(stringFromView(binding!!.etStartReading))) {
                                    Toast.makeText(
                                        this@UploadConveyanceVoacharClass,
                                        "Enter Start meter Reading ",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else if (TextUtils.isEmpty(stringFromView(binding!!.etEndReading))) {
                                    Toast.makeText(
                                        this@UploadConveyanceVoacharClass,
                                        "Enter End meter Reading",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    onNext()
                                }
                            } else {
                                onNext()
                            }
                        }
                    }
                })
        }
        binding!!.uploadReport.setOnClickListener {
            ReportsFileSelect = true
            CommudityFileSelect = false
            OtherFileSelect = false
            callImageSelector()
        }
        binding!!.uploadOther.setOnClickListener {
            ReportsFileSelect = false
            CommudityFileSelect = false
            OtherFileSelect = true
            callImageSelector()
        }
        binding!!.uploadCommudity.setOnClickListener {
            ReportsFileSelect = false
            CommudityFileSelect = true
            OtherFileSelect = false
            callImageSelector()
        }
        binding!!.OtherImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@UploadConveyanceVoacharClass,
                R.layout.popup_photo_full,
                view,
                OtherFile,
                null
            )
        }
        binding!!.ReportsImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@UploadConveyanceVoacharClass,
                R.layout.popup_photo_full,
                view,
                reportFile,
                null
            )
        }
        binding!!.CommudityImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@UploadConveyanceVoacharClass,
                R.layout.popup_photo_full,
                view,
                commudityFile,
                null
            )
        }
        binding!!.lpCommiteDate.setOnClickListener { popUpDatePicker() }
        binding!!.userCommitmentDate.setOnClickListener { popUpDatePicker() }
    }

    fun popUpDatePicker() {
        calender = Calendar.getInstance()
        val dateDialog = DatePickerDialog(
            this@UploadConveyanceVoacharClass, date, calender
                .get(Calendar.YEAR), calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        calender.add(Calendar.DATE, -1) // subtract 1 day from Today
        dateDialog.datePicker.minDate = calender.getTimeInMillis()
        dateDialog.datePicker.maxDate = System.currentTimeMillis()
        dateDialog.show()
    }

    var date = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        calender!![Calendar.YEAR] = year
        calender!![Calendar.MONTH] = monthOfYear
        calender!![Calendar.DAY_OF_MONTH] = dayOfMonth
        val myFormat = "dd-MM-yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding!!.userCommitmentDate.setText(sdf.format(calender!!.time).toString())
    }

    private fun callImageSelector() {
        val options = Options.init()
            .setRequestCode(100) //Request code for activity results
            .setCount(1) //Number of images to restrict selection count
            .setFrontfacing(false) //Front Facing camera on start
            .setExcludeVideos(true) //Option to exclude videos
            .setVideoDurationLimitinSeconds(30) //Duration for video recording
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientation
            .setPath("/apnagodam/lp/images") //Custom Path For media Storage
        Pix.start(this@UploadConveyanceVoacharClass, options.setRequestCode(REQUEST_CAMERA_PICTURE))
    }

    // update file
    fun onNext() {
        var KanthaImage = ""
        var CommudityFileSelectImage = ""
        var otherFileImage = ""
        if (fileReport != null) {
            KanthaImage = "" + Utility.transferImageToBase64(fileReport)
        }
        if (fileCommudity != null) {
            CommudityFileSelectImage = "" + Utility.transferImageToBase64(fileCommudity)
        }
        if (FileOther != null) {
            otherFileImage = "" + Utility.transferImageToBase64(FileOther)
        }
        apiService.doCreateConveyance(
            CreateConveyancePostData(
                stringFromView(binding!!.userCommitmentDate),
                KanthaImage,
                CommudityFileSelectImage,
                otherFileImage,
                stringFromView(binding!!.etVehicleNo),
                stringFromView(binding!!.etFrom),
                stringFromView(
                    binding!!.etTo
                ),
                stringFromView(binding!!.etStartReading),
                stringFromView(binding!!.etEndReading),
                stringFromView(binding!!.etKms),
                stringFromView(
                    binding!!.etCharges
                ),
                stringFromView(binding!!.etLocation),
                stringFromView(binding!!.etOtherExpense),
                stringFromView(binding!!.etTotal),
                stringFromView(
                    binding!!.notes
                ),
                SelectedApproveIDIs,
                packagingTypeID,
                SelectedTerminalIDIs
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {body->
                Utility.showAlertDialog(
                    this@UploadConveyanceVoacharClass,
                    getString(R.string.alert),
                    body.message
                ) { startActivityAndClear(MyConveyanceListClass::class.java) }
            }.subscribe()

    }

    val isValid: Boolean
        get() {
            if (TextUtils.isEmpty(stringFromView(binding!!.etTotal))) {
                return Utility.showEditTextError(binding!!.tilTotal, R.string.enter_total)
            } else if (TextUtils.isEmpty(stringFromView(binding!!.notes))) {
                return Utility.showEditTextError(binding!!.notes, R.string.purpose)
            }
            if (TextUtils.isEmpty(stringFromView(binding!!.etVehicleNo))) {
                return Utility.showEditTextError(binding!!.tilVehicleNo, R.string.vehicle_no_conv)
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etFrom))) {
                return Utility.showEditTextError(binding!!.tilFrom, R.string.from_place_con_list)
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etTo))) {
                return Utility.showEditTextError(binding!!.tilTo, R.string.to_place_con_list)
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etLocation))) {
                return Utility.showEditTextError(binding!!.tilLocation, R.string.enter_location)
            }
            return true
        }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 2) {
            val returnValue = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)!!
            Log.e("getImageesValue", returnValue[0].toString())
            if (requestCode == REQUEST_CAMERA_PICTURE) {
                if (ReportsFileSelect) {
                    ReportsFileSelect = false
                    CommudityFileSelect = false
                    OtherFileSelect = false
                    fileReport = File(compressImage(returnValue[0].toString()))
                    val uri = Uri.fromFile(fileReport)
                    reportFile = uri.toString()
                    binding!!.ReportsImage.setImageURI(uri)
                } else if (CommudityFileSelect) {
                    ReportsFileSelect = false
                    CommudityFileSelect = false
                    OtherFileSelect = false
                    fileCommudity = File(compressImage(returnValue[0].toString()))
                    val uri = Uri.fromFile(fileCommudity)
                    commudityFile = uri.toString()
                    binding!!.CommudityImage.setImageURI(uri)
                } else if (OtherFileSelect) {
                    ReportsFileSelect = false
                    CommudityFileSelect = false
                    OtherFileSelect = false
                    FileOther = File(compressImage(returnValue[0].toString()))
                    val uri = Uri.fromFile(FileOther)
                    OtherFile = uri.toString()
                    binding!!.OtherImage.setImageURI(uri)
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
                    Pix.start(this, Options.init().setRequestCode(100))
                } else {
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
