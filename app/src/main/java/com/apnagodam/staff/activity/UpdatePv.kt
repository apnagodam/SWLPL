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
    var stackId: String = "0";
    lateinit var pvUploadRequestModel: PvUploadRequestModel
    var updateModelData = PvRequestModel.BlockNo(
        "", "", "", "", ""
    )
    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_pv)
        pvRecyclerviewAdapter = UpdatePvRecyclerviewAdapter(list, this, lifecycleScope, this)
        searchableSpinner = SearchableSpinner(this)
        stackSearchableSpinner = SearchableSpinner(this)

        setObservers()
        pvViewModel.getPvTerminal()

        binding.rvPv.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = pvRecyclerviewAdapter
        }


        this.lifecycleScope.launch {
            EventBus.subscribe<PvRequestModel.BlockNo> { updateModel ->


                if (updateModelData.block_no == updateModel.block_no) {
                    if (pvModelList.isNotEmpty()) {
                        pvModelList[index].plusMinus = updateModel.plusMinus
                    }
                } else {
                    pvModelList.add(updateModel)
                    index = pvModelList.indexOf(updateModel)

                }
                updateModelData = updateModel

                if (terminalId == 0 || stackId == "0") {
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
                                block_no = pvModelList

                            ), terminalId, stackId
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
                    try {
                        Toast.makeText(this@UpdatePv, it.message, Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {

                    }
                }

                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    if (it.data != null) {
                        pvViewModel.getPvTerminal()
                        finish()

                    }
                }
            }
        }
        pvViewModel.pvTerminalResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    Utility.hideDialog(this@UpdatePv)
                    try {
                        Toast.makeText(this@UpdatePv, it.message, Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {

                    }
                }

                is NetworkResult.Loading -> {
                    Utility.showDialog(this@UpdatePv, "")
                }

                is NetworkResult.Success -> {
                    Utility.hideDialog(this@UpdatePv)

                    if (it.data != null) {
                        val listOfTerminals = arrayListOf<String>()
                        val listOfStacks = arrayListOf<String>()
                        terminalList =
                            it.data.terminalData as ArrayList<PvResponseModel.TerminalDatum>
                        searchableSpinner.windowTitle = "Select Terminal"
                        stackSearchableSpinner.windowTitle = "Select Stack"

                        if (it.data.stackData != null) {
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
                                                it.data.stackData!![position].stackNo!!.toDouble()
                                                    .toInt()

                                            stackId = result.toString()
                                            binding.tvStacks.text = selectedString
                                            binding.btAddPV.visibility = View.VISIBLE
                                            binding.btAddPV.setOnClickListener {
                                                list.add("hello")
                                                pvRecyclerviewAdapter.notifyItemInserted(list.size)
                                            }

                                        }

                                    }
                            }
                        }

                        SharedPreferencesRepository.getDataManagerInstance().user.let { userDetails ->


                            for (i in terminalList.indices) {

                                if (userDetails.terminal != null) {
                                    if (terminalList[i].terminalId.toString() == userDetails.terminal.toString()) {
                                        listOfTerminals.add("${terminalList[i].name}")
                                    }
                                } else {
                                    listOfTerminals.add("${terminalList[i].name}")
                                }
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

inline fun <T, R> Iterable<T>.allUniqueBy(transform: (T) -> R): Boolean {
    val hashset = hashSetOf<R>()
    return this.all { hashset.add(transform(it)) }
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
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}