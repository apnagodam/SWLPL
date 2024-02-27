package com.apnagodam.staff.Base;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.exifinterface.media.ExifInterface;

import com.apnagodam.staff.ApnaGodamApp;
import com.apnagodam.staff.BuildConfig;
import com.apnagodam.staff.Network.ApiService;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.Network.RetrofitAPIClient;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.LoginActivity;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.MenuItem;
import com.apnagodam.staff.utils.LocaleHelper;
import com.apnagodam.staff.utils.Logger;
import com.apnagodam.staff.utils.Tags;
import com.apnagodam.staff.utils.Utility;
import com.google.android.material.snackbar.Snackbar;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.callback.BitmapLoadCallback;
import com.yalantis.ucrop.model.ExifInfo;
import com.yalantis.ucrop.util.BitmapLoadUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import dagger.hilt.android.AndroidEntryPoint;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity implements Tags {
    public ApnaGodamApp ePowerApp;
    protected T binding;
    protected ApiService apiService;
    public static final int REQUEST_CAMERA = 100;
    private String currentOperation;
    //for pic photo from camera  and gallery
    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    public static final int REQUEST_CAMERA_PICTURE = 0x02;
    protected Uri mediaUri;
    public String SpiltImageoFCommudity = "";
    protected Uri camUri;
    public File file;
    private static final int REQUEST_CAMERA_CODE = 120;

    public BaseActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
              //  .penaltyDeath()
                .build());
        ePowerApp = (ApnaGodamApp) getApplication();
        apiService = RetrofitAPIClient.getRetrofitClient();
        binding = DataBindingUtil.setContentView(this, getLayoutResId());

        setUp();

    }

    void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract void setUp();

    public void setCurrentOperation(String currentOperation) {
        this.currentOperation = currentOperation;
    }

    public ApnaGodamApp getEpowerApp() {
        if (ePowerApp == null) {
            ePowerApp = (ApnaGodamApp) getApplication();
        }
        return ePowerApp;
    }


    public void showDialog() {
        Utility.showDialog(BaseActivity.this, "");
    }

    public void hideDialog() {
        Utility.hideDialog(BaseActivity.this);
    }

    public JSONObject getObject(ResponseBody responseBody) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseBody.string());
        } catch (Exception e) {
            System.out.println(e);
        }
        return jsonObject;
    }

    public void startActivity(Class clazz) {
        Intent intent = new Intent(BaseActivity.this, clazz);
        startActivity(intent);
        //  overridePendingTransition(android.R.anim.slide_in, android.R.anim.slide_out);
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void startActivity(Class clazz, Bundle bundle) {
        Intent intent = new Intent(BaseActivity.this, clazz);
        intent.putExtra(BUNDLE, bundle);
        startActivity(intent);
        // overridePendingTransition(android.R.anim.slide_in, android.R.anim.slide_out);
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void startActivityAndClear(Class clazz) {
        Intent intent = new Intent(BaseActivity.this, clazz);
        startActivity(intent);
        finish();
        // overridePendingTransition(android.R.anim.slide_in, android.R.anim.slide_out);
//        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void startActivityAndClear(Class clazz, Bundle bundle) {
        Intent intent = new Intent(BaseActivity.this, clazz);
        finish();
        intent.putExtra(BUNDLE, bundle);
        startActivity(intent);
        // overridePendingTransition(android.R.anim.slide_in, android.R.anim.slide_out);
//        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void startActivity(Class clazz, String key, String value) {
        Intent intent = new Intent(BaseActivity.this, clazz);
        intent.putExtra(key, value);
        startActivity(intent);
//        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    public void setBackBtn(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    public void logout(String Msg, String logout) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this);
        alertDialog.setMessage(Msg);
        alertDialog.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            dialogInterface.dismiss();
            SharedPreferencesRepository.clearLoginPref();
            if (logout.equalsIgnoreCase("Logout")) {
                callBidderLogout();
            } else {
            }
        });

        alertDialog.setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.create().show();

    }

    private void callBidderLogout() {


    }

    public void logout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this);
        alertDialog.setMessage(R.string.logout_alert);
        alertDialog.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            dialogInterface.dismiss();
            SharedPreferencesRepository.clearLoginPref();
            //   startActivityAndClear(LoginActivity.class);
        });
        alertDialog.setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.create().show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Utility.hideSoftKeyboard(BaseActivity.this);
        View v = getCurrentFocus();
        if (v instanceof EditText) {
            Rect outRect = new Rect();
            v.getGlobalVisibleRect(outRect);
            if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                v.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(@StringRes int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected String stringFromView(TextView textView) {
        return textView.getText().toString().trim();
    }

    public void showErrorSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        View view = snackbar.getView();

        view.setBackgroundColor(getResources().getColor(R.color.colorRed));
        snackbar.show();
    }

    protected String ifEmpty(String string) {
        return TextUtils.isEmpty(string) ? "" : string;
    }

 /*   public ArrayList<MenuItem> getMenuList() {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("", 0));
        MenuItem menuItem1 = new MenuItem(getResources().getString(R.string.home), R.drawable.ic_home_solid);
        menuItems.add(menuItem1);
      *//*  MenuItem menuItem2 = new MenuItem(getResources().getString(R.string.referral_code), R.drawable.ic_baseline_group_add_24);
        menuItems.add(menuItem2);*//*
        MenuItem menuItem2 = new MenuItem(getResources().getString(R.string.select_language), R.drawable.ic_baseline_group_add_24);
        menuItems.add(menuItem2);
        for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
            if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("11")) {
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView() == 1) {
                    MenuItem menuItem3 = new MenuItem(getResources().getString(R.string.lead_generate), R.drawable.ic_baseline_settings_24);
                    menuItems.add(menuItem3);
                }
            }  if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("12")){
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView()==1) {
                    MenuItem menuItem4 = new MenuItem(getResources().getString(R.string.create_case), R.drawable.ic_baseline_settings_24);
                    menuItems.add(menuItem4);
                }}
            if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("13")){
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView()==1) {
                    MenuItem menuItem5 = new MenuItem(getResources().getString(R.string.pricing_title), R.drawable.ic_baseline_notifications_24);
                    menuItems.add(menuItem5);
                }}
            if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("15")){
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView()==1) {
                    MenuItem menuItem6 = new MenuItem(getResources().getString(R.string.truck_book), R.drawable.ic_baseline_notifications_24);
                    menuItems.add(menuItem6);
                }}
            if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("16")){
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView()==1) {
                    MenuItem menuItem7 = new MenuItem(getResources().getString(R.string.labour_book), R.drawable.ic_baseline_notifications_24);
                    menuItems.add(menuItem7);
                }}
            if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("20")){
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView()==1) {
                    MenuItem menuItem8 = new MenuItem(getResources().getString(R.string.firstkanta_parchi), R.drawable.ic_baseline_notifications_24);
                    menuItems.add(menuItem8);
                }}
            if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("18")){
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView()==1) {
                    MenuItem menuItem9 = new MenuItem(getResources().getString(R.string.f_quality_repots), R.drawable.ic_baseline_notifications_24);
                    menuItems.add(menuItem9);
                }}
            if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("20")){
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView()==1) {
                    MenuItem menuItem10 = new MenuItem(getResources().getString(R.string.secoundkanta_parchi), R.drawable.ic_baseline_notifications_24);
                    menuItems.add(menuItem10);
                }}
            if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("18")){
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView()==1) {
                    MenuItem menuItem11 = new MenuItem(getResources().getString(R.string.s_quality_repots), R.drawable.ic_baseline_notifications_24);
                    menuItems.add(menuItem11);
                }}
            if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("19")){
                if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getView()==1) {
                    MenuItem menuItem12 = new MenuItem(getResources().getString(R.string.gate_passs), R.drawable.ic_baseline_notifications_24);
                    menuItems.add(menuItem12);
                }}
        }
        MenuItem menuIte13 = new MenuItem(getResources().getString(R.string.logout), R.drawable.ic_logout_new_black_24dp);
        menuItems.add(menuIte13);
        return menuItems;
    }*/

/* public ArrayList<MenuItem> getMenuList() {
     ArrayList<MenuItem> menuItems = new ArrayList<>();
     menuItems.add(new MenuItem("", 0));
     MenuItem menuItem1 = new MenuItem(getResources().getString(R.string.home), R.drawable.ic_home_solid);
     menuItems.add(menuItem1);
      *//*  MenuItem menuItem2 = new MenuItem(getResources().getString(R.string.referral_code), R.drawable.ic_baseline_group_add_24);
        menuItems.add(menuItem2);*//*
     MenuItem menuItem2 = new MenuItem(getResources().getString(R.string.select_language), R.drawable.ic_baseline_group_add_24);
     menuItems.add(menuItem2);
     MenuItem menuItem3 = new MenuItem(getResources().getString(R.string.lead_generate), R.drawable.ic_baseline_settings_24);
     menuItems.add(menuItem3);
     MenuItem menuItem4 = new MenuItem(getResources().getString(R.string.create_case), R.drawable.ic_baseline_settings_24);
     menuItems.add(menuItem4);

     MenuItem menuItem5 = new MenuItem(getResources().getString(R.string.pricing_title), R.drawable.ic_baseline_notifications_24);
     menuItems.add(menuItem5);
     MenuItem menuItem6 = new MenuItem(getResources().getString(R.string.truck_book), R.drawable.ic_baseline_notifications_24);
     menuItems.add(menuItem6);
     MenuItem menuItem7 = new MenuItem(getResources().getString(R.string.labour_book), R.drawable.ic_baseline_notifications_24);
     menuItems.add(menuItem7);
     MenuItem menuItem8 = new MenuItem(getResources().getString(R.string.firstkanta_parchi), R.drawable.ic_baseline_notifications_24);
     menuItems.add(menuItem8);
     MenuItem menuItem9 = new MenuItem(getResources().getString(R.string.f_quality_repots), R.drawable.ic_baseline_notifications_24);
     menuItems.add(menuItem9);
     MenuItem menuItem10 = new MenuItem(getResources().getString(R.string.secoundkanta_parchi), R.drawable.ic_baseline_notifications_24);
     menuItems.add(menuItem10);
     MenuItem menuItem11 = new MenuItem(getResources().getString(R.string.s_quality_repots), R.drawable.ic_baseline_notifications_24);
     menuItems.add(menuItem11);
     MenuItem menuItem12 = new MenuItem(getResources().getString(R.string.gate_passs), R.drawable.ic_baseline_notifications_24);
     menuItems.add(menuItem12);
     MenuItem menuIte13 = new MenuItem(getResources().getString(R.string.logout), R.drawable.ic_logout_new_black_24dp);
     menuItems.add(menuIte13);
     return menuItems;
 }*/

    public ArrayList<MenuItem> getMenuList() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();
        ArrayList<MenuItem.SubList> getSubList = new ArrayList<>();
        getSubList.add(new MenuItem.SubList("dummy", R.drawable.ic_home_solid));
        getSubList.add(new MenuItem.SubList("dummy", R.drawable.ic_home_solid));

        menuItems.add(new MenuItem("", 0, getSubList));
        MenuItem menuItem1 = new MenuItem(getResources().getString(R.string.home), R.drawable.ic_home_solid, getSubList);
        menuItems.add(menuItem1);
        MenuItem menuItem2 = new MenuItem(getResources().getString(R.string.referral_code), R.drawable.ic_baseline_group_add_24, getSubList);
        menuItems.add(menuItem2);
        MenuItem menuItem3 = new MenuItem(getResources().getString(R.string.select_language), R.drawable.ic_baseline_group_add_24, getSubList);
        menuItems.add(menuItem3);

       /* MenuItem menuItem3 = new MenuItem(getResources().getString(R.string.lead_generate), R.drawable.ic_baseline_settings_24);
        menuItems.add(menuItem3);
        MenuItem menuItem4 = new MenuItem(getResources().getString(R.string.create_case), R.drawable.ic_baseline_settings_24);
        menuItems.add(menuItem4);

        MenuItem menuItem5 = new MenuItem(getResources().getString(R.string.pricing_title), R.drawable.ic_baseline_notifications_24);
        menuItems.add(menuItem5);
        MenuItem menuItem6 = new MenuItem(getResources().getString(R.string.truck_book), R.drawable.ic_baseline_notifications_24);
        menuItems.add(menuItem6);
        MenuItem menuItem7 = new MenuItem(getResources().getString(R.string.labour_book), R.drawable.ic_baseline_notifications_24);
        menuItems.add(menuItem7);
        MenuItem menuItem8 = new MenuItem(getResources().getString(R.string.firstkanta_parchi), R.drawable.ic_baseline_notifications_24);
        menuItems.add(menuItem8);
        MenuItem menuItem9 = new MenuItem(getResources().getString(R.string.f_quality_repots), R.drawable.ic_baseline_notifications_24);
        menuItems.add(menuItem9);
        MenuItem menuItem10 = new MenuItem(getResources().getString(R.string.secoundkanta_parchi), R.drawable.ic_baseline_notifications_24);
        menuItems.add(menuItem10);
        MenuItem menuItem11 = new MenuItem(getResources().getString(R.string.s_quality_repots), R.drawable.ic_baseline_notifications_24);
        menuItems.add(menuItem11);
        MenuItem menuItem12 = new MenuItem(getResources().getString(R.string.gate_passs), R.drawable.ic_baseline_notifications_24);
        menuItems.add(menuItem12);*/
      /*  MenuItem menuItem4 = new MenuItem(getResources().getString(R.string.spot_sell), R.drawable.deal_statment);
        menuItems.add(menuItem4);*/

        MenuItem menuItem5 = new MenuItem(getResources().getString(R.string.logout), R.drawable.ic_logout_new_black_24dp, getSubList);
        menuItems.add(menuItem5);
        return menuItems;

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(LocaleHelper.onAttach(newBase)));
    }

    public void onImageSelected() {
      /*  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT > 22) {
                askForPermissions(PERMISSIONS,
                        REQUEST_CAMERA_PERMISSIONS);
            } else {
                startCameraForPic();
            }
        } else {
            startCameraForPic();
        }*/
        if (requestPermissions()) {
            startCameraForPic();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_CODE
            );
        }
    }

    private Boolean requestPermissions() {
        int cameraPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writeExternalPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );
        int readExternalPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );
        return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalPermission == PackageManager.PERMISSION_GRANTED
                && readExternalPermission == PackageManager.PERMISSION_GRANTED;
    }

    protected void startCameraForPic() {
        File camFile = Utility.getOutputMediaFile(BaseActivity.this, "img");
        if (camFile.exists()) {
            camFile.delete();
        }
        camFile = Utility.getOutputMediaFile(BaseActivity.this, "img");
        camUri = Uri.fromFile(camFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(camFile));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", camFile));
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivityForResult(intent, REQUEST_CAMERA_PICTURE);
    }

    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //    currentPhotoPath = image.getAbsolutePath();
        Log.d("pathhhhhhh", image.getAbsolutePath());
        return image;
    }

    protected void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                camUri = photoURI;
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_PICTURE);
            }
        }
    }

    protected final void askForPermissions(String[] permissions, int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                            onImageSelected();
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]))
                                return;
                        } else {
                            requestPermissions();
                        }
                    }
                    // open dialog
                }
        }
    }

    public void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME + System.currentTimeMillis();
        destinationFileName += ".jpeg";
        File f = new File(destinationFileName);
        if (f.exists()) {
            f.delete();
        }
        Logger.showLogE("exist" + f.exists());
        File file = new File(getCacheDir(), destinationFileName);
        Logger.showLogE("File exist " + file.exists());
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(file));
        uCrop = advancedConfig(uCrop);
        uCrop.start(this);
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(70);
        options.setMaxScaleMultiplier(5);
        options.setImageToCropBoundsAnimDuration(666);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        //options.setMaxBitmapSize(640);
        options.withAspectRatio(1, 1);
        return uCrop.withOptions(options);
    }

    public void handleCropResult(@NonNull Intent result, ImageView passbookImage) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            Logger.showLogE("URI " + resultUri);
            file = new File(resultUri.getPath());
            loadImage(resultUri, passbookImage);
        } else {
            Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
        }
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 916.0f;
        float maxWidth = 712.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }

