package com.example.tim.redditfeed.Comment;

public class Comment {
    private String comment;
    private String author;
    private String id;
    private String date;

    public Comment(String comment, String author, String id) {
        this.comment = comment;
        this.author = author;
        this.id = id;
//        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment='" + comment + '\'' +
                ", author='" + author + '\'' +
                ", id='" + id + '\'' +
                '}';
}
}
