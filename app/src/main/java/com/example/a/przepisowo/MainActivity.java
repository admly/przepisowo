package com.example.a.przepisowo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txt;
    private FirebaseAuth mAuth;
    private TextView helloUser;

    /**
     * Dodaje menu do action bara
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Obsługa dropdown menu
     * @param menu
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.ustawieniaUzytkownika:
                goToSettings();
                return true;
            case R.id.WylogujUseraViaDropdown:
                mAuth.signOut();
                goToLoginActivity();
                return true;
            case R.id.costam2:
                Toast.makeText(MainActivity.this, "Dobrze slyszales",
                        Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mAuth != null) {
            helloUser = findViewById(R.id.HelloUserTv);
            if (!mAuth.getCurrentUser().getDisplayName().isEmpty()) {
                helloUser.setText("Hello, " + mAuth.getCurrentUser().getDisplayName());
            } else {
                helloUser.setText("Hello, " + mAuth.getCurrentUser().getEmail().split("@")[0]);
            }
        }

        findViewById(R.id.twojePrzepisyBt).setOnClickListener(this);
        findViewById(R.id.DodajPrzepisBt).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void goToTwojePrzepisy() {
        Intent intent = new Intent(this, TwojePrzepisyActivity.class);
        startActivity(intent);
    }

    public void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToDodajPrzepis() {
        Intent intent = new Intent(this, DodajPrzepis.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.twojePrzepisyBt) {
            goToTwojePrzepisy();
        } if (i == R.id.DodajPrzepisBt){
            goToDodajPrzepis();
        }
    }

}
