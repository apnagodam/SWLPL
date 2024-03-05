package com.apnagodam.staff.activity

import android.app.Activity
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
import com.apnagodam.staff.Network.Request.ApprovedIntantionPOst
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.viewmodel.IntensionViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.adapter.IntantionListAdpter
import com.apnagodam.staff.databinding.IntantionListBinding
import com.apnagodam.staff.module.AllIntantionList
import com.apnagodam.staff.utils.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntantionApprovalListClass() : BaseActivity<IntantionListBinding?>() {
    private var intantionListAdpter: IntantionListAdpter? = null
    private var pageOffset = 1
    private var totalPage = 0
    private var getOrdersList = arrayListOf<AllIntantionList.Datum>()
    private var tempraryList = arrayListOf<AllIntantionList.Datum>()
    private var inventory_id = ""

    val intentionViewModel by  viewModels<IntensionViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.intantion_list
    }

    override fun setUp() {
        getOrdersList = arrayListOf()
        tempraryList = arrayListOf()
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.ivClose.setOnClickListener(View.OnClickListener {
            finish()
        })
        setDataAdapter()
        getConvencyList("")
        binding!!.tvPrevious.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (pageOffset != 1) {
                    pageOffset--
                    getConvencyList("")
                }
            }
        })
        binding!!.tvNext.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (totalPage != pageOffset) {
                    pageOffset++
                    getConvencyList("")
                }
            }
        })
        binding!!.swipeRefresherStock.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh() {
                getConvencyList("")
            }
        })
        binding!!.filterIcon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val builder = AlertDialog.Builder(this@IntantionApprovalListClass)
                val inflater = (this@IntantionApprovalListClass as Activity).layoutInflater
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
                            getConvencyList(notes.text.toString().trim { it <= ' ' })
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Please Fill Text",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })
            }
        })
    }

    private fun getConvencyList(SerachKey: String) {
        showDialog()
        intentionViewModel.getintentionList("15", pageOffset, SerachKey)
        intentionViewModel.intentionListResponse.observe(this){
            when(it){
                is NetworkResult.Error -> {
                    hideDialog()
                    showToast(it.message)
                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    hideDialog()
                    tempraryList!!.clear()
                    getOrdersList!!.clear()
                    binding!!.swipeRefresherStock.isRefreshing = false
                    if(it.data!=null){
                        if (it.data.data.dataa.size == 0) {
                            binding!!.txtemptyMsg.visibility = View.VISIBLE
                            binding!!.fieldStockList.visibility = View.GONE
                            binding!!.pageNextPrivious.visibility = View.GONE
                        } else if (it.data.data.lastPage == 1) {
                            binding!!.txtemptyMsg.visibility = View.GONE
                            binding!!.fieldStockList.visibility = View.VISIBLE
                            binding!!.pageNextPrivious.visibility = View.GONE
                            getOrdersList!!.clear()
                            tempraryList!!.clear()
                            totalPage = it.data.data.lastPage
                            getOrdersList!!.addAll(it.data.data.dataa)
                            tempraryList!!.addAll((getOrdersList)!!)
                            intantionListAdpter!!.notifyDataSetChanged()
                        } else {
                            binding!!.txtemptyMsg.visibility = View.GONE
                            binding!!.fieldStockList.visibility = View.VISIBLE
                            binding!!.pageNextPrivious.visibility = View.VISIBLE
                            getOrdersList!!.clear()
                            tempraryList!!.clear()
                            totalPage = it.data.data.lastPage
                            getOrdersList!!.addAll(it.data.data.dataa)
                            tempraryList!!.addAll((getOrdersList)!!)
                            intantionListAdpter!!.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

    }

    private fun setDataAdapter() {
        binding!!.fieldStockList.addItemDecoration(
            DividerItemDecoration(
                this@IntantionApprovalListClass,
                LinearLayoutManager.HORIZONTAL
            )
        )
        binding!!.fieldStockList.setHasFixedSize(true)
        binding!!.fieldStockList.isNestedScrollingEnabled = false
        val horizontalLayoutManager = LinearLayoutManager(
            this@IntantionApprovalListClass,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.fieldStockList.layoutManager = horizontalLayoutManager
        intantionListAdpter =
            IntantionListAdpter(getOrdersList, this@IntantionApprovalListClass, activity)
        binding!!.fieldStockList.adapter = intantionListAdpter
    }

    override fun onResume() {
        super.onResume()
        getConvencyList("")

    }

    fun ViewData(position: Int) {
        val displayRectangle = Rect()
        val window = this.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val builder =
            AlertDialog.Builder(this@IntantionApprovalListClass, R.style.CustomAlertDialog)
        val inflater = this@IntantionApprovalListClass.layoutInflater
        val dialogView = inflater.inflate(R.layout.intantion_view, null)
        dialogView.minimumWidth = (displayRectangle.width() * 1f).toInt()
        dialogView.minimumHeight = (displayRectangle.height() * 1f).toInt()
        builder.setView(dialogView)
        val alertDialog = builder.create()
        val cancel_btn = dialogView.findViewById<ImageView>(R.id.cancel_btn)
        val date = dialogView.findViewById<TextView>(R.id.genrated_by)
        val Terminal = dialogView.findViewById<TextView>(R.id.customer_name)
        val customer_name = dialogView.findViewById<TextView>(R.id.phone_no)
        val mobile = dialogView.findViewById<TextView>(R.id.location_title)
        val coustomer_address = dialogView.findViewById<TextView>(R.id.commodity_name)
        val commodity_name = dialogView.findViewById<TextView>(R.id.est_quantity_nmae)
        val packingType = dialogView.findViewById<TextView>(R.id.terminal_name)
        val totalBags = dialogView.findViewById<TextView>(R.id.purpose_name)
        val weightQtl = dialogView.findViewById<TextView>(R.id.commitemate_date)
        val sellingPrice = dialogView.findViewById<TextView>(R.id.create_date)
        val quality_grade = dialogView.findViewById<TextView>(R.id.converted_by)
        val gst = dialogView.findViewById<TextView>(R.id.vehicle_no)
        val coustomer_gsst_no = dialogView.findViewById<TextView>(R.id.in_out)
        val finalPprice = dialogView.findViewById<TextView>(R.id.gate_passs)
        val paymentstatus = dialogView.findViewById<TextView>(R.id.start_date_time)
        inventory_id = "" + getOrdersList!![position]!!.id
        date.text = "" + (if ((getOrdersList!!.get(position)!!
                .updatedAt) != null
        ) getOrdersList!!.get(position)!!.updatedAt else "N/A")
        Terminal.text = "" + (if ((getOrdersList!!.get(position)!!
                .warehouseName) != null
        ) getOrdersList!!.get(position)!!.warehouseName else "N/A")
        customer_name.text = "" + (if ((getOrdersList!!.get(position)!!
                .fname) != null
        ) getOrdersList!!.get(position)!!.fname else "N/A")
        mobile.text = "" + (if ((getOrdersList!!.get(position)!!
                .phone) != null
        ) getOrdersList!!.get(position)!!.phone else "N/A")
        coustomer_address.text = "" + (if ((getOrdersList!!.get(position)!!
                .area_vilage) != null
        ) getOrdersList!!.get(position)!!
            .area_vilage + " " + getOrdersList!!.get(position)!!
            .city + " " + getOrdersList!!.get(position)!!.state else "N/A")
        commodity_name.text = "" + (if ((getOrdersList!!.get(position)!!
                .category) != null
        ) getOrdersList!!.get(position)!!.category else "N/A")
        packingType.text = "" + (if ((getOrdersList!!.get(position)!!
                .packing) != null
        ) getOrdersList!!.get(position)!!.packing else "N/A")
        totalBags.text = "" + (if ((getOrdersList!!.get(position)!!
                .totalBags) != null
        ) getOrdersList!!.get(position)!!.totalBags else "N/A")
        weightQtl.text = "" + (if ((getOrdersList!!.get(position)!!
                .weight) != null
        ) getOrdersList!!.get(position)!!.weight else "N/A")
        sellingPrice.text = "" + (if ((getOrdersList!!.get(position)!!
                .sellPrice) != null
        ) getOrdersList!!.get(position)!!.sellPrice else "N/A")
        quality_grade.text = "" + (if ((getOrdersList!!.get(position)!!
                .qualityGrade) != null
        ) getOrdersList!!.get(position)!!.qualityGrade else "N/A")
        gst.text = "" + (if ((getOrdersList!!.get(position)!!
                .gstType) != null
        ) "With " + getOrdersList!!.get(position)!!.gstType else " Without GST ")
        coustomer_gsst_no.text = "" + (if ((getOrdersList!!.get(position)!!
                .gstNo) != null
        ) getOrdersList!!.get(position)!!.gstNo else "N/A")
        finalPprice.text = "" + (if ((getOrdersList!!.get(position)!!
                .totalAmount) != null
        ) getOrdersList!!.get(position)!!.totalAmount else "N/A")
        paymentstatus.text = "" + (if ((getOrdersList!!.get(position)!!
                .paymentMode) != null
        ) getOrdersList!!.get(position)!!.paymentMode else "N/A")
        cancel_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                alertDialog.dismiss()
            }
        })
        alertDialog.show()
    }

    fun cancelIntantion(position: Int) {
        Utility.showDecisionDialog(
            this@IntantionApprovalListClass,
            getString(R.string.alert),
            resources.getString(R.string.alert_reject_int),
            object : Utility.AlertCallback {
                override fun callback() {
                    inventory_id = "" + getOrdersList!![position]!!.id
                    val builder = AlertDialog.Builder(this@IntantionApprovalListClass)
                    val inflater = (this@IntantionApprovalListClass as Activity).layoutInflater
                    val dialogView = inflater.inflate(R.layout.close_pricicng_diloag, null)
                    val title = dialogView.findViewById<View>(R.id.name) as TextView
                    val title_header = dialogView.findViewById<View>(R.id.title_header) as TextView
                    val notes = dialogView.findViewById<View>(R.id.notes) as EditText
                    val submit = dialogView.findViewById<View>(R.id.btn_submit) as Button
                    val cancel_btn = dialogView.findViewById<View>(R.id.cancel_btn) as ImageView
                    builder.setView(dialogView)
                    builder.setCancelable(false)
                    val alertDialog = builder.create()
                    alertDialog.show()
                    title.visibility = View.GONE
                    title_header.text = "Reject Intention"
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
                                apiService.rejeectIntantion(
                                    ApprovedIntantionPOst(
                                        inventory_id,
                                        notes.text.toString().trim { it <= ' ' })
                                )!!
                                    .enqueue(object : NetworkCallback<LoginResponse?>(
                                        activity
                                    ) {
                                         override fun onSuccess(body: LoginResponse?) {
                                            Toast.makeText(
                                                this@IntantionApprovalListClass,
                                                body!!.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                            alertDialog.dismiss()
                                            getConvencyList("")
                                        }
                                    })
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Please Fill notes",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    })
                }
            })
    }

    fun ApproveIntantion(position: Int) {
        Utility.showDecisionDialog(
            this@IntantionApprovalListClass,
            getString(R.string.alert),
            resources.getString(R.string.alert_appprove_int),
            object : Utility.AlertCallback {
                override fun callback() {
                    inventory_id = "" + getOrdersList!![position]!!.id
                    val builder = AlertDialog.Builder(this@IntantionApprovalListClass)
                    val inflater = (this@IntantionApprovalListClass as Activity).layoutInflater
                    val dialogView = inflater.inflate(R.layout.close_pricicng_diloag, null)
                    val title = dialogView.findViewById<View>(R.id.name) as TextView
                    val title_header = dialogView.findViewById<View>(R.id.title_header) as TextView
                    val notes = dialogView.findViewById<View>(R.id.notes) as EditText
                    val submit = dialogView.findViewById<View>(R.id.btn_submit) as Button
                    val cancel_btn = dialogView.findViewById<View>(R.id.cancel_btn) as ImageView
                    builder.setView(dialogView)
                    builder.setCancelable(false)
                    val alertDialog = builder.create()
                    alertDialog.show()
                    title.visibility = View.GONE
                    title_header.text = "Approve Intention"
                    cancel_btn.setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View) {
                            alertDialog.dismiss()
                        }
                    })
                    submit.setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View) {
                            apiService.ApproveIntantion(
                                ApprovedIntantionPOst(
                                    inventory_id,
                                    notes.text.toString().trim { it <= ' ' })
                            )!!
                                .enqueue(object : NetworkCallback<LoginResponse?>(
                                    activity
                                ) {
                                     override fun onSuccess(body: LoginResponse?) {
                                        Toast.makeText(
                                            this@IntantionApprovalListClass,
                                            body!!.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                        alertDialog.dismiss()
                                        getConvencyList("")
                                    }
                                })
                            /*if (notes.getText().toString().trim() != null && !notes.getText().toString().trim().isEmpty()) {
                            ApproveRejectIntantion(alertDialog, inventory_id, notes.getText().toString().trim());
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Fill notes", Toast.LENGTH_LONG).show();
                        }*/
                        }
                    })
                }
            })
    }

    private fun ApproveRejectIntantion(
        alertDialog: AlertDialog,
        inventory_id: String,
        trim: String
    ) {
//        apiService.empyConveyanceDelate(new SelfRejectConveyancePOst(inventory_id))
//
//                .enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
//            @Override
//            protected void onSuccess(LoginResponse body) {
//                Toast.makeText(IntantionApprovalListClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
//                alertDialog.dismiss();
//                getConvencyList("");
//            }
//        });
    }
}
