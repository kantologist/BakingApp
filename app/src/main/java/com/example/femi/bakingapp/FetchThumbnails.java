package com.example.femi.bakingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import Models.Recipe;
import Models.Step;
import timber.log.Timber;

/**
 * Created by user on 16/09/2017.
 */

public class FetchThumbnails extends AsyncTaskLoader<Void>{
    private Recipe recipe;
    private Context context;

    public FetchThumbnails(Context context, Recipe recipe) {
        super(context);
        this.recipe = recipe;
        this.context = context;
    }

    @Override
    public Void loadInBackground() {
        for (Step step: recipe.getSteps()){
            try{
                Bitmap bitmap = getVideoThumbnail(step.getVideoURL());
                String uri = getImageFromBitmap(context, bitmap);
                step.setThumbnailURI(uri);
            } catch (Throwable t){
                Timber.d(t.toString());
            }

        }
        return null;
    }

    // Reference: https://stackoverflow.com/questions/22954894/is-it-possible-to-generate-a-thumbnail-from-a-video-url-in-android
    public static Bitmap getVideoThumbnail(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if(Build.VERSION.SDK_INT >= 14)
            {
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());

            }else {
                mediaMetadataRetriever.setDataSource(videoPath);
            }

            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in get video thumbnail" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }

        return bitmap;
    }

    public String getImageFromBitmap(Context context, Bitmap bitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media
                .insertImage(context.getContentResolver(), bitmap, "Title", null);
        return path;
    }

}
