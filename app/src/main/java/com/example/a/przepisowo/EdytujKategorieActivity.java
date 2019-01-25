package com.example.a.przepisowo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a.przepisowo.model.RecipeModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EdytujKategorieActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EdytujKategorieActivity";
    RecipeModel recipeModel;
    int ingredientId;
    String recipeId;
    FirebaseFirestore db;
    String czasDoEdycji;
    CheckBox sniadanieCheckBox1;
    CheckBox obiadCheckBox1;
    CheckBox kolacjaCheckBox1;
    CheckBox przystawkaCheckBox1;
    ArrayList<String> kategorieWModelu;
    ArrayList<String> kategorie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edytuj_kategorie);
        kategorieWModelu = new ArrayList<>();

        recipeModel = (RecipeModel) this.getIntent().getSerializableExtra(Constans.RECIPE_OBJECT);
        ingredientId = this.getIntent().getIntExtra(Constans.INGREDIENT_ID, -1);

        recipeId = this.getIntent().getStringExtra(Constans.RECIPE_ID);

        db = FirebaseFirestore.getInstance();
        findViewById(R.id.edytujKategorieBt).setOnClickListener(this);
        sniadanieCheckBox1 = findViewById(R.id.sniadanieCheckBox1);
        obiadCheckBox1 = findViewById(R.id.obiadCheckBox1);
        kolacjaCheckBox1= findViewById(R.id.kolacjaCheckBox1);
        przystawkaCheckBox1 = findViewById(R.id.przystawkaCheckBox1);
        kategorieWModelu.addAll(recipeModel.getCategories());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(kategorieWModelu.contains("Sniadanie")){
            sniadanieCheckBox1.setChecked(true);
        }

        if(kategorieWModelu.contains("Obiad")){
            obiadCheckBox1.setChecked(true);
        }

        if(kategorieWModelu.contains("Kolacja")){
            kolacjaCheckBox1.setChecked(true);
        }

        if(kategorieWModelu.contains("Przystawka")){
            przystawkaCheckBox1.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.edytujKategorieBt) {
            uaktualnijKategorie();
        }
    }

    private void uaktualnijKategorie() {
        kategorie = new ArrayList<>();
        if(przystawkaCheckBox1.isChecked()){
            kategorie.add("Przystawka");
        }
        if(obiadCheckBox1.isChecked()){
            kategorie.add("Obiad");
        }

        if(kolacjaCheckBox1.isChecked()){
            kategorie.add("Kolacja");
        }

        if(sniadanieCheckBox1.isChecked()){
            kategorie.add("Sniadanie");
        }

        wykonajEdycjeWFirestore();
        recipeModel.setCategories(kategorie);
        goToEdytujPrzepisActivity();
    }

    private void goToEdytujPrzepisActivity() {
        Intent intent = new Intent(this, EdytujPrzepisActivity.class);
        intent.putExtra(Constans.RECIPE_OBJECT, recipeModel);
        intent.putExtra(Constans.RECIPE_ID, recipeId);
        startActivity(intent);
    }

    private void wykonajEdycjeWFirestore() {
        db.collection("recipes").document(recipeId).update("categories",
                kategorie);
        Toast.makeText(EdytujKategorieActivity.this, "Kategorie zedytowane",
                Toast.LENGTH_LONG).show();
    }
}
