package com.adesh.popularmovies;

import android.net.Uri;

/**
 * Created by adesh on 8/2/16.
 */
public class Trailer {
    private final String key, name;

    public Trailer(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public Uri getThumbnailUri() {
        Uri uri = Uri.parse("http://img.youtube.com/vi/" + key + "/mqdefault.jpg");
        return uri;
    }

    public Uri getVideoUri() {
        Uri uri = Uri.parse("https://youtube.com/").buildUpon().appendPath("watch").appendQueryParameter("v", key).build();
        return uri;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
