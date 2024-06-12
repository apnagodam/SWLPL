package com.apnagodam.staff.activity.displedged

import android.content.Intent
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.activity.viewModels
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.DispleasedRequestModel
import com.apnagodam.staff.Network.Response.PleasedApproverResponse
import com.apnagodam.staff.Network.Response.PleasedCommodityResponse
import com.apnagodam.staff.Network.Response.PleasedStackResponse
import com.apnagodam.staff.Network.Response.PleasedUsersResponse
import com.apnagodam.staff.Network.viewmodel.DispleaseViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.BaseActivity
import com.apnagodam.staff.databinding.ActivityDispledgedBagsBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.CommudityResponse
import com.apnagodam.staff.utils.CameraHelper
import com.apnagodam.staff.utils.Utility
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DispledgedBags : BaseActivity<ActivityDispledgedBagsBinding>() {
    private val displeaseViewModel by viewModels<DispleaseViewModel>()

    private var terminalList = mutableListOf<CommudityResponse.Terminals>()
    private lateinit var terminalSpinner: SearchableSpinner
    private lateinit var userSpinner: SearchableSpinner
    private lateinit var commoditySpinner: SearchableSpinner
    private lateinit var stackSpinner: SearchableSpinner
    private lateinit var approvedSpinner: SearchableSpinner
    private val terminalNames = arrayListOf<String>()
    private var selectedTerminalId = ""
    private var selectedUsersId = ""
    private var usersList = arrayListOf<PleasedUsersResponse.Datum>()
    private var usersNameList = arrayListOf<String>()
    private var commodityList = arrayListOf<PleasedCommodityResponse.Datum>()
    private var commodityNames = arrayListOf<String>()

    private var stackList = arrayListOf<PleasedStackResponse.Datum>()
    private var stackNames = arrayListOf<String>()

    private var approverList = arrayListOf<PleasedApproverResponse.Employee>()
    private var approverNames = arrayListOf<String>()

    private var stackId = ""
    private var stackNo = ""
    private var approverName = ""
    private var approverId = ""
    private var commodityId = ""
    override fun setUI() {
        setSpinners()
        setClickListeners()
        getTerminalsList().let {
            if (it.isNotEmpty() && !terminalList.containsAll(it)) {
                binding.spinnerTerminals.isEnabled = true
                binding.spinnerTerminals.setBackgroundResource(R.drawable.btn_borders)
                terminalList.clear();
                terminalList.addAll(getTerminalsList())
                terminalList.forEach {
                    terminalNames.add(it.name)
                }
            }
        }


    }

    private fun setClickListeners() {
        binding.spinnerTerminals.setOnClickListener {
            terminalSpinner.show()
        }
        binding.spinnerUsers.setOnClickListener {
            if (selectedTerminalId.isEmpty()) {
                showToast(this, "Please select terminal first!")
            } else {
                userSpinner.show()

            }
        }
        binding.spinnerCommodity.setOnClickListener {
            if (selectedUsersId.isEmpty()) {
                showToast(this, "Please select user first!")
            } else {
                commoditySpinner.show()

            }
        }
        binding.spinnerStack.setOnClickListener {
            stackSpinner.show()
        }
        binding.spinnerApprovedBy.setOnClickListener {
            approvedSpinner.show()
        }
        binding.btSelectImage.setOnClickListener {
            checkForPermission {
                CameraHelper.captureImage(this)


            }


        }
        binding.imgDispleased.setOnClickListener {
            getCurrentLocation()
            checkForPermission {
                CameraHelper.captureImage(this)
            }
        }
        binding.btSubmit.setOnClickListener {

            if (validateFields()) {
                var displeasedRequestModel = DispleasedRequestModel(
                    user = selectedUsersId,
                    terminal_id = selectedTerminalId,
                    commodity_id = commodityId,
                    stack_id = stackId,
                    quantity = binding.etNetWeight.text.toString(),
                    Bags = binding.etBags.text.toString(),
                    displedge_image = Utility.transferImageToBase64(CameraHelper.imageFile),
                    approved_by = approverId,
                    emp_displege_notes = binding.edNotes.text.toString(),
                )
                displeaseViewModel.postDispleasedRequest(
                    displeasedRequestModel
                )

            }
        }

    }

    private fun validateFields(): Boolean {
        if (selectedTerminalId.isEmpty()) {
            showToast(this, "Please Select Terminal")
            return false
        }
        if (selectedUsersId.isEmpty()) {
            showToast(this, "Please Select User")
            return false

        }
        if (commodityId.isEmpty()) {
            showToast(this, "Please Select Commodity")
            return false
        }
        if (stackId.isEmpty()) {
            showToast(this, "Please Select Stack")
            return false
        }
        if (approverId.isEmpty()) {
            showToast(this, "Please Select Approver")
            return false
        }
        if (CameraHelper.imageFile == null) {
            showToast(this, "Please Select image")
            return false
        }
        if (binding.etBags.text.toString().isEmpty()) {
            binding.etBags.error = "Bags cannot be empty"
            return false
        }
        if (binding.etNetWeight.text.toString().isEmpty()) {
            binding.etNetWeight.error = "Net weight cannot be empty"
            return false
        }
        return true
    }

    private fun setSpinners() {
        terminalSpinner = SearchableSpinner(this)
        terminalSpinner.let {
            it.setSpinnerListItems(terminalNames)
            it.onItemSelectListener = object : OnItemSelectListener {
                override fun setOnItemSelectListener(position: Int, selectedString: String) {
                    binding.spinnerTerminals.setText(selectedString)
                    selectedTerminalId = terminalList[position].id

                    displeaseViewModel.getPleasedUser(selectedTerminalId)


                }

            };
        }
        userSpinner = SearchableSpinner(this)
        commoditySpinner = SearchableSpinner(this)
        stackSpinner = SearchableSpinner(this)
        approvedSpinner = SearchableSpinner(this)
        terminalSpinner.windowTitle = "Select Terminal"
        userSpinner.windowTitle = "Select Users"
        commoditySpinner.windowTitle = "Select Commodity"
        approvedSpinner.windowTitle = "Approved By"

    }

    private fun getTerminalsList(): List<CommudityResponse.Terminals> =
        SharedPreferencesRepository.getDataManagerInstance().terminals

    override fun setObservers() {
        displeaseViewModel.pleasedUsersResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    it.message?.let {
                        showToast(
                            this, it
                        )
                    }
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    it.data?.let { data ->
                        data.message?.let {
                            showToast(this, it)
                        }
                        data.data?.let { users ->
                            usersList.clear()
                            usersNameList.clear()
                            users.forEach {
                                usersList.add(it)
                                usersNameList.add(it.fname.toString())
                            }
                            binding.spinnerUsers.isEnabled = true
                            binding.spinnerUsers.setBackgroundResource(R.drawable.btn_borders)
                            userSpinner.let {
                                it.setSpinnerListItems(usersNameList)
                                it.onItemSelectListener = object : OnItemSelectListener {
                                    override fun setOnItemSelectListener(
                                        position: Int,
                                        selectedString: String
                                    ) {
                                        binding.spinnerUsers.setText(selectedString)
                                        selectedUsersId = usersList[position].userId.toString()


                                        displeaseViewModel.getPleasedCommodity(
                                            selectedTerminalId,
                                            selectedUsersId
                                        )
                                    }

                                };


                            }
                        }
                    }
                }
            }
        }

        displeaseViewModel.pleasedCommodityResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    it.data?.let { data ->
                        data.message?.let {
                            showToast(this, it)
                        }
                        data.status?.let {
                            if (it == "1") {
                                data.data?.let { commodity ->
                                    commodityNames.clear()
                                    commodityList.clear()
                                    commodity.forEach {
                                        commodityNames.add(it.category.toString())
                                        commodityList.add(it)
                                    }

                                    commoditySpinner.setSpinnerListItems(commodityNames)
                                    binding.spinnerCommodity.isEnabled = true
                                    binding.spinnerCommodity.setBackgroundResource(R.drawable.btn_borders)
                                    commoditySpinner.let {
                                        it.onItemSelectListener = object : OnItemSelectListener {
                                            override fun setOnItemSelectListener(
                                                position: Int,
                                                selectedString: String
                                            ) {
                                                binding.spinnerCommodity.setText(selectedString)
                                                commodityId =
                                                    commodityList[position].commodity.toString()


                                                displeaseViewModel.getPleaseStacks(
                                                    selectedTerminalId,
                                                    selectedUsersId,
                                                    commodityId
                                                )
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        displeaseViewModel.pleasedStackResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {

                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    it.data?.let { data ->
                        data.message?.let {
                            showToast(this, it)
                        }
                        data.status?.let {
                            if (it == "1") {
                                data.data?.let { commodity ->
                                    stackNames.clear()
                                    stackList.clear()
                                    commodity.forEach {
                                        stackNames.add(it.stackNo.toString())
                                        stackList.add(it)
                                    }

                                    stackSpinner.setSpinnerListItems(stackNames)
                                    binding.spinnerStack.isEnabled = true
                                    binding.spinnerStack.setBackgroundResource(R.drawable.btn_borders)
                                    stackSpinner.let {
                                        it.onItemSelectListener = object : OnItemSelectListener {
                                            override fun setOnItemSelectListener(
                                                position: Int,
                                                selectedString: String
                                            ) {
                                                binding.spinnerStack.setText(selectedString)
                                                stackId =
                                                    stackList[position].stackId.toString()


//                                                displeaseViewModel.getPleaseStacks(
//                                                    selectedTerminalId,
//                                                    selectedUsersId,
//                                                    commodityId
//                                                )
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        displeaseViewModel.pleasedApproverResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    it.data?.let { data ->
                        data.message?.let {
                            showToast(this, it)
                        }
                        data.status?.let {
                            if (it == "1") {
                                data.employees?.let { employees ->
                                    approverNames.clear()
                                    approverList.clear()
                                    employees.forEach {
                                        approverNames.add(it.employee.toString())
                                        approverList.add(it)
                                    }


                                    approvedSpinner.setSpinnerListItems(approverNames)
                                    binding.spinnerApprovedBy.isEnabled = true
                                    binding.spinnerApprovedBy.setBackgroundResource(R.drawable.btn_borders)
                                    approvedSpinner.let {
                                        it.onItemSelectListener = object : OnItemSelectListener {
                                            override fun setOnItemSelectListener(
                                                position: Int,
                                                selectedString: String
                                            ) {
                                                binding.spinnerApprovedBy.setText(selectedString)
                                                approverId =
                                                    approverList[position].id.toString()


//                                                displeaseViewModel.getPleaseStacks(
//                                                    selectedTerminalId,
//                                                    selectedUsersId,
//                                                    commodityId
//                                                )
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        displeaseViewModel.postDispleasedResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    it.data?.let { data ->
                        data.message?.let {
                            showToast(this, it)
                        }
                        data.status?.let {
                            if (it == "1") {
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        CameraHelper.onActivityResult(
            requestCode,
            resultCode,
            data,
            this@DispledgedBags,
            currentLocation
        )
        if (CameraHelper.imageFile != null) {
            binding.imgDispleased.scaleType = ImageView.ScaleType.FIT_XY
            binding.imgDispleased.setImageURI(CameraHelper.fileUri)
        }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityDispledgedBagsBinding =
        ActivityDispledgedBagsBinding.inflate(layoutInflater)

    override fun callApis() {
        displeaseViewModel.getPleasedApprovar()
    }
}