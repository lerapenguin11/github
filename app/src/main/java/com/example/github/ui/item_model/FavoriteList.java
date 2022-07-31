package com.example.github.ui.item_model;

public class FavoriteList {
    private String id;
    private String repoName;
    private String description;
    private String starred;
    private String update;

    public FavoriteList(String id, String repoName, String description, String starred, String update) {
        this.id = id;
        this.repoName = repoName;
        this.description = description;
        this.starred = starred;
        this.update = update;
    }

    public String getId() {
        return id;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getDescription() {
        return description;
    }

    public String getStarred() {
        return starred;
    }

    public String getUpdate() {
        return update;
    }
}
