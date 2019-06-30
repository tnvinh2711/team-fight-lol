package com.zinzin.loltft.model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    String name;
    List<Hero> heroList = new ArrayList<>();

    public Team() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Hero> getHeroList() {
        return heroList;
    }

    public void setHeroList(List<Hero> heroList) {
        this.heroList = heroList;
    }

    public static class Hero{
        String url_hero;
        String name_hero;
        String cost;
        String type;
        String url_item_1;
        String url__item_combine_1_1;
        String url__item_combine_1_2;
        String url_item_2;
        String url__item_combine_2_1;
        String url__item_combine_2_2;
        String url_item_3;
        String url__item_combine_3_1;
        String url__item_combine_3_2;

        public Hero() {
        }

        public String getUrl_hero() {
            return url_hero;
        }

        public void setUrl_hero(String url_hero) {
            this.url_hero = url_hero;
        }

        public String getName_hero() {
            return name_hero;
        }

        public void setName_hero(String name_hero) {
            this.name_hero = name_hero;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl_item_1() {
            return url_item_1;
        }

        public void setUrl_item_1(String url_item_1) {
            this.url_item_1 = url_item_1;
        }

        public String getUrl__item_combine_1_1() {
            return url__item_combine_1_1;
        }

        public void setUrl__item_combine_1_1(String url__item_combine_1_1) {
            this.url__item_combine_1_1 = url__item_combine_1_1;
        }

        public String getUrl__item_combine_1_2() {
            return url__item_combine_1_2;
        }

        public void setUrl__item_combine_1_2(String url__item_combine_1_2) {
            this.url__item_combine_1_2 = url__item_combine_1_2;
        }

        public String getUrl_item_2() {
            return url_item_2;
        }

        public void setUrl_item_2(String url_item_2) {
            this.url_item_2 = url_item_2;
        }

        public String getUrl__item_combine_2_1() {
            return url__item_combine_2_1;
        }

        public void setUrl__item_combine_2_1(String url__item_combine_2_1) {
            this.url__item_combine_2_1 = url__item_combine_2_1;
        }

        public String getUrl__item_combine_2_2() {
            return url__item_combine_2_2;
        }

        public void setUrl__item_combine_2_2(String url__item_combine_2_2) {
            this.url__item_combine_2_2 = url__item_combine_2_2;
        }

        public String getUrl_item_3() {
            return url_item_3;
        }

        public void setUrl_item_3(String url_item_3) {
            this.url_item_3 = url_item_3;
        }

        public String getUrl__item_combine_3_1() {
            return url__item_combine_3_1;
        }

        public void setUrl__item_combine_3_1(String url__item_combine_3_1) {
            this.url__item_combine_3_1 = url__item_combine_3_1;
        }

        public String getUrl__item_combine_3_2() {
            return url__item_combine_3_2;
        }

        public void setUrl__item_combine_3_2(String url__item_combine_3_2) {
            this.url__item_combine_3_2 = url__item_combine_3_2;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }
    }
}
