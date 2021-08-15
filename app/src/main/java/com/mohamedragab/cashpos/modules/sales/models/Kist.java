package com.mohamedragab.cashpos.modules.sales.models;


public class Kist {
    private int id;
    private String invoice_id;
    private String description;
    private int   dayslimit;
    private String collectdate;
    private double kist_value;
    private String damen_name;
    private String damen_phone;

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    private String paydate;

    public String getDamen_name() {
        return damen_name;
    }

    public void setDamen_name(String damen_name) {
        this.damen_name = damen_name;
    }

    public String getDamen_phone() {
        return damen_phone;
    }

    public void setDamen_phone(String damen_phone) {
        this.damen_phone = damen_phone;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDayslimit() {
        return dayslimit;
    }

    public void setDayslimit(int dayslimit) {
        this.dayslimit = dayslimit;
    }

    public String getCollectdate() {
        return collectdate;
    }

    public void setCollectdate(String collectdate) {
        this.collectdate = collectdate;
    }

    public double getKist_value() {
        return kist_value;
    }

    public void setKist_value(double kist_value) {
        this.kist_value = kist_value;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    private String statue;
    private String client_name;
    private String pay_type;


}
