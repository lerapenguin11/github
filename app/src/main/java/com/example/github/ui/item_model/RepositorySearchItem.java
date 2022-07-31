package com.example.github.ui.item_model;

public class RepositorySearchItem {
    private String id;
    private String title_repo;
    private String description_repo;
    private String update_repo;
    private String starred_repo;
    private String favoriteStatus;

    public RepositorySearchItem(String id, String title_repo, String description_repo,
                                String update_repo, String starred_repo, String favoriteStatus) {
        this.id = id;
        this.title_repo = title_repo;
        this.description_repo = description_repo;
        this.update_repo = update_repo;
        this.starred_repo = starred_repo;
        this.favoriteStatus = favoriteStatus;
    }

    public String getFavoriteStatus() {
        return favoriteStatus;
    }

    public String getId() {
        return id;
    }

    public String getTitle_repo() {
        return title_repo;
    }

    public String getDescription_repo() {
        return description_repo;
    }

    public String getUpdate_repo() {
        return update_repo;
    }

    public String getStarred_repo() {
        return starred_repo;
    }

    public void setFavoriteStatus(String favoriteStatus) {
        this.favoriteStatus = favoriteStatus;
    }
}
