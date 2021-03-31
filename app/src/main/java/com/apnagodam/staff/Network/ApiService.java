package com.apnagodam.staff.Network;


import com.apnagodam.staff.Network.Request.ApprovedConveyancePOst;
import com.apnagodam.staff.Network.Request.ApprovedIntantionPOst;
import com.apnagodam.staff.Network.Request.ApprovedRejectConveyancePOst;
import com.apnagodam.staff.Network.Request.ApprovedRejectVendorConveyancePOst;
import com.apnagodam.staff.Network.Request.ApprovedVendorConveyancePOst;
import com.apnagodam.staff.Network.Request.AttendancePostData;
import com.apnagodam.staff.Network.Request.ClosedCasesPostData;
import com.apnagodam.staff.Network.Request.CreateCaseIDPostData;
import com.apnagodam.staff.Network.Request.CreateConveyancePostData;
import com.apnagodam.staff.Network.Request.CreateLeadsPostData;
import com.apnagodam.staff.Network.Request.CreatePricingSetPostData;
import com.apnagodam.staff.Network.Request.CreateVendorConveyancePostData;
import com.apnagodam.staff.Network.Request.DharmaKanthaPostData;
import com.apnagodam.staff.Network.Request.LoginPostData;
import com.apnagodam.staff.Network.Request.OTPData;
import com.apnagodam.staff.Network.Request.OTPGatePassData;
import com.apnagodam.staff.Network.Request.OTPVerifyGatePassData;
import com.apnagodam.staff.Network.Request.SelfRejectConveyancePOst;
import com.apnagodam.staff.Network.Request.SelfRejectVendorConveyancePOst;
import com.apnagodam.staff.Network.Request.StackPostData;
import com.apnagodam.staff.Network.Request.UpdateLeadsPostData;
import com.apnagodam.staff.Network.Request.UploadFirstQualityPostData;
import com.apnagodam.staff.Network.Request.UploadFirstkantaParchiPostData;
import com.apnagodam.staff.Network.Request.UploadGatePassPostData;
import com.apnagodam.staff.Network.Request.UploadGatePassPostDataNew;
import com.apnagodam.staff.Network.Request.UploadLabourDetailsPostData;
import com.apnagodam.staff.Network.Request.UploadReleaseOrderlsPostData;
import com.apnagodam.staff.Network.Request.UploadSecoundQualityPostData;
import com.apnagodam.staff.Network.Request.UploadSecoundkantaParchiPostData;
import com.apnagodam.staff.Network.Request.UploadTruckDetailsPostData;
import com.apnagodam.staff.Network.Response.AttendanceResponse;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.Network.Response.OTPvarifedResponse;
import com.apnagodam.staff.Network.Response.VersionCodeResponse;
import com.apnagodam.staff.module.AllCaseIDResponse;
import com.apnagodam.staff.module.AllConvancyList;
import com.apnagodam.staff.module.AllIntantionList;
import com.apnagodam.staff.module.AllLabourBookListResponse;
import com.apnagodam.staff.module.AllLeadsResponse;
import com.apnagodam.staff.module.AllLevelEmpListPojo;
import com.apnagodam.staff.module.AllTruckBookListResponse;
import com.apnagodam.staff.module.AllUserListPojo;
import com.apnagodam.staff.module.AllUserPermissionsResultListResponse;
import com.apnagodam.staff.module.AllVendorConvancyList;
import com.apnagodam.staff.module.AllpricingResponse;
import com.apnagodam.staff.module.CaseStatusINPojo;
import com.apnagodam.staff.module.CheckInventory;
import com.apnagodam.staff.module.CommudityResponse;
import com.apnagodam.staff.module.DashBoardData;
import com.apnagodam.staff.module.FirstQuilityReportListResponse;
import com.apnagodam.staff.module.FirstkanthaParchiListResponse;
import com.apnagodam.staff.module.GatePassListResponse;
import com.apnagodam.staff.module.GatePassPDFPojo;
import com.apnagodam.staff.module.MobileNUmberPojo;
import com.apnagodam.staff.module.ReleaseOrderPojo;
import com.apnagodam.staff.module.SecoundQuilityReportListResponse;
import com.apnagodam.staff.module.SecoundkanthaParchiListResponse;
import com.apnagodam.staff.module.SpotSellDealTrackPojo;
import com.apnagodam.staff.module.StackListPojo;
import com.apnagodam.staff.module.StateMapModel;
import com.apnagodam.staff.module.TerminalListPojo;
import com.apnagodam.staff.module.TransporterDetailsPojo;
import com.apnagodam.staff.module.TransporterListPojo;
import com.apnagodam.staff.module.VehcilePricingCheeck;
import com.apnagodam.staff.module.VendorExpensionApprovedListPojo;
import com.apnagodam.staff.module.VendorExpensionNamePojo;
import com.apnagodam.staff.module.VendorNamePojo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    // employee conveyance list
    @GET("emp_api/apna_emp_get_levelwiselist")
    Call<AllLevelEmpListPojo> getlevelwiselist();

    @GET("emp_api/apna_emp_get_conveyance")
    Call<AllConvancyList> getConvancyList(@Query("limit") String limit, @Query("page") int page, @Query("search") String search);

    @POST("emp_api/apna_emp_conveyance_delete")
    Call<LoginResponse> empyConveyanceDelate(@Body SelfRejectConveyancePOst selfRejectConveyancePOst);

    @POST("emp_api/apna_emp_conveyance_create")
    Call<LoginResponse> doCreateConveyance(@Body CreateConveyancePostData createConveyancePostData);

    @GET("emp_api/apna_emp_get_conveyance_req")
    Call<AllConvancyList> getApprovalRequestConvancyList(@Query("limit") String limit, @Query("page") int page, @Query("search") String search);


    //  reject by approved person
    @POST("emp_api/apna_emp_conveyance_reject")
    Call<LoginResponse> RejectConveyanceDelate(@Body ApprovedRejectConveyancePOst approvedRejectConveyancePOst);

    //  approved  by approved person
    @POST("emp_api/apna_emp_conveyance_verify")
    Call<LoginResponse> ApproveConveyanceDelate(@Body ApprovedConveyancePOst approvedConveyancePOst);


    @GET("api/app_version")
    Call<VersionCodeResponse> getversionCode(@Query("app_type") String appType);

    // permission all
    @GET("emp_api/apna_emp_permissions")
    Call<AllUserPermissionsResultListResponse> getPermission(@Query("designation_id") String designation_id, @Query("emp_level_id") String emp_level_id);

    // leads
    @GET("emp_api/apna_emp_leads")
    Call<AllLeadsResponse> getAllLeads(@Query("limit") String limit, @Query("page") int page_no, @Query("in_out") String in_out, @Query("search") String search);

    @POST("emp_api/apna_emp_create_lead")
    Call<LoginResponse> doCreateLeads(@Body CreateLeadsPostData createLeadsPostData);


    @POST("emp_api/apna_emp_update_lead")
    Call<LoginResponse> updateLeads(@Body UpdateLeadsPostData createLeadsPostData);

    //caseID
    @POST("emp_api/apna_emp_create_caseid")
    Call<LoginResponse> doCreateCaseID(@Body CreateCaseIDPostData createCaseIDPostData);

    @GET("emp_api/apna_emp_get_caseid")
    Call<AllCaseIDResponse> getAllCase(@Query("limit") String limit, @Query("page") int page_no, @Query("status") String status, @Query("search") String search);

    @GET("emp_api/apna_emp_levelwise_terminal")
    Call<TerminalListPojo> getTerminalListLevel();

    @GET("emp_api/apna_emp_transpoter_name")
    Call<TransporterListPojo> getTransporterList();

    @GET("emp_api/apna_emp_transpoter_detail")
    Call<TransporterDetailsPojo> getTransporterDetails(@Query("transport_id") String limit);

    // pricing
    @GET("emp_api/apna_emp_get_pricing")
    Call<AllpricingResponse> getAllpricingList(@Query("limit") String limit, @Query("page") int page_no, @Query("in_out") String in_out, @Query("search") String search);

    @GET("emp_api/apna_emp_check_vehicleno")
    Call<VehcilePricingCheeck> cheeckvehiclePricicng(@Query("case_id") String case_id);

    @POST("emp_api/apna_emp_close_case")
    Call<LoginResponse> doClosedCase(@Body ClosedCasesPostData closedCasesPostData);

    @POST("emp_api/apna_emp_addPrice")
    Call<LoginResponse> setPricing(@Body CreatePricingSetPostData createPricingSetPostData);

    // tuck book
    @GET("emp_api/apna_emp_get_truckbook")
    Call<AllTruckBookListResponse> getTruckBookList(@Query("limit") String limit, @Query("page") int page, @Query("in_out") String in_out, @Query("search") String search);

    @POST("emp_api/apna_emp_update_truckbook")
    Call<LoginResponse> uploadTruckDetails(@Body UploadTruckDetailsPostData uploadTruckDetailsPostData);

    // labour book
    @GET("emp_api/apna_emp_get_labourbook")
    Call<AllLabourBookListResponse> getLabourBookList(@Query("limit") String limit, @Query("page") String page_no, @Query("in_out") String in_out, @Query("search") String search);

    @POST("emp_api/apna_emp_update_labour")
    Call<LoginResponse> uploadLabourDetails(@Body UploadLabourDetailsPostData uploadLabourDetailsPostData);

    // first katha parchi
    @GET("emp_api/apna_emp_get_kanta_prachi")
    Call<FirstkanthaParchiListResponse> getf_kanthaParchiList(@Query("limit") String limit, @Query("page") String page_no, @Query("in_out") String in_out, @Query("search") String search);

    @POST("emp_api/apna_emp_kanta_parchi")
    Call<LoginResponse> uploadFirstkantaParchi(@Body UploadFirstkantaParchiPostData uploadFirstkantaParchiPostData);

    // first quality reports
    @GET("emp_api/apna_emp_get_quality")
    Call<FirstQuilityReportListResponse> getf_qualityReportsList(@Query("limit") String limit, @Query("page") String page_no, @Query("in_out") String in_out, @Query("search") String search);

    @POST("emp_api/apna_emp_f_quality")
    Call<LoginResponse> uploadFirstQualityReports(@Body UploadFirstQualityPostData uploadFirstQualityPostData);

    // Second katha parchi
    @GET("emp_api/apna_emp_get_s_k_p")
    Call<SecoundkanthaParchiListResponse> getS_kanthaParchiList(@Query("limit") String limit, @Query("page") String page_no, @Query("in_out") String in_out, @Query("search") String search);

    @POST("emp_api/apna_emp_s_kanta_parchi")
    Call<LoginResponse> uploadSecoundkantaParchi(@Body UploadSecoundkantaParchiPostData uploadSecoundkantaParchiPostData);

    // Second quality reports
    @GET("emp_api/apna_emp_get_s_quality")
    Call<SecoundQuilityReportListResponse> getS_qualityReportsList(@Query("limit") String limit, @Query("page") String page_no, @Query("in_out") String in_out, @Query("search") String search);

    @POST("emp_api/apna_emp_s_quality")
    Call<LoginResponse> uploadSecoundQualityReports(@Body UploadSecoundQualityPostData uploadSecoundQualityPostData);

    // gate pass
    @GET("emp_api/apna_emp_get_gatepass")
    Call<GatePassListResponse> getGatePass(@Query("limit") String limit, @Query("page") String page_no, @Query("in_out") String in_out, @Query("search") String search);

    @POST("emp_api/apna_emp_gatepass")
    Call<LoginResponse> uploadGatePass(@Body UploadGatePassPostData uploadGatePassPostData);

    @POST("emp_api/apna_emp_gatepass")
    Call<LoginResponse> uploadGatePassNew(@Body UploadGatePassPostDataNew uploadGatePassPostDataNew);

    // deal track on spot sell
    @GET("emp_api/apna_emp_get_spot_deals")
    Call<SpotSellDealTrackPojo> getSpotSellDealTrackList(@Query("limit") String limit, @Query("page") int page, @Query("search") String search);

    /// vendor lIST
    @GET("emp_api/apna_amp_vendor_list")
    Call<VendorNamePojo> getvendorUserList();

    //  EXPENSION LIST
    @GET("emp_api/apna_emp_vendor_exp_list")
    Call<VendorExpensionNamePojo> ExpensionList(@Query("phone_no") String phone_no);

    // location or tereminal list
    @GET("emp_api/apna_emp_vendor_terminal_list")
    Call<VendorExpensionNamePojo> TerminalList(@Query("phone_no") String phone_no);

    //  approved LIST
    @GET("emp_api/apna_emp_vendor_approveBy")
    Call<VendorExpensionApprovedListPojo> ExpensionApprovedList(@Query("exp_id") String exp_id, @Query("charge_amount") String charge_amount);

    // vendor voucher list
    @GET("emp_api/apna_emp_vendor_voucher_list")
    Call<AllVendorConvancyList> getVendorConvancyList(@Query("limit") String limit, @Query("page") int page, @Query("search") String search);

    @POST("emp_api/apna_emp_vendor_voucher_create")
    Call<LoginResponse> doVendorCreateConveyance(@Body CreateVendorConveyancePostData createVendorConveyancePostData);

    // self dealte /reject vendsor voacher
    @POST("emp_api/apna_emp_vendor_voucher_delete")
    Call<LoginResponse> vendorConveyanceDelate(@Body SelfRejectVendorConveyancePOst selfRejectVendorConveyancePOst);

    @GET("emp_api/apna_emp_vendor_voucher_requestList")
    Call<AllVendorConvancyList> getVendorApprovalRequestConvancyList(@Query("limit") String limit, @Query("page") int page, @Query("search") String search);

    //  reject byapproved person
    @POST("emp_api/apna_emp_vendor_voucher_reject")
    Call<LoginResponse> vendorRejectConveyanceDelate(@Body ApprovedRejectVendorConveyancePOst approvedRejectVendorConveyancePOst);

    //  approved  byapproved person
    @POST("emp_api/apna_emp_vendor_voucher_approve")
    Call<LoginResponse> vendorApproveConveyanceDelate(@Body ApprovedVendorConveyancePOst approvedVendorConveyancePOst);

    // get intention to buyer list
    @GET("emp_api/apna_emp_get_intentionToSell_request")
    Call<AllIntantionList> getintentionList(@Query("limit") String limit, @Query("page") int page, @Query("search") String search);

    // approve imitation
    @POST("emp_api/apna_emp_intentiontosell_approve")
    Call<LoginResponse> ApproveIntantion(@Body ApprovedIntantionPOst approvedIntantionPOst);

    // approve intation
    @POST("emp_api/apna_emp_intentiontosell_reject")
    Call<LoginResponse> rejeectIntantion(@Body ApprovedIntantionPOst approvedIntantionPOst);

    @GET("emp_api/apna_emp_statewise_user_heatmap")
    Call<StateMapModel> apnaEmpStatewiseUserHeatmap(@Query("state_name") String state);
    @GET("emp_api/apna_emp_get_user_location")
    Call<MobileNUmberPojo> routeMap(@Query("phone_no") String phone_no);

    @GET("emp_api/apna_emp_dhramkanta_list")
    Call<DharmaKanthaPostData> getDharmaKanthaListLevel();

    @POST("emp_api/apna_emp_gatepass_otp")
    Call<LoginResponse> doGatePassOTP(@Body OTPGatePassData otpGatePassData);

    @POST("emp_api/apna_emp_gatepass_otp_verification")
    Call<LoginResponse> doVerifyGatePassOTP(@Body OTPVerifyGatePassData otpVerifyGatePassData);
    @GET("emp_api/apna_emp_generate_gate_pass")
    Call<GatePassPDFPojo> getGatePassPDf(@Query("case_id") String case_id);
    @GET("emp_api/apna_emp_get_user_data")
    Call<AllUserListPojo> getUserList();
    @GET("emp_api/apna_emp_get_InOutInventoryDetail")
    Call<CheckInventory> checkWeightBag(@Query("case_id") String case_id);

    @POST("emp_api/apna_emp_get_stack_number")
    Call<StackListPojo> getStackList(@Body StackPostData stackPostData);

    @GET("emp_api/apna_emp_get_Incasestatus")
    Call<CaseStatusINPojo> getCaseStatusList(@Query("limit") String limit, @Query("search") String search);

    // release order
    @GET("emp_api/apna_emp_release_order")
    Call<ReleaseOrderPojo> getReleaseOrderList(@Query("limit") String limit, @Query("page") int page, @Query("in_out") String in_out, @Query("search") String search);
    @POST("emp_api/apna_emp_update_releaseorder")
    Call<LoginResponse> uploadRleaseOrder(@Body UploadReleaseOrderlsPostData uploadReleaseOrderlsPostData);


    // delivered  order
    @GET("emp_api/apna_emp_delivery_order")
    Call<ReleaseOrderPojo> getDeliveredOrderList(@Query("limit") String limit, @Query("page") int page, @Query("in_out") String in_out, @Query("search") String search);
    @POST("emp_api/apna_emp_update_deliverorder")
    Call<LoginResponse> uploadDeliveredOrder(@Body UploadReleaseOrderlsPostData uploadReleaseOrderlsPostData);


}


