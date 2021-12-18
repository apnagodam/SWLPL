package com.apnagodam.staff.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.CoWinNetworkCallback;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.CreateConveyancePostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.convancy_voachar.ApprovalRequestConveyanceListClass;
import com.apnagodam.staff.activity.convancy_voachar.MyConveyanceListClass;
import com.apnagodam.staff.adapter.ConvancyListAdpter;
import com.apnagodam.staff.adapter.CowinListAdpter;
import com.apnagodam.staff.databinding.ActivityEmpConveyanceBinding;
import com.apnagodam.staff.databinding.ActivityEmpCowinDetailsBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.AllConvancyList;
import com.apnagodam.staff.module.AllLevelEmpListPojo;
import com.apnagodam.staff.module.CowinPojo;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CoWinVactionClass extends BaseActivity<ActivityEmpCowinDetailsBinding> {
    String CowinDistrictID = null;
    String CowinDistricVallue="";
    private Calendar calender;
    private CowinListAdpter cowinListAdpter;
    private List<CowinPojo.Center> getOrdersList;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_emp_cowin_details;
    }
    @Override
    protected void setUp() {
        getOrdersList = new ArrayList();
        calender = Calendar.getInstance();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // get approve person  list
        clickListner();
        setDataAdapter();

        binding.spinnerConvType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    CowinDistrictID = parent.getItemAtPosition(position).toString();
                    if (CowinDistrictID.equalsIgnoreCase("Jaipur-1")) {
                                CowinDistricVallue = "505";
                    } else {
                                CowinDistricVallue = "506";
                    }

                } else {
                                     CowinDistrictID=null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // can leave this empty
            }
        });

    }
    private void setDataAdapter() {
        binding.fieldStockList.addItemDecoration(new DividerItemDecoration(CoWinVactionClass.this, LinearLayoutManager.HORIZONTAL));
        binding.fieldStockList.setHasFixedSize(true);
        binding.fieldStockList.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(CoWinVactionClass.this, LinearLayoutManager.VERTICAL, false);
        binding.fieldStockList.setLayoutManager(horizontalLayoutManager);
        cowinListAdpter = new CowinListAdpter(getOrdersList, CoWinVactionClass.this);
        binding.fieldStockList.setAdapter(cowinListAdpter);
    }

    private void clickListner() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(StaffDashBoardActivity.class);
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            if (TextUtils.isEmpty(stringFromView(binding.userCommitmentDate))) {
                                Toast.makeText(CoWinVactionClass.this, "Select Booking Date", Toast.LENGTH_LONG).show();
                            } else if (CowinDistrictID == null) {
                                Toast.makeText(getApplicationContext(),"Select District Name ", Toast.LENGTH_LONG).show();
                            }  else {
                                onNext();
                            }            }
        });
        binding.lpCommiteDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpDatePicker();
            }
        });
        binding.userCommitmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpDatePicker();
            }
        });
    }

    public void popUpDatePicker() {
        calender = Calendar.getInstance();
        DatePickerDialog dateDialog = new DatePickerDialog(CoWinVactionClass.this, date, calender
                .get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH));
       // calender.add(Calendar.DATE, -1); // subtract 1 day from Today
        dateDialog.getDatePicker().setMinDate(calender.getTimeInMillis());
       // dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dateDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calender.set(Calendar.YEAR, year);
            calender.set(Calendar.MONTH, monthOfYear);
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd-MM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            binding.userCommitmentDate.setText(sdf.format(calender.getTime()).toString());
        }
    };



    // update file
    public void onNext() {
        CowinServices.getCowinData(CowinDistricVallue,stringFromView(binding.userCommitmentDate)).enqueue(new CoWinNetworkCallback<CowinPojo>(getActivity()) {
            @Override
            protected void onSuccess(CowinPojo body) {
                getOrdersList.clear();
                if (body.getCenters().size() == 0) {
                    binding.fieldStockList.setVisibility(View.GONE);
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                } else {
                    binding.fieldStockList.setVisibility(View.VISIBLE);
                    getOrdersList.addAll(body.getCenters());
                    cowinListAdpter.notifyDataSetChanged();
                }
            }
        });
    }

}
