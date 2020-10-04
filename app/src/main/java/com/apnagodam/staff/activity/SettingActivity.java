package com.apnagodam.staff.activity;

import android.content.Intent;
import android.view.View;
import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.SettingsBinding;
import com.apnagodam.staff.utils.LocaleHelper;

public class SettingActivity extends BaseActivity<SettingsBinding> {
    boolean isEmail;
    boolean isNotEmail;
    private static final int CHANGE_LANGUAGE_CODE = 458;

    @Override
    protected int getLayoutResId() {
        return R.layout.settings;
    }

    @Override
    protected void setUp() {
        setBackBtn(binding.include.toolbar);
        binding.include.titleTxt.setText(getString(R.string.setting_title));
        binding.include.tvDone.setVisibility(View.GONE);
        binding.tvChangeMobileNumber.setOnClickListener(v -> startActivity(LoginActivity.class, "setting", "changeMobileNumber"));
        binding.tvCurrentLanguage.setText(LocaleHelper.getLanguageName(getActivity()));
        binding.tvCurrentLanguage.setOnClickListener(v -> startActivity(LanguageActivity.class));
        binding.suggestEmail.setOnClickListener(v -> onEmailClick());
        binding.notEmail.setOnClickListener(v -> onNotEmailClick());
    }

    public void onEmailClick() {
        isEmail = !isEmail;
        setEmail(isEmail);
        //postSetting();
    }



    public void onNotEmailClick() {
        isNotEmail = !isNotEmail;

        setNotEmail(isNotEmail);
       // postSetting();
    }
    private void setEmail(boolean flag) {
        if (flag) {
            binding.suggestEmail.setImageResource(R.drawable.on_btn);
            isEmail = true;
        } else {
            binding.suggestEmail.setImageResource(R.drawable.off_btn);
            isEmail = false;
        }
    }
    private void setNotEmail(boolean flag) {
        if (flag) {
            binding.notEmail.setImageResource(R.drawable.on_btn);
            isNotEmail = true;
        } else {
            binding.notEmail.setImageResource(R.drawable.off_btn);
            isNotEmail = false;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CHANGE_LANGUAGE_CODE) {
            if (resultCode == RESULT_OK) recreate();
            else finish();

        }
    }

}

