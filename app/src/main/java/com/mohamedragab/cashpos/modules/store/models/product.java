package com.mohamedragab.cashpos.modules.store.models;


import java.io.Serializable;

public class product implements Serializable {
    private int id;
    private String code_id;
    private String name;
    private String description;
    private String category;
    private String measure1;

    public String getMeasure1() {
        return measure1;
    }

    public void setMeasure1(String measure1) {
        this.measure1 = measure1;
    }

    public String getMeasure2() {
        return measure2;
    }

    public void setMeasure2(String measure2) {
        this.measure2 = measure2;
    }

    public String getMeasure3() {
        return measure3;
    }

    public void setMeasure3(String measure3) {
        this.measure3 = measure3;
    }

    private String measure2;
    private String measure3;
    private double sellprice;
    private double sellprice2;
    private double factor2;
    private double factor3;
    private double sellprice3;
    private double buyprice;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getSellprice2() {
        return sellprice2;
    }

    public void setSellprice2(double sellprice2) {
        this.sellprice2 = sellprice2;
    }

    public double getFactor2() {
        return factor2;
    }

    public void setFactor2(double factor2) {
        this.factor2 = factor2;
    }

    public double getFactor3() {
        return factor3;
    }

    public void setFactor3(double factor3) {
        this.factor3 = factor3;
    }

    public double getSellprice3() {
        return sellprice3;
    }

    public void setSellprice3(double sellprice3) {
        this.sellprice3 = sellprice3;
    }

    private double quantity;
    private int itemid;
    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode_id() {
        return code_id;
    }

    public void setCode_id(String code_id) {
        this.code_id = code_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSellprice() {
        return sellprice;
    }

    public void setSellprice(double sellprice) {
        this.sellprice = sellprice;
    }

    public double getBuyprice() {
        return buyprice;
    }

    public void setBuyprice(double buyprice) {
        this.buyprice = buyprice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(String expiredate) {
        this.expiredate = expiredate;
    }


    private String expiredate;


}
