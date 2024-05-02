package com.apnagodam.staff.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.HomeViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.LoginActivity.Companion.TAG
import com.apnagodam.staff.databinding.ActivitySplashBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.utils.Utility
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@AndroidEntryPoint
class SplashActivity() : BaseActivity<ActivitySplashBinding?>() {
    var market_uri = "https://play.google.com/store/apps/details?id=com.apnagodam.staff&hl=en"

    //    private lateinit var inAppUpdate: InAppUpdate
    val homeViewModel by viewModels<HomeViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }

    suspend fun changeLanguage(): Flow<Unit> {
        return flow {
            emit(
                Utility.changeLanguage(
                    this@SplashActivity,
                    SharedPreferencesRepository.getDataManagerInstance().selectedLanguage
                )
            )
        }
    }

    override fun setUp() {


        nextMClass()

    }

    override fun onDestroy() {

        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12345) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d(TAG, "" + "Result Ok")
                    //  handle user's approval }
                }
                Activity.RESULT_CANCELED -> {
                    {
//if you want to request the update again just call checkUpdate()
                    }
                    Log.d(TAG, "" + "Result Cancelled")
                    //  handle user's rejection  }
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    //if you want to request the update again just call checkUpdate()
                    Log.d(TAG, "" + "Update Failure")
                    //  handle update failure
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()

    }
    private fun setObservers() {
        getappVersion()
        homeViewModel.commoditiesReponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog()

                }

                is NetworkResult.Loading -> {
                    showDialog()
                }

                is NetworkResult.Success -> {
                    it.data?.let {
                        if (it.status == "2" || it.status == "3" || it.status == "0") {
                            startActivity(Intent(this, LoginActivity::class.java))
                        } else if (it.status == "1") {
                            SharedPreferencesRepository.getDataManagerInstance()
                                .setCommdity(it.categories)
                            SharedPreferencesRepository.getDataManagerInstance().employee =
                                it.employee
                            SharedPreferencesRepository.getDataManagerInstance()
                                .setContractor(it.labourList)

                            afterpermissionNext()
                        }
                    }


                }
            }


        }
        homeViewModel.response.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();

                }

                is NetworkResult.Loading -> {
                }

                is NetworkResult.Success -> {
                    it.data?.let {
                        if (it.status == "2" || it.status == "3" || it.status == "0") {
                            startActivity(Intent(this, LoginActivity::class.java))
                        } else if (it.status == "1") {
                            SharedPreferencesRepository.getDataManagerInstance()
                                .saveUserPermissionData(it.userPermissionsResult)
                            startActivityAndClear(StaffDashBoardActivity::class.java)
                        }
                    }


                }

            }
        }
        homeViewModel.commoditiesReponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {

                }

                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    it.data?.let {
                        if (it.status == "2" || it.status == "3" || it.status == "0") {
                            startActivity(Intent(this, LoginActivity::class.java))
                        } else if (it.status == "1") {
                            SharedPreferencesRepository.getDataManagerInstance()
                                .setCommdity(it.categories)
                            SharedPreferencesRepository.getDataManagerInstance().employee =
                                it.employee
                            SharedPreferencesRepository.getDataManagerInstance()
                                .setContractor(
                                    it
                                        .labourList
                                )
                        }
                    }

                }
            }
        }
    }

    private fun nextMClass() {

        permissionsBuilder(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).build().send() {
            if (it.allGranted()) {
                setObservers()
            } else {
                Toast.makeText(
                    this,
                    "Location or  Camera Permissions Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }


    }



    private fun afterpermissionNext() {
        SharedPreferencesRepository.getDataManagerInstance().userPermission
        if (SharedPreferencesRepository.getDataManagerInstance().userPermission != null && SharedPreferencesRepository.getDataManagerInstance().userPermission.isNotEmpty()) {
            startActivityAndClear(StaffDashBoardActivity::class.java)
        } else {
            val userDetails = SharedPreferencesRepository.getDataManagerInstance().user
            if (userDetails != null && userDetails.fname != null && !userDetails.fname.isEmpty()) {
                homeViewModel.getPermissions(userDetails!!.role_id, userDetails!!.level_id)


            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                intent.putExtra("setting", "")
                this@SplashActivity.startActivity(intent)
                finish()
            }
        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        afterpermissionNext()

    }

    /* private void nextMClass() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserDetails userDetails = SharedPreferencesRepository.getDataManagerInstance().getUser();
                boolean name = SharedPreferencesRepository.getDataManagerInstance().isUSerName();
                if (name) {
                    startActivityAndClear(StaffDashBoardActivity.class);
                } else if (userDetails!=null&&(userDetails.getFname() != null) && (!userDetails.getFname().isEmpty())) {
                    startActivityAndClear(StaffDashBoardActivity.class);
                }  else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.putExtra("setting", "");
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }
            }
        }, 3000);
    }*/
    private fun getappVersion() {
        homeViewModel.getAppVersion("Emp")
        homeViewModel.appVersionResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    it.data?.let {
                        if (it.status == "2" || it.status == "3" || it.status == "0") {
                            startActivity(Intent(this, LoginActivity::class.java))
                        } else if (it.status == "1") {
                            getCommodityList()

                        }
                    }

                }
            }
        }


    }


    private fun getCommodityList() {
        homeViewModel.getCommodities("Emp")


    }


    companion object {
        private const val REQUEST_CAMERA_CODE = 120
        private const val REQUEST_UPDATE=12345
    }
}
