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
import com.example.a.przepisowo.EdytujInfoActivity;
import com.example.a.przepisowo.EdytujKategorieActivity;
import com.example.a.przepisowo.EdytujStepActivity;
import com.example.a.przepisowo.R;
import com.example.a.przepisowo.TwojePrzepisyActivity;
import com.example.a.przepisowo.model.RecipeModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentInfo extends Fragment implements View.OnClickListener {
    private ListView list;
    private ArrayAdapter<String> adapter;
    private List<String> foodList;
    RecipeModel recipeModel;
    String recipeId;

    public FragmentInfo() {
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

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        rootView.findViewById(R.id.twojePrzepisyWsteczBt3).setOnClickListener(this);


        foodList = new ArrayList<>();
        foodList.add(Integer.toString(recipeModel.getTime()));

        list = (ListView) rootView.findViewById(R.id.listView1);
        adapter = new ArrayAdapter<String>(this.getContext(), R.layout.row, foodList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToEdytujInfo();
            }
        });
        if(recipeModel.getCategories() != null) {
            foodList = new ArrayList<>();
            foodList.addAll(recipeModel.getCategories());
            list = (ListView) rootView.findViewById(R.id.listView4);
            adapter = new ArrayAdapter<String>(this.getContext(), R.layout.row, foodList);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    goToEdytujKategorie();
                }
            });

        }

        return rootView;
    }

    private void goToEdytujKategorie() {
        Intent intent = new Intent(this.getContext(), EdytujKategorieActivity.class);
        intent.putExtra(Constans.RECIPE_OBJECT, recipeModel);
        intent.putExtra(Constans.RECIPE_ID, recipeId);
        startActivity(intent);
    }

    private void goToEdytujInfo() {
        Intent intent = new Intent(this.getContext(), EdytujInfoActivity.class);
        intent.putExtra(Constans.RECIPE_OBJECT, recipeModel);
        intent.putExtra(Constans.RECIPE_ID, recipeId);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.twojePrzepisyWsteczBt3) {
            goToTwojePrzepisy();
        }
    }
    private void goToTwojePrzepisy() {
        Intent intent = new Intent(this.getActivity(), TwojePrzepisyActivity.class);
        startActivity(intent);
    }
}


