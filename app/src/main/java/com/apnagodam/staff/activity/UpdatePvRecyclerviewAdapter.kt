package com.apnagodam.staff.activity

import android.content.Context
import android.content.Intent
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
import com.apnagodam.staff.databinding.LayoutPvRvBinding
import com.apnagodam.staff.utils.EventBus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class UpdatePvRecyclerviewAdapter @Inject constructor(
    val list: ArrayList<String>,
    val context: Context,
    val scope: LifecycleCoroutineScope,
    val activity: UpdatePv
) :
    RecyclerView.Adapter<UpdatePvRecyclerviewAdapter.UpdatePvViewholder>() {

    class UpdatePvViewholder(
        private val layoutPvRvBinding: LayoutPvRvBinding,
        private val context: Context,
        private val activity: UpdatePv
    ) :
        RecyclerView.ViewHolder(layoutPvRvBinding.root) {

        fun bind(
            context: Context,
            scope: LifecycleCoroutineScope,
            activity: UpdatePv,
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
                var listOfDanda = (1..10).toList()

                var plusMinusValue = MutableLiveData<String>()


                var listOfMinusPlus = arrayListOf<String>()
                listOfMinusPlus.add("+")
                listOfMinusPlus.add("-")
                it.edDanda.adapter = ArrayAdapter(
                    context,
                    R.layout.multiline_spinner_dropdown_item,
                    listOfDanda
                )
                it.edDhang.adapter = ArrayAdapter(
                    context,
                    R.layout.multiline_spinner_dropdown_item,
                    listOfDanda
                )
                it.edHeight.adapter = ArrayAdapter(
                    context,
                    R.layout.multiline_spinner_dropdown_item,
                    listOfDanda
                )
                it.edPlusMinus.adapter =
                    ArrayAdapter(
                        context,
                        R.layout.multiline_spinner_dropdown_item,
                        listOfMinusPlus
                    )


                var pvModelList = arrayListOf<PvUpdateModel>()

                plusMinusValue.observe(activity) { pm ->
                    if (!danda.value.isNullOrEmpty() && !dhang.value.isNullOrEmpty() && !height.value.isNullOrEmpty() && !it.edMinusPlusvalue.text.isNullOrEmpty()) {

                        scope.launch {
                            delay(1000)
                            EventBus.publish(
                                PvRequestModel.BlockNo(
                                    block_no = position.toString(),
                                    danda.value!!,
                                    dhang.value!!,
                                    height.value!!,
                                    plusMinus = it.edMinusPlusvalue.text.toString()
                                )
                            )
                        }
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
                        delay(1000)
                        if (!danda.value.isNullOrEmpty() && !dhang.value.isNullOrEmpty() && !height.value.isNullOrEmpty() && !text.isNullOrEmpty()) {
                            if (minusPlus.value.equals("+")) {
                                val plusMinus =
                                    (((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt()) + text.toString()
                                        .toInt()).toString()
                                it.edTotal.setText(
                                    plusMinus
                                )
                                if (!it.edMinusPlusvalue.text.isNullOrEmpty()) {

                                    EventBus.publish(
                                        PvRequestModel.BlockNo(
                                            block_no = position.toString(),
                                            danda.value!!,
                                            dhang.value!!,
                                            height.value!!,
                                            plusMinus = it.edMinusPlusvalue.text.toString()
                                        )
                                    )
                                }
                            } else if (minusPlus.value.equals("-")) {
                                val plusMinus =
                                    (((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt()) - text.toString()
                                        .toInt()).toString()
                                it.edTotal.setText(
                                    plusMinus
                                )
                                if (!it.edMinusPlusvalue.text.isNullOrEmpty()) {

                                    EventBus.publish(
                                        PvRequestModel.BlockNo(
                                            block_no = position.toString(),
                                            danda.value!!,
                                            dhang.value!!,
                                            height.value!!,
                                            plusMinus = it.edMinusPlusvalue.text.toString()
                                        )
                                    )
                                }
                            }


                        }

                    }


                }
                it.edTotal.doOnTextChanged { text, start, before, count ->

                    scope.launch {
                        delay(1000)
                        if (!danda.value.isNullOrEmpty() && !dhang.value.isNullOrEmpty() && !height.value.isNullOrEmpty() && !it.edMinusPlusvalue.text.isNullOrEmpty()) {
                            if (minusPlus.value.equals("+")) {
                                val plusMinus =
                                    (((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt()) + it.edMinusPlusvalue.text.toString()
                                        .toInt()).toString()
                                it.edTotal.setText(
                                    plusMinus
                                )
                                if (!it.edTotal.text.isNullOrEmpty()) {

                                    EventBus.publish(
                                        PvRequestModel.BlockNo(
                                            block_no = position.toString(),
                                            danda.value!!,
                                            dhang.value!!,
                                            height.value!!,
                                            plusMinus = it.edMinusPlusvalue.text.toString()
                                        )
                                    )
                                }
                            } else if (minusPlus.value.equals("-")) {
                                val plusMinus =
                                    (((dhang.value!!.toInt() + danda.value!!.toInt()) * height.value!!.toInt()) - it.edMinusPlusvalue.text.toString()
                                        .toInt()).toString()
                                it.edTotal.setText(
                                    plusMinus
                                )
                                if (!it.edTotal.text.isNullOrEmpty()) {

                                    EventBus.publish(
                                        PvRequestModel.BlockNo(
                                            block_no = position.toString(),
                                            danda.value!!,
                                            dhang.value!!,
                                            height.value!!,
                                            plusMinus = it.edMinusPlusvalue.text.toString()
                                        )
                                    )
                                }
                            }


                        }

                    }
                }

                it.edDanda.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
                    AdapterView.OnItemClickListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        danda.value = listOfDanda[position].toString()

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        danda.value = listOfDanda[position].toString()

                    }

                }
                it.edDhang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        dhang.value = listOfDanda[position].toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                }
                it.edPlusMinus.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        minusPlus.value = listOfMinusPlus[position].toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                }
                it.edHeight.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        height.value = listOfDanda[position].toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        height.value = listOfDanda[position].toString()

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
                                    plusMinus = it.edMinusPlusvalue.text.toString()
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdatePvViewholder {
        val binding = DataBindingUtil.inflate<LayoutPvRvBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_pv_rv,
            parent,
            false
        )
        return UpdatePvViewholder(binding, parent.context, activity)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UpdatePvViewholder, position: Int) {
        holder.bind(context, scope, activity, position)

    }
}