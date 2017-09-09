package com.example.femi.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single RecipeDescription detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeDescriptionListActivity}.
 */
public class RecipeDescriptionDetailActivity extends AppCompatActivity {

    @BindView(R.id.app_bar) AppBarLayout appBar;
    @BindView(R.id.detail_toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipedescription_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(RecipeDescriptionDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(RecipeDescriptionDetailFragment.ARG_ITEM_ID));
            RecipeDescriptionDetailFragment fragment = new RecipeDescriptionDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipedescription_detail_container, fragment)
                    .commit();
        }

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            appBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //
            NavUtils.navigateUpTo(this, new Intent(this, RecipeDescriptionListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
