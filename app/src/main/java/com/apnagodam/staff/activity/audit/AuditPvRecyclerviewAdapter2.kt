package com.apnagodam.staff.activity.audit

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.apnagodam.staff.AuditPvRecyclerviewAdapter
import com.apnagodam.staff.Network.PvRequestModel
import com.apnagodam.staff.Network.PvUpdateModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.LayoutAuditPvRvBinding
import com.apnagodam.staff.databinding.LayoutPvRvBinding
import com.apnagodam.staff.utils.EventBus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuditPvRecyclerviewAdapter2 @Inject constructor(
    val list: MutableList<String>,
    val context: Context,
    val scope: LifecycleCoroutineScope,
    val activity: AuditPvActivity
) :
    RecyclerView.Adapter<AuditPvRecyclerviewAdapter2.UpdatePvViewHolder>() {
    class UpdatePvViewHolder(private val binding: LayoutAuditPvRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            context: Context,
            scope: LifecycleCoroutineScope,
            activity: AuditPvActivity,
            position: Int
        ) {
            binding.let {
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

                    }
                }

                it.edPlusMinus.adapter = ArrayAdapter(
                    context, R.layout.support_simple_spinner_dropdown_item, listOfMinusPlus
                )


                var pvModelList = arrayListOf<PvUpdateModel>()

                plusMinusValue.observe(activity) { pm ->
                    if (!danda.value.isNullOrEmpty() && !dhang.value.isNullOrEmpty() && !height.value.isNullOrEmpty() && !it.edMinusPlusvalue.text.isNullOrEmpty()) {
//
//                        scope.launch {
//                            delay(1000)
//                            EventBus.publish(
//                                PvRequestModel.BlockNo(
//                                    block_no = position.toString(),
//                                    danda.value!!,
//                                    dhang.value!!,
//                                    height.value!!,
//                                    plusMinus = it.edMinusPlusvalue.text.toString()
//                                )
//                            )
//                        }
                    }
                }

                danda.observe(activity) { dan ->
                    if (!danda.value.isNullOrEmpty() && !dhang.value.isNullOrEmpty() && !height.value.isNullOrEmpty() && !it.edMinusPlusvalue.text.isNullOrEmpty()) {
                        if (minusPlus.value.equals("+")) {
                            it.edTotal.setText((((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt())).toString())
                        } else if (minusPlus.value.equals("-")) {
                            it.edTotal.setText((((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt())).toString())
                        }

                    }
                }
                dhang.observe(activity) { dh ->
                    if (!danda.value.isNullOrEmpty() && !dhang.value.isNullOrEmpty() && !height.value.isNullOrEmpty() && !it.edMinusPlusvalue.text.isNullOrEmpty()) {
                        if (minusPlus.value.equals("+")) {
                            it.edTotal.setText((((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt())).toString())

                        } else if (minusPlus.value.equals("-")) {
                            it.edTotal.setText((((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt())).toString())

                        }

                    }
                }
                height.observe(activity) { hg ->
                    if (!danda.value.isNullOrEmpty() && !dhang.value.isNullOrEmpty() && !height.value.isNullOrEmpty() && !it.edMinusPlusvalue.text.isNullOrEmpty()) {
                        if (it.edMinusPlusvalue.text.toString().equals("+")) {
                            it.edTotal.setText((((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt())).toString())

                        } else if (minusPlus.value.equals("-")) {
                            it.edTotal.setText((((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt())).toString())

                        }

                    }
                }
                minusPlus.observe(activity) { pm ->
                    if (!danda.value.isNullOrEmpty() && !dhang.value.isNullOrEmpty() && !height.value.isNullOrEmpty() && !minusPlus.value.isNullOrEmpty() && it.edMinusPlusvalue.text.toString()
                            .isNotEmpty()
                    ) {
                        if (minusPlus.value.equals("+")) {
                            it.edTotal.setText(
                                (((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt()) + it.edMinusPlusvalue.text.toString()
                                    .toInt()).toString()
                            )

                        } else if (minusPlus.value.equals("-")) {
                            it.edTotal.setText(
                                (((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt()) - it.edMinusPlusvalue.text.toString()
                                    .toInt()).toString()
                            )

                        }

                    }
                }
                total.observe(activity) { ttl ->
                    if (!danda.value.isNullOrEmpty() && !dhang.value.isNullOrEmpty() && !height.value.isNullOrEmpty() && !it.edMinusPlusvalue.text.isNullOrEmpty()) {
                        if (minusPlus.value.equals("+")) {
                            total.value =
                                (((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt()) + it.edMinusPlusvalue.toString()
                                    .toInt()).toString()
                            it.edTotal.setText(
                                (((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt()) + it.edMinusPlusvalue.toString()
                                    .toInt()).toString()
                            )

                        } else if (minusPlus.value.equals("-")) {
                            it.edTotal.setText(
                                (((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt()) - it.edMinusPlusvalue.toString()
                                    .toInt()).toString()
                            )
                            total.value =
                                (((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt()) + it.edMinusPlusvalue.toString()
                                    .toInt()).toString()

                        }

                    }
                }
                it.edMinusPlusvalue.doOnTextChanged { text, start, before, count ->

                    scope.launch {
                        delay(500).let {

                        }
                        if (!danda.value.isNullOrEmpty() && !dhang.value.isNullOrEmpty() && !height.value.isNullOrEmpty() && !it.edTotalBlocks.text.isNullOrEmpty() && !text.isNullOrEmpty()) {
                            if (minusPlus.value.equals("+")) {

                                val plusMinus = calculatePositiveValues(
                                    dhang.value.toString().toInt(),
                                    danda.value.toString().toInt(),
                                    height.value.toString().toInt(),
                                    text.toString().toInt(),
                                    it.edTotalBlocks.text.toString().toInt()
                                )

                                it.edTotal.setText(
                                    plusMinus.toString()
                                )
                                if (!it.edTotal.text.isNullOrEmpty()) {
                                    Handler().postDelayed({
                                        scope.launch {
                                            EventBus.publish(
                                                PvRequestModel.BlockNo(
                                                    block_no = position.plus(1).toString(),
                                                    danda.value!!,
                                                    dhang.value!!,
                                                    height.value!!,
                                                    plusMinus = it.edMinusPlusvalue.text.toString(),
                                                    total = it.edTotal.text.toString(),
                                                    no_of_blocks = it.edTotalBlocks.toString()

                                                )
                                            )
                                        }

                                    }, 1000)


                                }
                            } else if (minusPlus.value.equals("-")) {
                                val plusMinus = calculateNegativeValues(
                                    dhang.value.toString().toInt(),
                                    danda.value.toString().toInt(),
                                    height.value.toString().toInt(),
                                    text.toString().toInt(),
                                    it.edTotalBlocks.text.toString().toInt()
                                )

                                it.edTotal.setText(
                                    plusMinus.toString()
                                )
                                if (!it.edTotal.text.isNullOrEmpty()) {
                                    Handler().postDelayed({
                                        scope.launch {
                                            EventBus.publish(
                                                PvRequestModel.BlockNo(
                                                    block_no = position.plus(1).toString(),
                                                    danda.value!!,
                                                    dhang.value!!,
                                                    height.value!!,
                                                    plusMinus = "-${it.edMinusPlusvalue.text.toString()}",
                                                    total = it.edTotal.text.toString(),
                                                    no_of_blocks = it.edTotalBlocks.toString()


                                                )
                                            )
                                        }

                                    }, 1000)


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
                        delay(500).let { _ ->
                            if (text.isNullOrEmpty() || text.toString().toInt() == 0) {
                                it.edTotalBlocks.setError("This cant be zero or empty")
                            } else {
                                if (!danda.value.isNullOrEmpty() && !dhang.value.isNullOrEmpty() && !height.value.isNullOrEmpty() && !it.edMinusPlusvalue.text.isNullOrEmpty() && text.isNotEmpty()) {
                                    if (minusPlus.value.equals("+")) {

                                        val plusMinus = calculatePositiveValues(
                                            dhang.value.toString().toInt(),
                                            danda.value.toString().toInt(),
                                            height.value.toString().toInt(),
                                            it.edMinusPlusvalue.text.toString().toInt(),
                                            text.toString().toInt()
                                        )

                                        it.edTotal.setText(
                                            plusMinus.toString()
                                        )
                                        if (!it.edTotal.text.isNullOrEmpty()) {
                                            Handler().postDelayed({
                                                scope.launch {
                                                    EventBus.publish(
                                                        PvRequestModel.BlockNo(
                                                            block_no = (position + 1).toString(),
                                                            danda.value!!,
                                                            dhang.value!!,
                                                            height.value!!,
                                                            plusMinus = it.edMinusPlusvalue.text.toString(),
                                                            total = it.edTotal.text.toString(),
                                                            no_of_blocks = text.toString()

                                                        )
                                                    )

                                                }
                                            }, 1000)


                                        }
                                    } else if (minusPlus.value.equals("-")) {
                                        val plusMinus = calculateNegativeValues(
                                            dhang.value.toString().toInt(),
                                            danda.value.toString().toInt(),
                                            height.value.toString().toInt(),
                                            it.edMinusPlusvalue.text.toString().toInt(),
                                            text.toString().toInt(),

                                            )

                                        it.edTotal.setText(
                                            plusMinus.toString()
                                        )
                                        if (!it.edTotal.text.isNullOrEmpty()) {
                                            Handler().postDelayed({
                                                scope.launch {
                                                    EventBus.publish(
                                                        PvRequestModel.BlockNo(
                                                            block_no = (position + 1).toString(),
                                                            danda.value!!,
                                                            dhang.value!!,
                                                            height.value!!,
                                                            plusMinus = "-${it.edMinusPlusvalue.text.toString()}",
                                                            total = it.edTotal.text.toString(),
                                                            no_of_blocks = it.edTotalBlocks.toString()

                                                        )
                                                    )
                                                }

                                            }, 1000)


                                        }
                                    }


                                }
                            }
                        }


                    }

                }
                it.edDanda.doOnTextChanged { text, start, before, count ->

                    scope.launch {
                        delay(1000)
                        if (text != "0" && text.toString().isNotEmpty()) {
                            danda.value = text.toString()
                        } else {
                            it.edDanda.error = "Value cannot be O or empty"
                        }
                    }

                }
                it.edDhang.doOnTextChanged { text, start, before, count ->
                    scope.launch {
                        delay(1000)
                        if (text != "0" && text.toString().isNotEmpty()) {
                            dhang.value = text.toString()
                        } else {
                            it.edDhang.error = "Value cannot be O or empty"
                        }
                    }
                }
                it.edHeight.doOnTextChanged { text, start, before, count ->

                    scope.launch {
                        delay(1000)
                        if (text != "0" && text.toString().isNotEmpty()) {
                            height.value = text.toString()
                        } else {
                            it.edHeight.error = "Value cannot be O or empty"
                        }
                    }
                }
                it.edPlusMinus.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        minusPlus.value = listOfMinusPlus[position].toString()

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdatePvViewHolder {
        val binding = DataBindingUtil.inflate<LayoutAuditPvRvBinding>(
            LayoutInflater.from(parent.context), R.layout.layout_audit_pv_rv, parent, false
        )

        return AuditPvRecyclerviewAdapter2.UpdatePvViewHolder(
            binding,
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UpdatePvViewHolder, position: Int) {
    }
}