package com.mohamedragab.cashpos.modules.moneybox.models;


public class money {
    private int id;
    private double value;
    private String  notes;
    private String  date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalbefore() {
        return totalbefore;
    }

    public void setTotalbefore(double totalbefore) {
        this.totalbefore = totalbefore;
    }

    public double getTotalAfter() {
        return totalAfter;
    }

    public void setTotalAfter(double totalAfter) {
        this.totalAfter = totalAfter;
    }

    private double totalbefore;
    private double totalAfter;







}
