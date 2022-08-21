package com.mohamedragab.cashpos.modules.login.models;


public class User {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatalink() {
        return datalink;
    }

    public void setDatalink(String datalink) {
        this.datalink = datalink;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    private String name;
    private String phone;


    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    private String adminid;

    private String address;
    private String password;
    private String datalink;

    public String getDatabaseurl() {
        return databaseurl;
    }

    public void setDatabaseurl(String databaseurl) {
        this.databaseurl = databaseurl;
    }

    public String getDatabaseupdate() {
        return databaseupdate;
    }

    public void setDatabaseupdate(String databaseupdate) {
        this.databaseupdate = databaseupdate;
    }

    private String databaseurl;
    private String databaseupdate;
    private String used;

    public String getShoptype() {
        return shoptype;
    }

    public void setShoptype(String shoptype) {
        this.shoptype = shoptype;
    }

    private String shoptype;

    public String getBackupdate() {
        return backupdate;
    }

    public void setBackupdate(String backupdate) {
        this.backupdate = backupdate;
    }

    private String backupdate;

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    private String blocked;

    private String lastseen;



}
