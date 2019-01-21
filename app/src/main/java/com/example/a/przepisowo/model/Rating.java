package com.example.a.przepisowo.model;

public class Rating {
    private String recipeId;
    private String uid;
    private float rating;

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Rating(String recipeId, String uid, float rating) {
        this.recipeId = recipeId;
        this.uid = uid;
        this.rating = rating;
    }
}
