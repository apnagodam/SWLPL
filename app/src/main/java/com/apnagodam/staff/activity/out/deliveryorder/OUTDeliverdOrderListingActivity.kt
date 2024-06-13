package com.apnagodam.staff.activity.out.deliveryorder

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallbackWProgress
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.OrdersViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.adapter.OUTDeliverdOrderAdapter
import com.apnagodam.staff.databinding.ActivityListingBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.ReleaseOrderPojo
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OUTDeliverdOrderListingActivity : BaseActivity<ActivityListingBinding?>() {
    private var truckBookAdapter: OUTDeliverdOrderAdapter? = null
    private var pageOffset = 1
    private var totalPage = 0
    private var AllCases: MutableList<ReleaseOrderPojo.Datum?>? = null
    private var TruckImage: String? = null
    override fun getLayoutResId(): Int {
        return R.layout.activity_listing
    }
    val ordersViewModel by viewModels<OrdersViewModel>()
    override fun setUp() {
        AllCases = arrayListOf()
        binding!!.rvDefaultersStatus.addItemDecoration(DividerItemDecoration(this@OUTDeliverdOrderListingActivity, LinearLayoutManager.HORIZONTAL))
        setAdapter()
        setSupportActionBar(binding!!.toolbar)
        binding!!.titleHeader.text = resources.getString(R.string.out_deiverd_book)
        binding!!.tvId.text = resources.getString(R.string.case_idd)
        binding!!.tvMoreView.text = resources.getString(R.string.more_view_truck)
        binding!!.tvPhone.text = resources.getString(R.string.out_relase_book)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        getAllCases("")
        binding!!.swipeRefresherStock.setOnRefreshListener { getAllCases("") }
        binding!!.ivClose.setOnClickListener { startActivityAndClear(StaffDashBoardActivity::class.java) }
        binding!!.tvPrevious.setOnClickListener {
            if (pageOffset != 1) {
                pageOffset--
                getAllCases("")
            }
        }
        binding!!.tvNext.setOnClickListener {
            if (totalPage != pageOffset) {
                pageOffset++
                getAllCases("")
            }
        }
        binding!!.filterIcon.setOnClickListener {
            val builder = AlertDialog.Builder(this@OUTDeliverdOrderListingActivity)
            val inflater = (this@OUTDeliverdOrderListingActivity as Activity).layoutInflater
            val dialogView = inflater.inflate(R.layout.fiter_diloag, null)
            val notes = dialogView.findViewById<View>(R.id.notes) as EditText
            val submit = dialogView.findViewById<View>(R.id.btn_submit) as Button
            val cancel_btn = dialogView.findViewById<View>(R.id.cancel_btn) as ImageView
            builder.setView(dialogView)
            builder.setCancelable(false)
            val alertDialog = builder.create()
            alertDialog.show()
            cancel_btn.setOnClickListener { alertDialog.dismiss() }
            submit.setOnClickListener {
                if (notes.text.toString().trim { it <= ' ' } != null && !notes.text.toString().trim { it <= ' ' }.isEmpty()) {
                    alertDialog.dismiss()
                    pageOffset = 1
                    getAllCases(notes.text.toString().trim { it <= ' ' })
                    //     ClosedPricing(alertDialog, AllCases.get(postion).getCaseId(), notes.getText().toString().trim());
                } else {
                    Toast.makeText(applicationContext, "Please Fill Text", Toast.LENGTH_LONG).show()
                }
            }
            //  setDateTimeField();
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityAndClear(StaffDashBoardActivity::class.java)
    }

    private fun setAdapter() {
        binding!!.rvDefaultersStatus.setHasFixedSize(true)
        binding!!.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager = LinearLayoutManager(this@OUTDeliverdOrderListingActivity, LinearLayoutManager.VERTICAL, false)
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager
        truckBookAdapter = OUTDeliverdOrderAdapter(AllCases, this@OUTDeliverdOrderListingActivity, activity)
        binding!!.rvDefaultersStatus.adapter = truckBookAdapter
        //        pagination(horizontalLayoutManager);
    }

    private fun getAllCases(search: String) {
        showDialog()
        ordersViewModel.getOrdersList("10", pageOffset, "OUT", search)
        ordersViewModel.ordersResponse.observe(this){
            when(it){
                is NetworkResult.Error -> hideDialog()
                is NetworkResult.Loading -> showDialog()
                is NetworkResult.Success -> {
                    binding!!.swipeRefresherStock.isRefreshing = false
                    AllCases!!.clear()
                    if (it.data!!.data == null) {
                        binding!!.txtemptyMsg.visibility = View.VISIBLE
                        binding!!.rvDefaultersStatus.visibility = View.GONE
                        binding!!.pageNextPrivious.visibility = View.GONE
                    } else {
                        AllCases!!.clear()
                        totalPage = it.data!!.data.lastPage
                        var userDetails = SharedPreferencesRepository.getDataManagerInstance().user

                        for (i in it.data!!.data.data.indices) {

                            if (userDetails.terminal == null) {
                                AllCases!!.add(it.data!!.data.data[i])
                            } else if (it.data!!.data.data[i].terminalId.toString() == userDetails.terminal.toString()) {
                                AllCases!!.add(it.data!!.data.data[i])

                            }


                        }
                     //   AllCases!!.addAll(it.data!!.data.data)
                        truckBookAdapter!!.notifyDataSetChanged()
                    }
                }
            }
        }

    }

    fun ViewData(position: Int) {
        val displayRectangle = Rect()
        val window = this.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val builder = AlertDialog.Builder(this@OUTDeliverdOrderListingActivity, R.style.CustomAlertDialog)
        val inflater = this@OUTDeliverdOrderListingActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.release_order_view, null)
        dialogView.minimumWidth = (displayRectangle.width() * 1f).toInt()
        dialogView.minimumHeight = (displayRectangle.height() * 1f).toInt()
        builder.setView(dialogView)
        val alertDialog = builder.create()
        val cancel_btn = dialogView.findViewById<ImageView>(R.id.cancel_btn)
        val lead_id = dialogView.findViewById<TextView>(R.id.lead_id)
        val customer_name = dialogView.findViewById<TextView>(R.id.customer_name)
        val gate_pass = dialogView.findViewById<TextView>(R.id.gate_pass)
        val user = dialogView.findViewById<TextView>(R.id.user)
        val coldwin = dialogView.findViewById<TextView>(R.id.coldwin)
        val purchase_details = dialogView.findViewById<TextView>(R.id.purchase_details)
        val loan_details = dialogView.findViewById<TextView>(R.id.loan_details)
        val selas_details = dialogView.findViewById<TextView>(R.id.selas_details)
        val notes = dialogView.findViewById<TextView>(R.id.notes)
        gate_pass.text = "Gaatepass/CDF Name : " + if (AllCases!![position]!!.gatePassCdfUserName != null) AllCases!![position]!!.gatePassCdfUserName else "N/A"
        coldwin.text = "ColdWin Name: " + if (AllCases!![position]!!.coldwinName != null) AllCases!![position]!!.coldwinName else "N/A"
        user.text = "User : " + if (AllCases!![position]!!.fpoUserId != null) AllCases!![position]!!.fpoUserId else "N/A"
        purchase_details.text = "purchase Details : " + if (AllCases!![position]!!.purchaseName != null) AllCases!![position]!!.purchaseName else "N/A"
        loan_details.text = "Loan Details : " + if (AllCases!![position]!!.loanName != null) AllCases!![position]!!.loanName else "N/A"
        selas_details.text = "Sale Details : " + if (AllCases!![position]!!.saleName != null) AllCases!![position]!!.saleName else "N/A"
        lead_id.text = "" + AllCases!![position]!!.caseId
        customer_name.text = "" + AllCases!![position]!!.custFname
        val reports_file = dialogView.findViewById<View>(R.id.reports_file) as ImageView
        notes.text = "" + AllCases!![position]!!.notes
        if (AllCases!![position]!!.file == null || AllCases!![position]!!.file.isEmpty()) {
            reports_file.visibility = View.GONE
        }
        TruckImage = Constants.delivery_order_photo + AllCases!![position]!!.file
        reports_file.setOnClickListener { view -> PhotoFullPopupWindow(this@OUTDeliverdOrderListingActivity, R.layout.popup_photo_full, view, TruckImage, null) }
        cancel_btn.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }

    fun checkVeehicleNo(postion: Int) {
        val bundle = Bundle()
        bundle.putString("user_name", AllCases!![postion]!!.custFname)
        bundle.putString("case_id", AllCases!![postion]!!.caseId)
        startActivity(OUTDeliverdUpoladOrderClass::class.java, bundle)
    }
}
