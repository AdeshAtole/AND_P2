package com.adesh.popularmovies;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by adesh on 11/12/15.
 */
public class Movie implements Serializable {
    private String id, title, posterUrl, plot;
    private float rating;
    private float popularity;
    private Date releaseDate;
    private byte[] posterBlob;


    public Movie(String id, String title, String posterUrl, String plot, float rating, float popularity, Date releaseDate) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.plot = plot;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        posterBlob = null;
    }


    public Movie(String id, String title, byte[] posterBlob, String plot, float rating, float popularity, Date releaseDate) {
        this.id = id;
        this.posterBlob = posterBlob;
        this.title = title;
        this.plot = plot;
        this.rating = rating;
        this.popularity = popularity;
        this.releaseDate = releaseDate;
        posterUrl = null;
    }

    public byte[] getPosterBlob() {
        return posterBlob;
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

    public float getPopularity() {
        return popularity;
    }

//    public ContentValues toContentValues(Context context) throws IOException {
//        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
//
//        ContentValues values = new ContentValues();
//        values.put(MovieColumns._ID, this.id);
//        values.put(MovieColumns.RELEASE_DATE, format.format(this.releaseDate));
//        values.put(MovieColumns.RATING, this.rating);
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        Picasso.with(context).load(getPosterUrl()).get().compress(Bitmap.CompressFormat.PNG, 0, os);
//        values.put(MovieColumns.POSTER, os.toByteArray());
//        values.put(MovieColumns.TITLE, this.title);
//
//        return values;
//    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", plot='" + plot + '\'' +
                ", rating=" + rating +
                ", popularity=" + popularity +
                ", releaseDate=" + releaseDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return id.equals(movie.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

