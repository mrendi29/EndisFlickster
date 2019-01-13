package com.example.caushie.endisflickster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import model.Movie;

public class MovieActivity extends AppCompatActivity {

    private static  String MOVIE_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed ";
    // We will create the list of movies (data model) and we will use it to display the movies into the screen.
    List<Movie> movies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

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

                     movies = Movie.fromJsonArray(movieJsonArray);

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
