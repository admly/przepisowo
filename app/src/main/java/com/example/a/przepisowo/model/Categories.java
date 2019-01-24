package com.example.a.przepisowo.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Categories implements Serializable {
    ArrayList<String> dishTypes;

    public Categories(ArrayList<String> dishTypes) {
        this.dishTypes = dishTypes;
    }

    public Categories(){}

    public ArrayList<String> getDishTypes() {
        return dishTypes;
    }

    public void setDishTypes(ArrayList<String> dishTypes) {
        this.dishTypes = dishTypes;
    }
}
