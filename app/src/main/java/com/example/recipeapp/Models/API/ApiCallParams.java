package com.example.recipeapp.Models.API;

public class ApiCallParams {
    private String title;
    private String cuisine;
    private String excludeCuisine;
    private String included;
    private String excluded;
    private String type;
    private String time;

    public ApiCallParams(String title, String cuisine, String excludeCuisine, String included, String excluded, String type, String time) {
        this.title = title;
        this.cuisine = cuisine;
        this.excludeCuisine = excludeCuisine;
        this.included = included;
        this.excluded = excluded;
        this.type = type;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getCuisine() {
        return cuisine;
    }

    public String getExcludeCuisine() {
        return excludeCuisine;
    }

    public String getIncluded() {
        return included;
    }

    public String getExcluded() {
        return excluded;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }
}
