package com.apnagodam.staff.activity.audit

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Response.CmDetailsResponse
import com.apnagodam.staff.Network.viewmodel.AuditViewModel
import com.apnagodam.staff.activity.BaseActivity
import com.apnagodam.staff.databinding.ActivityCmDetailsBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.CommudityResponse
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CmDetailsActivity : BaseActivity<ActivityCmDetailsBinding>() {

    private var terminalList = arrayListOf<CommudityResponse.Terminals>()
    private var terminalId: Int = 0;
    private var cmId = "";
    private val listOfTerminals = arrayListOf<String>()
    private val listOfCmSpinner = arrayListOf<String>()
    private var listOfCm = arrayListOf<CmDetailsResponse.Datum>()
    lateinit var searchableSpinner: SearchableSpinner
    lateinit var searchableSpinnerCM: SearchableSpinner

    private val auditViewModel by viewModels<AuditViewModel>()

    override fun setUI() {
        searchableSpinner = SearchableSpinner(this)
        searchableSpinnerCM = SearchableSpinner(this)

        binding.btSubmit.setOnClickListener {
            if (terminalId.equals(0)) {
                showToast(this, "Please select terminal")
            }
            if (cmId.isEmpty()) {
                showToast(this, "Please Select CM Agency")
            } else {
                if (binding.tilCmMobile.editText?.text?.length == 10 && !binding.tilCmName.editText?.text.isNullOrEmpty() && binding.tilGuardMobile.editText?.text?.length == 10 && !binding.tilGuardName.editText?.text.isNullOrEmpty()) {
                    auditViewModel.postAuditCM(
                        terminalId.toString(),
                        cmId,
                        binding.tilGuardName.editText?.text.toString(),
                        binding.tilGuardMobile.editText?.text.toString(),
                        binding.edNotes.text.toString(),
                        binding.tilCmName.editText?.text.toString(),
                        binding.tilCmMobile.editText?.text.toString()
                    )

                } else {
                    showToast(this, "Please input valid data")
                }


            }

        }

    }


    override fun setObservers() {
        auditViewModel.postAuditCMResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog(this)
                }

                is NetworkResult.Loading -> {
                    showDialog(this)
                }

                is NetworkResult.Success -> {
                    it.data?.let { response ->
                        response.message?.let { message ->
                            showToast(this, message)
                        }
                        response.status?.let { status ->
                            if (status == "1") {
                                finish()
                            }
                        }
                    }
                }
            }
        }
        auditViewModel.getCmDataResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog(this)
                    it.message?.let { message ->
                        showToast(
                            this, message
                        )
                    }
                }

                is NetworkResult.Loading -> {
                    showDialog(this)
                }

                is NetworkResult.Success -> {
                    hideDialog(this)
                    it.data?.let { data ->
                        data.getData()?.let { dataList ->
                            listOfCm = dataList
                        }

                        listOfCm.forEach { cmData ->
                            cmData.vendorFirstName?.let { vendorName ->
                                listOfCmSpinner.add(vendorName)

                            }
                        }

                        getCmList()
                    }
                }
            }
        }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityCmDetailsBinding =
        ActivityCmDetailsBinding.inflate(layoutInflater)

    override fun callApis() {
        auditViewModel.getCmData()
        getTerminalsFromPreferences()

    }


    private fun getTerminalsFromPreferences() {

        SharedPreferencesRepository.getDataManagerInstance().user.let { userDetails ->
            terminalList.clear()
            listOfTerminals.clear();
            val localTerminalList = SharedPreferencesRepository.getDataManagerInstance().terminals
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
                    terminalId = terminalList[position].id.toInt()


                }
            }
            binding.tvTerminal.setOnClickListener { searchableSpinner.show() }
        }

    }

    private fun getCmList() {

        searchableSpinnerCM.setSpinnerListItems(listOfCmSpinner)

        searchableSpinnerCM.onItemSelectListener = object : OnItemSelectListener {
            override fun setOnItemSelectListener(
                position: Int,
                selectedString: String
            ) {
                binding.tvCM.text = selectedString
                cmId = listOfCm[position].id.toString()

            }
        }
        binding.tvCM.setOnClickListener { searchableSpinnerCM.show() }

    }

}