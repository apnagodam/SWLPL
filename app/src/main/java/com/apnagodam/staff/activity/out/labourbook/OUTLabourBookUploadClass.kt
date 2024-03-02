package com.apnagodam.staff.activity.out.labourbook

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.apnagodam.staff.activity.`in`.labourbook.LabourType
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
class OUTLabourBookUploadClass : BaseActivity<ActivityUploadLabourDetailsBinding?>(),
    View.OnClickListener, AdapterView.OnItemSelectedListener {
    var UserName: String? = null
    var CaseID: String? = ""
    private var calender: Calendar? = null
    lateinit var SpinnerControactorAdapter: ArrayAdapter<String>
    var contractorsID: String? = null
    var contractorNameID: String? = null
    var isClientLabour = true;

    // drop down  of meter status
    lateinit var contractorName: ArrayList<String>
    val homeViewModel by viewModels<HomeViewModel>()
    val labourViewModel by viewModels<LabourViewModel>()
    lateinit var searchableSpinner: SearchableSpinner

    var labourType = LabourType.DEFAULT.type
    var checked = false
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
                        contractorNameID =
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
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, itemList!!)



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
                    labourType = itemList.get(position)
                    // selected item in the list
                    if (labourType == LabourType.CLIENT.type) {
                        isClientLabour = true;
                        binding!!.layoutLabour.visibility = View.GONE
                    } else if (labourType == LabourType.COMPANY.type) {
                        isClientLabour = false;
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
        homeViewModel.getCommodities("Emp")
        homeViewModel.commoditiesReponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {

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
                        contractorNameID,
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

                labourViewModel.labourDetailsUploadResponse.observe(this@OUTLabourBookUploadClass) {
                    when (it) {
                        is NetworkResult.Error -> {
                            hideDialog()
                        }

                        is NetworkResult.Loading -> {}
                        is NetworkResult.Success -> {

                            when (it.data) {
                                null -> {
                                    hideDialog()
                                }

                                else -> {
                                    showToast(it.data.message)
                                    hideDialog()
                                    finish()
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
            if (labourType == LabourType.COMPANY.type) {
                if (Validationhelper().fieldEmpty(binding!!.tilContractor)) {
                    binding!!.tilContractor.error =
                        "This Field cannot be empty"
                }
            }
            else if (labourType == LabourType.CLIENT.type){
                contractorNameID = "1"

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
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {}
    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
}
