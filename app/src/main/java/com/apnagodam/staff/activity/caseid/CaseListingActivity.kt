package com.apnagodam.staff.activity.caseid

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.CaseIdViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.adapter.CasesTopAdapter
import com.apnagodam.staff.databinding.ActivityListingBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.AllCaseIDResponse
import com.apnagodam.staff.paging.adapter.CasesAdapter
import com.apnagodam.staff.paging.state.ListLoadStateAdapter
import com.apnagodam.staff.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CaseListingActivity() : BaseActivity<ActivityListingBinding?>() {
    private var pageOffset = 1
    private var totalPage = 0
    private var AllCases: MutableList<AllCaseIDResponse.Datum?>? = null

    @Inject
    lateinit var adapter: CasesAdapter
    val caseIdViewModel: CaseIdViewModel by viewModels<CaseIdViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_listing
    }

    override fun setUp() {
        setView()
    }

    private fun setView() {
        binding!!.pageNextPrivious.visibility = View.GONE
        AllCases = arrayListOf()

        setObservers()
        getAllCases("")

        setSupportActionBar(binding!!.toolbar)
        binding!!.titleHeader.text = resources.getString(R.string.case_list)
        binding!!.tvId.text = resources.getString(R.string.case_idd)
        binding!!.tvMoreView.text = resources.getString(R.string.status_title)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.swipeRefresherStock.setOnRefreshListener(OnRefreshListener {

            getAllCases("")
        })
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
                val builder = AlertDialog.Builder(this@CaseListingActivity)
                val inflater = (this@CaseListingActivity as Activity).layoutInflater
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
                        caseIdViewModel.getAllCasesPagingData(searchQuery = notes.text.toString())
                        alertDialog.dismiss()
                        pageOffset = 1
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
            LinearLayoutManager(this@CaseListingActivity, LinearLayoutManager.VERTICAL, false)
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager

        binding!!.rvDefaultersStatus.adapter = adapter.withLoadStateFooter(
            footer = ListLoadStateAdapter {
                caseIdViewModel.getPagingData()
            })

    }


    override fun onResume() {
        caseIdViewModel.getAllCasesPagingData()
        super.onResume()
    }

    private fun setObservers() {
        caseIdViewModel.allCasesPaginatino.observe(this) {
            lifecycleScope.launch {
                adapter.submitData(it)

            }
        }
        setAdapter()
        lifecycleScope.launch {
            adapter.loadStateFlow.collect {
                when (it.refresh) {
                    is LoadState.Error -> {

                    }

                    LoadState.Loading -> {
                        Utility.showDialog(this@CaseListingActivity, "")
                    }

                    is LoadState.NotLoading -> {
                        Utility.hideDialog(this@CaseListingActivity)
                        binding!!.swipeRefresherStock.isRefreshing = false
                    }
                }
                // to determine if we are done with the loading state,
                // you should have already  shown your loading view elsewhere when the entering your fragment


            }
        }
    }

    private fun getAllCases(search: String) {

        caseIdViewModel.getAllCasesPagingData()


//        caseIdViewModel.getCaseId("15",pageOffset,"1",search)
//        caseIdViewModel.response.observe(this){
//            body->
//            when(body){
//                is NetworkResult.Success->
//                {
//                    binding!!.swipeRefresherStock.isRefreshing = false
//                    AllCases!!.clear()
//                    if (body.data!!.getaCase() == null) {
//                        binding!!.txtemptyMsg.visibility = View.VISIBLE
//                        binding!!.rvDefaultersStatus.visibility = View.GONE
//                        binding!!.pageNextPrivious.visibility = View.GONE
//                    } else {
//                        AllCases!!.clear()
//                        totalPage = body.data.getaCase()!!.lastPage!!
//                        body.data.getaCase()!!.data!!.forEach {
//                            if (userDetails.terminal==null){
//                                AllCases!!.add(it)
//                            }
//                            else if (it.terminalId.toString() == userDetails.terminal.toString()) {
//                                AllCases!!.add(it)
//
//                            }
//                        }
////                        AllCases!!.addAll(body.data.getaCase().data)
//                        casesTopAdapter = CasesTopAdapter(AllCases!!.reversed(),this,apiService)
//                        setAdapter()
//
//                        //  AllCases=body.getCases();
//                        // binding.rvDefaultersStatus.setAdapter(new CasesTopAdapter(body.getCases(), CaseListingActivity.this));
//                    }
//                    hideDialog()
//
//                }
//
//                is NetworkResult.Error ->{
//                    hideDialog()
//
//                }
//                is NetworkResult.Loading -> {}
//            }
//
//
//        }


    }

    fun ViewData(position: Int) {
        val displayRectangle = Rect()
        val window = this.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val builder = AlertDialog.Builder(this@CaseListingActivity, R.style.CustomAlertDialog)
        val inflater = (this@CaseListingActivity as Activity).layoutInflater
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
        val create_date = dialogView.findViewById<View>(R.id.create_date) as TextView
        val case_id = dialogView.findViewById<View>(R.id.a1) as TextView
        val sub_user = dialogView.findViewById<View>(R.id.a10) as TextView
        val case_extra = dialogView.findViewById<View>(R.id.case_extra) as LinearLayout
        case_extra.visibility = View.VISIBLE
        val converted_by = dialogView.findViewById<View>(R.id.converted_by) as TextView
        val vehicle_no = dialogView.findViewById<View>(R.id.vehicle_no) as TextView
        val in_out = dialogView.findViewById<View>(R.id.in_out) as TextView
        val staclLL = dialogView.findViewById<View>(R.id.staclLL) as LinearLayout
        val stackView = dialogView.findViewById(R.id.stackView) as View
        staclLL.visibility = View.VISIBLE
        stackView.visibility = View.VISIBLE
        val stack_no = dialogView.findViewById<View>(R.id.stack_no) as TextView
        converted_by.text = "" + AllCases!!.get(position)!!
            .leadConvFname + " " + AllCases!!.get(position)!!.leadConvLname
        vehicle_no.text = "" + AllCases!!.get(position)!!.vehicleNo
        in_out.text = "" + AllCases!!.get(position)!!.inOut
        case_id.text = resources.getString(R.string.case_idd)
        sub_user.text = resources.getString(R.string.sub_user)
        lead_id.text = "" + AllCases!!.get(position)!!.caseId
        genrated_by.text = "" + AllCases!!.get(position)!!
            .leadGenFname + " " + AllCases!!.get(position)!!.leadGenLname
        customer_name.text = "" + AllCases!!.get(position)!!.custFname
        location_title.text = "" + AllCases!!.get(position)!!.location
        phone_no.text = "" + AllCases!!.get(position)!!.phone
        commodity_name.text =
            AllCases!!.get(position)!!.cateName + "(" + AllCases!!.get(position)!!.commodityType + ")"
        est_quantity_nmae.text = "" + AllCases!!.get(position)!!.totalWeight
        terminal_name.text = "" + AllCases!!.get(position)!!
            .terminalName + " " + AllCases!!.get(position)!!.warehouseCode
        purpose_name.text = "" + AllCases!!.get(position)!!.purpose
        commitemate_date.text = (AllCases!!.get(position)!!.fpoUsers)
        create_date.text = "" + AllCases!!.get(position)!!.createdAt
        stack_no.text = "" + AllCases!!.get(position)!!.stack_number
        cancel_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                alertDialog.dismiss()
            }
        })
        alertDialog.show()
    }
}
