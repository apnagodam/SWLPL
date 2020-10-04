package com.apnagodam.staff.module;

/**
 * Created by User on 31-07-2020
 */

public class MenuItem {

    private String menuTitle;
    private int menuImage;

    public MenuItem(String menuTitle, int menuImage) {
        this.menuTitle = menuTitle;
        this.menuImage = menuImage;
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
}