//    public String getFilename() {
//        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
//        return uriSting;
//    }

    public String getFilename() {
        //    File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        String finalPath = getActivity().getFilesDir().toString() + File.separator + System.currentTimeMillis() + ".jpg";
        File file = new File(finalPath);
        if (!file.exists()) {
            //        file.mkdirs();
            file.getParentFile().mkdirs();
        }
        //    String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        String uriSting = (file.getAbsolutePath());
        return uriSting;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public void loadImage(Uri resultUri, ImageView passbookImage) {
        BitmapLoadUtils.decodeBitmapInBackground(this, resultUri, null, 400, 400, new BitmapLoadCallback() {
            @Override
            public void onBitmapLoaded(@NonNull Bitmap bitmap, @NonNull ExifInfo exifInfo, @NonNull Uri imageInputUri, @Nullable Uri imageOutputUri) {
                passbookImage.setImageBitmap(bitmap);

            }

            @Override
            public void onFailure(@NonNull Exception bitmapWorkerException) {

            }
        }
        );
    }

    public void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Logger.showLogE("handleCropError: " + cropError);
        }
    }

//    protected void onSaveInstanceState(Bundle icicle) {
//        super.onSaveInstanceState(icicle);
//        if (camUri != null)
//            icicle.putString("param", camUri.toString());
//    }
//
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        String value = savedInstanceState.getString("param");
//        camUri = Uri.parse(value);
//    }

}
