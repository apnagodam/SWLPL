package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UploadSecoundQualityPostData {@SerializedName("case_id")
@Expose
private String case_id;

    @SerializedName("report_file")
    @Expose
    private String report_file;

    private ArrayList<UploadFirstQualityPostData.CommodityData> commodityList;
    @SerializedName("packaging_type")
    @Expose
    private String packaging_type;
    @SerializedName("infested")
    @Expose
    private String infested;

    @SerializedName("live_insects")
    @Expose
    private String live_insects;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("commodity_img")
    @Expose
    private String commodity_img;

    @SerializedName("extra_claim")
    @Expose
    private String extraClaim;

    public UploadSecoundQualityPostData(String case_id, String report_file, ArrayList<UploadFirstQualityPostData.CommodityData> commodityList, String packaging_type, String infested, String live_insects, String notes, String commodity_img,String extraClaim) {
        this.case_id = case_id;
        this.report_file = report_file;
        this.commodityList = commodityList;
        this.packaging_type = packaging_type;
        this.infested = infested;
        this.live_insects = live_insects;
        this.notes = notes;
        this.commodity_img = commodity_img;
        this.extraClaim=extraClaim;
    }


    public static class CommodityData {
        private UploadFirstQualityPostData.CommodityData datum;

        public UploadFirstQualityPostData.CommodityData getCommodityData() {
            return datum;
        }

        public void setCommodityData(UploadFirstQualityPostData.CommodityData datum) {
            this.datum = datum;
        }

        public CommodityData(Integer id, String name, String value, Integer min, Integer max) {
            this.id = id;
            this.name = name;
            this.value = value;
            this.max = max;
            this.min = min;
        }

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("value")
        @Expose
        private String value;
        @SerializedName("min")
        @Expose
        private Integer min;
        @SerializedName("max")
        @Expose
        private Integer max;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Integer getMin() {
            return min;
        }

        public void setMin(Integer min) {
            this.min = min;
        }

        public Integer getMax() {
            return max;
        }

        public void setMax(Integer max) {
            this.max = max;
        }

    }
}


