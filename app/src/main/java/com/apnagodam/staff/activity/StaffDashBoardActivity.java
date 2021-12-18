package com.apnagodam.staff.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.GlideApp;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.AttendancePostData;
import com.apnagodam.staff.Network.Response.AttendanceResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.caseid.CaseIDGenerateClass;
import com.apnagodam.staff.activity.casestatus.CaseStatusINListClass;
import com.apnagodam.staff.activity.convancy_voachar.MyConveyanceListClass;
import com.apnagodam.staff.activity.dispaldgeinventory.MyAddedDispladgeBagsListClass;
import com.apnagodam.staff.activity.errorlog.MyErrorLogListClass;
import com.apnagodam.staff.activity.feedbackemp.UploadEmpSuggestionClass;
import com.apnagodam.staff.activity.in.cctv.INCCTVListingActivity;
import com.apnagodam.staff.activity.in.first_kantaparchi.FirstkanthaParchiListingActivity;
import com.apnagodam.staff.activity.in.first_quality_reports.FirstQualityReportListingActivity;
import com.apnagodam.staff.activity.in.gatepass.GatePassListingActivity;
import com.apnagodam.staff.activity.in.ivr.INIVRListingActivity;
import com.apnagodam.staff.activity.in.labourbook.LabourBookListingActivity;
import com.apnagodam.staff.activity.in.pricing.InPricingListingActivity;
import com.apnagodam.staff.activity.in.pv.PVUploadClass;
import com.apnagodam.staff.activity.in.secound_kanthaparchi.SecoundkanthaParchiListingActivity;
import com.apnagodam.staff.activity.in.secound_quality_reports.SecoundQualityReportListingActivity;
import com.apnagodam.staff.activity.in.truckbook.TruckBookListingActivity;
import com.apnagodam.staff.activity.kra.MyKRAHistoryListClass;
import com.apnagodam.staff.activity.lead.LeadGenerateClass;
import com.apnagodam.staff.activity.out.cctv.OutCCTVListingActivity;
import com.apnagodam.staff.activity.out.deliveryorder.OUTDeliverdOrderListingActivity;
import com.apnagodam.staff.activity.out.f_katha_parchi.OutFirstkanthaParchiListingActivity;
import com.apnagodam.staff.activity.out.f_quailty_report.OutFirstQualityReportListingActivity;
import com.apnagodam.staff.activity.out.gatepass.OutGatePassListingActivity;
import com.apnagodam.staff.activity.out.ivr.OutIVRListingActivity;
import com.apnagodam.staff.activity.out.labourbook.OUTLabourBookListingActivity;
import com.apnagodam.staff.activity.out.pv.OUTPVUploadClass;
import com.apnagodam.staff.activity.out.releaseorder.OUTRelaseOrderListingActivity;
import com.apnagodam.staff.activity.out.s_katha_parchi.OutSecoundkanthaParchiListingActivity;
import com.apnagodam.staff.activity.out.s_quaility_report.OutSecoundQualityReportListingActivity;
import com.apnagodam.staff.activity.out.truckbook.OUTTruckBookListingActivity;
import com.apnagodam.staff.activity.remoteaudit.AllAsserstListClass;
import com.apnagodam.staff.activity.vendorPanel.MyVendorVoacherListClass;
import com.apnagodam.staff.adapter.ExpandableListAdapter;
import com.apnagodam.staff.adapter.NavigationAdapter;
import com.apnagodam.staff.databinding.StaffDashboardBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.interfaces.OnProfileClickListener;
import com.apnagodam.staff.module.AllUserPermissionsResultListResponse;
import com.apnagodam.staff.module.DashBoardData;
import com.apnagodam.staff.module.MenuModel;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.LocationUtils;
import com.apnagodam.staff.utils.RecyclerItemClickListener;
import com.apnagodam.staff.utils.Utility;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StaffDashBoardActivity extends BaseActivity<StaffDashboardBinding>
        implements View.OnClickListener, OnProfileClickListener, RecyclerItemClickListener.OnItemClickListener, AdapterView.OnItemSelectedListener,
        DrawerLayout.DrawerListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    boolean doubleBackToExitPressedOnce = false;
    // for location
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    private String latitude, longitude;
    boolean OnOfffAttendance;
    String attendanceINOUTStatus = "2";
    public File fileSelfie;
    UserDetails userDetails;
    ImageView selfieImage;
    Options options;
    // for location
    private LocationUtils locationUtils;
    private int REQUEST_CODE = 1000;
    GoogleApiClient mGoogleApiClient;
    ExpandableListAdapter expandableListAdapter;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.staff_dashboard;
    }

    @Override
    protected void setUp() {
        // setBackBtn(binding.mainHeader.toolbar);
        setSupportActionBar(binding.mainContent.mainHeader.toolbar);
        userDetails = SharedPreferencesRepository.getDataManagerInstance().getUser();
        //     font = Typeface.createFromAsset(getAssets(), "FontAwesome.ttf" );
        getAllPermission();
        if (userDetails.getProfileImage() != null) {
            GlideApp.with(binding.userProfileImage.getContext())
                    .load(Constants.IMAGE_BASE_URL + userDetails.getProfileImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.user_shape)
                    .circleCrop()
                    .into(binding.userProfileImage);
        }
        binding.userNameText.setText(userDetails.getFname() + " " + userDetails.getLname() + "(" + userDetails.getEmp_id() + ")");
        binding.drawerLayout.addDrawerListener(this);
        binding.profileId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaffDashBoardActivity.this, StaffProfileActivity.class);
                startActivity(intent);
            }
        });
        binding.mainContent.mainHeader.toogleIcon.setOnClickListener(this);
        toggle = new ActionBarDrawerToggle(StaffDashBoardActivity.this, binding.drawerLayout, toolbar,
                0, 0);

        toggle.setDrawerIndicatorEnabled(false);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        binding.menuList.setLayoutManager(new LinearLayoutManager(this));
