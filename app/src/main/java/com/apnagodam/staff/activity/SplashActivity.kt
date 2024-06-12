package com.apnagodam.staff.activity


import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.HomeViewModel
import com.apnagodam.staff.Network.viewmodel.NetworkSpeedViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivitySplashBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.utils.UpdateHelper
import com.apnagodam.staff.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@AndroidEntryPoint
class SplashActivity() : BaseActivity<ActivitySplashBinding?>() {

    //    private lateinit var inAppUpdate: InAppUpdate
    val homeViewModel by viewModels<HomeViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }



    val result =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            // handle callback
            if (result.resultCode != Activity.RESULT_OK) {
                UpdateHelper.checkForUpdate(this)
            }
        }

    override fun setUp() {
        UpdateHelper.checkForUpdate(this)
        getappVersion()
        nextMClass()


    }


    private fun setObservers() {
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
                                .setTerminals(it.terminals)
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
        setObservers()
//        networkSpeedViewModel.getNetworkSpeed(this)

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


    }


    private fun getCommodityList() {
        homeViewModel.getCommodities("Emp")


    }


    companion object {
        private const val REQUEST_CAMERA_CODE = 120
        private const val REQUEST_UPDATE = 12345
    }
}
