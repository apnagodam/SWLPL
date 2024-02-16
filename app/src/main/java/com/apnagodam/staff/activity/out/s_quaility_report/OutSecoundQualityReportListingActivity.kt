package com.apnagodam.staff.activity.out.s_quaility_report

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
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.activity.`in`.secound_quality_reports.UploadSecoundQualtityReportsClass
import com.apnagodam.staff.adapter.OutSecoundQualityReportAdapter
import com.apnagodam.staff.databinding.ActivityListingBinding
import com.apnagodam.staff.module.SecoundQuilityReportListResponse
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OutSecoundQualityReportListingActivity() : BaseActivity<ActivityListingBinding?>() {
    private var ReportsFile: String? = null
    private var CommudityImage: String? = null
    private lateinit var outSecoundQualityReportAdapter: OutSecoundQualityReportAdapter
    private var pageOffset = 1
    private var totalPage = 0
    private lateinit var AllCases: ArrayList<SecoundQuilityReportListResponse.Datum>
    override fun getLayoutResId(): Int {
        return R.layout.activity_listing
    }

    override fun setUp() {
        binding!!.pageNextPrivious.visibility = View.VISIBLE
        AllCases = arrayListOf()
        setAdapter()
        binding!!.swipeRefresherStock.setOnRefreshListener(OnRefreshListener { getAllCases("") })
        setSupportActionBar(binding!!.toolbar)
        binding!!.titleHeader.text = resources.getString(R.string.s_quality_repots)
        binding!!.tvId.text = resources.getString(R.string.case_idd)
        binding!!.tvMoreView.text = resources.getString(R.string.more_view_truck)
        binding!!.tvPhone.text = resources.getString(R.string.quality_repots)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        /* binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(SecoundQualityReportListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(SecoundQualityReportListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);*/getAllCases("")
        binding!!.ivClose.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                startActivityAndClear(StaffDashBoardActivity::class.java)
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
                val builder = AlertDialog.Builder(this@OutSecoundQualityReportListingActivity)
                val inflater =
                    (this@OutSecoundQualityReportListingActivity as Activity).layoutInflater
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

    private fun setAdapter() {
        binding!!.rvDefaultersStatus.addItemDecoration(
            DividerItemDecoration(
                this@OutSecoundQualityReportListingActivity,
                LinearLayoutManager.VERTICAL
            )
        )
        binding!!.rvDefaultersStatus.setHasFixedSize(true)
        binding!!.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager = LinearLayoutManager(
            this@OutSecoundQualityReportListingActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager
        outSecoundQualityReportAdapter = OutSecoundQualityReportAdapter(
            AllCases,
            this@OutSecoundQualityReportListingActivity,
            activity
        )
        binding!!.rvDefaultersStatus.adapter = outSecoundQualityReportAdapter
    }

    private fun getAllCases(search: String) {
        showDialog()

        apiService.getS_qualityReportsList("10", "" + pageOffset, "OUT", search)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {body->   binding!!.swipeRefresherStock.isRefreshing = false
                AllCases!!.clear()
                if (body.quilityReport == null) {
                    binding!!.txtemptyMsg.visibility = View.VISIBLE
                    binding!!.rvDefaultersStatus.visibility = View.GONE
                    binding!!.pageNextPrivious.visibility = View.GONE
                } else {
                    AllCases!!.clear()
                    totalPage = body.quilityReport.lastPage
                    AllCases!!.addAll(body.quilityReport.data)
                    outSecoundQualityReportAdapter!!.notifyDataSetChanged()
                    //     AllCases = body.getData();
                    //     binding.rvDefaultersStatus.setAdapter(new SecoundQualityReportAdapter(body.getData(), SecoundQualityReportListingActivity.this));
                } }
            .subscribe()

    }

    fun ViewData(position: Int) {
        val displayRectangle = Rect()
        val window = this.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val builder = AlertDialog.Builder(
            this@OutSecoundQualityReportListingActivity,
            R.style.CustomAlertDialog
        )
        val inflater = this@OutSecoundQualityReportListingActivity.layoutInflater
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
        ReportsFile = Constants.Secound__quality + AllCases!![position]!!
            .imge
        CommudityImage = Constants.Secound__quality + AllCases!![position]!!
            .commodity_img
        reports_file.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                PhotoFullPopupWindow(
                    this@OutSecoundQualityReportListingActivity,
                    R.layout.popup_photo_full,
                    view,
                    ReportsFile,
                    null
                )
            }
        })
        commodity_file.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                PhotoFullPopupWindow(
                    this@OutSecoundQualityReportListingActivity,
                    R.layout.popup_photo_full,
                    view,
                    CommudityImage,
                    null
                )
            }
        })
        case_id.text = resources.getString(R.string.case_idd)
        username.text = resources.getString(R.string.total_weight)
        lead_id.text = "" + AllCases!!.get(position)!!.caseId
        genrated_by.text = "" + (if ((AllCases!!.get(position)!!
                .totalWeight) != null
        ) AllCases!!.get(position)!!.totalWeight else "N/A")
        customer_name.text = "" + AllCases!!.get(position)!!.custFname
        vehicle_no.text = resources.getString(R.string.moisture_level)
        phone_no.text = "" + (if ((AllCases!!.get(position)!!
                .moistureLevel) != null
        ) AllCases!!.get(position)!!.moistureLevel else "N/A")
        processing_fees.text = resources.getString(R.string.tcw)
        location_title.text = "" + (if ((AllCases!!.get(position)!!
                .thousandCrownW) != null
        ) AllCases!!.get(position)!!.thousandCrownW else "N/A")
        inteerset_rate.text = resources.getString(R.string.broken)
        commodity_name.text = "" + (if ((AllCases!!.get(position)!!
                .broken) != null
        ) AllCases!!.get(position)!!.broken else "N/A")
        transport_rate.text = resources.getString(R.string.fm_level)
        est_quantity_nmae.text = "" + (if ((AllCases!!.get(position)!!
                .foreignMatter) != null
        ) AllCases!!.get(position)!!.foreignMatter else "N/A")
        loan.text = resources.getString(R.string.thin)
        terminal_name.text = "" + (if ((AllCases!!.get(position)!!
                .thin) != null
        ) AllCases!!.get(position)!!.thin else "N/A")
        price.text = resources.getString(R.string.dehuck)
        purpose_name.text = "" + (if ((AllCases!!.get(position)!!
                .damage) != null
        ) AllCases!!.get(position)!!.damage else "N/A")
        rent.text = resources.getString(R.string.discolor)
        commitemate_date.text = "" + (if ((AllCases!!.get(position)!!
                .blackSmith) != null
        ) AllCases!!.get(position)!!.blackSmith else "N/A")
        labour_rent.text = resources.getString(R.string.infested)
        create_date.text = "" + (if ((AllCases!!.get(position)!!
                .infested) != null
        ) AllCases!!.get(position)!!.infested else "N/A")
        total_weight.text = resources.getString(R.string.live)
        converted_by.text = "" + (if ((AllCases!!.get(position)!!
                .liveInsects) != null
        ) AllCases!!.get(position)!!.liveInsects else "N/A")
        notes.text = resources.getString(R.string.packaging)
        total_weights.text = "" + (if ((AllCases!!.get(position)!!
                .packagingType) != null
        ) AllCases!!.get(position)!!.packagingType else "N/A")
        bags.text = resources.getString(R.string.notes)
        total_bags.text = "" + (if ((AllCases!!.get(position)!!
                .notes) != null
        ) AllCases!!.get(position)!!.notes else "N/A")
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
        ///////////////////////////////////////////////////
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
        startActivity(UploadSecoundQualtityReportsClass::class.java, bundle)
    }
}
