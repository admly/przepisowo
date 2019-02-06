package com.example.a.przepisowo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.example.a.przepisowo.adapters.ListViewAdapterPrzepisyPrezentacja;
import com.example.a.przepisowo.callbacks.FetchRecipesCallback;
import com.example.a.przepisowo.dialogFragment.FilterDialogFragment;
import com.example.a.przepisowo.dialogFragment.NoticeDialogListener;
import com.example.a.przepisowo.model.Categories;
import com.example.a.przepisowo.model.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwojePrzepisyActivity extends AppCompatActivity implements View.OnClickListener, NoticeDialogListener {

    private static final String TAG = "TwojePrzepisyActivity";
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    HashMap<String, RecipeModel> docs;
    private List<String> recipesNameList;
    ListView androidGridView;
    Map<String, RecipeModel> resultMap;
    Map<String, RecipeModel> currentRecipes;
    Map<String, RecipeModel> myRecipes = new HashMap<>();
    EditText searchBar;
    Button filterPrzepisyBt;
    Categories categoriesObj;
    List<String> filteredCategories;
    int[] gridViewImageId = {
            R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie,
            R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie, R.drawable.jedzenie,
    };

    Button nameFilter;
    Button ingFilter;
    SearchStrategy searchStrategy = SearchStrategy.NAME;
    Drawable activeButtonDrawable;
    Drawable deactivatedButtonDrawable;


    FirestoreService firestoreService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.przepisy_prezentacja);
        db = FirebaseFirestore.getInstance();
        this.searchBar = findViewById(R.id.et_search);
        this.nameFilter = findViewById(R.id.name_button);
        this.ingFilter = findViewById(R.id.ing_button);
        activeButtonDrawable = getResources().getDrawable(R.drawable.roundedbutton);
        deactivatedButtonDrawable = getResources().getDrawable(R.drawable.greyed_roundedbutton);
        searchBar.clearFocus();
        findViewById(R.id.powrotDoMenu).setOnClickListener(this);
        findViewById(R.id.filterPrzepisyBt).setOnClickListener(this);
        findViewById(R.id.name_button).setOnClickListener(this);
        findViewById(R.id.ing_button).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firestoreService = new FirestoreService(mAuth, db);
        getCategoriesFromFirestore();
        firestoreService.getLastRecipesFromFirestore(new FetchRecipesCallback() {
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
     * Pobierz liste kategorii
     *
     * @param
     */
    private void getCategoriesFromFirestore() {
        docs = new HashMap<>();

        db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                categoriesObj = document.toObject(Categories.class);
                            }
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
        if (i == R.id.name_button) {
            this.searchStrategy = SearchStrategy.NAME;
            nameFilter.setBackground(activeButtonDrawable);
            ingFilter.setBackground(deactivatedButtonDrawable);
            searchBar.getText().clear();
        }
        if (i == R.id.ing_button) {
            this.searchStrategy = SearchStrategy.INGREDIENT;
            nameFilter.setBackground(deactivatedButtonDrawable);
            ingFilter.setBackground(activeButtonDrawable);
            searchBar.getText().clear();
        }
//        if (i == R.id.checkBox) {
//            searchBar.getText().clear();
//            // TODO: get my recipes from DB since we limit the result first!
//            if (mojePrzepisy.isChecked()) {
//                firestoreService.getMyRecipesOnly(new FetchRecipesCallback() {
//                    @Override
//                    public void onCallback(Map<String, RecipeModel> value) {
//                        resultMap = value;
//                        currentRecipes = new HashMap<>(resultMap);
//                        setUpListView(currentRecipes);
//                    }
//                });
//            } else {
//                firestoreService.getLastRecipesFromFirestore(new FetchRecipesCallback() {
//                    @Override
//                    public void onCallback(Map<String, RecipeModel> value) {
//                        resultMap = value;
//                        currentRecipes = new HashMap<>(resultMap);
//                        setUpListView(currentRecipes);
//                    }
//                });
//            }
//        }
        if (i == R.id.powrotDoMenu) {
            goToMainActivity();
        }
        if (i == R.id.filterPrzepisyBt) {
            FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constans.CATEGORIES_OBJECT, categoriesObj);
            bundle.putStringArrayList(Constans.FILTERED_CATEGORIES, (ArrayList<String>) filteredCategories);
            filterDialogFragment.setArguments(bundle);
            filterDialogFragment.show(getFragmentManager(), "FilterDialogFragment");
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
                if (s.toString().length() > 1) {
                    if (searchStrategy.equals(SearchStrategy.NAME)) {
                        firestoreService.getRecipesByName(new FetchRecipesCallback() {
                            @Override
                            public void onCallback(Map<String, RecipeModel> value) {
                                resultMap = value;
                                currentRecipes = new HashMap<>(resultMap);
                                setUpListView(currentRecipes);
                            }
                        }, s.toString());
                    } else {
                        firestoreService.getRecipesByIngredients(new FetchRecipesCallback() {
                            @Override
                            public void onCallback(Map<String, RecipeModel> value) {
                                resultMap = value;
                                currentRecipes = new HashMap<>(resultMap);
                                setUpListView(currentRecipes);
                            }
                        }, s.toString());
                    }
                } else {
                    firestoreService.getLastRecipesFromFirestore(new FetchRecipesCallback() {
                        @Override
                        public void onCallback(Map<String, RecipeModel> value) {
                            resultMap = value;
                            currentRecipes = new HashMap<>(resultMap);
                            setUpListView(currentRecipes);
                        }
                    });
                }
                filteredCategories = new ArrayList<>();
            }
        });
    }

    private Map<String, RecipeModel> filterRecipesByCategories(List<String> input) {
        Map<String, RecipeModel> filteredResult = new HashMap<>();
        List<String> categories = new ArrayList<>();
        boolean onlyMine = false;
        for (String cat : input) {
            if (!cat.equals("Tylko moje")) {
                categories.add(cat);
            } else {
                onlyMine = true;
            }
        }
        for (Map.Entry<String, RecipeModel> r : this.currentRecipes.entrySet()) {
            if (r.getValue().getCategories() != null) {
                ArrayList<String> list = r.getValue().getCategories();
                if (CollectionUtils.containsAll(list, categories)) {
                    if (onlyMine) {
                        if(mAuth.getUid().equals(r.getValue().getUID())){
                            filteredResult.put(r.getKey(), r.getValue());
                        }
                    } else {
                        filteredResult.put(r.getKey(), r.getValue());
                    }
                }
            }
        }

        return filteredResult;
    }

    @Override
    public void onDialogPositiveClick(FilterDialogFragment dialog) {
        searchBar.getText().clear();
        filteredCategories = dialog.getmSelectedItems();
        if (filteredCategories.size() > 0) {
            firestoreService.getAllRecipesFirestore(new FetchRecipesCallback() {
                @Override
                public void onCallback(Map<String, RecipeModel> value) {
                    resultMap = value;
                    currentRecipes = new HashMap<>(resultMap);
                    setUpListView(filterRecipesByCategories(filteredCategories));
                }
            });
        } else {
            firestoreService.getLastRecipesFromFirestore(new FetchRecipesCallback() {
                @Override
                public void onCallback(Map<String, RecipeModel> value) {
                    resultMap = value;
                    currentRecipes = new HashMap<>(resultMap);
                    setUpListView(currentRecipes);
                }
            });
        }
    }

    @Override
    public void onDialogNegativeClick(FilterDialogFragment dialog) {
        dialog.getShowsDialog();
    }

    private enum SearchStrategy {
        NAME, INGREDIENT;
    }
}
