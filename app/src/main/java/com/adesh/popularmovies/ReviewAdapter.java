package com.adesh.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adesh on 9/2/16.
 */
public class ReviewAdapter extends BaseAdapter {
    List<Review> reviews = new ArrayList<>();
    Context context;
    LayoutInflater inflater = null;

    ReviewAdapter(Context context) {
        this.context = context;
        reviews = new ArrayList<Review>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        notifyDataSetChanged();
    }

    public ReviewAdapter(List<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        notifyDataSetChanged();
    }

    public void add(Review review) {
        reviews.add(review);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int i) {
        return reviews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = inflater.inflate(R.layout.review_item_layout, null);
        }

        TextView content = (TextView) v.findViewById(R.id.reviewTextView);
        TextView author = (TextView) v.findViewById(R.id.authorTextView);
        content.setText(reviews.get(i).getContent());
        author.setText("-" + reviews.get(i).getAuthor());

        return v;
    }
}
