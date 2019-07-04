package com.zinzin.loltft.model;

import java.util.List;

public class Item {
    int id;
    String name;
    String des;
    String url;
    List<String> listCombine;
    List<ItemBuilder> listItemBuilder;

    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getListCombine() {
        return listCombine;
    }

    public void setListCombine(List<String> listCombine) {
        this.listCombine = listCombine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ItemBuilder> getListItemBuilder() {
        return listItemBuilder;
    }

    public void setListItemBuilder(List<ItemBuilder> listItemBuilder) {
        this.listItemBuilder = listItemBuilder;
    }
}
