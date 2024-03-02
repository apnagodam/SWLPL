package com.apnagodam.staff.activity.`in`.secound_kanthaparchi

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
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
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.KantaParchiViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.adapter.SecoundkanthaparchiAdapter
import com.apnagodam.staff.databinding.ActivityListingBinding
import com.apnagodam.staff.module.SecoundkanthaParchiListResponse
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecoundkanthaParchiListingActivity : BaseActivity<ActivityListingBinding?>() {
    private var firstkantaParchiFile: String? = null
    private var TruckImage: String? = null
    private lateinit var secoundkanthaparchiAdapter: SecoundkanthaparchiAdapter
    private var pageOffset = 1
    private var totalPage = 0
    private lateinit var AllCases: ArrayList<SecoundkanthaParchiListResponse.Datum>
    val kantaParchiViewModel by viewModels<KantaParchiViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_listing
    }

    override fun setUp() {
        binding!!.pageNextPrivious.visibility = View.VISIBLE
        AllCases = arrayListOf()

        getAllCases("")

        setSupportActionBar(binding!!.toolbar)
        binding!!.titleHeader.text = resources.getString(R.string.secoundkanta_parchi)
        binding!!.tvId.text = resources.getString(R.string.case_idd)
        binding!!.tvMoreView.text = resources.getString(R.string.more_view_truck)
        binding!!.tvPhone.text = resources.getString(R.string.kanta_parchi)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.swipeRefresherStock.setOnRefreshListener { getAllCases("") }
        /*  binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(SecoundkanthaParchiListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(SecoundkanthaParchiListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);*/
        binding!!.ivClose.setOnClickListener {onBackPressedDispatcher.onBackPressed() }
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
            val builder = AlertDialog.Builder(this@SecoundkanthaParchiListingActivity)
            val inflater = (this@SecoundkanthaParchiListingActivity as Activity).layoutInflater
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
                this@SecoundkanthaParchiListingActivity,
                LinearLayoutManager.VERTICAL
            )
        )
        binding!!.rvDefaultersStatus.setHasFixedSize(true)
        binding!!.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager = LinearLayoutManager(
            this@SecoundkanthaParchiListingActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager
             binding!!.rvDefaultersStatus.adapter = secoundkanthaparchiAdapter
    }

    private fun getAllCases(search: String) {
        showDialog()
        kantaParchiViewModel.getSKantaParchiListing("10",pageOffset.toString(),"IN",search)
        kantaParchiViewModel.sKantaParchiResponse.observe(this)
        {
            when(it){
                is NetworkResult.Error -> showErrorSnackBar("Something went wrong")
                is NetworkResult.Loading -> showDialog()
                is NetworkResult.Success -> {
                    hideDialog()
                    if(it.data!=null){
                        binding!!.swipeRefresherStock.isRefreshing = false
                        AllCases!!.clear()
                        if (it.data.secoundKataParchiDatum == null) {
                            binding!!.txtemptyMsg.visibility = View.VISIBLE
                            binding!!.rvDefaultersStatus.visibility = View.GONE
                            binding!!.pageNextPrivious.visibility = View.GONE
                        } else {
                            AllCases!!.clear()
                            totalPage = it.data.secoundKataParchiDatum.lastPage
                            AllCases!!.addAll(it.data.secoundKataParchiDatum.data)
                            secoundkanthaparchiAdapter =
                                SecoundkanthaparchiAdapter(AllCases, this@SecoundkanthaParchiListingActivity, activity)

                            setAdapter()

                            //  AllCases = body.getData();
                            //  binding.rvDefaultersStatus.setAdapter(new SecoundkanthaparchiAdapter(body.getData(), SecoundkanthaParchiListingActivity.this));
                        }
                    }
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        getAllCases("")
    }
    fun ViewData(position: Int) {
        val displayRectangle = Rect()
        val window = this.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val builder =
            AlertDialog.Builder(this@SecoundkanthaParchiListingActivity, R.style.CustomAlertDialog)
        val inflater = (this@SecoundkanthaParchiListingActivity as Activity).layoutInflater
        val dialogView = inflater.inflate(R.layout.dilog_kanta_parchi, null)
        dialogView.minimumWidth = (displayRectangle.width() * 1f).toInt()
        dialogView.minimumHeight = (displayRectangle.height() * 1f).toInt()
        builder.setView(dialogView)
        val alertDialog = builder.create()
        val cancel_btn = dialogView.findViewById<View>(R.id.cancel_btn) as ImageView
        val case_id = dialogView.findViewById<View>(R.id.a1) as TextView
        val lead_id = dialogView.findViewById<View>(R.id.lead_id) as TextView
        val customer_name = dialogView.findViewById<View>(R.id.customer_name) as TextView
        val kanta_parchi_file = dialogView.findViewById<View>(R.id.kanta_parchi_file) as ImageView
        val truck_file = dialogView.findViewById<View>(R.id.truck_file) as ImageView
        val notes = dialogView.findViewById<View>(R.id.notes) as TextView
        val gate_pass = dialogView.findViewById<View>(R.id.gate_pass) as TextView
        val user = dialogView.findViewById<View>(R.id.user) as TextView
        val coldwin = dialogView.findViewById<View>(R.id.coldwin) as TextView
        val purchase_details = dialogView.findViewById<View>(R.id.purchase_details) as TextView
        val loan_details = dialogView.findViewById<View>(R.id.loan_details) as TextView
        val selas_details = dialogView.findViewById<View>(R.id.selas_details) as TextView
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
                this@SecoundkanthaParchiListingActivity,
                R.layout.popup_photo_full,
                view,
                firstkantaParchiFile,
                null
            )
        }
        truck_file.setOnClickListener { view ->
            PhotoFullPopupWindow(
                this@SecoundkanthaParchiListingActivity,
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
        var intent = Intent(this,UploadSecoundkantaParchiClass::class.java)
        intent.putExtra("user_name",  AllCases!![postion]!!.getCustFname())
        intent.putExtra("case_id", AllCases!![postion]!!.getCaseId())
        intent.putExtra("vehicle_no",  AllCases!![postion]!!.getVehicleNo())
        intent.putExtra("file3", AllCases!![postion]!!.file3)

        startActivity(intent)

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
