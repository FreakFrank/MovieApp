package com.example.android.movieapp;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static android.R.attr.button;
import static android.R.attr.logo;
import static android.R.transition.move;
import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION_CODES.M;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {
    public MovieDetailsFragment() {

    }

    ArrayList<String> MovieDetails;
    JSONArray movieReviewsArray;
    JSONArray movieTrailersArray;
    ArrayList<String> movieTrailersKeys;
    String allReviews = "";
    TextView review;
    View rootView;
    ListView trailersListView;

    public void getReviewsAndTrailers(ArrayList<String> MovieDetails) {
        Uri movieTrailersUri = Uri.parse("http://api.themoviedb.org/3/movie/" + MovieDetails.get(5) + "/videos?").buildUpon()
                .appendQueryParameter("api_key", "e9fedb645e711fbaf2d6802fab60f121")
                .build();
        Uri movieReviewsUri = Uri.parse("http://api.themoviedb.org/3/movie/" + MovieDetails.get(5) + "/reviews?").buildUpon()
                .appendQueryParameter("api_key", "e9fedb645e711fbaf2d6802fab60f121")
                .build();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, movieTrailersUri.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            movieTrailersArray = new JSONObject(response.toString()).getJSONArray("results");
                            movieTrailersKeys = new ArrayList<>();
                            ArrayList<String> movieTrailersNames = new ArrayList<>();
                            for (int i = 0; i < movieTrailersArray.length(); i++){
                                movieTrailersKeys.add(movieTrailersArray.getJSONObject(i).getString("key").toString());
                                movieTrailersNames.add(movieTrailersArray.getJSONObject(i).getString("name").toString());
                            }
                            trailersAdapter trailers = new trailersAdapter(getActivity(),movieTrailersNames);
                            trailersListView.setAdapter(trailers);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest
                (Request.Method.GET, movieReviewsUri.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            movieReviewsArray = new JSONObject(response.toString()).getJSONArray("results");
                            allReviews = "";
                            for (int i = 0; i < movieReviewsArray.length();i++){
                                allReviews += movieReviewsArray.getJSONObject(i).getString("author").toUpperCase()+"'s Review :- " + "\n" +
                                        movieReviewsArray.getJSONObject(i).getString("content") + "\n\n";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        review.setText(allReviews);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsObjRequest);
        requestQueue.add(jsObjRequest2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        String MovieInfo = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        MovieDetails = new ArrayList<>(Arrays.asList(MovieInfo.split("&")));
        review = (TextView) rootView.findViewById(R.id.reviewsView);
        trailersListView = (ListView) rootView.findViewById(R.id.trailersListView);
        trailersListView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + movieTrailersKeys.get(position)));
                startActivity(intent);
            }
        });
        getReviewsAndTrailers(MovieDetails);
        ImageView thumb = (ImageView) rootView.findViewById(R.id.movie_thumbnail);
        Picasso.with(getActivity()).load(MovieDetails.get(4)).into(thumb);
        TextView title = (TextView) rootView.findViewById(R.id.original_title);
        title.setText(MovieDetails.get(0));
        TextView overView = (TextView) rootView.findViewById(R.id.over_view);
        overView.setText(MovieDetails.get(1));
        TextView voteAvg = (TextView) rootView.findViewById(R.id.vote_average);
        voteAvg.setText(MovieDetails.get(2));
        TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date);
        releaseDate.setText(MovieDetails.get(3));
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Created by kareemismail on 12/1/16.
     */


}
