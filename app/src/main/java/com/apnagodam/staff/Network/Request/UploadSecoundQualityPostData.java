package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadSecoundQualityPostData {
    @SerializedName("case_id")
    @Expose
    private String case_id;

    @SerializedName("report_file")
    @Expose
    private String report_file;
    @SerializedName("moisture_level")
    @Expose
    private String moisture_level;

    @SerializedName("thousand_crown_w")
    @Expose
    private String thousand_crown_w;

    @SerializedName("broken")
    @Expose
    private String broken;

    @SerializedName("foreign_matter")
    @Expose
    private String foreign_matter;
    @SerializedName("thin")
    @Expose
    private String thin;

    @SerializedName("damage")
    @Expose
    private String damage;
    @SerializedName("black_smith")
    @Expose
    private String black_smith;

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
    public String getCommodity_img() {
        return commodity_img;
    }

    public void setCommodity_img(String commodity_img) {
        this.commodity_img = commodity_img;
    }

    @SerializedName("commodity_img")
    @Expose
    private String commodity_img;
    @SerializedName("commodity_file")
    @Expose
    private String commodity_file;
    public UploadSecoundQualityPostData(String case_id, String report_file, String moisture_level,
                                        String thousand_crown_w, String foreign_matter, String thin, String damage, String black_smith
            , String broken,  String infested, String live_insects, String notes,String commodity_img,String commodity_file) {
        this.case_id = case_id;
        this.report_file = report_file;
        this.moisture_level = moisture_level;
        this.thousand_crown_w = thousand_crown_w;
        this.foreign_matter = foreign_matter;
        this.thin = thin;
        this.damage = damage;
        this.black_smith = black_smith;
        this.broken=broken;
        this.infested = infested;
        this.live_insects = live_insects;
        this.notes=notes;
        this.commodity_img=commodity_img;
        this.commodity_file=commodity_file;
    }
}


