package com.apnagodam.staff.utils;

import android.util.Log;

import com.apnagodam.staff.BuildConfig;


public class Logger {

    private static String TAG = Logger.class.getSimpleName();

    public static void showLogE(String message) {
        if (BuildConfig.DEBUG)
            Log.e(TAG, message);
    }

    public static void showLogD(String message) {
        if (BuildConfig.DEBUG)
            Log.e(TAG, message);
    }

    public static void showLogE(Exception e) {
        if (BuildConfig.DEBUG)
            Log.e(TAG, e.getMessage());
    }

    public static void showLogD(Exception e) {
        if (BuildConfig.DEBUG)
            Log.e(TAG, e.getMessage());
    }

}
