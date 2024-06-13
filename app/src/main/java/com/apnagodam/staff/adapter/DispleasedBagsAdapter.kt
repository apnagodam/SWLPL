package com.apnagodam.staff.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apnagodam.staff.Network.Response.DispleasedListResponse
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.LayoutDispleasedBinding
import com.apnagodam.staff.helper.DateTimeHelper
import com.apnagodam.staff.helper.DialogHelper
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import java.util.Date

class DispleasedBagsAdapter(
    private val displeasedList: ArrayList<DispleasedListResponse.Datum>,
    var callBack: (DispleasedListResponse.Datum) -> Unit,
    var activity: Activity
) :
    RecyclerView.Adapter<DispleasedBagsAdapter.DispleasedBagsViewHolder>() {

    lateinit var layoutDispleasedBinding: LayoutDispleasedBinding

    inner class DispleasedBagsViewHolder(private val layoutDispleasedBinding: LayoutDispleasedBinding) :
        RecyclerView.ViewHolder(layoutDispleasedBinding.root) {

        fun bind(
            data: DispleasedListResponse.Datum,
            callBack: (DispleasedListResponse.Datum) -> Unit, activity: Activity
        ) {
            layoutDispleasedBinding.let {
                it.tvName.text = data.userName
                it.tvCommodity.text = data.category
                it.tvNotes.text = data.notes.toString()
                it.tvBags.text = data.bags
                it.tvWeight.text = data.netWeight
                it.tvApprovar.text = "${data.firstName} ${data.lastName}"
                it.tvDate.text = DateTimeHelper.formatDate(data.createdAt ?: Date().toString())
                it.tvTerminal.text = data.terminalName
                it.tvStack.text = data.stackNumber

                it.tvImage.setOnClickListener {
                    PhotoFullPopupWindow(
                        activity,
                        R.layout.popup_photo_full,
                        it,
                        Constants.DISPLEGED_IMAGE_URL + data.displedgeImage,
                        null
                    )
                }
                it.btSubmit.setOnClickListener {
                    callBack.invoke(data)
                }

            }

            when (data.status) {
                1 -> {
                    layoutDispleasedBinding.btSubmit.text = "Reject"
                }

                else -> {
                    layoutDispleasedBinding.btSubmit.text = "Pending"
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DispleasedBagsViewHolder {
        layoutDispleasedBinding =
            LayoutDispleasedBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return DispleasedBagsViewHolder(layoutDispleasedBinding)
    }

    override fun getItemCount(): Int {

        return displeasedList.size;
    }

    override fun onBindViewHolder(holder: DispleasedBagsViewHolder, position: Int) {
        holder.bind(displeasedList[position], callBack, activity)
    }
}