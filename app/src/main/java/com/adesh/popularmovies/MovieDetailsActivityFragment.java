package com.adesh.popularmovies;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.adesh.popularmovies.db.MovieColumns;
import com.adesh.popularmovies.db.MoviesProvider;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    @Bind(R.id.trailerRecyclerView)
    RecyclerView trailerRecyclerView;
    @Bind(R.id.readReviewButton)
    Button readReviewsButton;
    @Bind(R.id.trailersTitleTextView)
    TextView trailersTitle;
    @Bind(R.id.favoriteButton)
    ToggleButton favoriteButton;
    //    @Bind(R.id.favoriteButtonTextView)
//    TextView favoriteButtonTextView;
    @Bind(R.id.originalScrollView)
    ScrollView originalView;
    @Bind(R.id.noSelectionView)
    RelativeLayout noSelectionView;
    @Bind(R.id.shareTrailerButton)
    Button shareTrailer;

    TrailerAdapter adapter = null;

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);
        Movie movie = (Movie) getActivity().getIntent().getSerializableExtra("MOVIE");

        if (movie == null) {
            try {

                movie = (Movie) getArguments().getSerializable("MOVIE");
            } catch (NullPointerException e) {

            }
        }

        if (movie == null) {
//            Toast.makeText(getActivity(), "Select a movie", Toast.LENGTH_LONG).show();
            originalView.setVisibility(View.GONE);
            noSelectionView.setVisibility(View.VISIBLE);

        } else {
            originalView.setVisibility(View.VISIBLE);
            noSelectionView.setVisibility(View.GONE);
            if (!MainActivity.mTwoPane)
                getActivity().setTitle("Movie Details");
            titleTextView.setText(movie.getTitle());
            overviewTextView.setText(movie.getPlot());
            ratingsTextView.setText(String.valueOf(movie.getRating()));

            SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy");
            releaseDateTextView.setText("Release Date: " + format.format(movie.getReleaseDate()));


            if (!MainActivityFragment.offline) {
                trailerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                adapter = new TrailerAdapter(getActivity());
                trailerRecyclerView.setAdapter(adapter);
                trailerRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Trailer t = adapter.getItemAt(position);
                        Log.v("APP", t.toString());
                        startActivity(new Intent(Intent.ACTION_VIEW, t.getVideoUri()));
                    }
                }));

                Picasso.with(getActivity()).load(MainActivity.BASE_URL_IMAGE + movie.getPosterUrl()).into(posterImageView);
                TrailerFetcher fetcher = new TrailerFetcher();
                fetcher.execute(movie.getId());
                final Movie finalMovie1 = movie;
                readReviewsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!MainActivity.mTwoPane) {
                            Intent i = new Intent(getActivity(), ReviewActivity.class);
                            i.putExtra("MOVIE", finalMovie1);
                            startActivity(i);
                        } else {
                            Bundle args = new Bundle();
                            args.putSerializable("MOVIE", finalMovie1);
                            ReviewActivityFragment fragment = new ReviewActivityFragment();
                            fragment.setArguments(args);
                            getFragmentManager().beginTransaction().replace(R.id.movie_details_container, fragment).addToBackStack("").commit();

                        }
                    }
                });
            } else {
                readReviewsButton.setVisibility(View.GONE);
                trailerRecyclerView.setVisibility(View.GONE);
                shareTrailer.setVisibility(View.GONE);
                trailersTitle.setVisibility(View.GONE);
                byte[] posterBlob = movie.getPosterBlob();
                posterImageView.setImageBitmap(BitmapFactory.decodeByteArray(posterBlob, 0, posterBlob.length));
                favoriteButton.setVisibility(View.GONE);
//                favoriteButtonTextView.setVisibility(View.GONE);
            }


            updateFavoriteStatus(movie);
            final Movie finalMovie = movie;
            favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Toast.makeText(getActivity(), String.valueOf(b), Toast.LENGTH_SHORT).show();
                    if (b) {
                        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
                        ContentValues values = new ContentValues();
                        values.put(MovieColumns._ID, finalMovie.getId());
                        values.put(MovieColumns.RELEASE_DATE, format.format(finalMovie.getReleaseDate()));
                        values.put(MovieColumns.RATING, finalMovie.getRating());
                        values.put(MovieColumns.POPULARITY, finalMovie.getPopularity());
                        values.put(MovieColumns.PLOT, finalMovie.getPlot());
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
//                        Picasso.with(getActivity()).load(movie.getPosterUrl()).get().compress(Bitmap.CompressFormat.PNG, 0, os);
                        Bitmap bitmap = ((BitmapDrawable) posterImageView.getDrawable()).getBitmap();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 0, os);
                        values.put(MovieColumns.POSTER, os.toByteArray());
                        values.put(MovieColumns.TITLE, finalMovie.getTitle());

                        Uri uri = getActivity().getContentResolver().insert(MoviesProvider.Movies.MOVIES, values);
                        Log.v("CP", uri.toString());
                        Toast.makeText(getActivity(), "Added to favorites", Toast.LENGTH_SHORT).show();
                        updateFavoriteStatus(finalMovie);
                    } else {
//                    Toast.makeText(getActivity(), "Pending", Toast.LENGTH_SHORT).show();
                        int st = getActivity().getContentResolver().delete(MoviesProvider.Movies.withId(Long.parseLong(finalMovie.getId())), null, null);
                        Log.v("CP", String.valueOf(st));
                        updateFavoriteStatus(finalMovie);
                        Toast.makeText(getActivity(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        final Movie finalMovie2 = movie;
        shareTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    Trailer trailer = adapter.getItemAt(0);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_TEXT, "Hey, watch this " + finalMovie2.getTitle() + " trailer. " + trailer.getVideoUri().toString());
                    startActivity(Intent.createChooser(i, "Share"));
                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(getActivity(), "No trailer to share", Toast.LENGTH_SHORT).show();

                }
            }
        });
        return view;

    }

    private void updateFavoriteStatus(Movie movie) {
        Cursor cursor = getActivity().getContentResolver().query(MoviesProvider.Movies.withId(Long.parseLong(movie.getId())), null, null, null, null);
        if (cursor.getCount() == 0) {
            favoriteButton.setChecked(false);
//            favoriteButtonTextView.setText("Add to favorites");
        } else {
            favoriteButton.setChecked(true);
//            favoriteButtonTextView.setText("Unfavorite");
        }
    }

    class TrailerFetcher extends AsyncTask<String, Void, String> {
        static final String base_url = "http://api.themoviedb.org/3/movie/";


        @Override
        protected void onPostExecute(String s) {
//            super.onPostExecute(s);

//            ArrayList<Trailer> trailerArrayList = new ArrayList<>();

            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = (JSONArray) object.get("results");
                boolean trailerPresent = false;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject thumbnail = (JSONObject) array.get(i);
                    if (thumbnail.getString("type").equals("Trailer") && thumbnail.getString("site").equals("YouTube")) {
//                        trailerArrayList.add(new Trailer(thumbnail.getString("key"), thumbnail.getString("name")));
                        adapter.add(new Trailer(thumbnail.getString("key"), thumbnail.getString("name")));
                        trailerPresent = true;
//                        Log.i("JSON", "Added " + thumbnail.toString());
                    }
                }

                if (!trailerPresent) {
                    trailersTitle.setVisibility(View.GONE);
                    trailerRecyclerView.setVisibility(View.GONE);
                }

//                Toast.makeText(getActivity())
//                Log.v("JSON", array.toString());
//                Log.v("JSON", String.valueOf(trailerArrayList.size()));

//                TrailerAdapter adapter = new TrailerAdapter(getActivity(), trailerArrayList);

//                trailerRecyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            Uri uri = Uri.withAppendedPath(Uri.parse(base_url), params[0]).buildUpon().appendPath("videos").appendQueryParameter("api_key", MainActivity.API_KEY).build();
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String s;
                while ((s = reader.readLine()) != null) {
                    builder.append(s);
                }
                response = builder.toString();
                Log.v("APP1", response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }
}
