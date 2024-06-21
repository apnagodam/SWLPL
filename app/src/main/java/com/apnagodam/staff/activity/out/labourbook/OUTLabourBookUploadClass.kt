package com.apnagodam.staff.activity.out.labourbook

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadLabourDetailsPostData
import com.apnagodam.staff.Network.viewmodel.HomeViewModel
import com.apnagodam.staff.Network.viewmodel.LabourViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.`in`.labourbook.LabourType
import com.apnagodam.staff.databinding.ActivityUploadLabourDetailsBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.utils.Validationhelper
import com.leo.searchablespinner.SearchableSpinner
import dagger.hilt.android.AndroidEntryPoint
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
        setObservers()
        getCommodityList()
    }

    fun setObservers() {
        labourViewModel.labourContractorNameResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    it.message?.let {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    if (labourType == LabourType.COMPANY.type) {
                        it.data?.let { data ->
                            data.data?.let { labourData ->
                                labourData.labourRate?.let { labourRate ->
                                    binding!!.etLabourRate.setText(labourRate)

                                }
                                labourData.contractorName?.let { contractorName ->
                                    contractorsID = contractorName
                                    binding!!.etContractor.setText(contractorName)

//                                binding!!.etLabourRate.setText(SharedPreferencesRepository.getDataManagerInstance().contractorList[i].rate.toString())
                                }
                                labourData.id?.let {
                                    contractorNameID = it.toString()

                                }
                                labourData.contractorPhone?.let { contractorPhone ->
                                    binding!!.etContractorPhone.setText(contractorPhone)
                                }
                            }
                        }
                    }

                }
            }
        }

        labourViewModel.labourDetailsUploadResponse.observe(this@OUTLabourBookUploadClass) {
            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    hideDialog()
                }

                is NetworkResult.Loading -> {
                }

                is NetworkResult.Success -> {
                    if (it.data != null) {
                        if (it.data.status == "1") {
                            hideDialog()
                            Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                            finish()

                        } else {
                            Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()

                        }
                    }


                }
            }
        }

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

//        searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
//            override fun setOnItemSelectListener(
//                position: Int,
//                selectedString: String
//            ) {
//                binding!!.etContractor.setText(selectedString)
//                contractorsID = contractorName.get(position)
//                for (i in SharedPreferencesRepository.getDataManagerInstance().contractorList.indices) {
//                    if (contractorsID.equals(
//                            SharedPreferencesRepository.getDataManagerInstance().contractorList[i].contractorName,
//                            ignoreCase = true
//                        )
//                    ) {
//                        contractorNameID =
//                            SharedPreferencesRepository.getDataManagerInstance().contractorList[i].id.toString()
//                        binding!!.etContractorPhone.setText(SharedPreferencesRepository.getDataManagerInstance().contractorList[i].contractorPhone.toString())
//                        binding!!.etLabourRate.setText(SharedPreferencesRepository.getDataManagerInstance().contractorList[i].rate.toString())
//                    }
//                }
//            }
//        }

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
                    parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long
                ) {
                    labourType = itemList[position]
                    // selected item in the list
                    if (position == 0 || position == 1) {
                        binding!!.layoutLabour.visibility = View.GONE
                        contractorsID = ""
                        binding!!.etContractorPhone.setText("")
                        binding!!.etLabourRate.setText("")
                    } else {

                        binding!!.layoutLabour.visibility = View.VISIBLE
                        intent?.let {
                            it.getStringExtra("warehouse_id")?.let { warehouse ->
                                it.getStringExtra("commodity_id")?.let { commodity ->
                                    labourViewModel.getLabourContractorName(warehouse, commodity);

                                }

                            }
                        }


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
                        SharedPreferencesRepository.getDataManagerInstance()
                            .setCommdity(it.data.categories)
                        SharedPreferencesRepository.getDataManagerInstance().employee =
                            it.data.employee
                        SharedPreferencesRepository.getDataManagerInstance()
                            .setContractor(it.data.labourList)
                        if (SharedPreferencesRepository.getDataManagerInstance().contractorList != null) {
                            setValueOnSpinner()
                        }
                    }
                }
            }
        }

        intent?.let {
            it.getStringExtra("warehouse_id")?.let { warehouse ->
                it.getStringExtra("commodity_id")?.let { commodity ->
                    labourViewModel.getLabourContractorName(warehouse, commodity);

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
            } else if (labourType == LabourType.CLIENT.type) {
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
