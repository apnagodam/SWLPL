package com.apnagodam.staff.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
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
import com.apnagodam.staff.adapter.StackRequestAdapterOutwards
import com.apnagodam.staff.databinding.ActivityInwardList2Binding
import com.apnagodam.staff.databinding.ActivityOutwardsListBinding
import com.apnagodam.staff.module.AllCaseIDResponse
import com.apnagodam.staff.utils.RecyclerItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OutwardsListActivity : BaseActivity<ActivityOutwardsListBinding?>(),
    RecyclerItemClickListener.OnItemClickListener,
    AdapterView.OnItemSelectedListener {
    var allCasesList = arrayListOf<StackRequestResponse.OutwardRequestDatum>()
    private var casesTopAdapter: StackRequestAdapterOutwards? = null
    val caseIdViewModel: CaseIdViewModel by viewModels<CaseIdViewModel>()
    private var pageOffset = 1
    private var totalPage = 0

    override fun getLayoutResId(): Int {
        return R.layout.activity_outwards_list
    }

    override fun setUp() {
        setSupportActionBar(binding!!.tlOutwards)
        binding!!.tlOutwards.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        setView()
    }


    fun setView() {
        getAllCases("")
    }

    private fun getAllCases(search: String) {
        caseIdViewModel.getStackRequest()
        caseIdViewModel.stackRequestResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    showToast(it.message)
                    hideDialog()
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    when (it.data) {
                        null -> {}
                        else -> {
                            if (it.data.status == "1") {

                                allCasesList = it.data.outwardRequestData
                                setAdapter()

                            }
                        }
                    }
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        getAllCases("")
    }

    private fun setAdapter() {
        casesTopAdapter = StackRequestAdapterOutwards(allCasesList, this)

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

    override fun onItemClick(view: View?, position: Int) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}