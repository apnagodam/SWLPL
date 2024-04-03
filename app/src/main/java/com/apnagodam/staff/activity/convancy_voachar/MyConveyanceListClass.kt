package com.apnagodam.staff.activity.convancy_voachar

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.apnagodam.staff.Network.Request.SelfRejectConveyancePOst
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.viewmodel.ConveyanceViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.adapter.ConvancyListAdpter
import com.apnagodam.staff.databinding.ConvencyListBinding
import com.apnagodam.staff.module.AllConvancyList
import com.apnagodam.staff.module.AllLevelEmpListPojo
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.PhotoFullPopupWindow
import com.apnagodam.staff.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class MyConveyanceListClass() : BaseActivity<ConvencyListBinding?>() {
    private var convancyListAdpter: ConvancyListAdpter? = null
    private var pageOffset = 1
    private var totalPage = 0
    private var getOrdersList: MutableList<AllConvancyList.Datum?>? = null
    private var tempraryList: MutableList<AllConvancyList.Datum?>? = null
    private var inventory_id = ""
    private var startMeterImage: String? = null
    private var endMeterImage: String? = null
    private var otherImaage: String? = null

    // for FB button
    private var fabOpenAnimation: Animation? = null
    private var fabCloseAnimation: Animation? = null
    private var isFabMenuOpen = false
    val conveyanceViewModel by viewModels<ConveyanceViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.convency_list
    }

    override fun setUp() {
        getOrdersList = arrayListOf()
        tempraryList = arrayListOf()
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.ivClose.setOnClickListener(View.OnClickListener {
           finish()
        })
        binding!!.fieldStockList.addItemDecoration(
            DividerItemDecoration(
                this@MyConveyanceListClass,
                LinearLayoutManager.HORIZONTAL
            )
        )
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
                val builder = AlertDialog.Builder(this@MyConveyanceListClass)
                val inflater = (this@MyConveyanceListClass as Activity).layoutInflater
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
            startActivity(UploadConveyanceVoacharClass::class.java)
        }

        fun onShareFabClick(view: View?) {
            startActivityAndClear(ApprovalRequestConveyanceListClass::class.java)
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

    private fun getapproveCount() {
        showDialog()
        conveyanceViewModel.getlevelwiselist()
        conveyanceViewModel.conveyanceListResponse.observe(this){
            when(it){
                is NetworkResult.Error ->{
                    hideDialog()
                    showToast(it.message)
                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success ->{
                    hideDialog()
                   if(it.data!=null){
                       it.data.let {body->
                           if (body.request_count > 0) {
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
                       }
                   }
                }
            }
        }


    }

    private fun getConvencyList(SerachKey: String) {
        showDialog()
        conveyanceViewModel.getConveyanceList("15", pageOffset, SerachKey)
        conveyanceViewModel.conveyanceResponse.observe(this){
            when(it){
                is NetworkResult.Error -> {
                    hideDialog()
                    showToast(it.message)
                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    hideDialog()
                    getapproveCount()
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
                            convancyListAdpter!!.notifyDataSetChanged()
                        } else {
                            binding!!.txtemptyMsg.visibility = View.GONE
                            binding!!.fieldStockList.visibility = View.VISIBLE
                            binding!!.pageNextPrivious.visibility = View.VISIBLE
                            getOrdersList!!.clear()
                            tempraryList!!.clear()
                            totalPage = it.data.data.lastPage
                            getOrdersList!!.addAll(it.data.data.dataa)
                            tempraryList!!.addAll((getOrdersList)!!)
                            convancyListAdpter!!.notifyDataSetChanged()
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
            LinearLayoutManager(this@MyConveyanceListClass, LinearLayoutManager.VERTICAL, false)
        binding!!.fieldStockList.layoutManager = horizontalLayoutManager
        convancyListAdpter = ConvancyListAdpter(getOrdersList, this@MyConveyanceListClass, activity)
        binding!!.fieldStockList.adapter = convancyListAdpter
    }

    override fun onBackPressed() {
        if (isFabMenuOpen) collapseFabMenu() else {
            super.onBackPressed()
            startActivity(StaffDashBoardActivity::class.java)
        }
    }

    fun ViewData(position: Int) {
        val displayRectangle = Rect()
        val window = this.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val builder = AlertDialog.Builder(this@MyConveyanceListClass, R.style.CustomAlertDialog)
        val inflater = this@MyConveyanceListClass.layoutInflater
        val dialogView = inflater.inflate(R.layout.con_view, null)
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
        val purpose_name = dialogView.findViewById<TextView>(R.id.purpose_name)
        val commitemate_date = dialogView.findViewById<TextView>(R.id.commitemate_date)
        val create_date = dialogView.findViewById<TextView>(R.id.create_date)
        val converted_by = dialogView.findViewById<TextView>(R.id.converted_by)
        val vehicle_no = dialogView.findViewById<TextView>(R.id.vehicle_no)
        val in_out = dialogView.findViewById<TextView>(R.id.in_out)
        val gate_passs = dialogView.findViewById<TextView>(R.id.gate_passs)
        val total_trans_cost = dialogView.findViewById<TextView>(R.id.total_trans_cost)
        val advance_patyment = dialogView.findViewById<TextView>(R.id.advance_patyment)
        val start_date_time = dialogView.findViewById<TextView>(R.id.start_date_time)
        val end_date_time = dialogView.findViewById<TextView>(R.id.end_date_time)
        val settleememt_amount = dialogView.findViewById<TextView>(R.id.settleememt_amount)
        val reports_file = dialogView.findViewById<ImageView>(R.id.reports_file)
        val commodity_file = dialogView.findViewById<ImageView>(R.id.commodity_file)
        val viewOther = dialogView.findViewById<View>(R.id.viewOther)
        val LlOther = dialogView.findViewById<LinearLayout>(R.id.llOther)
        val OtherFile = dialogView.findViewById<ImageView>(R.id.other_file)
        val llconytype = dialogView.findViewById<LinearLayout>(R.id.llconytype)
        val conveyance_type = dialogView.findViewById<TextView>(R.id.conveyance_type)
        val view_conv_type = dialogView.findViewById<View>(R.id.view_conv_type)
        viewOther.visibility = View.VISIBLE
        LlOther.visibility = View.VISIBLE
        llconytype.visibility = View.VISIBLE
        view_conv_type.visibility = View.VISIBLE
        startMeterImage = Constants.conveyance + getOrdersList!![position]!!
            .image
        endMeterImage = Constants.conveyance + getOrdersList!![position]!!
            .image2
        otherImaage = Constants.conveyance + getOrdersList!![position]!!
            .other_charge_img
        reports_file.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                PhotoFullPopupWindow(
                    this@MyConveyanceListClass,
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
                    this@MyConveyanceListClass,
                    R.layout.popup_photo_full,
                    view,
                    endMeterImage,
                    null
                )
            }
        })
        OtherFile.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                PhotoFullPopupWindow(
                    this@MyConveyanceListClass,
                    R.layout.popup_photo_full,
                    view,
                    otherImaage,
                    null
                )
            }
        })
        conveyance_type.text = "" + (if ((getOrdersList!!.get(position)!!
                .conveyance_type) != null
        ) getOrdersList!!.get(position)!!.conveyance_type else "N/A")
        inventory_id = "" + getOrdersList!![position]!!.id
        lead_id.text = "CV - " + (if ((getOrdersList!!.get(position)!!
                .uniqueId) != null
        ) getOrdersList!!.get(position)!!.uniqueId else "N/A")
        genrated_by.text = "" + (if ((getOrdersList!!.get(position)!!
                .date) != null
        ) getOrdersList!!.get(position)!!.date else "N/A")
        customer_name.text = "" + (if ((getOrdersList!!.get(position)!!
                .vehicleNo) != null
        ) getOrdersList!!.get(position)!!.vehicleNo else "N/A")
        commodity_name.text = "" + (if ((getOrdersList!!.get(position)!!
                .purpose) != null
        ) getOrdersList!!.get(position)!!.purpose else "N/A")
        phone_no.text = "" + (if ((getOrdersList!!.get(position)!!
                .fromPlace) != null
        ) getOrdersList!!.get(position)!!.fromPlace else "N/A")
        location_title.text = "" + (if ((getOrdersList!!.get(position)!!
                .toPlace) != null
        ) getOrdersList!!.get(position)!!.toPlace else "N/A")
        est_quantity_nmae.text = "" + (if ((getOrdersList!!.get(position)!!
                .location) != null
        ) (getOrdersList!!.get(position)!!.location) else "N/A")
        terminal_name.text = " " + (if ((getOrdersList!!.get(position)!!
                .startReading) != null
        ) getOrdersList!!.get(position)!!.startReading else "N/A")
        purpose_name.text = "" + (if ((getOrdersList!!.get(position)!!
                .endReading) != null
        ) getOrdersList!!.get(position)!!.endReading else "N/A")
        commitemate_date.text = "" + (if ((getOrdersList!!.get(position)!!
                .kms) != null
        ) getOrdersList!!.get(position)!!.kms else "N/A")
        create_date.text = "" + (if ((getOrdersList!!.get(position)!!
                .charges) != null
        ) getOrdersList!!.get(position)!!.charges else "N/A")
        converted_by.text = "" + (if ((getOrdersList!!.get(position)!!
                .otherExpense) != null
        ) getOrdersList!!.get(position)!!.otherExpense else "N/A")
        vehicle_no.text = "" + (if ((getOrdersList!!.get(position)!!
                .total) != null
        ) getOrdersList!!.get(position)!!.total else "N/A")
        in_out.text = "" + (if ((getOrdersList!!.get(position)!!
                .finalPrize) != null
        ) (getOrdersList!!.get(position)!!.finalPrize) else "N/A")
        gate_passs.text = "" + (if ((getOrdersList!!.get(position)!!
                .notes) != null
        ) getOrdersList!!.get(position)!!.notes else "N/A")
        total_trans_cost.text = "" + (if ((getOrdersList!!.get(position)!!
                .firstName + " " + getOrdersList!!.get(position)!!
                .lastName + "(" + getOrdersList!!.get(position)!!
                .empId + ")") != null
        ) ((getOrdersList!!.get(position)!!
            .firstName + " " + getOrdersList!!.get(position)!!
            .lastName + "(" + getOrdersList!!.get(position)!!.empId + ")")) else "N/A")
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
        if (getOrdersList!![position]!!.payment.equals("1", ignoreCase = true)) {
            // show button self rejected
            end_date_time.text = resources.getString(R.string.pending)
            end_date_time.setTextColor(resources.getColor(R.color.darkYellow))
        } else if (getOrdersList!![position]!!.payment.equals("0", ignoreCase = true)) {
            // rejected by self or approved from high authority
            end_date_time.text = resources.getString(R.string.rejected)
            end_date_time.setTextColor(resources.getColor(R.color.red))
        } else if (getOrdersList!![position]!!.payment.equals("2", ignoreCase = true)) {
            //approved from high authority
            end_date_time.text = resources.getString(R.string.done)
            end_date_time.setTextColor(resources.getColor(R.color.colorGreen))
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////
        if (getOrdersList!![position]!!.verify.equals("1", ignoreCase = true)) {
            // show button self rejected
            settleememt_amount.text = resources.getString(R.string.pending)
            settleememt_amount.setTextColor(resources.getColor(R.color.darkYellow))
        } else if (getOrdersList!![position]!!.verify.equals("0", ignoreCase = true)) {
            // rejected by self or approved from high authority
            settleememt_amount.text = resources.getString(R.string.rejected)
            settleememt_amount.setTextColor(resources.getColor(R.color.red))
        } else if (getOrdersList!![position]!!.verify.equals("2", ignoreCase = true)) {
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
            this@MyConveyanceListClass,
            getString(R.string.alert),
            resources.getString(R.string.alert_conv_reject),
            object : Utility.AlertCallback {
                override fun callback() {
                    inventory_id = "" + getOrdersList!![position]!!.id
                    apiService.empyConveyanceDelate(SelfRejectConveyancePOst(inventory_id))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {body->
                            Toast.makeText(
                                this@MyConveyanceListClass,
                                body.message,
                                Toast.LENGTH_LONG
                            ).show()
                            getConvencyList("")
                        }

                }
            })
    }
}
