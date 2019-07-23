package com.example.pokemonclient.activities;

import android.animation.TimeAnimator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokemonclient.models.Poke;
import com.example.pokemonclient.views.PokeAdapter;
import com.example.pokemonclient.views.PokeManager;
import com.example.pokemonclient.views.PokesViewModel;
import com.example.pokemonclient.R;

import java.util.List;
import java.util.Random;

//activity with list of all pokemons
public class MainActivity extends AppCompatActivity {

    private PokesViewModel pokesViewModel;
    private RecyclerView pokeRecyclerView;
    private LinearLayoutManager layoutManager;
    private TextView infoTextView;
    private CheckBox attackCheckBox;
    private CheckBox defenseCheckBox;
    private CheckBox hpCheckBox;
    private PokeManager pokeManager;
    private PokeAdapter pokeAdapter;
    private Button reloadPokesButton;
    private ImageButton showToolsButton;
    private View toolsLayout;
    private ImageView updateImageView;
    //angle for animation
    private Float rotateAngle = 0.0f;
    TimeAnimator animator;

    //keys for storing data in bundle
    private static final String BUNDLE_RECYCLE_LAYOUT = "mainactivity.recycler.layout";
    private static final String BUNDLE_TOOLS_VISIBLE = "mainactivity.tools.visible";
    public static final String PREF_START_ELEMENT_POSITION = "mainactivity.startElementPosition";
    public static final String APP_SHARED_PREF = "appSharedPref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();           //hide action bar
        setContentView(R.layout.activity_main);

