package com.apnagodam.staff.activity.`in`.truckbook

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallbackWProgress
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.TruckBookViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.adapter.TruckBookAdapter
import com.apnagodam.staff.databinding.ActivityListingBinding
import com.apnagodam.staff.module.AllTruckBookListResponse
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class TruckBookListingActivity() : BaseActivity<ActivityListingBinding?>() {
    private var truckBookAdapter: TruckBookAdapter? = null
    private var pageOffset = 1
    private var totalPage = 0
    private var AllCases: MutableList<AllTruckBookListResponse.Datum?>? = null
    private var TruckImage: String? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_listing
    }
    private val truckViewModel  : TruckBookViewModel by viewModels<TruckBookViewModel>()

    override fun setUp() {
        AllCases = arrayListOf()
        getAllCases("")

        setAdapter()
        setSupportActionBar(binding!!.toolbar)
        binding!!.titleHeader.text = resources.getString(R.string.truck_book)
        binding!!.tvId.text = resources.getString(R.string.case_idd)
        binding!!.tvMoreView.text = resources.getString(R.string.more_view_truck)
        binding!!.tvPhone.text = resources.getString(R.string.truck_book)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        /* binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(TruckBookListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(TruckBookListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);*/
        binding!!.swipeRefresherStock.setOnRefreshListener(OnRefreshListener { getAllCases("") })
        binding!!.ivClose.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                onBackPressed()
            }
        })
        binding!!.tvPrevious.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (pageOffset != 1) {
                    pageOffset--
                    getAllCases("")
                }
            }
        })
        binding!!.tvNext.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (totalPage != pageOffset) {
                    pageOffset++
                    getAllCases("")
                }
            }
        })
        binding!!.filterIcon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val builder = AlertDialog.Builder(this@TruckBookListingActivity)
                val inflater = (this@TruckBookListingActivity as Activity).layoutInflater
                val dialogView = inflater.inflate(R.layout.fiter_diloag, null)
                val notes = dialogView.findViewById<View>(R.id.notes) as EditText
                val submit = dialogView.findViewById<View>(R.id.btn_submit) as Button
                val cancel_btn = dialogView.findViewById<View>(R.id.cancel_btn) as ImageView
                builder.setView(dialogView)
                builder.setCancelable(false)
                val alertDialog = builder.create()
                alertDialog.show()
                cancel_btn.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View) {
                        alertDialog.dismiss()
                    }
                })
                submit.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View) {
                        if (notes.text.toString()
                                .trim { it <= ' ' } != null && !notes.text.toString()
                                .trim { it <= ' ' }
                                .isEmpty()
                        ) {
                            alertDialog.dismiss()
                            pageOffset = 1
                            getAllCases(notes.text.toString().trim { it <= ' ' })
                            //     ClosedPricing(alertDialog, AllCases.get(postion).getCaseId(), notes.getText().toString().trim());
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Please Fill Text",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })
                //  setDateTimeField();
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun setAdapter() {
        binding!!.rvDefaultersStatus.addItemDecoration(
            DividerItemDecoration(
                this@TruckBookListingActivity,
                LinearLayoutManager.HORIZONTAL
            )
        )
        binding!!.rvDefaultersStatus.setHasFixedSize(true)
        binding!!.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager =
            LinearLayoutManager(this@TruckBookListingActivity, LinearLayoutManager.VERTICAL, false)
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager
        truckBookAdapter = TruckBookAdapter(AllCases, this@TruckBookListingActivity, activity)
        binding!!.rvDefaultersStatus.adapter = truckBookAdapter
        //        pagination(horizontalLayoutManager);
    }

    private fun getAllCases(search: String) {
        showDialog()
        truckViewModel.getTruckBookList("10",pageOffset,"IN",search)
        truckViewModel.response.observe(this){
            when(it){

                is NetworkResult.Error -> {
                    hideDialog()
                }
                is NetworkResult.Loading -> {
                    showDialog()
                }
                is NetworkResult.Success ->{
                    binding!!.swipeRefresherStock.isRefreshing = false
                    AllCases!!.clear()
                    if (it.data!!.truckBookCollection == null) {
                        binding!!.txtemptyMsg.visibility = View.VISIBLE
                        binding!!.rvDefaultersStatus.visibility = View.GONE
                        binding!!.pageNextPrivious.visibility = View.GONE
                    } else {
                        // AllCases=body.getTruckBookCollection().getData();
                        AllCases!!.clear()
                        totalPage = it.data.truckBookCollection.lastPage
                        AllCases!!.addAll(it.data.truckBookCollection.data)
                        truckBookAdapter!!.notifyDataSetChanged()
                        //                    binding.rvDefaultersStatus.setAdapter(new TruckBookAdapter(body.getTruckBookCollection(), TruckBookListingActivity.this));
                    }
                    hideDialog()
                }
            }

        }

    }

    fun ViewData(position: Int) {
        val displayRectangle = Rect()
        val window = this.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val builder = AlertDialog.Builder(this@TruckBookListingActivity, R.style.CustomAlertDialog)
        val inflater = this@TruckBookListingActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.lead_view, null)
        dialogView.minimumWidth = (displayRectangle.width() * 1f).toInt()
        dialogView.minimumHeight = (displayRectangle.height() * 1f).toInt()
        builder.setView(dialogView)
        val alertDialog = builder.create()
        val cancel_btn = dialogView.findViewById<ImageView>(R.id.cancel_btn)
        val lead_id = dialogView.findViewById<TextView>(R.id.lead_id)
        val genrated_by = dialogView.findViewById<TextView>(R.id.genrated_by)
        val customer_name = dialogView.findViewById<TextView>(R.id.customer_name)
        val phone_no = dialogView.findViewById<TextView>(R.id.phone_no)
        val location_title = dialogView.findViewById<TextView>(R.id.location_title)
        val commodity_name = dialogView.findViewById<TextView>(R.id.commodity_name)
        val est_quantity_nmae = dialogView.findViewById<TextView>(R.id.est_quantity_nmae)
        val terminal_name = dialogView.findViewById<TextView>(R.id.terminal_name)
        val purpose_name = dialogView.findViewById<TextView>(R.id.purpose_name)
        val commitemate_date = dialogView.findViewById<TextView>(R.id.commitemate_date)
        val total_weights = dialogView.findViewById<TextView>(R.id.vehicle_no)
        val total_bags = dialogView.findViewById<TextView>(R.id.in_out)
        val create_date = dialogView.findViewById<TextView>(R.id.create_date)
        val case_id = dialogView.findViewById<TextView>(R.id.a1)
        val username = dialogView.findViewById<TextView>(R.id.a2)
        val vehicle_no = dialogView.findViewById<TextView>(R.id.a4)
        val processing_fees = dialogView.findViewById<TextView>(R.id.a5)
        val inteerset_rate = dialogView.findViewById<TextView>(R.id.a6)
        val transport_rate = dialogView.findViewById<TextView>(R.id.a7)
        val loan = dialogView.findViewById<TextView>(R.id.a8)
        val price = dialogView.findViewById<TextView>(R.id.a9)
        val rent = dialogView.findViewById<TextView>(R.id.a10)
        val labour_rent = dialogView.findViewById<TextView>(R.id.a11)
        val total_weight = dialogView.findViewById<TextView>(R.id.a12)
        val notes = dialogView.findViewById<TextView>(R.id.a13)
        val bags = dialogView.findViewById<TextView>(R.id.a14)
        val price_extra = dialogView.findViewById<LinearLayout>(R.id.price_extra)
        price_extra.visibility = View.VISIBLE
        val case_extra = dialogView.findViewById<LinearLayout>(R.id.case_extra)
        case_extra.visibility = View.VISIBLE
        val truck_extra = dialogView.findViewById<LinearLayout>(R.id.truck_extra)
        truck_extra.visibility = View.VISIBLE
        val converted_by = dialogView.findViewById<TextView>(R.id.converted_by)
        val gate_pass = dialogView.findViewById<TextView>(R.id.gate_pass)
        val user = dialogView.findViewById<TextView>(R.id.user)
        val coldwin = dialogView.findViewById<TextView>(R.id.coldwin)
        val purchase_details = dialogView.findViewById<TextView>(R.id.purchase_details)
        val loan_details = dialogView.findViewById<TextView>(R.id.loan_details)
        val selas_details = dialogView.findViewById<TextView>(R.id.selas_details)
        val gate_passs = dialogView.findViewById<TextView>(R.id.gate_passs)
        val total_trans_cost = dialogView.findViewById<TextView>(R.id.total_trans_cost)
        val advance_patyment = dialogView.findViewById<TextView>(R.id.advance_patyment)
        val start_date_time = dialogView.findViewById<TextView>(R.id.start_date_time)
        val end_date_time = dialogView.findViewById<TextView>(R.id.end_date_time)
        val settleememt_amount = dialogView.findViewById<TextView>(R.id.settleememt_amount)
        gate_passs.text = "" + (if ((AllCases!!.get(position)!!
                .gatePass) != null
        ) AllCases!!.get(position)!!.gatePass else "N/A")
        total_trans_cost.text = "" + (if ((AllCases!!.get(position)!!
                .totalTransportCost) != null
        ) AllCases!!.get(position)!!.totalTransportCost else "N/A")
        advance_patyment.text = "" + (if ((AllCases!!.get(position)!!
                .advancePayment) != null
        ) AllCases!!.get(position)!!.advancePayment else "N/A")
        start_date_time.text = "" + (if ((AllCases!!.get(position)!!
                .startDateTime) != null
        ) AllCases!!.get(position)!!.startDateTime else "N/A")
        end_date_time.text = "" + (if ((AllCases!!.get(position)!!
                .endDateTime) != null
        ) AllCases!!.get(position)!!.endDateTime else "N/A")
        settleememt_amount.text = "" + (if ((AllCases!!.get(position)!!
                .finalSettlementAmount) != null
        ) AllCases!!.get(position)!!.finalSettlementAmount else "N/A")
        total_bags.text = "" + (if ((AllCases!!.get(position)!!
                .noOfBags) != null
        ) AllCases!!.get(position)!!.noOfBags else "N/A")
        total_weights.text = "" + (if ((AllCases!!.get(position)!!
                .notes) != null
        ) AllCases!!.get(position)!!.notes else "N/A")
        converted_by.text = "" + (if ((AllCases!!.get(position)!!
                .totalWeight) != null
        ) AllCases!!.get(position)!!.totalWeight else "N/A")
        gate_pass.text = "Gaatepass/CDF Name : " + (if ((AllCases!!.get(position)!!
                .gatePassCdfUserName) != null
        ) AllCases!!.get(position)!!.gatePassCdfUserName else "N/A")
        coldwin.text = "ColdWin Name: " + (if ((AllCases!!.get(position)!!
                .coldwinName) != null
        ) AllCases!!.get(position)!!.coldwinName else "N/A")
        user.text = "User : " + (if ((AllCases!!.get(position)!!
                .fpoUserId) != null
        ) AllCases!!.get(position)!!.fpoUserId else "N/A")
        purchase_details.text = "purchase Details : " + (if ((AllCases!!.get(position)!!
                .purchaseName) != null
        ) AllCases!!.get(position)!!.purchaseName else "N/A")
        loan_details.text = "Loan Details : " + (if ((AllCases!!.get(position)!!
                .loanName) != null
        ) AllCases!!.get(position)!!.loanName else "N/A")
        selas_details.text = "Sale Details : " + (if ((AllCases!!.get(position)!!
                .saleName) != null
        ) AllCases!!.get(position)!!.saleName else "N/A")
        total_weight.text = resources.getString(R.string.total_weight)
        bags.text = resources.getString(R.string.bags)
        case_id.text = resources.getString(R.string.case_idd)
        notes.text = resources.getString(R.string.notes)
        rent.text = resources.getString(R.string.turnaround_time)
        labour_rent.text = resources.getString(R.string.commodity)
        loan.text = resources.getString(R.string.min_weight)
        transport_rate.text = resources.getString(R.string.transport_rate)
        price.text = resources.getString(R.string.max_weight)
        vehicle_no.text = resources.getString(R.string.vehicle_noa)
        processing_fees.text = resources.getString(R.string.driver_name)
        inteerset_rate.text = resources.getString(R.string.driver_phone_no)
        username.text = resources.getString(R.string.transporter)
        lead_id.text = "" + AllCases!!.get(position)!!.caseId
        genrated_by.text = "" + (if ((AllCases!!.get(position)!!
                .transporter) != null
        ) AllCases!!.get(position)!!.transporter else "N/A")
        customer_name.text = "" + AllCases!!.get(position)!!.custFname
        location_title.text = "" + (if ((AllCases!!.get(position)!!
                .driverName) != null
        ) AllCases!!.get(position)!!.driverName else "N/A")
        phone_no.text = "" + (if ((AllCases!!.get(position)!!
                .vehicle) != null
        ) AllCases!!.get(position)!!.vehicle else "N/A")
        commodity_name.text = "" + (if ((AllCases!!.get(position)!!
                .driverPhone) != null
        ) AllCases!!.get(position)!!.driverPhone else "N/A")
        est_quantity_nmae.text = "" + (if ((AllCases!!.get(position)!!
                .ratePerKm) != null
        ) AllCases!!.get(position)!!.ratePerKm else "N/A")
        terminal_name.text = "" + (if ((AllCases!!.get(position)!!
                .minWeight) != null
        ) AllCases!!.get(position)!!.minWeight else "N/A")
        purpose_name.text = "" + (if ((AllCases!!.get(position)!!
                .maxWeight) != null
        ) AllCases!!.get(position)!!.maxWeight else "N/A")
        commitemate_date.text = "" + (if ((AllCases!!.get(position)!!
                .turnaroundTime) != null
        ) AllCases!!.get(position)!!.turnaroundTime else "N/A")
        create_date.text = "" + (if ((AllCases!!.get(position)!!
                .commodityId) != null
        ) AllCases!!.get(position)!!.commodityId else "N/A")
        val truck_file = dialogView.findViewById<View>(R.id.Bilty_Image) as ImageView
        val biltyLL = dialogView.findViewById<View>(R.id.biltyLL) as LinearLayout
        biltyLL.visibility = View.VISIBLE
        if (AllCases!![position]!!.file == null || AllCases!![position]!!.file.isEmpty()) {
            truck_file.visibility = View.GONE
        }
        TruckImage = Constants.Truck_bilty_photo + AllCases!![position]!!
            .file
        truck_file.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                PhotoFullPopupWindow(
                    this@TruckBookListingActivity,
                    R.layout.popup_photo_full,
                    view,
                    TruckImage,
                    null
                )
            }
        })
        cancel_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                alertDialog.dismiss()
            }
        })
        alertDialog.show()
    }

    fun checkVeehicleNo(postion: Int) {
        val bundle = Bundle()
        bundle.putString("user_name", AllCases!![postion]!!.custFname)
        bundle.putString("case_id", AllCases!![postion]!!.caseId)
        bundle.putString("vehicle_no", AllCases!![postion]!!.vehicleNo)
        startActivity(TruckUploadDetailsClass::class.java, bundle)
        /* apiService.cheeckvehiclePricicng(AllCases.get(postion).getCaseId()).enqueue(new NetworkCallback<VehcilePricingCheeck>(getActivity()) {
            @Override
            protected void onSuccess(VehcilePricingCheeck body) {
                if (body.getVichel_status().equalsIgnoreCase("1")) {
                    Toast.makeText(getApplicationContext(), body.getMessage(), Toast.LENGTH_LONG).show();
                } else {

                }
            }
        });*/
    }
}
