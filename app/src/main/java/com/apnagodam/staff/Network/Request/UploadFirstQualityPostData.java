package com.apnagodam.staff.Network.Request;

import com.apnagodam.staff.Network.Response.QualityParamsResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UploadFirstQualityPostData {
    @SerializedName("case_id")
    @Expose
    private String case_id;

    @SerializedName("report_file")
    @Expose
    private String report_file;

    private ArrayList<CommodityData> commodityList;
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

    public UploadFirstQualityPostData(String case_id, String report_file, ArrayList<CommodityData> commodityList, String packaging_type, String infested, String live_insects, String notes, String commodity_img) {
        this.case_id = case_id;
        this.report_file = report_file;
        this.commodityList = commodityList;
        this.packaging_type = packaging_type;
        this.infested = infested;
        this.live_insects = live_insects;
        this.notes = notes;
        this.commodity_img = commodity_img;
    }


    public static class CommodityData {
        private CommodityData datum;

        public CommodityData getCommodityData() {
            return datum;
        }

        public void setCommodityData(CommodityData datum) {
            this.datum = datum;
        }

        public CommodityData(Integer id, String name, String value, Float min, Float max) {
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
        private Float min;
        @SerializedName("max")
        @Expose
        private Float max;

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

        public Float getMin() {
            return min;
        }

        public void setMin(Float min) {
            this.min = min;
        }

        public Float getMax() {
            return max;
        }

        public void setMax(Float max) {
            this.max = max;
        }

    }
}


