package com.apnagodam.staff.activity.in.pricing;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.CreatePricingSetPostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.in.labourbook.LabourBookUploadClass;
import com.apnagodam.staff.databinding.ActivitySetPricingBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.Utility;

public class SetPricingClass extends BaseActivity<ActivitySetPricingBinding> implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    String selectPurpose = null;
    String UserName, vehhicleNO, CaseID = "";
    boolean checked = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_pricing;
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
        setValueOnSpinner();
        binding.checkNotRequried.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                                @Override
                                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                    if (buttonView.isChecked()) {
                                                                        // checked
                                                                        Checked();
                                                                    } else {
                                                                        // not checked
                                                                        NotChecked();
                                                                    }
                                                                }
                                                            }
        );
    }

    private void Checked() {
        checked = true;
        binding.etPrice.setEnabled(false);
        binding.etPrice.setClickable(false);
        binding.etPrice.setFocusable(false);
        binding.etPrice.setText("");
        binding.etProcessingFees.setEnabled(false);
        binding.etProcessingFees.setClickable(false);
        binding.etProcessingFees.setFocusable(false);
        binding.etProcessingFees.setText("");
        binding.etRent.setEnabled(false);
        binding.etRent.setClickable(false);
        binding.etRent.setFocusable(false);
        binding.etRent.setText("");
        binding.etIntersetRate.setEnabled(false);
        binding.etIntersetRate.setClickable(false);
        binding.etIntersetRate.setFocusable(false);
        binding.etIntersetRate.setText("");
        binding.spinnerTransactionType.setEnabled(false);
        binding.spinnerTransactionType.setClickable(false);
        binding.spinnerTransactionType.setFocusable(false);
        binding.spinnerTransactionType.setPrompt("");
        binding.etLoanPer.setEnabled(false);
        binding.etLoanPer.setClickable(false);
        binding.etLoanPer.setFocusable(false);
        binding.etLoanPer.setText("");
        binding.etLabourRate.setEnabled(false);
        binding.etLabourRate.setClickable(false);
        binding.etLabourRate.setFocusable(false);
        binding.etLabourRate.setText("");
        binding.etUsernamee.setEnabled(false);
        binding.etUsernamee.setClickable(false);
        binding.etUsernamee.setFocusable(false);
        binding.etUsernamee.setText("");
        binding.etUsernameGatepass.setEnabled(false);
        binding.etUsernameGatepass.setClickable(false);
        binding.etUsernameGatepass.setFocusable(false);
        binding.etUsernameGatepass.setText("");
        binding.etColdWin.setEnabled(false);
        binding.etColdWin.setClickable(false);
        binding.etColdWin.setFocusable(false);
        binding.etColdWin.setText("");
        binding.etTallyPurchasee.setEnabled(false);
        binding.etTallyPurchasee.setClickable(false);
        binding.etTallyPurchasee.setFocusable(false);
        binding.etTallyPurchasee.setText("");
        binding.etTallyLoan.setEnabled(false);
        binding.etTallyLoan.setClickable(false);
        binding.etTallyLoan.setFocusable(false);
        binding.etTallyLoan.setText("");
        binding.etTallySale.setEnabled(false);
        binding.etTallySale.setClickable(false);
        binding.etTallySale.setFocusable(false);
        binding.etTallySale.setText("");
    }

    private void NotChecked() {
        checked = false;
        binding.etTallySale.setEnabled(true);
        binding.etTallySale.setClickable(true);
        binding.etTallySale.setFocusable(true);
        binding.etTallySale.setFocusableInTouchMode(true);
        binding.etTallyLoan.setEnabled(true);
        binding.etTallyLoan.setClickable(true);
        binding.etTallyLoan.setFocusable(true);
        binding.etTallyLoan.setFocusableInTouchMode(true);
        binding.etTallyPurchasee.setEnabled(true);
        binding.etTallyPurchasee.setClickable(true);
        binding.etTallyPurchasee.setFocusable(true);
        binding.etTallyPurchasee.setFocusableInTouchMode(true);
        binding.etColdWin.setEnabled(true);
        binding.etColdWin.setClickable(true);
        binding.etColdWin.setFocusable(true);
        binding.etColdWin.setFocusableInTouchMode(true);
        binding.etUsernameGatepass.setEnabled(true);
        binding.etUsernameGatepass.setClickable(true);
        binding.etUsernameGatepass.setFocusable(true);
        binding.etUsernameGatepass.setFocusableInTouchMode(true);
        binding.etUsernamee.setEnabled(true);
        binding.etUsernamee.setClickable(true);
        binding.etUsernamee.setFocusable(true);
        binding.etUsernamee.setFocusableInTouchMode(true);
        binding.etLabourRate.setEnabled(true);
        binding.etLabourRate.setClickable(true);
        binding.etLabourRate.setFocusable(true);
        binding.etLabourRate.setFocusableInTouchMode(true);
        binding.etLoanPer.setEnabled(true);
        binding.etLoanPer.setClickable(true);
        binding.etLoanPer.setFocusable(true);
        binding.etLoanPer.setFocusableInTouchMode(true);
        binding.etPrice.setEnabled(true);
        binding.etPrice.setClickable(true);
        binding.etPrice.setFocusable(true);
        binding.etPrice.setFocusableInTouchMode(true);
        binding.etProcessingFees.setEnabled(true);
        binding.etProcessingFees.setClickable(true);
        binding.etProcessingFees.setFocusable(true);
        binding.etProcessingFees.setFocusableInTouchMode(true);
        binding.etRent.setEnabled(true);
        binding.etRent.setClickable(true);
        binding.etRent.setFocusable(true);
        binding.etRent.setFocusableInTouchMode(true);
        binding.etIntersetRate.setEnabled(true);
        binding.etIntersetRate.setClickable(true);
        binding.etIntersetRate.setFocusable(true);
        binding.etIntersetRate.setFocusableInTouchMode(true);
        binding.spinnerTransactionType.setEnabled(true);
        binding.spinnerTransactionType.setClickable(true);
        binding.spinnerTransactionType.setFocusable(true);
        binding.spinnerTransactionType.setFocusableInTouchMode(true);
    }

    private void setValueOnSpinner() {
        binding.spinnerTransactionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    selectPurpose = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // can leave this empty
            }
        });

    }

    private void clickListner() {
        binding.ivClose.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.btn_login:
                Utility.showDecisionDialog(SetPricingClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        if (isValid()) {
                            if (checked) {
                                if (binding.notes.getText().toString().trim().isEmpty()) {
                                    Toast.makeText(SetPricingClass.this, "Enter Notes Here!!", Toast.LENGTH_LONG).show();
                                } else {
                                    callApi();
                                }
                            } else if (selectPurpose == null) {
                                Toast.makeText(SetPricingClass.this, "Select Transaction Type", Toast.LENGTH_LONG).show();
                            } else {
                                callApi();
                            }
                        }
                    }
                });
                break;
        }
    }

    private void callApi() {
        apiService.setPricing(new CreatePricingSetPostData(
                CaseID, stringFromView(binding.etPrice), stringFromView(binding.etProcessingFees),
                stringFromView(binding.etIntersetRate), stringFromView(binding.etLoanPer), selectPurpose, stringFromView(binding.etRent),
                vehhicleNO, stringFromView(binding.etLabourRate), stringFromView(binding.notes), stringFromView(binding.etUsernamee),
                stringFromView(binding.etUsernameGatepass), stringFromView(binding.etColdWin), stringFromView(binding.etTallyPurchasee), stringFromView(binding.etTallyLoan),
                stringFromView(binding.etTallySale))).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
            @Override
            protected void onSuccess(LoginResponse body) {
                Toast.makeText(SetPricingClass.this, body.getMessage(), Toast.LENGTH_LONG).show();
                startActivityAndClear(InPricingListingActivity.class);
            }
        });
    }

    boolean isValid() {
        if (checked) {

        } else {

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
