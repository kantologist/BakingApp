package com.example.femi.bakingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
        Timber.plant(new Timber.DebugTree());
    }

    @Override
    public Void loadInBackground() {
        for (Step step: recipe.getSteps()){
            try{
                String filename = (recipe.getName()+step.getShortDescription()).trim();
                File imageFile = new File(Environment.getExternalStorageDirectory(), filename);
                if (imageFile.exists()){
                    Timber.d("used existing image");
                    Bitmap bitmap = getImageFromBitmap(imageFile);
                    step.setImageBitmap(bitmap);
                } else {
                    Bitmap bitmap = getVideoThumbnail(step.getVideoURL());
                    saveImageBitmap(context, bitmap, filename);
                    Bitmap out_bitmap = getImageFromBitmap(imageFile);
                    step.setImageBitmap(out_bitmap);
                }
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

    public Bitmap getImageFromBitmap(File imageFile) throws FileNotFoundException {
        FileInputStream in = new FileInputStream(imageFile);
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        return bitmap;

    }

    public void saveImageBitmap(Context context, Bitmap bitmap, String filename)
    {
        File imageFile = new File(Environment.getExternalStorageDirectory(), filename);
            try {
                FileOutputStream out = new FileOutputStream(imageFile.getAbsolutePath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                out.flush();
                out.close();
//                String path = MediaStore.Images.Media
//                        .insertImage(context.getContentResolver(), bitmap, "Title", null);
//                Timber.d("saved image " + path);
//                return path;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Timber.d("unable to save image");
            } catch (IOException e) {
                e.printStackTrace();
            }
//        return null;
    }

}
