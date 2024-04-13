package com.apnagodam.staff.Network.Response;

import java.util.List;

import com.apnagodam.staff.module.SecoundQuilityReportListResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QualityParamsResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public  static class Datum {
        private Datum datum;

        public Datum getDatum() {
            return datum ;
        }
        public void setDatum(Datum datum) {
            this.datum = datum;
        }

        public Datum(Integer id,String name,Float min,Float max){
            this.id = id;
            this.name = name;
            this.max = max;
            this.min = min;
        }
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
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


