package com.mohamedragab.cashpos.modules.sales.models;


public class invoice {
    private int id;
    private String invoice_id;
    private String discount_kind;

    public String getKind() {
        return kind;
    }

    public String getDiscount_kind() {
        return discount_kind;
    }

    public void setDiscount_kind(String discount_kind) {
        this.discount_kind = discount_kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    private String customer_name;
    private String date;
    private String kind;
    private double total;

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    private double discount;

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    private String cashier;

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
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
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
