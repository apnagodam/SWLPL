package com.apnagodam.staff.paging.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.LayoutCancelCaseIdBinding
import com.apnagodam.staff.module.AllCaseIDResponse
import javax.inject.Inject
import androidx.databinding.DataBindingUtil.inflate
import androidx.lifecycle.LifecycleOwner
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.CaseIdViewModel

class CancelCasesAdapter(
    var apiService: CaseIdViewModel,
    context: Activity,
    lifecycleOwner: LifecycleOwner
) :
    PagingDataAdapter<AllCaseIDResponse.Datum, CancelCasesAdapter.CancelCasesViewHolder>(Comparator) {

    lateinit var activity: Activity
    lateinit var lifecycleOwner: LifecycleOwner

    init {
        activity = context
        this.lifecycleOwner = lifecycleOwner
    }

    class CancelCasesViewHolder(
        private var binding: LayoutCancelCaseIdBinding,
        apiService: CaseIdViewModel,
        context: Activity,
        lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            Leads: AllCaseIDResponse.Datum,
            context: Activity,
            apiService: CaseIdViewModel,
            lifecycleOwner: LifecycleOwner
        ) {
            binding.executePendingBindings()
            //  binding.moreView.setVisibility(View.GONE);


            //  binding.moreView.setVisibility(View.GONE);
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
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.dialog_cancel_case_id)
                val btYes =
                    dialog.findViewById<TextView>(R.id.btYes)
                val ivLabImage =
                    dialog.findViewById<EditText>(R.id.ivLabImage)
                val btNo = dialog.findViewById<TextView>(R.id.btNo)

                btNo.setOnClickListener {
                    dialog.dismiss()
                }
                apiService.cancelCaseIdResponse.observe(lifecycleOwner) {
                    when (it) {
                        is NetworkResult.Error -> {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show();
                            dialog
                                .dismiss()
                        }

                        is NetworkResult.Loading -> {

                        }

                        is NetworkResult.Success -> {
                            if (it.data != null) {
                                apiService.getPagingData()
                                if (it.data.status == "1") {
                                    Toast.makeText(context, it.data.message, Toast.LENGTH_SHORT)
                                        .show();
                                }
                                else{
                                    Toast.makeText(context, it.data.message, Toast.LENGTH_SHORT)
                                        .show();
                                }
                            }
                            dialog
                                .dismiss()

                        }
                    }
                }
                btYes.setOnClickListener {
                 if(ivLabImage.text.toString().isNotEmpty() && ivLabImage.text.isNotBlank()){
                     apiService.cancelCaseId(Leads.caseId.toString(), ivLabImage.text.toString())
                 }else{
                     ivLabImage.setError("this field cannot be empty")
                 }


                }
                dialog.show()
            }
        }
    }

    override fun onBindViewHolder(holder: CancelCasesViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(
                it,
                activity,
                apiService,
                lifecycleOwner = lifecycleOwner
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancelCasesViewHolder {

        val binding = inflate<LayoutCancelCaseIdBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_cancel_case_id,
            parent,
            false
        )

        return CancelCasesViewHolder(binding, apiService, activity, lifecycleOwner = lifecycleOwner)
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