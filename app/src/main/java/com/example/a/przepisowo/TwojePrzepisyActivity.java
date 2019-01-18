package com.example.a.przepisowo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
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
                setUpListView(resultMap);
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
            System.out.println(recipe.getValue().getTime());
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
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if(i == R.id.checkBox){
            if(mojePrzepisy.isChecked()){
                Map<String, RecipeModel> myRecipesMap = new HashMap<>();
                for (Map.Entry<String, RecipeModel> recipe : resultMap.entrySet()){
                    if(currentUser.getUid().equals(recipe.getValue().getUID())){
                        myRecipesMap.put(recipe.getKey(), recipe.getValue());
                    }
                }
                setUpListView(myRecipesMap);
            } else {
                setUpListView(resultMap);
            }
        }
    }
}
