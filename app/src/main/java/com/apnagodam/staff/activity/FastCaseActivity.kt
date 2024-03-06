package com.apnagodam.staff.activity

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.RequestOfflineCaseData
import com.apnagodam.staff.Network.Response.BaseResponse
import com.apnagodam.staff.Network.Response.ResponseSendOtp
import com.apnagodam.staff.Network.Response.ResponseStackData
import com.apnagodam.staff.Network.Response.ResponseUserData
import com.apnagodam.staff.Network.Response.ResponseWarehouse
import com.apnagodam.staff.Network.Response.ResponseWarehouse.CommodityList
import com.apnagodam.staff.Network.Response.ResponseWarehouse.ContractorList
import com.apnagodam.staff.Network.Response.ResponseWarehouse.DharmKanta
import com.apnagodam.staff.Network.Response.ResponseWarehouse.WarehouseData
import com.apnagodam.staff.Network.Response.VerifyOtpFastcase
import com.apnagodam.staff.Network.viewmodel.CaseIdViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityFastCaseBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.utils.ImageHelper
import com.apnagodam.staff.utils.Utility
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.gms.location.LocationServices
import com.thorny.photoeasy.OnPictureReady
import com.thorny.photoeasy.PhotoEasy
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class FastCaseActivity : BaseActivity<ActivityFastCaseBinding?>() {
    private var mContext: Context? = null
    private var SpinnerCommudityAdapter: ArrayAdapter<String>? = null
    private var SpinnerTerminalAdapter: ArrayAdapter<String?>? = null
    private var SpinnerStackAdapter: ArrayAdapter<String?>? = null
    private var SpinnerKantaAdapter: ArrayAdapter<String?>? = null
    private var SpinnerContractorAdapter: ArrayAdapter<String?>? = null
    private var StackName = arrayListOf<String>()
    private var CommudityName = arrayListOf<String>()
    private var terminalName = arrayListOf<String>()
    private var kantaName = arrayListOf<String>()
    private var labourContractorName = arrayListOf<String>()
    private var commudityData: List<CommodityList> = ArrayList()
    private var terminalData: List<WarehouseData> = ArrayList()
    private var stackData: List<ResponseStackData.Data> = ArrayList()
    private var kantaData: List<DharmKanta> = ArrayList()
    private var contractorList: List<ContractorList> = ArrayList()
    private var stackID = ""
    private var commudityID = ""
    private var terminalID = ""
    private var kantaID = ""
    private var labourContractorID = ""
    private var fileTruck: File? = null
    private var fileKanta: File? = null
    private var IMAGE = ""
    private var userData = ResponseUserData()
    private val camUri: Uri? = null
    val caseIdViewModel by viewModels<CaseIdViewModel>()
    private var VERIFY: String? = "0"
    lateinit var photoEasy: PhotoEasy
    var lat = 0.0
    var long = 0.0
    var currentLocation = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (savedInstanceState != null) {
//            IMAGE = savedInstanceState.getString("param");
//        }
        if (savedInstanceState != null) {
            //    if(imageUri != null){
            VERIFY = savedInstanceState.getString("VERIFY")
            camUri = savedInstanceState.getParcelable("imageUri")
            binding!!.imgTruck.setImageURI(camUri)

            //     }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_fast_case
    }

    override fun setUp() {
        mContext = this@FastCaseActivity
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

        }
        else{
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
        }
        warehouse()
        binding!!.radioGroup.setOnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
            if (binding!!.rbOnline.id == checkedId) {
                binding!!.lytOtp.visibility = View.GONE
                binding!!.btnSendOtp.visibility = View.VISIBLE
                binding!!.edtEnterOtp.visibility = View.VISIBLE
            } else if (binding!!.rbOffline.id == checkedId) {
                binding!!.lytOtp.visibility = View.VISIBLE
                binding!!.edtEnterOtp.visibility = View.GONE
                binding!!.btnSendOtp.visibility = View.GONE
                binding!!.lytVerifyOtp.visibility = View.GONE
            }
        }
        binding!!.btnSubmitNumber.setOnClickListener {
            if (binding!!.etPhone.text.toString().length == 0) {
                Toast.makeText(mContext, "Enter Phone Number", Toast.LENGTH_SHORT).show()
            } else if (binding!!.etPhone.text.toString().length < 10) {
                Toast.makeText(mContext, "Enter Correct Phone Number", Toast.LENGTH_SHORT).show()
            } else {
                getUserData(binding!!.etPhone.text.toString())
            }
        }
        binding!!.btnSendOtp.setOnClickListener { sendOtpFastCase(binding!!.etToken.text.toString()) }
        binding!!.btnVerifySendOtp.setOnClickListener {
            if (binding!!.edtEnterOtp.text.toString().length < 6) {
                binding!!.edtEnterOtp.error = "Enter Otp"
                Toast.makeText(mContext, "Enter Otp", Toast.LENGTH_SHORT).show()
            } else {
                verifyOtpFastCase(
                    binding!!.etToken.text.toString(),
                    binding!!.edtEnterOtp.text.toString()
                )
            }
        }
        binding!!.btnResendOtp.setOnClickListener { sendOtpFastCase(binding!!.etToken.text.toString()) }
        binding!!.lytTruck.setOnClickListener {
            IMAGE = "TRUCK"
            dispatchTakePictureIntent()
            //        dispatchTakePictureIntent();
        }
        binding!!.lytFirstKantaParchi.setOnClickListener {
            IMAGE = "KANTA"
            dispatchTakePictureIntent()
        }
        binding!!.btnSubmit.setOnClickListener {
            if (binding!!.rbOnline.isChecked) {
                if (binding!!.etToken.text!!.length == 0) {
                    Toast.makeText(mContext, "Enter Token Number", Toast.LENGTH_SHORT).show()
                } else if (binding!!.edtEnterOtp.text!!.length == 0) {
                    Toast.makeText(mContext, "Enter OTP", Toast.LENGTH_SHORT).show()
                } else if (binding!!.edtUsername.text!!.length == 0) {
                    Toast.makeText(mContext, "Check Token Number", Toast.LENGTH_SHORT).show()
                } else if (binding!!.spinnerCommudity.selectedItemPosition == 0) {
                    Toast.makeText(mContext, "Select Commodity", Toast.LENGTH_SHORT).show()
                } else if (binding!!.spinnerTerminal.selectedItemPosition == 0) {
                    Toast.makeText(mContext, "Select Terminal", Toast.LENGTH_SHORT).show()
                } else if (binding!!.spinnerStack.selectedItemPosition == 0) {
                    Toast.makeText(mContext, "Select Stack", Toast.LENGTH_SHORT).show()
                } else if (binding!!.edtBag.text!!.length == 0) {
                    Toast.makeText(mContext, "Enter Bags", Toast.LENGTH_SHORT).show()
                } else if (binding!!.edtWeight.text!!.length == 0) {
                    Toast.makeText(mContext, "Enter Weight", Toast.LENGTH_SHORT).show()
                } else if (binding!!.edtVehicleNo.text!!.length == 0) {
                    Toast.makeText(mContext, "Enter Vehicle No.", Toast.LENGTH_SHORT).show()
                } else if (binding!!.spinnerKanta.selectedItemPosition == 0) {
                    Toast.makeText(mContext, "Select Kanta", Toast.LENGTH_SHORT).show()
                } else if (binding!!.edtKantaParchiNo.text!!.length == 0) {
                    Toast.makeText(mContext, "Enter Kanta Parchi Number", Toast.LENGTH_SHORT).show()
                } else if (binding!!.spinnerLabourContractor.selectedItemPosition == 0) {
                    Toast.makeText(mContext, "Select Contractor", Toast.LENGTH_SHORT).show()
                } else if (fileTruck == null) {
                    Toast.makeText(mContext, "Select Truck Photo", Toast.LENGTH_SHORT).show()
                } else if (fileKanta == null) {
                    Toast.makeText(mContext, "Select Kanta Parchi", Toast.LENGTH_SHORT).show()
                } else if (binding!!.spinnerSplitDelivery.selectedItemPosition == 0) {
                    Toast.makeText(mContext, "Select Split Delivery", Toast.LENGTH_SHORT).show()
                } else if (VERIFY != "1") {
                    Toast.makeText(mContext, "Verify OTP", Toast.LENGTH_SHORT).show()
                } else {
                    onlineFastCase()
                }
            } else if (binding!!.rbOffline.isChecked) {
                if (binding!!.etPhone.text!!.length == 0) {
                    Toast.makeText(mContext, "Enter Phone Number", Toast.LENGTH_SHORT).show()
                } else if (binding!!.etToken.text!!.length == 0) {
                    Toast.makeText(mContext, "Enter Token Number", Toast.LENGTH_SHORT).show()
                } else if (binding!!.edtUsername.text!!.length == 0) {
                    Toast.makeText(mContext, "Check Phone Number", Toast.LENGTH_SHORT).show()
                } else if (binding!!.spinnerCommudity.selectedItemPosition == 0) {
                    Toast.makeText(mContext, "Select Commodity", Toast.LENGTH_SHORT).show()
                } else if (binding!!.spinnerTerminal.selectedItemPosition == 0) {
                    Toast.makeText(mContext, "Select Terminal", Toast.LENGTH_SHORT).show()
                } else if (binding!!.spinnerStack.selectedItemPosition == 0) {
                    Toast.makeText(mContext, "Select Stack", Toast.LENGTH_SHORT).show()
                } else if (binding!!.edtBag.text!!.length == 0) {
                    Toast.makeText(mContext, "Enter Bags", Toast.LENGTH_SHORT).show()
                } else if (binding!!.edtWeight.text!!.length == 0) {
                    Toast.makeText(mContext, "Enter Weight", Toast.LENGTH_SHORT).show()
                } else if (binding!!.edtVehicleNo.text!!.length == 0) {
                    Toast.makeText(mContext, "Enter Vehicle No.", Toast.LENGTH_SHORT).show()
                } else if (binding!!.spinnerKanta.selectedItemPosition == 0) {
                    Toast.makeText(mContext, "Select Kanta", Toast.LENGTH_SHORT).show()
                } else if (binding!!.edtKantaParchiNo.text!!.length == 0) {
                    Toast.makeText(mContext, "Enter Kanta Parchi Number", Toast.LENGTH_SHORT).show()
                } else if (binding!!.spinnerLabourContractor.selectedItemPosition == 0) {
                    Toast.makeText(mContext, "Select Contractor", Toast.LENGTH_SHORT).show()
                } else if (fileTruck == null) {
                    Toast.makeText(mContext, "Select Truck Photo", Toast.LENGTH_SHORT).show()
                } else if (fileKanta == null) {
                    Toast.makeText(mContext, "Select Kanta Parchi", Toast.LENGTH_SHORT).show()
                } else if (binding!!.spinnerSplitDelivery.selectedItemPosition == 0) {
                    Toast.makeText(mContext, "Select Split Delivery", Toast.LENGTH_SHORT).show()
                } else {
                    offlineFastCase()
                }
            }
        }
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
    private fun warehouse() {
        showDialog()
        caseIdViewModel.getFastcaseWarehouseData()
        caseIdViewModel.fastCaseWarehouseResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog()
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    hideDialog()
                    if (it.data != null) {
                        CommudityName = ArrayList()
                        terminalName = ArrayList()
                        StackName = ArrayList()
                        kantaName = ArrayList()
                        labourContractorName = ArrayList()
                        CommudityName.add(resources.getString(R.string.commodity_name))
                        terminalName.add(resources.getString(R.string.terminal_name1))
                        kantaName.add(resources.getString(R.string.kanta_name))
                        StackName.add(resources.getString(R.string.select_stack))
                        labourContractorName.add(resources.getString(R.string.select_contractor))
                        it.data.let { body ->
                            commudityData = body!!.commodity_list
                            terminalData = body.warehouseData
                            kantaData = body.dharam_kanta
                            contractorList = body.contractorLists
                            for (i in body.commodity_list.indices) {
                                CommudityName!!.add(body.commodity_list[i].category)
                            }
                            for (i in body.warehouseData.indices) {
                                terminalName!!.add(body.warehouseData[i].name)
                            }
                            for (i in body.dharam_kanta.indices) {
                                kantaName!!.add(body.dharam_kanta[i].name)
                            }
                            for (i in body.contractorLists.indices) {
                                labourContractorName!!.add(body.contractorLists[i].contractor_name)
                            }
                            setCommoditySpinner(CommudityName)
                            setTerminalSpinner(terminalName)
                            setKantaSpinner(kantaName)
                            setContractorSpinner(labourContractorName)
                        }
                    }
                }
            }
        }
    }

    private val stack: Unit
        private get() {
            showDialog()
            caseIdViewModel.getStack(terminalID, commudityID)
            caseIdViewModel.stackResponse.observe(this) {
                when (it) {
                    is NetworkResult.Error -> {
                        hideDialog()
                    }

                    is NetworkResult.Loading -> {

                    }

                    is NetworkResult.Success -> {
                        hideDialog()
                        if (it.data != null) {
                            if (it.data.status == "1") {
                                it.data.let { body ->
                                    stackData = body!!.data
                                    for (i in body.data.indices) {
                                        StackName!!.add(body.data[i].stack_number)
                                    }
                                    setStackSpinner(StackName)
                                }
                            } else showToast(it.message)
                        }
                    }
                }
            }
        }

    private fun setCommoditySpinner(CommudityName: List<String>) {

        // commodity listing
        SpinnerCommudityAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,CommudityName)

        // Set Adapter in the spinner
        binding!!.spinnerCommudity.adapter = SpinnerCommudityAdapter
        binding!!.spinnerCommudity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    // selected item in the list
                    if (position != 0) {
                        commudityID = commudityData[position - 1].id
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }
    }

    private fun setTerminalSpinner(terminalName: List<String>) {

        // commodity listing
        SpinnerTerminalAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, terminalName)
        // Set Adapter in the spinner
        binding!!.spinnerTerminal.adapter = SpinnerTerminalAdapter
        binding!!.spinnerTerminal.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    // selected item in the list
                    if (position != 0) {
                        terminalID = terminalData[position - 1].id
                        stack
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }
    }

    private fun setStackSpinner(stackName: List<String>?) {

        // commodity listing
        SpinnerStackAdapter =
           ArrayAdapter(this, R.layout.multiline_spinner_item, stackName!!)
        // Set Adapter in the spinner
        binding!!.spinnerStack.adapter = SpinnerStackAdapter
        binding!!.spinnerStack.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    // selected item in the list
                    if (position != 0) {
                        stackID = stackData[position - 1].id
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }
    }

    private fun setKantaSpinner(terminalName: List<String>?) {
        // commodity listing
        SpinnerKantaAdapter =
          ArrayAdapter(this, R.layout.multiline_spinner_item, terminalName!!)
        // Set Adapter in the spinner
        binding!!.spinnerKanta.adapter = SpinnerKantaAdapter
        binding!!.spinnerKanta.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    // selected item in the list
                    if (position != 0) {
                        kantaID = kantaData[position - 1].id
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }
    }

    private fun setContractorSpinner(terminalName: List<String>?) {

        // commodity listing
        SpinnerContractorAdapter =
           ArrayAdapter(this, R.layout.multiline_spinner_item, terminalName!!)
        // Set Adapter in the spinner
        binding!!.spinnerLabourContractor.adapter = SpinnerContractorAdapter
        binding!!.spinnerLabourContractor.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    // selected item in the list
                    if (position != 0) {
                        labourContractorID = contractorList[position - 1].id
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
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
                if(thumbnail!=null){

                    if (IMAGE == "TRUCK") {
                        var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                            File(compressImage(bitmapToFile(thumbnail).path)),
                            stampMap
                        )
                        fileTruck = File(compressImage(bitmapToFile(stampedBitmap).path))
                        binding!!.imgTruck.setImageBitmap(stampedBitmap)
                    } else if (IMAGE == "KANTA") {
                        var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                            File(compressImage(bitmapToFile(thumbnail).path)),
                            stampMap
                        )
                        fileKanta = File(compressImage(bitmapToFile(stampedBitmap).path))
                        binding!!.imgFirstKantaParchi.setImageBitmap(stampedBitmap)
                    }

                }

            }

        })
