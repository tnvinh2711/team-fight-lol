package com.zinzin.loltft.model;

public class UnitsInfo {
    String imgInfo;
    String name;
    String type;
    String des;
    int count;

    public UnitsInfo(String imgInfo, String name, String type, String des, int count) {
        this.imgInfo = imgInfo;
        this.name = name;
        this.type = type;
        this.des = des;
        this.count = count;
    }

    public UnitsInfo(String imgInfo, String name, String type, String des) {
        this.imgInfo = imgInfo;
        this.name = name;
        this.type = type;
        this.des = des;
    }

    public String getImgInfo() {
        return imgInfo;
    }

    public void setImgInfo(String imgInfo) {
        this.imgInfo = imgInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


}
