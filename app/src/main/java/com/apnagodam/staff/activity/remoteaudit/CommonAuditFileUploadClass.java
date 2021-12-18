package com.apnagodam.staff.activity.remoteaudit;

import android.os.Bundle;
import android.view.View;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.UploadRemoteBinding;

public class CommonAuditFileUploadClass extends BaseActivity<UploadRemoteBinding> {
    String ScreenName;

    @Override
    protected int getLayoutResId() {
        return R.layout.upload_remote;
    }

    @Override
    protected void setUp() {
        Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle != null) {
            ScreenName = bundle.getString("RegisterName");
        }
        binding.heading.setText("Upload " + " " + ScreenName);
        binding.addRegistor.setText("Add " + " " + ScreenName);
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(AllAsserstListClass.class);
            }
        });
    }
}
