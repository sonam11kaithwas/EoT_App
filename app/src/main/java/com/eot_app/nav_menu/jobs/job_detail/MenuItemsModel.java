package com.eot_app.nav_menu.jobs.job_detail;

public class MenuItemsModel {
    private final int menu_item_id;
    private final String menu_title;
    private final int menu_icon;
    private boolean isalreadySet;
    //    private boolean hasChildren;
    private boolean isSelected;

    public MenuItemsModel(int menu_item_id, String menu_title, int menu_icon) {
        this.menu_item_id = menu_item_id;
        this.menu_title = menu_title;
        this.menu_icon = menu_icon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    //  public boolean isHasChildren() {
    //    return hasChildren;
    //}

    public boolean isIsalreadySet() {
        return isalreadySet;
    }

    public void setIsalreadySet(boolean isalreadySet) {
        this.isalreadySet = isalreadySet;
    }

    public int getMenu_item_id() {
        return menu_item_id;
    }

    public String getMenu_title() {
        return menu_title;
    }

    public int getMenu_icon() {
        return menu_icon;
    }
}
