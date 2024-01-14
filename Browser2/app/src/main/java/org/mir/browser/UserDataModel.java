package org.mir.browser;

import org.mir.browser.Bookmarks.BookmarkModel;
import org.mir.browser.Downloads.DownloadModel;
import org.mir.browser.History.HistoryModel;

public class UserDataModel {
    private HistoryModel History;
    private BookmarkModel Bookmarks;
    private DownloadModel Downloads;

    public UserDataModel() {

    }

    public UserDataModel(HistoryModel history, BookmarkModel bookmarks, DownloadModel downloads) {
        History = history;
        Bookmarks = bookmarks;
        Downloads = downloads;
    }

    public HistoryModel getHistory() {
        return History;
    }

    public void setHistory(HistoryModel history) {
        History = history;
    }

    public BookmarkModel getBookmarks() {
        return Bookmarks;
    }

    public void setBookmarks(BookmarkModel bookmarks) {
        Bookmarks = bookmarks;
    }

    public DownloadModel getDownloads() {
        return Downloads;
    }

    public void setDownloads(DownloadModel downloads) {
        Downloads = downloads;
    }
}
