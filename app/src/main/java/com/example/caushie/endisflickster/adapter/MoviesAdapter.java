package com.example.caushie.endisflickster.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.caushie.endisflickster.DetailActivity;
import com.example.caushie.endisflickster.R;

import org.parceler.Parcels;

import java.util.List;

import model.Movie;

//after finishing work assigning the instances of variables we should extend the adapter of the recycler view with the one we created below.
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Smile", "onCreateViewHolder");
        //onCreateViewHolder is responsible for inflating the  xml we created and the result is a view.
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        //returns a new view  holder with that particular view by calling the constructor.
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("Smile", "onBindViewHolder: " + position);

        //take the data that lives in that positions and attach that data to a particular viewholder that the adapter has given to us.
        Movie movie = movies.get(position);
        //Bind the movie data into the viewholder.
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    //First thing define viewholder.
    //Then exetend the RecycleView Viewholder.

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        ProgressBar myProgressBar;
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            myProgressBar = itemView.findViewById(R.id.progressBar);
            container = itemView.findViewById(R.id.rvLayout);


        }

        public void bind(final Movie movie) {
            String imageUrl = movie.getPosterPath();
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackDropPath();
            }
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            //Create the container to pass data through activities.
            //Add click listener on the whole row.
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailActivity.class);

                    i.putExtra("movie", Parcels.wrap(movie));


                    context.startActivity(i);


                }
            });


            //The reason progress bar was not working is because i had two different IDs for the xml layouts.
            myProgressBar.setVisibility(View.VISIBLE);

            Glide.with(context).load(imageUrl).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                    myProgressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                    myProgressBar.setVisibility(View.GONE);
                    return false;
                }
            })
                    .into(ivPoster);

        }
    }

}
