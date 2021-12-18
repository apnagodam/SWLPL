package com.apnagodam.staff.activity.remoteaudit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.OtpActivity;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.adapter.HomeAdapter;
import com.apnagodam.staff.databinding.RemoteAuditBinding;
import com.apnagodam.staff.module.AssertPojo;

import java.util.ArrayList;

public class AllAsserstListClass extends BaseActivity<RemoteAuditBinding> implements HomeAdapter.ItemListener{
    private RecyclerView recyclerView;
    private ArrayList<AssertPojo> arrayList;
    @Override
    protected int getLayoutResId() {
        return R.layout.remote_audit;
    }

    @Override
    protected void setUp() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(StaffDashBoardActivity.class);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        arrayList = new ArrayList<>();

        arrayList.add(new AssertPojo("Visitor Register", R.drawable.ic_baseline_emoji_people_24, "#09A9FF"));
        arrayList.add(new AssertPojo("CCTV Register", R.drawable.cctv_24, "#3E51B1"));
        arrayList.add(new AssertPojo("Seal Register", R.drawable.seal_24, "#673BB7"));
        arrayList.add(new AssertPojo("Daily Activity Register", R.drawable.dailyactivity_24, "#4BAA50"));
        arrayList.add(new AssertPojo("Labour Register", R.drawable.labour, "#F94336"));
        arrayList.add(new AssertPojo("Kanta Register", R.drawable.hindi, "#0A9B88"));
        arrayList.add(new AssertPojo("Stationary Register", R.drawable.hindi, "#09A9FF"));
        arrayList.add(new AssertPojo("P.V. Register", R.drawable.hindi, "#3E51B1"));
        arrayList.add(new AssertPojo("CHR Register", R.drawable.hindi, "#673BB7"));
        arrayList.add(new AssertPojo("Cleaning Activity Register", R.drawable.hindi, "#4BAA50"));
        arrayList.add(new AssertPojo("Stock Register", R.drawable.hindi, "#F94336"));
        arrayList.add(new AssertPojo("Pledge Register", R.drawable.hindi, "#0A9B88"));
        arrayList.add(new AssertPojo("Madeup Activity Register", R.drawable.hindi, "#4BAA50"));
        arrayList.add(new AssertPojo("Fumigation Register", R.drawable.hindi, "#F94336"));
        arrayList.add(new AssertPojo("Spray Register", R.drawable.hindi, "#0A9B88"));

        HomeAdapter adapter = new HomeAdapter(this, arrayList, this);
        recyclerView.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void onItemClick(AssertPojo item) {
        Bundle bundle = new Bundle();
        bundle.putString("RegisterName", item.text);
        startActivity(CommonAuditFileUploadClass.class, bundle);
       // Toast.makeText(getApplicationContext(), item.text + " is clicked", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(StaffDashBoardActivity.class);
    }

}
