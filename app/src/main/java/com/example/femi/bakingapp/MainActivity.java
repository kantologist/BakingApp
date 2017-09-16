package com.example.femi.bakingapp;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.Toast;

import java.util.List;

import Adapters.RecipeAdapter;
import Models.Recipe;
import Rest.Api_Client;
import Rest.Api_Interface;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private RecipeAdapter adapter;
    @BindView(R.id.recipe_list) AbsListView absListView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipe;
    private List<Recipe> recipies;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        getRecipies(this);
    }

    private void getRecipies(final Activity activity){

        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                getRecipiesFromNetwork(activity);
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRecipiesFromNetwork(activity);
            }
        });
    }


    private void getRecipiesFromNetwork(final Activity activity){
        Api_Interface api_interface = Api_Client.recipeRequest().create(Api_Interface.class);
        Call<List<Recipe>> call = api_interface.getRecipe();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipies=response.body();
                swipe.setRefreshing(false);
                adapter = new RecipeAdapter(activity, R.layout.item_recipe ,recipies);
                absListView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                swipe.setRefreshing(false);
                Toast.makeText(activity, "Network problem. Refresh by pulling", Toast.LENGTH_SHORT)
                        .show();
                Timber.d("error: " + t.toString());
            }
        });
    }
}
