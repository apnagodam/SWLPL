package com.apnagodam.staff.activity.caseid

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.CreateCaseIDPostData
import com.apnagodam.staff.Network.Request.StackPostData
import com.apnagodam.staff.Network.Response.LoginResponse
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
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
@AndroidEntryPoint
class CaseIDGenerateClass() : BaseActivity<ActivityCaseIdBinding?>(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {
   lateinit var SpinnerStackAdapter: ArrayAdapter<String>
    lateinit  var SpinnerCommudityAdapter: ArrayAdapter<String>
    lateinit   var spinnerTeerminalAdpter: ArrayAdapter<String>
    lateinit   var SpinnerUserListAdapter: ArrayAdapter<String>
    lateinit  var spinnerEmployeeAdpter: ArrayAdapter<String>
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
    lateinit  var TerminalsID: List<String>
    lateinit  var CustomerName: ArrayList<String>
    lateinit  var CustomerID: ArrayList<String>
    lateinit   var LeadGenerateOtherName: ArrayList<String>
    lateinit  var LeadGenerateOtheID: ArrayList<String>
    lateinit  var data: ArrayList<TerminalListPojo.Datum>
    lateinit   var Userdata: ArrayList<AllUserListPojo.User>
    lateinit   var Stackdata: ArrayList<StackListPojo.Datum>
    lateinit var commodityData: ArrayList<CommodityResponseData.Data>

    val leadsViewModel by viewModels<LeadsViewModel>()
    override fun getLayoutResId(): Int {

        return R.layout.activity_case_id
    }

    override fun setUp() {
        binding!!.etCustomerGatepass.isEnabled = false
        binding!!.etCustomerGatepass.isFocusable = false
        binding!!.etCustomerGatepass.isClickable = false
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        /* binding.RLCommodity.setVisibility(View.GONE);
        binding.RLStack.setVisibility(View.GONE);*/binding!!.RLStack.visibility = View.GONE
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
        try {
            setValueOnSpinner()
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
    }

    private fun setValueOnSpinner() {
        setAllData()

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getCommudity().size(); i++) {
//                    CommudityName.add(SharedPreferencesRepository.getDataManagerInstance().getCommudity().get(i).getCategory());
//                }
//                for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().GetTerminal().size(); i++) {
//                    TerminalName.add(SharedPreferencesRepository.getDataManagerInstance().GetTerminal().get(i).getName()
//                            + "(" + SharedPreferencesRepository.getDataManagerInstance().GetTerminal().get(i).getWarehouseCode() + ")");
//                }
//                for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getuserlist().size(); i++) {
//                    CustomerName.add(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getFname());
//                }
//                for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getEmployee().size(); i++) {
//                    if (SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getDesignationId().equalsIgnoreCase("6") ||
//                            SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getDesignationId().equalsIgnoreCase("7")) {
//                        LeadGenerateOtherName.add(SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getFirstName() + " " +
//                                SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getLastName() +
//                                "(" + SharedPreferencesRepository.getDataManagerInstance().getEmployee().get(i).getEmpId() + ")");
//                    }
//                }
//            }
//        });

        // UserList listing
        SpinnerUserListAdapter = ArrayAdapter(this, R.layout.multiline_spinner_item, (CustomerName)!!)

//        SpinnerUserListAdapter = object :
//            ArrayAdapter<String?>(this, R.layout.multiline_spinner_item, (CustomerName)!!) {
//            //            //By using this method we will define how
//            //            // the text appears before clicking a spinner
//            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//                val v = super.getView(position, convertView, parent)
//                (v as TextView).setTextColor(Color.parseColor("#000000"))
//                return v
//            }
//
//            //
//            //            //By using this method we will define
//            //            //how the listview appears after clicking a spinner
//            override fun getDropDownView(
//                position: Int,
//                convertView: View,
//                parent: ViewGroup
//            ): View {
//                val v = super.getDropDownView(position, convertView, parent)
//                v.setBackgroundColor(Color.parseColor("#05000000"))
//                (v as TextView).setTextColor(Color.parseColor("#000000"))
//                return v
//            }
//        }
        //
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
                            //    System.out.println(part[0]);
                            // seleectCoustomer= String.valueOf((""+Integer.parseInt(part[1])).split("\\)"));
                            seleectCoustomer = (part.get(1))
                            //                binding.inoutRl.setVisibility(View.VISIBLE);
                            //                    getstack();
                            commodityList
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

        // Employee listing
        object : Thread() {
            override fun run() {
                try {
                    runOnUiThread(Runnable {
                        object : Thread() {
                            override fun run() {
                                for (i in SharedPreferencesRepository.getDataManagerInstance().employee.indices) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().employee[i].designationId.equals(
                                            "6",
                                            ignoreCase = true
                                        ) ||
                                        SharedPreferencesRepository.getDataManagerInstance().employee[i].designationId.equals(
                                            "7",
                                            ignoreCase = true
                                        )
                                    ) {
                                        LeadGenerateOtherName!!.add(
                                            (SharedPreferencesRepository.getDataManagerInstance().employee[i].firstName + " " +
                                                    SharedPreferencesRepository.getDataManagerInstance().employee[i].lastName +
                                                    "(" + SharedPreferencesRepository.getDataManagerInstance().employee[i].empId + ")")
                                        )
                                    }
                                }
                            }
                        }.start()
                    })
                    sleep(30)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
        spinnerEmployeeAdpter = ArrayAdapter(this,
            R.layout.multiline_spinner_item,
            (LeadGenerateOtherName)!!)

//            object : ArrayAdapter<String?>(
//            this,
//            R.layout.multiline_spinner_item,
//            (LeadGenerateOtherName)!!
//        ) {
//            //By using this method we will define how
//            // the text appears before clicking a spinner
//            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//                val v = super.getView(position, convertView, parent)
//                (v as TextView).setTextColor(Color.parseColor("#000000"))
//                return v
//            }
//
//            //By using this method we will define
//            //how the listview appears after clicking a spinner
//            override fun getDropDownView(
//                position: Int,
//                convertView: View,
//                parent: ViewGroup
//            ): View {
//                val v = super.getDropDownView(position, convertView, parent)
//                v.setBackgroundColor(Color.parseColor("#05000000"))
//                (v as TextView).setTextColor(Color.parseColor("#000000"))
//                return v
//            }
//        }
        spinnerEmployeeAdpter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)
        // Set Adapter in the spinner
        binding!!.spinnerLeadConvertOther.adapter = spinnerEmployeeAdpter
        binding!!.spinnerLeadConvertOther.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    // selected item in the list
                    if (position != 0) {
                        val EmpID: String = parentView.getItemAtPosition(position).toString()
                        for (i in SharedPreferencesRepository.getDataManagerInstance().employee.indices) {
                            if (EmpID.equals(
                                    SharedPreferencesRepository.getDataManagerInstance().employee.get(
                                        i
                                    ).firstName + " " + SharedPreferencesRepository.getDataManagerInstance().employee.get(
                                        i
                                    ).lastName + "(" + SharedPreferencesRepository.getDataManagerInstance().employee.get(
                                        i
                                    ).empId + ")", ignoreCase = true
                                )
                            ) {
                                selectConvertOther =
                                    SharedPreferencesRepository.getDataManagerInstance().employee.get(
                                        i
                                    ).userId.toString()
                            }
                        }
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }

        /*// commodity listing
        SpinnerCommudityAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, CommudityName) {
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

        SpinnerCommudityAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerCommudity.setAdapter(SpinnerCommudityAdapter);
        binding.spinnerCommudity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    String presentMeterStatusID = parentView.getItemAtPosition(position).toString();
                    for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getCommudity().size(); i++) {
                        if (presentMeterStatusID.equalsIgnoreCase(SharedPreferencesRepository.getDataManagerInstance().getCommudity().get(i).getCategory() + "(" + SharedPreferencesRepository.getDataManagerInstance().getCommudity().get(i).getCommodityType() + ")")) {
                            commudityID = String.valueOf(SharedPreferencesRepository.getDataManagerInstance().getCommudity().get(i).getId());
                            getstack();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });*/

        // layout Terminal Listing resource and list of items.
        spinnerTeerminalAdpter =

            ArrayAdapter(this, R.layout.multiline_spinner_item, (TerminalName)!!)
//            object :
//            ArrayAdapter<String?>(this, R.layout.multiline_spinner_item, (TerminalName)!!) {
//            //By using this method we will define how
//            // the text appears before clicking a spinner
//            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//                val v = super.getView(position, convertView, parent)
//                (v as TextView).setTextColor(Color.parseColor("#000000"))
//                return v
//            }
//
//            //By using this method we will define
//            //how the listview appears after clicking a spinner
//            override fun getDropDownView(
//                position: Int,
//                convertView: View,
//                parent: ViewGroup
//            ): View {
//                val v = super.getDropDownView(position, convertView, parent)
//                v.setBackgroundColor(Color.parseColor("#05000000"))
//                (v as TextView).setTextColor(Color.parseColor("#000000"))
//                return v
//            }
//        }
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
                        val presentMeterStatusID: String =
                            parentView.getItemAtPosition(position).toString()
                        for (i in data!!.indices) {
                            if (presentMeterStatusID.contains(data!!.get(i).name)) {
                                TerminalID = data!!.get(i).id.toString()
                                //                    binding.rlUser.setVisibility(View.VISIBLE);
                                binding!!.inoutRl.visibility = View.VISIBLE
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
                            //                } else if (seleectCoustomer == null) {
                            //                    Toast.makeText(CaseIDGenerateClass.this, getResources().getString(R.string.select_coustomer), Toast.LENGTH_LONG).show();
                        } else {
                            selectInOUt = parent.getItemAtPosition(position).toString()
                            binding!!.rlUser.visibility = View.VISIBLE
                            //                    binding.RLCommodity.setVisibility(View.VISIBLE);
                            userList
                            getstack()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // can leave this empty
                }
            }

        /*   // spinner select other generated
        binding.spinnerLeadConvertOther.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    selectConvertOther = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // can leave this empty
            }
        });*/

        /*  binding.spinnerUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomerNamePopup(CustomerName);
            }
        });*/

        // layout Terminal Listing resource and list of items.
        SpinnerStackAdapter = ArrayAdapter(this, R.layout.multiline_spinner_item, (StackName)!!)
//            object : ArrayAdapter<String?>(this, R.layout.multiline_spinner_item, (StackName)!!) {
//                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//                    val v = super.getView(position, convertView, parent)
//                    (v as TextView).setTextColor(Color.parseColor("#000000"))
//                    return v
//                }
//
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
//        SpinnerStackAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)
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
                    if (position != 0) {
                        val presentMeterStatusID: String =
                            parentView.getItemAtPosition(position).toString()
                        for (i in Stackdata!!.indices) {
                            if (presentMeterStatusID.contains(Stackdata!!.get(i).stackNumber)) {
                                stackID = Stackdata!!.get(i).id.toString()
                                if (selectInOUt!!.contains("IN")) {
                                    binding!!.etCustomerVehicle.setText("" + Stackdata!!.get(i).vehicle_no)
                                    binding!!.etCustomerVehicle.isClickable = false
                                    binding!!.etCustomerVehicle.isFocusable = false
                                    binding!!.etCustomerVehicle.isEnabled = false
                                    binding!!.etCustomerVehicle.isFocusableInTouchMode = false
                                } else {
                                    binding!!.etCustomerVehicle.setText("")
                                    binding!!.etCustomerVehicle.isClickable = true
                                    binding!!.etCustomerVehicle.isFocusable = true
                                    binding!!.etCustomerVehicle.isEnabled = true
                                    binding!!.etCustomerVehicle.isFocusableInTouchMode = true
                                }
                                break
                            }
                        }
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

    private fun getstack() {
        apiService.getStackList(
            StackPostData(
                commudityID,
                TerminalID,
                seleectCoustomer,
                selectInOUt
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {body->
                StackName!!.clear()
                stackID = null
                binding!!.spinnerStack.prompt = ""
                StackName!!.add("Stack Number")
                Stackdata = body.data as ArrayList<StackListPojo.Datum>
                for (i in Stackdata.indices) {
                    StackName!!.add(Stackdata.get(i).stackNumber)
                    //SpinnerStackAdapter.notifyDataSetChanged();
                }
                SpinnerStackAdapter = ArrayAdapter(this@CaseIDGenerateClass,
                    R.layout.multiline_spinner_item,
                    (StackName)!!)
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
            }.subscribe()

    }

    private val commodityList: Unit
        private get() {
            apiService.getCommodityList(TerminalID, selectInOUt, seleectCoustomer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext{body->
                    CommudityName!!.clear()
                    commudityID = null
                    binding!!.spinnerCommudity.prompt = ""
                    CommudityName!!.add(resources.getString(R.string.commodity_name))
                    commodityData = body.dataList as ArrayList<CommodityResponseData.Data>
                    for (i in commodityData.indices) {
                        CommudityName!!.add(commodityData.get(i).category)
                        //SpinnerStackAdapter.notifyDataSetChanged();
                    }
                    SpinnerCommudityAdapter =
                        ArrayAdapter(this@CaseIDGenerateClass,
                            R.layout.multiline_spinner_item,
                            (CommudityName)!!)
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
                }.subscribe()

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
                                    /* for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getuserlist().size(); i++) {
                                        CustomerName.add(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getFname() + "(" + SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getPhone());
                                    }*/hideDialog()
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

    private fun terminalListLevel(){
        leadsViewModel.getTerminalList()
        leadsViewModel.response.observe(this){
            when(it){
                is NetworkResult.Error -> hideDialog()
                is NetworkResult.Loading -> showDialog()
                is NetworkResult.Success -> {
                    data = it.data!!.data as ArrayList<TerminalListPojo.Datum>
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
            apiService.getUserList(TerminalID, selectInOUt).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {body->
                    Userdata = body.users as ArrayList<AllUserListPojo.User>
                    for (i in Userdata.indices) {
                        CustomerName!!.add(Userdata.get(i).fname + "(" + Userdata.get(i).phone)
                    }
                }.subscribe()

        }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener(this)
        binding!!.btnCreateeCase.setOnClickListener(this)
        binding!!.tvDone.setOnClickListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityAndClear(StaffDashBoardActivity::class.java)
        //    startActivityAndClear(StaffDashBoardActivity.class);
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_close -> startActivityAndClear(StaffDashBoardActivity::class.java)
            R.id.tv_done -> startActivityAndClear(CaseListingActivity::class.java)
            R.id.btn_createe_case -> if (isValid) {
                /*  for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getuserlist().size(); i++) {
                                if (UserID.equalsIgnoreCase(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getFname())) {
                                    seleectCoustomer = String.valueOf(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getPhone());
                                }
                            }*/
                if (TerminalID == null) {
                    Toast.makeText(
                        this@CaseIDGenerateClass,
                        resources.getString(R.string.terminal_name),
                        Toast.LENGTH_LONG
                    ).show()
                } else if (selectInOUt == null) {
                    Toast.makeText(
                        this@CaseIDGenerateClass,
                        resources.getString(R.string.select_in_out),
                        Toast.LENGTH_LONG
                    ).show()
                } else if (seleectCoustomer == null) {
                    Toast.makeText(
                        this@CaseIDGenerateClass,
                        resources.getString(R.string.select_coustomer),
                        Toast.LENGTH_LONG
                    ).show()
                } else if (commudityID == null) {
                    Toast.makeText(
                        this@CaseIDGenerateClass,
                        resources.getString(R.string.commudity_name),
                        Toast.LENGTH_LONG
                    ).show()
                } else if (stackID == null) {
                    Toast.makeText(
                        this@CaseIDGenerateClass,
                        "Select Stack Number",
                        Toast.LENGTH_LONG
                    ).show()
                } /*else if (selectPurpose == null) {
                        Toast.makeText(CaseIDGenerateClass.this, getResources().getString(R.string.select_purposee), Toast.LENGTH_LONG).show();
                    }*/
                /*else if (selectConvertOther == null) {
                        Toast.makeText(CaseIDGenerateClass.this, getResources().getString(R.string.select_employee), Toast.LENGTH_LONG).show();
                    }*/ else {
                    Utility.showDecisionDialog(
                        this@CaseIDGenerateClass,
                        getString(R.string.alert),
                        "Are You Sure to Summit?",
                        object : Utility.AlertCallback {
                            override fun callback() {
                                showDialog()
                                /*  for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getuserlist().size(); i++) {
                                    if (UserID.equalsIgnoreCase(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getFname() + "(" + SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getPhone() + ")")) {
                                        seleectCoustomer = String.valueOf(SharedPreferencesRepository.getDataManagerInstance().getuserlist().get(i).getPhone());
                                    }
                                }*/
                                val userDetails =
                                    SharedPreferencesRepository.getDataManagerInstance().user
                                if (selectInOUt.equals("OUT", ignoreCase = true)) {
                                    selectPurpose = "Self Withdrawal"
                                } else {
                                    selectPurpose = "For Storage"
                                }

//                                apiService.doCreateCaseID(new CreateCaseIDPostData(
//                                        stringFromView(binding.etCustomerGatepass), selectInOUt, stringFromView(binding.etCustomerWeight), stringFromView(binding.etCustomerBags),
//                                        seleectCoustomer, commudityID, TerminalID, stringFromView(binding.etCustomerVehicle),
//                                        selectPurpose, stringFromView(binding.etSpotToken), stackID, selectConvertOther)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
//                                    @Override
//                                    protected void onSuccess(LoginResponse body) {
//                                        hideDialog();
//                                        Utility.showAlertDialog(CaseIDGenerateClass.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
//                                            @Override
//                                            public void callback() {
//                                                startActivityAndClear(CaseListingActivity.class);
//                                                finish();
//                                            }
//                                        });
//                                    }
//                                });

                                apiService.doCreateCaseID( CreateCaseIDPostData(
                                    TerminalID,
                                    selectInOUt,
                                    seleectCoustomer,
                                    commudityID,
                                    "",
                                    stackID,
                                    stringFromView(binding!!.etCustomerBags),
                                    stringFromView(binding!!.etCustomerWeight),
                                    stringFromView(
                                        binding!!.etCustomerWeightQuantal
                                    ),
                                    stringFromView(binding!!.etCustomerVehicle),
                                    stringFromView(binding!!.etSpotToken),
                                    "",
                                    "",
                                    ""
                                )).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnNext {body->
                                        Utility.showAlertDialog(
                                            this@CaseIDGenerateClass,
                                            getString(R.string.alert),
                                            body.message,
                                            object : Utility.AlertCallback {
                                                override fun callback() {
                                                    startActivityAndClear(CaseListingActivity::class.java)
                                                    finish()
                                                }
                                            })
                                    }

                                    .subscribe()

                            }
                        })
                }
            }
        }
    }

    val isValid: Boolean
        get() {
            if (TextUtils.isEmpty(stringFromView(binding!!.etCustomerBags))) {
                return Utility.showEditTextError(binding!!.tilCustomerBags, R.string.bags)
            } /*else if (TextUtils.isEmpty(stringFromView(binding.etCustomerGatepass))) {
            return Utility.showEditTextError(binding.tilCustomerGatepass, R.string.gate_pass);
        }*/ else if (TextUtils.isEmpty(stringFromView(binding!!.etCustomerWeight))) {
                return Utility.showEditTextError(binding!!.tilCustomerWeight, R.string.weight_kg)
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etCustomerVehicle))) {
                return Utility.showEditTextError(binding!!.tilCustomerVehicle, R.string.vehicle_no)
            } /*  else if (TextUtils.isEmpty(stringFromView(binding.etCustomerFpo))) {
            return Utility.showEditTextError(binding.tilCustomerFpo, R.string.fpo_sub_useername);
        }*/
            return true
        }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {}
    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
}
