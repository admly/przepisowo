package com.example.a.przepisowo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a.przepisowo.callbacks.FetchRecipesCallback;
import com.example.a.przepisowo.model.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class EdytujSkladnikActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EdytujSkladnikActivity";
    RecipeModel recipeModel;
    int ingredientId;
    EditText edytujSkladnikEt;
    String recipeId;
    FirebaseFirestore db;
    String newIngredientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edytuj_skladnik);

        recipeModel = (RecipeModel) this.getIntent().getSerializableExtra(Constans.RECIPE_OBJECT);
        ingredientId = this.getIntent().getIntExtra(Constans.INGREDIENT_ID, -1);
        recipeId = this.getIntent().getStringExtra(Constans.RECIPE_ID);
        edytujSkladnikEt = findViewById(R.id.edytujSkladnikEt);
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.edytujSkladnikBt).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        edytujSkladnikEt.setText(recipeModel.getIngredients().get(ingredientId));
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.edytujSkladnikBt) {
            uaktualnijSkladnik();
        }
    }

    private void uaktualnijSkladnik() {
        newIngredientName = edytujSkladnikEt.getText().toString();
        retrieveDataFromFirestore(newIngredientName);

    }

    private void retrieveDataFromFirestore(String newIngredientName) {
        db.collection("recipes").document(recipeId).update("ingredients",
                FieldValue.arrayRemove(recipeModel.getIngredients().get(ingredientId)));
        db.collection("recipes").document(recipeId).update("ingredients",
                FieldValue.arrayUnion(newIngredientName));
        Toast.makeText(EdytujSkladnikActivity.this, "Składnik zedytowany",
                Toast.LENGTH_LONG).show();

        goToEdytujPrzepisActivity();
    }

    private void goToEdytujPrzepisActivity() {
        Intent intent = new Intent(this, EdytujPrzepisActivity.class);
        recipeModel.getIngredients().set(ingredientId, newIngredientName);
        intent.putExtra(Constans.RECIPE_OBJECT, recipeModel);
        intent.putExtra(Constans.RECIPE_ID, recipeId);

        startActivity(intent);
    }
}
