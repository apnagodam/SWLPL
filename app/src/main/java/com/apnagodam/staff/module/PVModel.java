package com.apnagodam.staff.module;

public class PVModel {

    private int sNumber;

    public double getDhang() {
        return dhang;
    }

    public void setDhang(double dhang) {
        this.dhang = dhang;
    }

    public double getDanda() {
        return danda;
    }

    public void setDanda(double danda) {
        this.danda = danda;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getPlusminus() {
        return plusminus;
    }

    public void setPlusminus(double plusminus) {
        this.plusminus = plusminus;
    }

    private double dhang;
    private double danda;
    private double height;
    private double plusminus;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private  String remark;
    public double getTotalPV() {
        return TotalPV;
    }

    public void setTotalPV(double totalPV) {
        TotalPV = totalPV;
    }

    private double TotalPV;
    private String isPlus;

    public double getTotalBags() {
        return totalBags;
    }

    public void setTotalBags(double totalBags) {
        this.totalBags = totalBags;
    }

    private double totalBags;
    public int getsNumber() {
        return sNumber;
    }

    public void setsNumber(int sNumber) {
        this.sNumber = sNumber;
    }


    public String getIsPlus() {
        return isPlus;
    }

    public void setIsPlus(String isPlus) {
        this.isPlus = isPlus;
    }

    @Override
    public String toString() {
        return "PVModel{" +
                "sNumber=" + sNumber +
                ", dhang=" + dhang +
                ", danda=" + danda +
                ", height=" + height +
                ", plusminus=" + plusminus +
                ", TotalPV=" + TotalPV +
                ", isPlus='" + isPlus + '\'' +
                ", totalBags=" + totalBags +
                ", remark=" + remark +
                '}';
    }
}
