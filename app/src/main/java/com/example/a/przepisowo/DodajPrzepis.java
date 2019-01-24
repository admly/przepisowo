package com.example.a.przepisowo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a.przepisowo.model.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DodajPrzepis extends AppCompatActivity implements View.OnClickListener {
    EditText nazwaPrzepisuEt;
    EditText skladnikiEt;
    EditText przepisEt;
    EditText czasEt;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    CheckBox sniadanieCheckBox;
    CheckBox obiadCheckBox;
    CheckBox kolacjaCheckBox;
    CheckBox przystawkaCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_przepis);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.dodajNowyPrzepisBt).setOnClickListener(this);

        db = FirebaseFirestore.getInstance();

        nazwaPrzepisuEt = findViewById(R.id.nazwaPrzepisuEt);
        skladnikiEt = findViewById(R.id.skladnikiEt);
        przepisEt = findViewById(R.id.przepisEt);
        czasEt = findViewById(R.id.czasEt);
        sniadanieCheckBox = findViewById(R.id.sniadanieCheckBox);
        obiadCheckBox = findViewById(R.id.obiadCheckBox);
        kolacjaCheckBox= findViewById(R.id.kolacjaCheckBox);
        przystawkaCheckBox = findViewById(R.id.przystawkaCheckBox);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.dodajNowyPrzepisBt) {
            if(!validateForm()){
                return;
            }
            dodajPrzepisProcess();
        }
    }

    private void dodajPrzepisProcess() {
        RecipeModel recipeModel = prepareNewRecipeObject();
        db.collection("recipes").add(recipeModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DodajPrzepis.this, "Dodano nowy przepis!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DodajPrzepis.this, "Coś poszło nie tak, spróbuj pozniej!",
                            Toast.LENGTH_LONG).show();
                }
                goToTwojePrzepisy();
            }
        });
    }

    private RecipeModel prepareNewRecipeObject() {
        String nazwaPrzepisu = nazwaPrzepisuEt.getText().toString();
        String skladnikiUnprocessed = skladnikiEt.getText().toString();
        String przepisUnprocessed = przepisEt.getText().toString();
        int czas = Integer.valueOf(czasEt.getText().toString());

        ArrayList<String> skladniki = new ArrayList<>(Arrays.asList(skladnikiUnprocessed.trim()
                .replaceAll("\\s+", " ")
                .split("\\s*,\\s*")));

        ArrayList<String> przepis = new ArrayList<>(Arrays.asList(przepisUnprocessed.trim()
                .replaceAll("\\s+", " ")
                .split("\\s*,\\s*")));

        List<String> kategorie = new ArrayList<>();
        if(przystawkaCheckBox.isChecked()){
            kategorie.add("Przystawka");
        }
        if(obiadCheckBox.isChecked()){
            kategorie.add("Obiad");
        }

        if(kolacjaCheckBox.isChecked()){
            kategorie.add("Kolacja");
        }

        if(sniadanieCheckBox.isChecked()){
            kategorie.add("Sniadanie");
        }

        return new RecipeModel(currentUser.getUid(), skladniki,
                nazwaPrzepisu.trim(), przepis, czas, (ArrayList<String>) kategorie);
    }

    public void goToTwojePrzepisy() {
        Intent intent = new Intent(this, TwojePrzepisyActivity.class);
        startActivity(intent);
    }


    private boolean validateForm() {
        boolean valid = true;

        String nazwaPrzepisu = nazwaPrzepisuEt.getText().toString();
        if (TextUtils.isEmpty(nazwaPrzepisu)) {
            nazwaPrzepisuEt.setError("Required.");
            valid = false;
        } else {
            nazwaPrzepisuEt.setError(null);
        }

        String skladniki = skladnikiEt.getText().toString();
        if (TextUtils.isEmpty(skladniki)) {
            skladnikiEt.setError("Required.");
            valid = false;
        } else {
            skladnikiEt.setError(null);
        }

        String przepis = przepisEt.getText().toString();
        if (TextUtils.isEmpty(przepis)) {
            przepisEt.setError("Required.");
            valid = false;
        } else {
            przepisEt.setError(null);
        }

        String czas = czasEt.getText().toString();
        if (TextUtils.isEmpty(czas)) {
            czasEt.setError("Required.");
            valid = false;
        } else {
            czasEt.setError(null);
        }

        return valid;
    }
}
