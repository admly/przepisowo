package com.example.a.przepisowo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.example.a.przepisowo.adapters.ListViewAdapterPrzepisyPrezentacja;
import com.example.a.przepisowo.callbacks.FetchRecipesCallback;
import com.example.a.przepisowo.model.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwojePrzepisyActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TwojePrzepisyActivity";
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    HashMap<String, RecipeModel> docs;
    private List<String> recipesNameList;
    ListView androidGridView;
    CheckBox mojePrzepisy;
    Map<String, RecipeModel> resultMap;
    Map<String, RecipeModel> currentRecipes;
    Map<String, RecipeModel> myRecipes = new HashMap<>();
    EditText searchBar;

    int[] gridViewImageId = {
            R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie,
            R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.przepisy_prezentacja);
        db = FirebaseFirestore.getInstance();
        mojePrzepisy = findViewById(R.id.checkBox);
        mojePrzepisy.setOnClickListener(this);
        this.searchBar = findViewById(R.id.et_search);
        searchBar.clearFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        retrieveDataFromFirestore(new FetchRecipesCallback() {
            @Override
            public void onCallback(Map<String, RecipeModel> value) {
                resultMap = value;
                currentRecipes = new HashMap<>(resultMap);
                setUpListView(currentRecipes);
                setSearchListener(searchBar);
                searchBar.clearFocus();
            }
        });
    }

    /**
     * Pobierz przepisy z firestore dla aktualnego usera
     * @param callback
     */
    private void retrieveDataFromFirestore(final FetchRecipesCallback callback) {
        docs = new HashMap<>();

        db.collection("recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                docs.put(document.getId(), document.toObject(RecipeModel.class));
                            }
                            callback.onCallback(docs);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void setUpListView(final Map<String, RecipeModel> recipesList) {
        recipesNameList = new ArrayList<>();
        List<Integer> recipesTime = new ArrayList<>();
        //przygotuj liste nazw przepisow z Firestore
        for (Map.Entry<String, RecipeModel> recipe : recipesList.entrySet()) {
            recipesNameList.add(recipe.getValue().getName());
            recipesTime.add(recipe.getValue().getTime());
        }
        //zbuduj GridView z przepisami
        ListViewAdapterPrzepisyPrezentacja adapterViewAndroid = new ListViewAdapterPrzepisyPrezentacja(this, recipesNameList, gridViewImageId, recipesTime);
        androidGridView = (ListView) findViewById(R.id.listView2);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                //przeslij ID przepisu do edycji
                for (Map.Entry<String, RecipeModel> recipe : recipesList.entrySet()) {
                    if (recipe.getValue().getName().equals(recipesNameList.get(+i))) {
                        goToEdytujPrzepisActivity(recipe);

                    }
                }
            }
        });


            }

    public void goToEdytujPrzepisActivity(Map.Entry<String, RecipeModel> recipeWithId) {
        Intent intent = new Intent(this, EdytujPrzepisActivity.class);
        intent.putExtra(Constans.RECIPE_OBJECT, recipeWithId.getValue());
        intent.putExtra(Constans.RECIPE_ID, recipeWithId.getKey());
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if(i == R.id.checkBox){
            searchBar.getText().clear();
            if(mojePrzepisy.isChecked()){
                if(myRecipes.isEmpty()) {
                    for (Map.Entry<String, RecipeModel> recipe : resultMap.entrySet()){
                        if(currentUser.getUid().equals(recipe.getValue().getUID())){
                            myRecipes.put(recipe.getKey(), recipe.getValue());
                        }
                    }
                }
                currentRecipes = myRecipes;
            } else {
                currentRecipes = new HashMap<>(resultMap);
            }
            setUpListView(currentRecipes);
        }
    }

    private void setSearchListener(EditText searchBar) {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    setUpListView(filterRecipesByName(s.toString()));
                } else {
                    setUpListView(currentRecipes);
                }
            }
        });
    }

    private Map<String, RecipeModel> filterRecipesByName(String name) {
        Map<String, RecipeModel> filteredResult = new HashMap<>();
        for (Map.Entry<String, RecipeModel> r : this.currentRecipes.entrySet()) {
            if (r.getValue().getName().startsWith(name)) {
                filteredResult.put(r.getKey(), r.getValue());
            }
        }
        return filteredResult;
    }
}
