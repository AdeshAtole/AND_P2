package com.adesh.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
    TrailerAdapter adapter = null;

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);
        final Movie movie = (Movie) getActivity().getIntent().getSerializableExtra("MOVIE");
        getActivity().setTitle("Movie Details");
        titleTextView.setText(movie.getTitle());
        overviewTextView.setText(movie.getPlot());
        ratingsTextView.setText("" + movie.getRating());

        SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy");
        releaseDateTextView.setText("Release Date: " + format.format(movie.getReleaseDate()));
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        trailerRecyclerView.setAdapter(new TrailerAdapter(getActivity(), new ArrayList<Trailer>()));


        adapter = new TrailerAdapter(getActivity());
        trailerRecyclerView.setAdapter(adapter);
//        trailerRecyclerView.seton
        trailerRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                adapter.getItemAt(position);
                Trailer t = adapter.getItemAt(position);
                Log.v("APP", t.toString());
                startActivity(new Intent(Intent.ACTION_VIEW, t.getVideoUri()));
            }
        }));
//        trailerRecyclerView.setAdapter();
        Picasso.with(getActivity()).load(MainActivity.BASE_URL_IMAGE + movie.getPosterUrl()).into(posterImageView);
        TrailerFetcher fetcher = new TrailerFetcher();
        fetcher.execute(movie.getId());
        readReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ReviewActivity.class);
                i.putExtra("MOVIE", movie);
                startActivity(i);
//                Toast.makeText(getActivity(), "Clicked...", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
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
                        Log.i("JSON", "Added " + thumbnail.toString());
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
