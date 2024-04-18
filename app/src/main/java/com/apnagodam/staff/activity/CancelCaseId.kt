package com.apnagodam.staff.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.apnagodam.staff.Network.viewmodel.CaseIdViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.paging.adapter.CancelCasesAdapter
import com.apnagodam.staff.paging.state.ListLoadStateAdapter
import com.apnagodam.staff.databinding.ActivityCancelCaseIdBinding
import com.apnagodam.staff.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CancelCaseId : AppCompatActivity() {

    lateinit var binding: ActivityCancelCaseIdBinding
    lateinit var casesAdapter: CancelCasesAdapter

    val caseIdViewModel: CaseIdViewModel by viewModels<CaseIdViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cancel_case_id)

        casesAdapter = CancelCasesAdapter(caseIdViewModel,this,this)
        setUI()
        setObservers()

        caseIdViewModel.getPagingData()
    }



    private fun setUI(){

    }

    private fun setObservers(){
        caseIdViewModel.userCasesPagination.observe(this){
            lifecycleScope.launch {
                casesAdapter.submitData(it)
            }
            lifecycleScope.launch {
                casesAdapter.loadStateFlow.collect {
                    // to determine if we are done with the loading state,
                    // you should have already  shown your loading view elsewhere when the entering your fragment
                    when (it.refresh) {
                        is LoadState.Error -> {
                        }

                        LoadState.Loading -> {
                            Utility.showDialog(this@CancelCaseId, "")
                        }

                        is LoadState.NotLoading -> {
                            Utility.hideDialog(this@CancelCaseId)
                        }
                    }
                }
            }
            binding.rvCancelCaseId.isNestedScrollingEnabled = false
            val horizontalLayoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvCancelCaseId.layoutManager = horizontalLayoutManager

            binding.rvCancelCaseId.adapter = casesAdapter.withLoadStateFooter(
                footer = ListLoadStateAdapter {
                    caseIdViewModel.getPagingData()
                }
            )
        }
    }
}