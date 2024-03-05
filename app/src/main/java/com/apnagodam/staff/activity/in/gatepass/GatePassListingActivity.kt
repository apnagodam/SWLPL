package com.apnagodam.staff.activity.`in`.gatepass

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
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.GatePassViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.GatePassPDFPrieviewClass
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.adapter.GatepassAdapter
import com.apnagodam.staff.databinding.ActivityListingBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.GatePassListResponse
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
@AndroidEntryPoint
class GatePassListingActivity() : BaseActivity<ActivityListingBinding?>() {
    private var firstkantaParchiFile: String? = null
    private val TruckImage: String? = null
    private lateinit var gatepassAdapter: GatepassAdapter
    private var pageOffset = 1
    private var totalPage = 0
    private lateinit var AllCases: ArrayList<GatePassListResponse.Datum>

    val gatePassViewModel by viewModels<GatePassViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_listing
    }

    override fun setUp() {
        binding!!.pageNextPrivious.visibility = View.VISIBLE
        AllCases = arrayListOf()
        binding!!.rvDefaultersStatus.addItemDecoration(
            DividerItemDecoration(
                this@GatePassListingActivity,
                LinearLayoutManager.VERTICAL
            )
        )
        setAdapter()
        setSupportActionBar(binding!!.toolbar)
        binding!!.titleHeader.text = resources.getString(R.string.gate_pass_list)
        binding!!.tvId.text = resources.getString(R.string.case_idd)
        binding!!.tvMoreView.text = resources.getString(R.string.more_view_truck)
        binding!!.tvPhone.text = resources.getString(R.string.gate_passs)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.swipeRefresherStock.setOnRefreshListener(OnRefreshListener { getAllCases("") })
        /* binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(GatePassListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(GatePassListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);*/getAllCases("")
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
                val builder = AlertDialog.Builder(this@GatePassListingActivity)
                val inflater = (this@GatePassListingActivity as Activity).layoutInflater
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

        binding!!.rvDefaultersStatus.setHasFixedSize(true)
        binding!!.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager =
            LinearLayoutManager(this@GatePassListingActivity, LinearLayoutManager.VERTICAL, false)
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager
        gatepassAdapter = GatepassAdapter(AllCases, this@GatePassListingActivity, activity)
        binding!!.rvDefaultersStatus.adapter = gatepassAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun getAllCases(search: String) {
        gatePassViewModel.getGatePassList("10",pageOffset.toString(),"IN",search)
        gatePassViewModel.gatePassList.observe(this){
            when(it){
                is NetworkResult.Error -> hideDialog()
                is NetworkResult.Loading -> showDialog()
                is NetworkResult.Success -> {

                    if(it.data!=null){
                        binding!!.swipeRefresherStock.isRefreshing = false
                        AllCases!!.clear()
                        if (it.data.gatePassData == null) {
                            binding!!.txtemptyMsg.visibility = View.VISIBLE
                            binding!!.rvDefaultersStatus.visibility = View.GONE
                            binding!!.pageNextPrivious.visibility = View.GONE
                        } else {
                            AllCases!!.clear()
                            totalPage = it.data.gatePassData.lastPage
                            var userDetails = SharedPreferencesRepository.getDataManagerInstance().user

                            for (i in it.data.gatePassData.data.indices) {

                                if (userDetails.terminal == null) {
                                    AllCases!!.add(it.data.gatePassData.data[i])
                                } else if (it.data.gatePassData.data[i].terminalId.toString() == userDetails.terminal.toString()) {
                                    AllCases!!.add(it.data.gatePassData.data[i])

                                } else break


                            }
//                            AllCases!!.addAll(it.data.gatePassData.data)
                            gatepassAdapter!!.notifyDataSetChanged()
                            // AllCases = body.getData();
                            //  binding.rvDefaultersStatus.setAdapter(new GatepassAdapter(body.getData(), GatePassListingActivity.this));
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
        val builder = AlertDialog.Builder(this@GatePassListingActivity, R.style.CustomAlertDialog)
        val inflater = (this@GatePassListingActivity as Activity).layoutInflater
        val dialogView = inflater.inflate(R.layout.dilog_kanta_parchi, null)
        dialogView.minimumWidth = (displayRectangle.width() * 1f).toInt()
        dialogView.minimumHeight = (displayRectangle.height() * 1f).toInt()
        builder.setView(dialogView)
        val alertDialog = builder.create()
        val cancel_btn = dialogView.findViewById<View>(R.id.cancel_btn) as ImageView
        val case_id = dialogView.findViewById<View>(R.id.a1) as TextView
        val gatePass = dialogView.findViewById<View>(R.id.a4) as TextView
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
        val bags = dialogView.findViewById<View>(R.id.no_of_bags) as TextView
        val wieight = dialogView.findViewById<View>(R.id.total_weight) as TextView
        val view = dialogView.findViewById(R.id.view) as View
        val llview = dialogView.findViewById<View>(R.id.llview) as LinearLayout
        val gate_pass_extra = dialogView.findViewById<View>(R.id.gate_pass_extra) as LinearLayout
        val LLStack = dialogView.findViewById<View>(R.id.LLStack) as LinearLayout
        val stack_no = dialogView.findViewById<View>(R.id.stack_no) as TextView
        val avgweight = dialogView.findViewById<View>(R.id.avgweight) as LinearLayout
        avgweight.visibility = View.VISIBLE
        val avg_weight = dialogView.findViewById<View>(R.id.avg_weight) as TextView
        ////
        view.visibility = View.GONE
        llview.visibility = View.GONE
        gate_pass_extra.visibility = View.VISIBLE
        LLStack.visibility = View.VISIBLE
        gatePass.text = resources.getString(R.string.gate_passs_file)
        if (AllCases!![position]!!.gpCaseId == null || AllCases!![position]!!.gpCaseId.isEmpty()) {
            kanta_parchi_file.visibility = View.GONE
        }
        case_id.text = resources.getString(R.string.case_idd)
        lead_id.text = "" + AllCases!!.get(position)!!.caseId
        customer_name.text = "" + AllCases!!.get(position)!!.custFname
        avg_weight.text = "" + (if ((AllCases!!.get(position)!!
                .gatepass_avgWeight) != null
        ) AllCases!!.get(position)!!.gatepass_avgWeight else "N/A")
        stack_no.text = "" + (if ((AllCases!!.get(position)!!
                .stack_number) != null
        ) AllCases!!.get(position)!!.stack_number else "N/A")
        notes.text = "" + (if ((AllCases!!.get(position)!!
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
        bags.text = "" + (if ((AllCases!!.get(position)!!
                .noOfBags) != null
        ) AllCases!!.get(position)!!.noOfBags else "N/A")
        wieight.text = "" + (if ((AllCases!!.get(position)!!
                .totalWeight) != null
        ) AllCases!!.get(position)!!.totalWeight else "N/A")
        /////
        firstkantaParchiFile = Constants.gate_pass + AllCases!![position]!!
            .file
        kanta_parchi_file.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                startActivity(
                    GatePassPDFPrieviewClass::class.java, "CaseID", AllCases!![position]!!
                        .caseId
                )
                //new PhotoFullPopupWindow(GatePassListingActivity.this, R.layout.popup_photo_full, view, firstkantaParchiFile, null);
            }
        })
        truck_file.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                PhotoFullPopupWindow(
                    this@GatePassListingActivity,
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
        bundle.putString("no_bags",AllCases[postion].noOfBags)
        bundle.putString("weight",AllCases[postion].totalWeight)

        bundle.putString("avg_weight",AllCases[postion].gatepass_avgWeight)
        bundle.putString("user_name", AllCases!![postion]!!.custFname)
        bundle.putString("case_id", AllCases!![postion]!!.caseId)
        bundle.putString("terminal_name", AllCases!![postion]!!.terminal_name)
        bundle.putString("vehicle_no", AllCases!![postion]!!.vehicleNo)
        bundle.putString("stackNo", AllCases!![postion]!!.stack_number)
        bundle.putString("INOUT", AllCases!![postion]!!.inOut)

        var intent = Intent(this,UploadGatePassClass::class.java)
        intent.putExtra("all_case",AllCases[postion])

        startActivity(intent)
    }
}
