package com.example.a.przepisowo.callbacks;

import com.example.a.przepisowo.model.RecipeModel;

import java.util.List;
import java.util.Map;

public interface FetchRecipesCallback {
    void onCallback(Map<String, RecipeModel> value);
}
