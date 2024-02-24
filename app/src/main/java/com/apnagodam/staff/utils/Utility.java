package com.apnagodam.staff.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.apnagodam.staff.ApnaGodamApp;
import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.R;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    private static int TYPE_WIFI = 1;
    private static int TYPE_MOBILE = 2;
    private static int TYPE_NOT_CONNECTED = 0;
    private static CustomProgressDialog mCustomProgressDialog;
    private static File path = ApnaGodamApp.app.getCacheDir();

    public static String timeFromdateTime(String inputDateStr) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("dd-mm-yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date);
            return outputDateStr;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static void copyAssets(String fileName) {
        AssetManager assetManager = ApnaGodamApp.app.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fileName);
            File outFile = new File(path, fileName);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + fileName, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // NOOP
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // NOOP
                    e.printStackTrace();
                }
            }
        }
    }

    public static double round(double value, int numberOfDigitsAfterDecimalPoint) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        if (context == null) {
            return Constants.NOT_CONNECT;
        }
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = Constants.CONNECT_TO_WIFI;
        } else if (conn == TYPE_MOBILE) {
            status = getNetworkClass(context);
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = Constants.NOT_CONNECT;
        }
        return status;
    }

    public static int dpTOPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static String getNetworkClass(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return "-"; // not connected
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    return "4G";
                default:
                    return "?";
            }
        }
        return "?";
    }

    /**
     * Convert byte array to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for (int idx = 0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    /**
     * Get utf8 byte array.
     *
     * @param str
     * @return array of NULL if error was found
     */
    public static byte[] getUTF8Bytes(String str) {
        try {
            return str.getBytes(StandardCharsets.UTF_8);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Load UTF8withBOM or any ansi text file.
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static String loadFileAsString(String filename) throws IOException {
        final int BUFLEN = 1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8 = false;
            int read, count = 0;
            while ((read = is.read(bytes)) != -1) {
                if (count == 0 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                    isUTF8 = true;
                    baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count += read;
            }
            return isUTF8 ? new String(baos.toByteArray(), StandardCharsets.UTF_8) : new String(baos.toByteArray());
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4) {
                                return sAddr;
                            } else {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } // for now eat exceptions
        return "";
    }

    /**
     * Show Progress Dialog
     *
     * @param activity - Activity context
     * @param message  - loading message
     */
    public static void showDialog(Activity activity, String message) {
        if (mCustomProgressDialog == null && activity != null)
            mCustomProgressDialog = new CustomProgressDialog(activity);
        mCustomProgressDialog.setCancelable(false);

        try {
            if (mCustomProgressDialog != null && !mCustomProgressDialog.isShowing()
                    && !activity.isFinishing())
                mCustomProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide Progress Dialog
     */
    public static void hideDialog(Activity activity) {
        try {
            if (mCustomProgressDialog != null && mCustomProgressDialog.isShowing() &&
                    !activity.isFinishing())
                mCustomProgressDialog.dismiss();
            mCustomProgressDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Validate Email
     *
     * @param target
     * @return
     */
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static File getOutputMediaFile(Context context, String media) {
        try {
            String e = context.getString(R.string.app_name);
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), e);
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }

            String timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())).format(new Date());
            File mediaFile = null;
            switch (media) {

                case "img":
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                    break;
                case "video":
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + "video_" + timeStamp + ".mp4");
                    break;
                case "pdf":
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + "gatepass_" + timeStamp + ".pdf");
                    break;

            }

            return mediaFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File getOutpuPDFtMediaFile(Context context, String media) {
        try {
            String e = context.getString(R.string.app_name);
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), e);
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }
            String timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())).format(new Date());
            File mediaFile = null;
            if (media.equals("pdf")) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "gatepass_" + timeStamp + ".pdf");
            }

            return mediaFile;
        } catch (Exception var6) {
            return null;
        }
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data=? ", new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int values1 = cursor.getInt(cursor.getColumnIndex("_id"));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + values1);
        } else if (imageFile.exists()) {
            ContentValues values = new ContentValues();
            values.put("_data", filePath);
            return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return null;
        }
    }

    public static String transferImageToBase64(File file) {
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            String encodedString = Base64.encodeToString(bytes, Base64.NO_WRAP);
            Logger.showLogE("Base 64 " + encodedString);

            return encodedString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void showSoftKeyboard(Context context, EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    public static void hideSoftKeyboard(Context context) {
        View view = ((Activity) context).getWindow().getCurrentFocus();
        if (view == null)
            return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static Drawable getMaskDrawable(Context context, int maskId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getDrawable(maskId);
        } else {
            drawable = context.getResources().getDrawable(maskId);
        }
        if (drawable == null) {
            throw new IllegalArgumentException("maskId is invalid");
        }
        return drawable;
    }

    public static String encryptToSHA_512(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean isValidPassword(final String password) {
        String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 9 && phone.length() <= 10;
        }
        return false;
    }

    private boolean isValidMobile2(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    /* public static ArrayList<DashboardMenu> getDashBoardMenu() {
        ArrayList<DashboardMenu> list = new ArrayList<>();
        list.add(new DashboardMenu(R.string.dashboard, R.drawable.ic_dashboard));
        list.add(new DashboardMenu(R.string.bill_history, R.drawable.ic_bill_history));
        list.add(new DashboardMenu(R.string.payment_history, R.drawable.ic_deposit_detail));
        list.add(new DashboardMenu(R.string.complaints, R.drawable.ic_complaints));
        list.add(new DashboardMenu(R.string.services, R.drawable.ic_services));
        list.add(new DashboardMenu(R.string.my_accounts, R.drawable.ic_my_accounts));
        return list;
    }*/

    public static void showAlertDialog(Context context, String title, String message, boolean isCancelable) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(isCancelable);
            alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showAlertDialog(Context context, String title, String message) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showAlertDialog(Context context, String title, String message, AlertCallback alertCallback) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                    alertCallback.callback();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean showEditTextError(EditText editText, String message) {
        editText.requestFocus();
        editText.setError(message);
        return false;
    }

    public static boolean showEditTextError(EditText editText, int message) {
        return showEditTextError(editText, editText.getContext().getString(message));
    }

    public static boolean showEditTextError(TextInputLayout editText, String message) {
        editText.setErrorEnabled(true);
        editText.requestFocus();
        editText.setError(message);
        return false;
    }

    public static boolean showEditTextError(TextInputLayout editText, int message) {
        return showEditTextError(editText, editText.getContext().getString(message));
    }

  /*  public static void showOnFailureError(Context context, @NotNull Throwable t) {
        if (t instanceof GenericException) {
            Utility.showAlertDialog(context, context.getString(R.string.alert), ((GenericException) t).message);
        } else {
            Utility.showAlertDialog(context, context.getString(R.string.alert), context.getString(R.string.error_request));
        }
    }*/

    /*public static void setTitle(BaseActivity activity, String title) {
        TextView titleTxt = activity.findViewById(R.id.titleTxt);
        if (titleTxt != null) {
            titleTxt.setText(title);
        }
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        if (toolbar != null) {
            activity.setBackBtn(toolbar);
        }

    }*/

    /* public static void setTitle(BaseActivity activity, @StringRes int title) {
         TextView titleTxt = activity.findViewById(R.id.titleTxt);
         if (titleTxt != null) {
             titleTxt.setText(title);
         }
         Toolbar toolbar = activity.findViewById(R.id.toolbar);
         if (toolbar != null) {
             activity.setBackBtn(toolbar);
         }
     }*/

    public interface AlertCallback {
        void callback();
    }

    public static void showDecisionDialog(Context context, String title, String message, final AlertCallback callbackListener) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    callbackListener.callback();
                    dialog.dismiss();
                }
            });
            alertDialogBuilder.setNeutralButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    /*    public static void setLocale(BaseActivity activity) {
            String currentLanguage = SharedPreferencesRepository.getLocale();
            String languageToLoad = Constants.ENGLISH_LOCALE;
            if (!currentLanguage.isEmpty() && currentLanguage.equals(Constants.ENGLISH_LOCALE)) {
                languageToLoad = Constants.HINDI_LOCALE;
            }
            Locale locale = new Locale(languageToLoad);
            Resources res = activity.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration configuration = res.getConfiguration();
            configuration.setLocale(locale);
            res.updateConfiguration(configuration, dm);
            SharedPreferencesRepository.setLocale(languageToLoad);
            // Refresh the app
            Intent refresh = new Intent(activity, activity.getClass());
            activity.overridePendingTransition(0, 0);
            activity.startActivity(refresh);
            activity.overridePendingTransition(0, 0);
            activity.finish();
        }*/

    public static String getCorrectFormatMobileNo(String mobileNo) {
        if (TextUtils.isEmpty(mobileNo) || mobileNo.length() < 10) {
            return mobileNo;
        }
        String result = mobileNo;
        result = mobileNo.replaceAll("[^0-9]", "");
        result = result.substring(result.length() - 10);
        return result;
    }

    public static void setLocale(BaseActivity activity) {
        String currentLanguage = SharedPreferencesRepository.getLocale();
        String languageToLoad = Constants.ENGLISH_LOCALE;

        Logger.showLogE(currentLanguage);

        if (currentLanguage.equals(Constants.ENGLISH_LOCALE)) {
            languageToLoad = Constants.HINDI_LOCALE;
        }
        Logger.showLogE(languageToLoad);

        LocaleHelper.setLocale(activity, languageToLoad);
        Intent refresh = new Intent(activity, activity.getClass());
        activity.overridePendingTransition(0, 0);
        activity.startActivity(refresh);
        activity.overridePendingTransition(0, 0);
        activity.finish();
    }

  /*  public static void openBrowser(BaseActivity activity, String title, String webLink) {
        Intent intent = new Intent(activity, BrowserActivity.class);
        intent.putExtra(Tags.TITLE, title);
        intent.putExtra(Tags.URL, webLink);
        activity.startActivity(intent);
    }*/

    public static void makeLinks(TextView textView, String[] links, ClickableSpan[] clickableSpans) {
        SpannableString spannableString = new SpannableString(textView.getText());
        for (int i = 0; i < links.length; i++) {
            ClickableSpan clickableSpan = clickableSpans[i];
            String link = links[i];
            int startIndexOfLink = textView.getText().toString().indexOf(link);
            spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setHighlightColor(Color.TRANSPARENT); // prevent TextView change background when highlight
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    @NotNull
    public static String getMaskMobileNo(String phone) {
        return "+91 " + phone.replaceAll("\\d(?=(?:\\D*\\d){4})", "*");
    }

    public static String convertDateToDDMMYYYY(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public static Date getExactLastMonthDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    public static String getCorrectPhoneNumber(String mobile) {
        String result = mobile;
        result = mobile.replaceAll("[^0-9]", "");
        if (result.length() > 10)
            result = result.substring(result.length() - 10);
        return result;
    }

    public static Float formatFloat(Float floatValue) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMaximumFractionDigits(3);
        formatter.setMinimumFractionDigits(0);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        try {
            return NumberFormat.getNumberInstance(Locale.US).parse(formatter.format(floatValue)).floatValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return floatValue;
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static void storeImage(Bitmap image, boolean isBarCode) {
        File file;
        file = new File(path, isBarCode ? "barcode.png" : "qrcode.png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("File not found: ", e.getMessage());
        } catch (IOException e) {
            Log.d("Error accessing file: ", e.getMessage());
        }
    }

    public static Bitmap removeMargins(Bitmap bmp, int color) {

        long dtMili = System.currentTimeMillis();
        int MTop = 0, MBot = 0, MLeft = 0, MRight = 0;
        boolean found1 = false, found2 = false;

        int[] bmpIn = new int[bmp.getWidth() * bmp.getHeight()];
        int[][] bmpInt = new int[bmp.getWidth()][bmp.getHeight()];

        bmp.getPixels(bmpIn, 0, bmp.getWidth(), 0, 0, bmp.getWidth(),
                bmp.getHeight());

        for (int ii = 0, contX = 0, contY = 0; ii < bmpIn.length; ii++) {
            bmpInt[contX][contY] = bmpIn[ii];
            contX++;
            if (contX >= bmp.getWidth()) {
                contX = 0;
                contY++;
                if (contY >= bmp.getHeight()) {
                    break;
                }
            }
        }

        for (int hP = 0; hP < bmpInt[0].length && !found2; hP++) {
            // looking for MTop
            for (int wP = 0; wP < bmpInt.length && !found2; wP++) {
                if (bmpInt[wP][hP] != color) {
                    Log.e("MTop 2", "Pixel found @" + hP);
                    MTop = hP;
                    found2 = true;
                    break;
                }
            }
        }
        found2 = false;

        for (int hP = bmpInt[0].length - 1; hP >= 0 && !found2; hP--) {
            // looking for MBot
            for (int wP = 0; wP < bmpInt.length && !found2; wP++) {
                if (bmpInt[wP][hP] != color) {
                    Log.e("MBot 2", "Pixel found @" + hP);
                    MBot = bmp.getHeight() - hP;
                    found2 = true;
                    break;
                }
            }
        }
        found2 = false;

        for (int wP = 0; wP < bmpInt.length && !found2; wP++) {
            // looking for MLeft
            for (int hP = 0; hP < bmpInt[0].length && !found2; hP++) {
                if (bmpInt[wP][hP] != color) {
                    Log.e("MLeft 2", "Pixel found @" + wP);
                    MLeft = wP;
                    found2 = true;
                    break;
                }
            }
        }
        found2 = false;

        for (int wP = bmpInt.length - 1; wP >= 0 && !found2; wP--) {
            // looking for MRight
            for (int hP = 0; hP < bmpInt[0].length && !found2; hP++) {
                if (bmpInt[wP][hP] != color) {
                    Log.e("MRight 2", "Pixel found @" + wP);
                    MRight = bmp.getWidth() - wP;
                    found2 = true;
                    break;
                }
            }

        }
        found2 = false;

        int sizeY = bmp.getHeight() - MBot - MTop, sizeX = bmp.getWidth()
                - MRight - MLeft;

        Bitmap bmp2 = Bitmap.createBitmap(bmp, MLeft, MTop, sizeX, sizeY);
        dtMili = (System.currentTimeMillis() - dtMili);
        Log.e("Margin   2",
                "Time needed " + dtMili + "mSec\nh:" + bmp.getWidth() + "w:"
                        + bmp.getHeight() + "\narray x:" + bmpInt.length + "y:"
                        + bmpInt[0].length);
        return bmp2;
    }

    public static File saveHtmlFile(String htmlString) {
        String fileName = "htmlBill.html";
        File file = new File(path, fileName);
        file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            byte[] data = htmlString.getBytes();
            out.write(data);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /*public static void setDiscom(BaseActivity activity, Spinner spinner) {
        if (spinner != null) {
            ArrayList<Discom> list = new ArrayList<>();

//            String baseUrl = "http://192.168.18.190/epower";
            String baseUrl = "http://210.212.99.66/epowerapi";

            list.add(new Discom(0, activity.getString(R.string.select_discom), ""));
            list.add(new Discom(2, "JAIPUR VIDUT VITRAN NIGAM LTD.", baseUrl));
            list.add(new Discom(1, "AJMER VIDUT VITRAN NIGAM LTD.", baseUrl));
            list.add(new Discom(3, "JODHPUR VIDYUT VITRAN NIGAM LTD", baseUrl));
            HintAdapter adapter = new HintAdapter<Discom>(activity, list);
            adapter.setDropDownViewResource(R.layout.layout_spinner);
            spinner.setAdapter(adapter);

            */
    /*Integer selID = SharedPreferencesRepository.getDiscom().getId();
            for (int i = 0; i < list.size(); i++) {
                Discom discom = list.get(i);
                if (discom.getId().equals(selID)) {
                    spinner.setSelection(i);
                }
            }*//*
        }
    }
*/

    public String calculateFileSize(Uri filepath) {
        return calculateFileSize(filepath.getPath());
    }

    public String calculateFileSize(String filepath) {
        //String filepathstr=filepath.toString();
        File file = new File(filepath);

        long fileSizeInBytes = file.length();
        float fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        float fileSizeInMB = fileSizeInKB / 1024;

        return Float.toString(fileSizeInMB);
    }

    public static void changeLanguage(Context context, String languageCode) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(languageCode));
        res.updateConfiguration(conf, dm);
//        prefs.setLanguageType(ConstantsValue.Language_Code, languageCode);
    }
}

