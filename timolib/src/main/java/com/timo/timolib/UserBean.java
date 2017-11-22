package com.timo.timolib;

import com.timo.timolib.http.BaseBean;

public class UserBean extends BaseBean {
    private String landlordId;
    private String landlordImg;
    private String landlordNickName;
    private int landlordSex;
    private String landlordPhone;
    private String landlordPassword;
    private String landlordIntroduction;
    private String landlordAboutMe;
    private String landlordBirthday;
    private String landlordCityName;
    private String landlordIdCardsInfoId;
    private String createTime;
    private int landlordAge;
    private int careStatus;
    private int fansNumber;
    private String roomList;
    private String landlordIdCards;
    private String isPerfect;

    public String getPerfect() {
        return isPerfect;
    }

    public void setPerfect(String perfect) {
        isPerfect = perfect;
    }

    public String getUserId() {
        return landlordId;
    }

    public void setUserId(String landlordId) {
        this.landlordId = landlordId;
    }

    public String getLandlordImg() {
        return landlordImg;
    }

    public void setLandlordImg(String landlordImg) {
        this.landlordImg = landlordImg;
    }

    public String getLandlordNickName() {
        return landlordNickName;
    }

    public void setLandlordNickName(String landlordNickName) {
        this.landlordNickName = landlordNickName;
    }

    public int getLandlordSex() {
        return landlordSex;
    }

    public void setLandlordSex(int landlordSex) {
        this.landlordSex = landlordSex;
    }

    public String getLandlordPhone() {
        return landlordPhone;
    }

    public void setLandlordPhone(String landlordPhone) {
        this.landlordPhone = landlordPhone;
    }

    public String getLandlordPassword() {
        return landlordPassword;
    }

    public void setLandlordPassword(String landlordPassword) {
        this.landlordPassword = landlordPassword;
    }

    public String getLandlordIntroduction() {
        return landlordIntroduction;
    }

    public void setLandlordIntroduction(String landlordIntroduction) {
        this.landlordIntroduction = landlordIntroduction;
    }

    public String getLandlordAboutMe() {
        return landlordAboutMe;
    }

    public void setLandlordAboutMe(String landlordAboutMe) {
        this.landlordAboutMe = landlordAboutMe;
    }

    public String getLandlordBirthday() {
        return landlordBirthday;
    }

    public void setLandlordBirthday(String landlordBirthday) {
        this.landlordBirthday = landlordBirthday;
    }

    public String getLandlordCityName() {
        return landlordCityName;
    }

    public void setLandlordCityName(String landlordCityName) {
        this.landlordCityName = landlordCityName;
    }

    public String getLandlordIdCardsInfoId() {
        return landlordIdCardsInfoId;
    }

    public void setLandlordIdCardsInfoId(String landlordIdCardsInfoId) {
        this.landlordIdCardsInfoId = landlordIdCardsInfoId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getLandlordAge() {
        return landlordAge;
    }

    public void setLandlordAge(int landlordAge) {
        this.landlordAge = landlordAge;
    }

    public int getCareStatus() {
        return careStatus;
    }

    public void setCareStatus(int careStatus) {
        this.careStatus = careStatus;
    }

    public int getFansNumber() {
        return fansNumber;
    }

    public void setFansNumber(int fansNumber) {
        this.fansNumber = fansNumber;
    }

    public String getRoomList() {
        return roomList;
    }

    public void setRoomList(String roomList) {
        this.roomList = roomList;
    }

    public String getLandlordIdCards() {
        return landlordIdCards;
    }

    public void setLandlordIdCards(String landlordIdCards) {
        this.landlordIdCards = landlordIdCards;
    }
}
