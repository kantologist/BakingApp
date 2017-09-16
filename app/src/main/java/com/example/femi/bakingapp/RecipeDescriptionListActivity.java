package com.example.femi.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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

import java.util.List;

import Adapters.RecipeDescriptionAdapter;
import Models.Ingredient;
import Models.Recipe;
import Models.Step;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * An activity representing a list of RecipeDtails. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDescriptionDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDescriptionListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private static final String RECIPE_KEY="recipe_key";
    private Recipe recipe;
    private List<Ingredient> ingredients;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ingredients) TextView ingredient_text;
    private String ing_list = "";

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



        View recyclerView = findViewById(R.id.recipedescription_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.recipedescription_detail_container) != null) {
            mTwoPane = true;
        }
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
}
