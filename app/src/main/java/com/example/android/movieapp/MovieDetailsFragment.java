package com.example.android.movieapp;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.R.attr.logo;
import static android.R.attr.width;
import static android.content.ContentValues.TAG;
import static com.example.android.movieapp.MainActivityFragment.MovieInfo;

/**
 * A placeholder fragment containing a simple view.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class MovieDetailsFragment extends Fragment {
    private Uri mUri;
    static final String DETAIL_URI = "URI";

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
    Realm realm = Realm.getDefaultInstance();
    public void getReviewsAndTrailers(ArrayList<String> MovieDetails) {
        final Uri movieTrailersUri = Uri.parse("http://api.themoviedb.org/3/movie/" + MovieDetails.get(5) + "/videos?").buildUpon()
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
                            int i = 0;
                            for (i = 0; i < movieTrailersArray.length(); i++){
                                movieTrailersKeys.add(movieTrailersArray.getJSONObject(i).getString("key").toString());
                                movieTrailersNames.add(movieTrailersArray.getJSONObject(i).getString("name").toString());
                            }
                            Display display = getActivity().getWindowManager().getDefaultDisplay();
                            Point size = new Point();
                            display.getSize(size);
                            int width = size.x;
                            int height = size.y;
                            trailersAdapter trailers = new trailersAdapter(getActivity(),movieTrailersNames);
                            trailersListView.setAdapter(trailers);
                            if(movieTrailersArray.length() == 0){
                                LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams((width-20),(int)((height/14)*1));
                                trailersListView.setLayoutParams(mParam);
                                movieTrailersNames.add("No Trailers for this movie !");
                            }
                            else{
                                LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams((width-20),(int)((height/12)*i));
                                trailersListView.setLayoutParams(mParam);
                            }
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
                            if(movieReviewsArray.length() == 0)
                                allReviews += "No Reviews for this movie !";
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
        Bundle arguments = getArguments();
                if (arguments != null) {
                        mUri = arguments.getParcelable(MovieDetailsFragment.DETAIL_URI);
                    }
        rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        final String MovieInfo = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        if(MovieDetails == null)
            return null;

        Log.d(TAG, "onCreateView: kammelt");
            MovieDetails = new ArrayList<>(Arrays.asList(MovieInfo.split("&")));
        review = (TextView) rootView.findViewById(R.id.reviewsView);
        trailersListView = (ListView) rootView.findViewById(R.id.trailersListView);
        /*trailersListView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/
        trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(movieTrailersKeys.size() == 0)
                    return;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + movieTrailersKeys.get(position)));
                startActivity(intent);
            }
        });
        final Button favourites = (Button) rootView.findViewById(R.id.AddToFavourites);
        final Movie searchQuery = realm.where(Movie.class).equalTo("id",MovieDetails.get(5)).findFirst();
        if(searchQuery != null)
            favourites.setText("Remove from Favourites");
        favourites.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(favourites.getText().equals("Remove from Favourites")){
                    favourites.setText("Add to Favourites");
                    realm.beginTransaction();
                    searchQuery.removeFromRealm();
                    realm.commitTransaction();
                }
                else{
                    favourites.setText("Remove from Favourites");
                    realm.beginTransaction();
                    Movie movie = realm.createObject(Movie.class);
                    movie.setId(MovieDetails.get(5));
                    movie.setAllInfo(MovieInfo);
                    realm.commitTransaction();
                }
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


    /**
     * Created by kareemismail on 12/1/16.
     */


}
