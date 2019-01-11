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

public class EdytujInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "EdytujInfoActivity";
    RecipeModel recipeModel;
    int ingredientId;
    EditText edytujInfoEt;
    String recipeId;
    FirebaseFirestore db;
    String czasDoEdycji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edytuj_info);

        recipeModel = (RecipeModel) this.getIntent().getSerializableExtra(Constans.RECIPE_OBJECT);
        ingredientId = this.getIntent().getIntExtra(Constans.INGREDIENT_ID, -1);

        recipeId = this.getIntent().getStringExtra(Constans.RECIPE_ID);
        edytujInfoEt = findViewById(R.id.edytujInfoEt);
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.edytujInfoBt).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        edytujInfoEt.setText(Integer.toString(recipeModel.getTime()));
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.edytujInfoBt) {
            uaktualnijCzas();
        }
    }

    private void uaktualnijCzas() {
        czasDoEdycji = edytujInfoEt.getText().toString();
        wykonajEdycjeWFirestore();
        recipeModel.setTime(Integer.parseInt(czasDoEdycji));
        goToEdytujPrzepisActivity();
    }

    private void goToEdytujPrzepisActivity() {
        Intent intent = new Intent(this, EdytujPrzepisActivity.class);
        intent.putExtra(Constans.RECIPE_OBJECT, recipeModel);
        intent.putExtra(Constans.RECIPE_ID, recipeId);
        startActivity(intent);
    }

    private void wykonajEdycjeWFirestore() {
        db.collection("recipes").document(recipeId).update("time",
                Integer.parseInt(czasDoEdycji));
        Toast.makeText(EdytujInfoActivity.this, "Czas zedytowany",
                Toast.LENGTH_LONG).show();
    }

}
