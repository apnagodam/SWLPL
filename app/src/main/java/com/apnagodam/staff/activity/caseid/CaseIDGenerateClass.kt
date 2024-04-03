package com.apnagodam.staff.activity.caseid

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.CreateCaseIDPostData
import com.apnagodam.staff.Network.Request.StackPostData
import com.apnagodam.staff.Network.Response.StackRequestResponse.InwardRequestDatum
import com.apnagodam.staff.Network.Response.StackRequestResponse.OutwardRequestDatum
import com.apnagodam.staff.Network.viewmodel.CaseIdViewModel
import com.apnagodam.staff.Network.viewmodel.GatePassViewModel
import com.apnagodam.staff.Network.viewmodel.LeadsViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.adapter.CustomerNameAdapter
import com.apnagodam.staff.databinding.ActivityCaseIdBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.AllUserListPojo
import com.apnagodam.staff.module.CommodityResponseData
import com.apnagodam.staff.module.StackListPojo
import com.apnagodam.staff.module.TerminalListPojo
import com.apnagodam.staff.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CaseIDGenerateClass() : BaseActivity<ActivityCaseIdBinding?>(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    lateinit var SpinnerStackAdapter: ArrayAdapter<String>
    lateinit var SpinnerCommudityAdapter: ArrayAdapter<String>
    lateinit var spinnerTeerminalAdpter: ArrayAdapter<String>
    lateinit var SpinnerUserListAdapter: ArrayAdapter<String>
    lateinit var spinnerEmployeeAdpter: ArrayAdapter<String>
    var stackID: String? = null
    var commudityID: String? = null
    var TerminalID: String? = null
    var selectPurpose: String? = null
    var selectInOUt: String? = null
    var seleectCoustomer: String? = null
    var selectConvertOther: String? = null
    var UserID: String? = null
    lateinit var StackName: ArrayList<String>
    lateinit var CommudityName: ArrayList<String>
    lateinit var TerminalName: ArrayList<String>
    lateinit var TerminalsID: List<String>
    lateinit var CustomerName: ArrayList<String>
    lateinit var CustomerID: ArrayList<String>
    lateinit var LeadGenerateOtherName: ArrayList<String>
    lateinit var LeadGenerateOtheID: ArrayList<String>
    lateinit var data: ArrayList<TerminalListPojo.Datum>
    lateinit var Userdata: ArrayList<AllUserListPojo.User>
    lateinit var Stackdata: ArrayList<StackListPojo.Datum>
    lateinit var commodityData: ArrayList<CommodityResponseData.Data>
    val gatePassViewModel by viewModels<GatePassViewModel>()

    var otpData = mapOf<String, Any>()
    val leadsViewModel by viewModels<LeadsViewModel>()
    val caseIdViewModel by viewModels<CaseIdViewModel>()
    var outwardsStack: OutwardRequestDatum? = null
    var inwardsStack: InwardRequestDatum? = null
    override fun getLayoutResId(): Int {

        return R.layout.activity_case_id
    }

    //
//    TerminalID,
//    selectInOUt,
//    seleectCoustomer,
//    commudityID,
//    "",
//    stackID,
//    "",
    override fun setUp() {
        var userDetails = SharedPreferencesRepository.getDataManagerInstance().user
        if (intent.getSerializableExtra("outwards_stack") != null) {

            outwardsStack =
                intent.getSerializableExtra("outwards_stack") as OutwardRequestDatum?
            otpData = mapOf(
                "driver_number" to outwardsStack!!.driverNumber,
                "otp" to "",
                "in_out" to outwardsStack!!.inOutType,
                "stack_id" to outwardsStack!!.stackId
            )

            TerminalID = outwardsStack!!.terminalId.toString()
            selectInOUt = outwardsStack!!.inOutType
            seleectCoustomer = outwardsStack!!.userNumber.toString()
            commudityID = outwardsStack!!.commodityId.toString()
            stackID = outwardsStack!!.stackId.toString()
            binding!!.tilTerminal.editText!!.setText(outwardsStack!!.terminalName)
            binding!!.tilCommodity.editText!!.setText(outwardsStack!!.commodity)
            binding!!.tilUser.editText!!.setText(outwardsStack!!.userName)
            binding!!.tilStackNo.editText!!.setText(outwardsStack!!.stackNumber)
            binding!!.tilInOut.editText!!.setText(outwardsStack!!.inOutType)
            binding!!.etCustomerWeight!!.setText(outwardsStack!!.wgtInKg.toString())
            binding!!.etCustomerWeightQuantal!!.setText(outwardsStack!!.wgtInQtl)
            binding!!.etCustomerVehicle!!.setText(outwardsStack!!.vehicleNumber)
            binding!!.etDriverMobileNumber!!.setText(outwardsStack!!.driverNumber)


//            binding!!.tilTerminal.editText!!.setText(outwardsStack!!.terminalName)

        } else if (intent.getSerializableExtra("inwards_stack") != null) {
            inwardsStack =
                intent.getSerializableExtra("inwards_stack") as InwardRequestDatum?

            otpData = mapOf(
                "driver_number" to inwardsStack!!.driverNumber,
                "otp" to "",
                "in_out" to inwardsStack!!.inOutType,
                "stack_id" to inwardsStack!!.stackId
            )
            TerminalID = inwardsStack!!.terminalId.toString()
            selectInOUt = inwardsStack!!.inOutType
            seleectCoustomer = inwardsStack!!.userNumber.toString()
            commudityID = inwardsStack!!.commodityId.toString()
            stackID = inwardsStack!!.stackId.toString()
            binding!!.tilTerminal.editText!!.setText(inwardsStack!!.terminalName)
            binding!!.tilCommodity.editText!!.setText(inwardsStack!!.commodity)
            binding!!.tilUser.editText!!.setText(inwardsStack!!.userName)
            binding!!.tilStackNo.editText!!.setText(inwardsStack!!.stackNumber)
            binding!!.tilInOut.editText!!.setText(inwardsStack!!.inOutType)
            binding!!.etCustomerWeight!!.setText(inwardsStack!!.wgtInKg.toString())
            binding!!.etCustomerWeightQuantal!!.setText(inwardsStack!!.wgtInQtl)
            binding!!.etCustomerVehicle!!.setText(inwardsStack!!.vehicleNumber)
            binding!!.etDriverMobileNumber!!.setText(inwardsStack!!.driverNumber)
        }
        binding!!.etCustomerGatepass.isEnabled = false
        binding!!.etCustomerGatepass.isFocusable = false
        binding!!.etCustomerGatepass.isClickable = false
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.RLStack.visibility = View.GONE
        terminalListLevel()
        clickListner()
        StackName = ArrayList()
        CommudityName = ArrayList()
        TerminalName = ArrayList()
        TerminalsID = ArrayList()
        CustomerName = ArrayList()
        CustomerID = ArrayList()
        LeadGenerateOtherName = ArrayList()
        LeadGenerateOtheID = ArrayList()
        CommudityName.add(resources.getString(R.string.commodity_name))
        TerminalName.add(resources.getString(R.string.terminal_name1))
        CustomerName.add(resources.getString(R.string.select_coustomer))
        LeadGenerateOtherName.add("If Lead Converted By Other")
        binding!!.sendOtp.setOnClickListener {
            if (binding!!.etDriverMobileNumber.text!!.isNotEmpty()) {
                onSendOtp()

            } else {
                showToast("Driver not available ")
            }
        }
        binding!!.reSendOtp.setOnClickListener {
            if (binding!!.etDriverMobileNumber.text!!.isNotEmpty()) {
                onSendOtp()

            } else {
                showToast("Driver not available ")
            }
        }
//        binding!!.etOtp.doOnTextChanged { text, start, before, count ->
//            if (text!!.length == 6) {
//                binding!!.tilOtp.isEnabled = false;
//                binding!!.tilOtp.isFocusable = false
//                verifyOtp()
//            }
//        }
//

        binding!!.verifyOtp.setOnClickListener {
            verifyOtp()
        }
        try {
            //  setValueOnSpinner()
            //  setValueOnSpinner()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding!!.etCustomerWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length != 0 && charSequence != "") {
                    var finalWeightQTl = charSequence.toString().trim { it <= ' ' }.toDouble() / 100
                    finalWeightQTl = Utility.round(finalWeightQTl, 2)
                    binding!!.etCustomerWeightQuantal.setText("" + finalWeightQTl)
                } else {
                    binding!!.etCustomerWeightQuantal.setText("")
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        caseIdViewModel.driverOtpResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog()
                    binding!!.verifyOtp.isEnabled = true

                }

                is NetworkResult.Loading -> {
                    binding!!.verifyOtp.isEnabled = false

                }

                is NetworkResult.Success -> {

                    if (it.data != null) {

                        if (it.data.status == "1") {
                            if (it.data.type == "Match") {
                                binding!!.btnCreateeCase.visibility = View.VISIBLE
                                binding!!.btnCreateeCase.isEnabled = true;
                                binding!!.verifyOtp.isEnabled = false
                                binding!!.etOtp.isFocusable = false;
                                binding!!.etOtp.isEnabled = false;

                                binding!!.verifyOtp.visibility = View.GONE
                            }
                        } else {
                            showToast(it.data.message)
                        }
                    }
                    hideDialog()


                }
            }
        }
        binding!!.btnCreateeCase.setOnClickListener {
            verifyAndCreateCaseId()

        }
        caseIdViewModel.createCaseIdResponse.observe(this@CaseIDGenerateClass)
        {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog()
                    binding!!.btnCreateeCase.isEnabled = true;
                    binding!!.verifyOtp.isEnabled = true;
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data != null) {
                        if (it.data.status == "1") {
                            binding!!.btnCreateeCase.isEnabled = true;
                            binding!!.verifyOtp.isEnabled = true;
                            binding!!.btnCreateeCase.isEnabled = true;
                            binding!!.verifyOtp.isEnabled = true;

                            startActivityAndClear(StaffDashBoardActivity::class.java)
                            showToast(it.data.message)
                        } else {
                            startActivityAndClear(StaffDashBoardActivity::class.java)
                            showToast(it.data.message)
                        }
                    }
                    hideDialog()
                }
            }
        }
    }

    val isOTPValid: Boolean
        get() {
            if (TextUtils.isEmpty(stringFromView(binding!!.etDriverMobileNumber))) {
                return Utility.showEditTextError(binding!!.itlMobileNumber, "Enter Driver Phone")
            }
            return true
        }

    private fun setValueOnSpinner() {
        setAllData()


        SpinnerUserListAdapter =
            ArrayAdapter(this, R.layout.multiline_spinner_item, (CustomerName))


        SpinnerUserListAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)
        //        // Set Adapter in the spinner
        binding!!.spinnerUserName.adapter = SpinnerUserListAdapter
        binding!!.spinnerUserName.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    //                // selected item in the list
                    if (position != 0) {
                        try {
                            UserID = parentView.getItemAtPosition(position).toString()
                            val part: Array<String> = UserID!!.split("(?<=\\D)(?=\\d)".toRegex())
                                .dropLastWhile { it.isEmpty() }
                                .toTypedArray()

                            seleectCoustomer = (part.get(1))

                            commodityList()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    // SpinnerUserListAdapter.notifyDataSetChanged();
                }

                //
                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    //                // your code here
                }
            }


        spinnerTeerminalAdpter =

            ArrayAdapter(this, R.layout.multiline_spinner_item, (TerminalName)!!)

        spinnerTeerminalAdpter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)
        // Set Adapter in the spinner
        binding!!.spinnerTerminal.adapter = spinnerTeerminalAdpter
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
                        var data = data[position - 1]
                        val presentMeterStatusID: String =
                            parentView.getItemAtPosition(position).toString()
                        if (presentMeterStatusID.contains(data.name)) {
                            TerminalID = data.id.toString()
                            //                    binding.rlUser.setVisibility(View.VISIBLE);
                            binding!!.inoutRl.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }

        // spinner purpose
        binding!!.spinnerPurpose.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) selectPurpose = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // can leave this empty
                }
            }

        // spinner in/out
        binding!!.spinnerInOut.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) {
                        if (TerminalID == null) {
                            Toast.makeText(
                                this@CaseIDGenerateClass,
                                resources.getString(R.string.terminal_name),
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            selectInOUt = parent.getItemAtPosition(position).toString()
                            binding!!.rlUser.visibility = View.VISIBLE
                            userList
                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // can leave this empty
                }
            }


        SpinnerStackAdapter = ArrayAdapter(this, R.layout.multiline_spinner_item, (StackName)!!)

        binding!!.spinnerStack.adapter = SpinnerStackAdapter
        binding!!.spinnerStack.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    // selected item in the list
                    //  getstack()
                    if (position != 0) {
                        val presentMeterStatusID: String =
                            parentView.getItemAtPosition(position).toString()
                        var stackData = Stackdata!!.get(position - 1);
                        stackID = stackData.id

                        binding!!.etCustomerWeight.setText(
                            (stackData.requestWeight.toString().toInt() * 100).toString()
                        )
                        binding!!.etCustomerVehicle.setText("" + stackData.vehicle_no)
                        binding!!.etDriverMobileNumber.setText(stackData.driverNumber)
                        binding!!.etCustomerVehicle.isClickable = false
                        binding!!.etCustomerVehicle.isFocusable = false
                        binding!!.etCustomerVehicle.isEnabled = false
                        binding!!.etCustomerVehicle.isFocusableInTouchMode = false
                        if (selectInOUt!!.contains("IN")) {

                        }

//                        else {
//                            binding!!.etCustomerWeight.setText("" + stackData.requestWeight)
//                            binding!!.etCustomerVehicle.setText("" + stackData.vehicle_no)
//                            binding!!.etDriverMobileNumber.setText(stackData.driverNumber)
//                            binding!!.etCustomerVehicle.isClickable = true
//                            binding!!.etCustomerVehicle.isFocusable = true
//                            binding!!.etCustomerVehicle.isEnabled = true
//                            binding!!.etCustomerVehicle.isFocusableInTouchMode = true
//                        }


                    } else {
                        stackID = null
                        binding!!.etCustomerVehicle.isClickable = true
                        binding!!.etCustomerVehicle.isFocusable = true
                        binding!!.etCustomerVehicle.isEnabled = true
                        binding!!.etCustomerVehicle.isFocusableInTouchMode = true
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }
    }

    fun onSendOtp() {

        var stackId = otpData.get("stack_id").toString()
        var otp = otpData.get("otp").toString()
        var driverNumber = otpData.get("driver_number").toString()
        var inOut = otpData.get("in_out").toString()
        caseIdViewModel.driverOtp(driverNumber, stackId, inOut)
        caseIdViewModel.driverOtpResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {

                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    if (it.data!!.type == "Send") {
                        binding!!.tilOtp.visibility = View.VISIBLE
                        binding!!.sendOtp.visibility = View.GONE
                        binding!!.verifyOtp.visibility = View.VISIBLE
                        showToast(it.data!!.message)
                    }


                    binding!!.reSendOtp.visibility = View.VISIBLE

                }
            }
        }
    }

    fun verifyOtp() {
        showDialog()
        var stackId = otpData.get("stack_id").toString()
        var otp = otpData.get("otp").toString()
        var driverNumber = otpData.get("driver_number").toString()
        var inOut = otpData.get("in_out").toString()
        caseIdViewModel.driverOtp(driverNumber, stackId, inOut, binding!!.etOtp.text.toString())
        binding!!.verifyOtp.isEnabled = false

    }

    private fun getstack() {
        caseIdViewModel.getStackList(
            StackPostData(
                commudityID,
                TerminalID,
                seleectCoustomer,
                selectInOUt
            )
        )

        caseIdViewModel.stackReponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    showToast(it.message)
                }

                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {

                    StackName!!.clear()
                    stackID = null
                    binding!!.spinnerStack.prompt = ""
                    StackName!!.add("Stack Number")
                    Stackdata = it.data!!.data as ArrayList<StackListPojo.Datum>
                    for (i in Stackdata.indices) {
                        StackName!!.add(Stackdata.get(i).stackNumber)
                        //SpinnerStackAdapter.notifyDataSetChanged();
                    }
                    SpinnerStackAdapter = ArrayAdapter(
                        this@CaseIDGenerateClass,
                        R.layout.multiline_spinner_item,
                        (StackName)!!
                    )
                    //                        object : ArrayAdapter<String?>(
                    //                        this@CaseIDGenerateClass,
                    //                        R.layout.multiline_spinner_item,
                    //                        (StackName)!!
                    //                    ) {
                    //                        override fun getView(
                    //                            position: Int,
                    //                            convertView: View?,
                    //                            parent: ViewGroup
                    //                        ): View {
                    //                            val v = super.getView(position, convertView, parent)
                    //                            (v as TextView).setTextColor(Color.parseColor("#000000"))
                    //                            return v
                    //                        }
                    //
                    //                        override fun getDropDownView(
                    //                            position: Int,
                    //                            convertView: View,
                    //                            parent: ViewGroup
                    //                        ): View {
                    //                            val v = super.getDropDownView(position, convertView, parent)
                    //                            v.setBackgroundColor(Color.parseColor("#05000000"))
                    //                            (v as TextView).setTextColor(Color.parseColor("#000000"))
                    //                            return v
                    //                        }
                    //                    }
                    SpinnerStackAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)

                    binding!!.spinnerStack.adapter = SpinnerStackAdapter
                }
            }
        }


    }

    fun commodityList() {
        caseIdViewModel.getCommodities(TerminalID!!, selectInOUt!!, seleectCoustomer!!)

        caseIdViewModel.commoditiesResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    CommudityName!!.clear()
                    commudityID = null
                    binding!!.spinnerCommudity.prompt = ""
                    CommudityName!!.add(resources.getString(R.string.commodity_name))
                    commodityData = it.data!!.dataList as ArrayList<CommodityResponseData.Data>
                    for (i in commodityData.indices) {
                        CommudityName!!.add(commodityData.get(i).category)
                        //SpinnerStackAdapter.notifyDataSetChanged();
                    }
                    SpinnerCommudityAdapter =
                        ArrayAdapter(
                            this@CaseIDGenerateClass,
                            R.layout.multiline_spinner_item,
                            (CommudityName)!!
                        )
                    @SuppressLint("SuspiciousIndentation")
                    object : ArrayAdapter<String?>(
                        this@CaseIDGenerateClass,
                        R.layout.multiline_spinner_item,
                    ) {
                        override fun getView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val v = super.getView(position, convertView, parent)
                            (v as TextView).setTextColor(Color.parseColor("#000000"))
                            return v
                        }

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
                    SpinnerCommudityAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)
                    binding!!.spinnerCommudity.adapter = SpinnerCommudityAdapter
                    binding!!.spinnerCommudity.onItemSelectedListener =
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
                                    for (i in commodityData.indices) {
                                        if (presentMeterStatusID.equals(
                                                commodityData.get(i).category,
                                                ignoreCase = true
                                            )
                                        ) {
                                            commudityID = commodityData.get(i).id
                                            binding!!.RLStack.visibility = View.VISIBLE
                                            getstack()
                                            break
                                        }
                                    }
                                }
                            }

                            override fun onNothingSelected(parentView: AdapterView<*>?) {
                                // your code here
                            }
                        }
                    binding!!.RLCommodity.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun showCustomerNamePopup(getList: List<String>) {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.decorView.setBackgroundResource(android.R.color.transparent)
            dialog.window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            dialog.setContentView(R.layout.customer_list_popup)
            dialog.setCanceledOnTouchOutside(false)
            val recyclerViewCustomerPopup =
                dialog.findViewById<RecyclerView>(R.id.recyclerViewCustomerPopup)
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recyclerViewCustomerPopup.layoutManager = layoutManager
            recyclerViewCustomerPopup.setHasFixedSize(true)
            recyclerViewCustomerPopup.isNestedScrollingEnabled = false
            val customerNameAdapter = CustomerNameAdapter(getList, this)
            recyclerViewCustomerPopup.adapter = customerNameAdapter
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setAllData() {

        object : Thread() {
            override fun run() {
                try {
                    runOnUiThread(object : Runnable {
                        override fun run() {
                            object : Thread() {
                                override fun run() {
                                    for (i in SharedPreferencesRepository.getDataManagerInstance().commudity.indices) {
                                        CommudityName!!.add(SharedPreferencesRepository.getDataManagerInstance().commudity[i].category + "(" + SharedPreferencesRepository.getDataManagerInstance().commudity[i].commodityType + ")")
                                    }
                                    hideDialog()
                                }
                            }.start()
                        }
                    })
                    sleep(30)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    private fun terminalListLevel() {
        leadsViewModel.getTerminalList()
        leadsViewModel.response.observe(this) {
            when (it) {
                is NetworkResult.Error -> hideDialog()
                is NetworkResult.Loading -> showDialog()
                is NetworkResult.Success -> {
                    data = it.data!!.data
                    for (i in data.indices) {
                        TerminalName.add(data.get(i).name + "(" + data.get(i).warehouseCode + ")")
                    }
                }
            }

            //            getUser
        }

    }

    private val userList: Unit
        private get() {
            caseIdViewModel.getUsersList(TerminalID.toString(), selectInOUt.toString())
            caseIdViewModel.usersListResponse.observe(this) {
                when (it) {
                    is NetworkResult.Error -> {

                    }

                    is NetworkResult.Loading -> {

                    }

                    is NetworkResult.Success -> {
                        if (it.data != null) {
                            if (it.data.status == "1") {
                                Userdata = it.data.users as ArrayList<AllUserListPojo.User>
                                for (i in Userdata.indices) {
                                    CustomerName!!.add(Userdata.get(i).fname + "(" + Userdata.get(i).phone)
                                }
                            } else {
                                showToast(it.data.message)
                            }
                        }
                    }
                }
            }


        }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener(this)
        binding!!.btnCreateeCase.setOnClickListener(this)
        binding!!.tvDone.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_close -> onBackPressedDispatcher.onBackPressed()
            R.id.tv_done -> startActivity(CaseListingActivity::class.java)
            R.id.btn_createe_case -> verifyAndCreateCaseId()
        }
    }

    fun verifyAndCreateCaseId() {
        binding!!.btnCreateeCase.isEnabled = false;
        binding!!.verifyOtp.isEnabled = false;
        if (isValid) {
            if (TerminalID == null) {
                Toast.makeText(
                    this@CaseIDGenerateClass,
                    resources.getString(R.string.terminal_name),
                    Toast.LENGTH_LONG
                ).show()
                binding!!.btnCreateeCase.isEnabled = true;
                binding!!.verifyOtp.isEnabled = true;


            } else if (selectInOUt == null) {
                Toast.makeText(
                    this@CaseIDGenerateClass,
                    resources.getString(R.string.select_in_out),
                    Toast.LENGTH_LONG
                ).show()
                binding!!.btnCreateeCase.isEnabled = true;
                binding!!.verifyOtp.isEnabled = true;


            } else if (seleectCoustomer == null) {
                Toast.makeText(
                    this@CaseIDGenerateClass,
                    resources.getString(R.string.select_coustomer),
                    Toast.LENGTH_LONG
                ).show()
                binding!!.btnCreateeCase.isEnabled = true;
                binding!!.verifyOtp.isEnabled = true;


            } else if (commudityID == null) {
                Toast.makeText(
                    this@CaseIDGenerateClass,
                    resources.getString(R.string.commudity_name),
                    Toast.LENGTH_LONG
                ).show()
                binding!!.btnCreateeCase.isEnabled = true;
                binding!!.verifyOtp.isEnabled = true;


            } else if (stackID == null) {
                Toast.makeText(
                    this@CaseIDGenerateClass,
                    "Select Stack Number",
                    Toast.LENGTH_LONG
                ).show()
                binding!!.btnCreateeCase.isEnabled = true;
                binding!!.verifyOtp.isEnabled = true;

            } else {
                showDialog()
                caseIdViewModel.doCreateCaseId(
                    CreateCaseIDPostData(
                        TerminalID,
                        selectInOUt,
                        seleectCoustomer,
                        commudityID,
                        "",
                        stackID,
                        "",
                        stringFromView(binding!!.etCustomerWeight),
                        stringFromView(
                            binding!!.etCustomerWeightQuantal
                        ),
                        stringFromView(binding!!.etCustomerVehicle),
                        stringFromView(binding!!.etSpotToken),
                        stringFromView(binding!!.etDriverMobileNumber)
                    )
                )



            }
        }
    }

    val isValid: Boolean
        get() {
            if (TextUtils.isEmpty(stringFromView(binding!!.etCustomerWeight))) {
                return Utility.showEditTextError(binding!!.tilCustomerWeight, R.string.weight_kg)
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etCustomerVehicle))) {
                return Utility.showEditTextError(binding!!.tilCustomerVehicle, R.string.vehicle_no)
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etDriverMobileNumber))) {
                return Utility.showEditTextError(binding!!.itlMobileNumber, R.string.vehicle_no)
            }

            return true
        }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {}
    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
}
fun <T> debounce(
    delayMillis: Long = 300L,
    scope: CoroutineScope,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (debounceJob == null) {
            debounceJob = scope.launch {
                action(param)
                delay(delayMillis)
                debounceJob = null
            }
        }
    }
}
fun <T> throttleFirst(
    skipMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var throttleJob: Job? = null
    return { param: T ->
        if (throttleJob?.isCompleted != false) {
            throttleJob = coroutineScope.launch {
                destinationFunction(param)
                delay(skipMs)
            }
        }
    }
}