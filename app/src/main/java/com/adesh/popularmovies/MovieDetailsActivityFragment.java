package com.adesh.popularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {

    @Bind(R.id.titleTextView)
    TextView titleTextView;
    @Bind(R.id.overviewTextView)
    TextView overviewTextView;
    @Bind(R.id.ratingsTextView)
    TextView ratingsTextView;
    @Bind(R.id.posterImageView)
    ImageView posterImageView;
    @Bind(R.id.releaseDateTextView)
    TextView releaseDateTextView;

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);
        Movie movie = (Movie) getActivity().getIntent().getSerializableExtra("MOVIE");
        titleTextView.setText(movie.getTitle());
        overviewTextView.setText(movie.getPlot());
        ratingsTextView.setText("" + movie.getRating());

        SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy");
        releaseDateTextView.setText("Release Date: " + format.format(movie.getReleaseDate()));
        Picasso.with(getActivity()).load(MainActivity.BASE_URL_IMAGE + movie.getPosterUrl()).into(posterImageView);
        return view;
    }
}
