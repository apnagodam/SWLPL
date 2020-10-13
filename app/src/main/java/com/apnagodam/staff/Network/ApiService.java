package com.apnagodam.staff.Network;


import com.apnagodam.staff.Network.Request.AttendancePostData;
import com.apnagodam.staff.Network.Request.ClosedCasesPostData;
import com.apnagodam.staff.Network.Request.CreateCaseIDPostData;
import com.apnagodam.staff.Network.Request.CreateLeadsPostData;
import com.apnagodam.staff.Network.Request.CreatePricingSetPostData;
import com.apnagodam.staff.Network.Request.LoginPostData;
import com.apnagodam.staff.Network.Request.OTPData;
import com.apnagodam.staff.Network.Request.UpdateLeadsPostData;
import com.apnagodam.staff.Network.Request.UploadFirstQualityPostData;
import com.apnagodam.staff.Network.Request.UploadFirstkantaParchiPostData;
import com.apnagodam.staff.Network.Request.UploadGatePassPostData;
import com.apnagodam.staff.Network.Request.UploadLabourDetailsPostData;
import com.apnagodam.staff.Network.Request.UploadSecoundQualityPostData;
import com.apnagodam.staff.Network.Request.UploadSecoundkantaParchiPostData;
import com.apnagodam.staff.Network.Request.UploadTruckDetailsPostData;
import com.apnagodam.staff.Network.Response.AttendanceResponse;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.Network.Response.OTPvarifedResponse;
import com.apnagodam.staff.Network.Response.VersionCodeResponse;
import com.apnagodam.staff.module.AllCaseIDResponse;
import com.apnagodam.staff.module.AllLabourBookListResponse;
import com.apnagodam.staff.module.AllLeadsResponse;
import com.apnagodam.staff.module.AllTruckBookListResponse;
import com.apnagodam.staff.module.AllUserPermissionsResultListResponse;
import com.apnagodam.staff.module.AllpricingResponse;
import com.apnagodam.staff.module.CommudityResponse;
import com.apnagodam.staff.module.DashBoardData;
import com.apnagodam.staff.module.FirstQuilityReportListResponse;
import com.apnagodam.staff.module.FirstkanthaParchiListResponse;
import com.apnagodam.staff.module.GatePassListResponse;
import com.apnagodam.staff.module.GetPassID;
import com.apnagodam.staff.module.InventoryRespionse;
import com.apnagodam.staff.module.SecoundQuilityReportListResponse;
import com.apnagodam.staff.module.SecoundkanthaParchiListResponse;
import com.apnagodam.staff.module.TerminalResponse;
import com.apnagodam.staff.module.VehcilePricingCheeck;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/apna_default_list")
    Call<CommudityResponse> getcommuydity_terminal_user_emp_listing(@Query("app_type") String appType);

    @POST("api/apna_send_otp")
    Call<LoginResponse> doLogin(@Body LoginPostData loginPostData);

    @POST("api/apna_verify_otp")
    Call<OTPvarifedResponse> doOTPVerify(@Body OTPData otpData);

    // for attendance Employee
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

    // permission all
    @GET("emp_api/apna_emp_permissions")
    Call<AllUserPermissionsResultListResponse> getPermission(@Query("designation_id") String designation_id, @Query("emp_level_id") String emp_level_id);

    // leads
    @GET("emp_api/apna_emp_leads")
    Call<AllLeadsResponse> getAllLeads(@Query("limit") String limit, @Query("page") int page_no,@Query("in_out") String in_out);

    @POST("emp_api/apna_emp_create_lead")
    Call<LoginResponse> doCreateLeads(@Body CreateLeadsPostData createLeadsPostData);

    @POST("emp_api/apna_emp_update_lead")
    Call<LoginResponse> updateLeads(@Body UpdateLeadsPostData createLeadsPostData);

    //caseID
    @POST("emp_api/apna_emp_create_caseid")
    Call<LoginResponse> doCreateCaseID(@Body CreateCaseIDPostData createCaseIDPostData);

    @GET("emp_api/apna_emp_get_caseid")
    Call<AllCaseIDResponse> getAllCase(@Query("limit") String limit, @Query("page") int page_no,@Query("status") String status);

    @GET("emp_api/apna_emp_getpass_no")
    Call<GetPassID> getGatePass(@Query("terminal_id") String terminal_id);

    // pricing
    @GET("emp_api/apna_emp_get_pricing")
    Call<AllpricingResponse> getAllpricingList(@Query("limit") String limit, @Query("page") int page_no,@Query("in_out") String in_out);

    @GET("emp_api/apna_emp_check_vehicleno")
    Call<VehcilePricingCheeck> cheeckvehiclePricicng(@Query("case_id") String case_id);

    @POST("emp_api/apna_emp_close_case")
    Call<LoginResponse> doClosedCase(@Body ClosedCasesPostData closedCasesPostData);

    @POST("emp_api/apna_emp_addPrice")
    Call<LoginResponse> setPricing(@Body CreatePricingSetPostData createPricingSetPostData);

    // tuck book
    @GET("emp_api/apna_emp_get_truckbook")
    Call<AllTruckBookListResponse> getTruckBookList(@Query("limit") String limit, @Query("page") int page,@Query("in_out") String in_out);

    @POST("emp_api/apna_emp_update_truckbook")
    Call<LoginResponse> uploadTruckDetails(@Body UploadTruckDetailsPostData uploadTruckDetailsPostData);

    // labour book
    @GET("emp_api/apna_emp_get_labourbook")
    Call<AllLabourBookListResponse> getLabourBookList(@Query("limit") String limit, @Query("page") String page_no,@Query("in_out") String in_out);

    @POST("emp_api/apna_emp_update_labour")
    Call<LoginResponse> uploadLabourDetails(@Body UploadLabourDetailsPostData uploadLabourDetailsPostData);

    // first katha parchi
    @GET("emp_api/apna_emp_get_kanta_prachi")
    Call<FirstkanthaParchiListResponse> getf_kanthaParchiList(@Query("limit") String limit, @Query("page") String page_no,@Query("in_out") String in_out);

    @POST("emp_api/apna_emp_kanta_parchi")
    Call<LoginResponse> uploadFirstkantaParchi(@Body UploadFirstkantaParchiPostData uploadFirstkantaParchiPostData);

    // first quality reports
    @GET("emp_api/apna_emp_get_quality")
    Call<FirstQuilityReportListResponse> getf_qualityReportsList(@Query("limit") String limit, @Query("page") String page_no,@Query("in_out") String in_out);

    @POST("emp_api/apna_emp_f_quality")
    Call<LoginResponse> uploadFirstQualityReports(@Body UploadFirstQualityPostData uploadFirstQualityPostData);

    // Second katha parchi
    @GET("emp_api/apna_emp_get_s_k_p")
    Call<SecoundkanthaParchiListResponse> getS_kanthaParchiList(@Query("limit") String limit, @Query("page") String page_no,@Query("in_out") String in_out);

    @POST("emp_api/apna_emp_s_kanta_parchi")
    Call<LoginResponse> uploadSecoundkantaParchi(@Body UploadSecoundkantaParchiPostData uploadSecoundkantaParchiPostData);

    // Second quality reports
    @GET("emp_api/apna_emp_get_s_quality")
    Call<SecoundQuilityReportListResponse> getS_qualityReportsList(@Query("limit") String limit, @Query("page") String page_no,@Query("in_out") String in_out);

    @POST("emp_api/apna_emp_s_quality")
    Call<LoginResponse> uploadSecoundQualityReports(@Body UploadSecoundQualityPostData uploadSecoundQualityPostData);

    // gate pass
    @GET("emp_api/apna_emp_get_gatepass")
    Call<GatePassListResponse> getGatePass(@Query("limit") String limit, @Query("page") String page_no,@Query("in_out") String in_out);

    @POST("emp_api/apna_emp_gatepass")
    Call<LoginResponse> uploadGatePass(@Body UploadGatePassPostData uploadGatePassPostData);
}


