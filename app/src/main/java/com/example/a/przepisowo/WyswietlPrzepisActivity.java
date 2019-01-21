package com.example.a.przepisowo;

import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.a.przepisowo.fragmentsForEdytujPrzepisActivity.OcenyFragment;
import com.example.a.przepisowo.fragmentsForEdytujPrzepisActivity.SkladnikiFragment;
import com.example.a.przepisowo.fragmentsForEdytujPrzepisActivity.FragmentInfo;
import com.example.a.przepisowo.fragmentsForEdytujPrzepisActivity.KrokiPrzepisuFragment;
import com.example.a.przepisowo.model.RecipeModel;


public class WyswietlPrzepisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wyswietl_przepis);


        RecipeModel object = (RecipeModel) getIntent().getSerializableExtra(Constans.RECIPE_OBJECT);
        String recipeId = getIntent().getStringExtra(Constans.RECIPE_ID);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager1);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constans.RECIPE_OBJECT, object);
        bundle.putString(Constans.RECIPE_ID, recipeId);
        SkladnikiFragment fg1 = new SkladnikiFragment();
        KrokiPrzepisuFragment fg2 = new KrokiPrzepisuFragment();
        FragmentInfo fg3 = new FragmentInfo();
        OcenyFragment fg4 = new OcenyFragment();
        fg1.setArguments(bundle);
        fg2.setArguments(bundle);
        fg3.setArguments(bundle);
        fg4.setArguments(bundle);

        adapter.addFragment(fg1, "Składniki");
        adapter.addFragment(fg3, "Czas");
        adapter.addFragment(fg2, "Przepis");
        adapter.addFragment(fg4, "Oceny");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



