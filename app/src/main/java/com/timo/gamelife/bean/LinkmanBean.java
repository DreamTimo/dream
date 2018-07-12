package com.timo.gamelife.bean;

import com.timo.timolib.network.basebean.BaseBean;

/**
 * Created by 45590 on 2018/7/6.
 */

public class LinkmanBean extends BaseBean {
    /**
     * linkmanId : c088c6d9e09c455b900c8f51ee8e47bb
     * name : 王子
     * tel : 13546789075
     * wechat : 13546789999
     * company : 华慧视
     * email : 123456789@qq.com
     * address : 郑州
     * position : 技术总监
     * remark : 爱比较流畅看电视
     * picturePath : 192.168.1.200:8088/hhsapp/linkman_pic/丽丽/丽丽.jpg
     * photoCode : null
     * featureKey : null
     * createTime : 2018-06-14 11:36:59.0
     * userIdList : null
     * similarity : 0.311727
     */

    private String linkmanId;
    private String name;
    private String tel;
    private String wechat;
    private String company;
    private String email;
    private String address;
    private String position;
    private String remark;
    private String picturePath;
    private Object photoCode;
    private Object featureKey;
    private String createTime;
    private Object userIdList;
    private double similarity;

    public String getLinkmanId() {
        return linkmanId;
    }

    public void setLinkmanId(String linkmanId) {
        this.linkmanId = linkmanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public Object getPhotoCode() {
        return photoCode;
    }

    public void setPhotoCode(Object photoCode) {
        this.photoCode = photoCode;
    }

    public Object getFeatureKey() {
        return featureKey;
    }

    public void setFeatureKey(Object featureKey) {
        this.featureKey = featureKey;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(Object userIdList) {
        this.userIdList = userIdList;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    @Override
    public String toString() {
        return "LinkmanBean{" +
                "linkmanId='" + linkmanId + '\'' +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", wechat='" + wechat + '\'' +
                ", company='" + company + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", position='" + position + '\'' +
                ", remark='" + remark + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", photoCode=" + photoCode +
                ", featureKey=" + featureKey +
                ", createTime='" + createTime + '\'' +
                ", userIdList=" + userIdList +
                ", similarity=" + similarity +
                '}';
    }
}
