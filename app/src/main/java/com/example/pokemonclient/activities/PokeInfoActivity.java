package com.example.pokemonclient.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokemonclient.views.ImageFragment;
import com.example.pokemonclient.models.Poke;
import com.example.pokemonclient.R;
import com.example.pokemonclient.views.ViewPagerAdapter;

//activity with wide information about single pokemon
public class PokeInfoActivity extends AppCompatActivity {

    public static final String POKE_OBJECT = "pokeObject";
    TextView pokeNameTextView;
    TextView pokeTypeTextView;
    TextView pokeExperienceTextView;
    TextView pokeAttackTextView;
    TextView pokeDefenceTextView;
    TextView pokeHpTextView;
    TextView pokeWeightTextView;
    TextView pokeHeightTextView;
    TextView pokeSpeedTextView;
    ImageView pokeImageView;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_poke_info);

        Intent intent = getIntent();
        //getting object with information about pokemon from intent
        Poke poke = (Poke)intent.getSerializableExtra(POKE_OBJECT);
        initData(poke);
    }

    private void initData(Poke poke){
        pokeNameTextView = findViewById(R.id.pokeNameTextView);
        pokeTypeTextView = findViewById(R.id.pokeTypeTextView);
        pokeExperienceTextView = findViewById(R.id.pokeExperienceTextView);
        pokeAttackTextView = findViewById(R.id.pokeAttackTextView);
        pokeDefenceTextView = findViewById(R.id.pokeDefenceTextView);
        pokeHpTextView = findViewById(R.id.pokeHpTextView);
        pokeWeightTextView = findViewById(R.id.pokeWeightTextView);
        pokeHeightTextView = findViewById(R.id.pokeHeightTextView);
        pokeSpeedTextView = findViewById(R.id.pokeSpeedTextView);
        pokeImageView = findViewById(R.id.pokeImageView);
        viewPager = findViewById(R.id.pokeImageViewPager);

        pokeNameTextView.setText(poke.getName().toUpperCase());
        pokeTypeTextView.setText(poke.getTypesString());
        pokeExperienceTextView.setText(String.valueOf(poke.getBaseExperience()));
        pokeAttackTextView.setText(String.valueOf(poke.getAttack()));
        pokeDefenceTextView.setText(String.valueOf(poke.getDefense()));
        pokeHpTextView.setText(String.valueOf(poke.getHp()));
        pokeWeightTextView.setText(String.valueOf(poke.getWeight()));
        pokeHeightTextView.setText(String.valueOf(poke.getHeight()));
        pokeSpeedTextView.setText(String.valueOf(poke.getSpeed()));
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), PokeInfoActivity.this);

        if(poke.getAllImagesUrl().isEmpty()){
            ImageFragment fragment = ImageFragment.newInstance("");
            viewPagerAdapter.addFragment(fragment);
        }
        for(String x : poke.getAllImagesUrl()) {
            ImageFragment fragment = ImageFragment.newInstance(x);
            viewPagerAdapter.addFragment(fragment);
        }
        viewPager.setAdapter(viewPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager, true);

    }



}
