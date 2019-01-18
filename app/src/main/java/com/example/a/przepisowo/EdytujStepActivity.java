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

public class EdytujStepActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EdytujStepActivity";
    RecipeModel recipeModel;
    int stepId;
    EditText edytujStepEt;
    String recipeId;
    FirebaseFirestore db;
    String newStepName;
    String stepDoUsuniecia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edytuj_step);

        recipeModel = (RecipeModel) this.getIntent().getSerializableExtra(Constans.RECIPE_OBJECT);
        stepId = this.getIntent().getIntExtra(Constans.STEP_ID, -1);
        recipeId = this.getIntent().getStringExtra(Constans.RECIPE_ID);

        edytujStepEt = findViewById(R.id.edytujStepEt);
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.edytujStepBt).setOnClickListener(this);
        findViewById(R.id.usunStepBt).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        edytujStepEt.setText(recipeModel.getSteps().get(stepId));
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if(i == R.id.edytujStepBt){
            edytujStepPrzepisu();
        } else if (i == R.id.usunStepBt) {
            usunStep();
        }
    }

    private void usunStep() {
        stepDoUsuniecia = edytujStepEt.getText().toString();
        wykonajUsuwanieWFirestore();
        recipeModel.getSteps().remove(stepId);
        goToEdytujPrzepisActivity();
    }

    private void wykonajUsuwanieWFirestore() {
        db.collection("recipes").document(recipeId).update("steps",
                FieldValue.arrayRemove(recipeModel.getSteps().get(stepId)));
        Toast.makeText(EdytujStepActivity.this, "Składnik usuniety",
                Toast.LENGTH_LONG).show();
    }

    private void edytujStepPrzepisu() {
        newStepName = edytujStepEt.getText().toString();
        retrieveDataFromFirestore(newStepName);
        recipeModel.getSteps().set(stepId, newStepName);
        goToEdytujPrzepisActivity();
    }

    private void retrieveDataFromFirestore(String newStepName) {
        db.collection("recipes").document(recipeId).update("steps",
                FieldValue.arrayRemove(recipeModel.getSteps().get(stepId)));
        db.collection("recipes").document(recipeId).update("steps",
                FieldValue.arrayUnion(newStepName));
        Toast.makeText(EdytujStepActivity.this, "Krok zedytowany",
                Toast.LENGTH_LONG).show();

    }

    private void goToEdytujPrzepisActivity() {
        Intent intent = new Intent(this, EdytujPrzepisActivity.class);
        intent.putExtra(Constans.RECIPE_OBJECT, recipeModel);
        intent.putExtra(Constans.RECIPE_ID, recipeId);
        startActivity(intent);
    }
}
