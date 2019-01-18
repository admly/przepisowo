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

public class DodajStepActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EdytujKrokActivity";
    RecipeModel recipeModel;
    EditText dodajStepEt;
    String recipeId;
    FirebaseFirestore db;
    String stepDoDodania;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_step);

        recipeModel = (RecipeModel) this.getIntent().getSerializableExtra(Constans.RECIPE_OBJECT);
        recipeId = this.getIntent().getStringExtra(Constans.RECIPE_ID);
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.wykonajDodajStepBt).setOnClickListener(this);
        dodajStepEt = findViewById(R.id.dodajStepEt);
    }

    private void dodajStep() {
        stepDoDodania = dodajStepEt.getText().toString();
        dodajStepDoFirestore(stepDoDodania);
        recipeModel.getSteps().add(stepDoDodania);
        goToEdytujPrzepisActivity();
    }

    private void dodajStepDoFirestore(String stepDoDodania) {
        db.collection("recipes").document(recipeId).update("steps",
                FieldValue.arrayUnion(stepDoDodania));
        Toast.makeText(DodajStepActivity.this, "Krok dodany",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if(i == R.id.wykonajDodajStepBt){
            dodajStep();
        }
    }

    private void goToEdytujPrzepisActivity() {
        Intent intent = new Intent(this, EdytujPrzepisActivity.class);
        intent.putExtra(Constans.RECIPE_OBJECT, recipeModel);
        intent.putExtra(Constans.RECIPE_ID, recipeId);
        startActivity(intent);
    }
}
