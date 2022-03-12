package com.mohamedragab.cashpos.modules.omlatransactions.models;


public class omlatransaction {
    private int id;



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

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public double getNotpaid() {
        return notpaid;
    }

    public void setNotpaid(double notpaid) {
        this.notpaid = notpaid;
    }

    private String name;
    private String process;
    private double value;
    private String date;
    private String invoiceId;
    private double notpaid;


}
