package com.apnagodam.staff.activity.attendance

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.apnagodam.staff.R
import com.arthurivanets.bottomsheets.BaseBottomSheet
import com.arthurivanets.bottomsheets.config.BaseConfig
import com.arthurivanets.bottomsheets.config.Config

class AttendanceBottomSheet(
    private var hostActivity: Activity,
    config: BaseConfig = Config.Builder(hostActivity).build()
) : BaseBottomSheet(hostActivity, config) {
    override fun onCreateSheetContentView(context: Context): View {
        val inflater = LayoutInflater.from(hostActivity).inflate(
            R.layout.layout_attendance,
            this,
            false
        )
        return inflater


    }

}