package org.mir.browser.History;

public class HistoryModel {
    public String title;
    public String url;

    public HistoryModel() {
    }

    public HistoryModel(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
