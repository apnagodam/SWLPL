package com.apnagodam.staff.activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.apnagodam.staff.ApnaGodamApp
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.BuildConfig
import com.apnagodam.staff.Network.NetworkCallbackWProgress
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Response.VersionCodeResponse
import com.apnagodam.staff.Network.viewmodel.HomeViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.LoginActivity
import com.apnagodam.staff.adapter.NavigationAdapter
import com.apnagodam.staff.databinding.ActivitySplashBinding
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.CommudityResponse
import com.apnagodam.staff.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class SplashActivity() : BaseActivity<ActivitySplashBinding?>() {
    var market_uri = "https://play.google.com/store/apps/details?id=com.apnagodam.staff&hl=en"

    val homeViewModel by viewModels<HomeViewModel>()
    override fun getLayoutResId(): Int {
        Utility.changeLanguage(
            this,
            SharedPreferencesRepository.getDataManagerInstance().selectedLanguage
        )
        return R.layout.activity_splash
    }

    override fun setUp() {
        getappVersion()
        nextMClass()
    }

    private fun nextMClass() {
        when (requestPermissions()) {
            true -> afterpermissionNext()
            else -> {
                ActivityCompat.requestPermissions(
                    this,
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

    private fun afterpermissionNext() {
        val userDetails = SharedPreferencesRepository.getDataManagerInstance().user
        val name = SharedPreferencesRepository.isUSerName()

        when (name) {
            true -> startActivityAndClear(StaffDashBoardActivity::class.java)
            else -> {
                if (userDetails != null && userDetails.fname != null && !userDetails.fname.isEmpty()) {
                    homeViewModel.getPermissions(userDetails!!.role_id, userDetails!!.level_id)
                    homeViewModel.response.observe(this) {
                        when (it) {
                            is NetworkResult.Error -> {}
                            is NetworkResult.Loading -> {
                                showToast("loading")
                            }

                            is NetworkResult.Success -> {
                               if(it.data!=null){
                                   if(it.data.status==1){
                                       SharedPreferencesRepository.getDataManagerInstance()
                                           .saveUserPermissionData(it.data.getUserPermissionsResult())

                                       startActivityAndClear(StaffDashBoardActivity::class.java)
                                   }
                               }


                            }

                        }
                    }

                } else {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    intent.putExtra("setting", "")
                    this@SplashActivity.startActivity(intent)
                    finish()
                }
            }
        }

    }

    private fun requestPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val writeExternalPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readExternalPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalPermission == PackageManager.PERMISSION_GRANTED && readExternalPermission == PackageManager.PERMISSION_GRANTED
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
                        requestPermissions()
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
                afterpermissionNext()

                // open dialog
            }
        }
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

    private fun showUpdateDialogue() {
        val dialogue = Dialog(this)
        dialogue.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogue.setContentView(R.layout.dialogue_update_apk)
        dialogue.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialogue.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialogue.setCancelable(false)
        dialogue.setCanceledOnTouchOutside(false)
        dialogue.setTitle(null)
        ApnaGodamApp.showUpdate = false
        val updateBtn = dialogue.findViewById<Button>(R.id.btn_update)
        updateBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(market_uri))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                            ContextCompat.checkSelfPermission(
                                    SplashActivity.this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                            SplashActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(SplashActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                5);
                    } else {
                        dialogue.dismiss();
                        ApnaGodamApp.getInstance().showUpdate = false;
                        downloadApk();
                    }*/
        }
        dialogue.show()
    }

    private fun getCommodityList() {
        homeViewModel.getCommodities("Emp")
        homeViewModel.commoditiesReponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> TODO()
                is NetworkResult.Loading -> TODO()
                is NetworkResult.Success -> {
                    if (BuildConfig.APPLICATION_ID != null) {
                        SharedPreferencesRepository.getDataManagerInstance()
                            .setCommdity(it.data!!.categories)
                        SharedPreferencesRepository.getDataManagerInstance().employee =
                            it.data!!.employee
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


    companion object {
        private const val REQUEST_CAMERA_CODE = 120
    }
}
