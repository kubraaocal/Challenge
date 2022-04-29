package com.example.challenge.model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatRecycler {



    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("reference_image_id")
    @Expose
    private String imageId;

    private boolean favStatus;

    @SerializedName("image")
    @Expose
    private Image image;

    public Image getImage() {
        return image;
    }

    public class Image{
        @SerializedName("url")
        @Expose
        private String imageUrl;

        @SerializedName("id")
        @Expose
        private String imageId;

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    public CatRecycler(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageId=imageUrl;
    }


    public String getImageId() {
        return imageId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isFavStatus() {
        return favStatus;
    }

    public void setFavStatus(boolean favStatus) {
        this.favStatus = favStatus;
    }
}