//        binding.menuList.addOnItemTouchListener(new RecyclerItemClickListener(StaffDashBoardActivity.this, this));
        binding.mainContent.mainHeader.tvIvr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singlePerm();
            }
        });
        binding.mainContent.mainHeader.attendanceOnOff.setOnClickListener(v -> TakeAttendance(OnOfffAttendance));
        binding.mainContent.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fileSelfie = null;
                    binding.mainContent.selfieImage.setImageBitmap(null);
                    binding.mainContent.cardAttandance.setVisibility(View.GONE);
                    binding.mainContent.WelcomeMsg.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        binding.mainContent.UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageSelected();
            }
        });
        binding.mainContent.clockInOut.setOnClickListener(v -> callServer());
        //    getAttendanceStatus();
        getdashboardData();
        locationUtils = new LocationUtils(getActivity(), 1000, 2000, (location, address) -> {
            //   showToast(location.getLatitude() + " : " + location.getLongitude());
            Log.d("location", "" + location.getLatitude() + "," + "" + location.getLongitude());
            latitude = "" + location.getLatitude();
            longitude = "" + location.getLongitude();
            LocationUtils.LocationAddress locationAddress = new LocationUtils.LocationAddress();
            String geolocation = String.valueOf(locationAddress.getAddressFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), StaffDashBoardActivity.this));
            Log.e("geolocationLogin", " " + geolocation);
        }, REQUEST_CODE);
        //set google api client for hint request
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
    }

    private void populateExpandableList() {
        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        binding.expandableListView.setAdapter(expandableListAdapter);
        binding.expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
                       /* WebView webView = findViewById(R.id.webView);
                        webView.loadUrl(headerList.get(groupPosition).url);
                        onBackPressed();*/
                        Log.e("groupclick", "gdddddd");
                        if (headerList.get(groupPosition).menuName.equals(getResources().getString(R.string.home))) {
                            startActivityAndClear(StaffDashBoardActivity.class);
                        } else if (headerList.get(groupPosition).menuName.equals(getResources().getString(R.string.referral_code))) {
                            String phone = SharedPreferencesRepository.getDataManagerInstance().getUser().getPhone();
                            String sharedUrl = "click here for download to Farmer App:- https://play.google.com/store/apps/details?id=com.apnagodam&hl=en&referrer=" + phone;
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, sharedUrl);
                            sendIntent.setType("text/plain");
                            Intent.createChooser(sendIntent, "Share via");
                            startActivity(sendIntent);
                            Log.e("refer url :", "" + sharedUrl);
                        } else if (headerList.get(groupPosition).menuName.equals(getResources().getString(R.string.select_language))) {
                            startActivity(LanguageActivity.class);
                        }/* else if (headerList.get(groupPosition).menuName.equals(getResources().getString(R.string.lead_generate))) {
                            startActivity(LeadGenerateClass.class);
                        }*/ else if (headerList.get(groupPosition).menuName.equals("Transaction Request")) {
                            for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("12")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getEdit() == 1) {
                                        startActivity(CaseIDGenerateClass.class);
                                    }
                                }
                            }
                        }/*else if (headerList.get(groupPosition).menuName.equals(getResources().getString(R.string.vendor))) {
                            startActivity(MyVendorVoacherListClass.class);
                        }*/ else if (headerList.get(groupPosition).menuName.equals("Displedge Request")) {
                            for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("93")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(MyAddedDispladgeBagsListClass.class);
                                    }
                                }
                            }
                        } else if (headerList.get(groupPosition).menuName.equals(getResources().getString(R.string.my_error_logs))) {
                            for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("43")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(MyErrorLogListClass.class);
                                    }
                                }
                            }
                        } else if (headerList.get(groupPosition).menuName.equals("Feedback")) {
                            startActivity(UploadEmpSuggestionClass.class);
                        }else if (headerList.get(groupPosition).menuName.equals("KRA")) {
                            startActivity(MyKRAHistoryListClass.class);
                        } else if (headerList.get(groupPosition).menuName.equals(getResources().getString(R.string.spot_sell))) {
                            startActivity(SpotDealTrackListActivity.class);
                        } else if (headerList.get(groupPosition).menuName.equals(getResources().getString(R.string.intantion_title))) {
                            startActivity(IntantionApprovalListClass.class);
                        } else if (headerList.get(groupPosition).menuName.equals(getResources().getString(R.string.heat_map))) {
                            for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("91")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(StateMapActivity.class);
                                    }
                                }
                            }
                        } else if (headerList.get(groupPosition).menuName.equals("Remote Audit")) {
                            for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("91")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(AllAsserstListClass.class);
                                    }
                                }
                            }
                        } else if (headerList.get(groupPosition).menuName.equals("Customer Map")) {
                            startActivity(MobileNumberMapActivity.class);
                        } else if (headerList.get(groupPosition).menuName.equals(getResources().getString(R.string.logout))) {
                            logout((getResources().getString(R.string.logout_alert)), "Logout");
                        }

                    }
                }
                return false;
            }
        });

        binding.expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    if (model.url.length() > 0) {
                        for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                            if (model.menuName.equals(getResources().getString(R.string.price))) {
                                Log.e("pricing", "pricing");
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("14")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(InPricingListingActivity.class);
                                    }
                                }
                                //  startActivity(InPricingListingActivity.class);
                            } else if (model.menuName.equals(getResources().getString(R.string.truck_book))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("15")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(TruckBookListingActivity.class);
                                    }
                                }
                                // startActivity(TruckBookListingActivity.class);
                            } else if (model.menuName.equals(getResources().getString(R.string.labour_book))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("16")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(LabourBookListingActivity.class);
                                    }
                                }
                                //   startActivity(LabourBookListingActivity.class);
                            } else if (model.menuName.equals(getResources().getString(R.string.firstkanta_parchi))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("20")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(FirstkanthaParchiListingActivity.class);
                                    }
                                }
                                // startActivity(FirstkanthaParchiListingActivity.class);
                            } else if (model.menuName.equals(getResources().getString(R.string.f_quality_repots))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("28")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(FirstQualityReportListingActivity.class);
                                    }
                                }
                                // startActivity(FirstQualityReportListingActivity.class);
                            } else if (model.menuName.equals("PV Sheet")) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("42")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(PVUploadClass.class);
                                    }
                                }
                                //  startActivity(SecoundkanthaParchiListingActivity.class);
                            } else if (model.menuName.equals(getResources().getString(R.string.secoundkanta_parchi))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("20")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(SecoundkanthaParchiListingActivity.class);
                                    }
                                }
                                //  startActivity(SecoundkanthaParchiListingActivity.class);
                            } else if (model.menuName.equals(getResources().getString(R.string.s_quality_repots))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("28")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(SecoundQualityReportListingActivity.class);
                                    }
                                }
                                //  startActivity(SecoundQualityReportListingActivity.class);
                            } else if (model.menuName.equals("CCTV")) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("33")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(INCCTVListingActivity.class);
                                    }
                                }
                                //  startActivity(SecoundQualityReportListingActivity.class);
                            } else if (model.menuName.equals("IVR Tagging")) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("37")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(INIVRListingActivity.class);
                                    }
                                }
                                //  startActivity(SecoundQualityReportListingActivity.class);
                            } else if (model.menuName.equals(getResources().getString(R.string.gate_passs))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("19")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(GatePassListingActivity.class);
                                    }
                                }
                                //   startActivity(GatePassListingActivity.class);
                            } else if (model.menuName.equals(getResources().getString(R.string.my_convance))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("41")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(MyConveyanceListClass.class);
                                    }
                                }
                                //startActivity(MyConveyanceListClass.class);
                            } else if (model.menuName.equals(getResources().getString(R.string.vendor))) {
                                startActivity(MyVendorVoacherListClass.class);
                            } else if (model.menuName.equals(getResources().getString(R.string.case_status_in))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("44")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(CaseStatusINListClass.class);
                                    }
                                }
                                //startActivity(MyConveyanceListClass.class);
                            } else if (model.menuName.equals(getResources().getString(R.string.case_status_out))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("44")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(CaseStatusINListClass.class);
                                    }
                                }
                                //startActivity(MyConveyanceListClass.class);
                            }
                            /* ******************************out case module*****************************/
                            else if (model.menuName.equals(getResources().getString(R.string.out_relase_book))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("24")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(OUTRelaseOrderListingActivity.class);
                                    }
                                }
                            } else if (model.menuName.equals(getResources().getString(R.string.out_deiverd_book))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("25")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(OUTDeliverdOrderListingActivity.class);
                                    }
                                }
                                // startActivity(TruckBookListingActivity.class);
                            } else if (model.menuName.equals(getResources().getString(R.string.out_truck_book))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("15")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(OUTTruckBookListingActivity.class);
                                    }
                                }
                                // startActivity(TruckBookListingActivity.class);
                            } else if (model.menuName.equals(getResources().getString(R.string.out_labour_book))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("16")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(OUTLabourBookListingActivity.class);
                                    }
                                }
                                //   startActivity(LabourBookListingActivity.class);
                            } else if (model.menuName.equals(getResources().getString(R.string.out_f_katha_book))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("20")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(OutFirstkanthaParchiListingActivity.class);
                                    }
                                }
                            } else if (model.menuName.equals(getResources().getString(R.string.out_f_quality_book))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("28")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(OutFirstQualityReportListingActivity.class);
                                    }
                                }
                            } else if (model.menuName.equals(getResources().getString(R.string.out_s_katha__book))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("20")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(OutSecoundkanthaParchiListingActivity.class);
                                    }
                                }
                            } else if (model.menuName.equals("OUT CCTV")) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("33")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(OutCCTVListingActivity.class);
                                    }
                                }
                            } else if (model.menuName.equals("OUT IVR")) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("37")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(OutIVRListingActivity.class);
                                    }
                                }
                            } else if (model.menuName.equals(getResources().getString(R.string.out_s_quality_book))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("28")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(OutSecoundQualityReportListingActivity.class);
                                    }
                                }
                            } else if (model.menuName.equals(getResources().getString(R.string.out_gatepass_book))) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("19")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(OutGatePassListingActivity.class);
                                    }
                                }
                            } else if (model.menuName.equals("Out PV Sheet")) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("42")) {
                                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                        startActivity(OUTPVUploadClass.class);
                                    }
                                }
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    private void prepareMenuData() {
        // MenuModel menuModel0 = new MenuModel("Cowin Vaccine", true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.dashboard_icon); //Menu of Android Tutorial. No sub menus
        MenuModel menuModel = new MenuModel(getResources().getString(R.string.home), true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.dashboard_icon); //Menu of Android Tutorial. No sub menus
        MenuModel menuModel1 = new MenuModel(getResources().getString(R.string.referral_code), true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.refer_earn); //Menu of Android Tutorial. No sub menus
       // MenuModel menuModel2 = new MenuModel(getResources().getString(R.string.select_language), true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.langauge); //Menu of Android Tutorial. No sub menus
        //headerList.add(menuModel0);
        headerList.add(menuModel);
        headerList.add(menuModel1);
       // headerList.add(menuModel2);
        for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
            /*if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("11")) {
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                    MenuModel menuModel3 = new MenuModel(getResources().getString(R.string.lead_generate), true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.lead_gernate_icon); //Menu of Android Tutorial. No sub menus
                    headerList.add(menuModel3);
                }
            }*/
            if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("12")) {
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                    MenuModel menuModel4 = new MenuModel("Transaction Request", true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.caseid); //Menu of Android Tutorial. No sub menus
                    headerList.add(menuModel4);
                }
            }
        }
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }
        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel;
        menuModel = new MenuModel("IN", true, true, "https://www.journaldev.com/19226/python-fractions", R.drawable.in_icon); //Menu of Java Tutorials
        headerList.add(menuModel);
        /*childModel = new MenuModel(getResources().getString(R.string.price), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.pricing_icon);
        childModelsList.add(childModel);*/

        childModel = new MenuModel(getResources().getString(R.string.truck_book), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.truck);
        childModelsList.add(childModel);

        childModel = new MenuModel(getResources().getString(R.string.labour_book), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.labour);
        childModelsList.add(childModel);

        childModel = new MenuModel(getResources().getString(R.string.firstkanta_parchi), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.katha_photo);
        childModelsList.add(childModel);

        childModel = new MenuModel(getResources().getString(R.string.f_quality_repots), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.quaility);
        childModelsList.add(childModel);
        childModel = new MenuModel("PV Sheet", false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.quaility);
        childModelsList.add(childModel);
        childModel = new MenuModel(getResources().getString(R.string.secoundkanta_parchi), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.katha_photo);
        childModelsList.add(childModel);
        childModel = new MenuModel(getResources().getString(R.string.s_quality_repots), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.quaility);
        childModelsList.add(childModel);
        childModel = new MenuModel("CCTV", false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.quaility);
        childModelsList.add(childModel);
        childModel = new MenuModel("IVR Tagging", false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.quaility);
        childModelsList.add(childModel);

        childModel = new MenuModel(getResources().getString(R.string.gate_passs), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.gatepass);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            Log.d("API123", "here");
            childList.put(menuModel, childModelsList);
        }

        childModelsList = new ArrayList<>();
        menuModel = new MenuModel("OUT", true, true, "https://www.journaldev.com/19226/python-fractions", R.drawable.out_icon); //Menu of Python Tutorials
        headerList.add(menuModel);
        childModel = new MenuModel(getResources().getString(R.string.out_relase_book), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.truck);
        childModelsList.add(childModel);
        childModel = new MenuModel(getResources().getString(R.string.out_deiverd_book), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.labour);
        childModelsList.add(childModel);
        childModel = new MenuModel(getResources().getString(R.string.out_truck_book), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.truck);
        childModelsList.add(childModel);
        childModel = new MenuModel(getResources().getString(R.string.out_labour_book), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.labour);
        childModelsList.add(childModel);
        childModel = new MenuModel(getResources().getString(R.string.out_f_quality_book), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.quaility);
        childModelsList.add(childModel);
        childModel = new MenuModel(getResources().getString(R.string.out_f_katha_book), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.katha_photo);
        childModelsList.add(childModel);
        childModel = new MenuModel(getResources().getString(R.string.out_s_quality_book), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.quaility);
        childModelsList.add(childModel);
        childModel = new MenuModel(getResources().getString(R.string.out_s_katha__book), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.katha_photo);
        childModelsList.add(childModel);
        childModel = new MenuModel("OUT CCTV", false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.quaility);
        childModelsList.add(childModel);
        childModel = new MenuModel("OUT IVR", false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.quaility);
        childModelsList.add(childModel);
        childModel = new MenuModel(getResources().getString(R.string.out_gatepass_book), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.gatepass);
        childModelsList.add(childModel);
        childModel = new MenuModel("Out PV Sheet", false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.quaility);
        childModelsList.add(childModel);
        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }
        menuModel = new MenuModel("Voucher", true, true, "https://www.journaldev.com/19226/python-fractions", R.drawable.vocher_icon); //Menu of Java Tutorials
        headerList.add(menuModel);
        childModelsList = new ArrayList<>();
        childModel = new MenuModel(getResources().getString(R.string.my_convance), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.vocher_icon);
        childModelsList.add(childModel);
     /*   childModel = new MenuModel(getResources().getString(R.string.other_exp), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.ic_baseline_notifications_24);
        childModelsList.add(childModel);*/
        childModel = new MenuModel(getResources().getString(R.string.vendor), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.vocher_icon);
        childModelsList.add(childModel);
        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


       /* menuModel = new MenuModel(getResources().getString(R.string.case_status), true, true, "https://www.journaldev.com/19226/python-fractions", R.drawable.case_status_icon); //Menu of Java Tutorials
        headerList.add(menuModel);
        childModelsList = new ArrayList<>();
        childModel = new MenuModel(getResources().getString(R.string.case_status_in), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.vocher_icon);
        childModelsList.add(childModel);
        *//*childModel = new MenuModel(getResources().getString(R.string.other_exp), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.ic_baseline_notifications_24);
        childModelsList.add(childModel);*//*
        childModel = new MenuModel(getResources().getString(R.string.case_status_out), false, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.vocher_icon);
        childModelsList.add(childModel);
        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }*/
        MenuModel menuModel6 = new MenuModel("Displedge Request", true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.displadge_24); //Menu of Android Tutorial. No sub menus
        // MenuModel menuModel5 = new MenuModel(getResources().getString(R.string.vendor), true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.ic_baseline_settings_24); //Menu of Android Tutorial. No sub menus
        MenuModel menuModel7 = new MenuModel(getResources().getString(R.string.my_error_logs), true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.ic_baseline_error_24); //Menu of Android Tutorial. No sub menus
       // MenuModel menuModel23 = new MenuModel("Remote Audit", true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.remote_24); //Menu of Android Tutorial. No sub menus
        MenuModel menuModel22 = new MenuModel("Feedback", true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.ic_baseline_feedback_24); //Menu of Android Tutorial. No sub menus
       // MenuModel menuModel24 = new MenuModel("KRA", true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.ic_baseline_feedback_24); //Menu of Android Tutorial. No sub menus
      /*  MenuModel menuModel8 = new MenuModel(getResources().getString(R.string.spot_sell), true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.deal_track); //Menu of Android Tutorial. No sub menus
        MenuModel menuModel9 = new MenuModel(getResources().getString(R.string.intantion_title), true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.purchase); //Menu of Android Tutorial. No sub menus*/
        MenuModel menuModel10 = new MenuModel("Customer Map", true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.heat_map); //Menu of Android Tutorial. No sub menus
        MenuModel menuMode20 = new MenuModel(getResources().getString(R.string.heat_map), true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.heat_map); //Menu of Android Tutorial. No sub menus
        MenuModel menuMode21 = new MenuModel(getResources().getString(R.string.logout), true, false, "https://www.journaldev.com/19226/python-fractions", R.drawable.out_icon); //Menu of Android Tutorial. No sub menus
        //  headerList.add(menuModel5);
        headerList.add(menuModel6);
        headerList.add(menuModel7);
        headerList.add(menuModel22);
       /* headerList.add(menuModel23);
        headerList.add(menuModel24);*/
       /* headerList.add(menuModel8);
        headerList.add(menuModel9);*/
        headerList.add(menuModel10);
        headerList.add(menuMode20);
        headerList.add(menuMode21);
    }

    @Override
    public void onResume() {
        super.onResume();
        //  binding.mainContent.shimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        //   binding.mainContent.shimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    private void getAllPermission() {
        apiService.getPermission(userDetails.getRole_id(), userDetails.getLevel_id()).enqueue(new NetworkCallback<AllUserPermissionsResultListResponse>(getActivity()) {
            @Override
            protected void onSuccess(AllUserPermissionsResultListResponse body) {
                if (body.getUserPermissionsResult() == null || body.getUserPermissionsResult().isEmpty()) {
                } else {
                    SharedPreferencesRepository.getDataManagerInstance().saveUserPermissionData(body.getUserPermissionsResult());
//                    binding.menuList.setAdapter(new NavigationAdapter(getMenuList(), userDetails, StaffDashBoardActivity.this));
                    NavigationAdapter navigationVLCAdapter = new NavigationAdapter(getMenuList(), userDetails, StaffDashBoardActivity.this);
                    navigationVLCAdapter.setOnProfileClickInterface(StaffDashBoardActivity.this);
//                    binding.menuList.setAdapter(navigationVLCAdapter);
                    prepareMenuData();
                    populateExpandableList();
                }
            }
        });
    }

    private void getAttendanceStatus() {
        apiService.getattendanceStatus().enqueue(new NetworkCallback<AttendanceResponse>(getActivity()) {
            @Override
            protected void onSuccess(AttendanceResponse body) {
                // Toast.makeText(getApplicationContext(),body.getMessage(),Toast.LENGTH_SHORT).show();
                attendanceINOUTStatus = "" + body.getClock_status();
                if (attendanceINOUTStatus.equalsIgnoreCase("1")) {
                    OnOfffAttendance = true;
                    binding.mainContent.mainHeader.attendanceOnOff.setImageResource(R.drawable.out);
                } else {
                    binding.mainContent.mainHeader.attendanceOnOff.setImageResource(R.drawable.in);
                    OnOfffAttendance = false;
                }
                getdashboardData();
            }
        });
    }

    private void getdashboardData() {
        apiService.getDashboardData().enqueue(new NetworkCallback<DashBoardData>(getActivity()) {
            @Override
            protected void onSuccess(DashBoardData body) {
                if (body != null) {
                    attendanceINOUTStatus = "" + body.getClock_status();
                    if (attendanceINOUTStatus.equalsIgnoreCase("1")) {
                        OnOfffAttendance = true;
                        binding.mainContent.mainHeader.attendanceOnOff.setImageResource(R.drawable.out);
                    } else {
                        binding.mainContent.mainHeader.attendanceOnOff.setImageResource(R.drawable.in);
                        OnOfffAttendance = false;
                    }
                    binding.mainContent.incase.setText("" + body.getIn_case());
                    binding.mainContent.outcase.setText("" + body.getOut_case());
                    binding.mainContent.trotalAttend.setText("" + body.getAtten_month_data());
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    binding.mainContent.date.setText("" + formatter.format(date));
                    // stop animating Shimmer and hide the layout
                    // binding.mainContent.shimmerViewContainer.stopShimmerAnimation();
                    // binding.mainContent.shimmerViewContainer.setVisibility(View.GONE);
                }
            }
        });
    }

    private void callServer() {
        String EmployeeImage = "";
        /*HashMap<String,String>map =new HashMap<>();
        map.put("lat",""+latitude);
        map.put("long",""+longitude);
        map.put("clock_status",""+attendanceINOUTStatus);
        map.put("image", Utility.transferImageToBase64(file));*/
        if (fileSelfie != null) {
            if (latitude != null && longitude != null) {
                EmployeeImage = "" + Utility.transferImageToBase64(fileSelfie);
                apiService.attendance(new AttendancePostData("" + latitude, "" + longitude, "" + attendanceINOUTStatus, EmployeeImage)).enqueue(new NetworkCallback<AttendanceResponse>(getActivity()) {
                    @Override
                    protected void onSuccess(AttendanceResponse body) {
                        Toast.makeText(StaffDashBoardActivity.this, body.getMessage(), Toast.LENGTH_LONG).show();
                        fileSelfie = null;
                        startActivityAndClear(StaffDashBoardActivity.class);
                        if (body.getClock_status().equalsIgnoreCase("1")) {
                            binding.mainContent.mainHeader.attendanceOnOff.setImageResource(R.drawable.out);
                            OnOfffAttendance = true;
                        } else {
                            binding.mainContent.mainHeader.attendanceOnOff.setImageResource(R.drawable.in);
                            OnOfffAttendance = false;
                        }
                    }
                });
            } else {
                Toast.makeText(StaffDashBoardActivity.this, "Enable Your Location Please!!", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(StaffDashBoardActivity.this, "Take your selfie please!", Toast.LENGTH_LONG).show();
        }

    }

    private void TakeAttendance(boolean OnOfffAttendance) {
        OnOfffAttendance = !OnOfffAttendance;
        setFunctional(OnOfffAttendance);
    }

    private void setFunctional(boolean flag) {
        BottomSheetDialog dialog = new BottomSheetDialog(StaffDashBoardActivity.this);
        dialog.setContentView(R.layout.dilog_attedance_bottom);
        MaterialButton clockInOut = (MaterialButton) dialog.findViewById(R.id.clock_in_out);
        MaterialButton close = (MaterialButton) dialog.findViewById(R.id.close);
        TextView clockIn = (TextView) dialog.findViewById(R.id.clockIn);
        LinearLayout UploadImage = (LinearLayout) dialog.findViewById(R.id.UploadImage);
        selfieImage = (ImageView) dialog.findViewById(R.id.selfieImage);
        dialog.setCancelable(true);
        dialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fileSelfie = null;
                    selfieImage.setImageBitmap(null);
                    dialog.cancel();
                    dialog.dismiss();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        });
        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageSelected();
                // callProfileImageSelector(REQUEST_CAMERA);
            }
        });
        clockInOut.setOnClickListener(v -> callServer());
        if (!OnOfffAttendance) {
            attendanceINOUTStatus = "1";
        /*    binding.cardAttandance.setVisibility(View.VISIBLE);
            binding.WelcomeMsg.setVisibility(View.GONE);*/
            locationget();
          /*  binding.mainHeader.attendanceOnOff.setImageResource(R.drawable.out);
            OnOfffAttendance = true;*/
        } else {
          /*  binding.cardAttandance.setVisibility(View.VISIBLE);
            binding.WelcomeMsg.setVisibility(View.GONE);*/
            clockIn.setText(getResources().getString(R.string.clock_out));
            clockInOut.setText(getResources().getString(R.string.clock_out));
            locationget();
            attendanceINOUTStatus = "2";
           /* binding.mainHeader.attendanceOnOff.setImageResource(R.drawable.in);
            OnOfffAttendance = false;*/
        }
    }

    private void callProfileImageSelector(int requestCamera) {
        options = Options.init()
                .setRequestCode(requestCamera)                                           //Request code for activity results
                .setCount(1)
                .setFrontfacing(true)
                .setExcludeVideos(false)
                .setVideoDurationLimitinSeconds(60)
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/apnagodam/images");                                       //Custom Path For media Storage
        Pix.start(StaffDashBoardActivity.this, options);
    }

    private void locationget() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                StaffDashBoardActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                StaffDashBoardActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                //  Toast.makeText(this, "Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void singlePerm() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
