package com.example.recipeapp.Models;

import org.parceler.Parcel;

@Parcel
public class Ingredient {
    String name;
    Long id;

    public Ingredient(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public Ingredient() {
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}