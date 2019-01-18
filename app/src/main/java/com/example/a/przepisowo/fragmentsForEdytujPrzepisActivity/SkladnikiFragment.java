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
import com.example.a.przepisowo.DodajSkladnikActivity;
import com.example.a.przepisowo.EdytujSkladnikActivity;
import com.example.a.przepisowo.MainActivity;
import com.example.a.przepisowo.R;
import com.example.a.przepisowo.TwojePrzepisyActivity;
import com.example.a.przepisowo.model.RecipeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class SkladnikiFragment extends Fragment implements View.OnClickListener{
    private ListView list;
    private ArrayAdapter<String> adapter;
    private List<String> foodList;
    RecipeModel recipeModel;
    String recipeId;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public SkladnikiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        recipeModel = (RecipeModel) this.getArguments().getSerializable(Constans.RECIPE_OBJECT);
        recipeId = this.getArguments().getString(Constans.RECIPE_ID);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_skladniki, container, false);
        rootView.findViewById(R.id.dodajSkladnikBt).setOnClickListener(this);
        rootView.findViewById(R.id.twojePrzepisyWsteczBt2).setOnClickListener(this);


        currentUser = mAuth.getCurrentUser();
        if(recipeModel.getUID() == null || !recipeModel.getUID().equals(currentUser.getUid())){
            rootView.findViewById(R.id.dodajSkladnikBt).setVisibility(View.INVISIBLE);
        }
        foodList = new ArrayList<>();
        foodList.addAll(recipeModel.getIngredients());

        list = (ListView) rootView.findViewById(R.id.listView1);
        adapter = new ArrayAdapter<String>(this.getContext(), R.layout.row, foodList);
        list.setAdapter(adapter);

        if(recipeModel.getUID()!= null ){
            if(currentUser.getUid().equals(recipeModel.getUID())) {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    goToEdytujSkladnik(i);
                }
            });
        }}
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

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if(i == R.id.dodajSkladnikBt){
            goToDodajSkladnikActivity();
        } if (i == R.id.twojePrzepisyWsteczBt2) {
            goToTwojePrzepisy();
        }
    }

    private void goToDodajSkladnikActivity() {
        Intent intent = new Intent(this.getActivity(), DodajSkladnikActivity.class);
        intent.putExtra(Constans.RECIPE_OBJECT, recipeModel);
        intent.putExtra(Constans.RECIPE_ID, recipeId);
        startActivity(intent);
    }
    private void goToTwojePrzepisy() {
        Intent intent = new Intent(this.getActivity(), TwojePrzepisyActivity.class);
        startActivity(intent);
    }
}


