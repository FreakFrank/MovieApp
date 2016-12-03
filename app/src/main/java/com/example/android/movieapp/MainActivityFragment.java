package com.example.android.movieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.content.ContentValues.TAG;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.os.Build.VERSION_CODES.M;

/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment {
    boolean top = true;
    ImageAdapter imageAdapter = new ImageAdapter(getActivity(), new ArrayList<String>());
    GridView movies;
    View rootView;
    ArrayList<String> MovieInfo;
    private final int RESULT_SETTINGS = 0;
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movies = (GridView) rootView.findViewById(R.id.moviesGridView);
        getMovieInfo getMovies = new getMovieInfo();
        SharedPreferences sort = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sorting = sort.getString("sort_list", "top_rated");
        getMovies.execute(sorting);
        movies.setAdapter(imageAdapter);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.action_settings == id) {
            Intent settings = new Intent(getActivity(), SettingsActivity.class);
            startActivityForResult(settings, RESULT_SETTINGS);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SETTINGS:
                changeSorting();
        }
    }

    public void changeSorting() {
        SharedPreferences sort = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sorting = sort.getString("sort_list", "top_rated");
        getMovieInfo movieInfo = new getMovieInfo();
        movieInfo.execute(sorting);
    }

    public class getMovieInfo extends AsyncTask<String, Void, ArrayList<String>> {
        public ArrayList<String> getImages(String jsonString) throws JSONException {
            ArrayList<String> imageLinks = new ArrayList<String>();
            JSONObject jObject = new JSONObject(jsonString);
            JSONArray jsonArray = jObject.getJSONArray("results");
            MovieInfo = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String allInfo = "";
                allInfo += jsonArray.getJSONObject(i).getString("original_title") + "&"
                        + jsonArray.getJSONObject(i).getString("overview") + "&"
                        + jsonArray.getJSONObject(i).getString("vote_average") + "&"
                        + jsonArray.getJSONObject(i).getString("release_date") + "&"
                        + "http://image.tmdb.org/t/p/w500/" + jsonArray.getJSONObject(i).getString("poster_path") + "&"
                        + jsonArray.getJSONObject(i).getString("id");
                MovieInfo.add(allInfo);
                imageLinks.add("http://image.tmdb.org/t/p/w500/" + jsonArray.getJSONObject(i).getString("poster_path"));
            }
            return imageLinks;
        }
        public ArrayList<String> getMovieInfoFromDB(){
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Movie> favouriteMovies = realm.where(Movie.class).findAll();
            MovieInfo = new ArrayList<String>();
            ArrayList<String>imageLinks = new ArrayList<>();
            for(Movie movie:favouriteMovies){
                Movie fav = movie;
                MovieInfo.add(fav.getAllInfo());
                imageLinks.add(fav.getAllInfo().split("&")[4]);
            }
            return imageLinks;
        }
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            final String LOG_TAG = getMovieInfo.class.getSimpleName();
            String sort = params[0];
            Uri movie = null;
            if (sort.equals("top_rated")) {
                movie = Uri.parse("http://api.themoviedb.org/3/movie/top_rated?").buildUpon()
                        .appendQueryParameter("api_key", "e9fedb645e711fbaf2d6802fab60f121")
                        .build();
            } else  {
                if(sort.equals("most_pop")) {
                    movie = Uri.parse("http://api.themoviedb.org/3/movie/popular?").buildUpon()
                            .appendQueryParameter("api_key", "e9fedb645e711fbaf2d6802fab60f121")
                            .build();
                }
                else{
                    return getMovieInfoFromDB();
                }
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieInfo = null;
            try {
                URL url = new URL(movie.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null)
                    return null;
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer == null)
                    return null;
                movieInfo = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return getImages(movieInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            imageAdapter = new ImageAdapter(getActivity(), strings);
            movies.setAdapter(imageAdapter);
            movies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent detail = new Intent();
                    detail.setClass(getActivity(), MovieDetails.class).putExtra(Intent.EXTRA_TEXT, MovieInfo.get(position));
                    startActivity(detail);
                }
            });
        }
    }
}
