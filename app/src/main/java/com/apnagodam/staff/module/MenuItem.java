package com.apnagodam.staff.module;

import java.util.ArrayList;

/**
 * Created by User on 31-07-2020
 */

public class MenuItem {

    private String menuTitle;
    private int menuImage;
    private ArrayList<SubList> getList;

    public MenuItem(String menuTitle, int menuImage,ArrayList<SubList> getList) {
        this.menuTitle = menuTitle;
        this.menuImage = menuImage;
        this.getList = getList;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public int getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(int menuImage) {
        this.menuImage = menuImage;
    }

    public ArrayList<SubList> getGetList() {
        return getList;
    }

    public void setGetList(ArrayList<SubList> getList) {
        this.getList = getList;
    }

    public static class SubList {
        private String subMenuTitle;
        private int subMenuImage;

        public SubList(String subMenuTitle, int menuImage) {
            this.subMenuTitle = subMenuTitle;
            this.subMenuImage = menuImage;
        }

        public String getSubMenuTitle() {
            return subMenuTitle;
        }

        public void setSubMenuTitle(String subMenuTitle) {
            this.subMenuTitle = subMenuTitle;
        }

        public int getMenuImage() {
            return subMenuImage;
        }

        public void setMenuImage(int menuImage) {
            this.subMenuImage = menuImage;
        }
    }
}

