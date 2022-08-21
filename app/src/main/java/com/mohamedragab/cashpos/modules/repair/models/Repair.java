package com.mohamedragab.cashpos.modules.repair.models;


public class Repair {
    private int id;
    private String client_name;
    private String created_at;
    private String visit_at;
    private double money_collect;
    private String statue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getVisit_at() {
        return visit_at;
    }

    public void setVisit_at(String visit_at) {
        this.visit_at = visit_at;
    }

    public double getMoney_collect() {
        return money_collect;
    }

    public void setMoney_collect(double money_collect) {
        this.money_collect = money_collect;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    private String notes;



}
