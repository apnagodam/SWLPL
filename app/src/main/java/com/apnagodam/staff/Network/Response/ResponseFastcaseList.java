package com.apnagodam.staff.Network.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseFastcaseList extends BaseResponse {

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

        private String fast_case_id, customer_name, commodity, vichel_number, stack_no, terminal_name, token_number;

        public String getFast_case_id() {
            return fast_case_id;
        }

        public void setFast_case_id(String fast_case_id) {
            this.fast_case_id = fast_case_id;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getCommodity() {
            return commodity;
        }

        public void setCommodity(String commodity) {
            this.commodity = commodity;
        }

        public String getVichel_number() {
            return vichel_number;
        }

        public void setVichel_number(String vichel_number) {
            this.vichel_number = vichel_number;
        }

        public String getStack_no() {
            return stack_no;
        }

        public void setStack_no(String stack_no) {
            this.stack_no = stack_no;
        }

        public String getTerminal_name() {
            return terminal_name;
        }

        public void setTerminal_name(String terminal_name) {
            this.terminal_name = terminal_name;
        }

        public String getToken_number() {
            return token_number;
        }

        public void setToken_number(String token_number) {
            this.token_number = token_number;
        }
    }


}
