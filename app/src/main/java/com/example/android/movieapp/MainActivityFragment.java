package com.example.android.movieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    ImageAdapter imageAdapter = new ImageAdapter(getActivity(),new ArrayList<String>());
    GridView movies;
    View rootView;
    ArrayList<String> MovieInfo = new ArrayList<String>();
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
        movies= (GridView) rootView.findViewById(R.id.moviesGridView);
        String sortMoviesBy = "top_rated.desc";
        getMovieInfo getMovies = new getMovieInfo();
        getMovies.execute(sortMoviesBy);
        movies.setAdapter(imageAdapter);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public class getMovieInfo extends AsyncTask<String,Void, ArrayList<String>> {
        public ArrayList<String> getImages(String jsonString) throws JSONException {
            ArrayList<String> imageLinks = new ArrayList<String>();
            JSONObject jObject = new JSONObject(jsonString);
            JSONArray jsonArray = jObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                String allInfo = "";
                allInfo += jsonArray.getJSONObject(i).getString("original_title")+ "&"
                        +jsonArray.getJSONObject(i).getString("overview")+ "&"
                        +jsonArray.getJSONObject(i).getString("vote_average")+"&"
                        +jsonArray.getJSONObject(i).getString("release_date")+"&"
                        +"http://image.tmdb.org/t/p/w500/"+jsonArray.getJSONObject(i).getString("poster_path");
                MovieInfo.add(allInfo);
                imageLinks.add("http://image.tmdb.org/t/p/w500/"+jsonArray.getJSONObject(i).getString("poster_path"));
            }
            return imageLinks;
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            final String LOG_TAG = getMovieInfo.class.getSimpleName();
            Uri movie = Uri.parse("http://api.themoviedb.org/3/movie/top_rated?").buildUpon()
                    .appendQueryParameter("api_key", "e9fedb645e711fbaf2d6802fab60f121")
                    .build();
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
                Log.d(TAG, "doInBackground :"+movieInfo);
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
            imageAdapter = new ImageAdapter(getActivity(),strings);
            /*imageAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GridView images = (GridView) rootView.findViewById(R.id.moviesGridView);
                    String x = (String) images.getItemAtPosition(position);
                    Intent detail = new Intent();
                    detail.setClass(getActivity(), MovieDetails.class).putExtra(Intent.EXTRA_TEXT, x);
                    startActivity(detail);
                }
            });*/
            movies.setAdapter(imageAdapter);
            movies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent detail = new Intent();
                    detail.setClass(getActivity(),MovieDetails.class).putExtra(Intent.EXTRA_TEXT,MovieInfo.get(position));
                    startActivity(detail);
                }
            });
        }
    }
}
