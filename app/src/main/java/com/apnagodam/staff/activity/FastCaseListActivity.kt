package com.apnagodam.staff.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Response.ResponseFastcaseList
import com.apnagodam.staff.Network.viewmodel.CaseIdViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.adapter.FastCaseListAdapter
import com.apnagodam.staff.databinding.ActivityFastCaseListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FastCaseListActivity : BaseActivity<ActivityFastCaseListBinding?>() {
    private var mContext: Context? = null
    val caseIdViewModel by viewModels<CaseIdViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_fast_case_list
    }

    override fun setUp() {
        mContext = this@FastCaseListActivity
        binding!!.ivClose.setOnClickListener { onBackPressed() }
        fastCaseList
    }

    private val fastCaseList: Unit
        private get() {
            showDialog()
            caseIdViewModel.getFastcaseList()
            caseIdViewModel.fastcaseListResponse.observe(this){
                when(it){
                    is NetworkResult.Error -> {
                        hideDialog()
                    }
                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        hideDialog()
                        if (it.data!=null){
                            if (it.data.status=="1"){
                                val horizontalLayoutManager =
                                    LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                                binding!!.rvFastCaseList.layoutManager = horizontalLayoutManager
                                val fastCaseListAdapter = FastCaseListAdapter(it.data.data, mContext)
                                binding!!.rvFastCaseList.adapter = fastCaseListAdapter
                            }
                        }
                    }
                }
            }

        }
}