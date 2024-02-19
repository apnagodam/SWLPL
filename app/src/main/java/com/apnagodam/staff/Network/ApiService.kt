package com.apnagodam.staff.Network

import androidx.lifecycle.LiveData
import com.apnagodam.staff.Network.Request.ApprovedConveyancePOst
import com.apnagodam.staff.Network.Request.ApprovedIntantionPOst
import com.apnagodam.staff.Network.Request.ApprovedRejectConveyancePOst
import com.apnagodam.staff.Network.Request.ApprovedRejectVendorConveyancePOst
import com.apnagodam.staff.Network.Request.ApprovedVendorConveyancePOst
import com.apnagodam.staff.Network.Request.AttendancePostData
import com.apnagodam.staff.Network.Request.ClosedCasesPostData
import com.apnagodam.staff.Network.Request.CreateCaseIDPostData
import com.apnagodam.staff.Network.Request.CreateConveyancePostData
import com.apnagodam.staff.Network.Request.CreateLeadsPostData
import com.apnagodam.staff.Network.Request.CreatePricingSetPostData
import com.apnagodam.staff.Network.Request.CreateVendorConveyancePostData
import com.apnagodam.staff.Network.Request.DharmaKanthaPostData
import com.apnagodam.staff.Network.Request.LoginPostData
import com.apnagodam.staff.Network.Request.OTPData
import com.apnagodam.staff.Network.Request.OTPGatePassData
import com.apnagodam.staff.Network.Request.OTPVerifyGatePassData
import com.apnagodam.staff.Network.Request.RequestOfflineCaseData
import com.apnagodam.staff.Network.Request.SelfRejectConveyancePOst
import com.apnagodam.staff.Network.Request.SelfRejectVendorConveyancePOst
import com.apnagodam.staff.Network.Request.StackPostData
import com.apnagodam.staff.Network.Request.UpdateLeadsPostData
import com.apnagodam.staff.Network.Request.UploadFirstQualityPostData
import com.apnagodam.staff.Network.Request.UploadFirstkantaParchiPostData
import com.apnagodam.staff.Network.Request.UploadGatePassPostData
import com.apnagodam.staff.Network.Request.UploadGatePassPostDataNew
import com.apnagodam.staff.Network.Request.UploadLabourDetailsPostData
import com.apnagodam.staff.Network.Request.UploadReleaseOrderlsPostData
import com.apnagodam.staff.Network.Request.UploadSecoundQualityPostData
import com.apnagodam.staff.Network.Request.UploadSecoundkantaParchiPostData
import com.apnagodam.staff.Network.Request.UploadTruckDetailsPostData
import com.apnagodam.staff.Network.Response.AttendanceResponse
import com.apnagodam.staff.Network.Response.BaseResponse
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.Response.OTPvarifedResponse
import com.apnagodam.staff.Network.Response.ResponseFastcaseList
import com.apnagodam.staff.Network.Response.ResponseSendOtp
import com.apnagodam.staff.Network.Response.ResponseStackData
import com.apnagodam.staff.Network.Response.ResponseUserData
import com.apnagodam.staff.Network.Response.ResponseWarehouse
import com.apnagodam.staff.Network.Response.VerifyOtpFastcase
import com.apnagodam.staff.Network.Response.VersionCodeResponse
import com.apnagodam.staff.module.AllCaseIDResponse
import com.apnagodam.staff.module.AllConvancyList
import com.apnagodam.staff.module.AllIntantionList
import com.apnagodam.staff.module.AllLabourBookListResponse
import com.apnagodam.staff.module.AllLeadsResponse
import com.apnagodam.staff.module.AllLevelEmpListPojo
import com.apnagodam.staff.module.AllTruckBookListResponse
import com.apnagodam.staff.module.AllUserListPojo
import com.apnagodam.staff.module.AllUserPermissionsResultListResponse
import com.apnagodam.staff.module.AllVendorConvancyList
import com.apnagodam.staff.module.AllpricingResponse
import com.apnagodam.staff.module.CaseStatusINPojo
import com.apnagodam.staff.module.CheckInventory
import com.apnagodam.staff.module.CommodityResponseData
import com.apnagodam.staff.module.CommudityResponse
import com.apnagodam.staff.module.DashBoardData
import com.apnagodam.staff.module.FirstQuilityReportListResponse
import com.apnagodam.staff.module.FirstkanthaParchiListResponse
import com.apnagodam.staff.module.GatePassListResponse
import com.apnagodam.staff.module.GatePassPDFPojo
import com.apnagodam.staff.module.MobileNUmberPojo
import com.apnagodam.staff.module.ReleaseOrderPojo
import com.apnagodam.staff.module.SecoundQuilityReportListResponse
import com.apnagodam.staff.module.SecoundkanthaParchiListResponse
import com.apnagodam.staff.module.SpotSellDealTrackPojo
import com.apnagodam.staff.module.StackListPojo
import com.apnagodam.staff.module.StateMapModel
import com.apnagodam.staff.module.TerminalListPojo
import com.apnagodam.staff.module.TransporterDetailsPojo
import com.apnagodam.staff.module.TransporterListPojo
import com.apnagodam.staff.module.VehcilePricingCheeck
import com.apnagodam.staff.module.VendorExpensionApprovedListPojo
import com.apnagodam.staff.module.VendorExpensionNamePojo
import com.apnagodam.staff.module.VendorNamePojo
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("emp_api/apna_emp_conveyance_verify")
    fun ApproveConveyanceDelate(@Body approvedConveyancePOst: ApprovedConveyancePOst?): Call<LoginResponse?>?

    @POST("emp_api/apna_emp_intentiontosell_approve")
    fun ApproveIntantion(@Body approvedIntantionPOst: ApprovedIntantionPOst?): Call<LoginResponse?>?

    @GET("emp_api/apna_emp_vendor_approveBy")
    fun ExpensionApprovedList(
        @Query("exp_id") str: String?,
        @Query("charge_amount") str2: String?
    ): Observable<VendorExpensionApprovedListPojo>

    @GET("emp_api/apna_emp_vendor_exp_list")
    fun ExpensionList(@Query("phone_no") str: String?): Observable<VendorExpensionNamePojo>

    @POST("emp_api/apna_emp_conveyance_reject")
    fun RejectConveyanceDelate(@Body approvedRejectConveyancePOst: ApprovedRejectConveyancePOst?): Call<LoginResponse?>?

    @GET("emp_api/apna_emp_vendor_terminal_list")
    fun TerminalList(@Query("phone_no") str: String?): Observable<VendorExpensionNamePojo>

    @GET("emp_api/apna_emp_statewise_user_heatmap")
    fun apnaEmpStatewiseUserHeatmap(@Query("state_name") str: String?): Call<StateMapModel?>?

    @POST("emp_api/apna_emp_clock_in_out")
    fun attendance(@Body attendancePostData: AttendancePostData?): Observable<AttendanceResponse>

    @GET("emp_api/apna_emp_get_InOutInventoryDetail")
    fun checkWeightBag(@Query("case_id") str: String?): Observable<CheckInventory>

    @GET("emp_api/apna_emp_check_vehicleno")
    fun cheeckvehiclePricicng(@Query("case_id") str: String?): Call<VehcilePricingCheeck?>?

    @POST("emp_api/apna_emp_close_case")
    fun doClosedCase(@Body closedCasesPostData: ClosedCasesPostData?): Call<LoginResponse?>?

    @POST("emp_api/apna_emp_create_caseid")
    fun doCreateCaseID(@Body createCaseIDPostData: CreateCaseIDPostData?): Observable<LoginResponse>

    @POST("emp_api/apna_emp_conveyance_create")
    fun doCreateConveyance(@Body createConveyancePostData: CreateConveyancePostData?): Observable<LoginResponse>

    @POST("emp_api/apna_emp_create_lead")
    fun doCreateLeads(@Body createLeadsPostData: CreateLeadsPostData?): Observable<LoginResponse>

    @POST("emp_api/apna_emp_gatepass_otp")
    fun doGatePassOTP(@Body oTPGatePassData: OTPGatePassData?): Observable<LoginResponse>

    @POST("api/apna_send_otp")
   suspend fun doLogin(@Body loginPostData: LoginPostData): Response<LoginResponse>

    @POST("api/apna_user_logout")
    suspend fun doLogout(): Response<LoginResponse>

    @POST("api/apna_verify_otp")
   suspend fun doOTPVerify(@Body oTPData: OTPData): Response<OTPvarifedResponse>

    @POST("emp_api/apna_emp_vendor_voucher_create")
    fun doVendorCreateConveyance(@Body createVendorConveyancePostData: CreateVendorConveyancePostData?): Observable<LoginResponse>

    @POST("emp_api/apna_emp_gatepass_otp_verification")
    suspend fun doVerifyGatePassOTP(@Body oTPVerifyGatePassData: OTPVerifyGatePassData): Response<LoginResponse>

    @POST("emp_api/apna_emp_conveyance_delete")
    fun empyConveyanceDelate(@Body selfRejectConveyancePOst: SelfRejectConveyancePOst?): Observable<LoginResponse>

    @GET("emp_api/apna_emp_get_caseid")
    suspend fun getAllCase(
        @Query("limit") str: String?,
        @Query("page") i: Int,
        @Query("status") str2: String?,
        @Query("search") str3: String?
    ): Response<AllCaseIDResponse>

    @GET("emp_api/apna_emp_leads")
   suspend fun getAllLeads(
        @Query("limit") str: String?,
        @Query("page") i: Int,
        @Query("in_out") str2: String?,
        @Query("search") str3: String?
    ): Response<AllLeadsResponse>

    @GET("emp_api/apna_emp_get_pricing")
    fun getAllpricingList(
        @Query("limit") str: String?,
        @Query("page") i: Int,
        @Query("in_out") str2: String?,
        @Query("search") str3: String?
    ): Call<AllpricingResponse?>?

    @GET("emp_api/apna_emp_get_conveyance_req")
    fun getApprovalRequestConvancyList(
        @Query("limit") str: String?,
        @Query("page") i: Int,
        @Query("search") str2: String?
    ): Call<AllConvancyList?>?

    @GET("emp_api/apna_emp_get_Incasestatus")
    fun getCaseStatusList(
        @Query("limit") str: String?,
        @Query("search") str2: String?
    ): Call<CaseStatusINPojo?>?

    @GET("emp_api/apna_emp_get_conveyance")
    fun getConvancyList(
        @Query("limit") str: String?,
        @Query("page") i: Int,
        @Query("search") str2: String?
    ): Observable<AllConvancyList>

    @GET("emp_api/apna_emp_dashboard")
    suspend fun dashboardData(): Response<DashBoardData>

    @GET("emp_api/apna_emp_delivery_order")
   suspend fun getDeliveredOrderList(
        @Query("limit") str: String?,
        @Query("page") i: Int,
        @Query("in_out") str2: String?,
        @Query("search") str3: String?
    ): Response<ReleaseOrderPojo>

    @get:GET("emp_api/apna_emp_dhramkanta_list")
    val dharmaKanthaListLevel: Observable<DharmaKanthaPostData>

    @get:GET("emp_api/fc_list")
    val fastCaseList: Call<ResponseFastcaseList?>?

    @GET("emp_api/apna_emp_get_gatepass")
    suspend fun getGatePass(
        @Query("limit") str: String?,
        @Query("page") str2: String?,
        @Query("in_out") str3: String?,
        @Query("search") str4: String?
    ): Response<GatePassListResponse>

    @GET("emp_api/apna_emp_generate_gate_pass")
    fun getGatePassPDf(@Query("case_id") str: String?): Call<GatePassPDFPojo?>?

    @GET("emp_api/apna_emp_get_labourbook")
    suspend fun getLabourBookList(
        @Query("limit") str: String?,
        @Query("page") str2: String?,
        @Query("in_out") str3: String?,
        @Query("search") str4: String?
    ): Response<AllLabourBookListResponse>

    @GET("emp_api/apna_emp_permissions")
    suspend fun getPermission(
        @Query("designation_id") str: String?,
        @Query("emp_level_id") str2: String?
    ): Response<AllUserPermissionsResultListResponse>

    @GET("emp_api/apna_emp_release_order")
    fun getReleaseOrderList(
        @Query("limit") str: String?,
        @Query("page") i: Int,
        @Query("in_out") str2: String?,
        @Query("search") str3: String?
    ): Call<ReleaseOrderPojo?>?

    @GET("emp_api/apna_emp_get_s_k_p")
    suspend fun getS_kanthaParchiList(
        @Query("limit") str: String?,
        @Query("page") str2: String?,
        @Query("in_out") str3: String?,
        @Query("search") str4: String?
    ): Response<SecoundkanthaParchiListResponse>

    @GET("emp_api/apna_emp_get_s_quality")
    suspend fun getS_qualityReportsList(
        @Query("limit") str: String?,
        @Query("page") str2: String?,
        @Query("in_out") str3: String?,
        @Query("search") str4: String?
    ): Response<SecoundQuilityReportListResponse>

    @GET("emp_api/apna_emp_get_spot_deals")
    fun getSpotSellDealTrackList(
        @Query("limit") str: String?,
        @Query("page") i: Int,
        @Query("search") str2: String?
    ): Call<SpotSellDealTrackPojo?>?

    @POST("emp_api/fc_offline_stack_details")
    fun getStack(
        @Query("terminal_id") str: String?,
        @Query("commodity_id") str2: String?
    ): Call<ResponseStackData?>?

    @POST("emp_api/apna_emp_get_stack_number")
    fun getStackList(@Body stackPostData: StackPostData?): Observable<StackListPojo>

    @GET("emp_api/apna_emp_levelwise_terminal")
   suspend fun terminalListLevel(): Response<TerminalListPojo>

    @GET("emp_api/apna_emp_transpoter_detail")
    fun getTransporterDetails(@Query("transport_id") str: String?): Observable<TransporterDetailsPojo>

    @get:GET("emp_api/apna_emp_transpoter_name")
    val transporterList: Observable<TransporterListPojo>

    @GET("emp_api/apna_emp_get_truckbook")
   suspend fun getTruckBookList(
        @Query("limit") str: String?,
        @Query("page") i: Int,
        @Query("in_out") str2: String?,
        @Query("search") str3: String?
    ): Response<AllTruckBookListResponse>

    @GET("emp_api/apna_emp_get_user_data")
    fun getUserList(
        @Query("terminal_id") terminal_id: String?,
        @Query("in_out") in_out: String?
    ): Observable<AllUserListPojo>

    @GET("emp_api/apna_emp_get_commodity")
    fun getCommodityList(
        @Query("terminal_id") terminal_id: String?,
        @Query("in_out") in_out: String?,
        @Query("user_phone") user_phone: String?
    ): Observable<CommodityResponseData>

    @POST("emp_api/fc_offline_user_details")
    fun getUserName(@Query("phone") str: String?): Call<ResponseUserData?>?

    @GET("emp_api/apna_emp_vendor_voucher_requestList")
    fun getVendorApprovalRequestConvancyList(
        @Query("limit") str: String?,
        @Query("page") i: Int,
        @Query("search") str2: String?
    ): Call<AllVendorConvancyList?>?

    @GET("emp_api/apna_emp_vendor_voucher_list")
    fun getVendorConvancyList(
        @Query("limit") str: String?,
        @Query("page") i: Int,
        @Query("search") str2: String?
    ): Call<AllVendorConvancyList?>?

    @get:GET("emp_api/fc_offline_warehouse_details")
    val warehouseData: Call<ResponseWarehouse?>?

    @GET("emp_api/apna_emp_clock_status")
    fun getattendanceStatus(): Call<AttendanceResponse?>?

    @GET("api/apna_default_list")
    fun getcommuydity_terminal_user_emp_listing(@Query("app_type") str: String?): Observable<CommudityResponse>

    @GET("emp_api/apna_emp_get_kanta_prachi")
    suspend fun getf_kanthaParchiList(
        @Query("limit") str: String?,
        @Query("page") str2: String?,
        @Query("in_out") str3: String?,
        @Query("search") str4: String?
    ): Response<FirstkanthaParchiListResponse>

    @GET("emp_api/apna_emp_get_quality")
   suspend fun getf_qualityReportsList(
        @Query("limit") str: String?,
        @Query("page") str2: String?,
        @Query("in_out") str3: String?,
        @Query("search") str4: String?
    ): Response<FirstQuilityReportListResponse>

    @GET("emp_api/apna_emp_get_intentionToSell_request")
    fun getintentionList(
        @Query("limit") str: String?,
        @Query("page") i: Int,
        @Query("search") str2: String?
    ): Call<AllIntantionList?>?

    @GET("emp_api/apna_emp_get_levelwiselist")
    fun getlevelwiselist(): Observable<AllLevelEmpListPojo>

    @GET("emp_api/apna_amp_vendor_list")
    fun getvendorUserList(): Observable<VendorNamePojo>

    @GET("api/app_version")
    fun getversionCode(@Query("app_type") str: String?): Observable<VersionCodeResponse>

    @POST("emp_api/fc_offline_store")
    fun offlineFastCase(@Body requestOfflineCaseData: RequestOfflineCaseData?): Call<BaseResponse?>?

    @POST("emp_api/apna_emp_intentiontosell_reject")
    fun rejeectIntantion(@Body approvedIntantionPOst: ApprovedIntantionPOst?): Call<LoginResponse?>?

    @GET("emp_api/apna_emp_get_user_location")
    fun routeMap(@Query("phone_no") str: String?): Call<MobileNUmberPojo?>?

    @POST("emp_api/fc_send_otp")
    fun sendOtpFastCase(@Query("spot_token") str: String?): Call<ResponseSendOtp?>?

    @POST("emp_api/apna_emp_addPrice")
    fun setPricing(@Body createPricingSetPostData: CreatePricingSetPostData?): Call<LoginResponse?>?

    @POST("emp_api/apna_emp_update_lead")
    fun updateLeads(@Body updateLeadsPostData: UpdateLeadsPostData?): Observable<LoginResponse>

    @POST("emp_api/apna_emp_update_deliverorder")
    suspend fun uploadDeliveredOrder(@Body uploadReleaseOrderlsPostData: UploadReleaseOrderlsPostData): Response<LoginResponse>

    @POST("emp_api/apna_emp_f_quality")
    fun uploadFirstQualityReports(@Body uploadFirstQualityPostData: UploadFirstQualityPostData?): Observable<LoginResponse>

    @POST("emp_api/apna_emp_kanta_parchi")
    suspend fun uploadFirstkantaParchi(@Body uploadFirstkantaParchiPostData: UploadFirstkantaParchiPostData): Response<LoginResponse>

    @POST("emp_api/apna_emp_gatepass")
    fun uploadGatePass(@Body uploadGatePassPostData: UploadGatePassPostData?): Call<LoginResponse?>?

    @POST("emp_api/apna_emp_gatepass")
    fun uploadGatePassNew(@Body uploadGatePassPostDataNew: UploadGatePassPostDataNew?): Observable<LoginResponse>

    @POST("emp_api/apna_emp_update_labour")
    fun uploadLabourDetails(@Body uploadLabourDetailsPostData: UploadLabourDetailsPostData?): Observable<LoginResponse>

    @POST("emp_api/apna_emp_update_releaseorder")
    fun uploadRleaseOrder(@Body uploadReleaseOrderlsPostData: UploadReleaseOrderlsPostData?): Call<LoginResponse?>?

    @POST("emp_api/apna_emp_s_quality")
    suspend fun uploadSecoundQualityReports(@Body uploadSecoundQualityPostData: UploadSecoundQualityPostData?): Response<LoginResponse>

    @POST("emp_api/apna_emp_s_kanta_parchi")
    fun uploadSecoundkantaParchi(@Body uploadSecoundkantaParchiPostData: UploadSecoundkantaParchiPostData?): Observable<LoginResponse>

    @POST("emp_api/apna_emp_update_truckbook")
    fun uploadTruckDetails(@Body uploadTruckDetailsPostData: UploadTruckDetailsPostData?): Observable<LoginResponse>

    @POST("emp_api/apna_emp_vendor_voucher_approve")
    fun vendorApproveConveyanceDelate(@Body approvedVendorConveyancePOst: ApprovedVendorConveyancePOst?): Call<LoginResponse?>?

    @POST("emp_api/apna_emp_vendor_voucher_delete")
    fun vendorConveyanceDelate(@Body selfRejectVendorConveyancePOst: SelfRejectVendorConveyancePOst?): Call<LoginResponse?>?

    @POST("emp_api/apna_emp_vendor_voucher_reject")
    fun vendorRejectConveyanceDelate(@Body approvedRejectVendorConveyancePOst: ApprovedRejectVendorConveyancePOst?): Call<LoginResponse>

    @POST("emp_api/fc_submit_otp")
    fun verifyOtpFastCase(
        @Query("spot_token") str: String?,
        @Query("intention_otp") str2: String?
    ): Call<VerifyOtpFastcase?>?
}