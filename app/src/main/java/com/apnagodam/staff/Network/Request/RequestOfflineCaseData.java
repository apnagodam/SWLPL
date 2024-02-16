package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class RequestOfflineCaseData implements Serializable {

    private String customer_uid, commodity_id, number, spot_token, terminal_id, stack_id, no_of_bags, weight, vehicle_no, dharam_kanta, kanta_parchi_number,
            contractor_id, split_delivery, intention_otp, truck_file, kanta_parchi;

    public RequestOfflineCaseData(String customer_uid, String commodity_id, String number, String spot_token, String terminal_id, String stack_id,
                                  String no_of_bags, String weight, String vehicle_no, String dharam_kanta, String kanta_parchi_number, String contractor_id,
                                  String split_delivery, String intention_otp, String truck_file, String kanta_parchi) {
        this.customer_uid = customer_uid;
        this.commodity_id = commodity_id;
        this.number = number;
        this.spot_token = spot_token;
        this.terminal_id = terminal_id;
        this.stack_id = stack_id;
        this.no_of_bags = no_of_bags;
        this.weight = weight;
        this.vehicle_no = vehicle_no;
        this.dharam_kanta = dharam_kanta;
        this.kanta_parchi_number = kanta_parchi_number;
        this.contractor_id = contractor_id;
        this.split_delivery = split_delivery;
        this.intention_otp = intention_otp;
        this.truck_file = truck_file;
        this.kanta_parchi = kanta_parchi;
    }

}
