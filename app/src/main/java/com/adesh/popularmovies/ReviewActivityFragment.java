package com.adesh.popularmovies;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewActivityFragment extends Fragment {
    @Bind(R.id.reviewListView)
    ListView reviewListView;
    @Bind(R.id.noReviewsTitleTextView)
    TextView noReviews;
    ReviewAdapter reviewAdapter;

    public ReviewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        ButterKnife.bind(this, view);
        Movie movie = (Movie) getActivity().getIntent().getSerializableExtra("MOVIE");
        getActivity().setTitle(movie.getTitle());
//        Toast.makeText(getActivity(),movie.getTitle(),Toast.LENGTH_SHORT).show();


        reviewAdapter = new ReviewAdapter(getActivity());
        reviewListView.setAdapter(reviewAdapter);

        ReviewFetcher fetcher = new ReviewFetcher();
        fetcher.execute(movie.getId());


        return view;
    }


    class ReviewFetcher extends AsyncTask<String, Void, String> {

        static final String base_url = "http://api.themoviedb.org/3/movie/";

        @Override
        protected void onPostExecute(String s) {
//            super.onPostExecute(s);

            try {

                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    reviewAdapter.add(new Review(array.getJSONObject(i).getString("author"), array.getJSONObject(i).getString("content")));
                }

                if (array.length() == 0) {
                    reviewListView.setVisibility(View.GONE);
                    noReviews.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            Uri uri = Uri.parse(base_url).buildUpon().appendPath(strings[0]).appendPath("reviews").appendQueryParameter("api_key", MainActivity.API_KEY).build();
            String s = null;
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                while ((s = reader.readLine()) != null) {
                    builder.append(s);
                }
                s = builder.toString();


//  Log.i("REVIEW", builder.toString());
//                System.out.println(s);

//                JSONObject
            } catch (IOException e) {
                e.printStackTrace();
            }

            return s;
        }
    }
}
