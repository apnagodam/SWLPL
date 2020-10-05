package com.apnagodam.staff.Network;


import com.apnagodam.staff.Network.Request.AttendancePostData;
import com.apnagodam.staff.Network.Request.CreateCaseIDPostData;
import com.apnagodam.staff.Network.Request.CreateLeadsPostData;
import com.apnagodam.staff.Network.Request.LoginPostData;
import com.apnagodam.staff.Network.Request.OTPData;
import com.apnagodam.staff.Network.Response.AttendanceResponse;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.Network.Response.OTPvarifedResponse;
import com.apnagodam.staff.Network.Response.VersionCodeResponse;
import com.apnagodam.staff.module.AllCaseIDResponse;
import com.apnagodam.staff.module.AllLeadsResponse;
import com.apnagodam.staff.module.AllpricingResponse;
import com.apnagodam.staff.module.CommudityResponse;
import com.apnagodam.staff.module.DashBoardData;
import com.apnagodam.staff.module.GetPassID;
import com.apnagodam.staff.module.InventoryRespionse;
import com.apnagodam.staff.module.TerminalResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api/apna_send_otp")
    Call<LoginResponse> doLogin(@Body LoginPostData loginPostData);

    @POST("api/apna_verify_otp")
    Call<OTPvarifedResponse> doOTPVerify(@Body OTPData otpData);

    @GET("emp_api/apna_emp_clock_status")
    Call<AttendanceResponse> getattendanceStatus();

    @POST("emp_api/apna_emp_clock_in_out")
    Call<AttendanceResponse> attendance(@Body AttendancePostData attendancePostData);

    @POST("api/apna_user_logout")
    Call<LoginResponse> doLogout();

    @GET("emp_api/apna_emp_dashboard")
    Call<DashBoardData> getDashboardData();

    @GET("api/app_version")
    Call<VersionCodeResponse> getversionCode(@Query("app_type") String appType);

    @GET("emp_api/apna_emp_leads")
    Call<AllLeadsResponse> getAllLeads();

    @POST("emp_api/apna_emp_create_lead")
    Call<LoginResponse> doCreateLeads(@Body CreateLeadsPostData createLeadsPostData);
    @POST("emp_api/apna_emp_update_lead")
    Call<LoginResponse> updateLeads(@Body CreateLeadsPostData createLeadsPostData);
    @GET("api/apna_default_list")
    Call<CommudityResponse> getcommuydity_terminal_user_emp_listing();

    @POST("emp_api/apna_emp_create_caseid")
    Call<LoginResponse> doCreateCaseID(@Body CreateCaseIDPostData createCaseIDPostData);
    @GET("emp_api/apna_emp_get_caseid")
    Call<AllCaseIDResponse> getAllCase();
    @GET("emp_api/apna_emp_getpass_no")
    Call<GetPassID> getGatePass(@Query("terminal_id") String terminal_id);
    // pricing
    @GET("emp_api/apna_emp_get_pricing")
    Call<AllpricingResponse> getAllpricingList();
}


