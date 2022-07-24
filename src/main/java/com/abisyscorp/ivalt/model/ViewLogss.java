package com.abisyscorp.ivalt.model;

public class ViewLogss {
    String id;
    String mobile;
    String detail;
    String created_at;

    public ViewLogss(String id, String mobile, String detail, String created_at) {
        this.id = id;
        this.mobile = mobile;
        this.detail = detail;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
