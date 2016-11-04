package com.example.android.movieapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import static android.R.transition.move;
import static android.os.Build.VERSION_CODES.M;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        String MovieInfo = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
            Log.d("Printing the info",MovieInfo);
        ArrayList<String> MovieDetails = new ArrayList<String>(Arrays.asList(MovieInfo.split("&")));
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
}
