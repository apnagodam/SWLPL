package com.apnagodam.staff.activity.audit

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.AuditViewModel
import com.apnagodam.staff.activity.BaseActivity
import com.apnagodam.staff.databinding.ActivityInOutBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.CommudityResponse
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InOutActivity : BaseActivity<ActivityInOutBinding>() {
    lateinit var searchableSpinner: SearchableSpinner
    lateinit var searchableSpinnerInOut: SearchableSpinner
    private var selectedInOut = ""
    private var selectedTerminal=""

    private var listOfInOut = arrayListOf<String>("IN", "OUT")

    private var currentTime = ""
    private var currentDate = ""

    private val auditViewModel by viewModels<AuditViewModel>()
    private val listOfTerminals = arrayListOf<String>()
    private var terminalList = arrayListOf<CommudityResponse.Terminals>()
    override fun setUI() {
        setInOutSpinner()
        getTerminalsFromPreferences()
        getCurrentLocation()
//        binding.tvSelectTime.setOnClickListener {
//            currentTime = getCurrentTime()
//            currentDate = getCurrentDate()
//            binding.tvSelectTime.text = "$currentDate  $currentTime"
//        }
        binding.tvSelectLocation.setOnClickListener {
            getCurrentLocation()
            binding.tvSelectLocation.text =currentLocation
        }
        binding.btSubmit.setOnClickListener {
            if (selectedTerminal.isEmpty()) {
                showToast(this, "Select Terminal")
            }
            if (selectedInOut.isEmpty()) {
                showToast(this, "Select In or Out")
            }
            if (binding.tvSelectLocation.text.equals("Select Location")) {
                showToast(this, "Select Location!")
            } else {
                auditViewModel.postAuditInOut(lat.toString(),long.toString(), terminalId =selectedTerminal,selectedInOut,binding.edNotes.text.toString() )
            }

        }
    }

    override fun setObservers() {
        auditViewModel.postAuditInOutResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog(this)
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    hideDialog(this)
                    it.data?.let { response ->
                        response.message?.let { message ->
                            showToast(this, message)
                        }
                        response.status?.let { status ->
                            if (status.equals("1")) {
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityInOutBinding =
        ActivityInOutBinding.inflate(layoutInflater)

    override fun callApis() {

    }

    private fun getTerminalsFromPreferences() {
        searchableSpinner = SearchableSpinner(this)

        SharedPreferencesRepository.getDataManagerInstance().user.let { userDetails ->
            terminalList.clear()
            listOfTerminals.clear();
            terminalList = SharedPreferencesRepository.getDataManagerInstance().terminals

            searchableSpinner.windowTitle = "Select Terminal"

            for (i in terminalList) {
                listOfTerminals.add(i.name)
            }


            searchableSpinner.setSpinnerListItems(listOfTerminals)

            searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
                override fun setOnItemSelectListener(
                    position: Int,
                    selectedString: String
                ) {
                    binding.tvTerminal.text = selectedString
                    selectedTerminal = terminalList[position].id

                }
            }
            binding.tvTerminal.setOnClickListener { searchableSpinner.show() }
        }

    }
    private fun setInOutSpinner() {
        searchableSpinnerInOut = SearchableSpinner(this)
        searchableSpinnerInOut.windowTitle = "Select In Out"

        searchableSpinnerInOut.setSpinnerListItems(listOfInOut)

        searchableSpinnerInOut.onItemSelectListener = object : OnItemSelectListener {
            override fun setOnItemSelectListener(
                position: Int,
                selectedString: String
            ) {
                binding.tvSelectInOut.text = selectedString
                selectedInOut = selectedString
            }
        }
        binding.tvSelectInOut.setOnClickListener { searchableSpinnerInOut.show() }
    }
}