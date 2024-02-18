package com.apnagodam.staff.activity.out.labourbook

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
import com.apnagodam.staff.Network.viewmodel.LabourViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.adapter.OUTLaabourBookAdapter
import com.apnagodam.staff.databinding.ActivityListingBinding
import com.apnagodam.staff.module.AllLabourBookListResponse
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class OUTLabourBookListingActivity : BaseActivity<ActivityListingBinding?>() {
    private var laabourBookAdapter: OUTLaabourBookAdapter? = null
    private var pageOffset = 1
    private var totalPage = 0
    private lateinit var AllCases: ArrayList<AllLabourBookListResponse.Datum>
    val labourViewModel by viewModels<LabourViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_listing
    }

    override fun setUp() {
        binding!!.pageNextPrivious.visibility = View.VISIBLE
        AllCases = arrayListOf()
        setAdapter()
        setSupportActionBar(binding!!.toolbar)
        binding!!.titleHeader.text = resources.getString(R.string.labour_book)
        binding!!.tvId.text = resources.getString(R.string.case_idd)
        binding!!.tvMoreView.text = resources.getString(R.string.more_view_truck)
        binding!!.tvPhone.text = resources.getString(R.string.labour_book)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        /*  binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(LabourBookListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(LabourBookListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);*/getAllCases("")
        binding!!.swipeRefresherStock.setOnRefreshListener { getAllCases("") }
        binding!!.ivClose.setOnClickListener {
            finish()
            //  startActivityAndClear(StaffDashBoardActivity.class);
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
            val builder = AlertDialog.Builder(this@OUTLabourBookListingActivity)
            val inflater = (this@OUTLabourBookListingActivity as Activity).layoutInflater
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

    private fun setAdapter() {
        binding!!.rvDefaultersStatus.addItemDecoration(
            DividerItemDecoration(
                this@OUTLabourBookListingActivity,
                LinearLayoutManager.VERTICAL
            )
        )
        binding!!.rvDefaultersStatus.setHasFixedSize(true)
        binding!!.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager = LinearLayoutManager(
            this@OUTLabourBookListingActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager
        laabourBookAdapter =
            OUTLaabourBookAdapter(AllCases, this@OUTLabourBookListingActivity, activity)
        binding!!.rvDefaultersStatus.adapter = laabourBookAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        //  startActivityAndClear(StaffDashBoardActivity.class);
    }

    private fun getAllCases(search: String) {
        labourViewModel.getLabourList("10",  pageOffset.toString(), "OUT", search)
        labourViewModel.labourResponse.observe(this)
        {
            when(it){
                is NetworkResult.Error ->hideDialog()
                is NetworkResult.Loading -> showDialog()
                is NetworkResult.Success ->{
                    if (it.data!=null){
                        binding!!.swipeRefresherStock.isRefreshing = false
                        AllCases!!.clear()
                        if (it.data.labour.data == null) {
                            binding!!.txtemptyMsg.visibility = View.VISIBLE
                            binding!!.rvDefaultersStatus.visibility = View.GONE
                            binding!!.pageNextPrivious.visibility = View.GONE
                        } else if (it.data.labour.lastPage == 1) {
                            binding!!.txtemptyMsg.visibility = View.GONE
                            binding!!.rvDefaultersStatus.visibility = View.VISIBLE
                            binding!!.pageNextPrivious.visibility = View.GONE
                            totalPage = it.data.labour.lastPage
                            AllCases!!.addAll(it.data.labour.data)
                            laabourBookAdapter!!.notifyDataSetChanged()
                        } else {
                            AllCases!!.clear()
                            totalPage = it.data.labour.lastPage
                            AllCases!!.addAll(it.data.labour.data)
                            laabourBookAdapter!!.notifyDataSetChanged()
                            // AllCases = body.getCurrentPageCollection();
                            // binding.rvDefaultersStatus.setAdapter(new LaabourBookAdapter(body.getCurrentPageCollection(), LabourBookListingActivity.this));
                        }
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
            AlertDialog.Builder(this@OUTLabourBookListingActivity, R.style.CustomAlertDialog)
        val inflater = (this@OUTLabourBookListingActivity as Activity).layoutInflater
        val dialogView = inflater.inflate(R.layout.lead_view, null)
        dialogView.minimumWidth = (displayRectangle.width() * 1f).toInt()
        dialogView.minimumHeight = (displayRectangle.height() * 1f).toInt()
        builder.setView(dialogView)
        val alertDialog = builder.create()
        val cancel_btn = dialogView.findViewById<View>(R.id.cancel_btn) as ImageView
        val case_id = dialogView.findViewById<View>(R.id.a1) as TextView
        val lead_id = dialogView.findViewById<View>(R.id.lead_id) as TextView
        val username = dialogView.findViewById<View>(R.id.a2) as TextView
        val genrated_by = dialogView.findViewById<View>(R.id.genrated_by) as TextView
        val customer_name = dialogView.findViewById<View>(R.id.customer_name) as TextView
        val vehicle_no = dialogView.findViewById<View>(R.id.a4) as TextView
        val phone_no = dialogView.findViewById<View>(R.id.phone_no) as TextView
        val location_title = dialogView.findViewById<View>(R.id.location_title) as TextView
        val inteerset_rate = dialogView.findViewById<View>(R.id.a6) as TextView
        val commodity_name = dialogView.findViewById<View>(R.id.commodity_name) as TextView
        val transport_rate = dialogView.findViewById<View>(R.id.a7) as TextView
        val est_quantity_nmae = dialogView.findViewById<View>(R.id.est_quantity_nmae) as TextView
        val loan = dialogView.findViewById<View>(R.id.a8) as TextView
        val terminal_name = dialogView.findViewById<View>(R.id.terminal_name) as TextView
        val price = dialogView.findViewById<View>(R.id.a9) as TextView
        val purpose_name = dialogView.findViewById<View>(R.id.purpose_name) as TextView
        val rent = dialogView.findViewById<View>(R.id.a10) as TextView
        val commitemate_date = dialogView.findViewById<View>(R.id.commitemate_date) as TextView
        val llcreate = dialogView.findViewById<View>(R.id.llcreate) as LinearLayout
        val viewcreate = dialogView.findViewById(R.id.viewcreate) as View
        llcreate.visibility = View.GONE
        viewcreate.visibility = View.GONE
        val price_extra = dialogView.findViewById<View>(R.id.price_extra) as LinearLayout
        price_extra.visibility = View.VISIBLE
        val gate_pass = dialogView.findViewById<View>(R.id.gate_pass) as TextView
        val user = dialogView.findViewById<View>(R.id.user) as TextView
        val coldwin = dialogView.findViewById<View>(R.id.coldwin) as TextView
        val purchase_details = dialogView.findViewById<View>(R.id.purchase_details) as TextView
        val loan_details = dialogView.findViewById<View>(R.id.loan_details) as TextView
        val selas_details = dialogView.findViewById<View>(R.id.selas_details) as TextView

        ////
        case_id.text = resources.getString(R.string.case_idd)
        lead_id.text = "" + AllCases!![position]!!.caseId
        username.text = resources.getString(R.string.labour_contractor)
        genrated_by.text = "" + if (AllCases!![position]!!
                .labourContractor != null
        ) AllCases!![position]!!.labourContractor else "N/A"
        vehicle_no.text = resources.getString(R.string.contractor_phone)
        phone_no.text = "" + if (AllCases!![position]!!
                .contractorNo != null
        ) AllCases!![position]!!.contractorNo else "N/A"
        customer_name.text = "" + AllCases!![position]!!.custFname
        location_title.text = "" + if (AllCases!![position]!!
                .l_b_location != null
        ) AllCases!![position]!!.l_b_location else "N/A"
        inteerset_rate.text = resources.getString(R.string.labour_rate)
        commodity_name.text = "" + if (AllCases!![position]!!
                .labourRatePerBags != null
        ) AllCases!![position]!!.labourRatePerBags else "N/A"
        transport_rate.text = resources.getString(R.string.labour_total)
        est_quantity_nmae.text = "" + if (AllCases!![position]!!
                .totalLabour != null
        ) AllCases!![position]!!.totalLabour else "N/A"
        loan.text = resources.getString(R.string.booking_date)
        terminal_name.text = "" + if (AllCases!![position]!!
                .bookingDate != null
        ) AllCases!![position]!!.bookingDate else "N/A"
        price.text = resources.getString(R.string.total_bags)
        purpose_name.text = "" + if (AllCases!![position]!!
                .totalBags != null
        ) AllCases!![position]!!.totalBags else "N/A"
        rent.text = resources.getString(R.string.notes)
        commitemate_date.text = "" + if (AllCases!![position]!!
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
        /////
        cancel_btn.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }

    fun checkVeehicleNo(postion: Int) {
        val bundle = Bundle()
        bundle.putString("user_name", AllCases!![postion]!!.custFname)
        bundle.putString("case_id", AllCases!![postion]!!.caseId)
        startActivity(OUTLabourBookUploadClass::class.java, bundle)
    }
}
