package com.apnagodam.staff.activity.lead

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.LeadsViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.adapter.LeadsTopAdapter
import com.apnagodam.staff.databinding.ActivityListingBinding
import com.apnagodam.staff.module.AllLeadsResponse
import com.apnagodam.staff.module.AllLeadsResponse.Lead
import com.apnagodam.staff.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable

@AndroidEntryPoint
class LeadListingActivity() : BaseActivity<ActivityListingBinding?>() {
    private var leadsTopAdapter: LeadsTopAdapter? = null
    private var pageOffset = 1
    private var totalPage = 0
    private var AllCases: MutableList<AllLeadsResponse.Datum?>? = null
    private var response: Lead? = null
    private val leadsViewModel by viewModels<LeadsViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_listing
    }

    override fun setUp() {
        binding!!.pageNextPrivious.visibility = View.VISIBLE
        AllCases = arrayListOf()
        getLeadsListing("")

        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding!!.TitleWaititngEdititng.setVisibility(
            View.VISIBLE
        )
        //        binding.layoutLoader.setVisibility(View.GONE);
        binding!!.ivClose.setOnClickListener(View.OnClickListener {
            startActivityAndClear(
                StaffDashBoardActivity::class.java
            )
        })
        binding!!.tvPrevious.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (pageOffset != 1) {
                    pageOffset--
                    getLeadsListing("")
                }
            }
        })
        binding!!.tvNext.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (totalPage != pageOffset) {
                    pageOffset++
                    getLeadsListing("")
                }
            }
        })
        binding!!.swipeRefresherStock.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh() {
                getLeadsListing("")
            }
        })
        binding!!.filterIcon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val builder = AlertDialog.Builder(this@LeadListingActivity)
                val inflater = (this@LeadListingActivity as Activity).layoutInflater
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
                            getLeadsListing(notes.text.toString().trim { it <= ' ' })
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
                this@LeadListingActivity,
                LinearLayoutManager.VERTICAL
            )
        )
        binding!!.rvDefaultersStatus.setHasFixedSize(true)
        binding!!.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager =
            LinearLayoutManager(this@LeadListingActivity, LinearLayoutManager.VERTICAL, false)
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager
        binding!!.rvDefaultersStatus.adapter = leadsTopAdapter
    }

    private fun getLeadsListing(search: String) {
        showDialog()
        leadsViewModel.getAllLeads("10", pageOffset, "IN", search)
        leadsViewModel.allLeadsResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog()
                    showToast(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    showDialog()
                }

                is NetworkResult.Success -> {
                    hideDialog()
                    binding!!.swipeRefresherStock.isRefreshing = false
                    AllCases!!.clear()
                    response = it.data!!.leads
                    if (it.data.leads.data.size == 0) {
                        binding!!.txtemptyMsg.visibility = View.VISIBLE
                        binding!!.rvDefaultersStatus.visibility = View.GONE
                        binding!!.pageNextPrivious.visibility = View.GONE
                    } else {
                        totalPage = it.data.leads.lastPage
                        AllCases!!.addAll(it.data.leads.data)
                        leadsTopAdapter =
                            LeadsTopAdapter(AllCases, response, this@LeadListingActivity, activity)
                        setAdapter()
                        //   binding.rvDefaultersStatus.setAdapter(new LeadsTopAdapter(body.getLeads(), LeadListingActivity.this));
                    }
                }
            }
        }

    }

    fun ViewData(position: Int) {
        val displayRectangle = Rect()
        val window = this.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val builder = AlertDialog.Builder(this@LeadListingActivity, R.style.CustomAlertDialog)
        val inflater = this@LeadListingActivity.layoutInflater
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
        val create_date = dialogView.findViewById<TextView>(R.id.create_date)
        lead_id.text = "" + AllCases!!.get(position)!!.id
        genrated_by.text = "" + AllCases!!.get(position)!!
            .firstName + " " + AllCases!!.get(position)!!.lastName
        customer_name.text = "" + AllCases!!.get(position)!!.customerName
        location_title.text = "" + AllCases!!.get(position)!!.location
        phone_no.text = "" + AllCases!!.get(position)!!.phone
        commodity_name.text =
            AllCases!!.get(position)!!.cateName + "(" + AllCases!!.get(position)!!.commodityType + ")"
        est_quantity_nmae.text = "" + AllCases!!.get(position)!!.quantity
        terminal_name.text = "" + AllCases!!.get(position)!!.terminalName
        purpose_name.text = "" + AllCases!!.get(position)!!.purpose
        commitemate_date.text = (AllCases!!.get(position)!!.commodityDate)
        create_date.text = "" + AllCases!!.get(position)!!.createdAt
        cancel_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                alertDialog.dismiss()
            }
        })
        alertDialog.show()
    }

    /* public void editLead(int postion) {
         Intent intent=new Intent();
         intent.putExtra(Constants.LeadListData,  AllCases.get(postion));
         setResult(2,intent);
         finish();//finishing activity
     }*/
    /* public void editLeads(List<AllLeadsResponse.Datum> lead){
        Intent intent=new Intent();
        intent.putExtra(Constants.LeadListData, (Serializable) lead);
        setResult(2,intent);
        finish();//finishing activity
    }*/
    fun editLead(position: Int) {
        val lead = response
        val intent = Intent()
        intent.putExtra(Constants.LeadListData, lead as Serializable?)
        setResult(2, intent)
        finish() //finishing activity
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityAndClear(LeadListingActivity::class.java)
    }
}
