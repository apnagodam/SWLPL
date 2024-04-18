package com.apnagodam.staff.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.viewmodel.CaseIdViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityPvBinding
import com.apnagodam.staff.paging.adapter.PvAdapter
import com.apnagodam.staff.paging.state.ListLoadStateAdapter
import com.apnagodam.staff.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class pv : AppCompatActivity() {
    lateinit var binding: ActivityPvBinding

    lateinit var pvAdapter: PvAdapter
    val caseIdViewModel by viewModels<CaseIdViewModel>()

    @Inject
    lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pv)
        setUI()
    }

    fun setUI() {
        pvAdapter = PvAdapter({
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,UpdatePv::class.java))
        }, apiService)
        setObservers()
        caseIdViewModel.getAllCasesPagingData("");
    }

    fun setObservers() {
        binding.rvPv.isNestedScrollingEnabled = false
        val horizontalLayoutManager =
            LinearLayoutManager(this@pv, LinearLayoutManager.VERTICAL, false)
        binding.rvPv.layoutManager = horizontalLayoutManager

        binding.rvPv.adapter = pvAdapter.withLoadStateFooter(
            footer = ListLoadStateAdapter {
                caseIdViewModel.getPagingData()
            }
        )
        caseIdViewModel.allCasesPaginatino.observe(this) {
            lifecycleScope.launch {
                pvAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            pvAdapter.loadStateFlow.collect {
                // to determine if we are done with the loading state,
                // you should have already  shown your loading view elsewhere when the entering your fragment
                when (it.refresh) {
                    is LoadState.Error -> {
                    }

                    LoadState.Loading -> {
                        Utility.showDialog(this@pv, "")
                    }

                    is LoadState.NotLoading -> {
                        Utility.hideDialog(this@pv)
                    }
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
    }
}