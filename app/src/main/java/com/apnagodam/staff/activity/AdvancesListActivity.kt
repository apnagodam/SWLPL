package com.apnagodam.staff.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Response.AdvancePaymentListModel
import com.apnagodam.staff.Network.viewmodel.AdvancePaymentViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.adapter.AdvancesListAdapter
import com.apnagodam.staff.databinding.ActivityAdvancesListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdvancesListActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdvancesListBinding
    val advancePaymentViewModel by viewModels<AdvancePaymentViewModel>()
    lateinit var advancesListAdapter: AdvancesListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_advances_list)
        setObservers()

        setUI()
    }

    private fun setUI() {
        advancePaymentViewModel.getAdvancesList()
    }

    private fun setObservers() {
        advancePaymentViewModel.advanceslistResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    it.data?.let {
                        if (it.status == "1") {
                            it.data?.let {advanceData->
                                advancesListAdapter = AdvancesListAdapter(advanceData.data as ArrayList<AdvancePaymentListModel.Datum>)
                                binding.rvAdvances.adapter = advancesListAdapter
                                binding.rvAdvances.layoutManager = LinearLayoutManager(this)


                            }
                            Toast.makeText(this, "${it.data}", Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }
        }
    }
}