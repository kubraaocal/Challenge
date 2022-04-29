package com.example.challenge.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CatDetail {

    @SerializedName("url")
    @Expose
    private String imageUrl;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("breeds")
    @Expose
    private List<Breeds> breeds;

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Breeds> getBreeds() {
        return breeds;
    }

    public String getId() {
        return id;
    }

    private boolean favoriteStatus;

    public boolean isFavoriteStatus() {
        return favoriteStatus;
    }

    public void setFavoriteStatus(boolean favoriteStatus) {
        this.favoriteStatus = favoriteStatus;
    }

    public class Breeds{
        @SerializedName("id")
        @Expose
        private String id;

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

        public String getName() {
            return name;
        }

        public String getOrigin() {
            return origin;
        }

        public String getLifeSpan() {
            return lifeSpan;
        }

        public String getDogFriendly() {
            return dogFriendly;
        }

        public String getWikipediaUrl() {
            return wikipediaUrl;
        }

        public String getDescription() {
            return description;
        }
    }
}

