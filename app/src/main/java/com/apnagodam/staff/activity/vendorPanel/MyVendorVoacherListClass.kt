package com.apnagodam.staff.activity.vendorPanel

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.SelfRejectVendorConveyancePOst
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.viewmodel.ConveyanceViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.adapter.VendorVoacherListAdpter
import com.apnagodam.staff.databinding.VendorConvencyListBinding
import com.apnagodam.staff.module.AllVendorConvancyList
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyVendorVoacherListClass() : BaseActivity<VendorConvencyListBinding?>() {
    private var convancyListAdpter: VendorVoacherListAdpter? = null
    private var pageOffset = 1
    private var totalPage = 0
    private var getOrdersList = arrayListOf<AllVendorConvancyList.Datum>()
    private var tempraryList= arrayListOf<AllVendorConvancyList.Datum>()
    private var inventory_id = ""
    private var startMeterImage: String? = null
    private var endMeterImage: String? = null

    // for FB button
    private var fabOpenAnimation: Animation? = null
    private var fabCloseAnimation: Animation? = null
    private var isFabMenuOpen = false

    val conveyanceViewModel by viewModels<ConveyanceViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.vendor_convency_list
    }

    override fun setUp() {
        binding!!.tvId.text = "Unique ID"
        getOrdersList = arrayListOf()
        tempraryList = arrayListOf()
        setSupportActionBar(binding!!.toolbar)
        binding!!.titleHeader.text = "Vendor Conveyance List"
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.ivClose.setOnClickListener(View.OnClickListener {
            finish()
        })
        binding!!.fieldStockList.addItemDecoration(
            DividerItemDecoration(
                this@MyVendorVoacherListClass,
                LinearLayoutManager.HORIZONTAL
            )
        )
        setDataAdapter()
        getConvencyList("")
        /*  binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(UploadConveyanceVoacharClass.class);
            }
        });*/binding!!.tvPrevious.setOnClickListener(object : View.OnClickListener {
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
                val builder = AlertDialog.Builder(this@MyVendorVoacherListClass)
                val inflater = (this@MyVendorVoacherListClass as Activity).layoutInflater
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
        binding!!.fabHandler = FabHandler()
        animations
    }

    inner class FabHandler() {
        fun onBaseFabClick(view: View?) {
            if (isFabMenuOpen) collapseFabMenu() else expandFabMenu()
        }

        fun onCreateFabClick(view: View?) {
            startActivity(UploadVendorVoacherClass::class.java)
        }

        fun onShareFabClick(view: View?) {
            startActivityAndClear(ApprovalRequestVendorVoacherListClass::class.java)
        }
    }

    private fun expandFabMenu() {
        ViewCompat.animate(binding!!.fab).rotation(45.0f).withLayer().setDuration(300)
            .setInterpolator(OvershootInterpolator(10.0f)).start()
        binding!!.createLayout.startAnimation(fabOpenAnimation)
        binding!!.shareLayout.startAnimation(fabOpenAnimation)
        binding!!.createFab.isClickable = true
        binding!!.shareFab.isClickable = true
        isFabMenuOpen = true
    }

    private fun collapseFabMenu() {
        ViewCompat.animate(binding!!.fab).rotation(0.0f).withLayer().setDuration(300)
            .setInterpolator(OvershootInterpolator(10.0f)).start()
        binding!!.createLayout.startAnimation(fabCloseAnimation)
        binding!!.shareLayout.startAnimation(fabCloseAnimation)
        binding!!.createFab.isClickable = false
        binding!!.shareFab.isClickable = false
        isFabMenuOpen = false
    }

    private val animations: Unit
        private get() {
            fabOpenAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_open)
            fabCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        }

    override fun onResume() {
        super.onResume()
        getConvencyList("")
    }
    private fun getConvencyList(SerachKey: String) {
        showDialog()
        conveyanceViewModel.getVendorConvancyList("15", pageOffset, SerachKey)
        conveyanceViewModel.vendorConveyanceListResponse.observe(this){

            when(it){
                is NetworkResult.Error -> {
                    hideDialog()
                }
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    hideDialog()
                    if(it.data!=null){
                        it.data.let {
                            body-> if (body.request_count > 0) {
                            binding!!.shareLabelTextView.isClickable = true
                            binding!!.shareLabelTextView.isEnabled = true
                            binding!!.shareFab.isClickable = true
                            binding!!.shareFab.isEnabled = true
                            binding!!.shareLabelTextView.text =
                                "Approval Request " + "(" + body.request_count + ")"
                        } else {
                            binding!!.shareLabelTextView.isClickable = false
                            binding!!.shareFab.isClickable = false
                            binding!!.shareLabelTextView.isEnabled = false
                            binding!!.shareFab.isEnabled = false
                        }
                            tempraryList!!.clear()
                            getOrdersList!!.clear()
                            binding!!.swipeRefresherStock.isRefreshing = false
                            if (body.data.dataa.size == 0) {
                                binding!!.txtemptyMsg.visibility = View.VISIBLE
                                binding!!.fieldStockList.visibility = View.GONE
                                binding!!.pageNextPrivious.visibility = View.GONE
                            } else if (body.data.lastPage == 1) {
                                binding!!.txtemptyMsg.visibility = View.GONE
                                binding!!.fieldStockList.visibility = View.VISIBLE
                                binding!!.pageNextPrivious.visibility = View.GONE
                                getOrdersList!!.clear()
                                tempraryList!!.clear()
                                totalPage = body.data.lastPage
                                getOrdersList!!.addAll(body.data.dataa)
                                tempraryList!!.addAll((getOrdersList)!!)
                                convancyListAdpter!!.notifyDataSetChanged()
                            } else {
                                binding!!.txtemptyMsg.visibility = View.GONE
                                binding!!.fieldStockList.visibility = View.VISIBLE
                                binding!!.pageNextPrivious.visibility = View.VISIBLE
                                getOrdersList!!.clear()
                                tempraryList!!.clear()
                                totalPage = body.data.lastPage
                                getOrdersList!!.addAll(body.data.dataa)
                                tempraryList!!.addAll((getOrdersList)!!)
                                convancyListAdpter!!.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }

    }

    private fun setDataAdapter() {

        binding!!.fieldStockList.setHasFixedSize(true)
        binding!!.fieldStockList.isNestedScrollingEnabled = false
        val horizontalLayoutManager =
            LinearLayoutManager(this@MyVendorVoacherListClass, LinearLayoutManager.VERTICAL, false)
        binding!!.fieldStockList.layoutManager = horizontalLayoutManager
        convancyListAdpter =
            VendorVoacherListAdpter(getOrdersList, this@MyVendorVoacherListClass, activity)
        binding!!.fieldStockList.adapter = convancyListAdpter
    }

    override fun onBackPressed() {
        if (isFabMenuOpen) collapseFabMenu() else {
           onBackPressedDispatcher.onBackPressed()
        }
        super.onBackPressed()
    }

    fun ViewData(position: Int) {
        val displayRectangle = Rect()
        val window = this.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val builder = AlertDialog.Builder(this@MyVendorVoacherListClass, R.style.CustomAlertDialog)
        val inflater = this@MyVendorVoacherListClass.layoutInflater
        val dialogView = inflater.inflate(R.layout.vendor_con_view, null)
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
        val purpose_name = dialogView.findViewById<TextView>(R.id.voacher_purpose)
        val gate_passs = dialogView.findViewById<TextView>(R.id.gate_passs)
        val total_trans_cost = dialogView.findViewById<TextView>(R.id.total_trans_cost)
        val advance_patyment = dialogView.findViewById<TextView>(R.id.advance_patyment)
        val start_date_time = dialogView.findViewById<TextView>(R.id.start_date_time)
        val end_date_time = dialogView.findViewById<TextView>(R.id.end_date_time)
        val settleememt_amount = dialogView.findViewById<TextView>(R.id.settleememt_amount)
        val reports_file = dialogView.findViewById<ImageView>(R.id.reports_file)
        val commodity_file = dialogView.findViewById<ImageView>(R.id.commodity_file)
        startMeterImage = Constants.vendors_voucher + getOrdersList!![position]!!
            .expImage1
        endMeterImage = Constants.vendors_voucher + getOrdersList!![position]!!
            .expImage2
        reports_file.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                PhotoFullPopupWindow(
                    this@MyVendorVoacherListClass,
                    R.layout.popup_photo_full,
                    view,
                    startMeterImage,
                    null
                )
            }
        })
        commodity_file.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                PhotoFullPopupWindow(
                    this@MyVendorVoacherListClass,
                    R.layout.popup_photo_full,
                    view,
                    endMeterImage,
                    null
                )
            }
        })
        inventory_id = "" + getOrdersList!![position]!!.id
        lead_id.text = "" + (if ((getOrdersList!!.get(position)!!
                .uniqueId) != null
        ) getOrdersList!!.get(position)!!.uniqueId else "N/A")
        genrated_by.text = "" + (if ((getOrdersList!!.get(position)!!
                .date) != null
        ) getOrdersList!!.get(position)!!.date else "N/A")
        customer_name.text = "" + (if ((getOrdersList!!.get(position)!!
                .firstName) != null
        ) getOrdersList!!.get(position)!!
            .firstName + " " + getOrdersList!!.get(position)!!
            .lastName + "(" + getOrdersList!!.get(position)!!.empId + ")" else "N/A")
        commodity_name.text = "" + (if ((getOrdersList!!.get(position)!!
                .expensesName) != null
        ) getOrdersList!!.get(position)!!.expensesName else "N/A")
        phone_no.text = "" + (if ((getOrdersList!!.get(position)!!
                .vendorFirstName) != null
        ) getOrdersList!!.get(position)!!
            .vendorFirstName + " " + getOrdersList!!.get(position)!!
            .vendorLastName + "(" + getOrdersList!!.get(position)!!.vendorUniqueId + ")" else "N/A")
        location_title.text = "" + (if ((getOrdersList!!.get(position)!!
                .warehouseName) != null
        ) getOrdersList!!.get(position)!!
            .warehouseName + "(" + getOrdersList!!.get(position)!!.warehouseCode + ")" else "N/A")
        est_quantity_nmae.text = "" + (if ((getOrdersList!!.get(position)!!
                .amount) != null
        ) (getOrdersList!!.get(position)!!.amount) else "N/A")
        terminal_name.text = " " + (if ((getOrdersList!!.get(position)!!
                .finalPrice) != null
        ) getOrdersList!!.get(position)!!.finalPrice else "N/A")
        purpose_name.text = "" + (if ((getOrdersList!!.get(position)!!
                .purpose) != null
        ) getOrdersList!!.get(position)!!.purpose else "N/A")
        gate_passs.text = "" + (if ((getOrdersList!!.get(position)!!
                .notes) != null
        ) getOrdersList!!.get(position)!!.notes else "N/A")
        total_trans_cost.text = "" + (if ((getOrdersList!!.get(position)!!
                .approvelFname + " " + getOrdersList!!.get(position)!!
                .approvelLname + "(" + getOrdersList!!.get(position)!!
                .approvelEmpID + ")") != null
        ) ((getOrdersList!!.get(position)!!
            .approvelFname + " " + getOrdersList!!.get(position)!!
            .approvelLname + "(" + getOrdersList!!.get(position)!!.approvelEmpID + ")")) else "N/A")
        if (getOrdersList!![position]!!.status.equals("1", ignoreCase = true)) {
            // show button self rejected
            advance_patyment.text = resources.getString(R.string.reject)
            advance_patyment.setTextColor(resources.getColor(R.color.darkYellow))
        } else if (getOrdersList!![position]!!.status.equals("0", ignoreCase = true)) {
            // rejected by self or approved from high authority
            advance_patyment.text = resources.getString(R.string.rejected)
            advance_patyment.setTextColor(resources.getColor(R.color.red))
        } else if (getOrdersList!![position]!!.status.equals("2", ignoreCase = true)) {
            //approved from high authority
            advance_patyment.text = resources.getString(R.string.approved)
            advance_patyment.setTextColor(resources.getColor(R.color.colorGreen))
        }
        ///////////////////////////////////////////////////////
        if (getOrdersList!![position]!!.paymentStatus.equals("1", ignoreCase = true)) {
            // show button self rejected
            end_date_time.text = resources.getString(R.string.pending)
            end_date_time.setTextColor(resources.getColor(R.color.darkYellow))
        } else if (getOrdersList!![position]!!.paymentStatus.equals("0", ignoreCase = true)) {
            // rejected by self or approved from high authority
            end_date_time.text = resources.getString(R.string.rejected)
            end_date_time.setTextColor(resources.getColor(R.color.red))
        } else if (getOrdersList!![position]!!.paymentStatus.equals("2", ignoreCase = true)) {
            //approved from high authority
            end_date_time.text = resources.getString(R.string.done)
            end_date_time.setTextColor(resources.getColor(R.color.colorGreen))
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////
        if (getOrdersList!![position]!!.verfiyStatus.equals("1", ignoreCase = true)) {
            // show button self rejected
            settleememt_amount.text = resources.getString(R.string.pending)
            settleememt_amount.setTextColor(resources.getColor(R.color.darkYellow))
        } else if (getOrdersList!![position]!!.verfiyStatus.equals("0", ignoreCase = true)) {
            // rejected by self or approved from high authority
            settleememt_amount.text = resources.getString(R.string.rejected)
            settleememt_amount.setTextColor(resources.getColor(R.color.red))
        } else if (getOrdersList!![position]!!.verfiyStatus.equals("2", ignoreCase = true)) {
            //approved from high authority
            settleememt_amount.text = resources.getString(R.string.verify_conv)
            settleememt_amount.setTextColor(resources.getColor(R.color.colorGreen))
        }
        start_date_time.text = "" + (if ((getOrdersList!!.get(position)!!
                .updatedAt) != null
        ) getOrdersList!!.get(position)!!.updatedAt else "N/A")
        cancel_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                alertDialog.dismiss()
            }
        })
        alertDialog.show()
    }

    fun cancelConv(position: Int) {
        Utility.showDecisionDialog(
            this@MyVendorVoacherListClass,
            getString(R.string.alert),
            resources.getString(R.string.alert_conv_reject),
            object : Utility.AlertCallback {
                override fun callback() {
                    inventory_id = "" + getOrdersList!![position]!!.id
                    apiService.vendorConveyanceDelate(SelfRejectVendorConveyancePOst(inventory_id))!!
                        .enqueue(object : NetworkCallback<LoginResponse?>(
                            activity
                        ) {
                             override fun onSuccess(body: LoginResponse?) {
                                Toast.makeText(
                                    this@MyVendorVoacherListClass,
                                    body!!.message,
                                    Toast.LENGTH_LONG
                                ).show()
                                getConvencyList("")
                            }
                        })
                }
            })
    }
}
