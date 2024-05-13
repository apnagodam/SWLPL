package com.apnagodam.staff

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.apnagodam.staff.Network.PvRequestModel
import com.apnagodam.staff.Network.PvUpdateModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.audit.AuditPvActivity
import com.apnagodam.staff.databinding.LayoutAuditPvRvBinding
import com.apnagodam.staff.databinding.LayoutPvRvBinding
import com.apnagodam.staff.utils.EventBus
import com.apnagodam.staff.utils.RecyclerviewCallBack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class AuditPvRecyclerviewAdapter @Inject constructor(
    val list: MutableList<String>,
    val context: Context,
    val scope: LifecycleCoroutineScope,
    val activity: AuditPvActivity
) : RecyclerView.Adapter<AuditPvRecyclerviewAdapter.UpdatePvViewholder>() {
    var itemPosition = 0
    var dhangValue = ""
    var dandaValue = ""
    var height = ""

    fun setDanda(danda: String) {
        dandaValue = danda
    }

    fun getDanda() = dandaValue
    fun setPosition(position: Int) {
        itemPosition = position
    }

    fun getItem() = list

    fun getPosition(): Int = itemPosition

    var recyclerviewCallBack: RecyclerviewCallBack? = null

    class UpdatePvViewholder(
        private val layoutPvRvBinding: LayoutAuditPvRvBinding,
        private val context: Context,
        private val activity: AuditPvActivity,
        private val list: MutableList<String>,
        private val recyclerviewCallBack: RecyclerviewCallBack?
    ) : RecyclerView.ViewHolder(layoutPvRvBinding.root), RecyclerviewCallBack {


        fun bind(
            context: Context,
            scope: LifecycleCoroutineScope,
            activity: AuditPvActivity,
            position: Int
        ) {
            layoutPvRvBinding.let {
                val intent = Intent("custom-message")

                it.dhangLabel.text = "Block ${position + 1}"
                it.dhangLabel.paint?.isUnderlineText = true
                var danda = MutableLiveData<String>()
                var dhang = MutableLiveData<String>()
                var height = MutableLiveData<String>()
                var minusPlus = MutableLiveData<String>()
                var total = MutableLiveData<String>()
                var listOfDanda = (1..50).toList()

                var plusMinusValue = MutableLiveData<String>()


                var listOfMinusPlus = arrayListOf<String>()
                listOfMinusPlus.add("+")
                listOfMinusPlus.add("-")

                it.fabRemoveItem.setOnClickListener { _ ->
                    if (it.edMinusPlusvalue.hasFocus()) it.edMinusPlusvalue.clearFocus()
                    bindingAdapter?.let { adapter ->
                        it.edDanda.setText("0")
                        it.edHeight.setText("0")
                        it.edDhang.setText("0")
                        it.edMinusPlusvalue.setText("0")
                        it.edTotalBlocks.setText("1")
                        it.edTotal.setText("0")
                        scope.launch {
                            EventBus.publish(
                                PvRequestModel.BlockNo(
                                    block_no = position.plus(1).toString(),
                                    it.edDanda.text.toString(),
                                    it.edDhang.text.toString(),
                                    it.edHeight.text.toString(),
                                    plusMinus = it.edMinusPlusvalue.text.toString(),
                                    total = it.edTotal.text.toString(),
                                    no_of_blocks = it.edTotalBlocks.text.toString()

                                )
                            )
                        }
                        recyclerviewCallBack?.removeItem(position)
                        adapter.notifyDataSetChanged()

                    }
                }

                it.edPlusMinus.adapter = ArrayAdapter(
                    context, R.layout.support_simple_spinner_dropdown_item, listOfMinusPlus
                )


                var pvModelList = arrayListOf<PvUpdateModel>()


                it.edMinusPlusvalue.doOnTextChanged { text, start, before, count ->

                    scope.launch {
                        delay(100).let {

                        }
                        if (!it.edDanda.text.isNullOrEmpty() && !it.edDhang.text.toString()
                                .isNullOrEmpty() && !it.edHeight.text.toString()
                                .isNullOrEmpty() && !it.edTotalBlocks.text.isNullOrEmpty() && !text.toString()
                                .isNullOrEmpty()
                        ) {
                            if (minusPlus.value.equals("+")) {

                                val plusMinus = calculatePositiveValues(
                                    it.edDhang.text.toString().toInt(),
                                    it.edDanda.text.toString().toInt(),
                                    it.edHeight.text.toString().toInt(),
                                    text.toString().toInt(),
                                    it.edTotalBlocks.text.toString().toInt()
                                )

                                it.edTotal.setText(
                                    plusMinus.toString()
                                )
                                if (!it.edTotal.text.isNullOrEmpty()) {
                                    scope.launch {
                                        EventBus.publish(
                                            PvRequestModel.BlockNo(
                                                block_no = position.plus(1).toString(),
                                                it.edDanda.text.toString(),
                                                it.edDhang.text.toString(),
                                                it.edHeight.text.toString(),
                                                plusMinus = text.toString(),
                                                total = it.edTotal.text.toString(),
                                                no_of_blocks = it.edTotalBlocks.text.toString()

                                            )
                                        )
                                    }


                                }
                            } else if (minusPlus.value.equals("-")) {
                                val plusMinus = calculateNegativeValues(
                                    it.edDhang.text.toString().toInt(),
                                    it.edDanda.text.toString().toInt(),
                                    it.edHeight.text.toString().toInt(),
                                    text.toString().toInt(),
                                    it.edTotalBlocks.text.toString().toInt()
                                )

                                it.edTotal.setText(
                                    plusMinus.toString()
                                )
                                if (!it.edTotal.text.isNullOrEmpty()) {
                                    scope.launch {
                                        EventBus.publish(
                                            PvRequestModel.BlockNo(
                                                block_no = position.plus(1).toString(),
                                                it.edDanda.text.toString(),
                                                it.edDhang.text.toString(),
                                                it.edHeight.text.toString(),
                                                plusMinus = "-${text}",
                                                total = it.edTotal.text.toString(),
                                                no_of_blocks = it.edTotalBlocks.toString()


                                            )
                                        )
                                    }


                                }
                            }


                        }
//                        if (!danda.value.isNullOrEmpty() && !dhang.value.isNullOrEmpty() && !height.value.isNullOrEmpty() && !text.isNullOrEmpty()) {
//                            if (minusPlus.value.equals("+")) {
//                                val plusMinus =
//                                    (((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt()) + text.toString()
//                                        .toInt()).toString()
//                                it.edTotal.setText(
//                                    plusMinus
//                                )
//                                if (!it.edTotal.text.isNullOrEmpty()) {
//                                    Handler().postDelayed({
//                                        scope.launch {
//                                            EventBus.publish(
//                                                PvRequestModel.BlockNo(
//                                                    block_no = position.toString(),
//                                                    danda.value!!,
//                                                    dhang.value!!,
//                                                    height.value!!,
//                                                    plusMinus = it.edMinusPlusvalue.text.toString(),
//                                                    total = it.edTotal.text.toString()
//
//                                                )
//                                            )
//                                        }
//
//                                    }, 1000)
//
//
//                                }
//                            } else if (minusPlus.value.equals("-")) {
//                                val plusMinus =
//                                    (((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt()) - text.toString()
//                                        .toInt()).toString()
//                                it.edTotal.setText(
//                                    plusMinus
//                                )
//                                if (!it.edTotal.text.isNullOrEmpty()) {
//                                    Handler().postDelayed({
//                                        scope.launch {
//                                            EventBus.publish(
//                                                PvRequestModel.BlockNo(
//                                                    block_no = position.toString(),
//                                                    danda.value!!,
//                                                    dhang.value!!,
//                                                    height.value!!,
//                                                    plusMinus = it.edMinusPlusvalue.text.toString(),
//                                                    total = it.edTotal.text.toString()
//
//                                                )
//                                            )
//                                        }
//
//                                    }, 1000)
//
//
//                                }
//                            }
//
//
//                        }

                    }


                }
                it.edTotalBlocks.doOnTextChanged { text, start, before, count ->
                    scope.launch {
                        delay(100).let { _ ->
                            if (text.isNullOrEmpty() || text.toString().toInt() == 0) {
                                it.edTotalBlocks.setError("This cant be zero or empty")
                            } else {
                                if (!it.edDanda.text.isNullOrEmpty() && !it.edDhang.text.toString()
                                        .isNullOrEmpty() && !it.edHeight.text.toString()
                                        .isNullOrEmpty() && !it.edMinusPlusvalue.text.isNullOrEmpty() && text.isNotEmpty()
                                ) {
                                    if (minusPlus.value.equals("+")) {

                                        val plusMinus = calculatePositiveValues(
                                            it.edDhang.text.toString().toInt(),
                                            it.edDanda.text.toString().toInt(),
                                            it.edHeight.text.toString().toInt(),
                                            it.edMinusPlusvalue.text.toString().toInt(),
                                            text.toString().toInt()
                                        )

                                        it.edTotal.setText(
                                            plusMinus.toString()
                                        )
                                        if (!it.edTotal.text.isNullOrEmpty()) {
                                            scope.launch {
                                                EventBus.publish(
                                                    PvRequestModel.BlockNo(
                                                        block_no = (position + 1).toString(),
                                                        it.edDanda.text.toString(),
                                                        it.edDhang.text.toString(),
                                                        it.edHeight.text.toString(),
                                                        plusMinus = it.edMinusPlusvalue.text.toString(),
                                                        total = it.edTotal.text.toString(),
                                                        no_of_blocks = text.toString()

                                                    )
                                                )

                                            }

                                        }
                                    } else if (minusPlus.value.equals("-")) {
                                        val plusMinus = calculateNegativeValues(
                                            it.edDhang.text.toString().toInt(),
                                            it.edDanda.text.toString().toInt(),
                                            it.edHeight.text.toString().toInt(),
                                            it.edMinusPlusvalue.text.toString().toInt(),
                                            text.toString().toInt(),

                                            )

                                        it.edTotal.setText(
                                            plusMinus.toString()
                                        )
                                        if (!it.edTotal.text.isNullOrEmpty()) {
                                            scope.launch {
                                                EventBus.publish(
                                                    PvRequestModel.BlockNo(
                                                        block_no = (position + 1).toString(),
                                                        it.edDanda.text.toString(),
                                                        it.edDhang.text.toString(),
                                                        it.edHeight.text.toString(),
                                                        plusMinus = "-${it.edMinusPlusvalue.text.toString()}",
                                                        total = it.edTotal.text.toString(),
                                                        no_of_blocks = text.toString()

                                                    )
                                                )
                                            }


                                        }
                                    }


                                }
                            }
                        }


                    }

                }
                it.edDanda.doOnTextChanged { text, start, before, count ->

                    scope.launch {
                        delay(100).let { _ ->
                            if (text.isNullOrEmpty() || text.toString().toInt() == 0) {
                                it.edDanda.setError("This cant be zero or empty")
                            } else {
                                if (!it.edMinusPlusvalue.text.isNullOrEmpty() && !it.edDhang.text.toString()
                                        .isNullOrEmpty() && !it.edHeight.text.toString()
                                        .isNullOrEmpty() && !it.edTotalBlocks.text.isNullOrEmpty() && !text.toString()
                                        .isNullOrEmpty()
                                ) {
                                    if (minusPlus.value.equals("+")) {

                                        val plusMinus = calculatePositiveValues(
                                            it.edDhang.text.toString().toInt(),
                                            text.toString().toInt(),
                                            it.edHeight.text.toString().toInt(),
                                            it.edMinusPlusvalue.text.toString().toInt(),
                                            it.edTotalBlocks.text.toString().toInt()
                                        )

                                        it.edTotal.setText(
                                            plusMinus.toString()
                                        )
                                        if (!it.edTotal.text.isNullOrEmpty()) {
                                            scope.launch {
                                                EventBus.publish(
                                                    PvRequestModel.BlockNo(
                                                        block_no = (position + 1).toString(),
                                                        text.toString(),
                                                        it.edDanda.text.toString(),
                                                        it.edHeight.text.toString(),
                                                        plusMinus = it.edMinusPlusvalue.text.toString(),
                                                        total = it.edTotal.text.toString(),
                                                        no_of_blocks = it.edTotalBlocks.text.toString()

                                                    )
                                                )

                                            }


                                        }
                                    } else if (minusPlus.value.equals("-")) {
                                        val plusMinus = calculateNegativeValues(
                                            it.edDhang.text.toString().toInt(),
                                            text.toString().toInt(),
                                            it.edHeight.text.toString().toInt(),
                                            it.edMinusPlusvalue.text.toString().toInt(),
                                            it.edTotalBlocks.toString().toInt()
                                        )

                                        it.edTotal.setText(
                                            plusMinus.toString()
                                        )
                                        if (!it.edTotal.text.isNullOrEmpty()) {
                                            scope.launch {
                                                EventBus.publish(
                                                    PvRequestModel.BlockNo(
                                                        block_no = (position + 1).toString(),
                                                        text.toString(),
                                                        it.edDanda.text.toString(),
                                                        it.edHeight.text.toString(),
                                                        plusMinus = it.edMinusPlusvalue.text.toString(),
                                                        total = it.edTotal.text.toString(),
                                                        no_of_blocks = it.edTotalBlocks.toString()

                                                    )
                                                )
                                            }


                                        }
                                    }


                                }
                            }
                        }


                    }

                }
                it.edDhang.doOnTextChanged { text, start, before, count ->
                    scope.launch {
                        delay(100).let { _ ->
                            if (text.isNullOrEmpty() || text.toString().toInt() == 0) {
                                it.edDhang.setError("This cant be zero or empty")
                            } else {
                                if (!it.edMinusPlusvalue.text.isNullOrEmpty() && !it.edDhang.text.toString()
                                        .isNullOrEmpty() && !it.edHeight.text.toString()
                                        .isNullOrEmpty() && !it.edTotalBlocks.text.isNullOrEmpty() && !text.toString()
                                        .isNullOrEmpty()
                                ) {
                                    if (minusPlus.value.equals("+")) {

                                        val plusMinus = calculatePositiveValues(
                                            text.toString().toInt(),
                                            it.edDanda.text.toString().toInt(),
                                            it.edHeight.text.toString().toInt(),
                                            it.edMinusPlusvalue.text.toString().toInt(),
                                            it.edTotalBlocks.text.toString().toInt()
                                        )

                                        it.edTotal.setText(
                                            plusMinus.toString()
                                        )
                                        if (!it.edTotal.text.isNullOrEmpty()) {
                                            scope.launch {
                                                EventBus.publish(
                                                    PvRequestModel.BlockNo(
                                                        block_no = (position + 1).toString(),
                                                        it.edDanda.text.toString(),
                                                        text.toString(),
                                                        it.edHeight.text.toString(),
                                                        plusMinus = it.edMinusPlusvalue.text.toString(),
                                                        total = it.edTotal.text.toString(),
                                                        no_of_blocks = it.edTotalBlocks.text.toString()

                                                    )
                                                )

                                            }

                                        }
                                    } else if (minusPlus.value.equals("-")) {
                                        val plusMinus = calculateNegativeValues(
                                            text.toString().toInt(),
                                            it.edDanda.text.toString().toInt(),
                                            it.edHeight.text.toString().toInt(),
                                            it.edMinusPlusvalue.text.toString().toInt(),
                                            it.edTotalBlocks.text.toString().toInt()
                                        )

                                        it.edTotal.setText(
                                            plusMinus.toString()
                                        )
                                        if (!it.edTotal.text.isNullOrEmpty()) {
                                            scope.launch {
                                                EventBus.publish(
                                                    PvRequestModel.BlockNo(
                                                        block_no = (position + 1).toString(),
                                                        it.edDanda.text.toString(),
                                                        text.toString(),
                                                        it.edHeight.text.toString(),
                                                        plusMinus = it.edMinusPlusvalue.text.toString(),
                                                        total = it.edTotal.text.toString(),
                                                        no_of_blocks = it.edTotalBlocks.text.toString()

                                                    )
                                                )
                                            }


                                        }
                                    }


                                }
                            }
                        }


                    }
                }
                it.edHeight.doOnTextChanged { text, start, before, count ->

                    scope.launch {
                        delay(100).let { _ ->
                            if (text.isNullOrEmpty() || text.toString().toInt() == 0) {
                                it.edDhang.setError("This cant be zero or empty")
                            } else {
                                if (!it.edMinusPlusvalue.text.isNullOrEmpty() && !it.edDhang.text.toString()
                                        .isNullOrEmpty() && !it.edHeight.text.toString()
                                        .isNullOrEmpty() && !it.edTotalBlocks.text.isNullOrEmpty() && !text.toString()
                                        .isNullOrEmpty()
                                ) {
                                    if (minusPlus.value.equals("+")) {

                                        val plusMinus = calculatePositiveValues(
                                            it.edDhang.text.toString().toInt(),
                                            it.edDanda.text.toString().toInt(),
                                            text.toString().toInt(),
                                            it.edMinusPlusvalue.text.toString().toInt(),
                                            it.edTotalBlocks.text.toString().toInt()
                                        )

                                        it.edTotal.setText(
                                            plusMinus.toString()
                                        )
                                        if (!it.edTotal.text.isNullOrEmpty()) {
                                            scope.launch {
                                                EventBus.publish(
                                                    PvRequestModel.BlockNo(
                                                        block_no = (position + 1).toString(),
                                                        it.edDanda.text.toString(),
                                                        it.edDhang.text.toString(),
                                                        text.toString(),
                                                        plusMinus = it.edMinusPlusvalue.text.toString(),
                                                        total = it.edTotal.text.toString(),
                                                        no_of_blocks = it.edTotalBlocks.text.toString()

                                                    )
                                                )

                                            }

                                        }
                                    } else if (minusPlus.value.equals("-")) {
                                        val plusMinus = calculateNegativeValues(
                                            it.edDhang.text.toString().toInt(),
                                            it.edDanda.toString().toInt(),
                                            text.toString().toInt(),
                                            it.edMinusPlusvalue.text.toString().toInt(),
                                            it.edTotalBlocks.text.toString().toInt()
                                        )

                                        it.edTotal.setText(
                                            plusMinus.toString()
                                        )
                                        if (!it.edTotal.text.isNullOrEmpty()) {
                                            scope.launch {
                                                EventBus.publish(
                                                    PvRequestModel.BlockNo(
                                                        block_no = (position + 1).toString(),
                                                        it.edDanda.text.toString(),
                                                        it.edDhang.toString(),
                                                        text.toString(),
                                                        plusMinus = it.edMinusPlusvalue.text.toString(),
                                                        total = it.edTotal.text.toString(),
                                                        no_of_blocks = it.edTotalBlocks.text.toString()

                                                    )
                                                )
                                            }


                                        }
                                    }


                                }
                            }
                        }


                    }
                }
                it.edPlusMinus.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        minusPlus.value = listOfMinusPlus[position].toString()
                        recyclerviewCallBack?.getDanda(minusPlus.value.toString())

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                }



                it.btSubmit.setOnClickListener { _ ->
                    if (!it.edTotal.text.isNullOrEmpty()) {

                        scope.launch {
                            EventBus.publish(
                                PvRequestModel.BlockNo(
                                    block_no = position.toString(),
                                    danda.value!!,
                                    dhang.value!!,
                                    height.value!!,
                                    plusMinus = it.edMinusPlusvalue.text.toString(),
                                    total = it.edTotal.text.toString()
                                )
                            )
                        }
                    }


                }


//                it.edDhang.doAfterTextChanged { editable ->
//                    scope.launch {
//                        delay(1000).let {
//                            dhang = editable.toString()
//                            intent.putExtra("dhang", dhang)
//                        }
//
//                    }
//
//                }
//                it.edDanda.doAfterTextChanged { editable ->
//                    scope.launch {
//                        delay(1000).let {
//                            danda = editable.toString()
//                            intent.putExtra("danda", danda)
//                        }
//
//                    }
//
//                }
//
//                it.edHeight.doAfterTextChanged { editable ->
//                    scope.launch {
//                        delay(1000).let {
//                            height = editable.toString()
//                            intent.putExtra("height", height)
//                        }
//
//                    }
//
//                }
//
//                it.edPlusMinus.doAfterTextChanged { editable ->
//                    scope.launch {
//                        delay(1000).let {
//                            minusPlus = editable.toString()
//                            intent.putExtra("minusPlus", minusPlus)
//                        }
//
//                    }
//
//                }
//
//                it.edTotal.doAfterTextChanged { editable ->
//                    scope.launch {
//
//                        minusPlus = editable.toString()
//                        total = editable.toString()
//                        intent.putExtra("total", total)
//
//                        delay(1000).let {
//
//                          //  EventBus.publish()
//                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
//
//                        }
//                    }
//
//
//                }
//

            }
        }

        fun calculatePositiveValues(
            dhang: Int, danda: Int, height: Int, plusMinusValue: Int?, totalBlock: Int
        ): Int {
            if (plusMinusValue == null || plusMinusValue.equals(0)) {
                return (((dhang + danda) * height)) * totalBlock
            } else return (((dhang + danda) * height) + plusMinusValue) * totalBlock
        }


        fun calculateNegativeValues(
            dhang: Int, danda: Int, height: Int, plusMinusValue: Int?, totalBlock: Int
        ): Int {
            if (plusMinusValue == null || plusMinusValue.equals(0)) {
                return (((dhang + danda) * height)) * totalBlock
            } else return (((dhang + danda) * height) - plusMinusValue) * totalBlock
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdatePvViewholder {
        val binding = DataBindingUtil.inflate<LayoutAuditPvRvBinding>(
            LayoutInflater.from(parent.context), R.layout.layout_audit_pv_rv, parent, false
        )

        return UpdatePvViewholder(binding, parent.context, activity, list, recyclerviewCallBack)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UpdatePvViewholder, position: Int) {
        setPosition(position)
        holder.bind(context, scope, activity, position)

    }


}

