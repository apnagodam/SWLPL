package com.apnagodam.staff.activity.out.truckbook

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadTruckDetailsPostData
import com.apnagodam.staff.Network.viewmodel.TruckBookViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.`in`.truckbook.TransportType
import com.apnagodam.staff.databinding.ActivityUploadDetailsBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.TransporterListPojo
import com.apnagodam.staff.helper.ImageHelper
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import com.apnagodam.staff.utils.Validationhelper
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.google.android.gms.location.LocationServices
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import com.thorny.photoeasy.OnPictureReady
import com.thorny.photoeasy.PhotoEasy
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class OUTTruckUploadDetailsClass() : BaseActivity<ActivityUploadDetailsBinding?>(),
    View.OnClickListener, AdapterView.OnItemSelectedListener {
    var UserName: String? = null
    var CaseID: String? = ""
    private var calender: Calendar? = null
    var checked = false
    var spinnerRateType: String? = null
    var fileBiltyImage: File? = null
    var BiltyImageFile = false
    private var BiltyFile: String? = null
    var options: Options? = null
    var data: List<TransporterListPojo.Datum>? = null
    var TransporterName: MutableList<String> = arrayListOf()
    lateinit var spinnerTransporterAdpter: ArrayAdapter<String>
    var TransporterID: String? = null
    var transTypeList = arrayListOf<String>()
    val truckBookViewModel by viewModels<TruckBookViewModel>()
    var lat = 0.0
    var long = 0.0
    lateinit var transportTypeAdapter: ArrayAdapter<String>

    var transportType = TransportType.DEFAULT.type

    lateinit var photoEasy: PhotoEasy
    var currentLocation = ""
    lateinit var searchableSpinner: SearchableSpinner

    override fun getLayoutResId(): Int {
        return R.layout.activity_upload_details
    }

    override fun setUp() {
        searchableSpinner = SearchableSpinner(this)

        photoEasy = PhotoEasy.builder().setActivity(this)
            .build()
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
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

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }
        transTypeList.add(TransportType.DEFAULT.type)
        transTypeList.add(TransportType.CLIENT.type)
        transTypeList.add(TransportType.COMPANY.type)
        TransporterName.add("Select Transporter")
        calender = Calendar.getInstance()
        UserName = intent.getStringExtra("user_name")
        CaseID = intent.getStringExtra("case_id")
        transportTypeAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, transTypeList)
        transporterList()
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        clickListner()
        binding!!.customerName.text = UserName
        binding!!.caseId.text = CaseID
        binding!!.tilTransportRate.visibility = View.VISIBLE

        binding!!.spinnerRatetYpe.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    transportType = transTypeList.get(position)
                    if (transportType != TransportType.DEFAULT.type) {
                        spinnerRateType = parent.getItemAtPosition(position).toString()
                        binding!!.tilTransportRate.visibility = View.VISIBLE
                    } else {
                        binding!!.tilTransportRate.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // can leave this empty
                }
            }
        binding!!.spinnerTransportType.adapter =
            transportTypeAdapter
        binding!!.spinnerTransportType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {
                            binding!!.layoutTransport.visibility = View.GONE
                            binding!!.btnLogin.isEnabled = false
                        }

                        else -> {
                            if (position == 2) {
                                checked = false
                                NotChecked()
                                binding!!.layoutTransport.visibility = View.VISIBLE
                                binding!!.btnLogin.isEnabled = true

                            } else if (position == 1) {
                                checked = true
                                Checked()
                                binding!!.layoutTransport.visibility = View.GONE
                                binding!!.btnLogin.isEnabled = true
                            }


                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {


                }

            }
        binding!!.TruckImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this,
                R.layout.popup_photo_full,
                view,
                BiltyFile,
                null
            )
        }


    }

    private fun getTransporterDetails(transporterID: String) {
        truckBookViewModel.getTransporterDetails(transporterID)
        truckBookViewModel.transporterDetailsResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    if (it.data != null) {
                        when (it.data.data.vehicleNo) {
                            null -> binding!!.etVehicleNo.setText("N/A")
                            else -> binding!!.etVehicleNo.setText(it.data.data.vehicleNo)
                        }

                        binding!!.etDriverName.setText("" + it.data.data.transporterName)
                        binding!!.etDriverPhoneNo.setText("" + it.data.data.transporterPhoneNo)
                    }
                }
            }
        }


    }


    fun transporterList() {
        truckBookViewModel.transporterList()
        truckBookViewModel.transporterResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog()
                }

                is NetworkResult.Loading -> {
                    showDialog()
                }

                is NetworkResult.Success -> {
                    data = it.data!!.data
                    for (i in data!!.indices) {
                        if (!data!![i].transporterName.contains("Client Transport")) {
                            TransporterName!!.add(data!!.get(i).transporterName + "(" + data!!.get(i).transporterUniqueId + ")")

                        }
                    }
                    spinnerTransporterAdpter = ArrayAdapter(
                        this@OUTTruckUploadDetailsClass,
                        R.layout.support_simple_spinner_dropdown_item, TransporterName!!

                    )
                    searchableSpinner.windowTitle = "Select Transport Contractor"


                    searchableSpinner.setSpinnerListItems(TransporterName as ArrayList<String>)

                    searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
                        override fun setOnItemSelectListener(
                            position: Int,
                            selectedString: String
                        ) {
                            binding!!.etTransporterName.setText(selectedString)
                            if (data != null) {
                                val presentMeterStatusID = selectedString
                                for (i in data!!.indices) {
                                    if (presentMeterStatusID.contains(data!![i].transporterName + "(" + data!![i].transporterUniqueId + ")")) {
                                        TransporterID = data!![i].id.toString()
                                        break
                                    }
                                }
                                getTransporterDetails(TransporterID!!)
                            }
                            binding!!.spinnerTransporterName.setText(selectedString)
                        }


                    }
                    binding!!.spinnerTransporterName.setOnClickListener {
                        searchableSpinner.show()
                    }

                }
            }
        }

    }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener(this)
        binding!!.btnLogin.setOnClickListener(this)

        binding!!.uploadTruck.setOnClickListener {
            BiltyImageFile = true
            dispatchTakePictureIntent()
        }
        binding!!.checkNotRequried.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isChecked) {
                // checked
                Checked()
            } else {
                // not checked
                NotChecked()
            }
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        photoEasy.onActivityResult(1566, -1, object : OnPictureReady {
            override fun onFinish(thumbnail: Bitmap?) {

                if (thumbnail != null) {
                    val userDetails = SharedPreferencesRepository.getDataManagerInstance().user

                    var stampMap = mapOf(
                        "current_location" to "$currentLocation",
                        "emp_code" to userDetails.emp_id, "emp_name" to userDetails.fname
                    )
                    var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                        File(compressImage(bitmapToFile(thumbnail!!).path)),
                        stampMap
                    )
                    fileBiltyImage = File(compressImage(bitmapToFile(stampedBitmap).path))

                    val uri = Uri.fromFile(fileBiltyImage)
                    BiltyFile = uri.toString()
                    binding!!.TruckImage.setImageURI(uri)
                }
            }

        })
    }

    override fun dispatchTakePictureIntent() {
        val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            permissionsBuilder(Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION).build().send() {
                if(it.allGranted()){
                    photoEasy.startActivityForResult(this)
                }
                else{
                    Toast.makeText(
                        this,
                        "Location or Camera Permissions Denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        }
        else{
            Toast.makeText(
                this,
                "GPS Not Enabled",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

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
                    dispatchTakePictureIntent()
                    // callImageSelector(REQUEST_CAMERA)
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

    private fun Checked() {
        binding!!.layoutTransport.visibility = View.GONE
        /*  binding.etTransporterName.setEnabled(false);
        binding.etTransporterName.setClickable(false);
        binding.etTransporterName.setFocusable(false);
        binding.etTransporterName.setText("");*/binding!!.etTransporterName.setBackgroundColor(
            resources.getColor(R.color.lightgray)
        )
        binding!!.etVehicleNo.isEnabled = false
        binding!!.etVehicleNo.isClickable = false
        binding!!.etVehicleNo.isFocusable = false
        binding!!.etVehicleNo.isFocusableInTouchMode = false
        binding!!.etVehicleNo.setText("")
        binding!!.etVehicleNo.setBackgroundColor(resources.getColor(R.color.lightgray))
        binding!!.etDriverName.isEnabled = false
        binding!!.etDriverName.isClickable = false
        binding!!.etDriverName.isFocusable = false
        binding!!.etDriverName.setText("")
        binding!!.etDriverName.setBackgroundColor(resources.getColor(R.color.lightgray))
        binding!!.etDriverPhoneNo.isEnabled = false
        binding!!.etDriverPhoneNo.isClickable = false
        binding!!.etDriverPhoneNo.isFocusable = false
        binding!!.etDriverPhoneNo.setText("")
        binding!!.etDriverPhoneNo.setBackgroundColor(resources.getColor(R.color.lightgray))
        binding!!.etTransportRate.isEnabled = false
        binding!!.etTransportRate.isClickable = false
        binding!!.etTransportRate.isFocusable = false
        binding!!.etTransportRate.setText("")
        binding!!.etTransportRate.setBackgroundColor(resources.getColor(R.color.lightgray))
        /*binding!!.etMinWeight.isEnabled = false
        binding!!.etMinWeight.isClickable = false
        binding!!.etMinWeight.isFocusable = false
        binding!!.etMinWeight.setText("")
        binding!!.etMinWeight.setBackgroundColor(resources.getColor(R.color.lightgray))
        binding!!.etMaxWeight.isEnabled = false
        binding!!.etMaxWeight.isClickable = false
        binding!!.etMaxWeight.isFocusable = false
        binding!!.etMaxWeight.setText("")
        binding!!.etMaxWeight.setBackgroundColor(resources.getColor(R.color.lightgray))
        binding!!.etTurnaroundTime.isEnabled = false
        binding!!.etTurnaroundTime.isClickable = false
        binding!!.etTurnaroundTime.isFocusable = false
        binding!!.etTurnaroundTime.setText("")
        binding!!.etTurnaroundTime.setBackgroundColor(resources.getColor(R.color.lightgray))
        binding!!.etTurnaroundTime.isEnabled = false
        binding!!.etTotalWeight.isClickable = false
        binding!!.etTotalWeight.isFocusable = false
        binding!!.etTotalWeight.setText("")
        binding!!.etTotalWeight.setBackgroundColor(resources.getColor(R.color.lightgray))
        binding!!.etBags.isEnabled = false
        binding!!.etBags.isClickable = false
        binding!!.etBags.isFocusable = false
        binding!!.etBags.setText("")
        binding!!.etBags.setBackgroundColor(resources.getColor(R.color.lightgray))*/
//        binding!!.etTotalTransCost.isEnabled = false
//        binding!!.etTotalTransCost.isClickable = false
//        binding!!.etTotalTransCost.isFocusable = false
//        binding!!.etTotalTransCost.setText("")
//        binding!!.etTotalTransCost.setBackgroundColor(resources.getColor(R.color.lightgray))
        binding!!.etAdvancePatyment.isEnabled = false
        binding!!.etAdvancePatyment.isClickable = false
        binding!!.etAdvancePatyment.isFocusable = false
        binding!!.etAdvancePatyment.setText("")
        binding!!.etAdvancePatyment.setBackgroundColor(resources.getColor(R.color.lightgray))
//        binding!!.etFinalSettalementAmount.isEnabled = false
//        binding!!.etFinalSettalementAmount.isClickable = false
//        binding!!.etFinalSettalementAmount.isFocusable = false
//        binding!!.etFinalSettalementAmount.setText("")
//        binding!!.etFinalSettalementAmount.setBackgroundColor(resources.getColor(R.color.lightgray))
        /*

           binding!!.etStartDateTime.isEnabled = false
           binding!!.etStartDateTime.isClickable = false
           binding!!.etStartDateTime.isFocusable = false
           binding!!.etStartDateTime.setText("")
           binding!!.etStartDateTime.setBackgroundColor(resources.getColor(R.color.lightgray))
           binding!!.etEndDateTime.isEnabled = false
           binding!!.etEndDateTime.isClickable = false
           binding!!.etEndDateTime.isFocusable = false
           binding!!.etEndDateTime.setText("")
           binding!!.etEndDateTime.setBackgroundColor(resources.getColor(R.color.lightgray))
           binding!!.lpCommiteDate.isEnabled = false
           binding!!.lpCommiteDate.isClickable = false
           binding!!.lpCommiteDate.isFocusable = false
           binding!!.lpCommiteDate.setBackgroundColor(resources.getColor(R.color.lightgray))
           binding!!.lpEndDate.isEnabled = false
           binding!!.lpEndDate.isClickable = false
           binding!!.lpEndDate.isFocusable = false
           binding!!.lpEndDate.setBackgroundColor(resources.getColor(R.color.lightgray))*/
        binding!!.spinnerRatetYpe.isEnabled = false
        binding!!.spinnerRatetYpe.isClickable = false
        binding!!.uploadTruck.isEnabled = false
        binding!!.uploadTruck.isClickable = false
    }

    private fun NotChecked() {
        binding!!.layoutTransport.visibility = View.VISIBLE
        /*  binding.etTransporterName.setEnabled(true);
        binding.etTransporterName.setClickable(true);
        binding.etTransporterName.setFocusable(true);
        binding.etTransporterName.setFocusableInTouchMode(true);*/binding!!.uploadTruck.isEnabled =
            true
        binding!!.uploadTruck.isClickable = true
        binding!!.spinnerRatetYpe.isEnabled = true
        binding!!.spinnerRatetYpe.isClickable = true
        binding!!.etTransportRate.isEnabled = true
        binding!!.etTransportRate.isClickable = true
        binding!!.etTransportRate.isFocusable = true
        binding!!.etTransportRate.isFocusableInTouchMode = true
        /*binding!!.etMinWeight.isEnabled = true
        binding!!.etMinWeight.isClickable = true
        binding!!.etMinWeight.isFocusable = true
        binding!!.etMinWeight.isFocusableInTouchMode = true
        binding!!.etMaxWeight.isEnabled = true
        binding!!.etMaxWeight.isClickable = true
        binding!!.etMaxWeight.isFocusable = true
        binding!!.etMaxWeight.isFocusableInTouchMode = true
        binding!!.etTurnaroundTime.isEnabled = true
        binding!!.etTurnaroundTime.isClickable = true
        binding!!.etTurnaroundTime.isFocusable = true
        binding!!.etTurnaroundTime.isFocusableInTouchMode = true
        binding!!.etTotalWeight.isEnabled = true
        binding!!.etTotalWeight.isClickable = true
        binding!!.etTotalWeight.isFocusable = true
        binding!!.etTotalWeight.isFocusableInTouchMode = true
        binding!!.etBags.isEnabled = true
        binding!!.etBags.isClickable = true
        binding!!.etBags.isFocusable = true
        binding!!.etBags.isFocusableInTouchMode = true*/
//        binding!!.etTotalTransCost.isEnabled = true
//        binding!!.etTotalTransCost.isClickable = true
//        binding!!.etTotalTransCost.isFocusable = true
//        binding!!.etTotalTransCost.isFocusableInTouchMode = true
        binding!!.etAdvancePatyment.isEnabled = true
        binding!!.etAdvancePatyment.isClickable = true
        binding!!.etAdvancePatyment.isFocusable = true
        binding!!.etAdvancePatyment.isFocusableInTouchMode = true
//        binding!!.etFinalSettalementAmount.isEnabled = true
//        binding!!.etFinalSettalementAmount.isClickable = true
//        binding!!.etFinalSettalementAmount.isFocusable = true
//        binding!!.etFinalSettalementAmount.isFocusableInTouchMode = true
        /*
           binding!!.etStartDateTime.isEnabled = true
           binding!!.etStartDateTime.isClickable = true
           binding!!.etStartDateTime.isFocusable = true
           binding!!.etStartDateTime.isFocusableInTouchMode = true
           binding!!.etEndDateTime.isEnabled = true
           binding!!.etEndDateTime.isClickable = true
           binding!!.etEndDateTime.isFocusable = true
           binding!!.etEndDateTime.isFocusableInTouchMode = true
           binding!!.lpEndDate.isEnabled = true
           binding!!.lpEndDate.isClickable = true
           binding!!.lpEndDate.isFocusable = true
           binding!!.lpEndDate.isFocusableInTouchMode = true
           binding!!.lpCommiteDate.isEnabled = true
           binding!!.lpCommiteDate.isClickable = true
           binding!!.lpCommiteDate.isFocusable = true
           binding!!.lpCommiteDate.isFocusableInTouchMode = true*/
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_close -> {
                finish()
            }

            R.id.et_start_date_time -> popUpDatePicker()
            R.id.lp_commite_date -> popUpDatePicker()

            R.id.btn_login -> if (isValid) {
                callApi()

            }
        }
    }

    private fun callApi() {
        var BiltyImage = ""
        if (fileBiltyImage != null) {
            BiltyImage = "" + Utility.transferImageToBase64(fileBiltyImage)
        }
        truckBookViewModel.uploadTruckDetails(
            UploadTruckDetailsPostData(
                CaseID,
                TransporterID,
                stringFromView(binding!!.etVehicleNo),
                stringFromView(binding!!.etDriverName),
                stringFromView(binding!!.etDriverPhoneNo),
                "",
                " ",
                "",
                "",
                "",
                binding!!.etTransportRate.text.toString(),
                stringFromView(
                    binding!!.etAdvancePatyment
                ),
                "",
                "",
                " ",
                stringFromView(
                    binding!!.notes
                ),
                TransporterID,
                BiltyImage,
                spinnerRateType,
                stringFromView(
                    binding!!.etRealteCaseid,
                ), binding!!.etLocation.text.toString(),
                binding!!.etTransportRate.text.toString()
            )
        )
        truckBookViewModel.uploadTruckResponse.observe(this)
        {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    if (it.data != null) {
                        if (it.data.status == "1") {
                            finish()
                        } else
                            Toast.makeText(this,it.data.message,Toast.LENGTH_SHORT)
                    }
                }
            }
        }

    }

    val isValid: Boolean
        get() {
            if (checked) {
                TransporterID = "1"
                return true;
            } else {

                if (TextUtils.isEmpty(stringFromView(binding!!.etTransportRate))) {
                    return Utility.showEditTextError(
                        binding!!.tilTransportRate,
                        R.string.transport_rate_validation
                    )
                } else if (fileBiltyImage == null) {
                    Toast.makeText(this,"please select bilty image",Toast.LENGTH_SHORT)

                    return false;

                } else if (Validationhelper().fieldEmpty(binding!!.tilAdvancePatyment)) {
                    binding!!.tilAdvancePatyment.error = "This Field is Required"
                    return false;

                }
//                else if (Validationhelper().fieldEmpty(binding!!.tilFinalSettalementAmount))
//                {
//                    binding!!.tilFinalSettalementAmount.error = "This Field is Required"
//                }

                else if (Validationhelper().fieldEmpty(binding!!.tilLocation)) {
                    binding!!.tilLocation.error = "This Field is required"
                    return false;

                } else if (TextUtils.isEmpty(stringFromView(binding!!.etTransportRate))|| Integer.parseInt(
                        binding!!.etTransportRate.text.toString()
                    ) == 0
                ) {
                    binding!!.tilTransportRate.error = "This Field is required"
                    return false;

                } else if (TextUtils.isEmpty(stringFromView(binding!!.etAdvancePatyment))
                ) {
                    binding!!.tilAdvancePatyment.error = "This Field is required"
                    return false;

                } else if (TextUtils.isEmpty(stringFromView(binding!!.etLocation))) {
                    binding!!.tilLocation.error = "This Field is required"
                    return false;

                }


            }
            return true
        }

    fun popUpDatePicker() {
        val dateDialog = DatePickerDialog(
            this, date, calender
            !!.get(Calendar.YEAR), calender!![Calendar.MONTH],
            calender!![Calendar.DAY_OF_MONTH]
        )
        dateDialog.getDatePicker().setMinDate(System.currentTimeMillis())
        dateDialog.show()
    }

    var date: DatePickerDialog.OnDateSetListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            calender!![Calendar.YEAR] = year
            calender!![Calendar.MONTH] = monthOfYear
            calender!![Calendar.DAY_OF_MONTH] = dayOfMonth
            val myFormat = "dd-MMM-yyyy" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            //   binding!!.etStartDateTime.setText(sdf.format(calender!!.time).toString())
            StarttimePicker(sdf.format(calender!!.time).toString())
        }
    }

    private fun StarttimePicker(date: String) {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(timePicker: TimePicker, selectedHour: Int, selectedMinute: Int) {
                //    binding!!.etStartDateTime.setText("$date - $selectedHour:$selectedMinute")
            }
        }, hour, minute, true) //Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    fun EnddatePicker() {
        val dateDialog = DatePickerDialog(
            this, dateeend, calender
            !!.get(Calendar.YEAR), calender!![Calendar.MONTH],
            calender!![Calendar.DAY_OF_MONTH]
        )
        dateDialog.getDatePicker().setMinDate(System.currentTimeMillis())
        dateDialog.show()
    }

    var dateeend: DatePickerDialog.OnDateSetListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            calender!![Calendar.YEAR] = year
            calender!![Calendar.MONTH] = monthOfYear
            calender!![Calendar.DAY_OF_MONTH] = dayOfMonth
            val myFormat = "dd-MMM-yyyy" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            //   binding!!.etEndDateTime.setText(sdf.format(calender!!.time).toString())
            EndtimePicker(sdf.format(calender!!.time).toString())
        }
    }

    private fun EndtimePicker(date: String) {
        Log.d("Al JSon Data", "" + date)
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(timePicker: TimePicker, selectedHour: Int, selectedMinute: Int) {
                //     binding!!.etEndDateTime.setText("$date - $selectedHour:$selectedMinute")
            }
        }, hour, minute, true) //Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {}
    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
}
