package com.apnagodam.staff.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.PvRequestModel
import com.apnagodam.staff.Network.PvUploadRequestModel
import com.apnagodam.staff.Network.Response.PvResponseModel
import com.apnagodam.staff.Network.viewmodel.PvViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityUpdatePvBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.utils.EventBus
import com.apnagodam.staff.utils.Utility
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@AndroidEntryPoint
class UpdatePv : AppCompatActivity() {
    lateinit var pvRecyclerviewAdapter: UpdatePvRecyclerviewAdapter
    lateinit var binding: ActivityUpdatePvBinding
    var listOfLayout = arrayListOf<LinearLayout>()
    var list = arrayListOf<String>()
    var pvModelList = arrayListOf<PvRequestModel.BlockNo>()
    var layoutId = 1
    var terminalList = arrayListOf<PvResponseModel.TerminalDatum>()
    var stackList = arrayListOf<PvResponseModel.StackDatum>()
    val pvViewModel by viewModels<PvViewModel>()

    lateinit var searchableSpinner: SearchableSpinner
    lateinit var stackSearchableSpinner: SearchableSpinner
    var terminalId: Int = 0;
    var stackId: Float = 0.0f;
    lateinit var pvUploadRequestModel: PvUploadRequestModel
    var totalBags = 0;
    var index = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_pv)
        pvRecyclerviewAdapter = UpdatePvRecyclerviewAdapter(list, this, lifecycleScope, this)
        searchableSpinner = SearchableSpinner(this)
        stackSearchableSpinner = SearchableSpinner(this)

        setObservers()
        Utility.showDialog(this@UpdatePv, "")
        pvViewModel.getPvTerminal()

        binding.rvPv.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = pvRecyclerviewAdapter
            it.setItemViewCacheSize(50)
            it.setDrawingCacheEnabled(true)
        }

        var count = 1
        var total = 0;

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
                if (terminalId == 0 || stackId == 0.0f) {
                    try {
                        Toast.makeText(
                            this@UpdatePv,
                            "Invalid Selection please terminal or stack number",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {

                    }

                } else {

                    binding.btAdd.setOnClickListener {

                        pvViewModel.postPvData(
                            PvRequestModel(
                                terminal_id = terminalId.toString(),
                                stack_no = stackId,
                                block_no = list2
                            )
                        )


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
                        pvViewModel.postPvData(
                            PvRequestModel(
                                terminal_id = terminalId.toString(),
                                stack_no = stackId,
                                block_no = list2

                            )
                        )


                    }

                }
            }
        }
    }


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val dhang = intent!!.getStringExtra("dhang")
            val danda = intent.getStringExtra("danda")
            val height = intent.getStringExtra("height")
            val minusPlus = intent.getStringExtra("minusPlus")
            val total = intent.getStringExtra("total")


        }
    }


    private fun setObservers() {
        pvViewModel.postPvResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(this@UpdatePv, "${it.message}", Toast.LENGTH_LONG)
                        .show()
                    Utility.hideDialog(this@UpdatePv)

                }

                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {

                    Utility.hideDialog(this@UpdatePv)

                    if (it.data != null) {
                        it.data.let {
                            Toast.makeText(this@UpdatePv, "${it.message}", Toast.LENGTH_LONG)
                                .show()

                            if (it.status == "1") {
                                pvViewModel.getPvTerminal()
                                finish()
                            } else {

                            }
                        }


                    }
                }
            }
        }
        pvViewModel.pvTerminalResponse.observe(this) {
            val listOfStacks = arrayListOf<String>()
            val listOfTerminals = arrayListOf<String>()

            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(this@UpdatePv, "${it.message}", Toast.LENGTH_LONG).show()
                    Utility.hideDialog(this@UpdatePv)
                    try {
                        Toast.makeText(this@UpdatePv, "${it.message}", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {

                    }
                }

                is NetworkResult.Loading -> {
                    Utility.showDialog(this@UpdatePv, "")
                }

                is NetworkResult.Success -> {
                    Utility.hideDialog(this@UpdatePv)
                    SharedPreferencesRepository.getDataManagerInstance().user.let { userDetails ->

                        if (it.data != null) {
                            terminalList.clear()
                            listOfTerminals.clear();
                            listOfStacks.clear()
                            for (i in (it.data.terminalData as ArrayList<PvResponseModel.TerminalDatum>).indices) {

                                if (userDetails.terminal != null) {
                                    if ((it.data.terminalData as ArrayList<PvResponseModel.TerminalDatum>)[i].terminalId.toString() == userDetails.terminal.toString()) {
                                        terminalList.add((it.data.terminalData as ArrayList<PvResponseModel.TerminalDatum>)[i])
                                        //  listOfTerminals.add("${(it.data.terminalData as ArrayList<PvResponseModel.TerminalDatum>)[i].name}")
                                    }
                                } else {
                                    terminalList.add((it.data.terminalData as ArrayList<PvResponseModel.TerminalDatum>)[i])
                                }
                            }
                            searchableSpinner.windowTitle = "Select Terminal"
                            stackSearchableSpinner.windowTitle = "Select Stack"

                            if (it.data.stackData != null) {
                                stackList.clear()
                                listOfStacks.clear()
                                if (it.data.stackData!!.isNotEmpty()) {
                                    stackList =
                                        (it.data.stackData as ArrayList<PvResponseModel.StackDatum>?)!!
                                    stackList.forEach {
                                        listOfStacks.add(it.stackNo.toString())
                                    }
                                    stackSearchableSpinner.setSpinnerListItems(listOfStacks)
                                    stackSearchableSpinner.onItemSelectListener =
                                        object : OnItemSelectListener {
                                            override fun setOnItemSelectListener(
                                                position: Int,
                                                selectedString: String
                                            ) {
                                                val df = DecimalFormat("#")
                                                val result =
                                                    it.data.stackData!![position].stackNo!!.toFloat()
                                                stackId = result
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



                            for (i in terminalList.indices) {

                                if (userDetails.terminal != null) {
                                    if (terminalList[i].terminalId.toString() == userDetails.terminal.toString()) {
                                        listOfTerminals.add("${terminalList[i].name}")
                                    }
                                } else {
                                    listOfTerminals.add("${terminalList[i].name}")
                                }
                            }


                            searchableSpinner.setSpinnerListItems(listOfTerminals)

                            searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
                                override fun setOnItemSelectListener(
                                    position: Int,
                                    selectedString: String
                                ) {
                                    binding.tvTerminal.text = selectedString
                                    terminalId = terminalList[position].terminalId!!
                                    pvViewModel.getPvTerminal(terminalId = terminalList[position].terminalId)
                                    binding.tvStacks.visibility = View.VISIBLE

                                }
                            }
                            binding.tvTerminal.setOnClickListener { searchableSpinner.show() }
                            binding.tvStacks.setOnClickListener { stackSearchableSpinner.show() }

                        }
                    }
                }
            }
        }

    }


}


fun String.toDate(
    dateFormat: String = "yyyy-MM-dd HH:mm:ss",
    timeZone: TimeZone = TimeZone.getTimeZone("UTC")
): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
    formatter.timeZone = timeZone
    return formatter.format(this)
}