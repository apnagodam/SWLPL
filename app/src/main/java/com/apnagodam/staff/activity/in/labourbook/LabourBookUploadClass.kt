package com.apnagodam.staff.activity.`in`.labourbook

import android.app.DatePickerDialog
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.BuildConfig
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadLabourDetailsPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.viewmodel.HomeViewModel
import com.apnagodam.staff.Network.viewmodel.LabourViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityUploadLabourDetailsBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.utils.Utility
import com.apnagodam.staff.utils.Validationhelper
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class LabourBookUploadClass : BaseActivity<ActivityUploadLabourDetailsBinding?>(),
    View.OnClickListener, AdapterView.OnItemSelectedListener {
    var UserName: String? = null
    var CaseID: String? = ""
    private var calender: Calendar? = null
    lateinit var SpinnerControactorAdapter: ArrayAdapter<String>
    var contractorsID: String? = null

    var isClientLabour = true;

    // drop down  of meter status
    lateinit var contractorName: ArrayList<String>
    val homeViewModel by viewModels<HomeViewModel>()
    val labourViewModel by viewModels<LabourViewModel>()
    lateinit var searchableSpinner: SearchableSpinner
    var contractorNameId: String? = null
    var checked = false
    var labourType = LabourType.DEFAULT
    override fun getLayoutResId(): Int {
        return R.layout.activity_upload_labour_details
    }

    override fun setUp() {
        searchableSpinner = SearchableSpinner(this)
        binding!!.tvTitle.setText("Upload Labour Book")
        calender = Calendar.getInstance()
        UserName = intent.getStringExtra("user_name")
        CaseID = intent.getStringExtra("case_id")
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        clickListner()
        binding!!.customerName.text = UserName
        binding!!.caseId.text = CaseID
        contractorName = ArrayList()

        getCommodityList()
    }

    private fun setValueOnSpinner() {

        for (i in SharedPreferencesRepository.getDataManagerInstance().contractorList.indices) {
            if (!SharedPreferencesRepository.getDataManagerInstance().contractorList[i].contractorName.contains(
                    "Client Labour"
                )
            ) {
                contractorName!!.add(SharedPreferencesRepository.getDataManagerInstance().contractorList[i].contractorName)
            }
        }

        searchableSpinner.windowTitle = "Select Labour Contractor"


        searchableSpinner.setSpinnerListItems(contractorName)

        searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
            override fun setOnItemSelectListener(
                position: Int,
                selectedString: String
            ) {
                binding!!.etContractor.setText(selectedString)
                contractorsID = contractorName.get(position)
                for (i in SharedPreferencesRepository.getDataManagerInstance().contractorList.indices) {
                    if (contractorsID.equals(
                            SharedPreferencesRepository.getDataManagerInstance().contractorList[i].contractorName,
                            ignoreCase = true
                        )
                    ) {
                        contractorNameId =
                            SharedPreferencesRepository.getDataManagerInstance().contractorList[i].id.toString()
                        binding!!.etContractorPhone.setText(SharedPreferencesRepository.getDataManagerInstance().contractorList[i].contractorPhone.toString())
                        binding!!.etLabourRate.setText(SharedPreferencesRepository.getDataManagerInstance().contractorList[i].rate.toString())
                    }
                }
            }
        }

        var itemList = arrayListOf<String>()
        itemList.add(LabourType.DEFAULT.type)
        itemList.add(LabourType.CLIENT.type)
        itemList.add(LabourType.COMPANY.type)
        SpinnerControactorAdapter =
            ArrayAdapter(this, R.layout.multiline_spinner_item, itemList!!)


//            object :
//            ArrayAdapter<String?>(this, R.layout.multiline_spinner_item, contractorName!!) {
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
        SpinnerControactorAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)
        // Set Adapter in the spinner
        binding!!.spinnerContractor.adapter = SpinnerControactorAdapter
        binding!!.spinnerContractor.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    labourType.type = itemList[position]
                    // selected item in the list
                    if (position == 0 || position == 1) {
                        binding!!.layoutLabour.visibility = View.GONE
                    } else {
                        binding!!.layoutLabour.visibility = View.VISIBLE
                    }

                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                    binding!!.layoutLabour!!.visibility = View.GONE
                }
            }
    }

    private fun getCommodityList() {
        showDialog()
        homeViewModel.getCommodities("Emp")
        homeViewModel.commoditiesReponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog()
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    if (it.data != null) {
                        if (BuildConfig.APPLICATION_ID != null) {
                            SharedPreferencesRepository.getDataManagerInstance()
                                .setCommdity(it.data.categories)
                            SharedPreferencesRepository.getDataManagerInstance().employee =
                                it.data.employee
                            SharedPreferencesRepository.getDataManagerInstance()
                                .setContractor(it.data.labourList)
                        }
                        if (SharedPreferencesRepository.getDataManagerInstance().contractorList != null) {
                            setValueOnSpinner()
                        }
                    }
                    hideDialog()
                }
            }
        }


    }

    private fun clickListner() {
        binding!!.ivClose.setOnClickListener(this)
        binding!!.btnLogin.setOnClickListener(this)
        binding!!.etStartDateTime.setOnClickListener(this)
        binding!!.lpCommiteDate.setOnClickListener(this)

        binding!!.etContractor.setOnClickListener {
            searchableSpinner.show()
        }

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_close -> {
                finish()
            }

            R.id.et_start_date_time -> popUpDatePicker()
            R.id.lp_commite_date -> popUpDatePicker()
            R.id.btn_login -> if (isValid) {
                showDialog()
                labourViewModel.uploadLabourDetails(
                    UploadLabourDetailsPostData(
                        CaseID,
                        contractorNameId,
                        contractorsID,
                        stringFromView(binding!!.etContractorPhone),
                        "N/A",
                        stringFromView(binding!!.etLabourRate),
                        "N/A",
                        "N/A",
                        stringFromView(binding!!.notes),
                        "0000-00-00"
                    )
                )
                hideDialog()
                finish()
                labourViewModel.labourDetailsUploadResponse.observe(this@LabourBookUploadClass) {
                    when (it) {
                        is NetworkResult.Error -> {
                            showToast(it.message)
                            hideDialog()
                        }

                        is NetworkResult.Loading -> {}
                        is NetworkResult.Success -> {
                            if (it.data != null) {
                                if (it.data.status == "1") {
                                    showToast(it.data!!.message.toString())
                                } else {
                                    showToast(it.message)
                                }

                            }

                        }
                    }
                }


//                if (contractorsID == null) {
//                    Toast.makeText(
//                        this@LabourBookUploadClass,
//                        resources.getString(R.string.contractor_select),
//                        Toast.LENGTH_LONG
//                    ).show()
//                } else {
//
//                }
            }
        }
    }

    val isValid: Boolean
        get() {

            if (labourType.type != LabourType.CLIENT.type) {
                if (Validationhelper().fieldEmpty(binding!!.tilContractor)) {
                    binding!!.tilContractor.error =
                        "This Field cannot be empty"
                    return false

                }
            } else {
                contractorNameId = "1"
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
        }
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {}
    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
}

enum class LabourType(var type: String) {
    DEFAULT("Select Labour"), CLIENT("Client Labour"), COMPANY("Company Labour")
}

