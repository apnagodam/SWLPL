package com.apnagodam.staff.activity.displedged

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.DispleaseViewModel
import com.apnagodam.staff.activity.BaseActivity
import com.apnagodam.staff.adapter.DispleasedBagsAdapter
import com.apnagodam.staff.databinding.ActivityDispleasedListingBinding
import com.apnagodam.staff.helper.DialogHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DispleasedListingActivity : BaseActivity<ActivityDispleasedListingBinding>() {
    val displeaseViewModel by viewModels<DispleaseViewModel>()
    lateinit var displeasedBagsAdapter: DispleasedBagsAdapter

    override fun setUI() {

    }

    override fun setObservers() {
        displeaseViewModel.displeasedListingResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    it.data?.let { response ->
                        response.data?.let { displeasedList ->
                            displeasedBagsAdapter = DispleasedBagsAdapter(
                                displeasedList,
                                { datum ->
                                    DialogHelper.showRejectDialog(this) { action, notes ->
                                        when (action) {
                                            DialogHelper.Action.YES -> {
                                                datum.id?.let { id ->
                                                    displeaseViewModel.rejectDispleasedRequest(
                                                        id.toString(),
                                                        notes
                                                    )
                                                }
                                            }

                                            DialogHelper.Action.NO -> {

                                            }
                                        }
                                    }

                                }, this
                            )
                            binding.rvPleased.layoutManager = LinearLayoutManager(this)

                            binding.rvPleased.adapter = displeasedBagsAdapter

                        }

                    }
                }
            }
        }
        displeaseViewModel.displeasedRejectResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    it.data?.let { response ->
                        response.message?.let {
                            showToast(this, it)
                        }
                        response.status?.let {
                            if (it == "1") {
                                displeaseViewModel.getDispleasedList()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityDispleasedListingBinding =
        ActivityDispleasedListingBinding.inflate(layoutInflater)

    override fun callApis() {
        displeaseViewModel.getDispleasedList()
    }
}