package com.example.a.przepisowo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txt;
    private FirebaseAuth mAuth;
    private TextView helloUser;
    private ImageView ivDodajPrzepis;
    private ImageView ivPrzepisy;

    /**
     * Dodaje menu do action bara
     * @param menu
     * @return
     */
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    /**
//     * Obsługa dropdown menu
//     * @param menu
//     * @return
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case R.id.ustawieniaUzytkownika:
//                goToSettings();
//                return true;
//            case R.id.WylogujUseraViaDropdown:
//                mAuth.signOut();
//                goToLoginActivity();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ivDodajPrzepis = findViewById(R.id.iv_dodaj_przepis);
        ivDodajPrzepis.setClickable(true);
        ivDodajPrzepis.setOnClickListener(this);

        ivPrzepisy = findViewById(R.id.iv_przepisy);
        ivPrzepisy.setClickable(true);
        ivPrzepisy.setOnClickListener(this);

        ivDodajPrzepis = findViewById(R.id.iv_logout);
        ivDodajPrzepis.setClickable(true);
        ivDodajPrzepis.setOnClickListener(this);

        ivDodajPrzepis = findViewById(R.id.iv_user);
        ivDodajPrzepis.setClickable(true);
        ivDodajPrzepis.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth != null) {
            helloUser = findViewById(R.id.helloUserTv);
            if (!mAuth.getCurrentUser().getDisplayName().isEmpty()) {
                helloUser.setText(mAuth.getCurrentUser().getDisplayName());
            } else {
                helloUser.setText(mAuth.getCurrentUser().getEmail().split("@")[0]);
            }
        }

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
        if (i == R.id.iv_przepisy) {
            goToTwojePrzepisy();
        } if (i == R.id.iv_dodaj_przepis) {
            goToDodajPrzepis();
        } if (i == R.id.iv_logout) {
            mAuth.signOut();
            goToLoginActivity();
        } if (i == R.id.iv_user) {
            goToSettings();
        }
    }


}
