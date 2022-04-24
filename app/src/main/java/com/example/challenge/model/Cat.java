package com.example.challenge.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Cat {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("reference_image_id")
    @Expose
    private String imageId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("origin")
    @Expose
    private String origin;

    @SerializedName("life_span")
    @Expose
    private String lifeSpan;

    @SerializedName("dog_friendly")
    @Expose
    private String dogFriendly;

    @SerializedName("wikipedia_url")
    @Expose
    private String wikipediaUrl;

    @SerializedName("description")
    @Expose
    private String description;


    public String getId() {
        return id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(String lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public String getDogFriendly() {
        return dogFriendly;
    }

    public void setDogFriendly(String dogFriendly) {
        this.dogFriendly = dogFriendly;
    }

    public String getWikipediaUrl() {
        return wikipediaUrl;
    }

    public void setWikipediaUrl(String wikipediaUrl) {
        this.wikipediaUrl = wikipediaUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
