package com.apnagodam.staff.module;

public class MenuModel {
    public String menuName, url;
    public boolean hasChildren, isGroup;
    public int imageLocal;

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, String url,int imageLocal) {
        this.menuName = menuName;
        this.url = url;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
        this.imageLocal = imageLocal;
    }
}
