package com.apnagodam.staff.activity;

import android.content.Intent;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apnagodam.staff.ApnaGodamApp;
import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.R;
import com.apnagodam.staff.adapter.LanguageAdapter;
import com.apnagodam.staff.databinding.ActivityLanguageBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.helper.LocaleHelper;
import com.apnagodam.staff.utils.Utility;


public class LanguageActivity extends BaseActivity<ActivityLanguageBinding> implements LanguageAdapter.LanguageChangeCallback {
    String currentLanguage, selected;
    private ApnaGodamApp apnaGodamApp;
    CALLED_FROM called_from;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_language;
    }

    @Override
    protected void setUp() {
        apnaGodamApp = (ApnaGodamApp) getApplication();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        called_from = (CALLED_FROM) getIntent().getSerializableExtra(LanguageActivity.class.getSimpleName());
        binding.rvLanguage.setHasFixedSize(true);
        binding.rvLanguage.setLayoutManager(new LinearLayoutManager(this));
        currentLanguage = LocaleHelper.getLanguage(this);
        binding.rvLanguage.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvLanguage.setAdapter(new LanguageAdapter(this));

      /*  binding.tvDone.setOnClickListener(view -> {
            SharedPreferencesRepository.getDataManagerInstance().setLanguageSelected(true);
            setResult(RESULT_OK);
            if (called_from == CALLED_FROM.FROM_SPLASH) {
                finish();
            } else {
                // postSettingAfterLanguageSelect();
            }

           finish();
        });*/

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (called_from == CALLED_FROM.FROM_SPLASH) {
                    finish();
                }else
                {
                    onBackPressed();
                }

            }

        });
    }

    @Override
    public void onBackPressed() {
        LocaleHelper.setLocale(LanguageActivity.this, currentLanguage);
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onLanguageChange(String selected) {
        this.selected = selected;
        LocaleHelper.setLocale(LanguageActivity.this, selected);
        SharedPreferencesRepository.getDataManagerInstance().setLanguageSelected(true);
        SharedPreferencesRepository.getDataManagerInstance().setLanguageSelected(true);
        setResult(RESULT_OK);
        Utility.changeLanguage(this,selected);
        if (called_from == CALLED_FROM.FROM_SPLASH) {
            finish();
        } else {
            Intent intent = new Intent(getActivity(), SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
            getActivity().finish();
            getActivity().overridePendingTransition(0, 0);
        }
    }

    public enum CALLED_FROM {
        FROM_SETTING, FROM_SPLASH;
    }
}