//        try {
//            if (resultCode == RESULT_OK) {
//                if (requestCode == REQUEST_CAMERA_PICTURE) {
//
//                    //    Uri imageUri = data.getData();
//                    val camFile: File
//                    if (camUri != null) {
//                        //                    Log.d("bbbbbbb", "bb");
//                        if (IMAGE == "TRUCK") {
//                            camFile = File(camUri.path)
//                            fileTruck = File(compressImage(camUri.path.toString()))
//                            val uri = Uri.fromFile(fileTruck)
//                            binding!!.imgTruck.setImageURI(uri)
//                        } else if (IMAGE == "KANTA") {
//                            camFile = File(camUri.path)
//                            fileKanta = File(compressImage(camUri.path.toString()))
//                            val uri = Uri.fromFile(fileKanta)
//                            binding!!.imgFirstKantaParchi.setImageURI(uri)
//                        }
//                    } else {
//                        //                    Log.d("bbbbbbb", "cc");
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
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

    private fun getUserData(phone: String) {
        if (Utility.isNetworkAvailable(this)) {
            showDialog()

            caseIdViewModel.getUser(phone)
            caseIdViewModel.userResponse.observe(this) {
                when (it) {
                    is NetworkResult.Error -> {
                        hideDialog()
                    }

                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        hideDialog()
                        if (it.data != null) {
                            if (it.data.status == "1") {
                                it.data.let { body ->
                                    if (body.data != null) {
                                        binding!!.edtUsername.setText(body.data.name)
                                    } else {
                                        Toast.makeText(mContext, body.message, Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            } else
                                showToast(it.message)
                        }

                    }
                }
            }

        } else {
            Utility.showAlertDialog(
                this,
                this.getString(R.string.alert),
                this.getString(R.string.no_internet_connection)
            )
        }
    }

    private fun offlineFastCase() {
        var truck_file = ""
        var kanta_parchi = ""
        if (fileTruck != null) {
            truck_file = "" + Utility.transferImageToBase64(fileTruck)
        }
        if (fileKanta != null) {
            kanta_parchi = "" + Utility.transferImageToBase64(fileKanta)
        }
        if (Utility.isNetworkAvailable(this)) {

            showDialog()
            caseIdViewModel.offlineFastcase(
                RequestOfflineCaseData(
                    userData.data.user_id,
                    commudityID,
                    binding!!.etPhone.text.toString(),
                    binding!!.etToken.text.toString(),
                    terminalID,
                    stackID,
                    binding!!.edtBag.text.toString(),
                    binding!!.edtWeight.text.toString(),
                    binding!!.edtVehicleNo.text.toString(),
                    kantaID,
                    binding!!.edtKantaParchiNo.text.toString(),
                    labourContractorID,
                    binding!!.spinnerSplitDelivery.selectedItem.toString(),
                    "",
                    truck_file,
                    kanta_parchi
                )
            )
            caseIdViewModel.offlineFastcaseResponse.observe(this) {
                when (it) {
                    is NetworkResult.Error -> {
                        hideDialog()
                    }

                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        if (it.data != null) {
                            showToast(it.data.message)
                        }
                    }
                }
            }

        } else {
            Utility.showAlertDialog(
                this,
                this.getString(R.string.alert),
                this.getString(R.string.no_internet_connection)
            )
        }
    }

    private fun onlineFastCase() {
        var truck_file = ""
        var kanta_parchi = ""
        if (fileTruck != null) {
            truck_file = "" + Utility.transferImageToBase64(fileTruck)
        }
        if (fileKanta != null) {
            kanta_parchi = "" + Utility.transferImageToBase64(fileKanta)
        }
        if (Utility.isNetworkAvailable(this)) {
            caseIdViewModel.offlineFastcase(
                RequestOfflineCaseData(
                    userData.data.user_id,
                    commudityID,
                    binding!!.etPhone.text.toString(),
                    binding!!.etToken.text.toString(),
                    terminalID,
                    stackID,
                    binding!!.edtBag.text.toString(),
                    binding!!.edtWeight.text.toString(),
                    binding!!.edtVehicleNo.text.toString(),
                    kantaID,
                    binding!!.edtKantaParchiNo.text.toString(),
                    labourContractorID,
                    binding!!.spinnerSplitDelivery.selectedItem.toString(),
                    binding!!.edtEnterOtp.text.toString(),
                    truck_file,
                    kanta_parchi
                )
            )
            caseIdViewModel.offlineFastcaseResponse.observe(this) {
                when (it) {
                    is NetworkResult.Error -> {
                        hideDialog()
                    }

                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        if (it.data != null) {
                            Utility.showAlertDialog(
                                mContext,
                                getString(R.string.alert),
                                it.data.message
                            ) { finish() }
                        }
                    }
                }
            }

        } else {
            Utility.showAlertDialog(
                this,
                this.getString(R.string.alert),
                this.getString(R.string.no_internet_connection)
            )
        }
    }

    private fun sendOtpFastCase(token: String) {
        if (Utility.isNetworkAvailable(this)) {
            apiService.sendOtpFastCase(token)!!.enqueue(object : NetworkCallback<ResponseSendOtp?>(
                activity
            ) {
                override fun onSuccess(body: ResponseSendOtp?) {
                    if (!body!!.getUser_id().isEmpty()) {
                        binding!!.edtUsername.setText(body.getUser_name())
                        binding!!.lytVerifyOtp.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(mContext, body.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            Utility.showAlertDialog(
                this,
                this.getString(R.string.alert),
                this.getString(R.string.no_internet_connection)
            )
        }
    }

    private fun verifyOtpFastCase(token: String, otp: String) {
        if (Utility.isNetworkAvailable(this)) {
            apiService.verifyOtpFastCase(token, otp)!!
                .enqueue(object : NetworkCallback<VerifyOtpFastcase?>(
                    activity
                ) {
                    override fun onSuccess(body: VerifyOtpFastcase?) {
                        if (body!!.data != null && body.data == "1") {
                            VERIFY = "1"
                            binding!!.edtEnterOtp.visibility = View.GONE
                            binding!!.lytVerifyOtp.visibility = View.GONE
                        } else {
                            binding!!.edtEnterOtp.setText("")
                            Toast.makeText(mContext, body.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        } else {
            Utility.showAlertDialog(
                this,
                this.getString(R.string.alert),
                this.getString(R.string.no_internet_connection)
            )
        }
    }

    private fun onImageClicked() {
        /*  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT > 22) {
                askForPermissions(PERMISSIONS,
                        REQUEST_CAMERA_PERMISSIONS);
            } else {
                startCameraForPic();
            }
        } else {
            startCameraForPic();
        }*/
        if (requestPermissions()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQUEST_CAMERA_CODE
            )
        }
    }

    private fun requestPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val writeExternalPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readExternalPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalPermission == PackageManager.PERMISSION_GRANTED && readExternalPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        var camFile = Utility.getOutputMediaFile(mContext, "img")
        if (camFile.exists()) {
            camFile.delete()
        }
        camFile = Utility.getOutputMediaFile(mContext, "img")
        camUri = Uri.fromFile(camFile)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(camFile));
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", camFile)
        )
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivityForResult(intent, REQUEST_CAMERA_PICTURE)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("imageUri", camUri)
        outState.putString("VERIFY", VERIFY)
    } //    protected void onSaveInstanceState(Bundle icicle) {

    //        super.onSaveInstanceState(icicle);
    //        if (camUri != null)
    //            icicle.putString("param", IMAGE);
    //    }
    companion object {
        private const val REQUEST_CAMERA_CODE = 120
    }
}