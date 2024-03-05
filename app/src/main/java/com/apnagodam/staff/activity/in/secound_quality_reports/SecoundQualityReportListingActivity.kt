package com.apnagodam.staff.activity.`in`.secound_quality_reports

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.QualitReportViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.adapter.SecoundQualityReportAdapter
import com.apnagodam.staff.databinding.ActivityListingBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.SecoundQuilityReportListResponse
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class SecoundQualityReportListingActivity() : BaseActivity<ActivityListingBinding?>() {
    private var ReportsFile: String? = null
    private var CommudityImage: String? = null
    private lateinit var secoundQualityReportAdapter: SecoundQualityReportAdapter
    private var pageOffset = 1
    private var totalPage = 0
    private lateinit var AllCases: ArrayList<SecoundQuilityReportListResponse.Datum>
    override fun getLayoutResId(): Int {
        return R.layout.activity_listing
    }

    val qualityReportViewModel by viewModels<QualitReportViewModel>()
    override fun setUp() {
        getAllCases("")
        binding!!.rvDefaultersStatus.addItemDecoration(
            DividerItemDecoration(
                this@SecoundQualityReportListingActivity,
                LinearLayoutManager.VERTICAL
            )
        )
        binding!!.pageNextPrivious.visibility = View.VISIBLE
        AllCases = arrayListOf()
        binding!!.swipeRefresherStock.setOnRefreshListener(OnRefreshListener { getAllCases("") })
        setSupportActionBar(binding!!.toolbar)
        binding!!.titleHeader.text = resources.getString(R.string.s_quality_repots)
        binding!!.tvId.text = resources.getString(R.string.case_idd)
        binding!!.tvMoreView.text = resources.getString(R.string.more_view_truck)
        binding!!.tvPhone.text = resources.getString(R.string.quality_repots)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        /* binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(SecoundQualityReportListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(SecoundQualityReportListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);*/
        binding!!.ivClose.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                onBackPressedDispatcher.onBackPressed()

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
                val builder = AlertDialog.Builder(this@SecoundQualityReportListingActivity)
                val inflater = (this@SecoundQualityReportListingActivity as Activity).layoutInflater
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

    override fun onResume() {
        super.onResume()
        getAllCases("")
    }
    private fun setAdapter() {

        binding!!.rvDefaultersStatus.setHasFixedSize(true)
        binding!!.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager = LinearLayoutManager(
            this@SecoundQualityReportListingActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager

        binding!!.rvDefaultersStatus.adapter = secoundQualityReportAdapter
    }

    private fun getAllCases(search: String) {
        showDialog()
        qualityReportViewModel.getSecondQualityListing("10", "" + pageOffset, "IN", search)
        qualityReportViewModel.sQualityResponse.observe(this){
            when(it){
                is NetworkResult.Error -> hideDialog()
                is NetworkResult.Loading ->showDialog()
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
                        var userDetails = SharedPreferencesRepository.getDataManagerInstance().user

                        for (i in it.data!!.quilityReport.data.indices) {

                            if (userDetails.terminal == null) {
                                AllCases!!.add(it.data!!.quilityReport.data[i])
                            } else if (it.data!!.quilityReport.data[i].terminalId.toString() == userDetails.terminal.toString()) {
                                AllCases!!.add(it.data!!.quilityReport.data[i])

                            } else break


                        }
                        secoundQualityReportAdapter = SecoundQualityReportAdapter(
                            AllCases,
                            this@SecoundQualityReportListingActivity,
                            activity
                        )
                        setAdapter()

                        //     AllCases = body.getData();
                        //     binding.rvDefaultersStatus.setAdapter(new SecoundQualityReportAdapter(body.getData(), SecoundQualityReportListingActivity.this));
                    }
                }
            }
        }


    }

    fun ViewData(position: Int) {
        val displayRectangle = Rect()
        val window = this.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val builder =
            AlertDialog.Builder(this@SecoundQualityReportListingActivity, R.style.CustomAlertDialog)
        val inflater = (this@SecoundQualityReportListingActivity as Activity).layoutInflater
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
        ReportsFile = Constants.Secound__quality + AllCases!![position]!!
            .imge
        CommudityImage = Constants.Secound__quality + AllCases!![position]!!
            .commodity_img
        reports_file.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                PhotoFullPopupWindow(
                    this@SecoundQualityReportListingActivity,
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
                    this@SecoundQualityReportListingActivity,
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

        var intent = Intent(this,UploadSecoundQualtityReportsClass::class.java)
        intent.putExtra("user_name", AllCases!![postion]!!.custFname)
        intent.putExtra("case_id",AllCases!![postion]!!.caseId)
        intent.putExtra("vehicle_no", AllCases!![postion]!!.vehicleNo)
        intent.putExtra("skp_bags",AllCases[postion].skpBags)
        intent.putExtra("skp_weight",AllCases[postion].skpWeight)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
