package Models;

import android.graphics.Bitmap;
import android.net.Uri;

import io.realm.RealmObject;

/**
 * Created by femi on 9/6/17.
 */

public class Step {

    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;
    private transient Bitmap imageBitmap;


    public Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {

        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.imageBitmap=null;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }


    public String getVideoURL() {
        return videoURL;
    }


    public void setThumbnailURL(String thumbnailURI) {
        this.thumbnailURL = thumbnailURL ;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
