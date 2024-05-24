package com.apnagodam.staff.activity.audit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apnagodam.staff.AuditPvRecyclerviewAdapter
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.PvRequestModel
import com.apnagodam.staff.Network.Response.PvResponseModel
import com.apnagodam.staff.Network.Response.StacksListResponse
import com.apnagodam.staff.Network.viewmodel.AuditViewModel
import com.apnagodam.staff.Network.viewmodel.PvViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.BaseActivity
import com.apnagodam.staff.activity.UpdatePvRecyclerviewAdapter
import com.apnagodam.staff.databinding.ActivityAuditPvBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.CommudityResponse
import com.apnagodam.staff.utils.EventBus
import com.apnagodam.staff.utils.RecyclerviewCallBack
import com.apnagodam.staff.utils.Utility
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@AndroidEntryPoint
class AuditPvActivity : BaseActivity<ActivityAuditPvBinding>(), RecyclerviewCallBack {
    lateinit var pvRecyclerviewAdapter: AuditPvRecyclerviewAdapter
    var list = mutableListOf<String>()
    var pvModelList = arrayListOf<PvRequestModel.BlockNo>()
    var layoutId = 1
    var terminalList = arrayListOf<CommudityResponse.Terminals>()
    var stackList = arrayListOf<StacksListResponse.Datum>()
    val auditViewModel by viewModels<AuditViewModel>()

    lateinit var searchableSpinner: SearchableSpinner
    lateinit var stackSearchableSpinner: SearchableSpinner
    var terminalId: Int = 0;
    var stackId: Float = 0.0f;
    var commodityId = 0;
    val listOfStacks = arrayListOf<String>()
    private val listOfTerminals = arrayListOf<String>()

    var recyclerviewCallBack: RecyclerviewCallBack? = null
    override fun setUI() {
        var count = 1
        var total = 0;

        pvRecyclerviewAdapter = AuditPvRecyclerviewAdapter(list, this, lifecycleScope, this)
        pvRecyclerviewAdapter.recyclerviewCallBack = this
        searchableSpinner = SearchableSpinner(this)
        stackSearchableSpinner = SearchableSpinner(this)
        binding.rvPv.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = pvRecyclerviewAdapter
            it.setItemViewCacheSize(50)
            it.setDrawingCacheEnabled(true)
        }

        this.lifecycleScope.launch {

            EventBus.subscribe<PvRequestModel.BlockNo> { updateModel ->
                total = 0

                pvModelList.add(updateModel)

                if (pvModelList.isNotEmpty()) {
                    for (i in pvModelList.indices) {
                        if (pvModelList[i].block_no == updateModel.block_no) {
                            pvModelList[i] = updateModel

                        }
                    }
                }
                var list2 = pvModelList.distinct()

                list2.forEach {
                    total += it.total.toInt()
                }

                binding.tvTotalBags.text = "Total Bags: ${total}"
                binding.btAdd.setOnClickListener {

                    if (stackId == 0.0f || terminalId == 0) {
                        Toast.makeText(
                            this@AuditPvActivity,
                            "Invalid Selection please select terminal or stack number",
                            Toast.LENGTH_SHORT
                        );

                    } else if (list.size > 0) {
                        auditViewModel.postPvData(
                            PvRequestModel(
                                terminal_id = terminalId.toString(),
                                stack_no = stackId,
                                commodity_id = commodityId,
                                block_no = list2
                            )
                        )
                    } else {
                        Toast.makeText(
                            this@AuditPvActivity,
                            "Please Select at least one pv",
                            Toast.LENGTH_SHORT
                        );
                    }


                }


            }


        }
        lifecycleScope.launch {
            EventBus.subscribe<Int> { position ->
                if (pvModelList.isNotEmpty()) {
                    total -= pvModelList[position].total.toInt()
                    var list2 = arrayListOf<PvRequestModel.BlockNo>()
                    list2 = pvModelList.distinct() as ArrayList<PvRequestModel.BlockNo>
                    if (position == 0) {
                        total = 0
                        list2.clear()
                        //  list.clear()
                        //pvModelList.clear()

                    }
                    list.removeAt(position)
                    list2.removeAt(position)
                    pvRecyclerviewAdapter.notifyItemRemoved(position)
                    binding.tvTotalBags.text = "Total Bags: ${total}"

                    binding.btAdd.setOnClickListener {

                        if (stackId == 0.0f || terminalId == 0) {
                            Toast.makeText(
                                this@AuditPvActivity,
                                "Invalid Selection please select terminal or stack number",
                                Toast.LENGTH_SHORT
                            );

                        } else if (list.size > 0) {
                            auditViewModel.postPvData(
                                PvRequestModel(
                                    terminal_id = terminalId.toString(),
                                    stack_no = stackId,
                                    commodity_id = commodityId,
                                    block_no = list2
                                )
                            )
                        } else {
                            Toast.makeText(
                                this@AuditPvActivity,
                                "Please Select at least one pv",
                                Toast.LENGTH_SHORT
                            );
                        }


                    }

                }
            }
        }

