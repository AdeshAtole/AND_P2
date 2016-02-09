package com.adesh.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adesh on 8/2/16.
 */
public class TrailerAdapter extends RecyclerView.Adapter {

    Context context;
    List<Trailer> trailers;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
//    ArrayList<String> imageUrls;

    public TrailerAdapter(Context context, List<Trailer> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    public TrailerAdapter(Context context) {
        this.context = context;
        this.trailers = new ArrayList<Trailer>();
    }

    public void add(Trailer trailer) {
        trailers.add(trailer);
        notifyDataSetChanged();
    }

    public Trailer getItemAt(int position) {
        return trailers.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item_layout, parent, false);
        TrailerViewHolder holder = new TrailerViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Picasso.with(context).load(trailers.get(position).getThumbnailUri()).placeholder(R.drawable.loading_video).error(R.drawable.loading_video).into(((TrailerViewHolder) holder).imageView);
//        Log.v("JSON", "BIND HOLDER " + position);
    }

    @Override
    public int getItemCount() {
//        Log.v("VER", "called getKItemCount()");
        return trailers.size();
    }


    static class TrailerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.trailerThumbnailImageView);
        }


//        public ImageView getImageView() {
//            return imageView;
//        }
    }
}
