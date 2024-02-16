package com.apnagodam.staff.activity.out.s_katha_parchi

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.adapter.OutSecoundkanthaparchiAdapter
import com.apnagodam.staff.databinding.ActivityListingBinding
import com.apnagodam.staff.module.SecoundkanthaParchiListResponse
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class OutSecoundkanthaParchiListingActivity : BaseActivity<ActivityListingBinding?>() {
    private var firstkantaParchiFile: String? = null
    private var TruckImage: String? = null
    private var outSecoundkanthaparchiAdapter: OutSecoundkanthaparchiAdapter? = null
    private var pageOffset = 1
    private var totalPage = 0
    private lateinit var AllCases: ArrayList<SecoundkanthaParchiListResponse.Datum>
    override fun getLayoutResId(): Int {
        return R.layout.activity_listing
    }

    override fun setUp() {
        binding!!.pageNextPrivious.visibility = View.VISIBLE
        AllCases = arrayListOf()
        setAdapter()
        setSupportActionBar(binding!!.toolbar)
        binding!!.titleHeader.text = resources.getString(R.string.secoundkanta_parchi)
        binding!!.tvId.text = resources.getString(R.string.case_idd)
        binding!!.tvMoreView.text = resources.getString(R.string.more_view_truck)
        binding!!.tvPhone.text = resources.getString(R.string.kanta_parchi)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.swipeRefresherStock.setOnRefreshListener { getAllCases("") }
        /*  binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(SecoundkanthaParchiListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(SecoundkanthaParchiListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);*/getAllCases("")
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
            val builder = AlertDialog.Builder(this@OutSecoundkanthaParchiListingActivity)
            val inflater = (this@OutSecoundkanthaParchiListingActivity as Activity).layoutInflater
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
                this@OutSecoundkanthaParchiListingActivity,
                LinearLayoutManager.VERTICAL
            )
        )
        binding!!.rvDefaultersStatus.setHasFixedSize(true)
        binding!!.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager = LinearLayoutManager(
            this@OutSecoundkanthaParchiListingActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager
        outSecoundkanthaparchiAdapter = OutSecoundkanthaparchiAdapter(
            AllCases,
            this@OutSecoundkanthaParchiListingActivity,
            activity
        )
        binding!!.rvDefaultersStatus.adapter = outSecoundkanthaparchiAdapter
    }

    private fun getAllCases(search: String) {
        showDialog()
        val parchiListResponseObservable =
            apiService.getS_kanthaParchiList("10", pageOffset.toString(), "OUT", search)
                .subscribeOn(Schedulers.io())
                .doOnNext { body ->
                    binding!!.swipeRefresherStock.isRefreshing = false
                    AllCases!!.clear()
                    if (body.secoundKataParchiDatum == null) {
                        binding!!.txtemptyMsg.visibility = View.VISIBLE
                        binding!!.rvDefaultersStatus.visibility = View.GONE
                        binding!!.pageNextPrivious.visibility = View.GONE
                    } else {
                        AllCases!!.clear()
                        totalPage = body.secoundKataParchiDatum.lastPage
                        AllCases!!.addAll(body.secoundKataParchiDatum.data)
                        outSecoundkanthaparchiAdapter!!.notifyDataSetChanged()
                        //  AllCases = body.getData();
                        //  binding.rvDefaultersStatus.setAdapter(new SecoundkanthaparchiAdapter(body.getData(), SecoundkanthaParchiListingActivity.this));
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())

    }

    fun ViewData(position: Int) {
        val displayRectangle = Rect()
        val window = this.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val builder = AlertDialog.Builder(
            this@OutSecoundkanthaParchiListingActivity,
            R.style.CustomAlertDialog
        )
        val inflater = this@OutSecoundkanthaParchiListingActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.dilog_kanta_parchi, null)
        dialogView.minimumWidth = (displayRectangle.width() * 1f).toInt()
        dialogView.minimumHeight = (displayRectangle.height() * 1f).toInt()
        builder.setView(dialogView)
        val alertDialog = builder.create()
        val cancel_btn = dialogView.findViewById<ImageView>(R.id.cancel_btn)
        val case_id = dialogView.findViewById<TextView>(R.id.a1)
        val lead_id = dialogView.findViewById<TextView>(R.id.lead_id)
        val customer_name = dialogView.findViewById<TextView>(R.id.customer_name)
        val kanta_parchi_file = dialogView.findViewById<ImageView>(R.id.kanta_parchi_file)
        val truck_file = dialogView.findViewById<ImageView>(R.id.truck_file)
        val notes = dialogView.findViewById<TextView>(R.id.notes)
        val gate_pass = dialogView.findViewById<TextView>(R.id.gate_pass)
        val user = dialogView.findViewById<TextView>(R.id.user)
        val coldwin = dialogView.findViewById<TextView>(R.id.coldwin)
        val purchase_details = dialogView.findViewById<TextView>(R.id.purchase_details)
        val loan_details = dialogView.findViewById<TextView>(R.id.loan_details)
        val selas_details = dialogView.findViewById<TextView>(R.id.selas_details)
        ////
        if (AllCases!![position]!!.file == null || AllCases!![position]!!.file.isEmpty()) {
            kanta_parchi_file.visibility = View.GONE
        }
        if (AllCases!![position]!!.file2 == null || AllCases!![position]!!.file2.isEmpty()) {
            truck_file.visibility = View.GONE
        }
        case_id.text = resources.getString(R.string.case_idd)
        lead_id.text = "" + AllCases!![position]!!.caseId
        customer_name.text = "" + AllCases!![position]!!.custFname
        notes.text = "" + if (AllCases!![position]!!
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
        firstkantaParchiFile = Constants.Secound_kata + AllCases!![position]!!
            .file
        TruckImage = Constants.Secound_kata + AllCases!![position]!!.file2
        kanta_parchi_file.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@OutSecoundkanthaParchiListingActivity,
                R.layout.popup_photo_full,
                view,
                firstkantaParchiFile,
                null
            )
        }
        truck_file.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@OutSecoundkanthaParchiListingActivity,
                R.layout.popup_photo_full,
                view,
                TruckImage,
                null
            )
        }
        cancel_btn.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }

    fun checkVeehicleNo(postion: Int) {
        val bundle = Bundle()
        bundle.putString("user_name", AllCases!![postion]!!.custFname)
        bundle.putString("case_id", AllCases!![postion]!!.caseId)
        startActivity(OutUploadSecoundkantaParchiClass::class.java, bundle)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityAndClear(StaffDashBoardActivity::class.java)
    }
}
