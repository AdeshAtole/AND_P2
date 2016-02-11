package com.adesh.popularmovies;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.adesh.popularmovies.db.MovieColumns;
import com.adesh.popularmovies.db.MoviesProvider;

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
import java.util.Date;
import java.util.LinkedHashSet;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static boolean offline = false;

    //    boolean mTwoPane;
    @Bind(R.id.grid)
    GridView layout;

    public MainActivityFragment() {
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

//        if (getActivity().findViewById(R.id.movie_details_container) != null) {
//            mTwoPane = true;
//            getFragmentManager().beginTransaction().add(R.id.movie_details_container, new MovieDetailsActivityFragment()).commit();
//        } else {
//            Toast.makeText(getActivity(), "here frag", Toast.LENGTH_LONG).show();
//            mTwoPane = false;
//        }
//        tv.setText("Adesh");

//        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.id.)
//        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.list_item_layout, R.id.textView, new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i"});
//        PosterArrayAdapter adapter = new PosterArrayAdapter(getActivity(), R.layout.list_item_layout, R.id.textView, new Drawable[]{getActivity().getDrawable(R.drawable.no_pic)});

//        layout.setAdapter(adapter);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Uri uri = Uri.parse("http://api.themoviedb.org/3/discover/movie").buildUpon().appendQueryParameter("api_key", MainActivity.API_KEY).appendQueryParameter("sort_by", preferences.getString(getResources().getString(R.string.preference_sort_by_key), getResources().getString(R.string.preference_sort_by_default))).build();
        offline = !isNetworkAvailable();

        if (!offline) {
            new FetchMoviesTask().execute(uri.toString());
        } else {
            Toast.makeText(getActivity(), "Offline Mode. Some features may not work.", Toast.LENGTH_LONG).show();
            Cursor c = getActivity().getContentResolver().query(MoviesProvider.Movies.MOVIES, new String[]{MovieColumns._ID, MovieColumns.TITLE, MovieColumns.PLOT, MovieColumns.POSTER, MovieColumns.RATING, MovieColumns.RELEASE_DATE, MovieColumns.POPULARITY}, null, null, null);
            assert c != null;

            LinkedHashSet<Movie> movies = new LinkedHashSet<>();
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String id = c.getString(0);
                String title = c.getString(1);
                String plot = c.getString(2);
                byte[] poster = c.getBlob(3);
                float rating = c.getFloat(4);
                SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
                Date releaseDate = null;
                try {
                    releaseDate = format.parse(c.getString(5));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float popularity = c.getFloat(6);

                movies.add(new Movie(id, title, poster, plot, rating, popularity, releaseDate));

            }
            c.close();
            PosterArrayAdapter2 adapter2 = new PosterArrayAdapter2(movies, getActivity());
            layout.setAdapter(adapter2);
        }

        layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie m = (Movie) parent.getAdapter().getItem(position);
                if (!MainActivity.mTwoPane) {
                    Intent intent = new Intent(getActivity(), com.adesh.popularmovies.MovieDetailsActivity.class);
                    intent.putExtra("MOVIE", m);
                    startActivity(intent);
                } else {
                    Bundle args = new Bundle();
                    args.putSerializable("MOVIE", m);
                    MovieDetailsActivityFragment fragment = new MovieDetailsActivityFragment();
                    fragment.setArguments(args);
                    FragmentManager fm = getFragmentManager();
                    if (fm.getBackStackEntryCount() == 1) {
                        fm.popBackStack();
                    }
                    fm.beginTransaction().replace(R.id.movie_details_container, fragment).commit();
                }
            }
        });


//        ContentValues values = new ContentValues();
//        values.put(MovieColumns._ID, 666);
//        values.put(MovieColumns.TITLE, "Iron Man");
//        values.put(MovieColumns.PLOT, "SRK");
//        values.put(MovieColumns.POSTER, "poster_dummy");
//        values.put(MovieColumns.RATING, "6.2");
//        values.put(MovieColumns.RELEASE_DATE, "27/12/1994");
//        Uri temp = getActivity().getContentResolver().insert(MoviesProvider.Movies.MOVIES, values);
//        Log.i("URI", temp.toString());

//        Log.v("NETWORK", String.valueOf(isNetworkAvailable()));

        Cursor cursor = getActivity().getContentResolver().query(MoviesProvider.Movies.MOVIES, null, null, null, null);
        assert cursor != null;
        cursor.getCount();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.i("CURSOR", cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return view;
    }

    class FetchMoviesTask extends AsyncTask<String, Void, LinkedHashSet<Movie>> {

        @Override
        protected LinkedHashSet<Movie> doInBackground(String... params) {
            HttpURLConnection connection;
            LinkedHashSet<Movie> movieList = new LinkedHashSet<>();
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

//                Log.v("APP1", response);

                JSONObject object = new JSONObject(response);
                JSONArray movies = object.getJSONArray("results");
                for (int i = 0; i < movies.length(); i++) {
                    JSONObject movie = movies.getJSONObject(i);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    movieList.add(new Movie(movie.getString("id"), movie.getString("original_title"), movie.getString("poster_path"), movie.getString("overview"), movie.getLong("vote_average"), movie.getLong("popularity"), format.parse(movie.getString("release_date"))));
//                    Log.v("APP", movieList.get(i).toString());
                }

            } catch (IOException | JSONException | ParseException e) {
                e.printStackTrace();
            }

            return movieList;
        }

        @Override
        protected void onPostExecute(LinkedHashSet<Movie> movies) {
            super.onPostExecute(movies);
//            layout.setAdapter(new PosterArrayAdapter(getActivity(), R.layout.grid_item_layout, R.id.iv, movies));
            layout.setAdapter(new PosterArrayAdapter2(movies, getActivity()));
//            layout.getAdapter();
        }


    }
}
