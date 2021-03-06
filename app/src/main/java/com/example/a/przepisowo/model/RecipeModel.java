package com.example.a.przepisowo.model;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeModel implements Serializable {
    private String UID;
    private ArrayList<String> ingredients;
    private String name;
    private ArrayList<String> steps;
    private int time;
    private ArrayList<String> categories;

    public RecipeModel(String UID, ArrayList<String> ingredients,  String name, ArrayList<String> steps, int time, ArrayList<String> categories){
        this.UID = UID;
        this.ingredients = ingredients;
        this.name = name;
        this.steps = steps;
        this.time = time;
        this.categories = categories;
    }

    public RecipeModel(){}

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
}
