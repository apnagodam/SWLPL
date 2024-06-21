package com.apnagodam.staff.utils

import android.app.Activity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

object UpdateHelper {
    fun createInstance(activity: Activity) = AppUpdateManagerFactory.create(activity)

    fun appUpdateInfoTask(activity: Activity) = createInstance(activity).appUpdateInfo

    fun checkForUpdate(activity: Activity) {
        appUpdateInfoTask(activity).addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                createInstance(activity).startUpdateFlow(
                    it,
                    activity,
                    AppUpdateOptions.defaultOptions(AppUpdateType.IMMEDIATE)
                )
            }
        }
        appUpdateInfoTask(activity).addOnCanceledListener {
            activity.finish()
        }

    }

}