package com.mohamedragab.cashpos.modules.omla.models;


import java.io.Serializable;

public class omla  implements Serializable {
    private int id;
    private String name;
    private String address;
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getHasmoney() {
        return hasmoney;
    }

    public void setHasmoney(double hasmoney) {
        this.hasmoney = hasmoney;
    }

    public double getMaxnotpaid() {
        return maxnotpaid;
    }

    public void setMaxnotpaid(double maxnotpaid) {
        this.maxnotpaid = maxnotpaid;
    }

    public double getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(double paymoney) {
        this.paymoney = paymoney;
    }

    private String notes;
    private double hasmoney;
    private double paymoney;
    private double maxnotpaid;







}
