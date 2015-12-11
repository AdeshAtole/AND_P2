package com.adesh.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    @Bind(R.id.grid)
    GridView layout;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
//        tv.setText("Adesh");

//        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.id.)
//        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.list_item_layout, R.id.textView, new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i"});
//        PosterArrayAdapter adapter = new PosterArrayAdapter(getActivity(), R.layout.list_item_layout, R.id.textView, new Drawable[]{getActivity().getDrawable(R.drawable.no_pic)});

//        layout.setAdapter(adapter);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Uri uri = Uri.parse("http://api.themoviedb.org/3/discover/movie").buildUpon().appendQueryParameter("api_key", MainActivity.API_KEY).appendQueryParameter("sort_by", preferences.getString(getResources().getString(R.string.preference_sort_by_key), getResources().getString(R.string.preference_sort_by_default))).build();
        new FetchMoviesTask().execute(uri.toString());
        layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie m = (Movie) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), com.adesh.popularmovies.MovieDetailsActivity.class);
                intent.putExtra("MOVIE", m);
                startActivity(intent);
            }
        });
        return view;
    }

    class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {
            HttpURLConnection connection;
            ArrayList<Movie> movieList = new ArrayList<>();
            try {

                connection = (HttpURLConnection) new URL(params[0]).openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
//                String response = connection.getResponseMessage();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String s;
                while ((s = reader.readLine()) != null) {
                    builder.append(s);
                }
                String response = builder.toString();
//                Log.v("AST", response);

                JSONObject object = new JSONObject(response);
                JSONArray movies = object.getJSONArray("results");
                for (int i = 0; i < movies.length(); i++) {
                    JSONObject movie = movies.getJSONObject(i);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    movieList.add(new Movie(movie.getString("original_title"), movie.getString("poster_path"), movie.getString("overview"), movie.getLong("vote_average"), format.parse(movie.getString("release_date"))));
                    Log.v("APP", movieList.get(i).toString());
                }

            } catch (IOException | JSONException | ParseException e) {
                e.printStackTrace();
            }

            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
//            layout.setAdapter(new PosterArrayAdapter(getActivity(), R.layout.grid_item_layout, R.id.iv, movies));
            layout.setAdapter(new PosterArrayAdapter2(movies, getActivity()));
//            layout.getAdapter();
        }


    }
}
