package com.apnagodam.staff.activity.out.f_katha_parchi

import android.app.Activity
import android.content.Intent
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.KantaParchiViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.adapter.OutFirstkanthaparchiAdapter
import com.apnagodam.staff.databinding.ActivityListingBinding
import com.apnagodam.staff.module.FirstkanthaParchiListResponse
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class OutFirstkanthaParchiListingActivity() : BaseActivity<ActivityListingBinding?>() {
    private lateinit var outFirstkanthaparchiAdapter: OutFirstkanthaparchiAdapter
    private var pageOffset = 1
    private var totalPage = 0
    private lateinit var AllCases: ArrayList<FirstkanthaParchiListResponse.Datum>
    private var firstkantaParchiFile: String? = null
    private var TruckImage: String? = null
    val kantaParchiViewModel by viewModels<KantaParchiViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_listing
    }

    override fun setUp() {
        getAllCases("")
        binding!!.pageNextPrivious.visibility = View.VISIBLE
        AllCases = arrayListOf()
        setSupportActionBar(binding!!.toolbar)
        binding!!.titleHeader.text = resources.getString(R.string.firstkanta_parchi_title)
        binding!!.tvId.text = resources.getString(R.string.case_idd)
        binding!!.tvMoreView.text = resources.getString(R.string.more_view_truck)
        binding!!.tvPhone.text = resources.getString(R.string.kanta_parchi)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        /*  binding.rvDefaultersStatus.addItemDecoration(new DividerItemDecoration(FirstkanthaParchiListingActivity.this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(FirstkanthaParchiListingActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvDefaultersStatus.setLayoutManager(horizontalLayoutManager);*/
        binding!!.swipeRefresherStock.setOnRefreshListener(OnRefreshListener { getAllCases("") })
        binding!!.ivClose.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                finish()

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
                val builder = AlertDialog.Builder(this@OutFirstkanthaParchiListingActivity)
                val inflater = (this@OutFirstkanthaParchiListingActivity as Activity).layoutInflater
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
        binding!!.rvDefaultersStatus.addItemDecoration(
            DividerItemDecoration(
                this@OutFirstkanthaParchiListingActivity,
                LinearLayoutManager.VERTICAL
            )
        )
        binding!!.rvDefaultersStatus.setHasFixedSize(true)
        binding!!.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager = LinearLayoutManager(
            this@OutFirstkanthaParchiListingActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager

        binding!!.rvDefaultersStatus.adapter = outFirstkanthaparchiAdapter
    }

    private fun getAllCases(search: String) {
        kantaParchiViewModel.getKantaParchiListing("10", "" + pageOffset, "OUT", search)
        kantaParchiViewModel.kantaParchiResponse.observe(this){
            when(it){
                is NetworkResult.Error -> hideDialog()
                is NetworkResult.Loading -> showDialog()
                is NetworkResult.Success -> {
                    binding!!.swipeRefresherStock.isRefreshing = false
                    AllCases!!.clear()
                    if (it.data!!.firstKataParchiData == null) {
                        binding!!.txtemptyMsg.visibility = View.VISIBLE
                        binding!!.rvDefaultersStatus.visibility = View.GONE
                        binding!!.pageNextPrivious.visibility = View.GONE
                    } else {
                        AllCases!!.clear()
                        totalPage = it.data.firstKataParchiData.lastPage
                        AllCases!!.addAll(it.data.firstKataParchiData.data)
                        outFirstkanthaparchiAdapter = OutFirstkanthaparchiAdapter(
                            AllCases,
                            this@OutFirstkanthaParchiListingActivity,
                            activity
                        )
                        setAdapter()
                        //   AllCases = body.getFirstKataParchiData();
                        //  binding.rvDefaultersStatus.setAdapter(new FirstkanthaparchiAdapter(body.getFirstKataParchiData(), FirstkanthaParchiListingActivity.this));
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
            AlertDialog.Builder(this@OutFirstkanthaParchiListingActivity, R.style.CustomAlertDialog)
        val inflater = this@OutFirstkanthaParchiListingActivity.layoutInflater
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
        lead_id.text = "" + AllCases!!.get(position)!!.caseId
        customer_name.text = "" + AllCases!!.get(position)!!.custFname
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
        /////
        firstkantaParchiFile = Constants.First_kata + AllCases!![position]!!
            .file
        TruckImage = Constants.First_kata + AllCases!![position]!!.file2
        kanta_parchi_file.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                PhotoFullPopupWindow(
                    this@OutFirstkanthaParchiListingActivity,
                    R.layout.popup_photo_full,
                    view,
                    firstkantaParchiFile,
                    null
                )
            }
        })
        truck_file.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                PhotoFullPopupWindow(
                    this@OutFirstkanthaParchiListingActivity,
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
        var intent = Intent(this,OutUploadFirrstkantaParchiClass::class.java)
        intent.putExtra("user_name", AllCases!![postion]!!.custFname)
        intent.putExtra("case_id",AllCases!![postion]!!.caseId)
        intent.putExtra("vehicle_no", AllCases!![postion]!!.vehicleNo)

        startActivity(intent)
    }
}
