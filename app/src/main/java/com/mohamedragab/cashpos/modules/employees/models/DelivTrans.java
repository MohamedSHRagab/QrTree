package com.mohamedragab.cashpos.modules.employees.models;


public class DelivTrans {
    private int id;
    private String deliv_name;
    private String address;
    private double money;
    private String invoice_id;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeliv_name() {
        return deliv_name;
    }

    public void setDeliv_name(String deliv_name) {
        this.deliv_name = deliv_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
