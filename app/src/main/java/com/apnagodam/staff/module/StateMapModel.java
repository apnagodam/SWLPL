package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StateMapModel extends BaseResponse {

    @SerializedName("data")
    @Expose
    public List<Datum> data = null;
    @SerializedName("userDetail")
    @Expose
    public List<UserDetail> userDetail = null;


    public class Datum {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("mandi_samiti_id")
        @Expose
        public String mandiSamitiId;
        @SerializedName("warehouse_code")
        @Expose
        public String warehouseCode;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("name_hi")
        @Expose
        public String nameHi;
        @SerializedName("facility_ids")
        @Expose
        public String facilityIds;
        @SerializedName("bank_ids")
        @Expose
        public String bankIds;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("gatepass_start")
        @Expose
        public String gatepassStart;
        @SerializedName("gatepass_end")
        @Expose
        public String gatepassEnd;
        @SerializedName("no_of_stacks")
        @Expose
        public String noOfStacks;
        @SerializedName("dharam_kanta")
        @Expose
        public String dharamKanta;
        @SerializedName("labour_contractor")
        @Expose
        public String labourContractor;
        @SerializedName("contractor_phone")
        @Expose
        public String contractorPhone;
        @SerializedName("labour_rate")
        @Expose
        public String labourRate;
        @SerializedName("latitude")
        @Expose
        public double latitude;
        @SerializedName("longitude")
        @Expose
        public double longitude;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;


    }


    public class UserDetail {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("user_type")
        @Expose
        public String userType;
        @SerializedName("fname")
        @Expose
        public String fname;
        @SerializedName("lname")
        @Expose
        public String lname;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("phone")
        @Expose
        public String phone;
        @SerializedName("referral_code")
        @Expose
        public String referralCode;
        @SerializedName("referral_by")
        @Expose
        public String referralBy;
        @SerializedName("father_name")
        @Expose
        public String fatherName;
        @SerializedName("category")
        @Expose
        public String category;
        @SerializedName("gst_number")
        @Expose
        public String gstNumber;
        @SerializedName("khasra_no")
        @Expose
        public String khasraNo;
        @SerializedName("village")
        @Expose
        public String village;
        @SerializedName("tehsil")
        @Expose
        public String tehsil;
        @SerializedName("district")
        @Expose
        public String district;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("power")
        @Expose
        public String power;
        @SerializedName("aadhar_no")
        @Expose
        public String aadharNo;
        @SerializedName("pancard_no")
        @Expose
        public String pancardNo;
        @SerializedName("bank_name")
        @Expose
        public String bankName;
        @SerializedName("bank_branch")
        @Expose
        public String bankBranch;
        @SerializedName("bank_acc_no")
        @Expose
        public String bankAccNo;
        @SerializedName("bank_ifsc_code")
        @Expose
        public String bankIfscCode;
        @SerializedName("profile_image")
        @Expose
        public String profileImage;
        @SerializedName("aadhar_image")
        @Expose
        public String aadharImage;
        @SerializedName("cheque_image")
        @Expose
        public String chequeImage;
        @SerializedName("gst_image")
        @Expose
        public String gstImage;
        @SerializedName("pancard_image")
        @Expose
        public String pancardImage;
        @SerializedName("firm_name")
        @Expose
        public String firmName;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("area_vilage")
        @Expose
        public String areaVilage;
        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("state")
        @Expose
        public String state;
        @SerializedName("pincode")
        @Expose
        public String pincode;
        @SerializedName("mandi_license")
        @Expose
        public String mandiLicense;
        @SerializedName("latitude")
        @Expose
        public double latitude;
        @SerializedName("longitude")
        @Expose
        public double longitude;
        @SerializedName("live_latitude")
        @Expose
        public String liveLatitude;
        @SerializedName("live_longitude")
        @Expose
        public String liveLongitude;
        @SerializedName("transfer_amount")
        @Expose
        public String transferAmount;
        @SerializedName("verified_account")
        @Expose
        public String verifiedAccount;
        @SerializedName("approved_by")
        @Expose
        public String approvedBy;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;


    }
}


  

