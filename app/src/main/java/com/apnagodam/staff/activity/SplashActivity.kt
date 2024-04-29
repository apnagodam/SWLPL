package com.apnagodam.staff.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.BuildConfig
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.HomeViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivitySplashBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

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
                    if (it.data != null) {
                        SharedPreferencesRepository.getDataManagerInstance()
                            .setCommdity(it.data.categories)
                        SharedPreferencesRepository.getDataManagerInstance().employee =
                            it.data.employee
                        SharedPreferencesRepository.getDataManagerInstance()
                            .setContractor(it.data.labourList)

                        afterpermissionNext()
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
                    if (it.data != null) {

                        SharedPreferencesRepository.getDataManagerInstance()
                            .saveUserPermissionData(it.data.userPermissionsResult)
                        startActivityAndClear(StaffDashBoardActivity::class.java)

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
                    if (it.data != null) {
                        SharedPreferencesRepository.getDataManagerInstance()
                            .setCommdity(it.data.categories)
                        SharedPreferencesRepository.getDataManagerInstance().employee =
                            it.data.employee
                        SharedPreferencesRepository.getDataManagerInstance()
                            .setContractor(
                                it.data
                                    .labourList
                            )
                    }
                }
            }
        }
    }

    private fun nextMClass() {
        lifecycleScope.launch {
            requestPermissions().collect() {
                if (it == true) {

                } else {
                    ActivityCompat.requestPermissions(
                        this@SplashActivity,
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        REQUEST_CAMERA_CODE
                    )
                }

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


    suspend private fun requestPermissions(): Flow<Boolean> {

        return flow<Boolean> {
            val cameraPermission =
                ContextCompat.checkSelfPermission(this@SplashActivity, Manifest.permission.CAMERA)
            val writeExternalPermission = ContextCompat.checkSelfPermission(
                this@SplashActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val readExternalPermission = ContextCompat.checkSelfPermission(
                this@SplashActivity, Manifest.permission.READ_EXTERNAL_STORAGE
            )
            emit(
                cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalPermission == PackageManager.PERMISSION_GRANTED && readExternalPermission == PackageManager.PERMISSION_GRANTED
            )
        }
            .flowOn(Dispatchers.IO)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CAMERA_CODE -> if (grantResults.isNotEmpty()) {
                var i = 0
                while (i < grantResults.size) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        GlobalScope.launch {
                            requestPermissions().collect()
                        }

                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permissions[i]
                            )
                        ) return
                    } else {
                        //  requestPermissions();
                    }
                    i++
                }

                // open dialog
            }

        }
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
                    getCommodityList()

                }
            }
        }


    }


    private fun getCommodityList() {
        homeViewModel.getCommodities("Emp")


    }


    companion object {
        private const val REQUEST_CAMERA_CODE = 120
    }
}
