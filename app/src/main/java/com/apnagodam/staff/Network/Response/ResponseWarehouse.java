package com.apnagodam.staff.Network.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseWarehouse extends BaseResponse {

    @SerializedName("warehouse_data")
    @Expose
    private List<WarehouseData> warehouseData = null;

    @SerializedName("contractor_list")
    @Expose
    private List<ContractorList> contractorLists = null;

    @SerializedName("commodity_list")
    @Expose
    private List<CommodityList> commodity_list = null;

    @SerializedName("dharam_kanta")
    @Expose
    private List<DharmKanta> dharam_kanta = null;


    public List<WarehouseData> getWarehouseData() {
        return warehouseData;
    }

    public void setWarehouseData(List<WarehouseData> warehouseData) {
        this.warehouseData = warehouseData;
    }

    public List<ContractorList> getContractorLists() {
        return contractorLists;
    }

    public void setContractorLists(List<ContractorList> contractorLists) {
        this.contractorLists = contractorLists;
    }

    public List<CommodityList> getCommodity_list() {
        return commodity_list;
    }

    public void setCommodity_list(List<CommodityList> commodity_list) {
        this.commodity_list = commodity_list;
    }

    public List<DharmKanta> getDharam_kanta() {
        return dharam_kanta;
    }

    public void setDharam_kanta(List<DharmKanta> dharam_kanta) {
        this.dharam_kanta = dharam_kanta;
    }

    public class WarehouseData {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("name")
        @Expose
        private String name;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class ContractorList {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("contractor_name")
        @Expose
        private String contractor_name;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContractor_name() {
            return contractor_name;
        }

        public void setContractor_name(String contractor_name) {
            this.contractor_name = contractor_name;
        }
    }

    public class CommodityList {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("category")
        @Expose
        private String category;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    public class DharmKanta {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
