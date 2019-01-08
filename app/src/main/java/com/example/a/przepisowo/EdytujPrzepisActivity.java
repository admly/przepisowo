package com.example.a.przepisowo;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.a.przepisowo.fragmentsForEdytujPrzepisActivity.SkladnikiFragment;
import com.example.a.przepisowo.fragmentsForEdytujPrzepisActivity.FragmentThree;
import com.example.a.przepisowo.fragmentsForEdytujPrzepisActivity.KrokiPrzepisuFragment;
import com.example.a.przepisowo.model.RecipeModel;


import java.util.ArrayList;
import java.util.List;

public class EdytujPrzepisActivity extends AppCompatActivity {
    String newIngredientNameAfterEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edytuj_przepis);


        RecipeModel object = (RecipeModel) getIntent().getSerializableExtra(Constans.RECIPE_OBJECT);
        String recipeId = getIntent().getStringExtra(Constans.RECIPE_ID);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constans.RECIPE_OBJECT, object);
        bundle.putString(Constans.RECIPE_ID, recipeId);
        SkladnikiFragment fg1 = new SkladnikiFragment();
        KrokiPrzepisuFragment fg2 = new KrokiPrzepisuFragment();
        FragmentThree fg3 = new FragmentThree();
        fg1.setArguments(bundle);
        fg2.setArguments(bundle);
        fg3.setArguments(bundle);

        adapter.addFragment(fg1, "Składniki");
        adapter.addFragment(fg2, "Przepis");
        adapter.addFragment(fg3, "iksde");
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

class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


}

