package com.apnagodam.staff.activity.audit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.indices
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.AuditQVRequest
import com.apnagodam.staff.Network.Response.StacksListResponse
import com.apnagodam.staff.Network.viewmodel.AuditViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.BaseActivity
import com.apnagodam.staff.databinding.ActivityAuditQualityStockBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.CommudityResponse
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuditQualityStock : BaseActivity<ActivityAuditQualityStockBinding>() {
    val auditViewModel by viewModels<AuditViewModel>()

    lateinit var searchableSpinner: SearchableSpinner
    lateinit var stackSearchableSpinner: SearchableSpinner
    var terminalList = arrayListOf<CommudityResponse.Terminals>()
    var stackList = arrayListOf<StacksListResponse.Datum>()
    private val listOfStacks = arrayListOf<String>()
    private val listOfTerminals = arrayListOf<String>()
    var terminalId: Int = 0;
    var stackId: Float = 0.0f;
    var commodityId = 0;
    var qvParamList = arrayListOf<AuditQVRequest.QVMOdel>()
    var array = arrayListOf<String>()
    override fun setUI() {
        searchableSpinner = SearchableSpinner(this)
        stackSearchableSpinner = SearchableSpinner(this)

        binding.btSubmit.setOnClickListener {
            if (terminalId == 0) {
                showToast(this, "Please Select Terminal")
            }
            if (stackId == 0.0f) {
                showToast(this, "Please select Stack")
            } else {
                var isValid = false;
                qvParamList.clear()
                for (i in binding.llQuality.indices) {
                    (binding.llQuality[i] as TextInputLayout).editText?.let {
                        if (it.text.isEmpty()) {
                            isValid = false;
                            (binding.llQuality[i] as TextInputLayout).error =
                                "This Field cannot be empty!"
                        } else {
                            qvParamList.add(
                                AuditQVRequest.QVMOdel(id = i.toString(), it.text.toString())
                            )
                            isValid = true
                        }
                    }
                }

                if (isValid) {
                    for (i in array.indices) {
                        qvParamList[i].id = array[i]
                    }

                    val request = AuditQVRequest(
                        terminalId.toString(),
                        commodityId.toString(),
                        stackId.toString(),
                        qvParamList
                    )
                    auditViewModel.postAuditQV(
                        request
                    )
                }
            }
        }
    }

    override fun setObservers() {
        auditViewModel.getStackListResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog(this)
                }

                is NetworkResult.Loading -> {
                    showDialog(this)
                }

                is NetworkResult.Success -> {
                    hideDialog(this)
                    stackList.clear()
                    listOfStacks.clear()
                    it.data?.let {

                        it.data?.let {
                            stackList = it
                        }

                        stackList.forEach {
                            it.stackNumber?.let {
                                listOfStacks.add(it)
                            }

                        }
                        stackSearchableSpinner.setSpinnerListItems(listOfStacks)
                        stackSearchableSpinner.onItemSelectListener =
                            object : OnItemSelectListener {
                                override fun setOnItemSelectListener(
                                    position: Int,
                                    selectedString: String
                                ) {
                                    it.data?.get(position)?.let {
                                        it.commodityId?.let {
                                            commodityId = it
                                        }
                                        it.stackNumber?.let {
                                            stackId = it.toFloat()

                                        }
                                        auditViewModel.getAuditQvData("${stackList[position].commodityId ?: 8}")


                                    }


                                    binding.tvSelectStack.text = selectedString


                                }

                            }
                    }


                }
            }
        }
        auditViewModel.auditQVDataResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog(this)
                    it.message?.let {
                        showToast(this, it)
                    }

                }

                is NetworkResult.Loading -> {


                }

                is NetworkResult.Success -> {
                    hideDialog(this)
                    binding.llQuality.removeAllViews()
                    it.data?.let {
                        it.dataArray?.let { qvData ->
                            if (qvData.isNotEmpty()) {
                                for (i in qvData.indices) {
                                    if (i != 0 && i < qvData.size - 1)
                                        array.add((qvData[i].id ?: 0).toString())
                                    binding.llQuality.addView(
                                        generateEditTexts(
                                            qvData[i].name ?: ""
                                        )
                                    );

                                }
                            }


                        }
                    }

                }
            }
        }
        auditViewModel.postAuditQvResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog(this)
                }

                is NetworkResult.Loading -> {
                    showDialog(this)
                }

                is NetworkResult.Success -> {
                    hideDialog(this)
                    it.data?.let {
                        it.status?.let {
                            if (it == "1") {
                                finish()
                            }
                        }
                        it.message?.let {
                            showToast(this, it)
                        }
                    }
                }
            }
        }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityAuditQualityStockBinding =
        ActivityAuditQualityStockBinding.inflate(layoutInflater)

    override fun callApis() {
        getTerminalsFromPreferences()
    }

    fun generateEditTexts(hint: String): TextInputLayout {
        val textInputField =
            TextInputLayout(this, null, R.attr.customTextInputStyle)
        val editText = TextInputEditText(textInputField.context)
        editText.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        editText.filters = arrayOf(InputFilter.LengthFilter(5))
        textInputField.setHint(hint)
        textInputField.addView(editText)

        // ed.add(textInputField)
        editText.hint = hint;
        return textInputField
    }

    private fun getTerminalsFromPreferences() {

        SharedPreferencesRepository.getDataManagerInstance().user.let { userDetails ->
            terminalList.clear()
            listOfTerminals.clear();
            listOfStacks.clear()
            val localTerminalList = SharedPreferencesRepository.getDataManagerInstance().terminals
            terminalList = SharedPreferencesRepository.getDataManagerInstance().terminals

            searchableSpinner.windowTitle = "Select Terminal"
            stackSearchableSpinner.windowTitle = "Select Stack"

            for (i in terminalList) {
                listOfTerminals.add(i.name)
            }


            searchableSpinner.setSpinnerListItems(listOfTerminals)

            searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
                override fun setOnItemSelectListener(
                    position: Int,
                    selectedString: String
                ) {
                    binding.tvSelectTerminal.text = selectedString
                    terminalId = terminalList[position].id.toInt()

                    auditViewModel.getStackList(terminalList[position].id)
                    binding.tvSelectStack.visibility = View.VISIBLE

                }
            }
            binding.tvSelectTerminal.setOnClickListener { searchableSpinner.show() }
            binding.tvSelectStack.setOnClickListener { stackSearchableSpinner.show() }
        }

    }
}