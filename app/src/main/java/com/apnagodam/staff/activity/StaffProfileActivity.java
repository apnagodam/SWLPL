package com.apnagodam.staff.activity;

import android.view.View;
import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.GlideApp;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.StaffProfileBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.Constants;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class StaffProfileActivity extends BaseActivity<StaffProfileBinding> {
    @Override
    protected int getLayoutResId() {
        return R.layout.staff_profile;
    }

    @Override
    protected void setUp() {
        setBackBtn(binding.include.toolbar);
        binding.include.titleTxt.setText((getString(R.string.my_profile)));
        binding.include.tvDone.setVisibility(View.GONE);
        UserDetails userDetails = SharedPreferencesRepository.getDataManagerInstance().getUser();
        binding.userName.setText("" + userDetails.getFname()+" "+userDetails.getLname()+"("+userDetails.getEmp_id()+")");
        binding.StaffDesignation.setText("Designation: " + userDetails.getDesignation());
        binding.Address.setText("Location: " + userDetails.getAddress().toUpperCase());
        if (userDetails.getPancardNo() != null && !userDetails.getPancardNo().isEmpty()) {
            binding.pancardNo.setText("" + userDetails.getPancardNo().toUpperCase());
        }
        if (userDetails.getAadharNo() != null && !userDetails.getAadharNo().isEmpty()) {
            binding.aadharno.setText("" + userDetails.getAadharNo().toUpperCase());
        }
        if (userDetails.getBankAccNo() != null && !userDetails.getBankAccNo().isEmpty()) {
            binding.accountNo.setText("" + userDetails.getBankAccNo().toUpperCase());
        }
        binding.mobile.setText("" + userDetails.getPhone().toUpperCase());
        if (userDetails.getAadharImage()!=null&&!userDetails.getAadharImage().isEmpty()){
            binding.aadhar.setVisibility(View.VISIBLE);
            binding.cardAadhar.setVisibility(View.VISIBLE);
            GlideApp.with(binding.AaadharImage.getContext())
                    .load(Constants.IMAGE_BASE_URL+ userDetails.getAadharImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.AaadharImage);
        }
        if (userDetails.getChequeImage()!=null&&!userDetails.getChequeImage().isEmpty()){
            binding.passporttext.setVisibility(View.VISIBLE);
            binding.cardPassport.setVisibility(View.VISIBLE);
            GlideApp.with(binding.PassbookImage.getContext())
                    .load(Constants.IMAGE_BASE_URL+ userDetails.getChequeImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.PassbookImage);
        }
        if (userDetails.getProfileImage() != null && !userDetails.getProfileImage().isEmpty()) {
            GlideApp.with(binding.imgProfile.getContext())
                    .load(Constants.IMAGE_BASE_URL + userDetails.getProfileImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop()
                    .into(binding.imgProfile);
        } else {
            GlideApp.with(binding.imgProfile.getContext())
                    .load(Constants.IMAGE_BASE_URL + userDetails.getProfileImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.user_shape)
                    .circleCrop()
                    .into(binding.imgProfile);
        }
    }
}
