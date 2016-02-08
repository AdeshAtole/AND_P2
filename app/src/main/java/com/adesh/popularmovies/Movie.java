package com.adesh.popularmovies;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by adesh on 11/12/15.
 */
public class Movie implements Serializable {
    private String id, title, posterUrl, plot;
    private float rating;
    private Date releaseDate;

    public Movie(String id, String title, String posterUrl, String plot, float rating, Date releaseDate) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.plot = plot;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getPlot() {
        return plot;
    }

    public float getRating() {
        return rating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", plot='" + plot + '\'' +
                ", rating=" + rating +
                ", releaseDate=" + releaseDate +
                '}';
    }
}
