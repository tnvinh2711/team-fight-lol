package com.zinzin.loltft.model;

import java.util.ArrayList;
import java.util.List;

public class Unit {
    private String name = "";
    private String cost = "";
    private String tier = "";
    private String url = "";
    private List<Type> type = new ArrayList<>();

    public Unit() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Type> getType() {
        return type;
    }

    public void setType(List<Type> type) {
        this.type = type;
    }
}
