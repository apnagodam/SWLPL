package com.apnagodam.staff.activity

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Base.BaseViewHolder
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.AttendancePostData
import com.apnagodam.staff.Network.Response.StackRequestResponse
import com.apnagodam.staff.Network.Response.StackRequestResponse.OutwardRequestDatum
import com.apnagodam.staff.Network.viewmodel.CaseIdViewModel
import com.apnagodam.staff.Network.viewmodel.HomeViewModel
import com.apnagodam.staff.Network.viewmodel.LoginViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.audit.AuditNeighbourActivity
import com.apnagodam.staff.activity.audit.AuditPvActivity
import com.apnagodam.staff.activity.audit.AuditQualityStock
import com.apnagodam.staff.activity.audit.CmDetailsActivity
import com.apnagodam.staff.activity.audit.InOutActivity
import com.apnagodam.staff.activity.audit.VideoRecordingActivity
import com.apnagodam.staff.activity.caseid.CaseIDGenerateClass
import com.apnagodam.staff.activity.caseid.CaseListingActivity
import com.apnagodam.staff.activity.casestatus.CaseStatusINListClass
import com.apnagodam.staff.activity.convancy_voachar.MyConveyanceListClass
import com.apnagodam.staff.activity.displedged.DispleasedListingActivity
import com.apnagodam.staff.activity.displedged.DispledgedBags
import com.apnagodam.staff.activity.`in`.first_kantaparchi.FirstkanthaParchiListingActivity
import com.apnagodam.staff.activity.`in`.first_quality_reports.FirstQualityReportListingActivity
import com.apnagodam.staff.activity.`in`.labourbook.LabourBookListingActivity
import com.apnagodam.staff.activity.`in`.pricing.InPricingListingActivity
import com.apnagodam.staff.activity.`in`.secound_kanthaparchi.SecoundkanthaParchiListingActivity
import com.apnagodam.staff.activity.`in`.secound_quality_reports.SecoundQualityReportListingActivity
import com.apnagodam.staff.activity.`in`.truckbook.TruckBookListingActivity
import com.apnagodam.staff.activity.lead.LeadGenerateClass
import com.apnagodam.staff.activity.out.f_katha_parchi.OutFirstkanthaParchiListingActivity
import com.apnagodam.staff.activity.out.f_quailty_report.OutFirstQualityReportListingActivity
import com.apnagodam.staff.activity.out.labourbook.OUTLabourBookListingActivity
import com.apnagodam.staff.activity.out.releaseorder.OUTRelaseOrderListingActivity
import com.apnagodam.staff.activity.out.s_katha_parchi.OutSecoundkanthaParchiListingActivity
import com.apnagodam.staff.activity.out.s_quaility_report.OutSecoundQualityReportListingActivity
import com.apnagodam.staff.activity.out.truckbook.OUTTruckBookListingActivity
import com.apnagodam.staff.activity.vendorPanel.MyVendorVoacherListClass
import com.apnagodam.staff.adapter.CasesTopAdapter
import com.apnagodam.staff.adapter.ExpandableListAdapter
import com.apnagodam.staff.adapter.NavigationAdapter
import com.apnagodam.staff.adapter.OnBindCallback
import com.apnagodam.staff.databinding.LayoutTopCaseGenerateBinding
import com.apnagodam.staff.databinding.StaffDashboardBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.interfaces.OnProfileClickListener
import com.apnagodam.staff.module.AllCaseIDResponse
import com.apnagodam.staff.module.MenuModel
import com.apnagodam.staff.module.UserDetails
import com.apnagodam.staff.paging.adapter.CasesAdapter
import com.apnagodam.staff.paging.state.ListLoadStateAdapter
import com.apnagodam.staff.utils.ConstantObjects
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.ImageHelper
import com.apnagodam.staff.utils.LocationUtils
import com.apnagodam.staff.utils.RecyclerItemClickListener
import com.apnagodam.staff.utils.Utility
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.fxn.pix.Options
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.master.permissionhelper.PermissionHelper
import com.otaliastudios.cameraview.CameraView.PERMISSION_REQUEST_CODE
import com.thorny.photoeasy.OnPictureReady
import com.thorny.photoeasy.PhotoEasy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Locale
import java.util.UUID
import javax.inject.Inject


