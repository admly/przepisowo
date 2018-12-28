package com.example.a.przepisowo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView txt;

    /**
     * Obsługa klikniecia w ustawienia usera
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.ustawieniaUzytkownika:
                goToSettings();
                return true;
            case R.id.costam:
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
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String result = extras.getString("mesydz");
            txt = findViewById(R.id.mesydz);
            txt.setText(result);
        }
    }
}
