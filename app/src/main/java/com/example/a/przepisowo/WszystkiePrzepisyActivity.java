package com.example.a.przepisowo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.a.przepisowo.adapters.GridAdapter;
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

public class WszystkiePrzepisyActivity extends AppCompatActivity {
    private static final String TAG = "WszystkiePrzepisyAct";
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    HashMap<String, RecipeModel> docs;
    private List<String> recipesNameList;
    GridView androidGridView;

    int[] gridViewImageId = {
            R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie,
            R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wszystkie_przepisy);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        retrieveDataFromFirestore(new FetchRecipesCallback() {
            @Override
            public void onCallback(Map<String, RecipeModel> value) {
                setUpGridView(value);
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

    private void setUpGridView(final Map<String, RecipeModel> recipesList) {
        recipesNameList = new ArrayList<>();
        //przygotuj liste nazw przepisow z Firestore
        for (Map.Entry<String, RecipeModel> recipe : recipesList.entrySet()) {
            recipesNameList.add(recipe.getValue().getName());
        }

        //zbuduj GridView z przepisami
        GridAdapter adapterViewAndroid = new GridAdapter(this, recipesNameList, gridViewImageId);
        androidGridView = (GridView) findViewById(R.id.grid_view_image_text1);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                //przeslij ID przepisu do edycji
                for (Map.Entry<String, RecipeModel> recipe : recipesList.entrySet()) {
                    if (recipe.getValue().getName().equals(recipesNameList.get(+i))) {
                        goToWyswietlPrzepis(recipe);

                    }
                }
            }
        });
    }

    private void goToWyswietlPrzepis(Map.Entry<String,RecipeModel> recipeWithId) {
        Intent intent = new Intent(this, WyswietlPrzepisActivity.class);
        intent.putExtra(Constans.RECIPE_OBJECT, recipeWithId.getValue());
        intent.putExtra(Constans.RECIPE_ID, recipeWithId.getKey());
        startActivity(intent);
        startActivity(intent);
    }

}
