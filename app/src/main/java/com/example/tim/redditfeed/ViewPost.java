package com.example.tim.redditfeed;

public class ViewPost {
    private String title;
    private String author;
    private String date_updated;
    private String postURL;
    private String thumbnail_URL;
    public  String Bob = "bob";

    public ViewPost(String title, String author, String date_updated, String postURL, String thumbnail_URL) {
        this.title = title;
        this.author = author;
        this.date_updated = date_updated;
        this.postURL = postURL;
        this.thumbnail_URL = thumbnail_URL;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public String getThumbnail_URL() {
        return thumbnail_URL;
    }

    public void setThumbnail_URL(String thumbnail_URL) {
        this.thumbnail_URL = thumbnail_URL;
    }
}

