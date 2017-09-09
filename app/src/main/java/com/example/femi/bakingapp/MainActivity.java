package com.example.femi.bakingapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import Adapters.RecipeAdapter;
import Models.Recipe;
import Rest.Api_Client;
import Rest.Api_Interface;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private RecipeAdapter adapter;
    @BindView(R.id.recipe_list) AbsListView absListView;
    private List<Recipe> recipies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        recipies = new ArrayList<>();
        getRecipies(this);
    }

    private void getRecipies(final Activity activity){
        Api_Interface api_interface = Api_Client.recipeRequest().create(Api_Interface.class);
        Call<List<Recipe>> call = api_interface.getRecipe();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipies=response.body();
                adapter = new RecipeAdapter(activity, R.layout.item_recipe ,recipies);
                absListView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }
}