        getTerminalsFromPreferences()
    }

    override fun setObservers() {
        auditViewModel.getStackListResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog(this)
                }

                is NetworkResult.Loading -> {
                    showDialog(this)
                }

                is NetworkResult.Success -> {
                    hideDialog(this)
                    it.data?.let {
                        stackList.clear()
                        listOfStacks.clear()
                        it.data?.let {
                            stackList = it
                        }

                        stackList.forEach {
                            it.stackNumber?.let {
                                listOfStacks.add(it)
                            }

                        }
                        stackSearchableSpinner.setSpinnerListItems(listOfStacks)
                        stackSearchableSpinner.onItemSelectListener =
                            object : OnItemSelectListener {
                                override fun setOnItemSelectListener(
                                    position: Int,
                                    selectedString: String
                                ) {
                                    it.data?.get(position)?.let {
                                        it.commodityId?.let {
                                            commodityId = it
                                        }
                                        it.stackNumber?.let {
                                            stackId = it.toFloat()

                                        }

                                    }


                                    binding.tvStacks.text = selectedString
                                    binding.btAddPV.visibility = View.VISIBLE
                                    binding.btAddPV.setOnClickListener {

                                        list.add("hello")
                                        pvRecyclerviewAdapter.notifyItemInserted(list.size - 1)

                                    }

                                }

                            }
                    }


                }
            }
        }

        auditViewModel.postPvAuditResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog(this)
                    it.message?.let {
                        showToast(this, it)
                    }
                }

                is NetworkResult.Loading -> {
                    showDialog(this)
                }

                is NetworkResult.Success -> {
                    it.data?.let { response ->
                        response.message?.let { message ->
                            showToast(this, message)
                        }
                        response.status?.let { status ->
                            if (status.equals("1")) {
                                finish()
                            }
                        }
                    }
                }
            }
        }

    }

    private fun getTerminalsFromPreferences() {

        SharedPreferencesRepository.getDataManagerInstance().user.let { userDetails ->
            terminalList.clear()
            listOfTerminals.clear();
            listOfStacks.clear()
            val localTerminalList = SharedPreferencesRepository.getDataManagerInstance().terminals
            terminalList = SharedPreferencesRepository.getDataManagerInstance().terminals

            searchableSpinner.windowTitle = "Select Terminal"
            stackSearchableSpinner.windowTitle = "Select Stack"

            for (i in terminalList) {
                listOfTerminals.add(i.name)
            }


            searchableSpinner.setSpinnerListItems(listOfTerminals)

            searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
                override fun setOnItemSelectListener(
                    position: Int,
                    selectedString: String
                ) {
                    binding.tvTerminal.text = selectedString
                    terminalId = terminalList[position].id.toInt()

                    auditViewModel.getStackList(terminalList[position].id)
                    binding.tvStacks.visibility = View.VISIBLE

                }
            }
            binding.tvTerminal.setOnClickListener { searchableSpinner.show() }
            binding.tvStacks.setOnClickListener { stackSearchableSpinner.show() }
        }

    }

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityAuditPvBinding =
        ActivityAuditPvBinding.inflate(layoutInflater)

    override fun callApis() {

    }

    override fun removeItem(position: Int) {
//        showToast(this, position.toString())
        list.removeAt(position)
        pvRecyclerviewAdapter.notifyItemRemoved(position)
    }

    override fun getDhang(dhang: String) {
        super.getDhang(dhang)
    }

    override fun getDanda(danda: String) {

        super.getDanda(danda)
    }

    override fun getTotal(total: String) {

        super.getTotal(total)
    }

    override fun getHeight(height: String) {

        super.getHeight(height)
    }
}