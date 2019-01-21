package com.example.caushie.endisflickster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.caushie.endisflickster.adapter.MoviesAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import model.Movie;

public class MovieActivity extends AppCompatActivity {

    private static  String MOVIE_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed ";
    // We will create the list of movies (data model) and we will use it to display the movies into the screen.


    List<Movie> movies;
    //Key steps using a Recycler View.
    //Add RecyclerView support library to the gradle build file Done
    //Define a model class to use as the data source -DOne
    //Add a RecyclerView to your activity to display the items -Done
    //Create a custom row layout XML file to visualize the item -Done
    //Create a RecyclerView.Adapter and ViewHolder to render the item -Done
    //Bind the adapter to the data source to populate the RecyclerView -Done

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        //Bind the adapter to the data source to populate the RecyclerView
        RecyclerView rvMovies = findViewById(R.id.rvMovies);

        //We have not declared movies as a new arraylist  so we dont crash (because it was null)
        movies = new ArrayList<>();

        //Create an instance of the movies adapter that we just wrote.
        final MoviesAdapter adapter = new MoviesAdapter(this, movies);
        //Add a layout manager into the recycle view so the recycle view knows how to put the items on the screen.
        rvMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //Set the adaptor on the recycle view.
        rvMovies.setAdapter(adapter);

        AsyncHttpClient client = new AsyncHttpClient();
        //We are making a network request on that url


        /**
         *
         * 1. We are sending a network request to the moviedb server to download the movies in JSON.
         * 2. Array of movie items is pulled from the json and then each item is turned into a Movie Java object.
         *
         */
        client.get(MOVIE_URL, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //We will get a JSON array because that's what it is stored ne vendin qe pame bashke.
                try {

                    //1. get the JSon array from the database.
                    JSONArray movieJsonArray = response.getJSONArray("results");

                    //   movies = Movie.fromJsonArray(movieJsonArray);

                    //Then after all the movie objects are added on the other method
                    //the list of objects is passed here
                    // and the adapter is notified of the changes so they can be displayed.
                    movies.addAll(Movie.fromJsonArray(movieJsonArray));

                    adapter.notifyDataSetChanged();
                    Log.d("success",movies.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }
}
