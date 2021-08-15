package com.mohamedragab.cashpos.modules.home;

public class upload {
    public String name;
    public String url;

    public upload() {
    }

    public upload(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
