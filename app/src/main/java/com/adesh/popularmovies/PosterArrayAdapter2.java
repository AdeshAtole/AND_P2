package com.adesh.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by adesh on 11/12/15.
 */
public class PosterArrayAdapter2 extends BaseAdapter {
    List<Movie> movies;
    Context context;

    public PosterArrayAdapter2(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
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
        Picasso.with(context).load(MainActivity.BASE_URL_IMAGE + getItem(position).getPosterUrl()).error(R.drawable.no_pic).into(v);
        return v;
    }
}
