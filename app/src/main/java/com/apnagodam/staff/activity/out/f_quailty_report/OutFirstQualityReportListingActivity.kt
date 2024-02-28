package com.apnagodam.staff.activity.out.f_quailty_report

import android.app.Activity
import android.content.Intent
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
import com.apnagodam.staff.activity.`in`.first_quality_reports.UploadFirstQualtityReportsClass
import com.apnagodam.staff.activity.out.f_katha_parchi.OutUploadFirrstkantaParchiClass
import com.apnagodam.staff.adapter.OutFirstQualityReportAdapter
import com.apnagodam.staff.databinding.ActivityListingBinding
import com.apnagodam.staff.module.FirstQuilityReportListResponse
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class OutFirstQualityReportListingActivity : BaseActivity<ActivityListingBinding?>() {
    private lateinit var outFirstQualityReportAdapter: OutFirstQualityReportAdapter
    private var pageOffset = 1
    private var totalPage = 0
    private lateinit var AllCases: ArrayList<FirstQuilityReportListResponse.Datum>
    private var ReportsFile: String? = null
    private var CommudityImage: String? = null

    val qualityReportViewModel by viewModels<QualitReportViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_listing
    }

    override fun setUp() {
        binding!!.pageNextPrivious.visibility = View.VISIBLE
        AllCases = arrayListOf()
        getAllCases("")
        setSupportActionBar(binding!!.toolbar)
        binding!!.titleHeader.text = resources.getString(R.string.f_quality_repots)
        binding!!.tvId.text = resources.getString(R.string.case_idd)
        binding!!.tvMoreView.text = resources.getString(R.string.more_view_truck)
        binding!!.tvPhone.text = resources.getString(R.string.quality_repots)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        /*   binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(FirstQualityReportListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(FirstQualityReportListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);*/
        binding!!.swipeRefresherStock.setOnRefreshListener { getAllCases("") }
        binding!!.ivClose.setOnClickListener { finish() }
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
            val builder = AlertDialog.Builder(this@OutFirstQualityReportListingActivity)
            val inflater = (this@OutFirstQualityReportListingActivity as Activity).layoutInflater
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
                if (notes.text.toString().trim { it <= ' ' } != null && !notes.text.toString()
                        .trim { it <= ' ' }
                        .isEmpty()) {
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

    override fun onResume() {
        super.onResume()
        getAllCases("")
    }

    private fun setAdapter() {
        binding!!.rvDefaultersStatus.addItemDecoration(
            DividerItemDecoration(
                this@OutFirstQualityReportListingActivity,
                LinearLayoutManager.VERTICAL
            )
        )
        binding!!.rvDefaultersStatus.setHasFixedSize(true)
        binding!!.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager = LinearLayoutManager(
            this@OutFirstQualityReportListingActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager

        binding!!.rvDefaultersStatus.adapter = outFirstQualityReportAdapter
    }

    private fun getAllCases(search: String) {
        qualityReportViewModel.getFirstQualityListing("10", "" + pageOffset, "OUT", search)
        qualityReportViewModel.fQualityResponse.observe(this){
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
                        totalPage = it.data!!.quilityReport.lastPage
                        AllCases!!.addAll(it.data!!.quilityReport.data)
                        outFirstQualityReportAdapter = OutFirstQualityReportAdapter(
                            AllCases,
                            this@OutFirstQualityReportListingActivity,
                            activity
                        )
                        setAdapter()
                        // AllCases = body.getData();
                        // binding.rvDefaultersStatus.setAdapter(new FirstQualityReportAdapter(body.getData(), FirstQualityReportListingActivity.this));
                    }
                }
            }
        }



    }

    fun ViewData(position: Int) {
        val displayRectangle = Rect()
        val window = this.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val builder = AlertDialog.Builder(
            this@OutFirstQualityReportListingActivity,
            R.style.CustomAlertDialog
        )
        val inflater = this@OutFirstQualityReportListingActivity.layoutInflater
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
        val case_extra = dialogView.findViewById<LinearLayout>(R.id.case_extra)
        case_extra.visibility = View.VISIBLE
        val price_extra = dialogView.findViewById<LinearLayout>(R.id.price_extra)
        price_extra.visibility = View.VISIBLE
        val photo_extra = dialogView.findViewById<LinearLayout>(R.id.photo_extra)
        photo_extra.visibility = View.VISIBLE
        val gate_pass = dialogView.findViewById<TextView>(R.id.gate_pass)
        val user = dialogView.findViewById<TextView>(R.id.user)
        val coldwin = dialogView.findViewById<TextView>(R.id.coldwin)
        val purchase_details = dialogView.findViewById<TextView>(R.id.purchase_details)
        val loan_details = dialogView.findViewById<TextView>(R.id.loan_details)
        val selas_details = dialogView.findViewById<TextView>(R.id.selas_details)
        val converted_by = dialogView.findViewById<TextView>(R.id.converted_by)
        val reports_file = dialogView.findViewById<ImageView>(R.id.reports_file)
        val commodity_file = dialogView.findViewById<ImageView>(R.id.commodity_file)
        /////////////////////////////////////////////
        if (AllCases!![position]!!.imge == null || AllCases!![position]!!.imge.isEmpty()) {
            reports_file.visibility = View.GONE
        }
        if (AllCases!![position]!!.commodity_img == null || AllCases!![position]!!.commodity_img.isEmpty()) {
            commodity_file.visibility = View.GONE
        }
        ReportsFile = Constants.First_quality + AllCases!![position]!!
            .imge
        CommudityImage = Constants.First_quality + AllCases!![position]!!
            .commodity_img
        reports_file.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@OutFirstQualityReportListingActivity,
                R.layout.popup_photo_full,
                view,
                ReportsFile,
                null
            )
        }
        commodity_file.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@OutFirstQualityReportListingActivity,
                R.layout.popup_photo_full,
                view,
                CommudityImage,
                null
            )
        }
        case_id.text = resources.getString(R.string.case_idd)
        username.text = resources.getString(R.string.total_weight)
        lead_id.text = "" + AllCases!![position]!!.caseId
        genrated_by.text = "" + if (AllCases!![position]!!
                .totalWeight != null
        ) AllCases!![position]!!.totalWeight else "N/A"
        customer_name.text = "" + AllCases!![position]!!.custFname
        vehicle_no.text = resources.getString(R.string.moisture_level)
        phone_no.text = "" + if (AllCases!![position]!!
                .moistureLevel != null
        ) AllCases!![position]!!.moistureLevel else "N/A"
        processing_fees.text = resources.getString(R.string.tcw)
        location_title.text = "" + if (AllCases!![position]!!
                .thousandCrownW != null
        ) AllCases!![position]!!.thousandCrownW else "N/A"
        inteerset_rate.text = resources.getString(R.string.broken)
        commodity_name.text = "" + if (AllCases!![position]!!
                .broken != null
        ) AllCases!![position]!!.broken else "N/A"
        transport_rate.text = resources.getString(R.string.fm_level)
        est_quantity_nmae.text = "" + if (AllCases!![position]!!
                .foreignMatter != null
        ) AllCases!![position]!!.foreignMatter else "N/A"
        loan.text = resources.getString(R.string.thin)
        terminal_name.text = "" + if (AllCases!![position]!!
                .thin != null
        ) AllCases!![position]!!.thin else "N/A"
        price.text = resources.getString(R.string.dehuck)
        purpose_name.text = "" + if (AllCases!![position]!!
                .damage != null
        ) AllCases!![position]!!.damage else "N/A"
        rent.text = resources.getString(R.string.discolor)
        commitemate_date.text = "" + if (AllCases!![position]!!
                .blackSmith != null
        ) AllCases!![position]!!.blackSmith else "N/A"
        labour_rent.text = resources.getString(R.string.infested)
        create_date.text = "" + if (AllCases!![position]!!
                .infested != null
        ) AllCases!![position]!!.infested else "N/A"
        total_weight.text = resources.getString(R.string.live)
        converted_by.text = "" + if (AllCases!![position]!!
                .liveInsects != null
        ) AllCases!![position]!!.liveInsects else "N/A"
        notes.text = resources.getString(R.string.packaging)
        total_weights.text = "" + if (AllCases!![position]!!
                .packagingType != null
        ) AllCases!![position]!!.packagingType else "N/A"
        bags.text = resources.getString(R.string.notes)
        total_bags.text = "" + if (AllCases!![position]!!
                .notes != null
        ) AllCases!![position]!!.notes else "N/A"
        gate_pass.text = "Gaatepass/CDF Name : " + if (AllCases!![position]!!
                .gatePassCdfUserName != null
        ) AllCases!![position]!!.gatePassCdfUserName else "N/A"
        coldwin.text = "ColdWin Name: " + if (AllCases!![position]!!
                .coldwinName != null
        ) AllCases!![position]!!.coldwinName else "N/A"
        user.text = "User : " + if (AllCases!![position]!!
                .fpoUserId != null
        ) AllCases!![position]!!.fpoUserId else "N/A"
        purchase_details.text = "purchase Details : " + if (AllCases!![position]!!
                .purchaseName != null
        ) AllCases!![position]!!.purchaseName else "N/A"
        loan_details.text = "Loan Details : " + if (AllCases!![position]!!
                .loanName != null
        ) AllCases!![position]!!.loanName else "N/A"
        selas_details.text = "Sale Details : " + if (AllCases!![position]!!
                .saleName != null
        ) AllCases!![position]!!.saleName else "N/A"
        ///////////////////////////////////////////////////
        cancel_btn.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }

    fun checkVeehicleNo(postion: Int) {
        var intent = Intent(this, UploadOutFirstQualtityReportsClass::class.java)
        intent.putExtra("user_name", AllCases!![postion]!!.custFname)
        intent.putExtra("case_id",AllCases!![postion]!!.caseId)
        intent.putExtra("vehicle_no", AllCases!![postion]!!.vehicleNo)

        startActivity(intent)
    }
}
