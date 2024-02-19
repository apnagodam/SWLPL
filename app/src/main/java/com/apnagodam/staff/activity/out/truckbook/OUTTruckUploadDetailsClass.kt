package com.apnagodam.staff.activity.out.truckbook

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadTruckDetailsPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.viewmodel.TruckBookViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityUploadDetailsBinding
import com.apnagodam.staff.module.TransporterDetailsPojo
import com.apnagodam.staff.module.TransporterListPojo
import com.apnagodam.staff.utils.Utility
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    lateinit var TransporterName: java.util.ArrayList<String>
    var spinnerTransporterAdpter: ArrayAdapter<String?>? = null
    var TransporterID: String? = null
    val truckBookViewModel by viewModels<TruckBookViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_upload_details
    }

    override fun setUp() {

        TransporterName = arrayListOf()
        (TransporterName).add("Select")
        transporterList
        calender = Calendar.getInstance()
        val bundle = intent.getBundleExtra(BUNDLE)
        if (bundle != null) {
            UserName = bundle.getString("user_name")
            //  vehhicleNO = bundle.getString("vehicle_no");
            CaseID = bundle.getString("case_id")
        }
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        clickListner()
        binding!!.customerName.text = UserName
        binding!!.caseId.text = CaseID






        binding!!.spinnerRatetYpe.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) {
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
        binding!!.spinnerTransporterName.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    // selected item in the list
                    if (position != 0) {
                        val presentMeterStatusID = parentView.getItemAtPosition(position).toString()
                        for (i in data!!.indices) {
                            if (presentMeterStatusID.contains(data!![i].transporterName + "(" + data!![i].transporterUniqueId + ")")) {
                                TransporterID = data!![i].id.toString()
                                break
                            }
                        }
                        getTransporterDetails(TransporterID)
                        if (presentMeterStatusID.contains("Client")) {
                            Checked()
                        } else {
                            NotChecked()
                        }
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }
        /* binding.etAdvancePatyment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etAdvancePatyment.getText().toString().trim() != null && !binding.etAdvancePatyment.getText().toString().trim().isEmpty() && !binding.etMaxWeight.getText().toString().trim().isEmpty() && !binding.etTransportRate.getText().toString().trim().isEmpty()) {
                        Double etAdvancePatyment = (Double.parseDouble(binding.etAdvancePatyment.getText().toString().trim()));
                        Double maxWeight = (Double.parseDouble(binding.etMaxWeight.getText().toString().trim()));
                        Double TransportRate = (Double.parseDouble(binding.etTransportRate.getText().toString().trim()));
                        Double Advancement = maxWeight * TransportRate;
                        if (etAdvancePatyment > Advancement) {
                            Utility.showAlertDialog(OUTTruckUploadDetailsClass.this, getString(R.string.alert), "Advance Payment  Must be Less than or equal to " + Advancement + " RS", new Utility.AlertCallback() {
                                @Override
                                public void callback() {
                                    binding.etAdvancePatyment.setText("");
                                }
                            });
                        } else {
                            binding.etAdvancePatyment.setText("" + Advancement);
                        }
                    }
                } else {
                }
            }
        });*/
        /*  binding.etMaxWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etAdvancePatyment.getText().toString().trim() != null && !binding.etAdvancePatyment.getText().toString().trim().isEmpty() && !binding.etMaxWeight.getText().toString().trim().isEmpty() && !binding.etTransportRate.getText().toString().trim().isEmpty()) {
                        Double etAdvancePatyment = (Double.parseDouble(binding.etAdvancePatyment.getText().toString().trim()));
                        Double maxWeight = (Double.parseDouble(binding.etMaxWeight.getText().toString().trim()));
                        Double TransportRate = (Double.parseDouble(binding.etTransportRate.getText().toString().trim()));
                        Double Advancement = maxWeight * TransportRate;
                        if (etAdvancePatyment > Advancement) {
                            Utility.showAlertDialog(OUTTruckUploadDetailsClass.this, getString(R.string.alert), "Advance Payment  Must be Less than or equal to " + Advancement + " RS", new Utility.AlertCallback() {
                                @Override
                                public void callback() {
                                    binding.etAdvancePatyment.setText("");
                                }
                            });
                        } else {
                            binding.etAdvancePatyment.setText("" + Advancement);
                        }
                    }
                } else {
                }
            }
        });*/
        /* binding.etTransportRate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etAdvancePatyment.getText().toString().trim() != null && !binding.etAdvancePatyment.getText().toString().trim().isEmpty() && !binding.etMaxWeight.getText().toString().trim().isEmpty() && !binding.etTransportRate.getText().toString().trim().isEmpty()) {
                        Double etAdvancePatyment = (Double.parseDouble(binding.etAdvancePatyment.getText().toString().trim()));
                        Double maxWeight = (Double.parseDouble(binding.etMaxWeight.getText().toString().trim()));
                        Double TransportRate = (Double.parseDouble(binding.etTransportRate.getText().toString().trim()));
                        Double Advancement = maxWeight * TransportRate;
                        if (etAdvancePatyment > Advancement) {
                            Utility.showAlertDialog(OUTTruckUploadDetailsClass.this, getString(R.string.alert), "Advance Payment  Must be Less than or equal to " + Advancement + " RS", new Utility.AlertCallback() {
                                @Override
                                public void callback() {
                                    binding.etAdvancePatyment.setText("");
                                }
                            });
                        } else {
                            // binding.etAdvancePatyment.setText("" + Advancement);
                        }
                    }
                } else {
                }
            }
        });*/
    }

    private fun getTransporterDetails(transporterID: String?) {
        truckBookViewModel.getTransporterDetails(transporterID!!)
        truckBookViewModel.transporterDetailsResponse.observe(this){
            when(it){
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                 if(it.data!=null){
                     binding!!.etVehicleNo.setText("" + it.data.data.vehicleNo)
                     binding!!.etDriverName.setText("" + it.data.data.transporterName)
                     binding!!.etDriverPhoneNo.setText("" + it.data.data.transporterPhoneNo)
                 }
                }
            }
        }



    }

    private val transporterList: Unit
        private get() {
            truckBookViewModel.transporterList()
            truckBookViewModel.transporterResponse.observe(this) {
                when (it) {
                    is NetworkResult.Error -> TODO()
                    is NetworkResult.Loading -> TODO()
                    is NetworkResult.Success -> {
                        for (i in it.data!!.data!!.indices) {
                            TransporterName!!.add(
                                it.data!!.data!!.get(i).transporterName + "(" + it.data!!.data!!.get(
                                    i
                                ).transporterUniqueId + ")"
                            )
                        }
                        spinnerTransporterAdpter = object : ArrayAdapter<String?>(
                            this@OUTTruckUploadDetailsClass,
                            R.layout.multiline_spinner_dropdown_item,
                            TransporterName!! as List<String?>
                        ) {
                            //By using this method we will define how
                            // the text appears before clicking a spinner
                            override fun getView(
                                position: Int,
                                convertView: View?,
                                parent: ViewGroup
                            ): View {
                                val v = super.getView(position, convertView, parent)
                                (v as TextView).setTextColor(Color.parseColor("#000000"))
                                return v
                            }

                            //By using this method we will define
                            //how the listview appears after clicking a spinner
                            override fun getDropDownView(
                                position: Int,
                                convertView: View,
                                parent: ViewGroup
                            ): View {
                                val v = super.getDropDownView(position, convertView, parent)
                                v.setBackgroundColor(Color.parseColor("#05000000"))
                                (v as TextView).setTextColor(Color.parseColor("#000000"))
                                return v
                            }
                        }

                        // Set Adapter in the spinner
                        binding!!.spinnerTransporterName.adapter = spinnerTransporterAdpter
                    }
                }
            }


        }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener(this)
        binding!!.btnLogin.setOnClickListener(this)
        binding!!.etStartDateTime.setOnClickListener(this)
        binding!!.lpEndDate.setOnClickListener(this)
        binding!!.etEndDateTime.setOnClickListener(this)
        binding!!.uploadTruck.setOnClickListener {
            BiltyImageFile = true
            callImageSelector(REQUEST_CAMERA)
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

    private fun callImageSelector(requestCamera: Int) {
        options = Options.init()
            .setRequestCode(requestCamera) //Request code for activity results
            .setCount(1) //Number of images to restict selection count
            .setFrontfacing(false) //Front Facing camera on start
            .setExcludeVideos(false) //Option to exclude videos
            .setVideoDurationLimitinSeconds(30) //Option to exclude videos
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientaion
            .setPath("/apnagodam/lp/images") //Custom Path For media Storage
        Pix.start(this@OUTTruckUploadDetailsClass, options)
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

    private fun Checked() {

        checked = true
        /* binding.etTransporterName.setEnabled(false);
        binding.etTransporterName.setClickable(false);
        binding.etTransporterName.setFocusable(false);
        binding.etTransporterName.setText("");*/binding!!.etTransporterName.setBackgroundColor(
            resources.getColor(R.color.lightgray)
        )
        binding!!.etVehicleNo.isEnabled = false
        binding!!.etVehicleNo.isClickable = false
        binding!!.etVehicleNo.isFocusable = false
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
        binding!!.etMinWeight.isEnabled = false
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
        binding!!.etBags.setBackgroundColor(resources.getColor(R.color.lightgray))
        binding!!.etTotalTransCost.isEnabled = false
        binding!!.etTotalTransCost.isClickable = false
        binding!!.etTotalTransCost.isFocusable = false
        binding!!.etTotalTransCost.setText("")
        binding!!.etTotalTransCost.setBackgroundColor(resources.getColor(R.color.lightgray))
        binding!!.etAdvancePatyment.isEnabled = false
        binding!!.etAdvancePatyment.isClickable = false
        binding!!.etAdvancePatyment.isFocusable = false
        binding!!.etAdvancePatyment.setText("")
        binding!!.etAdvancePatyment.setBackgroundColor(resources.getColor(R.color.lightgray))
        binding!!.etFinalSettalementAmount.isEnabled = false
        binding!!.etFinalSettalementAmount.isClickable = false
        binding!!.etFinalSettalementAmount.isFocusable = false
        binding!!.etFinalSettalementAmount.setText("")
        binding!!.etFinalSettalementAmount.setBackgroundColor(resources.getColor(R.color.lightgray))
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
        binding!!.lpEndDate.setBackgroundColor(resources.getColor(R.color.lightgray))
        binding!!.uploadTruck.isEnabled = false
        binding!!.uploadTruck.isClickable = false
        binding!!.spinnerRatetYpe.isEnabled = false
        binding!!.spinnerRatetYpe.isClickable = false
    }

    private fun NotChecked() {
        checked = false
        /*  binding.etTransporterName.setEnabled(true);
        binding.etTransporterName.setClickable(true);
        binding.etTransporterName.setFocusable(true);
        binding.etTransporterName.setFocusableInTouchMode(true);*/binding!!.spinnerRatetYpe.isEnabled =
            true
        binding!!.spinnerRatetYpe.isClickable = true
        binding!!.uploadTruck.isEnabled = true
        binding!!.uploadTruck.isClickable = true
        binding!!.etTransportRate.isEnabled = true
        binding!!.etTransportRate.isClickable = true
        binding!!.etTransportRate.isFocusable = true
        binding!!.etTransportRate.isFocusableInTouchMode = true
        binding!!.etMinWeight.isEnabled = true
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
        binding!!.etBags.isFocusableInTouchMode = true
        binding!!.etTotalTransCost.isEnabled = true
        binding!!.etTotalTransCost.isClickable = true
        binding!!.etTotalTransCost.isFocusable = true
        binding!!.etTotalTransCost.isFocusableInTouchMode = true
        binding!!.etAdvancePatyment.isEnabled = true
        binding!!.etAdvancePatyment.isClickable = true
        binding!!.etAdvancePatyment.isFocusable = true
        binding!!.etAdvancePatyment.isFocusableInTouchMode = true
        binding!!.etFinalSettalementAmount.isEnabled = true
        binding!!.etFinalSettalementAmount.isClickable = true
        binding!!.etFinalSettalementAmount.isFocusable = true
        binding!!.etFinalSettalementAmount.isFocusableInTouchMode = true
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
        binding!!.lpCommiteDate.isFocusableInTouchMode = true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityAndClear(OUTTruckBookListingActivity::class.java)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_close -> startActivityAndClear(OUTTruckBookListingActivity::class.java)
            R.id.et_start_date_time -> popUpDatePicker()
            R.id.lp_commite_date -> popUpDatePicker()
            R.id.lp_end_date -> EnddatePicker()
            R.id.et_end_date_time -> EnddatePicker()
            R.id.btn_login -> if (isValid) {
                if (checked) {
                    if (binding!!.notes.text.toString().trim { it <= ' ' }.isEmpty()) {
                        Toast.makeText(
                            this@OUTTruckUploadDetailsClass,
                            "Enter Notes Here!!",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (TransporterID == null) {
                        Toast.makeText(
                            this@OUTTruckUploadDetailsClass,
                            "Select Transport Name!!",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Utility.showDecisionDialog(
                            this@OUTTruckUploadDetailsClass,
                            getString(R.string.alert),
                            "Are You Sure to Summit?"
                        ) { callApi() }
                    }
                } else if (TransporterID == null) {
                    Toast.makeText(
                        this@OUTTruckUploadDetailsClass,
                        "Select Transport Name!!",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (spinnerRateType == null) {
                    Toast.makeText(
                        this@OUTTruckUploadDetailsClass,
                        resources.getString(R.string.select_purposee),
                        Toast.LENGTH_LONG
                    ).show()
                } else if (TextUtils.isEmpty(stringFromView(binding!!.etStartDateTime))) {
                    Toast.makeText(
                        this@OUTTruckUploadDetailsClass,
                        resources.getString(R.string.start_date_time_val),
                        Toast.LENGTH_LONG
                    ).show()
                } else if (TextUtils.isEmpty(stringFromView(binding!!.etEndDateTime))) {
                    Toast.makeText(
                        this@OUTTruckUploadDetailsClass,
                        resources.getString(R.string.end_date_time_val),
                        Toast.LENGTH_LONG
                    ).show()
                } else if (fileBiltyImage == null) {
                    Toast.makeText(
                        this@OUTTruckUploadDetailsClass,
                        "Upload to Bilty Image",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Utility.showDecisionDialog(
                        this@OUTTruckUploadDetailsClass,
                        getString(R.string.alert),
                        "Are You Sure to Summit?"
                    ) { callApi() }
                }
            }
        }
    }

    private fun callApi() {
        var BiltyImage = ""
        if (fileBiltyImage != null) {
            BiltyImage = "" + Utility.transferImageToBase64(fileBiltyImage)
        }
        apiService.uploadTruckDetails(
            UploadTruckDetailsPostData(
                CaseID,
                TransporterID,
                stringFromView(binding!!.etVehicleNo),
                stringFromView(binding!!.etDriverName),
                stringFromView(binding!!.etDriverPhoneNo),
                stringFromView(
                    binding!!.etMinWeight
                ),
                stringFromView(binding!!.etMaxWeight),
                stringFromView(binding!!.etTurnaroundTime),
                stringFromView(
                    binding!!.etTotalWeight
                ),
                stringFromView(binding!!.etBags),
                stringFromView(binding!!.etTotalTransCost),
                stringFromView(
                    binding!!.etAdvancePatyment
                ),
                stringFromView(binding!!.etStartDateTime),
                stringFromView(binding!!.etFinalSettalementAmount),
                stringFromView(binding!!.etEndDateTime),
                stringFromView(
                    binding!!.notes
                ),
                TransporterID,
                BiltyImage,
                spinnerRateType,
                stringFromView(binding!!.etRealteCaseid)
            )
        )
            .doOnSubscribe {
                showDialog()
            }
            .doOnNext { body ->
                Utility.showAlertDialog(
                    this@OUTTruckUploadDetailsClass,
                    getString(R.string.alert),
                    body.message
                ) { startActivityAndClear(OUTTruckBookListingActivity::class.java) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

    }

    val isValid: Boolean
        get() {
            if (checked) {
            } else {
                /*if (TextUtils.isEmpty(stringFromView(binding.etTransporterName))) {
                return Utility.showEditTextError(binding.tilTransporterName, R.string.transporter_name);
            } else*/
                /* if (TextUtils.isEmpty(stringFromView(binding.etVehicleNo))) {
                return Utility.showEditTextError(binding.tilVehicleNo, R.string.vehicle_no);
            } else if (TextUtils.isEmpty(stringFromView(binding.etDriverName))) {
                return Utility.showEditTextError(binding.tilDriverName, R.string.driver_name_validation);
            } else if (TextUtils.isEmpty(stringFromView(binding.etDriverPhoneNo))) {
                return Utility.showEditTextError(binding.tilDriverPhoneNo, R.string.driver_phone_no_enter);
            }*/
                if (spinnerRateType == null) {
                    Toast.makeText(
                        this@OUTTruckUploadDetailsClass,
                        "Select Rate Type",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (TextUtils.isEmpty(stringFromView(binding!!.etTransportRate))) {
                    return Utility.showEditTextError(
                        binding!!.tilTransportRate,
                        R.string.transport_rate_validation
                    )
                } else if (TextUtils.isEmpty(stringFromView(binding!!.etMinWeight))) {
                    return Utility.showEditTextError(
                        binding!!.tilMinWeight,
                        R.string.min_weight_val
                    )
                } else if (TextUtils.isEmpty(stringFromView(binding!!.etMaxWeight))) {
                    return Utility.showEditTextError(
                        binding!!.tilMaxWeight,
                        R.string.max_weight_val
                    )
                } else if (TextUtils.isEmpty(stringFromView(binding!!.etTurnaroundTime))) {
                    return Utility.showEditTextError(
                        binding!!.tilTurnaroundTime,
                        R.string.turnaround_time_val
                    )
                } else if (TextUtils.isEmpty(stringFromView(binding!!.etTotalWeight))) {
                    return Utility.showEditTextError(
                        binding!!.tilTotalWeight,
                        R.string.total_weight_val
                    )
                } else if (TextUtils.isEmpty(stringFromView(binding!!.etBags))) {
                    return Utility.showEditTextError(binding!!.tilBags, R.string.bags_val)
                } else if (TextUtils.isEmpty(stringFromView(binding!!.etAdvancePatyment))) {
                    return Utility.showEditTextError(
                        binding!!.tilAdvancePatyment,
                        R.string.advance_patyment_val
                    )
                } else if (TextUtils.isEmpty(stringFromView(binding!!.etTotalTransCost))) {
                    return Utility.showEditTextError(
                        binding!!.tilTotalTransCost,
                        R.string.total_trans_cost_val
                    )
                } else if (TextUtils.isEmpty(stringFromView(binding!!.etFinalSettalementAmount))) {
                    return Utility.showEditTextError(
                        binding!!.tilFinalSettalementAmount,
                        R.string.settleememt_amount_val
                    )
                } else if (TextUtils.isEmpty(stringFromView(binding!!.etRealteCaseid))) {
                    return Utility.showEditTextError(
                        binding!!.etRealteCaseid,
                        "Enter Related CaseId"
                    )
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
        dateDialog.datePicker.minDate = System.currentTimeMillis()
        dateDialog.show()
    }

    var date = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        calender!![Calendar.YEAR] = year
        calender!![Calendar.MONTH] = monthOfYear
        calender!![Calendar.DAY_OF_MONTH] = dayOfMonth
        val myFormat = "dd-MMM-yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding!!.etStartDateTime.setText(sdf.format(calender!!.time).toString())
        StarttimePicker(sdf.format(calender!!.time).toString())
    }

    private fun StarttimePicker(date: String) {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(
            this,
            { timePicker, selectedHour, selectedMinute -> binding!!.etStartDateTime.setText("$date - $selectedHour:$selectedMinute") },
            hour,
            minute,
            true
        ) //Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    fun EnddatePicker() {
        val dateDialog = DatePickerDialog(
            this, dateeend, calender
            !!.get(Calendar.YEAR), calender!![Calendar.MONTH],
            calender!![Calendar.DAY_OF_MONTH]
        )
        dateDialog.datePicker.minDate = System.currentTimeMillis()
        dateDialog.show()
    }

    var dateeend = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        calender!![Calendar.YEAR] = year
        calender!![Calendar.MONTH] = monthOfYear
        calender!![Calendar.DAY_OF_MONTH] = dayOfMonth
        val myFormat = "dd-MMM-yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding!!.etEndDateTime.setText(sdf.format(calender!!.time).toString())
        EndtimePicker(sdf.format(calender!!.time).toString())
    }

    private fun EndtimePicker(date: String) {
        Log.d("Al JSon Data", "" + date)
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(
            this,
            { timePicker, selectedHour, selectedMinute -> binding!!.etEndDateTime.setText("$date - $selectedHour:$selectedMinute") },
            hour,
            minute,
            true
        ) //Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {}
    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
}
