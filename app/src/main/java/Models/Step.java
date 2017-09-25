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
    private String thumbnailURI;


    public Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {

        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.thumbnailURI = null;
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


    public void setThumbnailURI(String thumbnailURI) {
        this.thumbnailURI = thumbnailURI ;
    }

    public String getThumbnailURI() {
        return thumbnailURI;
    }

}
