package com.apnagodam.staff.activity.`in`.first_quality_reports

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
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.QualitReportViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.adapter.FirstQualityReportAdapter
import com.apnagodam.staff.databinding.ActivityListingBinding
import com.apnagodam.staff.module.FirstQuilityReportListResponse
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class FirstQualityReportListingActivity : BaseActivity<ActivityListingBinding?>() {
    private var firstQualityReportAdapter: FirstQualityReportAdapter? = null
    private var pageOffset = 1
    private var totalPage = 0
    private var AllCases: MutableList<FirstQuilityReportListResponse.Datum?>? = null
    private var ReportsFile: String? = null
    private var CommudityImage: String? = null
    val qualitReportViewModel by viewModels<QualitReportViewModel>()

    override fun getLayoutResId(): Int {
        return R.layout.activity_listing
    }

    override fun setUp() {



        binding!!.pageNextPrivious.visibility = View.VISIBLE
        AllCases = arrayListOf()
        setAdapter()
        setSupportActionBar(binding!!.toolbar)
        binding!!.titleHeader.text = resources.getString(R.string.f_quality_repots)
        binding!!.tvId.text = resources.getString(R.string.case_idd)
        binding!!.tvMoreView.text = resources.getString(R.string.more_view_truck)
        binding!!.tvPhone.text = resources.getString(R.string.quality_repots)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        /*   binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(FirstQualityReportListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(FirstQualityReportListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);*/getAllCases("")
        binding!!.swipeRefresherStock.setOnRefreshListener { getAllCases("") }
        binding!!.ivClose.setOnClickListener {
             startActivityAndClear(StaffDashBoardActivity::class.java)
        }
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
            val builder = AlertDialog.Builder(this@FirstQualityReportListingActivity)
            val inflater = (this@FirstQualityReportListingActivity as Activity).layoutInflater
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
        binding!!.rvDefaultersStatus.addItemDecoration(DividerItemDecoration(this@FirstQualityReportListingActivity, LinearLayoutManager.VERTICAL))
        binding!!.rvDefaultersStatus.setHasFixedSize(true)
        binding!!.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager = LinearLayoutManager(this@FirstQualityReportListingActivity, LinearLayoutManager.VERTICAL, false)
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager
        firstQualityReportAdapter = FirstQualityReportAdapter(AllCases, this@FirstQualityReportListingActivity, activity)
        binding!!.rvDefaultersStatus.adapter = firstQualityReportAdapter
    }

    private fun getAllCases(search: String) {
        qualitReportViewModel.getFirstQualityListing("10",pageOffset.toString(),"IN",search)
        qualitReportViewModel.fQualityResponse.observe(this){
            when(it){
                is NetworkResult.Error -> hideDialog()
                is NetworkResult.Loading -> showDialog()
                is NetworkResult.Success -> {
                    binding!!.swipeRefresherStock.isRefreshing = false
                    AllCases!!.clear()
                    if (it.data!!.quilityReport == null) {
                        binding!!.txtemptyMsg.visibility = View.VISIBLE
                        binding!!.rvDefaultersStatus.visibility = View.GONE
                        binding!!.pageNextPrivious.visibility = View.GONE
                    } else {
                        AllCases!!.clear()
                        totalPage = it.data.quilityReport.lastPage
                        AllCases!!.addAll(it.data.quilityReport.data)
                        firstQualityReportAdapter!!.notifyDataSetChanged()
                        // AllCases = body.getData();
                        // binding.rvDefaultersStatus.setAdapter(new FirstQualityReportAdapter(body.getData(), FirstQualityReportListingActivity.this));
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
        val builder = AlertDialog.Builder(this@FirstQualityReportListingActivity, R.style.CustomAlertDialog)
        val inflater = (this@FirstQualityReportListingActivity as Activity).layoutInflater
        val dialogView = inflater.inflate(R.layout.lead_view, null)
        dialogView.minimumWidth = (displayRectangle.width() * 1f).toInt()
        dialogView.minimumHeight = (displayRectangle.height() * 1f).toInt()
        builder.setView(dialogView)
        val alertDialog = builder.create()
        val cancel_btn = dialogView.findViewById<View>(R.id.cancel_btn) as ImageView
        val lead_id = dialogView.findViewById<View>(R.id.lead_id) as TextView
        val genrated_by = dialogView.findViewById<View>(R.id.genrated_by) as TextView
        val customer_name = dialogView.findViewById<View>(R.id.customer_name) as TextView
        val phone_no = dialogView.findViewById<View>(R.id.phone_no) as TextView
        val location_title = dialogView.findViewById<View>(R.id.location_title) as TextView
        val commodity_name = dialogView.findViewById<View>(R.id.commodity_name) as TextView
        val est_quantity_nmae = dialogView.findViewById<View>(R.id.est_quantity_nmae) as TextView
        val terminal_name = dialogView.findViewById<View>(R.id.terminal_name) as TextView
        val purpose_name = dialogView.findViewById<View>(R.id.purpose_name) as TextView
        val commitemate_date = dialogView.findViewById<View>(R.id.commitemate_date) as TextView
        val total_weights = dialogView.findViewById<View>(R.id.vehicle_no) as TextView
        val total_bags = dialogView.findViewById<View>(R.id.in_out) as TextView
        val create_date = dialogView.findViewById<View>(R.id.create_date) as TextView
        val case_id = dialogView.findViewById<View>(R.id.a1) as TextView
        val username = dialogView.findViewById<View>(R.id.a2) as TextView
        val vehicle_no = dialogView.findViewById<View>(R.id.a4) as TextView
        val processing_fees = dialogView.findViewById<View>(R.id.a5) as TextView
        val inteerset_rate = dialogView.findViewById<View>(R.id.a6) as TextView
        val transport_rate = dialogView.findViewById<View>(R.id.a7) as TextView
        val loan = dialogView.findViewById<View>(R.id.a8) as TextView
        val price = dialogView.findViewById<View>(R.id.a9) as TextView
        val rent = dialogView.findViewById<View>(R.id.a10) as TextView
        val labour_rent = dialogView.findViewById<View>(R.id.a11) as TextView
        val total_weight = dialogView.findViewById<View>(R.id.a12) as TextView
        val notes = dialogView.findViewById<View>(R.id.a13) as TextView
        val bags = dialogView.findViewById<View>(R.id.a14) as TextView
        val case_extra = dialogView.findViewById<View>(R.id.case_extra) as LinearLayout
        case_extra.visibility = View.VISIBLE
        val price_extra = dialogView.findViewById<View>(R.id.price_extra) as LinearLayout
        price_extra.visibility = View.VISIBLE
        val photo_extra = dialogView.findViewById<View>(R.id.photo_extra) as LinearLayout
        photo_extra.visibility = View.VISIBLE
        val gate_pass = dialogView.findViewById<View>(R.id.gate_pass) as TextView
        val user = dialogView.findViewById<View>(R.id.user) as TextView
        val coldwin = dialogView.findViewById<View>(R.id.coldwin) as TextView
        val purchase_details = dialogView.findViewById<View>(R.id.purchase_details) as TextView
        val loan_details = dialogView.findViewById<View>(R.id.loan_details) as TextView
        val selas_details = dialogView.findViewById<View>(R.id.selas_details) as TextView
        val converted_by = dialogView.findViewById<View>(R.id.converted_by) as TextView
        val reports_file = dialogView.findViewById<View>(R.id.reports_file) as ImageView
        val commodity_file = dialogView.findViewById<View>(R.id.commodity_file) as ImageView
        /////////////////////////////////////////////
        if (AllCases!![position]!!.imge == null || AllCases!![position]!!.imge.isEmpty()) {
            reports_file.visibility = View.GONE
        }
        if (AllCases!![position]!!.commodity_img == null || AllCases!![position]!!.commodity_img.isEmpty()) {
            commodity_file.visibility = View.GONE
        }
        ReportsFile = Constants.First_quality + AllCases!![position]!!.imge
        CommudityImage = Constants.First_quality + AllCases!![position]!!.commodity_img
        reports_file.setOnClickListener { view -> PhotoFullPopupWindow(this@FirstQualityReportListingActivity, R.layout.popup_photo_full, view, ReportsFile, null) }
        commodity_file.setOnClickListener { view -> PhotoFullPopupWindow(this@FirstQualityReportListingActivity, R.layout.popup_photo_full, view, CommudityImage, null) }
        case_id.text = resources.getString(R.string.case_idd)
        username.text = resources.getString(R.string.total_weight)
        lead_id.text = "" + AllCases!![position]!!.caseId
        genrated_by.text = "" + if (AllCases!![position]!!.totalWeight != null) AllCases!![position]!!.totalWeight else "N/A"
        customer_name.text = "" + AllCases!![position]!!.custFname
        vehicle_no.text = resources.getString(R.string.moisture_level)
        phone_no.text = "" + if (AllCases!![position]!!.moistureLevel != null) AllCases!![position]!!.moistureLevel else "N/A"
        processing_fees.text = resources.getString(R.string.tcw)
        location_title.text = "" + if (AllCases!![position]!!.thousandCrownW != null) AllCases!![position]!!.thousandCrownW else "N/A"
        inteerset_rate.text = resources.getString(R.string.broken)
        commodity_name.text = "" + if (AllCases!![position]!!.broken != null) AllCases!![position]!!.broken else "N/A"
        transport_rate.text = resources.getString(R.string.fm_level)
        est_quantity_nmae.text = "" + if (AllCases!![position]!!.foreignMatter != null) AllCases!![position]!!.foreignMatter else "N/A"
        loan.text = resources.getString(R.string.thin)
        terminal_name.text = "" + if (AllCases!![position]!!.thin != null) AllCases!![position]!!.thin else "N/A"
        price.text = resources.getString(R.string.dehuck)
        purpose_name.text = "" + if (AllCases!![position]!!.damage != null) AllCases!![position]!!.damage else "N/A"
        rent.text = resources.getString(R.string.discolor)
        commitemate_date.text = "" + if (AllCases!![position]!!.blackSmith != null) AllCases!![position]!!.blackSmith else "N/A"
        labour_rent.text = resources.getString(R.string.infested)
        create_date.text = "" + if (AllCases!![position]!!.infested != null) AllCases!![position]!!.infested else "N/A"
        total_weight.text = resources.getString(R.string.live)
        converted_by.text = "" + if (AllCases!![position]!!.liveInsects != null) AllCases!![position]!!.liveInsects else "N/A"
        notes.text = resources.getString(R.string.packaging)
        total_weights.text = "" + if (AllCases!![position]!!.packagingType != null) AllCases!![position]!!.packagingType else "N/A"
        bags.text = resources.getString(R.string.notes)
        total_bags.text = "" + if (AllCases!![position]!!.notes != null) AllCases!![position]!!.notes else "N/A"
        gate_pass.text = "Gaatepass/CDF Name : " + if (AllCases!![position]!!.gatePassCdfUserName != null) AllCases!![position]!!.gatePassCdfUserName else "N/A"
        coldwin.text = "ColdWin Name: " + if (AllCases!![position]!!.coldwinName != null) AllCases!![position]!!.coldwinName else "N/A"
        user.text = "User : " + if (AllCases!![position]!!.fpoUserId != null) AllCases!![position]!!.fpoUserId else "N/A"
        purchase_details.text = "purchase Details : " + if (AllCases!![position]!!.purchaseName != null) AllCases!![position]!!.purchaseName else "N/A"
        loan_details.text = "Loan Details : " + if (AllCases!![position]!!.loanName != null) AllCases!![position]!!.loanName else "N/A"
        selas_details.text = "Sale Details : " + if (AllCases!![position]!!.saleName != null) AllCases!![position]!!.saleName else "N/A"
        ///////////////////////////////////////////////////
        cancel_btn.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }

    fun checkVeehicleNo(postion: Int) {
        val bundle = Bundle()
        bundle.putString("user_name", AllCases!![postion]!!.custFname)
        bundle.putString("case_id", AllCases!![postion]!!.caseId)
        bundle.putString("vehicle_no", AllCases!![postion]!!.vehicleNo)
        startActivity(UploadFirstQualtityReportsClass::class.java, bundle)
    }
}
