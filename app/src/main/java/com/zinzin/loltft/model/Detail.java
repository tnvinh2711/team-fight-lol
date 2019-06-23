package com.zinzin.loltft.model;

import java.util.List;

public class Detail {
    private String url;
    private String name;
    private String nick_name;
    private String des;
    private String stat;
    private String skill_url;
    private String skill_name;
    private String skill_des;
    private String skill_damage;
    private List<Type> type;
    private List<String> items;

    public Detail() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getSkill_url() {
        return skill_url;
    }

    public void setSkill_url(String skill_url) {
        this.skill_url = skill_url;
    }

    public String getSkill_name() {
        return skill_name;
    }

    public void setSkill_name(String skill_name) {
        this.skill_name = skill_name;
    }

    public String getSkill_des() {
        return skill_des;
    }

    public void setSkill_des(String skill_des) {
        this.skill_des = skill_des;
    }

    public String getSkill_damage() {
        return skill_damage;
    }

    public void setSkill_damage(String skill_damage) {
        this.skill_damage = skill_damage;
    }

    public List<Type> getType() {
        return type;
    }

    public void setType(List<Type> type) {
        this.type = type;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }


    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
