package com.apnagodam.staff.activity.lead

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.CreateLeadsPostData
import com.apnagodam.staff.Network.Request.UpdateLeadsPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.viewmodel.LeadsViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.databinding.ActivityGeenerteLeadsBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.AllLeadsResponse.Lead
import com.apnagodam.staff.module.TerminalListPojo
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
@AndroidEntryPoint
class LeadGenerateClass() : BaseActivity<ActivityGeenerteLeadsBinding?>(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    lateinit var SpinnerCommudityAdapter: ArrayAdapter<String>
    lateinit var spinnerTeerminalAdpter: ArrayAdapter<String>
    var commudityID: String? = ""
    var TerminalID: String? = ""
    var selectPurpose: String? = ""

    // droup down  of meter status
    lateinit var CommudityName: MutableList<String>
    lateinit var CommudityID: List<String>
    lateinit var TerminalName: MutableList<String>
    lateinit var TerminalsID: List<String>
    lateinit var calender: Calendar
    private var isUpdate = false
    lateinit var getLeadId: String
    lateinit var data: List<TerminalListPojo.Datum>
    val leadsViewModel by viewModels<LeadsViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_geenerte_leads
    }

    override fun setUp() {
        calender = Calendar.getInstance()
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        terminalListLevel()
        clickListner()
        CommudityName = ArrayList()
        CommudityID = ArrayList()
        TerminalName = ArrayList()
        TerminalsID = ArrayList()
        CommudityName.add(resources.getString(R.string.commodity_name))
        TerminalName.add(resources.getString(R.string.terminal_name1))
        setValueOnSpinner()

    }

     private fun terminalListLevel(){
         leadsViewModel.getTerminalList()
         leadsViewModel.response.observe(this){
             when(it){
                 is NetworkResult.Error -> {hideDialog()}
                 is NetworkResult.Loading -> {
                     showDialog()
                 }
                 is NetworkResult.Success -> {
                     data = it.data!!.data
                     for (i in data.indices) {
                         TerminalName!!.add(data.get(i).name + "(" + data.get(i).warehouseCode + ")")
                     }

                     spinnerTeerminalAdpter =
                             ArrayAdapter(this, R.layout.multiline_spinner_item, (TerminalName)!!)

                     spinnerTeerminalAdpter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)
                     // Set Adapter in the spinner
                     binding!!.spinnerTerminal.adapter = spinnerTeerminalAdpter
                     //        object :
//            ArrayAdapter<String?>(this, R.layout.multiline_spinner_item) {
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
                                             if (presentMeterStatusID.contains(
                                                             data!!.get(i).name + "(" + data!!.get(
                                                                     i
                                                             ).warehouseCode + ")"
                                                     )
                                             ) {
                                                 TerminalID = data!!.get(i).id.toString()
                                             }
                                         }
                                     }
                                 }

                                 override fun onNothingSelected(parentView: AdapterView<*>?) {
                                     // your code here
                                 }
                             }
                 }
             }

         }

    }

    private fun setValueOnSpinner() {
        // spinner meter obj
        // layout resource and list of items.
        // commudityID = "" + SharedPreferencesRepository.getDataManagerInstance().getCommudity().get(0).getId();
        //   TerminalID = "" + SharedPreferencesRepository.getDataManagerInstance().GetTerminal().get(0).getId();
        for (i in SharedPreferencesRepository.getDataManagerInstance().commudity.indices) {
            CommudityName!!.add(SharedPreferencesRepository.getDataManagerInstance().commudity[i].category + "(" + SharedPreferencesRepository.getDataManagerInstance().commudity[i].commodityType + ")")
        }
        /* for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().GetTerminal().size(); i++) {
            TerminalName.add(SharedPreferencesRepository.getDataManagerInstance().GetTerminal().get(i).getName() + "(" + SharedPreferencesRepository.getDataManagerInstance().GetTerminal().get(i).getWarehouseCode() + ")");
        }*/

        SpinnerCommudityAdapter =
            ArrayAdapter(this, R.layout.multiline_spinner_item, (CommudityName)!!)

//            object :  {
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
        SpinnerCommudityAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)
        // Set Adapter in the spinner
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
                        for (i in SharedPreferencesRepository.getDataManagerInstance().commudity.indices) {
                            if (presentMeterStatusID.equals(
                                    SharedPreferencesRepository.getDataManagerInstance().commudity.get(
                                        i
                                    ).category + "(" + SharedPreferencesRepository.getDataManagerInstance().commudity.get(
                                        i
                                    ).commodityType + ")", ignoreCase = true
                                )
                            ) {
                                commudityID =
                                    SharedPreferencesRepository.getDataManagerInstance().commudity.get(
                                        i
                                    ).id.toString()
                            }
                        }
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }
        // layout resource and list of items.

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
    }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener(this)
        binding!!.lpCommiteDate.setOnClickListener(this)
        binding!!.userCommitmentDate.setOnClickListener(this)
        binding!!.btnLogin.setOnClickListener(this)
        binding!!.tvDone.setOnClickListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
       onBackPressed()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_close -> startActivityAndClear(StaffDashBoardActivity::class.java)
            R.id.tv_done -> callLeadListActivity()
            R.id.lp_commite_date -> popUpDatePicker()
            R.id.userCommitmentDate -> popUpDatePicker()
            R.id.btn_login -> Utility.showDecisionDialog(
                this@LeadGenerateClass,
                getString(R.string.alert),
                "Are You Sure to Summit?",
                object : Utility.AlertCallback {
                    override fun callback() {
                        if (isValid) {
                            if (commudityID == null) {
                                Toast.makeText(
                                    this@LeadGenerateClass,
                                    resources.getString(R.string.commudity_name),
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (TerminalID == null) {
                                Toast.makeText(
                                    this@LeadGenerateClass,
                                    resources.getString(R.string.terminal_name),
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (TextUtils.isEmpty(stringFromView(binding!!.userCommitmentDate))) {
                                Toast.makeText(
                                    this@LeadGenerateClass,
                                    "Select Commitment Date",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (selectPurpose == null) {
                                Toast.makeText(
                                    this@LeadGenerateClass,
                                    resources.getString(R.string.select_purposee),
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                if (isUpdate) {
                                    val userDetails =
                                        SharedPreferencesRepository.getDataManagerInstance().user

                                    apiService.updateLeads(
                                        UpdateLeadsPostData(
                                            getLeadId,
                                            userDetails.userId,
                                            stringFromView(binding!!.etCustomerName),
                                            stringFromView(
                                                binding!!.etCustomerQuantity
                                            ),
                                            stringFromView(binding!!.etCustomerLocation),
                                            stringFromView(binding!!.etCustomerNumber),
                                            commudityID,
                                            TerminalID,
                                            stringFromView(
                                                binding!!.userCommitmentDate
                                            ),
                                            selectPurpose
                                        )
                                    ).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnNext {body->
                                            Toast.makeText(
                                                this@LeadGenerateClass,
                                                body.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                            callLeadListActivity()
                                            Utility.showAlertDialog(
                                                this@LeadGenerateClass,
                                                getString(R.string.alert),
                                                body.message,
                                                Utility.AlertCallback { callLeadListActivity() })
                                        }.subscribe()

                                } else {
                                    val userDetails =
                                        SharedPreferencesRepository.getDataManagerInstance().user

                                    apiService.doCreateLeads(
                                        CreateLeadsPostData(
                                            userDetails.userId,
                                            stringFromView(binding!!.etCustomerName),
                                            stringFromView(binding!!.etCustomerQuantity),
                                            stringFromView(binding!!.etCustomerLocation),
                                            stringFromView(binding!!.etCustomerNumber),
                                            commudityID,
                                            TerminalID,
                                            stringFromView(
                                                binding!!.userCommitmentDate
                                            ),
                                            selectPurpose
                                        )
                                    ).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnNext{body-> Utility.showAlertDialog(
                                            this@LeadGenerateClass,
                                            getString(R.string.alert),
                                            body.message,
                                            object : Utility.AlertCallback {
                                                override fun callback() {
                                                    callLeadListActivity()
                                                }
                                            })}.subscribe()

                                }
                            }
                        }
                    }
                })
        }
    }

    private fun callLeadListActivity() {
        val intent = Intent(this@LeadGenerateClass, LeadListingActivity::class.java)
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && data != null) {
            val lead = data.getSerializableExtra(Constants.LeadListData) as Lead?
            isUpdate = true
            getLeadId = lead!!.data[0].id
            setLeadData(lead)
        }
    }

    private fun setLeadData(lead: Lead?) {
        binding!!.etCustomerName.setText(lead!!.data[0].customerName)
        binding!!.etCustomerNumber.setText(lead.data[0].phone)
        binding!!.etCustomerQuantity.setText(lead.data[0].quantity)
        binding!!.etCustomerLocation.setText(lead.data[0].location)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        try {
            val date = simpleDateFormat.parse(lead.data[0].commodityDate)
            binding!!.userCommitmentDate.setText(simpleDateFormat.format(date))
        } catch (pe: ParseException) {
            Log.e("getValueException", pe.toString())
        }
        when(lead.data[0].purpose){
            null->{
                binding!!.spinnerPurpose.setSelection(
                    getIndex(
                        binding!!.spinnerPurpose,
                        ""
                    )
                )
            }
            else->{
                binding!!.spinnerPurpose.setSelection(
                    getIndex(
                        binding!!.spinnerPurpose,
                        lead.data[0].purpose
                    )
                )
            }
        }

        binding!!.spinnerCommudity.setSelection(
            getIndex(
                binding!!.spinnerCommudity,
                lead.data[0].cateName
            )
        )
        binding!!.spinnerTerminal.setSelection(
            getIndex(
                binding!!.spinnerTerminal,
                lead.data[0].terminalName + "(" + lead.data[0].warehouseCode + ")"
            )
        )
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        var index = 0
        for (i in 0 until spinner.count) {
            if ((spinner.getItemAtPosition(i) == myString)) {
                index = i
            }
        }
        return index
    }

    val isValid: Boolean
        get() {
            if (TextUtils.isEmpty(stringFromView(binding!!.etCustomerName))) {
                return Utility.showEditTextError(binding!!.tilCustomerName, R.string.coustomer_name)
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etCustomerNumber))) {
                return Utility.showEditTextError(binding!!.tilCustomerNumber, R.string.mobile_no)
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etCustomerQuantity))) {
                return Utility.showEditTextError(binding!!.tilCustomerQuantity, R.string.quanity)
            } else if (TextUtils.isEmpty(stringFromView(binding!!.etCustomerLocation))) {
                return Utility.showEditTextError(binding!!.tilCustomerLocation, R.string.location)
            }
            return true
        }

    fun popUpDatePicker() {
        val dateDialog = DatePickerDialog(
            this@LeadGenerateClass, date, calender
                .get(Calendar.YEAR), calender!![Calendar.MONTH],
            calender!![Calendar.DAY_OF_MONTH]
        )
        dateDialog.datePicker.minDate = System.currentTimeMillis()
        dateDialog.show()
    }

    var date: OnDateSetListener = object : OnDateSetListener {
        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            calender!![Calendar.YEAR] = year
            calender!![Calendar.MONTH] = monthOfYear
            calender!![Calendar.DAY_OF_MONTH] = dayOfMonth
            val myFormat = "yyyy-MM-dd" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding!!.userCommitmentDate.setText(sdf.format(calender!!.time).toString())
        }
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {}
    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
}
