package com.apnagodam.staff.Network;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.Response.BaseResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.utils.ResponseCode;
import com.apnagodam.staff.utils.Utility;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NetworkCallbackWProgress<T extends BaseResponse> implements Callback<T> {
    private BaseActivity activity;
    public NetworkCallbackWProgress(BaseActivity activity) {
        this.activity = activity;
//        activity.showDialog();
    }
    @Override
    public void onResponse(@NotNull Call<T> call, Response<T> response) {
//        activity.hideDialog();
        if (response.body() != null) {
             if (response.body().getStatus().equals(ResponseCode.CODE_1000)) {
                onSuccess(response.body());
            } else {
                Utility.showAlertDialog(activity, activity.getString(R.string.alert), response.body().getMessage());
            }
        } else {
//            activity.hideDialog();
            Utility.showAlertDialog(activity, activity.getString(R.string.alert),"Network Issue");
        }
    }

    @Override
    public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
//        activity.hideDialog();
        Utility.showAlertDialog(activity, activity.getString(R.string.alert),"Network Issue");
//        Utility.showOnFailureError(activity, t);
    }

    protected abstract void onSuccess(T body);

}
