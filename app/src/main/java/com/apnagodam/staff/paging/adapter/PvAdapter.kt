package com.apnagodam.staff.paging.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.LayoutPvBinding
import com.apnagodam.staff.databinding.LayoutTopCaseGenerateBinding
import com.apnagodam.staff.module.AllCaseIDResponse
import javax.inject.Inject

class PvAdapter @Inject constructor(var action: () -> Unit, val apiService: ApiService) :
    PagingDataAdapter<AllCaseIDResponse.Datum, PvAdapter.PVviewholder>(Comparator) {




    class PVviewholder(
        private val binding: LayoutPvBinding,
        action: () -> Unit,
        apiService: ApiService
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(Leads: AllCaseIDResponse.Datum, action: () -> Unit, apiService: ApiService) {
            binding.tvId.text = Leads.stack_number

            binding.moreCase.visibility = View.VISIBLE
            binding.tvCaseId.text = Leads.caseId
            binding.tvName.setText(Leads.custFname)
            binding.tvPhone.setText(Leads.phone)
            binding.tvVehicle.setText(Leads.vehicleNo)
            binding.tvId.setTextColor(Color.BLACK)
            binding.tvName.setTextColor(Color.BLACK)
            binding.tvPhone.setTextColor(Color.BLACK)
            binding.tvDriverNum.setText(Leads.drivePhone.toString())

            binding.tvStatus.setOnClickListener {
                action.invoke()
            }
        }
    }

    override fun onBindViewHolder(holder: PVviewholder, position: Int) {
        getItem(position)?.let { userItemUiState ->
            holder.bind(
                userItemUiState,
                action,
                apiService
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PVviewholder {
        val binding = DataBindingUtil.inflate<LayoutPvBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_pv,
            parent,
            false
        )
        return PVviewholder(binding, this.action, this.apiService)
    }

    object Comparator : DiffUtil.ItemCallback<AllCaseIDResponse.Datum>() {
        override fun areItemsTheSame(
            oldItem: AllCaseIDResponse.Datum,
            newItem: AllCaseIDResponse.Datum
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: AllCaseIDResponse.Datum,
            newItem: AllCaseIDResponse.Datum
        ): Boolean {
            return oldItem == newItem
        }
    }
}