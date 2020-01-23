package com.example.newapp;

public class MatchRegistrations {
    String fid, url;

    public MatchRegistrations(String fid) {
        this.fid = fid;
    }

    public MatchRegistrations() {

    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
