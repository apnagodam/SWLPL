package com.apnagodam.staff.activity.`in`.truckbook

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.net.toUri
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
import com.apnagodam.staff.utils.Tags.BUNDLE
import com.apnagodam.staff.utils.Utility
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
@AndroidEntryPoint
class TruckUploadDetailsClass() : BaseActivity<ActivityUploadDetailsBinding?>(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    var UserName: String? = null
    var CaseID: String? = ""
    private var calender: Calendar? = null
    var checked = false
    var spinnerRateType: String? = null
    var fileBiltyImage: File? = null
    lateinit var transportTypeAdapter: ArrayAdapter<String>
    var BiltyImageFile = false
    private var BiltyFile: String? = null
    lateinit var options: Options
    var data: List<TransporterListPojo.Datum>? = null
    var TransporterName: MutableList<String> = arrayListOf()
    lateinit var spinnerTransporterAdpter: ArrayAdapter<String>
    var TransporterID: String? = null
    var transTypeList = arrayListOf<String>()

    val truckBookViewModel by viewModels<TruckBookViewModel>()
   override  fun getLayoutResId(): Int {
        return R.layout.activity_upload_details
    }

    override fun setUp() {
        transTypeList.add("Select")
        transTypeList.add("Client Transport")
        transTypeList.add("Company Transport")
        calender = Calendar.getInstance()
        val bundle = intent.getBundleExtra(BUNDLE)
        if (bundle != null) {
            UserName = bundle.getString("user_name")
            CaseID = bundle.getString("case_id")
        }
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
                    if (position==1){
                        Checked()
                    }
                    else{
                        NotChecked()
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {


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

                   if(data!=null){
                       val presentMeterStatusID = parentView.getItemAtPosition(position).toString()
                       for (i in data!!.indices) {
                           if (presentMeterStatusID.contains(data!![i].transporterName + "(" + data!![i].transporterUniqueId + ")")) {
                               TransporterID = data!![i].id.toString()
                               break
                           }
                       }
                       getTransporterDetails(TransporterID!!)
                   }
//                    if (presentMeterStatusID.contains("Client")) {
//                        Checked()
//                    } else {
//                        NotChecked()
//                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }

        /*    binding.etAdvancePatyment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etAdvancePatyment.getText().toString().trim() != null && !binding.etAdvancePatyment.getText().toString().trim().isEmpty() && !binding.etMaxWeight.getText().toString().trim().isEmpty() && !binding.etTransportRate.getText().toString().trim().isEmpty()) {
                        Double etAdvancePatyment = (Double.parseDouble(binding.etAdvancePatyment.getText().toString().trim()));
                        Double maxWeight = (Double.parseDouble(binding.etMaxWeight.getText().toString().trim()));
                        Double TransportRate = (Double.parseDouble(binding.etTransportRate.getText().toString().trim()));
                        Double Advancement = maxWeight * TransportRate;
                        if (etAdvancePatyment > Advancement) {
                            Utility.showAlertDialog(TruckUploadDetailsClass.this, getString(R.string.alert), "Advance Payment  Must be Less than or equal to " + Advancement + " RS", new Utility.AlertCallback() {
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
        });*/binding!!.etMaxWeight.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (binding!!.etAdvancePatyment.text.toString()
                        .trim { it <= ' ' } != null && !binding!!.etAdvancePatyment.text.toString()
                        .trim { it <= ' ' }
                        .isEmpty() && !binding!!.etMaxWeight.text.toString().trim { it <= ' ' }
                        .isEmpty() && !binding!!.etTransportRate.text.toString().trim { it <= ' ' }
                        .isEmpty()
                ) {
                    val etAdvancePatyment =
                        binding!!.etAdvancePatyment.text.toString().trim { it <= ' ' }
                            .toDouble()
                    val maxWeight = binding!!.etMaxWeight.text.toString().trim { it <= ' ' }
                        .toDouble()
                    val TransportRate = binding!!.etTransportRate.text.toString().trim { it <= ' ' }
                        .toDouble()
                    val Advancement = maxWeight * TransportRate
                    if (etAdvancePatyment > Advancement) {
                        Utility.showAlertDialog(
                            this@TruckUploadDetailsClass,
                            getString(R.string.alert),
                            "Advance Payment  Must be Less than or equal to $Advancement RS"
                        ) { binding!!.etAdvancePatyment.setText("") }
                    } else {
                        binding!!.etAdvancePatyment.setText("" + Advancement)
                    }
                }
            } else {
            }
        }
        binding!!.etTransportRate.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (binding!!.etAdvancePatyment.text.toString()
                        .trim { it <= ' ' } != null && !binding!!.etAdvancePatyment.text.toString()
                        .trim { it <= ' ' }
                        .isEmpty() && !binding!!.etMaxWeight.text.toString().trim { it <= ' ' }
                        .isEmpty() && !binding!!.etTransportRate.text.toString().trim { it <= ' ' }
                        .isEmpty()
                ) {
                    val etAdvancePatyment =
                        binding!!.etAdvancePatyment.text.toString().trim { it <= ' ' }
                            .toDouble()
                    val maxWeight = binding!!.etMaxWeight.text.toString().trim { it <= ' ' }
                        .toDouble()
                    val TransportRate = binding!!.etTransportRate.text.toString().trim { it <= ' ' }
                        .toDouble()
                    val Advancement = maxWeight * TransportRate
                    if (etAdvancePatyment > Advancement) {
                        Utility.showAlertDialog(
                            this@TruckUploadDetailsClass,
                            getString(R.string.alert),
                            "Advance Payment  Must be Less than or equal to $Advancement RS"
                        ) { binding!!.etAdvancePatyment.setText("") }
                    } else {
                        // binding.etAdvancePatyment.setText("" + Advancement);
                    }
                }
            } else {
            }
        }
        /*  binding.etStartDateTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etStartDateTime.getText().toString().trim() != null && !binding.etStartDateTime.getText().toString().trim().isEmpty() && !binding.etTurnaroundTime.getText().toString().trim().isEmpty()) {
                        Double maxWeight = (Double.parseDouble(binding.etStartDateTime.getText().toString().trim()));
                        Double TransportRate = (Double.parseDouble(binding.etTurnaroundTime.getText().toString().trim()));
                        Double Advancement = maxWeight * TransportRate;
                        binding.etEndDateTime.setText("" + Advancement);
                    }
                    // Toast.makeText(MainActivity.this, "focus loosed", Toast.LENGTH_LONG).show();
                } else {
                    /// Toast.makeText(MainActivity.this, "focused", Toast.LENGTH_LONG).show();
                }
            }
        });*/
        // check advance payment
        /* binding.etMaxWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etMaxWeight.getText().toString().trim() != null && !binding.etMaxWeight.getText().toString().trim().isEmpty() && !binding.etTransportRate.getText().toString().trim().isEmpty()) {
                        Double maxWeight = (Double.parseDouble(binding.etMaxWeight.getText().toString().trim()));
                        Double TransportRate = (Double.parseDouble(binding.etTransportRate.getText().toString().trim()));
                        Double Advancement = maxWeight * TransportRate;
                        binding.etAdvancePatyment.setText("" + Advancement);
                    }
                    // Toast.makeText(MainActivity.this, "focus loosed", Toast.LENGTH_LONG).show();
                } else {
                    /// Toast.makeText(MainActivity.this, "focused", Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.etTransportRate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etTransportRate.getText().toString().trim() != null && !binding.etTransportRate.getText().toString().trim().isEmpty() && !binding.etMaxWeight.getText().toString().trim().isEmpty()) {
                        Double maxWeight = (Double.parseDouble(binding.etMaxWeight.getText().toString().trim()));
                        Double TransportRate = (Double.parseDouble(binding.etTransportRate.getText().toString().trim()));
                        Double Advancepayment = maxWeight * TransportRate;
                        binding.etAdvancePatyment.setText("" + Advancepayment);
                    }
                } else {
                    /// Toast.makeText(MainActivity.this, "focused", Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }

    private fun getTransporterDetails(transporterID: String) {
        truckBookViewModel.getTransporterDetails(transporterID)
        truckBookViewModel.transporterDetailsResponse.observe(this){
            when(it){
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                   if (it.data!=null){
                       binding!!.etVehicleNo.setText("" + it.data.data.vehicleNo)
                       binding!!.etDriverName.setText("" + it.data.data.transporterName)
                       binding!!.etDriverPhoneNo.setText("" + it.data.data.transporterPhoneNo)
                   }
                }
            }
        }


    }

    fun  transporterList(){
        truckBookViewModel.transporterList()
        truckBookViewModel.transporterResponse.observe(this){
            when(it){
                is NetworkResult.Error -> {
                    hideDialog()
                }
                is NetworkResult.Loading -> {
                    showDialog()
                }
                is NetworkResult.Success -> {
                    data = it.data!!.data
                    for (i in data!!.indices) {
                        if(!data!![i].transporterName.contains("Client Transport")){
                            TransporterName!!.add(data!!.get(i).transporterName + "(" + data!!.get(i).transporterUniqueId + ")")

                        }
                    }
                     spinnerTransporterAdpter = ArrayAdapter(
                        this@TruckUploadDetailsClass,
                        R.layout.multiline_spinner_dropdown_item, TransporterName!!

                    )
//
//            spinnerTransporterAdpter = object : ArrayAdapter<String?>(
//                this@TruckUploadDetailsClass,
//                R.layout.multiline_spinner_dropdown_item, TransporterName!!
//            ) {
//                //By using this method we will define how
//                // the text appears before clicking a spinner
//                override fun getView(
//                    position: Int,
//                    convertView: View?,
//                    parent: ViewGroup
//                ): View {
//                    val v = super.getView(position, convertView, parent)
//                    (v as TextView).setTextColor(Color.parseColor("#000000"))
//                    return v
//                }
//
//                //By using this method we will define
//                //how the listview appears after clicking a spinner
//                override fun getDropDownView(
//                    position: Int,
//                    convertView: View,
//                    parent: ViewGroup
//                ): View {
//                    val v = super.getDropDownView(position, convertView, parent)
//                    v.setBackgroundColor(Color.parseColor("#05000000"))
//                    (v as TextView).setTextColor(Color.parseColor("#000000"))
//                    return v
//                }
//            }
//            spinnerTransporterAdpter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)
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
        binding!!.lpCommiteDate.setOnClickListener(this)
        binding!!.lpEndDate.setOnClickListener(this)
        binding!!.uploadTruck.setOnClickListener {
            BiltyImageFile = true
//            callImageSelector(REQUEST_CAMERA)
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

    private fun callImageSelector(requestCamera: Int) {
        options = Options.init()
        options.let {
            it.count = 1                                                   //Number of images to restrict selection count
            it.spanCount = 4                                               //Number for columns in grid
            it.path = "/apnagodam/lp/images"                                         //Custom Path For media Storage
            it.isFrontfacing = false                                       //Front Facing camera on start
            it.videoDurationLimitinSeconds = 0                            //Duration for video recording
            it.mode = Options.Mode.Picture
            it.requestCode = requestCamera
        }


        Pix.start(this@TruckUploadDetailsClass, options)
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                fileBiltyImage = File(compressImage(bitmapToFile(imageBitmap).toString()))
                val uri = Uri.fromFile(fileBiltyImage)
                BiltyFile = uri.toString()
                binding!!.TruckImage.setImageURI(uri)
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
    private fun bitmapToFile(bitmap:Bitmap): Uri {
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
        checked = true
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
        binding!!.spinnerRatetYpe.isEnabled = false
        binding!!.spinnerRatetYpe.isClickable = false
        binding!!.uploadTruck.isEnabled = false
        binding!!.uploadTruck.isClickable = false
    }

    private fun NotChecked() {
        binding!!.layoutTransport.visibility = View.VISIBLE
        checked = false
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
        startActivityAndClear(TruckBookListingActivity::class.java)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_close -> startActivityAndClear(TruckBookListingActivity::class.java)
            R.id.et_start_date_time -> popUpDatePicker()
            R.id.lp_commite_date -> popUpDatePicker()
            R.id.lp_end_date -> EnddatePicker()
            R.id.et_end_date_time -> EnddatePicker()
            R.id.btn_login -> if (isValid) {
                if (checked) {
                 if (TransporterID == null) {
                        Toast.makeText(
                            this@TruckUploadDetailsClass,
                            "Select Transport Name!!",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Utility.showDecisionDialog(
                            this@TruckUploadDetailsClass,
                            getString(R.string.alert),
                            "Are You Sure to Summit?"
                        ) { callApi() }
                    }
                } else if (TransporterID == null) {
                    Toast.makeText(
                        this@TruckUploadDetailsClass,
                        "Select Transport Name!!",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (TextUtils.isEmpty(stringFromView(binding!!.etStartDateTime))) {
                    Toast.makeText(
                        this@TruckUploadDetailsClass,
                        resources.getString(R.string.start_date_time_val),
                        Toast.LENGTH_LONG
                    ).show()
                } else if (TextUtils.isEmpty(stringFromView(binding!!.etEndDateTime))) {
                    Toast.makeText(
                        this@TruckUploadDetailsClass,
                        resources.getString(R.string.end_date_time_val),
                        Toast.LENGTH_LONG
                    ).show()
                } else if (fileBiltyImage == null) {
                    Toast.makeText(
                        this@TruckUploadDetailsClass,
                        "Upload to Bilty Image",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Utility.showDecisionDialog(
                        this@TruckUploadDetailsClass,
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
        )).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext{body->
                Utility.showAlertDialog(
                    this@TruckUploadDetailsClass,
                    getString(R.string.alert),
                    body.getMessage()
                ) { startActivityAndClear(TruckBookListingActivity::class.java) }
            }
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
            } else*/
                if (spinnerRateType == null) {
                    Toast.makeText(
                        this@TruckUploadDetailsClass,
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
            binding!!.etStartDateTime.setText(sdf.format(calender!!.time).toString())
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
                binding!!.etStartDateTime.setText("$date - $selectedHour:$selectedMinute")
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
            binding!!.etEndDateTime.setText(sdf.format(calender!!.time).toString())
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
                binding!!.etEndDateTime.setText("$date - $selectedHour:$selectedMinute")
            }
        }, hour, minute, true) //Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {}
    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
}
