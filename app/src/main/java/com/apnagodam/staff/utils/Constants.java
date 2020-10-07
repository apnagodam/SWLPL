package com.apnagodam.staff.utils;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by User on 28-07-2020.
 */

public class Constants {
    /*  // for local server
    public static final String IMAGE_BASE_URL = "http://192.168.0.24/apna_godam/trunk//resources/assets/upload/employyes/";
    public static final String IMAGE_BASE_URL_user_photo = "http://192.168.101.21/apna_godam/trunk//resources/frontend_assets/uploads/";
    public static final String API_BASE_URL = "http://192.168.0.24/apna_godam/trunk/";*/

    // live server
    /*// for live server
    public static final String IMAGE_BASE_URL = "https://apnagodam.in/resources/assets/upload/employyes/";
    public static final String IMAGE_BASE_URL_user_photo = "https://apnagodam.in/resources/frontend_assets/uploads/";
    public static final String API_BASE_URL = "https://apnagodam.in/";*/
      // for demo server
    public static final String IMAGE_BASE_URL = "https://apnagodam.in/demo/resources/assets/upload/employyes/";
    public static final String IMAGE_BASE_URL_user_photo = "https://apnagodam.in/demo/resources/frontend_assets/uploads/";
    public static final String API_BASE_URL = "https://apnagodam.in/demo/";
    public static final String ENDPOINT = API_BASE_URL;
    public static final String RES_SUCCESS = "Success";
    public static final String RES_ERROR = "Error";
    public static final String CONNECT_TO_WIFI = "WIFI";
    public static final String CONNECT_TO_MOBILE = "MOBILE";
    public static final String NOT_CONNECT = "NOT_CONNECT";
    public final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String URL = "url";
    public static final String RUPEES_SYMBOL = "₹";
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final String HINDI_LOCALE = "hi";
    public static final String ENGLISH_LOCALE = "en_US";
    public static final String COUNTRY_CODE = "+91";
    public static final String KEY_IS_NEW_USER = "KEY_IS_NEW_USER";
    public static final String KEY_IS_FROM_FORGOT_PASSWORD = "KEY_IS_FROM_FORGOT_PASSWORD";
    public static final String TITLE = "title";
    private static final String PACKAGE_NAME =
            "com.apnagodam";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    private static final float BITMAP_SCALE = 0.4f;
    private static final int BLUR_RADIUS = 8;

    public static Bitmap fastblur(Bitmap sentBitmap) {
        float scale = BITMAP_SCALE;
        int radius = BLUR_RADIUS;
        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];



    /*localConst*/
    public static final String LeadListData ="leadData";


}
