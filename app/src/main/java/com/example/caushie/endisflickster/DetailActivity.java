package com.example.caushie.endisflickster;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import model.Movie;

public class DetailActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = "AIzaSyDa7BbGaxb8rLBzoIkAhp6HjAJjxO-L7Tg";
    private static final String TRAILER_API_KEY = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    
    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;
    Movie movie;
    YouTubePlayerView youTubePlayerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Pull references
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        ratingBar = findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);

        //Geting intent from parcelable
        movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating(movie.getRating());

        //Make a request call for the youtube client as we did with the previous one.
        AsyncHttpClient client = new AsyncHttpClient();
        RequestHandle requestHandle = client.get(String.format(TRAILER_API_KEY, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    //Parsing
                    JSONArray results = response.getJSONArray("results");
                    //If there is nothing on the array we just return
                    if (results.length() == 0) {
                        return;
                    }
                    //Else we create the movietrailer object and we care only for the first one
                    JSONObject movieTrailer = results.getJSONObject(0);
                    //the nwe get the key which corresponds to the movie  by calling the method
                    String youtubeKey = movieTrailer.getString("key");
                    initializeYoutube(youtubeKey);
                 
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });


    }

    private void initializeYoutube(final String youtubeKey) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                Log.d("youtube", "success");

                if (movie.getRating() > 7) {
                    youTubePlayer.setFullscreen(true);

                }


                youTubePlayer.cueVideo(youtubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("youtube", "fail");
            }
        });
    }
}
