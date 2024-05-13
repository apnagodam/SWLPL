package com.apnagodam.staff.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.AdvancePaymentViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityAdvancesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdvancesActivity : BaseActivity<ActivityAdvancesBinding>() {
    val advancePaymentViewModel by viewModels<AdvancePaymentViewModel>()

    override fun setObservers() {
        advancePaymentViewModel.postAdvancesResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    it.message?.let {
                        showToast(this, it)
                    }

                }
                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    it.data?.let {
                        showToast(this, it.message)
                        if (it.status == "1") {
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityAdvancesBinding =
        ActivityAdvancesBinding.inflate(layoutInflater)


    override fun setUI() {
        binding.btAdvanceRequest.setOnClickListener {
            if (binding.tilAdvanceNotes.editText!!.text.isEmpty()) {
                binding.tilAdvanceNotes.error = "This field cannot be empty!"

            } else if (binding.tilAdvanceRequest.editText!!.text.isEmpty()) {
                binding.tilAdvanceRequest.error = "This field cannot be empty!"

            } else {
                advancePaymentViewModel.postAdvanceRequest(
                    binding.tilAdvanceRequest.editText?.text.toString(),
                    binding.tilAdvanceNotes.editText?.text.toString()
                )
            }
        }
        binding.tvAdvanceList.setOnClickListener {
            startActivity(Intent(this, AdvancesListActivity::class.java))

        }
    }

    override fun callApis() {
    }

}