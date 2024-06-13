package com.apnagodam.staff.activity

import android.app.Activity
import android.content.Intent
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
import com.apnagodam.staff.Network.viewmodel.SpotDealViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.adapter.SpotSellDealTrackListAdpter
import com.apnagodam.staff.databinding.MyCommudityListBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.SpotSellDealTrackPojo
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable

@AndroidEntryPoint
class SpotDealTrackListActivity() : BaseActivity<MyCommudityListBinding?>() {
    private var myCommudityListAdpter: SpotSellDealTrackListAdpter? = null
    private var pageOffset = 1
    private var totalPage = 0
    private var getOrdersList = arrayListOf<SpotSellDealTrackPojo.Datum>()
    private var tempraryList = arrayListOf<SpotSellDealTrackPojo.Datum>()
    private var inventory_id = ""
    private val ids = ""
    var addInventoryResponse: List<SpotSellDealTrackPojo.Datum>? = null

    val spotDealViewModel by viewModels<SpotDealViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.my_commudity_list
    }

    override fun setUp() {
        binding!!.titleHeader.text = resources.getString(R.string.spot_sell)
        binding!!.tvPhone.text = resources.getString(R.string.view)
        binding!!.tvName.text = resources.getString(R.string.net_Weight)
        binding!!.tvId.text = resources.getString(R.string.commodity)
        binding!!.pageNextPrivious.visibility = View.VISIBLE
        getOrdersList = arrayListOf()
        tempraryList = arrayListOf()
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.ivClose.setOnClickListener(View.OnClickListener {
            finish()
        })
        setDataAdapter()
        getBidsList("")
        binding!!.tvPrevious.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (pageOffset != 1) {
                    pageOffset--
                    getBidsList("")
                }
            }
        })
        binding!!.tvNext.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (totalPage != pageOffset) {
                    pageOffset++
                    getBidsList("")
                }
            }
        })
        binding!!.swipeRefresherStock.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh() {
                getBidsList("")
            }
        })
        binding!!.filterIcon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val builder = AlertDialog.Builder(this@SpotDealTrackListActivity)
                val inflater = (this@SpotDealTrackListActivity as Activity).layoutInflater
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
                            getBidsList(notes.text.toString().trim { it <= ' ' })
                            //     ClosedPricing(alertDialog, AllCases.get(postion).getCaseId(), notes.getText().toString().trim());
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Please Fill Text",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })
                //  setDateTimeField();
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getBidsList("")

    }

    private fun getBidsList(SerachKey: String) {
        showDialog()
        spotDealViewModel.getSpotDeals("15",pageOffset.toString(),SerachKey)

        spotDealViewModel.spotDealSellsResponse.observe(this@SpotDealTrackListActivity){
            body->
            when(body){
                is NetworkResult.Error -> {
                    hideDialog()
                    Toast.makeText(this,body.message,Toast.LENGTH_SHORT).show();

                }
                is NetworkResult.Loading ->{

                }
                is NetworkResult.Success -> {
                    tempraryList!!.clear()
                    getOrdersList!!.clear()
                    if(body.data!=null){
                        addInventoryResponse = body.data.deals.data
                        binding!!.swipeRefresherStock.isRefreshing = false
                        if (body.data.deals.data.size == 0) {
                            binding!!.txtemptyMsg.visibility = View.VISIBLE
                            binding!!.fieldStockList.visibility = View.GONE
                            binding!!.pageNextPrivious.visibility = View.GONE
                        } else if (body.data.deals.lastPage == 1) {
                            binding!!.txtemptyMsg.visibility = View.GONE
                            binding!!.fieldStockList.visibility = View.VISIBLE
                            binding!!.pageNextPrivious.visibility = View.GONE
                            getOrdersList!!.clear()
                            tempraryList!!.clear()
                            totalPage = body.data.deals.lastPage
                            getOrdersList!!.addAll(body.data.deals.data)
                            tempraryList!!.addAll((getOrdersList)!!)
                            myCommudityListAdpter!!.notifyDataSetChanged()
                        } else {
                            binding!!.txtemptyMsg.visibility = View.GONE
                            binding!!.fieldStockList.visibility = View.VISIBLE
                            binding!!.pageNextPrivious.visibility = View.VISIBLE
                            getOrdersList!!.clear()
                            tempraryList!!.clear()
                            totalPage = body.data.deals.lastPage
                            getOrdersList!!.addAll(body.data.deals.data)
                            tempraryList!!.addAll((getOrdersList)!!)
                            myCommudityListAdpter!!.notifyDataSetChanged()
                        }
                    }
                    hideDialog()
                }
            }
        }

    }

    private fun setDataAdapter() {
        binding!!.fieldStockList.addItemDecoration(
            DividerItemDecoration(
                this@SpotDealTrackListActivity,
                LinearLayoutManager.HORIZONTAL
            )
        )
        binding!!.fieldStockList.setHasFixedSize(true)
        binding!!.fieldStockList.isNestedScrollingEnabled = false
        val horizontalLayoutManager =
            LinearLayoutManager(this@SpotDealTrackListActivity, LinearLayoutManager.VERTICAL, false)
        binding!!.fieldStockList.layoutManager = horizontalLayoutManager
        myCommudityListAdpter = SpotSellDealTrackListAdpter(
            ids,
            getOrdersList,
            this@SpotDealTrackListActivity,
            activity
        )
        binding!!.fieldStockList.adapter = myCommudityListAdpter
    }



    fun ViewData(position: Int) {
        val displayRectangle = Rect()
        val window = this.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val builder = AlertDialog.Builder(this@SpotDealTrackListActivity, R.style.CustomAlertDialog)
        val inflater = this@SpotDealTrackListActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.lead_view_new, null)
        dialogView.minimumWidth = (displayRectangle.width() * 1f).toInt()
        dialogView.minimumHeight = (displayRectangle.height() * 1f).toInt()
        builder.setView(dialogView)
        val alertDialog = builder.create()
        val cancel_btn = dialogView.findViewById<ImageView>(R.id.cancel_btn)
        val gate_pass_no = dialogView.findViewById<TextView>(R.id.gate_pass_no)
        val terminal_name = dialogView.findViewById<TextView>(R.id.terminal_name)
        val location_name = dialogView.findViewById<TextView>(R.id.location_name)
        val commodity_name = dialogView.findViewById<TextView>(R.id.commodity_name)
        val wieght = dialogView.findViewById<TextView>(R.id.wieght)
        val quality_name = dialogView.findViewById<TextView>(R.id.quality_name)
        val creadte_date_name = dialogView.findViewById<TextView>(R.id.creadte_date_name)
        val a1 = dialogView.findViewById<TextView>(R.id.a1)
        val a2 = dialogView.findViewById<TextView>(R.id.a2)
        val a6 = dialogView.findViewById<TextView>(R.id.a6)
        a2.text = resources.getString(R.string.buyer_seller)
        a1.text = resources.getString(R.string.type)
        a6.text = resources.getString(R.string.total_weight_qtl)
        inventory_id = "" + getOrdersList!![position]!!.id
        if (SharedPreferencesRepository.getDataManagerInstance().user.userId.equals(
                getOrdersList!![position]!!.sellerId, ignoreCase = true
            )
        ) {
            gate_pass_no.text = "Seller"
            terminal_name.text = "" + (if ((getOrdersList!!.get(position)!!
                    .fname) != null
            ) getOrdersList!!.get(position)!!.fname else "N/A")
        } else {
            gate_pass_no.text = "Buyer"
            terminal_name.text = "" + (if ((getOrdersList!!.get(position)!!
                    .sellerName) != null
            ) getOrdersList!!.get(position)!!.sellerName else "N/A")
        }
        location_name.text = "" + (if ((getOrdersList!!.get(position)!!
                .location) != null
        ) getOrdersList!!.get(position)!!.location else "N/A")
        commodity_name.text = "" + (if ((getOrdersList!!.get(position)!!
                .category) != null
        ) getOrdersList!!.get(position)!!.category else "N/A")
        wieght.text = "" + (if ((getOrdersList!!.get(position)!!
                .quantity) != null
        ) getOrdersList!!.get(position)!!.quantity else "N/A")
        quality_name.text = "" + (if ((getOrdersList!!.get(position)!!
                .price) != null
        ) getOrdersList!!.get(position)!!.price else "N/A")
        creadte_date_name.text = "" + (if ((getOrdersList!!.get(position)!!
                .updatedAt) != null
        ) (getOrdersList!!.get(position)!!.updatedAt) else "N/A")
        cancel_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                alertDialog.dismiss()
            }
        })
        alertDialog.show()
    }

    fun wantToSell(position: Int) {
        inventory_id = "" + getOrdersList!![position]!!.id
        val intent = Intent(this@SpotDealTrackListActivity, ContractFormBillPrintClass::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("serialzable", addInventoryResponse!![position] as Serializable)
        startActivity(intent)
    }
}