package com.example.femi.bakingapp;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import Adapters.RecipeDescriptionAdapter;
import Models.Ingredient;
import Models.Recipe;
import Models.Step;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class RecipeDescriptionListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Void>{


    private boolean mTwoPane;
    private static final String RECIPE_KEY="recipe_key";
    private Recipe recipe;
    private List<Ingredient> ingredients;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ingredients) TextView ingredient_text;
    @BindView(R.id.swipe_refresh_steps) SwipeRefreshLayout swipe_step;
    private String ing_list = "";
    @BindView(R.id.recipedescription_list) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipedescription_list);

        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        String pass_recipe = getIntent().getStringExtra("Recipe");
        Gson gson = new Gson();
        if(savedInstanceState != null){
            String saved_recipe = savedInstanceState.getString(RECIPE_KEY);
            recipe = gson.fromJson(saved_recipe, Recipe.class);
            ingredients = recipe.getIngredients();
        } else {

            recipe = gson.fromJson(pass_recipe, Recipe.class);
            ingredients = recipe.getIngredients();
        }

//        setSupportActionBar(toolbar);
        toolbar.setTitle(recipe.getName());

        for(Ingredient ingredient:ingredients){
            ing_list += "." + String.valueOf(ingredient.getIngredient())
                    +"("+String.valueOf(ingredient.getQuantity())
                    +String.valueOf(ingredient.getMeasure())+")\n";
        }
        ingredient_text.setText(ing_list);
        Timber.d("the new string is: " + ing_list);


        assert recyclerView != null;
//        setupRecyclerView((RecyclerView) recyclerView);


        swipe_step.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // check for initial loading
        if (recipe.getLoaded()){
            setupRecyclerView((RecyclerView) recyclerView);
        } else {
            getImageUri(this);
        }

        if (findViewById(R.id.recipedescription_detail_container) != null) {
            mTwoPane = true;
        }
    }

    private void getImageUri(final RecipeDescriptionListActivity activity) {

        swipe_step.post(new Runnable() {
            @Override
            public void run() {
                swipe_step.setRefreshing(true);
                Timber.e("I have set refreshing");
                getSupportLoaderManager().initLoader(1, null, activity).forceLoad();
            }
        });

//        swipe_step.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Timber.e("I have listened to refresh");
//                getSupportLoaderManager().initLoader(1, null, activity).forceLoad();
//            }
//        });
    }

    @OnClick(R.id.fab_love)
    public void fabClicked(){
        Toast.makeText(this, "Widget is update with ingredient", Toast.LENGTH_SHORT).show();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.ingredients_widget);
        ComponentName widget = new ComponentName(this, IngredientsWidget.class);
        remoteViews.setTextViewText(R.id.appwidget_text, ing_list);
        remoteViews.setTextViewText(R.id.appwidget_header, recipe.getName());
        appWidgetManager.updateAppWidget(widget, remoteViews);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Gson gson = new Gson();
        String pass_recipe = gson.toJson(recipe);
        outState.putString(RECIPE_KEY, pass_recipe);
        Timber.d("I have saved it");
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RecipeDescriptionAdapter(this, recipe.getSteps()));
    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new FetchThumbnails(this, recipe);
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        swipe_step.setRefreshing(false);
        setupRecyclerView((RecyclerView) recyclerView);
        recipe.setLoaded(true);
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {
        swipe_step.setRefreshing(false);
        setupRecyclerView((RecyclerView) recyclerView);
        recipe.setLoaded(true);
    }
}
