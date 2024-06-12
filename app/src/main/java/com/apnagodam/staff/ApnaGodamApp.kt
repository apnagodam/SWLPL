package com.apnagodam.staff

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import java.util.Date


@HiltAndroidApp
class ApnaGodamApp : Application() {
    override fun onCreate() {
        super.onCreate()
        app = this
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder() //                        .setDefaultFontPath("fonts/Gotham.ttf")
                            .setDefaultFontPath("fonts/RobotoLight.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }

    companion object {
        @JvmField
        var date = Date()

        @JvmField
        var app: ApnaGodamApp? = null

        @JvmField
        var showUpdate = true
    }
}
