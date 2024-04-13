package com.apnagodam.staff.Network.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DharamKanta {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("kanta_parchi_number")
        @Expose
        private String kantaParchiNumber;
        @SerializedName("kanta_name")
        @Expose
        private String kantaName;

        @SerializedName("kanta_id")
        @Expose
        private Integer kantaId;

        public String getKantaParchiNumber() {
            return kantaParchiNumber;
        }

        public void setKantaParchiNumber(String kantaParchiNumber) {
            this.kantaParchiNumber = kantaParchiNumber;
        }

        public String getKantaName() {
            return kantaName;
        }

        public void setKantaName(String kantaName) {
            this.kantaName = kantaName;
        }


        public Integer getKantaId() {
            return kantaId;
        }

        public void setKantaId(Integer kantaId) {
            this.kantaId = kantaId;
        }

    }

}