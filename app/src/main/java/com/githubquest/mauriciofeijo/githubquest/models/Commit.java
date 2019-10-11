package com.githubquest.mauriciofeijo.githubquest.models;

import java.util.Date;

public class Commit {
    private String mAuthorName, mMessage, mUrl;
    private Date mDate;

    public Commit(String authorName, String mesasge, String url, Date date) {
        mAuthorName = authorName;
        mMessage = mesasge;
        mUrl = url;
        mDate = date;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String authorName) {
        mAuthorName = authorName;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
