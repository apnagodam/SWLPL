package com.apnagodam.staff.activity

import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Response.StackRequestResponse
import com.apnagodam.staff.Network.viewmodel.CaseIdViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.adapter.CasesTopAdapter
import com.apnagodam.staff.adapter.StackRequestAdapter
import com.apnagodam.staff.databinding.ActivityInwardList2Binding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.interfaces.OnProfileClickListener
import com.apnagodam.staff.module.AllCaseIDResponse
import com.apnagodam.staff.utils.RecyclerItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InwardListActivity : BaseActivity<ActivityInwardList2Binding?>(), View.OnClickListener,
    OnProfileClickListener, RecyclerItemClickListener.OnItemClickListener,
    AdapterView.OnItemSelectedListener{
    var allCasesList = arrayListOf<StackRequestResponse.InwardRequestDatum>()
    private var casesTopAdapter: StackRequestAdapter? = null
    val caseIdViewModel: CaseIdViewModel by viewModels<CaseIdViewModel>()
    private var pageOffset = 1
    private var totalPage = 0


    override fun getLayoutResId(): Int {
        return R.layout.activity_inward_list_2
    }

    override fun setUp() {
        setSupportActionBar(binding!!.tlInwards)
        binding!!.tlInwards.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        caseIdViewModel.stackRequestResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show();
                    hideDialog()
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    hideDialog()
                    when (it.data) {
                        null -> {}
                        else -> {
                            if (it.data.status == "1") {
                                var userDetails = SharedPreferencesRepository.getDataManagerInstance().user
                                allCasesList.clear()
                                it.data.inwardRequestData.forEach {inwards->
                                    if(userDetails.terminal==null){
                                        allCasesList.add(inwards)
                                    }
                                    else if(inwards.terminalId.toString() == userDetails!!.terminal){
                                        allCasesList.add(inwards)
                                    }
                                }
                                setAdapter()

                            }
                        }
                    }
                }
            }
        }
        setView()

    }

    fun setView() {
        getAllCases("")
    }

    private fun getAllCases(search: String) {
        showDialog()
        caseIdViewModel.getStackRequest()




    }

    override fun onResume() {
        super.onResume()
        getAllCases("")

    }
    private fun setAdapter() {
        casesTopAdapter = StackRequestAdapter(allCasesList.reversed(), this)

        binding!!.rvDefaultersStatus.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        binding!!.rvDefaultersStatus.setHasFixedSize(true)
        binding!!.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager

        binding!!.rvDefaultersStatus.adapter = casesTopAdapter

    }

    override fun onClick(v: View?) {
    }

    override fun onProfileImgClick() {
    }

    override fun onItemClick(view: View?, position: Int) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}


