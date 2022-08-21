package com.mohamedragab.cashpos.modules.buy.models;


public class invoice {
    private int id;
    private String invoice_id;
    private String mored_name;
    private String  date;
    private  double total;

    public String getMored_name() {
        return mored_name;
    }

    public void setMored_name(String mored_name) {
        this.mored_name = mored_name;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    private  String cashier;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getCustomer_name() {
        return mored_name;
    }

    public void setCustomer_name(String mored_name) {
        this.mored_name = mored_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getNotpaid() {
        return notpaid;
    }

    public void setNotpaid(double notpaid) {
        this.notpaid = notpaid;
    }

    private double notpaid;






}
