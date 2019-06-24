package com.zinzin.loltft.model;

import java.util.List;

public class Origin {
    String name;
    String url;
    List<String> des;

    public Origin() {
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
