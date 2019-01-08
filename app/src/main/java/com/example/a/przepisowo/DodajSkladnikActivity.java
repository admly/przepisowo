package com.example.a.przepisowo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a.przepisowo.model.RecipeModel;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class DodajSkladnikActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EdytujSkladnikActivity";
    RecipeModel recipeModel;
    int ingredientId;
    EditText dodajSkladnikEt;
    String recipeId;
    FirebaseFirestore db;
    String skladnikDoDodania;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_skladnik);

        recipeModel = (RecipeModel) this.getIntent().getSerializableExtra(Constans.RECIPE_OBJECT);
        recipeId = this.getIntent().getStringExtra(Constans.RECIPE_ID);
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.wykonajDodajSkladnikBt).setOnClickListener(this);
        dodajSkladnikEt = findViewById(R.id.dodajSkladnikEt);
    }

    private void dodajSkladnik() {
        skladnikDoDodania = dodajSkladnikEt.getText().toString();
        dodajSkladnikDoFirestore(skladnikDoDodania);
        recipeModel.getIngredients().add(skladnikDoDodania);
        goToEdytujPrzepisActivity();
    }

    private void dodajSkladnikDoFirestore(String skladnikDoDodania) {
        db.collection("recipes").document(recipeId).update("ingredients",
                FieldValue.arrayUnion(skladnikDoDodania));
        Toast.makeText(DodajSkladnikActivity.this, "Składnik dodany",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.wykonajDodajSkladnikBt){
            dodajSkladnik();
        }
    }

    private void goToEdytujPrzepisActivity() {
        Intent intent = new Intent(this, EdytujPrzepisActivity.class);
        intent.putExtra(Constans.RECIPE_OBJECT, recipeModel);
        intent.putExtra(Constans.RECIPE_ID, recipeId);
        startActivity(intent);
    }
}
