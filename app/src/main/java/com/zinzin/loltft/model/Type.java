package com.zinzin.loltft.model;

import java.util.List;

public class Type {
    String type = "";
    List<String> des;
    String name = "";
    String url = "";

    public String toString() {
        return "Type:{" + name + "," + url + "}";
    }

    public Type() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<String> getDes() {
        return des;
    }

    public void setDes(List<String> des) {
        this.des = des;
    }
}
