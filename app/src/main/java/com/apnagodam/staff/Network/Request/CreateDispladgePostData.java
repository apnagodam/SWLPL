package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateDispladgePostData {
    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("terminal_id")
    @Expose
    private String terminal_id;

    @SerializedName("commodity_id")
    @Expose
    private String commodity_id;
    @SerializedName("stack_id")
    @Expose
    private String stack_id;

    @SerializedName("quantity")
    @Expose
    private String quantity;

    @SerializedName("Bags")
    @Expose
    private String Bags;

    @SerializedName("approved_by")
    @Expose
    private String approved_by;
    @SerializedName("emp_displege_notes")
    @Expose
    private String emp_displege_notes;

    @SerializedName("displedge_image")
    @Expose
    private String displedge_image ;

    public CreateDispladgePostData(String user_id, String terminal_id, String commodity_id, String stack_id,
                                   String quantity, String Bags, String approved_by, String emp_displege_notes, String displedge_image) {
        this.user_id = user_id;
        this.terminal_id = terminal_id;
        this.commodity_id = commodity_id;
        this.stack_id = stack_id;
        this.quantity = quantity;
        this.Bags = Bags;
        this.approved_by = approved_by;
        this.emp_displege_notes = emp_displege_notes;
        this.displedge_image = displedge_image;
    }
}


