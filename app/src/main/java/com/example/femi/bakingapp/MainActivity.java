package com.example.femi.bakingapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
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
    private String RECIPE_KEY = "recipe_key";
    private int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;



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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
        if(savedInstanceState == null){
            getRecipies(this);
        } else {
            if (isNetworkAvailable()){
                Gson gson = new Gson();
                ArrayList<String> recipies_string = savedInstanceState.getStringArrayList(RECIPE_KEY);
                recipies = new ArrayList<Recipe>();
                for(String recipe: recipies_string){
                    recipies.add(gson.fromJson(recipe, Recipe.class));
                }
                adapter = new RecipeAdapter(this, R.layout.item_recipe ,recipies);
                absListView.setAdapter(adapter);
            } else {
                Snackbar.make(this.findViewById(R.id.activity_main), "Network problem. Refresh by pulling", Toast.LENGTH_SHORT)
                        .show();
            }

        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recipies != null){
            Gson gson = new Gson();
            ArrayList<String> recipies_string = new ArrayList<String>();
            for (Recipe recipe:recipies){
                recipies_string.add(gson.toJson(recipe));
            }
            outState.putStringArrayList(RECIPE_KEY, recipies_string);
        }
    }

    private void getRecipiesFromNetwork(final Activity activity){
        Api_Interface api_interface = Api_Client.recipeRequest().create(Api_Interface.class);
        Call<List<Recipe>> call = api_interface.getRecipe();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipies=response.body();
//                new FetchThumbnails(activity, recipies).execute();
                swipe.setRefreshing(false);
                adapter = new RecipeAdapter(activity, R.layout.item_recipe ,recipies);
                absListView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                swipe.setRefreshing(false);
                Snackbar.make(activity.findViewById(R.id.activity_main), "Network problem. Refresh by pulling", Toast.LENGTH_SHORT)
                        .show();
                Timber.d("error: " + t.toString());
            }
        });
    }
}

