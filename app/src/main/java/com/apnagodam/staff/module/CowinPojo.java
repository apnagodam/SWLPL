package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CowinPojo extends BaseResponse {
    @SerializedName("centers")
    @Expose
    private List<Center> centers = null;

    public List<Center> getCenters() {
        return centers;
    }

    public void setCenters(List<Center> centers) {
        this.centers = centers;
    }

    public class Center {

        @SerializedName("center_id")
        @Expose
        private Integer centerId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("state_name")
        @Expose
        private String stateName;
        @SerializedName("district_name")
        @Expose
        private String districtName;
        @SerializedName("block_name")
        @Expose
        private String blockName;
        @SerializedName("pincode")
        @Expose
        private Integer pincode;
        @SerializedName("lat")
        @Expose
        private Integer lat;
        @SerializedName("long")
        @Expose
        private Integer _long;
        @SerializedName("from")
        @Expose
        private String from;
        @SerializedName("to")
        @Expose
        private String to;
        @SerializedName("fee_type")
        @Expose
        private String feeType;
        @SerializedName("sessions")
        @Expose
        private List<Session> sessions = null;

        public Integer getCenterId() {
            return centerId;
        }

        public void setCenterId(Integer centerId) {
            this.centerId = centerId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public String getBlockName() {
            return blockName;
        }

        public void setBlockName(String blockName) {
            this.blockName = blockName;
        }

        public Integer getPincode() {
            return pincode;
        }

        public void setPincode(Integer pincode) {
            this.pincode = pincode;
        }

        public Integer getLat() {
            return lat;
        }

        public void setLat(Integer lat) {
            this.lat = lat;
        }

        public Integer getLong() {
            return _long;
        }

        public void setLong(Integer _long) {
            this._long = _long;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getFeeType() {
            return feeType;
        }

        public void setFeeType(String feeType) {
            this.feeType = feeType;
        }

        public List<Session> getSessions() {
            return sessions;
        }

        public void setSessions(List<Session> sessions) {
            this.sessions = sessions;
        }

    }

    public class Session {

        @SerializedName("session_id")
        @Expose
        private String sessionId;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("available_capacity")
        @Expose
        private Integer availableCapacity;
        @SerializedName("min_age_limit")
        @Expose
        private Integer minAgeLimit;
        @SerializedName("vaccine")
        @Expose
        private String vaccine;
        @SerializedName("slots")
        @Expose
        private List<String> slots = null;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getAvailableCapacity() {
            return availableCapacity;
        }

        public void setAvailableCapacity(Integer availableCapacity) {
            this.availableCapacity = availableCapacity;
        }

        public Integer getMinAgeLimit() {
            return minAgeLimit;
        }

        public void setMinAgeLimit(Integer minAgeLimit) {
            this.minAgeLimit = minAgeLimit;
        }

        public String getVaccine() {
            return vaccine;
        }

        public void setVaccine(String vaccine) {
            this.vaccine = vaccine;
        }

        public List<String> getSlots() {
            return slots;
        }

        public void setSlots(List<String> slots) {
            this.slots = slots;
        }

    }
}
