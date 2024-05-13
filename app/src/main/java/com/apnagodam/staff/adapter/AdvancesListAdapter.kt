package com.apnagodam.staff.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.apnagodam.staff.databinding.LayoutAdvancesListBinding
import com.apnagodam.staff.Network.Response.AdvancePaymentListModel

class AdvancesListAdapter(val advancesList: ArrayList<AdvancePaymentListModel.Datum>) :
    RecyclerView.Adapter<AdvancesListAdapter.AdvancesViewHolder>() {
    lateinit var binding: LayoutAdvancesListBinding

    inner class AdvancesViewHolder(private val binding: LayoutAdvancesListBinding) :
        ViewHolder(binding.root) {
        fun bind(data: AdvancePaymentListModel.Datum) {
            data.createdAt?.let {
                binding.tvId.text=it
            }
            data.notes?.let {
                binding.tvName.text = it
            }

            data.requestedAmount?.let {
                binding.tvVehicle.text = "\u20B9${it}"
            }
            data.status?.let {
                if (it == 0) {
                    binding.tvStatus.text = "Rejected"
                } else if (it == 1) {
                    binding.tvStatus.text = "Approved"
                } else if (it == 2) {
                    binding.tvStatus.text = "In Progress"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvancesViewHolder {
        binding =
            LayoutAdvancesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdvancesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return advancesList.size
    }

    override fun onBindViewHolder(holder: AdvancesViewHolder, position: Int) {
        holder.bind(data = advancesList[position])
    }

}