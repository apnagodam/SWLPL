package com.apnagodam.staff.activity.in.truckbook;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.CreatePricingSetPostData;
import com.apnagodam.staff.Network.Request.UploadTruckDetailsPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.pricing.InPricingListingActivity;
import com.apnagodam.staff.databinding.ActivitySetPricingBinding;
import com.apnagodam.staff.databinding.ActivityUploadDetailsBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.Utility;

public class TruckUploadDetailsClass extends BaseActivity<ActivityUploadDetailsBinding> implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    String UserName, vehhicleNO, CaseID = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_upload_details;
    }

    @Override
    protected void setUp() {
        Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle != null) {
            UserName = bundle.getString("user_name");
            vehhicleNO = bundle.getString("vehicle_no");
            CaseID = bundle.getString("case_id");
        }
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        clickListner();
        binding.customerName.setText(UserName);
        binding.caseId.setText(CaseID);

    }



    private void clickListner() {
        binding.ivClose.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.btn_login:
                if (isValid()) {
                       /* apiService.uploadTruckDetails(new UploadTruckDetailsPostData(
                                CaseID, stringFromView(binding.etPrice), stringFromView(binding.etProcessingFees),
                                stringFromView(binding.etIntersetRate), stringFromView(binding.etLoanPer),  stringFromView(binding.etRent),
                                vehhicleNO, stringFromView(binding.etLabourRate), stringFromView(binding.notes), stringFromView(binding.etUsernamee),
                                stringFromView(binding.etUsernameGatepass), stringFromView(binding.etColdWin), stringFromView(binding.etTallyPurchasee), stringFromView(binding.etTallyLoan),
                                stringFromView(binding.etTallySale))).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                            @Override
                            protected void onSuccess(LoginResponse body) {
                                Toast.makeText(TruckUploadDetailsClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                                startActivityAndClear(InPricingListingActivity.class);
                            }
                        });*/

                }
                break;
        }
    }

    boolean isValid() {
        if (TextUtils.isEmpty(stringFromView(binding.etPrice))) {
            return Utility.showEditTextError(binding.tilPrice, R.string.price);
        } else if (TextUtils.isEmpty(stringFromView(binding.etProcessingFees))) {
            return Utility.showEditTextError(binding.tilProcessingFees, R.string.processing_fees);
        } else if (TextUtils.isEmpty(stringFromView(binding.etRent))) {
            return Utility.showEditTextError(binding.tilRent, R.string.rent_mt);
        } else if (TextUtils.isEmpty(stringFromView(binding.etIntersetRate))) {
            return Utility.showEditTextError(binding.tilIntersetRate, R.string.inteerset_rate);
        } else if (TextUtils.isEmpty(stringFromView(binding.etLoanPer))) {
            return Utility.showEditTextError(binding.tilLoanPer, R.string.loan);
        } else if (TextUtils.isEmpty(stringFromView(binding.etLabourRate))) {
            return Utility.showEditTextError(binding.tilLabourRate, R.string.labour_rent_bag);
        }
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
