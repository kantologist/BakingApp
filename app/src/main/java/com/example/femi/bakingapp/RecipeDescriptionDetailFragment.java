package com.example.femi.bakingapp;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import Models.Recipe;
import Models.Step;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A fragment representing a single RecipeDescription detail screen.
 * This fragment is either contained in a {@link RecipeDescriptionListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDescriptionDetailActivity}
 * on handsets.
 */
public class RecipeDescriptionDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private Step step;
    private SimpleExoPlayer player;
    @BindView(R.id.recipedescription_detail) TextView desc;
    CollapsingToolbarLayout appBarLayout;
    AppBarLayout appBar;
    SimpleExoPlayerView playerView;
    private TrackSelector trackSelection;
    private MediaSource videoSource;


    public RecipeDescriptionDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            String pass_step = getArguments().getString(ARG_ITEM_ID);
            Gson gson = new Gson();
            step = gson.fromJson(pass_step, Step.class);


            Activity activity = this.getActivity();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            appBar = (AppBarLayout) activity.findViewById(R.id.app_bar);
            if (appBarLayout != null) {
                appBarLayout.setTitle(step.getShortDescription());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipedescription_detail, container, false);

        ButterKnife.bind(this,rootView);
        if (step != null) {
            desc.setText(step.getDescription());
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (checkVideo()){
            playerView = new SimpleExoPlayerView(getContext());
            playerView = (SimpleExoPlayerView) view.findViewById(R.id.player);
            playerView.setVisibility(View.VISIBLE);

            playerView.setUseController(true);
            playerView.requestFocus();
            initializePlayer();

            int orientation = getResources().getConfiguration().orientation;

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                playerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                playerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                desc.setVisibility(View.GONE);
                hideSystemUI();
            }
        }
    }


    public void hideSystemUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(checkVideo()){
            initializePlayer();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(checkVideo()){
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();

    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    public void initializePlayer(){
        // player
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelection =
                new DefaultTrackSelector(videoTrackSelectionFactory);




        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelection);

        if(checkVideo()){
            playerView.setPlayer(player);
        }

        DefaultBandwidthMeter bandwidthMeter1 = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "BakingApp"), bandwidthMeter1);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        Uri videoUri = Uri.parse(step.getVideoURL());
        videoSource = new ExtractorMediaSource(videoUri,
                dataSourceFactory, extractorsFactory, null, null);

        player.prepare(videoSource);
    }

    public boolean checkVideo(){
        if(!step.getVideoURL().isEmpty()){
            return true;
        } else {
            return false;
        }
    }

    public void releasePlayer(){
        if(player!=null){
            player.release();
            player=null;
            trackSelection = null;
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
