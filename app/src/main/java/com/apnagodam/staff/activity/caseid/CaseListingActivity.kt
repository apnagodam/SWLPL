package com.apnagodam.staff.activity.caseid

import android.app.Activity
import android.graphics.Rect
import android.view.LayoutInflater
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
class CaseListingActivity() : com.apnagodam.staff.activity.BaseActivity<ActivityListingBinding>() {
    private var pageOffset = 1
    private var totalPage = 0
    private var AllCases: MutableList<AllCaseIDResponse.Datum?>? = null

    @Inject
    lateinit var adapter: CasesAdapter
    val caseIdViewModel: CaseIdViewModel by viewModels<CaseIdViewModel>()


    override fun setUI() {
        binding!!.pageNextPrivious.visibility = View.GONE
        AllCases = arrayListOf()


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

    override fun setObservers() {
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
                        showDialog(this@CaseListingActivity)
                    }

                    is LoadState.NotLoading -> {
                          hideDialog(this@CaseListingActivity)
                        binding!!.swipeRefresherStock.isRefreshing = false
                    }
                }
                // to determine if we are done with the loading state,
                // you should have already  shown your loading view elsewhere when the entering your fragment


            }
        }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityListingBinding =
        ActivityListingBinding.inflate(layoutInflater)

    override fun callApis() {
        getAllCases("")

    }

    override fun onResume() {
        caseIdViewModel.getAllCasesPagingData()
        super.onResume()
    }

    private fun setAdapter() {
        binding.rvDefaultersStatus.setHasFixedSize(true)
        binding.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager =
            LinearLayoutManager(this@CaseListingActivity, LinearLayoutManager.VERTICAL, false)
        binding.rvDefaultersStatus.layoutManager = horizontalLayoutManager

        binding.rvDefaultersStatus.adapter = adapter.withLoadStateFooter(
            footer = ListLoadStateAdapter {
                caseIdViewModel.getPagingData()
            })

    }

    private fun getAllCases(search: String) {
        caseIdViewModel.getAllCasesPagingData()

    }


}
