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

    Uri getThumbnailUri() {
        Uri uri = Uri.parse("http://img.youtube.com/vi/" + key + "/mqdefault.jpg");
        return uri;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
