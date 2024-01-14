package org.mir.browser.Tab;

public class TabDataModel {
    private String tabName;

    public TabDataModel() {
        tabName = "New Tab";
    }

    public TabDataModel(String tabName) {
        this.tabName = tabName;
    }

    public String getTabName() {
        return tabName;
    }
}
