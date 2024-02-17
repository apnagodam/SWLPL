package com.apnagodam.staff.activity

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallback
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.AttendancePostData
import com.apnagodam.staff.Network.Response.AttendanceResponse
import com.apnagodam.staff.Network.viewmodel.HomeViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.caseid.CaseIDGenerateClass
import com.apnagodam.staff.activity.casestatus.CaseStatusINListClass
import com.apnagodam.staff.activity.convancy_voachar.MyConveyanceListClass
import com.apnagodam.staff.activity.`in`.first_kantaparchi.FirstkanthaParchiListingActivity
import com.apnagodam.staff.activity.`in`.first_quality_reports.FirstQualityReportListingActivity
import com.apnagodam.staff.activity.`in`.gatepass.GatePassListingActivity
import com.apnagodam.staff.activity.`in`.labourbook.LabourBookListingActivity
import com.apnagodam.staff.activity.`in`.pricing.InPricingListingActivity
import com.apnagodam.staff.activity.`in`.secound_kanthaparchi.SecoundkanthaParchiListingActivity
import com.apnagodam.staff.activity.`in`.secound_quality_reports.SecoundQualityReportListingActivity
import com.apnagodam.staff.activity.`in`.truckbook.TruckBookListingActivity
import com.apnagodam.staff.activity.lead.LeadGenerateClass
import com.apnagodam.staff.activity.out.deliveryorder.OUTDeliverdOrderListingActivity
import com.apnagodam.staff.activity.out.f_katha_parchi.OutFirstkanthaParchiListingActivity
import com.apnagodam.staff.activity.out.f_quailty_report.OutFirstQualityReportListingActivity
import com.apnagodam.staff.activity.out.gatepass.OutGatePassListingActivity
import com.apnagodam.staff.activity.out.labourbook.OUTLabourBookListingActivity
import com.apnagodam.staff.activity.out.releaseorder.OUTRelaseOrderListingActivity
import com.apnagodam.staff.activity.out.s_katha_parchi.OutSecoundkanthaParchiListingActivity
import com.apnagodam.staff.activity.out.s_quaility_report.OutSecoundQualityReportListingActivity
import com.apnagodam.staff.activity.out.truckbook.OUTTruckBookListingActivity
import com.apnagodam.staff.activity.vendorPanel.MyVendorVoacherListClass
import com.apnagodam.staff.adapter.ExpandableListAdapter
import com.apnagodam.staff.adapter.NavigationAdapter
import com.apnagodam.staff.databinding.StaffDashboardBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.interfaces.OnProfileClickListener
import com.apnagodam.staff.module.AllUserPermissionsResultListResponse
import com.apnagodam.staff.module.DashBoardData
import com.apnagodam.staff.module.MenuModel
import com.apnagodam.staff.module.UserDetails
import com.apnagodam.staff.utils.Constants
import com.apnagodam.staff.utils.LocationUtils
import com.apnagodam.staff.utils.RecyclerItemClickListener
import com.apnagodam.staff.utils.Utility
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
@AndroidEntryPoint
class StaffDashBoardActivity() : BaseActivity<StaffDashboardBinding?>(), View.OnClickListener,
    OnProfileClickListener, RecyclerItemClickListener.OnItemClickListener,
    AdapterView.OnItemSelectedListener, DrawerListener, ConnectionCallbacks,
    OnConnectionFailedListener {
    private var toggle: ActionBarDrawerToggle? = null
    private val toolbar: Toolbar? = null
    var doubleBackToExitPressedOnce = false
    var locationManager: LocationManager? = null
    private var latitude: String? = null
    private var longitude: String? = null
    var OnOfffAttendance = false
    var attendanceINOUTStatus = "2"
    var fileSelfie: File? = null
    var userDetails: UserDetails? = null
    var selfieImage: ImageView? = null
    var options: Options? = null

    // for location
    private val locationUtils: LocationUtils? = null
    private val REQUEST_CODE = 1000
    var mGoogleApiClient: GoogleApiClient? = null
    var expandableListAdapter: ExpandableListAdapter? = null
    var headerList: MutableList<MenuModel> = ArrayList()
    var childList = HashMap<MenuModel, List<MenuModel>?>()
    var font: Typeface? = null
    val homeViewModel by viewModels<HomeViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.staff_dashboard
    }

    override fun setUp() {

        getdashboardData()



        // setBackBtn(binding.mainHeader.toolbar);
        setSupportActionBar(binding!!.mainContent.mainHeader.toolbar)
        binding!!.drawerLayout.addDrawerListener(this)
        binding!!.profileId.setOnClickListener {
            val intent = Intent(this@StaffDashBoardActivity, StaffProfileActivity::class.java)
            startActivity(intent)
        }
        binding!!.mainContent.mainHeader.toogleIcon.setOnClickListener(this)
        toggle = ActionBarDrawerToggle(
            this@StaffDashBoardActivity, binding!!.drawerLayout, toolbar,
            0, 0
        )
        toggle!!.isDrawerIndicatorEnabled = false
        binding!!.drawerLayout.addDrawerListener(toggle!!)
        toggle!!.syncState()
        //        binding.menuList.setLayoutManager(new LinearLayoutManager(this));
//        binding.menuList.addOnItemTouchListener(new RecyclerItemClickListener(StaffDashBoardActivity.this, this));
        binding!!.mainContent.mainHeader.tvIvr.setOnClickListener { singlePerm() }
        binding!!.mainContent.mainHeader.attendanceOnOff.setOnClickListener { v: View? ->
            TakeAttendance(
                OnOfffAttendance
            )
        }
        binding!!.mainContent.close.setOnClickListener {
            try {
                fileSelfie = null
                binding!!.mainContent.selfieImage.setImageBitmap(null)
                binding!!.mainContent.cardAttandance.visibility = View.GONE
                binding!!.mainContent.WelcomeMsg.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding!!.mainContent.UploadImage.setOnClickListener { onImageSelected() }
        binding!!.mainContent.clockInOut.setOnClickListener { v: View? -> callServer() }
        //    getAttendanceStatus();
//        locationUtils = new LocationUtils(getActivity(), 1000, 2000, (location, address) -> {
//            //   showToast(location.getLatitude() + " : " + location.getLongitude());
//            Log.d("location", "" + location.getLatitude() + "," + "" + location.getLongitude());
//            latitude = "" + location.getLatitude();
//            longitude = "" + location.getLongitude();
//            LocationUtils.LocationAddress locationAddress = new LocationUtils.LocationAddress();
//            String geolocation = String.valueOf(locationAddress.getAddressFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), StaffDashBoardActivity.this));
//            Log.e("geolocationLogin", " " + geolocation);
//        }, REQUEST_CODE);
        //set google api client for hint request
//        mGoogleApiClient = GoogleApiClient.Builder(this)
//            .addConnectionCallbacks(this)
//            .enableAutoManage(this, this)
//            .addApi(Auth.CREDENTIALS_API)
//            .build()
    }

    private fun populateExpandableList() {
        expandableListAdapter = ExpandableListAdapter(this, headerList, childList)
        binding!!.expandableListView.setAdapter(expandableListAdapter)
        binding!!.expandableListView.setOnGroupClickListener { parent, v, groupPosition, id ->
            if (headerList[groupPosition].isGroup) {
                if (!headerList[groupPosition].hasChildren) {
                    Log.e("groupclick", "gdddddd")
                    if (headerList[groupPosition].menuName == resources.getString(R.string.home)) {
                        startActivityAndClear(StaffDashBoardActivity::class.java)
                    } else if (headerList[groupPosition].menuName == resources.getString(R.string.referral_code)) {
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
                    } else if (headerList[groupPosition].menuName == resources.getString(R.string.create_case)) {
                        startActivity(CaseIDGenerateClass::class.java)
                    } /*else if (headerList.get(groupPosition).menuName.equals(getResources().getString(R.string.vendor))) {
                            startActivity(MyVendorVoacherListClass.class);
                        }*/ else if (headerList[groupPosition].menuName == resources.getString(R.string.spot_sell)) {
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
                    } else if (headerList[groupPosition].menuName == resources.getString(R.string.logout)) {
                        logout(resources.getString(R.string.logout_alert), "Logout")
                    }
                }
            }
            false
        }
        binding!!.expandableListView.setOnChildClickListener(OnChildClickListener { parent, v, groupPosition, childPosition, id ->
            if (childList[headerList[groupPosition]] != null) {
                val model = childList[headerList[groupPosition]]!![childPosition]
                if (model.url.length > 0) {
                    for (i in SharedPreferencesRepository.getDataManagerInstance().userPermission.indices) {
                        if (model.menuName == resources.getString(R.string.price)) {
                            Log.e("pricing", "pricing")
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "14",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(InPricingListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                            //  startActivity(InPricingListingActivity.class);
                        } else if (model.menuName == resources.getString(R.string.truck_book)) {
                            Log.d(
                                "Tpiddddddddd",
                                SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId
                            )
                            Log.d(
                                "Tviewwwwwwww",
                                "" + SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view
                            )
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "15",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(TruckBookListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                            // startActivity(TruckBookListingActivity.class);
                        } else if (model.menuName == resources.getString(R.string.labour_book)) {

                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "16",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(LabourBookListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                            //   startActivity(LabourBookListingActivity.class);
                        } else if (model.menuName == resources.getString(R.string.firstkanta_parchi)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "20",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(FirstkanthaParchiListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                            // startActivity(FirstkanthaParchiListingActivity.class);
                        } else if (model.menuName == resources.getString(R.string.f_quality_repots)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "28",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(FirstQualityReportListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                            // startActivity(FirstQualityReportListingActivity.class);
                        } else if (model.menuName == resources.getString(R.string.secoundkanta_parchi)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "20",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(SecoundkanthaParchiListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                            //  startActivity(SecoundkanthaParchiListingActivity.class);
                        } else if (model.menuName == resources.getString(R.string.s_quality_repots)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "28",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(SecoundQualityReportListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                            //  startActivity(SecoundQualityReportListingActivity.class);
                        } else if (model.menuName == resources.getString(R.string.gate_passs)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "19",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(GatePassListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                            //   startActivity(GatePassListingActivity.class);
                        } else if (model.menuName == resources.getString(R.string.my_convance)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "41",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(MyConveyanceListClass::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                            //startActivity(MyConveyanceListClass.class);
                        } else if (model.menuName == resources.getString(R.string.vendor)) {
                            startActivity(MyVendorVoacherListClass::class.java)
                        } else if (model.menuName == resources.getString(R.string.case_status_in)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "44",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(CaseStatusINListClass::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                            //startActivity(MyConveyanceListClass.class);
                        } else if (model.menuName == resources.getString(R.string.case_status_out)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "44",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(CaseStatusINListClass::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                            //startActivity(MyConveyanceListClass.class);
                        } else if (model.menuName == resources.getString(R.string.out_relase_book)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "24",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(OUTRelaseOrderListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                        } else if (model.menuName == resources.getString(R.string.out_deiverd_book)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "25",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(OUTDeliverdOrderListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                            // startActivity(TruckBookListingActivity.class);
                        } else if (model.menuName == resources.getString(R.string.out_truck_book)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "15",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(OUTTruckBookListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                            // startActivity(TruckBookListingActivity.class);
                        } else if (model.menuName == resources.getString(R.string.out_labour_book)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "16",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(OUTLabourBookListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                            //   startActivity(LabourBookListingActivity.class);
                        } else if (model.menuName == resources.getString(R.string.out_f_katha_book)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "20",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(OutFirstkanthaParchiListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                        } else if (model.menuName == resources.getString(R.string.out_f_quality_book)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "28",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(OutFirstQualityReportListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                        } else if (model.menuName == resources.getString(R.string.out_s_katha__book)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "20",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(OutSecoundkanthaParchiListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                        } else if (model.menuName == resources.getString(R.string.out_s_quality_book)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "28",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(OutSecoundQualityReportListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                        } else if (model.menuName == resources.getString(R.string.out_gatepass_book)) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "19",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivity(OutGatePassListingActivity::class.java)
                                    return@OnChildClickListener true
                                }
                            }
                        }
                    }
                }
            }
            false
        })
    }

     fun prepareMenuData(){
         var menuModel = MenuModel(
                 resources.getString(R.string.home),
                 true,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.dashboard_icon
         ) //Menu of Android Tutorial. No sub menus
         val menuModel1 = MenuModel(
                 resources.getString(R.string.referral_code),
                 true,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.refer_earn
         ) //Menu of Android Tutorial. No sub menus
         val menuModel2 = MenuModel(
                 resources.getString(R.string.select_language),
                 true,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.langauge
         ) //Menu of Android Tutorial. No sub menus
         headerList.add(menuModel)
         headerList.add(menuModel1)
         headerList.add(menuModel2)
         if (SharedPreferencesRepository.getDataManagerInstance().userPermission != null && SharedPreferencesRepository.getDataManagerInstance().userPermission.isNotEmpty()) {
             for (i in SharedPreferencesRepository.getDataManagerInstance().userPermission.indices) {
                 if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                 "11",
                                 ignoreCase = true
                         )
                 ) {
                     if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                         val menuModel3 = MenuModel(
                                 resources.getString(R.string.lead_generate),
                                 true,
                                 false,
                                 "https://www.journaldev.com/19226/python-fractions",
                                 R.drawable.lead_gernate_icon
                         ) //Menu of Android Tutorial. No sub menus
                         headerList.add(menuModel3)
                     }
                 }
                 if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                 "12",
                                 ignoreCase = true
                         )
                 ) {
                     if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                         val menuModel4 = MenuModel(
                                 resources.getString(R.string.create_case),
                                 true,
                                 false,
                                 "https://www.journaldev.com/19226/python-fractions",
                                 R.drawable.caseid
                         ) //Menu of Android Tutorial. No sub menus
                         headerList.add(menuModel4)
                     }
                 }
             }
         }
         if (!menuModel.hasChildren) {
             childList[menuModel] = null
         }
         var childModelsList: MutableList<MenuModel> = ArrayList()
         var childModel: MenuModel
         menuModel = MenuModel(
                 "IN",
                 true,
                 true,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.in_icon
         ) //Menu of Java Tutorials
         headerList.add(menuModel)
         childModel = MenuModel(
                 resources.getString(R.string.truck_book),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.truck
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.labour_book),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.labour
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.firstkanta_parchi),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.katha_photo
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.f_quality_repots),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.quaility
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.secoundkanta_parchi),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.katha_photo
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.s_quality_repots),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.quaility
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.gate_passs),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.gatepass
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
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.out_icon
         ) //Menu of Python Tutorials
         headerList.add(menuModel)
         childModel = MenuModel(
                 resources.getString(R.string.out_relase_book),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.truck
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.out_deiverd_book),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.labour
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.out_truck_book),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.truck
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.out_labour_book),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.labour
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.out_f_quality_book),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.quaility
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.out_f_katha_book),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.katha_photo
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.out_s_quality_book),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.quaility
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.out_s_katha__book),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.katha_photo
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.out_gatepass_book),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.gatepass
         )
         childModelsList.add(childModel)
         if (menuModel.hasChildren) {
             childList[menuModel] = childModelsList
         }
         menuModel = MenuModel(
                 "Voucher",
                 true,
                 true,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.vocher_icon
         ) //Menu of Java Tutorials
         headerList.add(menuModel)
         childModelsList = ArrayList()
         childModel = MenuModel(
                 resources.getString(R.string.my_convance),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.vocher_icon
         )
         childModelsList.add(childModel)
         childModel = MenuModel(
                 resources.getString(R.string.vendor),
                 false,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.vocher_icon
         )
         childModelsList.add(childModel)
         if (menuModel.hasChildren) {
             childList[menuModel] = childModelsList
         }
         val menuModel6 = MenuModel(
                 resources.getString(R.string.spot_sell),
                 true,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.deal_track
         ) //Menu of Android Tutorial. No sub menus
         val menuModel7 = MenuModel(
                 resources.getString(R.string.intantion_title),
                 true,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.purchase
         ) //Menu of Android Tutorial. No sub menus
         val menuModel8 = MenuModel(
                 "Customer Map",
                 true,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.heat_map
         ) //Menu of Android Tutorial. No sub menus
         val menuModel9 = MenuModel(
                 resources.getString(R.string.heat_map),
                 true,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.heat_map
         ) //Menu of Android Tutorial. No sub menus
         val menuMode21 = MenuModel(
                 resources.getString(R.string.fastcase),
                 true,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.heat_map
         ) //Menu of Android Tutorial. No sub menus
         val menuMode22 = MenuModel(
                 resources.getString(R.string.fastcase_list),
                 true,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.heat_map
         ) //Menu of Android Tutorial. No sub menus
         val menuMode20 = MenuModel(
                 resources.getString(R.string.logout),
                 true,
                 false,
                 "https://www.journaldev.com/19226/python-fractions",
                 R.drawable.out_icon
         ) //Menu of Android Tutorial. No sub menus
         //  headerList.add(menuModel5);
         headerList.add(menuModel6)
         headerList.add(menuModel7)
         headerList.add(menuModel8)
         headerList.add(menuModel9)
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

    private fun getdashboardData() {
        homeViewModel.getDashboardData()

        homeViewModel.homeDataResponse.observe(this){
            when(it){
                is NetworkResult.Error -> hideDialog()
                is NetworkResult.Loading -> showDialog()
                is NetworkResult.Success -> {
                    attendanceINOUTStatus = "" + it.data!!.getClock_status()
                    if (attendanceINOUTStatus.equals("1", ignoreCase = true)) {
                        OnOfffAttendance = true
                        binding!!.mainContent.mainHeader.attendanceOnOff.setImageResource(R.drawable.out)
                    } else {
                        binding!!.mainContent.mainHeader.attendanceOnOff.setImageResource(R.drawable.`in`)
                        OnOfffAttendance = false
                    }
                    binding!!.mainContent.incase.text = "" + it.data.getIn_case()
                    binding!!.mainContent.outcase.text = "" + it.data.getOut_case()
                    binding!!.mainContent.trotalAttend.text = "" + it.data.getAtten_month_data()
                    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                    val date = Date()
                    binding!!.mainContent.date.text = "" + formatter.format(date)
                    hideDialog()

                }

            }
        }


        //     font = Typeface.createFromAsset(getAssets(), "FontAwesome.ttf" );
        try {
            userDetails = SharedPreferencesRepository.getDataManagerInstance().user
            if (userDetails != null) {
                if (userDetails!!.getProfileImage() != null) {
                    Glide.with(binding!!.userProfileImage.context)
                        .load(Constants.IMAGE_BASE_URL + userDetails!!.getProfileImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.user_shape)
                        .circleCrop()
                        .into(binding!!.userProfileImage)
                }
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION
                )
                binding!!.userNameText.text =
                    userDetails!!.getFname() + " " + userDetails!!.getLname() + "(" + userDetails!!.getEmp_id() + ")"

                homeViewModel.getPermissions(userDetails!!.role_id, userDetails!!.level_id)

                homeViewModel.response.observe(this){
                    when(it){
                        is NetworkResult.Error -> hideDialog()
                        is NetworkResult.Loading -> showDialog()
                        is NetworkResult.Success -> {
                            if (it.data!!.userPermissionsResult != null || it.data.userPermissionsResult
                                            .isNotEmpty()
                            ) {
                                SharedPreferencesRepository.getDataManagerInstance()
                                        .saveUserPermissionData(it.data.getUserPermissionsResult())
                                //                    binding.menuList.setAdapter(new NavigationAdapter(getMenuList(), userDetails, StaffDashBoardActivity.this));
                                val navigationVLCAdapter = NavigationAdapter(
                                        menuList,
                                        userDetails,
                                        this@StaffDashBoardActivity
                                )
                                navigationVLCAdapter.setOnProfileClickInterface(this@StaffDashBoardActivity)
                                //                    binding.menuList.setAdapter(navigationVLCAdapter);
                            }
                            prepareMenuData()
                            populateExpandableList()
                        }
                    }
                }

            }


        } catch (e: Exception) {
            Log.e("Exception : on profile", e.message!!)
        }
    }

    private fun callServer() {
        var EmployeeImage = ""
        if (fileSelfie != null) {
            if (latitude != null && longitude != null) {
                EmployeeImage = "" + Utility.transferImageToBase64(fileSelfie)

                apiService.attendance(
                    AttendancePostData(
                        "" + latitude,
                        "" + longitude,
                        "" + attendanceINOUTStatus,
                        EmployeeImage
                    )
                ).doOnNext { body ->
                    Toast.makeText(
                        this@StaffDashBoardActivity,
                        body.getMessage(),
                        Toast.LENGTH_LONG
                    ).show()
                    fileSelfie = null
                    OnOfffAttendance =
                        if (body.getClock_status().equals("1", ignoreCase = true)) {
                            binding!!.mainContent.mainHeader.attendanceOnOff.setImageResource(R.drawable.out)
                            true
                        } else {
                            binding!!.mainContent.mainHeader.attendanceOnOff.setImageResource(R.drawable.`in`)
                            false
                        }
                }.subscribe()

            } else {
                Toast.makeText(
                    this@StaffDashBoardActivity,
                    "Enable Your Location Please!!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                this@StaffDashBoardActivity,
                "Take your selfie please!",
                Toast.LENGTH_LONG
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
            onImageSelected()
            // callProfileImageSelector(REQUEST_CAMERA);
        })
        clockInOut.setOnClickListener(View.OnClickListener { v: View? -> callServer() })
        if (!OnOfffAttendance) {
            attendanceINOUTStatus = "1"
            /*    binding.cardAttandance.setVisibility(View.VISIBLE);
            binding.WelcomeMsg.setVisibility(View.GONE);*/locationget()
            /*  binding.mainHeader.attendanceOnOff.setImageResource(R.drawable.out);
            OnOfffAttendance = true;*/
        } else {
            /*  binding.cardAttandance.setVisibility(View.VISIBLE);
            binding.WelcomeMsg.setVisibility(View.GONE);*/
            clockIn.setText(resources.getString(R.string.clock_out))
            clockInOut.setText(resources.getString(R.string.clock_out))
            locationget()
            attendanceINOUTStatus = "2"
            /* binding.mainHeader.attendanceOnOff.setImageResource(R.drawable.in);
            OnOfffAttendance = false;*/
        }
    }

    private fun callProfileImageSelector(requestCamera: Int) {
        options = Options.init()
            .setRequestCode(requestCamera) //Request code for activity results
            .setCount(1)
            .setFrontfacing(true)
            .setExcludeVideos(false)
            .setVideoDurationLimitinSeconds(60)
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientaion
            .setPath("/apnagodam/images") //Custom Path For media Storage
        Pix.start(this@StaffDashBoardActivity, options)
    }

    private fun locationget() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS()
        } else {
            location
        }
    }

    private fun OnGPS() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Enable GPS").setCancelable(false)
            .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            }).setNegativeButton("No", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.cancel()
                }
            })
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private val location: Unit
        private get() {
            if (ActivityCompat.checkSelfPermission(
                    this@StaffDashBoardActivity, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this@StaffDashBoardActivity, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION
                )
            } else {
                val locationGPS =
                    locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (locationGPS != null) {
                    val lat = locationGPS.latitude
                    val longi = locationGPS.longitude
                    latitude = lat.toString()
                    longitude = longi.toString()
                    //  Toast.makeText(this, "Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    fun singlePerm() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CALL_PHONE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
//Single Permission is granted
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.setData(Uri.parse("tel:+91-7733901154"))
                    startActivity(intent)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
// check for permanent denial of permission
                    if (response.isPermanentlyDenied()) {
                        openSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    fun openSettingsDialog() {
        val builder = AlertDialog.Builder(this@StaffDashBoardActivity)
        builder.setTitle("Permissions Required")
        builder.setMessage("Permission is required for using this app. Please enable them in app settings.")
        builder.setPositiveButton("Go to SETTINGS", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.cancel()
                showsettings()
            }
        })
        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.cancel()
            }
        })
        builder.show()
    }

    fun showsettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.setData(uri)
        startActivityForResult(intent, 101)
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
    override fun onDrawerOpened(drawerView: View) {}
    override fun onDrawerClosed(drawerView: View) {}
    override fun onDrawerStateChanged(newState: Int) {}

    /*   @Override
    public void onItemClick(View view, int position) {
        binding.drawerLayout.postDelayed(() -> {
            toggleDrawer();
            switch (position) {
                case 0:
                    break;
                case 1:
                    startActivityAndClear(StaffDashBoardActivity.class);
                    break;
                case 2:
                    //    startActivity(LanguageActivity.class);
                    String phone = SharedPreferencesRepository.getDataManagerInstance().getUser().getPhone();
                    String sharedUrl = "click here for download to Farmer App:- https://play.google.com/store/apps/details?id=com.apnagodam&hl=en&referrer=" + phone;
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sharedUrl);
                    sendIntent.setType("text/plain");
                    Intent.createChooser(sendIntent, "Share via");
                    startActivity(sendIntent);
                    Log.e("refer url :", "" + sharedUrl);
                    break;
                case 3:
                    startActivity(LanguageActivity.class);
                    break;
             */
    /*   case 4:
                    startActivity(CaseIDGenerateClass.class);
                    break;
                case 5:
                    startActivity(InPricingListingActivity.class);
                    break;
                case 6:
                    startActivity(TruckBookListingActivity.class);
                    break;
                case 7:
                    startActivity(LabourBookListingActivity.class);
                    break;
                case 8:
                    startActivity(FirstkanthaParchiListingActivity.class);
                    break;
                case 9:
                    startActivity(FirstQualityReportListingActivity.class);
                    break;
                case 10:
                    startActivity(SecoundkanthaParchiListingActivity.class);
                    break;
                case 11:
                    startActivity(SecoundQualityReportListingActivity.class);
                    break;
                case 12:
                    startActivity(GatePassListingActivity.class);
                    break;
*/
    /*
                case 4:
                    startActivity(SpotDealTrackListActivity.class);
                    break;
                case 5:
                    //call logout api
                    logout((getResources().getString(R.string.logout_alert)), "Logout");
                    break;
            }
        }, 100);
    }*/
    override fun onItemClick(view: View, position: Int) {
        binding!!.drawerLayout.postDelayed({
            toggleDrawer()
            if (position == 0) {
            }
            if (position == 1) {
                onBackPressed()
            }
            if (position == 2) {
                startActivity(LanguageActivity::class.java)
            }
            for (i in SharedPreferencesRepository.getDataManagerInstance().userPermission.indices) {

                if (position == 3) {
                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                            "11",
                            ignoreCase = true
                        )
                    ) {
                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                            startActivity(LeadGenerateClass::class.java)
                        }
                    }
                }
                if (position == 4) {
                }
                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                        "12",
                        ignoreCase = true
                    )
                ) {
                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                        startActivity(CaseIDGenerateClass::class.java)
                    }
                }
                if (position == 5) {
                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                            "13",
                            ignoreCase = true
                        )
                    ) {
                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                            startActivity(InPricingListingActivity::class.java)
                        }
                    }
                }
                if (position == 6) {
                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                            "15",
                            ignoreCase = true
                        )
                    ) {
                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                            startActivity(TruckBookListingActivity::class.java)
                        } else {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                    "16",
                                    ignoreCase = true
                                )
                            ) {
                                if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                    startActivityAndClear(LabourBookListingActivity::class.java)
                                }
                            }
                        }
                    }
                }
                if (position == 7) {
                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                            "20",
                            ignoreCase = true
                        )
                    ) {
                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                            startActivity(FirstkanthaParchiListingActivity::class.java)
                        }
                    } else {
                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                                "16",
                                ignoreCase = true
                            )
                        ) {
                            if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                                startActivityAndClear(LabourBookListingActivity::class.java)
                            }
                        }
                    }
                }
                if (position == 8) {
                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                            "20",
                            ignoreCase = true
                        )
                    ) {
                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                            startActivity(FirstkanthaParchiListingActivity::class.java)
                        }
                    }
                }
                if (position == 9) {
                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                            "18",
                            ignoreCase = true
                        )
                    ) {
                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                            startActivity(FirstQualityReportListingActivity::class.java)
                        }
                    }
                }
                if (position == 10) {
                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                            "20",
                            ignoreCase = true
                        )
                    ) {
                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                            startActivity(SecoundkanthaParchiListingActivity::class.java)
                        }
                    }
                }
                if (position == 11) {
                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                            "18",
                            ignoreCase = true
                        )
                    ) {
                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                            startActivity(SecoundQualityReportListingActivity::class.java)
                        }
                    }
                }
                if (position == 12) {
                    if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].permissionId.equals(
                            "19",
                            ignoreCase = true
                        )
                    ) {
                        if (SharedPreferencesRepository.getDataManagerInstance().userPermission[i].view == 1) {
                            // startActivity(FirstkanthaParchiListingActivity.class);
                        }
                    }
                }
            }
            if (position == 13) {
                logout(resources.getString(R.string.logout_alert), "Logout")
            }
            when (position) {
                0 -> {}
                1 -> startActivityAndClear(StaffDashBoardActivity::class.java)
                2 -> startActivity(LanguageActivity::class.java)
                3 -> startActivity(LeadGenerateClass::class.java)
                4 -> startActivity(CaseIDGenerateClass::class.java)
                5 -> startActivity(InPricingListingActivity::class.java)
                6 -> startActivity(TruckBookListingActivity::class.java)
                7 -> startActivity(LabourBookListingActivity::class.java)
                8 -> startActivity(FirstkanthaParchiListingActivity::class.java)
                9 -> startActivity(FirstQualityReportListingActivity::class.java)
                10 -> startActivity(SecoundkanthaParchiListingActivity::class.java)
                11 -> startActivity(SecoundQualityReportListingActivity::class.java)
                12 -> startActivity(GatePassListingActivity::class.java)
                13 ->                     //call logout api
                    logout(resources.getString(R.string.logout_alert), "Logout")
            }
        }, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CAMERA_PICTURE) {
                    val camFile: File
                    if (camUri != null) {
                        camFile = File(camUri!!.path)
                        fileSelfie = File(compressImage(camUri!!.path.toString()))
                        val uri = Uri.fromFile(fileSelfie)
                        selfieImage!!.setImageURI(uri)
                    }
                }
            }
            if (requestCode == REQUEST_CODE) {
                locationUtils!!.startLocationUpdates()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra(Pix.IMAGE_RESULTS)) {
                    ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    assert returnValue != null;
                    Log.e("getImageesValue", returnValue.get(0).toString());
                    if (requestCode == REQUEST_CAMERA) {
                        fileSelfie = new File(compressImage(returnValue.get(0).toString()));
                        Uri uri = Uri.fromFile(fileSelfie);
                        selfieImage.setImageURI(uri);
                    }
                }
            }
        }
        if (requestCode == REQUEST_CODE) {
            locationUtils.startLocationUpdates();
        }
    }*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, options)
                } else {
                    Toast.makeText(
                        this,
                        "Approve permissions to open Pix ImagePicker",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
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
}
