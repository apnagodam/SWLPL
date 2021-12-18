package com.apnagodam.staff.Network;

import android.content.Intent;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.Response.BaseResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.LoginActivity;
import com.apnagodam.staff.utils.ResponseCode;
import com.apnagodam.staff.utils.Utility;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NetworkCallback<T extends BaseResponse> implements Callback<T> {
    private BaseActivity activity;
    public NetworkCallback(BaseActivity activity) {
        this.activity = activity;
        activity.showDialog();
    }
    @Override
    public void onResponse(@NotNull Call<T> call, Response<T> response) {
//        activity.hideDialog();
        if (response.body() != null) {
            activity.hideDialog();
             if (response.body().getStatus().equals(ResponseCode.CODE_1000)) {
                onSuccess(response.body());
            }
             else   if (response.body().getStatus()==3) {
              /*   SharedPreferencesRepository.getDataManagerInstance().clear();
                 SharedPreferencesRepository.getDataManagerInstance().setIsUserName(false);
                 SharedPreferencesRepository.getDataManagerInstance().saveSessionToken("");*/
                 Intent intent = new Intent(activity, LoginActivity.class);
                 intent.putExtra("setting"," ");
                 activity.startActivity(intent);
                 activity.finish();
             }else {
                Utility.showAlertDialog(activity, activity.getString(R.string.alert), response.body().getMessage());
            }
        } else {
            activity.hideDialog();
            Utility.showAlertDialog(activity, activity.getString(R.string.alert),"Network Issue");
        }
    }

    @Override
    public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
        activity.hideDialog();
        Utility.showAlertDialog(activity, activity.getString(R.string.alert),"Network Issue");
//        Utility.showOnFailureError(activity, t);
    }
    protected abstract void onSuccess(T body);
}
