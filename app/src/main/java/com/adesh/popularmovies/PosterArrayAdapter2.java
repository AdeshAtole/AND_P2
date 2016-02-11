package com.adesh.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by adesh on 11/12/15.
 */
public class PosterArrayAdapter2 extends BaseAdapter {
    LinkedHashSet<Movie> movies;
    Context context;

    public PosterArrayAdapter2(LinkedHashSet<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    public void add(Movie movie) {
        if (movies == null) {
            movies = new LinkedHashSet<>();
        }
        movies.add(movie);
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return new ArrayList<Movie>(movies).get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView v = (ImageView) convertView;
        if (v == null) {
            v = new ImageView(context);
            v.setAdjustViewBounds(true);

        }
//180 276
        if (getItem(position).getPosterUrl() != null) {
            Picasso.with(context).load(MainActivity.BASE_URL_IMAGE + getItem(position).getPosterUrl()).error(R.drawable.no_pic).into(v);
        } else {
            byte[] posterBlob = getItem(position).getPosterBlob();
            Bitmap poster = BitmapFactory.decodeByteArray(posterBlob, 0, posterBlob.length);
//            Picasso.with(context).
//            (ImageView)((Activity)context).findViewById(R.))
            v.setImageBitmap(poster);
        }
        return v;
    }
}
