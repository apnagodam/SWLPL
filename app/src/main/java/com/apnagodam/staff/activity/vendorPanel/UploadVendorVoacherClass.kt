package com.apnagodam.staff.activity.vendorPanel

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Geocoder
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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.CreateVendorConveyancePostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.viewmodel.ConveyanceViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.VendorConveyanceBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.AllLevelEmpListPojo
import com.apnagodam.staff.module.VendorExpensionApprovedListPojo
import com.apnagodam.staff.module.VendorExpensionNamePojo
import com.apnagodam.staff.module.VendorNamePojo
import com.apnagodam.staff.utils.ImageHelper
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.SearchableSpinner
import com.apnagodam.staff.utils.Utility
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
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
class UploadVendorVoacherClass : BaseActivity<VendorConveyanceBinding?>() {
    var packagingTypeID: String? = null
    var vendorTypeID: String? = null

    // role of image
    var UserName: String? = null
    var CaseID = ""
    var fileReport: File? = null
    var fileCommudity: File? = null
    var ReportsFileSelect = false
    var CommudityFileSelect = false
    private var reportFile: String? = null
    private var commudityFile: String? = null
    private lateinit var calender: Calendar

    // approved for
    var approveName: ArrayList<String> = arrayListOf()
    var approveID: ArrayList<String> = arrayListOf()
    lateinit var SpinnerApproveByAdapter: ArrayAdapter<String>
    var SelectedApproveIDIs: String? = null

    // Terminal Name
    var TerminalName: ArrayList<String> = arrayListOf()
    var TerminalID: ArrayList<String> = arrayListOf()
    lateinit var SpinnerTerminalAdapter: ArrayAdapter<String>
    var SelectedTerminalIDIs: String? = null

    // Expansion type
    var expansionName: ArrayList<String> = arrayListOf()
    var expansionID: ArrayList<String> = arrayListOf()
    lateinit var SpinnerExpansionAdapter: ArrayAdapter<String>
    var SelectedExpansionIDIs: String? = null

