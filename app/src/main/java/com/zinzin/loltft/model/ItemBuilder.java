package com.zinzin.loltft.model;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    List<String> listItem = new ArrayList<>();

    public ItemBuilder() {
    }

    public List<String> getListItem() {
        return listItem;
    }

    public void setListItem(List<String> listItem) {
        this.listItem = listItem;
    }
}
