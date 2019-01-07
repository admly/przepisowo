package com.example.a.przepisowo.fragmentsForEdytujPrzepisActivity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.a.przepisowo.Constans;
import com.example.a.przepisowo.EdytujSkladnikActivity;
import com.example.a.przepisowo.R;
import com.example.a.przepisowo.model.RecipeModel;

import java.util.ArrayList;
import java.util.List;

public class SkladnikiFragment extends Fragment {
    private ListView list;
    private ArrayAdapter<String> adapter;
    private List<String> foodList;
    RecipeModel recipeModel;
    String recipeId;

    public SkladnikiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeModel = (RecipeModel) this.getArguments().getSerializable(Constans.RECIPE_OBJECT);
        recipeId = this.getArguments().getString(Constans.RECIPE_ID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_skladniki, container, false);

        foodList = new ArrayList<>();
        foodList.addAll(recipeModel.getIngredients());

        list = (ListView) rootView.findViewById(R.id.listView1);
        adapter = new ArrayAdapter<String>(this.getContext(), R.layout.row, foodList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToEdytujSkladnik(i);
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    private void goToEdytujSkladnik(int ingredientId) {
            Intent intent = new Intent(this.getContext(), EdytujSkladnikActivity.class);
            intent.putExtra(Constans.RECIPE_OBJECT, recipeModel);
            intent.putExtra(Constans.RECIPE_ID, recipeId);
            intent.putExtra(Constans.INGREDIENT_ID, ingredientId);
            startActivity(intent);
    }

}


