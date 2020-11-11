package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SpotSellDealTrackPojo extends BaseResponse  implements Serializable {
    @SerializedName("deals")
    @Expose
    private Deals deals;

    public Deals getDeals() {
        return deals;
    }

    public void setDeals(Deals deals) {
        this.deals = deals;
    }

    public class Deals {

        @SerializedName("current_page")
        @Expose
        private Integer currentPage;
        @SerializedName("data")
        @Expose
        private List<Datum> data = null;
        @SerializedName("first_page_url")
        @Expose
        private String firstPageUrl;
        @SerializedName("from")
        @Expose
        private Integer from;
        @SerializedName("last_page")
        @Expose
        private Integer lastPage;
        @SerializedName("last_page_url")
        @Expose
        private String lastPageUrl;
        @SerializedName("next_page_url")
        @Expose
        private String nextPageUrl;
        @SerializedName("path")
        @Expose
        private String path;
        @SerializedName("per_page")
        @Expose
        private String perPage;
        @SerializedName("prev_page_url")
        @Expose
        private String prevPageUrl;
        @SerializedName("to")
        @Expose
        private Integer to;
        @SerializedName("total")
        @Expose
        private Integer total;

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }

        public String getFirstPageUrl() {
            return firstPageUrl;
        }

        public void setFirstPageUrl(String firstPageUrl) {
            this.firstPageUrl = firstPageUrl;
        }

        public Integer getFrom() {
            return from;
        }

        public void setFrom(Integer from) {
            this.from = from;
        }

        public Integer getLastPage() {
            return lastPage;
        }

        public void setLastPage(Integer lastPage) {
            this.lastPage = lastPage;
        }

        public String getLastPageUrl() {
            return lastPageUrl;
        }

        public void setLastPageUrl(String lastPageUrl) {
            this.lastPageUrl = lastPageUrl;
        }

        public String getNextPageUrl() {
            return nextPageUrl;
        }

        public void setNextPageUrl(String nextPageUrl) {
            this.nextPageUrl = nextPageUrl;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getPerPage() {
            return perPage;
        }

        public void setPerPage(String perPage) {
            this.perPage = perPage;
        }

        public String getPrevPageUrl() {
            return prevPageUrl;
        }

        public void setPrevPageUrl(String prevPageUrl) {
            this.prevPageUrl = prevPageUrl;
        }

        public Integer getTo() {
            return to;
        }

        public void setTo(Integer to) {
            this.to = to;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }
    }

    public class Datum  implements Serializable{

        public String getBuyer_phone() {
            return buyer_phone;
        }

        public void setBuyer_phone(String buyer_phone) {
            this.buyer_phone = buyer_phone;
        }

        public String getSeller_phone() {
            return seller_phone;
        }

        public void setSeller_phone(String seller_phone) {
            this.seller_phone = seller_phone;
        }

        public String getBuyer_pancard_no() {
            return buyer_pancard_no;
        }

        public void setBuyer_pancard_no(String buyer_pancard_no) {
            this.buyer_pancard_no = buyer_pancard_no;
        }

        public String getBorker_charge() {
            return borker_charge;
        }

        public void setBorker_charge(String borker_charge) {
            this.borker_charge = borker_charge;
        }

        @SerializedName("borker_charge")
        @Expose
        private String borker_charge;
        @SerializedName("buyer_phone")
        @Expose
        private String buyer_phone;
        @SerializedName("seller_phone")
        @Expose
        private String seller_phone;
        @SerializedName("buyer_pancard_no")
        @Expose
        private String buyer_pancard_no;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("contract_id")
        @Expose
        private String contractId;
        @SerializedName("buyer_id")
        @Expose
        private String buyerId;
        @SerializedName("seller_id")
        @Expose
        private String sellerId;
        @SerializedName("seller_cat_id")
        @Expose
        private String sellerCatId;
        @SerializedName("case_id")
        @Expose
        private String caseId;
        @SerializedName("payment_ref_no")
        @Expose
        private String paymentRefNo;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("final_price")
        @Expose
        private String finalPrice;
        @SerializedName("labour_rate")
        @Expose
        private String labourRate;
        @SerializedName("todays_price")
        @Expose
        private String todaysPrice;
        @SerializedName("bid_type")
        @Expose
        private String bidType;
        @SerializedName("mandi_fees")
        @Expose
        private String mandiFees;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("quality_category")
        @Expose
        private String qualityCategory;
        @SerializedName("fname")
        @Expose
        private String fname;
        @SerializedName("seller_name")
        @Expose
        private String sellerName;
        @SerializedName("sales_status")
        @Expose
        private String salesStatus;
        @SerializedName("location")
        @Expose
        private String location;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContractId() {
            return contractId;
        }

        public void setContractId(String contractId) {
            this.contractId = contractId;
        }

        public String getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(String buyerId) {
            this.buyerId = buyerId;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getSellerCatId() {
            return sellerCatId;
        }

        public void setSellerCatId(String sellerCatId) {
            this.sellerCatId = sellerCatId;
        }

        public String getCaseId() {
            return caseId;
        }

        public void setCaseId(String caseId) {
            this.caseId = caseId;
        }

        public String getPaymentRefNo() {
            return paymentRefNo;
        }

        public void setPaymentRefNo(String paymentRefNo) {
            this.paymentRefNo = paymentRefNo;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getFinalPrice() {
            return finalPrice;
        }

        public void setFinalPrice(String finalPrice) {
            this.finalPrice = finalPrice;
        }

        public String getLabourRate() {
            return labourRate;
        }

        public void setLabourRate(String labourRate) {
            this.labourRate = labourRate;
        }

        public String getTodaysPrice() {
            return todaysPrice;
        }

        public void setTodaysPrice(String todaysPrice) {
            this.todaysPrice = todaysPrice;
        }

        public String getBidType() {
            return bidType;
        }

        public void setBidType(String bidType) {
            this.bidType = bidType;
        }

        public String getMandiFees() {
            return mandiFees;
        }

        public void setMandiFees(String mandiFees) {
            this.mandiFees = mandiFees;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getQualityCategory() {
            return qualityCategory;
        }

        public void setQualityCategory(String qualityCategory) {
            this.qualityCategory = qualityCategory;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public String getSalesStatus() {
            return salesStatus;
        }

        public void setSalesStatus(String salesStatus) {
            this.salesStatus = salesStatus;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

    }


}