    // vendor Name
    lateinit var searchableSpinner: SearchableSpinner
    var vnedorName: ArrayList<String> = arrayListOf()
    var vendorID: ArrayList<String> = arrayListOf()
    lateinit var SpinnerVendorAdapter: ArrayAdapter<String>
    var SelectedVendorIDIs: String? = null
    var options: Options? = null
    var currentLocation = ""
    var lat = 0.0
    var long = 0.0
    val conveyanceViewModel by viewModels<ConveyanceViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.vendor_conveyance
    }

    override fun setUp() {
        calender = Calendar.getInstance()
        // approved for
        approveName = ArrayList()
        approveID = ArrayList()
        // staff field off
        searchableSpinner = findViewById(R.id.spinner_vendorName)
        approveName.add(resources.getString(R.string.approved_by))
        approveID.add("0")
        // terminal for
        TerminalName = ArrayList()
        TerminalID = ArrayList()
        expansionID = ArrayList()
        expansionName = ArrayList()
        TerminalName.add(resources.getString(R.string.terminal_name))
        TerminalID.add("0")
        expansionName.add(resources.getString(R.string.vendor_expension))
        expansionID.add("0")
        vnedorName = ArrayList()
        vendorID = ArrayList()
        vnedorName.add(resources.getString(R.string.vendor_name))
        vendorID.add("0")
        binding!!.vendortitle.text = "Generate Vendor Voucher"
        setSupportActionBar(binding!!.toolbar)
        binding!!.etLocation.visibility = View.GONE
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        showDialog()
        conveyanceViewModel.getVendorUserList()
        conveyanceViewModel.vendorUserListResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog()
                    showToast(it.message)
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    hideDialog()
                    if (it.data != null) {
                        it.data.let { body ->
                            for (i in body.data.indices) {
                                vnedorName.add(body.data[i].vendorFirstName + " " + body.data[i].vendorLastName + "(" + body.data[i].phone + ")")
                                vendorID.add(body.data[i].phone)
                            }
                            // get Warehouse person  list
                            locationWherHouseName()
                        }
                    }
                }
            }
        }
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


        clickListner()
        /*   binding.tilStartReading.setVisibility(View.GONE);
        binding.tilEndReading.setVisibility(View.GONE);
        binding.tilKms.setVisibility(View.GONE);
        binding.tilCharges.setVisibility(View.GONE);
        binding.tilOtherExpense.setVisibility(View.GONE);*/binding!!.etEndReading.onFocusChangeListener =
            OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if (binding!!.etStartReading.text.toString()
                            .trim { it <= ' ' } != null && !binding!!.etStartReading.text.toString()
                            .trim { it <= ' ' }
                            .isEmpty() && !binding!!.etEndReading.text.toString().trim { it <= ' ' }
                            .isEmpty()
                    ) {
                        val startReadingLenth =
                            binding!!.etStartReading.text.toString().trim { it <= ' ' }
                                .toInt()
                        val endReadingLenth =
                            binding!!.etEndReading.text.toString().trim { it <= ' ' }
                                .toInt()
                        if (endReadingLenth > startReadingLenth) {
                            finalAmountCalculation("End")
                        } else {
                            Utility.showAlertDialog(
                                this@UploadVendorVoacherClass,
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
                            .toInt()
                    val endReadingLenth = binding!!.etEndReading.text.toString().trim { it <= ' ' }
                        .toInt()
                    if (endReadingLenth > startReadingLenth) {
                        finalAmountCalculation("End")
                    } else {
                        Utility.showAlertDialog(
                            this@UploadVendorVoacherClass,
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
                if (editable.length != 0) {
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
                                    .toInt() > 0) {
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
        binding!!.spinnerVendorType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) {
                        vendorTypeID = parent.getItemAtPosition(position).toString()
                        if (vendorTypeID.equals("Staff", ignoreCase = true)) {
                            binding!!.tilVehicleNo.visibility = View.VISIBLE
                            binding!!.tilFrom.visibility = View.VISIBLE
                            binding!!.tilTo.visibility = View.VISIBLE
                            binding!!.rlconvType.visibility = View.VISIBLE
                            binding!!.tilStartReading.visibility = View.VISIBLE
                            binding!!.tilEndReading.visibility = View.VISIBLE
                            binding!!.tilKms.visibility = View.VISIBLE
                            binding!!.tilCharges.visibility = View.VISIBLE
                            binding!!.tilLocation.visibility = View.VISIBLE
                            binding!!.llDocument.visibility = View.VISIBLE
                            binding!!.tilTotal.visibility = View.VISIBLE
                            binding!!.rlexptype.visibility = View.GONE
                            binding!!.rlvendorname.visibility = View.GONE
                        } else {
                            binding!!.tilVehicleNo.visibility = View.GONE
                            binding!!.tilFrom.visibility = View.GONE
                            binding!!.tilTo.visibility = View.GONE
                            binding!!.rlconvType.visibility = View.GONE
                            binding!!.tilStartReading.visibility = View.GONE
                            binding!!.tilEndReading.visibility = View.GONE
                            binding!!.tilKms.visibility = View.GONE
                            binding!!.tilCharges.visibility = View.GONE
                            binding!!.tilLocation.visibility = View.GONE
                            binding!!.tilOtherExpense.visibility = View.VISIBLE
                            binding!!.tilTotal.visibility = View.GONE
                            binding!!.rlexptype.visibility = View.VISIBLE
                            binding!!.rlvendorname.visibility = View.VISIBLE
                        }
                    } else {
                        binding!!.tilVehicleNo.visibility = View.GONE
                        binding!!.tilFrom.visibility = View.GONE
                        binding!!.tilTo.visibility = View.GONE
                        binding!!.rlconvType.visibility = View.GONE
                        binding!!.tilStartReading.visibility = View.GONE
                        binding!!.tilEndReading.visibility = View.GONE
                        binding!!.tilKms.visibility = View.GONE
                        binding!!.tilCharges.visibility = View.GONE
                        binding!!.tilLocation.visibility = View.GONE
                        binding!!.tilOtherExpense.visibility = View.GONE
                        binding!!.tilTotal.visibility = View.GONE
                        binding!!.rlexptype.visibility = View.GONE
                        binding!!.rlvendorname.visibility = View.GONE
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
        binding!!.etTotal.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (binding!!.etTotal.text.toString()
                        .trim { it <= ' ' } != null && !binding!!.etTotal.text.toString()
                        .trim { it <= ' ' }
                        .isEmpty()
                ) {
                    val otherExpenseValue = binding!!.etTotal.text.toString().trim { it <= ' ' }
                        .toInt()
                    showDialog()
                    conveyanceViewModel.expensionApprovedList(SelectedExpansionIDIs.toString(),otherExpenseValue.toString())
                    conveyanceViewModel.expensionApprovedList.observe(this){
                        when(it){
                            is NetworkResult.Error -> {
                                hideDialog()
                                showToast(it.message)
                            }
                            is NetworkResult.Loading -> {

                            }
                            is NetworkResult.Success -> {
                                if (it.data!=null){
                                    it.data.let { body->
                                        approveName.clear()
                                        approveID.clear()
                                        approveName = ArrayList()
                                        approveID = ArrayList()
                                        approveName.add(resources.getString(R.string.approved_by))
                                        approveID.add("0")
                                        binding!!.etLocation.visibility = View.GONE
                                        binding!!.etLocation.setText("" + body.data)
                                        binding!!.etLocation.isClickable = false
                                        binding!!.etLocation.isEnabled = false
                                        binding!!.etLocation.isFocusable = false
                                        binding!!.etLocation.isFocusableInTouchMode = false
                                        // Approved listing
                                        for (i in body.data.indices) {
                                            approveID.add(body.data[i].userId)
                                            approveName.add(body.data[i].firstName + " " + body.data[i].lastName + "(" + body.data[i].empId + ")")
                                        }
                                        SpinnerApproveByAdapter =
                                            ArrayAdapter(
                                                this@UploadVendorVoacherClass,
                                                R.layout.multiline_spinner_item,
                                                approveName
                                            )

                                        SpinnerApproveByAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)
                                        // Set Adapter in the spinner
                                        binding!!.spinnerApprovedBy.adapter = SpinnerApproveByAdapter
                                    }
                                }
                            }
                        }
                    }


                    /*   if (otherExpenseValue <= 5000) {
                                // Approved listing
                                SpinnerApproveByAdapter = new ArrayAdapter<String>(UploadVendorVoacherClass.this, R.layout.multiline_spinner_item, approveName) {
                                    //By using this method we will define how
                                    // the text appears before clicking a spinner
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        View v = super.getView(position, convertView, parent);
                                        ((TextView) v).setTextColor(Color.parseColor("#000000"));
                                        return v;
                                    }
    
                                    //By using this method we will define
                                    //how the listview appears after clicking a spinner
                                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                        View v = super.getDropDownView(position, convertView, parent);
                                        v.setBackgroundColor(Color.parseColor("#05000000"));
                                        ((TextView) v).setTextColor(Color.parseColor("#000000"));
                                        return v;
                                    }
                                };
                                SpinnerApproveByAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                                // Set Adapter in the spinner
                                binding.spinnerApprovedBy.setEnabled(false);
                                binding.spinnerApprovedBy.setClickable(false);
                                binding.spinnerApprovedBy.setFocusableInTouchMode(false);
                                binding.spinnerApprovedBy.setAdapter(SpinnerApproveByAdapter);
                                SelectedApproveIDIs = approveName.get(1);
                            } else {
                                // Approved listing
                                SpinnerApproveByAdapter = new ArrayAdapter<String>(UploadVendorVoacherClass.this, R.layout.multiline_spinner_item, approveName) {
                                    //By using this method we will define how
                                    // the text appears before clicking a spinner
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        View v = super.getView(position, convertView, parent);
                                        ((TextView) v).setTextColor(Color.parseColor("#000000"));
                                        return v;
                                    }
    
                                    //By using this method we will define
                                    //how the listview appears after clicking a spinner
                                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                        View v = super.getDropDownView(position, convertView, parent);
                                        v.setBackgroundColor(Color.parseColor("#05000000"));
                                        ((TextView) v).setTextColor(Color.parseColor("#000000"));
                                        return v;
                                    }
                                };
                                SpinnerApproveByAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                                // Set Adapter in the spinner
                                binding.spinnerApprovedBy.setEnabled(false);
                                binding.spinnerApprovedBy.setClickable(false);
                                binding.spinnerApprovedBy.setFocusableInTouchMode(false);
                                binding.spinnerApprovedBy.setAdapter(SpinnerApproveByAdapter);
                                SelectedApproveIDIs = approveName.get(2);
                            }*/
                }
            } else {
                /// Toast.makeText(MainActivity.this, "focused", Toast.LENGTH_LONG).show();
            }
        }
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


        // vendor Name listing
        SpinnerVendorAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, vnedorName)

        SpinnerVendorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter in the spinner
        searchableSpinner.setAdapter(SpinnerVendorAdapter)
        searchableSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                // selected item in the list
                if (position != 0) {
                    val EmpID = parentView.getItemAtPosition(position).toString()
                    for (i in vnedorName.indices) {
                        if (EmpID.equals(vnedorName.get(position), ignoreCase = true)) {
                            SelectedVendorIDIs = vendorID.get(position)
                            binding!!.rlexptype.visibility = View.VISIBLE
                            apiService.ExpensionList(SelectedVendorIDIs)

                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext { body ->
                                    for (i in body.data.indices) {
                                        expansionName.add(body.data[i].expensesName)
                                        expansionID.add(body.data[i].id)
                                    }
                                }

                            break
                        }
                    }
                    //  getExpensionList();
                } else {
                    expansionName.clear()
                    expansionID.clear()
                    expansionName.add(resources.getString(R.string.vendor_expension))
                    expansionID.add("0")
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
        // Expansion listing
        SpinnerExpansionAdapter = ArrayAdapter(this, R.layout.multiline_spinner_item, expansionName)

        SpinnerExpansionAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)
        // Set Adapter in the spinner
        binding!!.spinnerExpType.adapter = SpinnerExpansionAdapter
        binding!!.spinnerExpType.onItemSelectedListener =
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
                        for (i in expansionName.indices) {
                            if (EmpID.equals(expansionName.get(position), ignoreCase = true)) {
                                SelectedExpansionIDIs = expansionID.get(position)
                                binding!!.rlTerminal.visibility = View.VISIBLE
                                apiService.TerminalList(SelectedVendorIDIs)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnNext { body ->
                                        for (i in body.warehouse_detail.indices) {
                                            TerminalID.add(body.warehouse_detail[i].id)
                                            TerminalName.add(body.warehouse_detail[i].name + "(" + body.warehouse_detail[i].warehouse_code + ")")
                                        }
                                    }
                                    .subscribe()


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

    private fun locationWherHouseName() {
        conveyanceViewModel.getlevelwiselist()
        conveyanceViewModel.conveyanceListResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog()
                    showToast(it.message)
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    if (it.data != null) {
                        it.data.let { body ->
                            for (i in body.data.indices) {
                                if (body.request_count > 0) {
                                    binding!!.tvDone.isClickable = true
                                    binding!!.tvDone.isEnabled = true
                                    binding!!.tvDone.text =
                                        "Approval Request " + "(" + body.request_count + ")"
                                }
                                /*  approveID.add(body.getData().get(i).getUserId());
                                approveName.add(body.getData().get(i).getFirstName() + " " + body.getData().get(i).getLastName() + "(" + body.getData().get(i).getEmpId() + ")");*/
                            }
                        }
                    }
                }
            }
        }


    }

//    private val expensionList: Unit
//        private get() {
//            apiService.ExpensionList(SelectedVendorIDIs)!!
//                .enqueue(object : NetworkCallback<VendorExpensionNamePojo?>(
//                    activity
//                ) {
//                    protected override fun onSuccess(body: VendorExpensionNamePojo) {
//                        for (i in body.data.indices) {
//                            expansionName!!.add(body.data[i].expensesName)
//                            expansionID!!.add(body.data[i].id)
//                        }
//                    }
//                })
//        }

    fun finalAmountCalculation(action: String?) {
        try {
            if (binding!!.etStartReading.text.toString() != "" && binding!!.etEndReading.text.toString() != "" && binding!!.etStartReading.text.toString()
                    .toInt() > 0 && binding!!.etEndReading.text.toString().toInt() > 0
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
        binding!!.ivClose.setOnClickListener { finish() }
        binding!!.tvDone.setOnClickListener {
            finish()
        }
        binding!!.btnSubmit.setOnClickListener {
            Utility.showDecisionDialog(
                this@UploadVendorVoacherClass,
                getString(R.string.alert),
                "Are You Sure to Summit?",
                object : Utility.AlertCallback {
                    override fun callback() {
                        if (isValid) {
                            if (TextUtils.isEmpty(stringFromView(binding!!.userCommitmentDate))) {
                                Toast.makeText(
                                    this@UploadVendorVoacherClass,
                                    "Select Date",
                                    Toast.LENGTH_LONG
                                ).show()
                            } /*else if (packagingTypeID == null) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.slect_conv_type), Toast.LENGTH_LONG).show();
                            }*/ else if (fileReport == null) {
                                Toast.makeText(
                                    applicationContext,
                                    "upload Expansion Image 1",
                                    Toast.LENGTH_LONG
                                ).show()
                            } /*else if (fileCommudity == null) {
                                Toast.makeText(getApplicationContext(), R.string.end_meter_image, Toast.LENGTH_LONG).show();
                            }*/ else if (SelectedApproveIDIs == null) {
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
                            } /* else if (packagingTypeID.equalsIgnoreCase("Two-wheeler") || packagingTypeID.equalsIgnoreCase("Four-wheeler")) {
                                if (TextUtils.isEmpty(stringFromView(binding.etStartReading))) {
                                    Toast.makeText(UploadVendorVoacherClass.this, "Enter Start meter Reading ", Toast.LENGTH_LONG).show();
                                } else if (TextUtils.isEmpty(stringFromView(binding.etEndReading))) {
                                    Toast.makeText(UploadVendorVoacherClass.this, "Enter End meter Reading", Toast.LENGTH_LONG).show();
                                } else {
                                    onNext();
                                }
                            }*/ else {
                                onNext()
                            }
                        }
                    }
                })
        }
        binding!!.uploadReport.setOnClickListener {
            ReportsFileSelect = true
            CommudityFileSelect = false
            dispatchTakePictureIntent()
            //allImageSelector(REQUEST_CAMERA)
        }
        binding!!.uploadCommudity.setOnClickListener {
            ReportsFileSelect = false
            CommudityFileSelect = true
            dispatchTakePictureIntent()
            //  callImageSelector(REQUEST_CAMERA)
        }
        binding!!.ReportsImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@UploadVendorVoacherClass,
                R.layout.popup_photo_full,
                view,
                reportFile,
                null
            )
        }
        binding!!.CommudityImage.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@UploadVendorVoacherClass,
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
            this@UploadVendorVoacherClass, date, calender
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
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding!!.userCommitmentDate.setText(sdf.format(calender!!.time).toString())
    }

    override fun dispatchTakePictureIntent() {
        ImagePicker.with(this)
            .setDismissListener {
                Toast.makeText(this@UploadVendorVoacherClass,"Please Select Image to upload",Toast.LENGTH_SHORT)
            }
            .galleryOnly()    //User can only select image from Gallery
            .start()
        permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build().send() {
            when (it.first()) {
                is PermissionStatus.Denied.Permanently -> {}
                is PermissionStatus.Denied.ShouldShowRationale -> {}
                is PermissionStatus.Granted -> {

//                    val intent = Intent(Intent.ACTION_GET_CONTENT)
//                    intent.type = "image/*"
//                    if (intent.resolveActivity(packageManager) != null) {
//                        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
//                    }
                }

                is PermissionStatus.RequestRequired -> {
//                    val intent = Intent(Intent.ACTION_GET_CONTENT)
//                    intent.type = "image/*"
//                    if (intent.resolveActivity(packageManager) != null) {
//                        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
//                    }
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

    private fun callImageSelector(requestCamera: Int) {
        options = Options.init()
            .setRequestCode(requestCamera) //Request code for activity results
            .setCount(1)
            .setFrontfacing(false)
            .setExcludeVideos(false)
            .setVideoDurationLimitinSeconds(60)
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientation
            .setPath("/apnagodam/images")
        Pix.start(this@UploadVendorVoacherClass, options) //Custom Path For media Storage
    }

    // update file
    fun onNext() {
        var KanthaImage = ""
        var CommudityFileSelectImage = ""
        if (fileReport != null) {
            KanthaImage = "" + Utility.transferImageToBase64(fileReport)
        }
        if (fileCommudity != null) {
            CommudityFileSelectImage = "" + Utility.transferImageToBase64(fileCommudity)
        }
        apiService.doVendorCreateConveyance(
            CreateVendorConveyancePostData(
                SelectedVendorIDIs,
                stringFromView(binding!!.etTotal),
                SelectedTerminalIDIs,
                SelectedExpansionIDIs,
                stringFromView(
                    binding!!.userCommitmentDate
                ),
                stringFromView(binding!!.notes),
                KanthaImage,
                CommudityFileSelectImage,
                SelectedApproveIDIs
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { body ->
                Utility.showAlertDialog(
                    this@UploadVendorVoacherClass,
                    getString(R.string.alert),
                    body.message
                ) { startActivityAndClear(MyVendorVoacherListClass::class.java) }
            }.subscribe()

    }

    val isValid: Boolean
        get() {
            if (TextUtils.isEmpty(stringFromView(binding!!.etTotal))) {
                return Utility.showEditTextError(binding!!.tilTotal, R.string.enter_total)
            } else if (TextUtils.isEmpty(stringFromView(binding!!.notes))) {
                return Utility.showEditTextError(binding!!.notes, R.string.purpose)
            }
            /*  if (TextUtils.isEmpty(stringFromView(binding.etVehicleNo))) {
            return Utility.showEditTextError(binding.tilVehicleNo, R.string.vehicle_no_conv);
        } else if (TextUtils.isEmpty(stringFromView(binding.etFrom))) {
            return Utility.showEditTextError(binding.tilFrom, R.string.from_place_con_list);
        } else if (TextUtils.isEmpty(stringFromView(binding.etTo))) {
            return Utility.showEditTextError(binding.tilTo, R.string.to_place_con_list);
        } else if (TextUtils.isEmpty(stringFromView(binding.etLocation))) {
            return Utility.showEditTextError(binding.tilLocation, R.string.enter_location);
        }*/return true
        }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val userDetails = SharedPreferencesRepository.getDataManagerInstance().user

        var stampMap = mapOf(
            "current_location" to "$currentLocation",
            "emp_code" to userDetails.emp_id, "emp_name" to userDetails.fname
        )
        if (data != null && data.data != null) {
            if (ReportsFileSelect) {
                val thumbnail = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)

                var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                    File(compressImage(bitmapToFile(thumbnail!!).path)),
                    stampMap
                )
                ReportsFileSelect = false
                CommudityFileSelect = false
                fileReport = File(compressImage(bitmapToFile(stampedBitmap).path))
                val uri = Uri.fromFile(fileReport)
                reportFile = uri.toString()
                binding!!.ReportsImage.setImageURI(uri)
            } else if (CommudityFileSelect) {
                val thumbnail = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)

                var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                    File(compressImage(bitmapToFile(thumbnail!!).path)),
                    stampMap
                )
                ReportsFileSelect = false
                CommudityFileSelect = false
                fileCommudity = File(compressImage(bitmapToFile(stampedBitmap).path))
                val uri = Uri.fromFile(fileCommudity)
                commudityFile = uri.toString()
                binding!!.CommudityImage.setImageURI(uri)
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
