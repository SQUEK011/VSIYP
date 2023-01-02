package com.example.vsiyp.ui.mediaeditor.menu;

import java.util.ArrayList;
import java.util.List;

public class MenuConfigBean {

    private List<EditMenuBean> editMenu;

    private List<EditMenuBean> operates;

    public List<EditMenuBean> getEditMenu() {
        List<EditMenuBean> menus = new ArrayList<>();
        if (editMenu != null) {
            menus.addAll(editMenu);
        }
        return menus;
    }

    public void setEditMenu(List<EditMenuBean> editMenu) {
        this.editMenu = editMenu;
    }

    public List<EditMenuBean> getOperates() {
        return operates;
    }

    public void setOperates(List<EditMenuBean> operates) {
        this.operates = operates;
    }
}