@AndroidEntryPoint
class StaffDashBoardActivity() : BaseActivity<StaffDashboardBinding?>(), View.OnClickListener,
    OnProfileClickListener, RecyclerItemClickListener.OnItemClickListener,
    AdapterView.OnItemSelectedListener, DrawerListener, ConnectionCallbacks, OnBindCallback,
    OnConnectionFailedListener {
    private var toggle: ActionBarDrawerToggle? = null
    private var casesTopAdapter: CasesTopAdapter? = null
    val audit = "Audit";
    private val toolbar: Toolbar? = null
    var doubleBackToExitPressedOnce = false
    var locationManager: LocationManager? = null
    private var latitude: String? = null
    private var longitude: String? = null
    var OnOfffAttendance = false
    var attendanceINOUTStatus = "2"
    var fileSelfie: File? = null

    var labFile = MutableLiveData<File>()
    var userDetails: UserDetails? = null
    var selfieImage: ImageView? = null
    var options: Options? = null
    var inCasesList = arrayListOf<AllCaseIDResponse.Datum>()
    var outCasesList = arrayListOf<AllCaseIDResponse.Datum>()

    // for location
    private val locationUtils: LocationUtils? = null
    private val REQUEST_CODE = 1000
    var mGoogleApiClient: GoogleApiClient? = null
    var expandableListAdapter: ExpandableListAdapter? = null
    var headerList: MutableList<MenuModel> = ArrayList()
    var childList = HashMap<MenuModel, List<MenuModel>?>()
    var font: Typeface? = null
    val homeViewModel by viewModels<HomeViewModel>()
    val loginViewModel by viewModels<LoginViewModel>()
    var lat = 0.0
    var long = 0.0
    private var AllCases = arrayListOf<AllCaseIDResponse.Datum>()
    val caseIdViewModel: CaseIdViewModel by viewModels<CaseIdViewModel>()
    private var pageOffset = MutableLiveData<Int>();
    private var totalPage = 0
    lateinit var photoEasy: PhotoEasy
    var currentLocation = ""
    var inwardsList = arrayListOf<StackRequestResponse.InwardRequestDatum>()
    var outwardsList = arrayListOf<OutwardRequestDatum>()

    var lastPage = MutableLiveData<Int>();

    @Inject
    lateinit var userAdapter: CasesAdapter
    override fun getLayoutResId(): Int {
        return R.layout.staff_dashboard
    }

    override fun setUp() {
        //  userAdapter = CasesAdapter(this, apiService)

        pageOffset.value = 1;
        lastPage.value = 0;
        var permissionHelper = PermissionHelper(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), 100
        )

        if (!permissionHelper.hasPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION,
            )
            permissionHelper.requestAll {}

        }



        setUI()
        setObservers()

        binding!!.swipeRefresherHome.setOnRefreshListener {
            getAllCases("")
        }
        // setBackBtn(binding.mainHeader.toolbar);

    }


    fun setUI() {
        setSupportActionBar(binding!!.toolbar)
        getdashboardData()

        getAllCases("")
        prepareMenuData()
        populateExpandableList()

        binding!!.drawerLayout.addDrawerListener(this)
        binding!!.profileId.setOnClickListener {
            val intent = Intent(this@StaffDashBoardActivity, StaffProfileActivity::class.java)
            startActivity(intent)
        }
        AllCases = arrayListOf()
        // requestPermission()

//        homeViewModel.homeDataResponse.observe(this) {
//            when (it) {
//                is NetworkResult.Error -> {
//                    hideDialog()
//                    binding!!.swipeRefresherHome.isRefreshing = false;
//                }
//
//                is NetworkResult.Loading -> showDialog()
//                is NetworkResult.Success -> {
//                    hideDialog()
//
//                    binding!!.swipeRefresherHome.isRefreshing = false;
//                    attendanceINOUTStatus = "" + it.data!!.getClock_status()
//                    if (attendanceINOUTStatus.equals("1", ignoreCase = true)) {
//                        OnOfffAttendance = true
//                        binding!!.mainContent.mainHeader.attendanceOnOff.setImageResource(R.drawable.out)
//                    } else {
//                        binding!!.mainContent.mainHeader.attendanceOnOff.setImageResource(R.drawable.`in`)
//                        OnOfffAttendance = false
//                    }
//
//
//                    binding!!.mainContent.trotalAttend.text = "" + it.data.getAtten_month_data()
//
//                    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
//                    val date = Date()
//                    binding!!.mainContent.date.text = "" + formatter.format(date)
//
//                }
//
//            }
//        }

        binding!!.tvCreateCase.setOnClickListener {
            startActivity(CaseIDGenerateClass::class.java)

        }
        binding!!.tvNext.setOnClickListener {
            pageOffset.value = pageOffset.value!! + 1
            caseIdViewModel.getCaseId("50", pageOffset.value!!, "1", "");
        };
        pageOffset.observe(this) {

            if (it == 1) {
                binding!!.tvPrevious.isEnabled = false
            } else {
                binding!!.tvPrevious.isEnabled = true

                binding!!.tvPrevious.setOnClickListener { _ ->
                    pageOffset.value = it - 1;
                    caseIdViewModel.getCaseId("50", pageOffset.value!!, "1", "");
                }
            }
        }

        lastPage.observe(this) { page ->
            if (page == pageOffset.value) {
                binding!!.tvNext.isEnabled = false

            } else {
                binding!!.tvNext.isEnabled = true

                binding!!.tvNext.setOnClickListener { _ ->
                    pageOffset.value = pageOffset.value!! + 1;
                    caseIdViewModel.getCaseId("50", pageOffset.value!!, "1", "");
                }
            }
        }




        binding!!.toogleIcon.setOnClickListener(this)
        toggle = ActionBarDrawerToggle(
            this@StaffDashBoardActivity, binding!!.drawerLayout, toolbar, 0, 0
        )
        toggle!!.isDrawerIndicatorEnabled = false
        binding!!.drawerLayout.addDrawerListener(toggle!!)
        toggle!!.syncState()
        //        binding.menuList.setLayoutManager(new LinearLayoutManager(this));
//        binding.menuList.addOnItemTouchListener(new RecyclerItemClickListener(StaffDashBoardActivity.this, this));
        binding!!.attendanceOnOff.setOnClickListener { v: View? ->
            TakeAttendance(
                OnOfffAttendance
            )
        }
        binding!!.close.setOnClickListener {
            try {
                fileSelfie = null
                binding!!.selfieImage.setImageBitmap(null)
                binding!!.cardAttandance.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding!!.UploadImage.setOnClickListener { onImageSelected() }
        binding!!.clockInOut.setOnClickListener { v: View? -> callServer() }
        photoEasy = PhotoEasy.builder().setActivity(this).enableRequestPermission(true).build()
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSION_REQUEST_CODE
        )
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //requestPermission()
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                lat = it.latitude
                long = it.longitude
            }


            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, long, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                currentLocation =
                    "${addresses.first().featureName},${addresses.first().subAdminArea}, ${addresses.first().locality}, ${
                        addresses.first().adminArea
                    }"

            }
        }

    }

    override fun onResume() {
        getAllCases("")
        super.onResume()
    }


    fun getAllCases(search: String) {
        caseIdViewModel.getPagingData();
        caseIdViewModel.getStackRequest()

    }

    private fun setAdapter() {


        binding!!.rvDefaultersStatus.isNestedScrollingEnabled = false
        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding!!.rvDefaultersStatus.layoutManager = horizontalLayoutManager

        binding!!.rvDefaultersStatus.adapter = userAdapter.withLoadStateFooter(

            footer = ListLoadStateAdapter {
                caseIdViewModel.getPagingData()
            })

    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this).setMessage(message).setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null).create().show()
    }

    fun takePicture() {
        permissionsBuilder(Manifest.permission.CAMERA).build().send {
            when (it.first()) {
                is PermissionStatus.Denied.Permanently -> {}
                is PermissionStatus.Denied.ShouldShowRationale -> {}
                is PermissionStatus.Granted -> {
                    photoEasy.startActivityForResult(this@StaffDashBoardActivity)

                }

                is PermissionStatus.RequestRequired -> {
                    photoEasy.startActivityForResult(this@StaffDashBoardActivity)

                }
            }

        }
    }

    private fun populateExpandableList() {
        expandableListAdapter = ExpandableListAdapter(this, headerList, childList)
        binding!!.expandableListView.setAdapter(expandableListAdapter)
        binding!!.expandableListView.setOnGroupClickListener { parent, v, groupPosition, id ->
            if (headerList[groupPosition].isGroup) {
                if (!headerList[groupPosition].hasChildren) {

                    if (headerList[groupPosition].menuName == resources.getString(R.string.referral_code)) {
                        val phone = SharedPreferencesRepository.getDataManagerInstance().user.phone
                        val sharedUrl =
                            "click here for download to Farmer App:- https://play.google.com/store/apps/details?id=com.apnagodam&hl=en&referrer=$phone"
                        val sendIntent = Intent()
                        sendIntent.setAction(Intent.ACTION_SEND)
                        sendIntent.putExtra(Intent.EXTRA_TEXT, sharedUrl)
                        sendIntent.setType("text/plain")
                        Intent.createChooser(sendIntent, "Share via")
                        startActivity(sendIntent)
                        Log.e("refer url :", "" + sharedUrl)
                    } else if (headerList[groupPosition].menuName == resources.getString(R.string.select_language)) {
                        startActivity(LanguageActivity::class.java)
                    } else if (headerList[groupPosition].menuName == resources.getString(R.string.lead_generate)) {
                        startActivity(LeadGenerateClass::class.java)
                    } else if (headerList[groupPosition].menuName == resources.getString(R.string.simple_case_id)) {
                        startActivity(CaseListingActivity::class.java)
                    } /*else if (headerList.get(groupPosition).menuName.equals(getResources().getString(R.string.vendor))) {
                            startActivity(MyVendorVoacherListClass.class);
                        }*/
                    else if (headerList[groupPosition].menuName == resources.getString(R.string.spot_sell)) {
                        startActivity(SpotDealTrackListActivity::class.java)
                    } else if (headerList[groupPosition].menuName == resources.getString(R.string.intantion_title)) {
                        startActivity(IntantionApprovalListClass::class.java)
                    } else if (headerList[groupPosition].menuName == resources.getString(R.string.heat_map)) {
                        startActivity(StateMapActivity::class.java)
                    } else if (headerList[groupPosition].menuName == "Customer Map") {
                        startActivity(MobileNumberMapActivity::class.java)
                    } else if (headerList[groupPosition].menuName == resources.getString(R.string.fastcase)) {
                        startActivity(FastCaseActivity::class.java)
                    } else if (headerList[groupPosition].menuName == resources.getString(R.string.fastcase_list)) {
                        startActivity(FastCaseListActivity::class.java)
                    } else if (headerList.get(groupPosition).menuName == resources.getString(R.string.cancel_case_id)) {
                        startActivity(CancelCaseId::class.java)
                    } else if (headerList.get(groupPosition).menuName.equals("PV")) {
                        startActivity(UpdatePv::class.java)
                    } else if (headerList[groupPosition].menuName.equals(ConstantObjects.DISPLEASED_BAGS)) {
                        startActivity(DispledgedBags::class.java)
                    } else if (headerList[groupPosition].menuName.equals(ConstantObjects.APPROVE_DISPLEASED_BAGS)) {
                        startActivity(DispleasedListingActivity::class.java)
                    } else if (headerList[groupPosition].menuName == "Advances") {

                        startActivity(AdvancesActivity::class.java)
                    } else if (headerList[groupPosition].menuName == resources.getString(R.string.logout)) {
                        loginViewModel.doLogout()

                        // logout(resources.getString(R.string.logout_alert), "Logout")
                    }

                }
            }
            false
        }
        binding!!.expandableListView.setOnChildClickListener(OnChildClickListener { parent, v, groupPosition, childPosition, id ->
            if (childList[headerList[groupPosition]] != null) {
                val model = childList[headerList[groupPosition]]!![childPosition]
                if (model.url.isNotEmpty() && SharedPreferencesRepository.getDataManagerInstance().userPermission != null) {
                    when (model.menuName) {
                        resources.getString(R.string.price) -> {
                            startActivity(InPricingListingActivity::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.truck_book) -> {
                            startActivity(TruckBookListingActivity::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.labour_book) -> {
                            startActivity(LabourBookListingActivity::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.firstkanta_parchi) -> {
                            startActivity(FirstkanthaParchiListingActivity::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.f_quality_repots) -> {
                            startActivity(FirstQualityReportListingActivity::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.secoundkanta_parchi) -> {
                            startActivity(SecoundkanthaParchiListingActivity::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.s_quality_repots) -> {
                            startActivity(SecoundQualityReportListingActivity::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.my_convance) -> {
                            startActivity(MyConveyanceListClass::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.vendor) -> {
                            startActivity(MyVendorVoacherListClass::class.java)
                        }

                        resources.getString(R.string.case_status_in) -> {
                            startActivity(CaseStatusINListClass::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.case_status_out) -> {
                            startActivity(CaseStatusINListClass::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.out_relase_book) -> {
                            startActivity(OUTRelaseOrderListingActivity::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.out_truck_book) -> {
                            startActivity(OUTTruckBookListingActivity::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.out_labour_book) -> {
                            startActivity(OUTLabourBookListingActivity::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.out_f_katha_book) -> {
                            startActivity(OutFirstkanthaParchiListingActivity::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.out_f_quality_book) -> {
                            startActivity(OutFirstQualityReportListingActivity::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.out_s_katha__book) -> {
                            startActivity(OutSecoundkanthaParchiListingActivity::class.java)
                            return@OnChildClickListener true
                        }

                        resources.getString(R.string.out_s_quality_book) -> {
                            startActivity(OutSecoundQualityReportListingActivity::class.java)
                            return@OnChildClickListener true
                        }

                        ConstantObjects.AUDIT_CM -> {
                            startActivity(CmDetailsActivity::class.java)
                            return@OnChildClickListener true
                        }

                        ConstantObjects.AUDIT_VIDEO -> {
                            startActivity(VideoRecordingActivity::class.java)
                            return@OnChildClickListener true

                        }

                        ConstantObjects.AUDIT_NEIGHBOUR -> {
                            startActivity(AuditNeighbourActivity::class.java)
                            return@OnChildClickListener true

                        }

                        ConstantObjects.AUDIT_PV -> {
                            startActivity(AuditPvActivity::class.java)
                            return@OnChildClickListener true

                        }


                        ConstantObjects.AUDIT_IN_OUT_LOCATION -> {
                            startActivity(InOutActivity::class.java)
                            return@OnChildClickListener true
                        }

                        ConstantObjects.AUDIT_QZ -> {
                            startActivity(AuditQualityStock::class.java)
                            return@OnChildClickListener true

                        }

                        else -> return@OnChildClickListener false
                    }


                }
            }
            false
        })
    }

    fun prepareMenuData() {
        var menuModel = MenuModel(
            resources.getString(R.string.home),
            true,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.dashboard
        ) //Menu of Android Tutorial. No sub menus
        val menuModel1 = MenuModel(
            resources.getString(R.string.referral_code),
            true,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.earn
        ) //Menu of Android Tutorial. No sub menus
        val menuModel2 = MenuModel(
            resources.getString(R.string.select_language),
            true,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.translate
        ) //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel)
        headerList.add(menuModel1)
        headerList.add(menuModel2)
        val menuModel3 = MenuModel(
            resources.getString(R.string.lead_generate),
            true,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.lead
        ) //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel3)
        val menuModel4 = MenuModel(
            resources.getString(R.string.simple_case_id),
            true,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.create_case_id
        ) //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel4)
        if (!menuModel.hasChildren) {
            childList[menuModel] = null
        }
        var childModelsList: MutableList<MenuModel> = ArrayList()
        var childModel: MenuModel
        menuModel = MenuModel(
            "IN",
            true,
            true,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.internal
        ) //Menu of Java Tutorials
        headerList.add(menuModel)
        childModel = MenuModel(
            resources.getString(R.string.truck_book),
            false,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.truck_option
        )
        childModelsList.add(childModel)
        childModel = MenuModel(
            resources.getString(R.string.labour_book),
            false,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.labour_option
        )
        childModelsList.add(childModel)
        childModel = MenuModel(
            resources.getString(R.string.firstkanta_parchi),
            false,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.kanta_parchi_one
        )
        childModelsList.add(childModel)
        childModel = MenuModel(
            resources.getString(R.string.f_quality_repots),
            false,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.first_quality_report
        )
        childModelsList.add(childModel)
        childModel = MenuModel(
            resources.getString(R.string.secoundkanta_parchi),
            false,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.second_kanta_parchi
        )
        childModelsList.add(childModel)
        childModel = MenuModel(
            resources.getString(R.string.s_quality_repots),
            false,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.second_quality_report
        )

        childModelsList.add(childModel)
        if (menuModel.hasChildren) {
            Log.d("API123", "here")
            childList[menuModel] = childModelsList
        }
        childModelsList = ArrayList()
        menuModel = MenuModel(
            "OUT",
            true,
            true,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.external
        ) //Menu of Python Tutorials
        headerList.add(menuModel)

        childModel = MenuModel(
            resources.getString(R.string.out_truck_book),
            false,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.truck_option
        )
        childModelsList.add(childModel)
        childModel = MenuModel(
            resources.getString(R.string.out_labour_book),
            false,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.labour_option
        )
        childModelsList.add(childModel)
        childModel = MenuModel(
            resources.getString(R.string.out_f_quality_book),
            false,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.first_quality_report
        )
        childModelsList.add(childModel)
        childModel = MenuModel(
            resources.getString(R.string.out_f_katha_book),
            false,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.kanta_parchi_one
        )
        childModelsList.add(childModel)
        childModel = MenuModel(
            resources.getString(R.string.out_s_quality_book),
            false,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.second_quality_report
        )
        childModelsList.add(childModel)
        childModel = MenuModel(
            resources.getString(R.string.out_s_katha__book),
            false,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.second_kanta_parchi
        )

        childModelsList.add(childModel)
        if (menuModel.hasChildren) {
            childList[menuModel] = childModelsList
        }
        menuModel = MenuModel(
            "Voucher",
            true,
            true,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.voucher
        ) //Menu of Java Tutorials
        headerList.add(menuModel)
        childModelsList = ArrayList()
        childModel = MenuModel(
            resources.getString(R.string.my_convance),
            false,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.voucher
        )
        childModelsList.add(childModel)
        childModel = MenuModel(
            resources.getString(R.string.vendor),
            false,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.voucher
        )
        childModelsList.add(childModel)
        if (menuModel.hasChildren) {
            childList[menuModel] = childModelsList
        }
//        val menuModel6 = MenuModel(
//            resources.getString(R.string.spot_sell),
//            true,
//            false,
//            ConstantObjects.EXPANDABLE_LIST_URL,
//            R.drawable.deal_track
//        ) //Menu of Android Tutorial. No sub menus
//        val menuModel7 = MenuModel(
//            resources.getString(R.string.intantion_title),
//            true,
//            false,
//            ConstantObjects.EXPANDABLE_LIST_URL,
//            R.drawable.purchase
//        ) //Menu of Android Tutorial. No sub menus
//        val menuModel8 = MenuModel(
//            "Customer Map",
//            true,
//            false,
//            ConstantObjects.EXPANDABLE_LIST_URL,
//            R.drawable.heat_map
//        ) //Menu of Android Tutorial. No sub menus
//        val menuModel9 = MenuModel(
//            resources.getString(R.string.heat_map),
//            true,
//            false,
//            ConstantObjects.EXPANDABLE_LIST_URL,
//            R.drawable.heat_map
//        ) //Menu of Android Tutorial. No sub menus

        val menuMode21 = MenuModel(
            resources.getString(R.string.fastcase),
            true,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.heat_map
        ) //Menu of Android Tutorial. No sub menus
        val menuMode22 = MenuModel(
            resources.getString(R.string.fastcase_list),
            true,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.heat_map
        ) //Menu of
        val menuModel23 = MenuModel(
            resources.getString(R.string.cancel_case_id),
            true,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.create_case_id
        ) // Android Tutorial. No sub menus
        val menuModel24 = MenuModel(
            "PV",
            true,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.create_case_id
        ) // And

        val menuModel25 = MenuModel(
            "Advances",
            true,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.earn
        ) // // roid
        val menuMode20 = MenuModel(
            resources.getString(R.string.logout),
            true,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.out_icon
        ) //Menu of Android Tutorial. No sub menus

        val menuMode26 = MenuModel(
            ConstantObjects.DISPLEASED_BAGS,
            true,
            false,
            ConstantObjects.EXPANDABLE_LIST_URL,
            R.drawable.pv
        )


        //  headerList.add(menuModel5);
//        headerList.add(menuModel6)
//        headerList.add(menuModel7)
//        headerList.add(menuModel8)
//        headerList.add(menuModel9)
        userDetails = SharedPreferencesRepository.getDataManagerInstance().user
        userDetails?.let {
            if (it.terminal == null) {
                menuModel = MenuModel(
                    ConstantObjects.AUDIT,
                    true,
                    true,
                    ConstantObjects.EXPANDABLE_LIST_URL,
                    R.drawable.audit
                )
                headerList.add(menuModel)

                childModelsList = ArrayList()
                childModel = MenuModel(
                    ConstantObjects.AUDIT_IN_OUT_LOCATION,
                    false,
                    false,
                    ConstantObjects.EXPANDABLE_LIST_URL,
                    R.drawable.location
                )
                childModelsList.add(childModel)

                childModel = MenuModel(
                    ConstantObjects.AUDIT_CM,
                    false,
                    false,
                    ConstantObjects.EXPANDABLE_LIST_URL,
                    R.drawable.cm
                )
                childModelsList.add(childModel)
                childModel = MenuModel(
                    ConstantObjects.AUDIT_NEIGHBOUR,
                    false,
                    false,
                    ConstantObjects.EXPANDABLE_LIST_URL,
                    R.drawable.neighbour
                )
                childModelsList.add(childModel)
                childModel = MenuModel(
                    ConstantObjects.AUDIT_PV,
                    false,
                    false,
                    ConstantObjects.EXPANDABLE_LIST_URL,
                    R.drawable.pv
                )
                childModelsList.add(childModel)

                childModel = MenuModel(
                    ConstantObjects.AUDIT_VIDEO,
                    false,
                    false,
                    ConstantObjects.EXPANDABLE_LIST_URL,
                    R.drawable.camera
                )
                childModelsList.add(childModel)
                childModel = MenuModel(
                    ConstantObjects.AUDIT_QZ,
                    false,
                    false,
                    ConstantObjects.EXPANDABLE_LIST_URL,
                    R.drawable.wheat
                )

                childModel = MenuModel(
                    ConstantObjects.AUDIT_QZ,
                    false,
                    false,
                    ConstantObjects.EXPANDABLE_LIST_URL,
                    R.drawable.quaility
                )
                childModelsList.add(childModel)
                if (menuModel.hasChildren) {
                    Log.d("API123", "here")
                    childList[menuModel] = childModelsList
                }

                val menuMode27 = MenuModel(
                    ConstantObjects.APPROVE_DISPLEASED_BAGS,
                    true,
                    false,
                    ConstantObjects.EXPANDABLE_LIST_URL,
                    R.drawable.pv
                )
                headerList.add(menuMode27)

            }
        }
        headerList.add(menuMode26)
        headerList.add(menuModel25)
        headerList.add(menuModel23)
        headerList.add(menuModel24)
        headerList.add(menuMode21)
        headerList.add(menuMode22)
        headerList.add(menuMode20)

    }


//    private val attendanceStatus: Unit
//        private get() {
//            apiService.getattendanceStatus()
//            apiService.getattendanceStatus().enqueue(object : NetworkCallback<AttendanceResponse?>(
//                activity
//            ) {
//                override fun onSuccess(body: AttendanceResponse) {
//                    // Toast.makeText(getApplicationContext(),body.getMessage(),Toast.LENGTH_SHORT).show();
//                    attendanceINOUTStatus = "" + body.getClock_status()
//                    if (attendanceINOUTStatus.equals("1", ignoreCase = true)) {
//                        OnOfffAttendance = true
//                        binding!!.mainContent.mainHeader.attendanceOnOff.setImageResource(R.drawable.out)
//                    } else {
//                        binding!!.mainContent.mainHeader.attendanceOnOff.setImageResource(R.drawable.`in`)
//                        OnOfffAttendance = false
//                    }
//                    getdashboardData()
//                }
//            })
//        }


    fun setObservers() {

        caseIdViewModel.getPagingData()
        //   userAdapter = CasesAdapter(this, apiService);
        caseIdViewModel.userCasesPagination.observe(this) {
            lifecycleScope.launch {
                userAdapter.submitData(it)
                setAdapter()


            }


        }
        lifecycleScope.launch {
            userAdapter.loadStateFlow.collect {


                when (it.refresh) {

                    is LoadState.Error -> {
                        Utility.hideDialog(this@StaffDashBoardActivity)
                        binding!!.swipeRefresherHome.isRefreshing = false

                    }

                    LoadState.Loading -> {
                        Utility.showDialog(this@StaffDashBoardActivity, "")
                        binding?.let { it.swipeRefresherHome.isRefreshing = true }
                    }

                    is LoadState.NotLoading -> {
                        Utility.hideDialog(this@StaffDashBoardActivity)
                        binding!!.swipeRefresherHome.isRefreshing = false
                    }
                }
            }
        }
        loginViewModel.logoutResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    SharedPreferencesRepository.getDataManagerInstance().clear()
                    SharedPreferencesRepository.setIsUserName(false)
                    SharedPreferencesRepository.saveSessionToken("")
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("setting", "")
                    startActivityAndClear(LoginActivity::class.java)

                }

                is NetworkResult.Loading -> {


                }

                is NetworkResult.Success -> {
                    SharedPreferencesRepository.getDataManagerInstance().clear()
                    SharedPreferencesRepository.setIsUserName(false)
                    SharedPreferencesRepository.saveSessionToken("")
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("setting", "")
                    startActivityAndClear(LoginActivity::class.java)
                }
            }
        }
//        caseIdViewModel.response.observe(this) { body ->
//            when (body) {
//                is NetworkResult.Success -> {
//                    hideDialog();
//                    binding!!.swipeRefresherHome.isRefreshing = false;
//                    if (body.data != null) {
//                        AllCases.clear()
//                        if (body.data.status == "1") {
//                            SharedPreferencesRepository.getDataManagerInstance().user.let {userDetails->
//                                body.data.getaCase().let {
//                                    totalPage = it?.lastPage!!
//                                    if(it.data!=null){
//                                        it.data.let { data->
//                                            if(data!=null){
//                                                for (i in data.indices) {
//
//                                                    if ((data[i].cctvReport == null
//                                                                || data[i].ivrReport == null
//                                                                || data[i].secondQualityReport == null
//                                                                || data[i].sendToLab == null
//                                                                || data[i].firstKantaParchi == null
//                                                                || data[i].secondKantaParchi == null
//                                                                || data[i].labourBook == null
//                                                                || data[i].truckbook == null
//                                                                || data[i].gatepassReport == null)
//                                                    ) {
//                                                        if (userDetails.terminal != null) {
//                                                            if (data[i].terminalId.toString() == userDetails.terminal.toString()) {
//                                                                AllCases.add(data[i])
//                                                            }
//                                                        } else {
//                                                            AllCases.add(data[i])
//                                                        }
//                                                    }
//                                                }
//                                            }
//
//                                        }
//                                    }
//
//                                }
//
//
//                                binding!!.mainContent.tvNext.isEnabled = AllCases.isNotEmpty()
//
//                                casesTopAdapter =
//                                    CasesTopAdapter(AllCases.reversed(), this, apiService)
//                                casesTopAdapter!!.notifyDataSetChanged()
//                                hideDialog()
//                                if (AllCases.isEmpty()) {
//                                    binding!!.mainContent.emptyData.visibility = View.VISIBLE
//                                    binding!!.mainContent!!.rvDefaultersStatus.visibility =
//                                        View.GONE
//                                }
//                            }
//                        } else {
//                            hideDialog()
//
//                            Toast.makeText(this, body.message, Toast.LENGTH_SHORT)
//                            binding!!.mainContent.emptyData.visibility = View.VISIBLE
//                            binding!!.mainContent!!.rvDefaultersStatus.visibility = View.GONE
//                        }
//                        lastPage.value = body.data.getaCase()!!.lastPage
//
//                    } else {
//                        hideDialog()
//                        binding!!.mainContent.emptyData.visibility = View.VISIBLE
//                        binding!!.mainContent!!.rvDefaultersStatus.visibility = View.GONE
//                    }
//
//                }
//
//                is NetworkResult.Error -> {
//                    hideDialog()
//                    Toast.makeText(this, body.message, Toast.LENGTH_SHORT)
//                    binding!!.mainContent.emptyData.visibility = View.GONE
//                    binding!!.mainContent!!.rvDefaultersStatus.visibility = View.GONE
//
//                }
//
//                is NetworkResult.Loading -> {
//                    showDialog()
//                }
//            }
//        }
        caseIdViewModel.stackRequestResponse.observe(this) { it ->
            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
                }

                is NetworkResult.Loading -> {
                }

                is NetworkResult.Success -> {
                    when (it.data) {
                        null -> {}
                        else -> {
                            it.data?.let {
                                if (it.status == "2" || it.status == "3" || it.status == "0") {
                                    startActivity(Intent(this, LoginActivity::class.java))
                                } else {
                                    inwardsList.clear()
                                    outwardsList.clear()
                                    for (i in it.inwardRequestData.indices) {

                                        if (userDetails != null) {
                                            if (userDetails!!.terminal == null) {
                                                inwardsList.add(it.inwardRequestData[i])
                                            } else if (it.inwardRequestData[i].terminalId.toString() == userDetails!!.terminal) {
                                                inwardsList.add(it.inwardRequestData[i])
                                            }
                                        }
                                    }

                                    for (j in it.outwardRequestData.indices) {
                                        if (userDetails != null) {
                                            if (userDetails!!.terminal == null) {
                                                outwardsList.add(it.outwardRequestData[j])
                                            } else if (it.outwardRequestData[j].terminalId.toString() == userDetails!!.terminal) {
                                                outwardsList.add(it.outwardRequestData[j])
                                            }
                                        }
                                    }


                                    binding!!.incase.setText(inwardsList.size.toString())
                                    binding!!.outcase.setText(outwardsList.size.toString())
                                    when (it.inwardRequestData.size) {
                                        0 -> {

                                        }

                                        else -> {
                                            binding!!.cardIncase.setOnClickListener {
                                                val intent =
                                                    Intent(this, InwardListActivity::class.java)
                                                startActivity(intent)
                                            }
                                        }
                                    }

                                    when (it.outwardRequestData.size) {
                                        0 -> {

                                        }

                                        else -> {
                                            binding!!.cardOutCase.setOnClickListener {
                                                val intent =
                                                    Intent(this, OutwardsListActivity::class.java)
                                                startActivity(intent)
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }


//        homeViewModel.attendenceResponse.observe(this) {
//            when (it) {
//                is NetworkResult.Error -> {
//                    hideDialog()
//                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
//                    binding!!.swipeRefresherHome.isRefreshing = false;
//
//                }
//
//                is NetworkResult.Loading -> {
//                    showDialog()
//                }
//
//                is NetworkResult.Success -> {
//                    binding!!.swipeRefresherHome.isRefreshing = false;
//
//                    hideDialog()
//                    if (it.data != null) {
//                        if (it.data.status == "1") {
//                            fileSelfie = null
//                            OnOfffAttendance =
//                                if (it.data.getClock_status()
//                                        .equals("1", ignoreCase = true)
//                                ) {
//                                    binding!!.mainContent.mainHeader.attendanceOnOff.setImageResource(
//                                        R.drawable.out
//                                    )
//                                    true
//                                } else {
//                                    binding!!.mainContent.mainHeader.attendanceOnOff.setImageResource(
//                                        R.drawable.`in`
//                                    )
//                                    false
//                                }
//                            Toast.makeText(
//                                this@StaffDashBoardActivity,
//                                it.data.message,
//                                Toast.LENGTH_LONG
//                            ).show()
//                        } else {
//                            Toast.makeText(
//                                this@StaffDashBoardActivity,
//                                it.data.message,
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//                }
//            }
//        }
        userDetails = SharedPreferencesRepository.getDataManagerInstance().user
        userDetails?.let {
            Glide.with(this)
                .load(Constants.IMAGE_BASE_URL + it.aadharImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.user_shape)
                .circleCrop().into(binding!!.userProfileImage)
            binding!!.userNameText.text =
                it.getFname() + " " + it.getLname() + "(" + it.getEmp_id() + ")"

            val navigationVLCAdapter = NavigationAdapter(
                menuList, userDetails, this
            )
            navigationVLCAdapter.setOnProfileClickInterface(this)
        }


    }

    fun getdashboardData() {
        homeViewModel.getCommodities("Emp")


        //     font = Typeface.createFromAsset(getAssets(), "FontAwesome.ttf" );
        try {


        } catch (e: Exception) {
            Log.e("Exception : on profile", e.message!!)
        }
    }

    private fun callServer() {
        var EmployeeImage = ""
        if (fileSelfie != null) {
            if (lat != 0.0 && long != 0.0) {
                EmployeeImage = "" + Utility.transferImageToBase64(fileSelfie)
                homeViewModel.attendence(
                    AttendancePostData(
                        "" + latitude, "" + longitude, "" + attendanceINOUTStatus, EmployeeImage
                    )
                )


            } else {
                Toast.makeText(
                    this@StaffDashBoardActivity, "Enable Your Location Please!!", Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                this@StaffDashBoardActivity, "Take your selfie please!", Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun TakeAttendance(OnOfffAttendance: Boolean) {
        var OnOfffAttendance = OnOfffAttendance
        OnOfffAttendance = !OnOfffAttendance
        setFunctional(OnOfffAttendance)
    }

    private fun setFunctional(flag: Boolean) {
        val dialog = BottomSheetDialog(this@StaffDashBoardActivity)
        dialog.setContentView(R.layout.dilog_attedance_bottom)
        val clockInOut: MaterialButton =
            dialog.findViewById<View>(R.id.clock_in_out) as MaterialButton
        val close: MaterialButton = dialog.findViewById<View>(R.id.close) as MaterialButton
        val clockIn: TextView = dialog.findViewById<View>(R.id.clockIn) as TextView
        val UploadImage: LinearLayout = dialog.findViewById<View>(R.id.UploadImage) as LinearLayout
        selfieImage = dialog.findViewById<View>(R.id.selfieImage) as ImageView?
        dialog.setCancelable(true)
        dialog.show()
        close.setOnClickListener(View.OnClickListener {
            try {
                fileSelfie = null
                selfieImage!!.setImageBitmap(null)
                dialog.cancel()
                dialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
        UploadImage.setOnClickListener(View.OnClickListener {
            dispatchTakePictureIntent()


            // callProfileImageSelector(REQUEST_CAMERA);
        })
        clockInOut.setOnClickListener(View.OnClickListener { v: View? -> callServer() })
        if (!OnOfffAttendance) {
            attendanceINOUTStatus = "1"

        } else {

            clockIn.setText(resources.getString(R.string.clock_out))
            clockInOut.setText(resources.getString(R.string.clock_out))
            attendanceINOUTStatus = "2"

        }
    }


    override fun dispatchTakePictureIntent() {
        permissionsBuilder(Manifest.permission.CAMERA).build().send {
            when (it.first()) {
                is PermissionStatus.Denied.Permanently -> {}
                is PermissionStatus.Denied.ShouldShowRationale -> {}
                is PermissionStatus.Granted -> {
                    photoEasy.startActivityForResult(this@StaffDashBoardActivity)

                }

                is PermissionStatus.RequestRequired -> {
                    photoEasy.startActivityForResult(this@StaffDashBoardActivity)

                }
            }

        }

    }

    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.toogleIcon -> toggleDrawer()
        }
    }

    fun toggleDrawer() {
        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding!!.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, R.string.exist_app, Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 3000)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        view.findViewById<TextView>(R.id.tv_status).setOnClickListener {
            Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show();

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
    override fun onDrawerOpened(drawerView: View) {}
    override fun onDrawerClosed(drawerView: View) {}
    override fun onDrawerStateChanged(newState: Int) {}


    override fun onItemClick(view: View, position: Int) {
//        binding!!.drawerLayout.postDelayed({
//            toggleDrawer()
//
//            if (position == 1) {
//                onBackPressed()
//            }
//            if (position == 2) {
//                startActivity(LanguageActivity::class.java)
//            }
//            for (i in SharedPreferencesRepository.getDataManagerInstance().userPermission.indices) {
//
//                if (position == 3) {
//                    startActivity(LeadGenerateClass::class.java)
//
//                }
//                if (position == 4) {
//                    startActivity(CaseListingActivity::class.java)
//
//                }
//                if (position == 5) {
//                    startActivity(InPricingListingActivity::class.java)
//
//                }
//                if (position == 6) {
//                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
//                            "15",
//                            ignoreCase = true
//                        )
//                    ) {
//                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
//                            startActivity(TruckBookListingActivity::class.java)
//                        } else {
//                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
//                                    "16",
//                                    ignoreCase = true
//                                )
//                            ) {
//                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
//                                    startActivity(LabourBookListingActivity::class.java)
//                                }
//                            }
//                        }
//                    }
//                }
//                if (position == 7) {
//                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
//                            "20",
//                            ignoreCase = true
//                        )
//                    ) {
//                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
//                            startActivity(FirstkanthaParchiListingActivity::class.java)
//                        }
//                    } else {
//                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
//                                "16",
//                                ignoreCase = true
//                            )
//                        ) {
//                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
//                                startActivity(LabourBookListingActivity::class.java)
//                            }
//                        }
//                    }
//                }
//                if (position == 8) {
//                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
//                            "20",
//                            ignoreCase = true
//                        )
//                    ) {
//                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
//                            startActivity(FirstkanthaParchiListingActivity::class.java)
//                        }
//                    }
//                }
//                if (position == 9) {
//                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
//                            "18",
//                            ignoreCase = true
//                        )
//                    ) {
//                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
//                            startActivity(FirstQualityReportListingActivity::class.java)
//                        }
//                    }
//                }
//                if (position == 10) {
//                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
//                            "20",
//                            ignoreCase = true
//                        )
//                    ) {
//                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
//                            startActivity(SecoundkanthaParchiListingActivity::class.java)
//                        }
//                    }
//                }
//                if (position == 11) {
//                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
//                            "18",
//                            ignoreCase = true
//                        )
//                    ) {
//                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
//                            startActivity(SecoundQualityReportListingActivity::class.java)
//                        }
//                    }
//                }
//                if (position == 12) {
//                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
//                            "19",
//                            ignoreCase = true
//                        )
//                    ) {
//                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
//                            // startActivity(FirstkanthaParchiListingActivity.class);
//                        }
//                    }
//                }
//            }
//            if (position == 13) {
//                logout(resources.getString(R.string.logout_alert), "Logout")
//            }
//            when (position) {
//                0 -> {}
//                1 -> {
//                }
//
//                2 -> startActivity(LanguageActivity::class.java)
//                3 -> startActivity(LeadGenerateClass::class.java)
//                4 -> startActivity(CaseIDGenerateClass::class.java)
//                5 -> startActivity(InPricingListingActivity::class.java)
//                6 -> startActivity(TruckBookListingActivity::class.java)
//                7 -> startActivity(LabourBookListingActivity::class.java)
//                8 -> startActivity(FirstkanthaParchiListingActivity::class.java)
//                9 -> startActivity(FirstQualityReportListingActivity::class.java)
//                10 -> startActivity(SecoundkanthaParchiListingActivity::class.java)
//                11 -> startActivity(SecoundQualityReportListingActivity::class.java)
//                12 -> startActivity(GatePassListingActivity::class.java)
//                13 -> {
//                    loginViewModel.doLogout()
//                    loginViewModel.logoutResponse.observe(this) {
//                        when (it) {
//                            is NetworkResult.Error -> {
//                                showToast(it.message)
//
//                            }
//
//                            is NetworkResult.Loading -> {
//
//
//                            }
//
//                            is NetworkResult.Success -> {
//                                SharedPreferencesRepository.getDataManagerInstance().clear()
//                                SharedPreferencesRepository.setIsUserName(false)
//                                SharedPreferencesRepository.saveSessionToken("")
//                                val intent = Intent(this, LoginActivity::class.java)
//                                intent.putExtra("setting", "")
//                                startActivity(LoginActivity::class.java)
//                                this.finish()
//
//                            }
//                        }
//                    }
//                }        //call logout api
//
//            }
//        }, 10)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        photoEasy.onActivityResult(1566, -1, object : OnPictureReady {
            override fun onFinish(thumbnail: Bitmap?) {

                if (thumbnail != null) {
                    val userDetails = SharedPreferencesRepository.getDataManagerInstance().user

                    var stampMap = mapOf(
                        "current_location" to "$currentLocation",
                        "emp_code" to userDetails.emp_id,
                        "emp_name" to userDetails.fname
                    )
                    var stampedBitmap = ImageHelper().createTimeStampinBitmap(
                        File(compressImage(bitmapToFile(thumbnail!!).path)), stampMap
                    )

                    labFile.value =
                        File(compressImage(bitmapToFile(stampedBitmap).path.toString()));

//                    fileSelfie = File(compressImage(bitmapToFile(stampedBitmap).path.toString()))
//                    val uri = Uri.fromFile(fileSelfie)
//                    selfieImage!!.setImageURI(uri)


                }
            }

        })

    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // main logic
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermission()
                    }
                }
            }
        }
    }

    override fun onProfileImgClick() {
        val intent = Intent(this, StaffProfileActivity::class.java)
        startActivity(intent)
    }

    override fun onConnected(bundle: Bundle?) {}
    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    companion object {
        // for location
        private const val REQUEST_LOCATION = 1
    }

    override fun onViewBound(
        viewHolder: BaseViewHolder<LayoutTopCaseGenerateBinding>?, position: Int
    ) {
        viewHolder!!.itemView.findViewById<TextView>(R.id.tv_status).setOnClickListener {
            Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show();

        }
        Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show();
    }
}