        initTools();
        initPokeRecyclerView();
        initPokeManager();
        if (savedInstanceState != null){
            restoreInstanceState(savedInstanceState);
        }
    }

    //save recycler view scroll position and tools visibility
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_TOOLS_VISIBLE, toolsLayout.getVisibility());
        outState.putParcelable(BUNDLE_RECYCLE_LAYOUT, pokeRecyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    //restore recycler view scroll position and tools visibility
    public void restoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            toolsLayout.setVisibility(savedInstanceState.getInt(BUNDLE_TOOLS_VISIBLE));
            Parcelable savedRecyclerViewState = savedInstanceState.getParcelable(BUNDLE_RECYCLE_LAYOUT);
            pokeRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerViewState);
        }
    }


    //init poke manager, that controls all work with data
    private void initPokeManager(){
        //loading listener is a custom listener, that catches states of loading new elements
        PokeManager.OnLoadingListener loadingListener = new PokeManager.OnLoadingListener() {
            @Override
            public void onLoadingStarted() {
                updateImageView.setVisibility(View.VISIBLE);
                if(animator.isStarted()){
                    animator.resume();
                }
                else{
                    animator.start();
                }
            }

            @Override
            public void onLoadingFinished() {
                updateImageView.setVisibility(View.GONE);
                if (animator.isRunning()) {
                    animator.pause();
                }
            }

            @Override
            public void onLoadingFailed(){
                updateImageView.setVisibility(View.VISIBLE);
                if (animator.isRunning()) {
                    animator.pause();
                }
                Toast.makeText(MainActivity.this,
                        getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
            }
        };

        //view model enables to save viewed data when screen orientation changes
        pokesViewModel = ViewModelProviders.of(this).get(PokesViewModel.class);
        pokesViewModel.getAllPokes().observe(this, new Observer<List<Poke>>(){
            @Override
            public void onChanged(@Nullable List<Poke> pokes) {
                //there we catch data that was added to database

                if(pokes != null && !pokes.isEmpty()) {
                    pokeAdapter.setItems(pokes);
                }
                else{
                    pokeManager.loadMorePokes();
                }
            }
        });

        pokeManager = new PokeManager(pokesViewModel, pokeAdapter);
        pokeManager.setOnLoadingListener(loadingListener);
        pokeManager.setStartLoadingPosition(loadStartElementPosition());
    }

    //init all buttons, checkbox and other UI elements
    private void initTools(){
        attackCheckBox = findViewById(R.id.attackCheckBox);
        defenseCheckBox = findViewById(R.id.defenseCheckBox);
        hpCheckBox = findViewById(R.id.hpCheckBox);
        showToolsButton = findViewById(R.id.showToolsButton);
        toolsLayout = findViewById(R.id.toolsLayout);
        //animator animate update image (custom progress bar)
        updateImageView = findViewById(R.id.updateImageView);
        infoTextView = findViewById(R.id.infoTextView);
        animator = new TimeAnimator();
        animator.setTimeListener(new TimeAnimator.TimeListener() {
            @Override
            public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                //when it's time to update image, change angle of rotation
                rotateAngle = rotateAngle - 0.5f * deltaTime;
                updateImageView.setRotation(rotateAngle);
            }
        });

        //check listener, that catches boxes checking, starts list sorting and scrolling to first element
        CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(pokeAdapter != null) {
                    pokeAdapter.sortByAttack = attackCheckBox.isChecked();
                    pokeAdapter.sortByDefense = defenseCheckBox.isChecked();
                    pokeAdapter.sortByHp = hpCheckBox.isChecked();
                    pokeAdapter.sortItems();
                    pokeRecyclerView.post(new Runnable(){
                        @Override
                        public void run(){
                            pokeRecyclerView.scrollToPosition(0);
                        }
                    });

                }
            }
        };


        attackCheckBox.setOnCheckedChangeListener(checkListener);
        defenseCheckBox.setOnCheckedChangeListener(checkListener);
        hpCheckBox.setOnCheckedChangeListener(checkListener);

        reloadPokesButton = findViewById(R.id.reloadPokesButton);
        //click listener enables catch button clicking and do some work for every button
        View.OnClickListener onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                switch (view.getId()){
                    case R.id.reloadPokesButton :
                        reloadPokesFromRandom();
                        break;
                    case R.id.showToolsButton :
                        if(toolsLayout.getVisibility() != View.VISIBLE) {
                            toolsLayout.setVisibility(View.VISIBLE);
                        }
                        else{
                            toolsLayout.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        reloadPokesButton.setOnClickListener(onClickListener);
        showToolsButton.setOnClickListener(onClickListener);
    }

    //init recycler view and all objects, that needed for this view
    private void initPokeRecyclerView(){
        pokeRecyclerView = findViewById(R.id.pokeRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        pokeRecyclerView.setLayoutManager(layoutManager);

        //custom listener, that allow me to catch recycler view's elements clicking
        PokeAdapter.OnPokeClickListener onPokeClickListener = new PokeAdapter.OnPokeClickListener() {
            @Override
            public void onPokeClick(Poke poke) {
                Intent intent = new Intent(MainActivity.this, PokeInfoActivity.class);
                intent.putExtra(PokeInfoActivity.POKE_OBJECT,poke);
                MainActivity.this.startActivity(intent);
            }
        };
        //poke adapter extends rec. view adapter and provide data for showing
        pokeAdapter = new PokeAdapter(MainActivity.this);
        pokeAdapter.setOnPokeClickListener(onPokeClickListener);
        pokeRecyclerView.setAdapter(pokeAdapter);
        //scroll listener used for catch moment, when it's need to load more elements to list
        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                loadOnEndOfList();
            }
        };
        pokeRecyclerView.addOnScrollListener(scrollListener);
    }

    //loading more elements to list if it's end of list
    private void loadOnEndOfList(){
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        if(!PokeManager.isPokesLoading && (visibleItemCount+firstVisibleItem) >= totalItemCount){
            pokeManager.loadMorePokes();
        }
    }

    //saving start element position (on server) of first element in list
    private void saveStartElementPosition(Integer position){
        SharedPreferences sharedPreferences = getSharedPreferences(APP_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_START_ELEMENT_POSITION,position);
        editor.apply();
    }

    //loading start element position (on server) of first element in list
    private Integer loadStartElementPosition(){
        SharedPreferences sharedPreferences = getSharedPreferences(APP_SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PREF_START_ELEMENT_POSITION, 0);
    }

    private void reloadPokesFromRandom(){
        if (pokeAdapter.getItems() != null
                && !pokeAdapter.getItems().isEmpty()) {
            Random random = new Random();
            random.setSeed(System.nanoTime());
            int rand = Math.abs(random.nextInt(pokeAdapter.getItemCount()));
            saveStartElementPosition(rand);
            pokeAdapter.deleteItems();
            pokesViewModel.deleteAll();
            pokeManager.setStartLoadingPosition(rand);
        }
    }
}