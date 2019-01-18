package com.example.a.przepisowo.fragmentsForEdytujPrzepisActivity;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.a.przepisowo.Constans;
import com.example.a.przepisowo.DodajSkladnikActivity;
import com.example.a.przepisowo.DodajStepActivity;
import com.example.a.przepisowo.EdytujSkladnikActivity;
import com.example.a.przepisowo.EdytujStepActivity;
import com.example.a.przepisowo.R;
import com.example.a.przepisowo.TwojePrzepisyActivity;
import com.example.a.przepisowo.model.RecipeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class KrokiPrzepisuFragment extends Fragment implements View.OnClickListener {
    private ListView list;
    private ArrayAdapter<String> adapter;
    private List<String> foodList;
    RecipeModel recipeModel;
    String recipeId;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public KrokiPrzepisuFragment() {
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_kroki_przepisu, container, false);
        rootView.findViewById(R.id.dodajKrokPrzepisuBt).setOnClickListener(this);
        rootView.findViewById(R.id.twojePrzepisyWsteczBt1).setOnClickListener(this);

        currentUser = mAuth.getCurrentUser();
        if(recipeModel.getUID() == null || !recipeModel.getUID().equals(currentUser.getUid())){
            rootView.findViewById(R.id.dodajKrokPrzepisuBt).setVisibility(View.INVISIBLE);
        }
        foodList = new ArrayList<>();
        foodList.addAll(recipeModel.getSteps());

        list = (ListView) rootView.findViewById(R.id.listView1);
        adapter = new ArrayAdapter<String>(this.getContext(), R.layout.row, foodList);
        list.setAdapter(adapter);
        if(recipeModel.getUID()!= null ){
        if(currentUser.getUid().equals(recipeModel.getUID())) {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    goToEdytujStep(i);
                }
            });
        }}
        // Inflate the layout for this fragment
        return rootView;
    }
    private void goToEdytujStep(int stepId) {
        Intent intent = new Intent(this.getContext(), EdytujStepActivity.class);
        intent.putExtra(Constans.RECIPE_OBJECT, recipeModel);
        intent.putExtra(Constans.RECIPE_ID, recipeId);
        intent.putExtra(Constans.STEP_ID, stepId);
        startActivity(intent);
    }

    private void goToDodajStepActivity() {
        Intent intent = new Intent(this.getActivity(), DodajStepActivity.class);
        intent.putExtra(Constans.RECIPE_OBJECT, recipeModel);
        intent.putExtra(Constans.RECIPE_ID, recipeId);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if(i == R.id.dodajKrokPrzepisuBt){
            goToDodajStepActivity();
        } if (i == R.id.twojePrzepisyWsteczBt1) {
            goToTwojePrzepisy();
        }
    }

    private void goToTwojePrzepisy() {
        Intent intent = new Intent(this.getActivity(), TwojePrzepisyActivity.class);
        startActivity(intent);
    }
}


