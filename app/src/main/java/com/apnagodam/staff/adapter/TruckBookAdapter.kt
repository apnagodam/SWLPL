package com.apnagodam.staff.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Base.BaseRecyclerViewAdapter
import com.apnagodam.staff.Base.BaseViewHolder
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.`in`.truckbook.TruckBookListingActivity
import com.apnagodam.staff.databinding.PricingDataBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.AllLeadsResponse.Lead
import com.apnagodam.staff.module.AllTruckBookListResponse

class TruckBookAdapter(
    private val Leads: MutableList<AllTruckBookListResponse.Datum?>,
    truckBookListingActivity: TruckBookListingActivity,
    activity: BaseActivity<*>
) : BaseRecyclerViewAdapter() {
    private val context: Context
    private val activity: BaseActivity<*>

    init {
        context = truckBookListingActivity
        this.activity = activity
    }

    override fun getItemCount(): Int {
        return Leads.size
    }
    override fun inflateLayout(view: ViewDataBinding): BaseViewHolder<*> {
        return DefaultersTopHolder(view)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.pricing_data
    }

    override fun getCollection(): Collection<*>? {
        return null
    }



    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        holder.onBind(position)
    }



    internal inner class DefaultersTopHolder(itemBinding: ViewDataBinding) :
        BaseViewHolder<PricingDataBinding?>(itemBinding) {
        @SuppressLint("SetTextI18n")
        override fun onBind(position: Int) {
          if(binding!=null){

              binding.let {
                  it!!.tvActionDone.setVisibility(View.GONE)
                  it.tvAction.setVisibility(View.GONE)

                  /* if (position==0){

                  }else {*/if (position % 2 == 0) {
                  it.getRoot().setBackgroundColor(Color.parseColor("#EBEBEB"))
              } else {
                  it.getRoot().setBackgroundColor(Color.WHITE)
              }

                  it.tvId.setText("" + Leads[position]!!.caseId)
                  it.tvName.setText(Leads[position]!!.custFname)
                  if (Leads[position]!!.tbCaseId != null) {
                      it.tvAction.setVisibility(View.GONE)
                      it.tvActionDone.setVisibility(View.GONE)
                      it.tvPhone.setVisibility(View.GONE)
                      it.tvPhoneDone.setVisibility(View.VISIBLE)
                  } else {
                      it.tvAction.setVisibility(View.GONE)
                      it.tvPhone.setText(context.resources.getString(R.string.upload_details))
                      it.tvPhone.setBackgroundColor(context.resources.getColor(R.color.yellow))
                      setAllData(it.tvPhone, it.tvPhoneDone, position)

                      /*
                      for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                          if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("15")) {
                              if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getEdit() == 1) {
                                  if (Leads.get(position).getP_case_id() != null) {
                                      binding.tvPhone.setVisibility(View.VISIBLE);
                                  } else {
                                      binding.tvPhone.setVisibility(View.GONE);
                                      binding.tvPhoneDone.setVisibility(View.VISIBLE);
                                      binding.tvPhoneDone.setText("Processing...");
                                      binding.tvPhoneDone.setTextColor(context.getResources().getColor(R.color.yellow));
                                  }
                              } else {
                                  binding.tvPhone.setVisibility(View.GONE);
                                  binding.tvPhoneDone.setVisibility(View.VISIBLE);
                                  binding.tvPhoneDone.setText("In Process");
                                  binding.tvPhoneDone.setTextColor(context.getResources().getColor(R.color.lead_btn));
                              }
                          }
                      }
      */
                  }
                  it.tvId.setTextColor(Color.BLACK)
                  it.tvName.setTextColor(Color.BLACK)
                  it.tvPhone.setTextColor(Color.BLACK)
                  activity.hideDialog()
                  it.view.setOnClickListener(View.OnClickListener {
                      if (context is TruckBookListingActivity) {
                          (context as TruckBookListingActivity).ViewData(position)
                      }
                  })
                  it.tvPhone.setOnClickListener(View.OnClickListener {
                      if (context is TruckBookListingActivity) {
                          (context as TruckBookListingActivity).checkVeehicleNo(position)
                      }
                  })
              }
          }
        }
    }

    private fun setAllData(tvPhone: TextView, tvPhoneDone: TextView, position: Int) {
        try {
            Thread {
                for (i in SharedPreferencesRepository.getDataManagerInstance()
                    .getUserPermission().indices) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission()
                            .get(i).getPermissionId().equals("15", ignoreCase = true)
                    ) {
                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission()
                                .get(i).getEdit() == 1
                        ) {
                            if (Leads[position]!!.p_case_id != null) {
                                tvPhone.setVisibility(View.VISIBLE)
                            } else {
                                tvPhone.setVisibility(View.GONE)
                                tvPhoneDone.setVisibility(View.VISIBLE)
                                tvPhoneDone.setText("Processing...")
                                tvPhoneDone.setTextColor(context.resources.getColor(R.color.yellow))
                            }
                        } else {
                            tvPhone.setVisibility(View.GONE)
                            tvPhoneDone.setVisibility(View.VISIBLE)
                            tvPhoneDone.setText("In Process")
                            tvPhoneDone.setTextColor(context.resources.getColor(R.color.lead_btn))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
