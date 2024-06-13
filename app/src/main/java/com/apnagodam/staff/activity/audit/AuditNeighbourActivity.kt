package com.apnagodam.staff.activity.audit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.AddNeighbourRequest
import com.apnagodam.staff.Network.viewmodel.AuditViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.BaseActivity
import com.apnagodam.staff.databinding.ActivityAuditNeighbourBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.CommudityResponse
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuditNeighbourActivity : BaseActivity<ActivityAuditNeighbourBinding>() {
    private var terminalList = arrayListOf<CommudityResponse.Terminals>()
    private var terminalId: Int = 0;
    private val listOfTerminals = arrayListOf<String>()
    lateinit var searchableSpinner: SearchableSpinner
    private val auditViewModel by viewModels<AuditViewModel>()
    private lateinit var addNeighbourRequest: AddNeighbourRequest
    private var neighDetailsArrayList = arrayListOf<AddNeighbourRequest.NeighbourDetails>()
    override fun setUI() {
        searchableSpinner = SearchableSpinner(this)
        binding.btSubmit.setOnClickListener {
            if (terminalId.equals(0)) {
                showToast(this, "Please select terminal")
            } else {
                if (validateNameFields()) {
                    if (validateNumberFields()) {
                        getDataFromFields()
                        if (neighDetailsArrayList.isNotEmpty()) {
                            addNeighbourRequest =
                                AddNeighbourRequest(
                                    terminalId.toString(),
                                    binding.edNotes.text.toString(),
                                    neighDetailsArrayList
                                )
                            auditViewModel.postAuditNeighbour(addNeighbourRequest)

                        }
                    } else {
                        showToast(this, "Neighbour number is not correct")

                    }
                } else {
                    showToast(this, "Please input at least single neightbour name")
                }

            }
        }
    }

    override fun setObservers() {
        auditViewModel.postAuditNeighborResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog(this)
                    it.message?.let { message ->
                        showToast(this, message)
                    }
                }

                is NetworkResult.Loading -> {
                    showDialog(this)
                }

                is NetworkResult.Success -> {
                    hideDialog(this)
                    it.data?.let { data ->
                        data.message?.let { message ->
                            showToast(this, message)
                        }
                        data.status?.let { status ->
                            when (status) {
                                "1" -> finish()
                                else -> showToast(this, "Something went wrong")
                            }
                        }
                    }
                }
            }
        }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityAuditNeighbourBinding =
        ActivityAuditNeighbourBinding.inflate(layoutInflater)

    override fun callApis() {
        getTerminalsFromPreferences()
    }

    private fun getDataFromFields() {

        if (!binding.tilNeighbourMobile1.editText?.text.isNullOrEmpty() && !binding.tilNeighbourName1.editText?.text.isNullOrEmpty()) {
            neighDetailsArrayList.add(
                AddNeighbourRequest.NeighbourDetails(
                    binding.tilNeighbourName1.editText?.text.toString(),
                    binding.tilNeighbourMobile1.editText?.text.toString()
                )
            )

        }
        if (!binding.tilNeighbourMobile2.editText?.text.isNullOrEmpty() && !binding.tilNeighbourName2.editText?.text.isNullOrEmpty()) {
            neighDetailsArrayList.add(
                AddNeighbourRequest.NeighbourDetails(
                    binding.tilNeighbourName2.editText?.text.toString(),
                    binding.tilNeighbourMobile2.editText?.text.toString()
                )
            )

        }
        if (!binding.tilNeighbourMobile3.editText?.text.isNullOrEmpty() && !binding.tilNeighbourName3.editText?.text.isNullOrEmpty()) {
            neighDetailsArrayList.add(
                AddNeighbourRequest.NeighbourDetails(
                    binding.tilNeighbourName3.editText?.text.toString(),
                    binding.tilNeighbourMobile3.editText?.text.toString()
                )
            )

        }
    }

    private fun validateNumberFields(): Boolean =
        (((!binding.tilNeighbourMobile1.editText?.text.isNullOrEmpty() && (binding.tilNeighbourMobile1.editText?.text?.length
            ?: 0) == 10) || (!binding.tilNeighbourMobile2.editText?.text.isNullOrEmpty() && (binding.tilNeighbourMobile2.editText?.text?.length
            ?: 0) == 10) || (!binding.tilNeighbourMobile3.editText?.text.isNullOrEmpty() && (binding.tilNeighbourMobile3.editText?.text?.length
            ?: 0) == 10)))

    private fun validateNameFields(): Boolean =
        (!binding.tilNeighbourName1.editText?.text.isNullOrEmpty() || !binding.tilNeighbourName2.editText?.text.isNullOrEmpty() || binding.tilNeighbourName3.editText?.text.isNullOrEmpty())

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
                    position: Int, selectedString: String
                ) {
                    binding.tvSelectTerminal.text = selectedString
                    terminalId = terminalList[position].id.toInt()


                }
            }
            binding.tvSelectTerminal.setOnClickListener { searchableSpinner.show() }
        }

    }
}