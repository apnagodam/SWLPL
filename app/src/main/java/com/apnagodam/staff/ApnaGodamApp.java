package com.apnagodam.staff;

import androidx.multidex.MultiDexApplication;


import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class ApnaGodamApp extends MultiDexApplication {

    static ApnaGodamApp app;
    public static Boolean showUpdate = true;
    synchronized public static ApnaGodamApp getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
        //                        .setDefaultFontPath("fonts/Gotham.ttf")
                                .setDefaultFontPath("fonts/RobotoLight.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }

}