//Single Permission is granted
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:+91-7733901154"));
                        startActivity(intent);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
// check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public void openSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StaffDashBoardActivity.this);
        builder.setTitle("Permissions Required");
        builder.setMessage("Permission is required for using this app. Please enable them in app settings.");
        builder.setPositiveButton("Go to SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                showsettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void showsettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toogleIcon:
                toggleDrawer();
                break;
        }
    }

    public void toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.exist_app, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 3000);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

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
             *//*   case 4:
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
*//*
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

    @Override
    public void onItemClick(View view, int position) {
        binding.drawerLayout.postDelayed(() -> {
            toggleDrawer();
            if (position == 0) {
            }
            if (position == 1) {
                startActivityAndClear(StaffDashBoardActivity.class);
            }
            if (position == 2) {
                startActivity(LanguageActivity.class);
            }
            for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                if (position == 3) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("11")) {
                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                            startActivity(LeadGenerateClass.class);
                        }
                    }
                }
                if (position == 4) {

                }
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("12")) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                        startActivity(CaseIDGenerateClass.class);
                    }
                }
                if (position == 5) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("13")) {
                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                            startActivity(InPricingListingActivity.class);
                        }
                    }
                }
                if (position == 6) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("15")) {
                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                            startActivity(TruckBookListingActivity.class);
                        } else {
                            if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("16")) {
                                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                    startActivityAndClear(LabourBookListingActivity.class);
                                }
                            }
                        }
                    }
                }
                if (position == 7) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("20")) {
                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                            startActivity(FirstkanthaParchiListingActivity.class);
                        }
                    } else {
                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("16")) {
                            if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                                startActivityAndClear(LabourBookListingActivity.class);
                            }
                        }
                    }
                }
                if (position == 8) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("20")) {
                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                            startActivity(FirstkanthaParchiListingActivity.class);
                        }
                    }
                }
                if (position == 9) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("18")) {
                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                            startActivity(FirstQualityReportListingActivity.class);
                        }
                    }
                }
                if (position == 10) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("20")) {
                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                            startActivity(SecoundkanthaParchiListingActivity.class);
                        }
                    }
                }
                if (position == 11) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("18")) {
                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                            startActivity(SecoundQualityReportListingActivity.class);
                        }
                    }
                }
                if (position == 12) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("19")) {
                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                            // startActivity(FirstkanthaParchiListingActivity.class);
                        }
                    }
                }
            }
            if (position == 13) {
                logout((getResources().getString(R.string.logout_alert)), "Logout");
            }
            switch (position) {
                case 0:
                    break;
                case 1:
                    startActivityAndClear(StaffDashBoardActivity.class);
                    break;
                case 2:
                    startActivity(LanguageActivity.class);
                 /* String phone = SharedPreferencesRepository.getDataManagerInstance().getUser().getPhone();
                    String sharedUrl = "click here for download to Farmer App:- https://play.google.com/store/apps/details?id=com.apnagodam&hl=en&referrer=" + phone;
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sharedUrl);
                    sendIntent.setType("text/plain");
                    Intent.createChooser(sendIntent, "Share via");
                    startActivity(sendIntent);
                    Log.e("refer url :", "" + sharedUrl);*/
                    break;
                case 3:
                    startActivity(LeadGenerateClass.class);
                    break;
                case 4:
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

                case 13:
                    //call logout api
                    logout((getResources().getString(R.string.logout_alert)), "Logout");
                    break;
            }
        }, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_CAMERA_PICTURE) {
                    File camFile;
                    if (this.camUri != null) {
                        camFile = new File(this.camUri.getPath());
                        fileSelfie = new File(compressImage(this.camUri.getPath().toString()));
                        Uri uri = Uri.fromFile(fileSelfie);
                        selfieImage.setImageURI(uri);
                    }
                }
            }
            if (requestCode == REQUEST_CODE) {
                locationUtils.startLocationUpdates();
            }
        } catch (Exception e) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, options);
                } else {
                    Toast.makeText(this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    @Override
    public void onProfileImgClick() {
        Intent intent = new Intent(this, StaffProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
