package com.apnagodam.staff.Network.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ResponseStackData extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("stack_number")
        @Expose
        private String stack_number;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStack_number() {
            return stack_number;
        }

        public void setStack_number(String stack_number) {
            this.stack_number = stack_number;
        }
    }

}
