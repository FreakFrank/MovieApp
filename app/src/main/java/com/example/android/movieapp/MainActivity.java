package com.example.android.movieapp;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import static com.example.android.movieapp.MainActivityFragment.MovieInfo;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {
    public static boolean mTwoPane;
    private static final String MovieDetails_TAG = "MDTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        if (findViewById(R.id.movie_details_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.movie_details_container, new MovieDetailsFragment(), MovieDetails_TAG)
                            .commit();
                }
            }
        } else {
            mTwoPane = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onItemSelected(Uri contentUri) {
        Log.d("CallBack", "onItemSelected: data5lt el callback");
        if (mTwoPane) {
                        // In two-pane mode, show the detail view in this activity by
                                // adding or replacing the detail fragment using a
                                       // fragment transaction.
                                               Bundle args = new Bundle();
                       args.putParcelable(MovieDetailsFragment.DETAIL_URI, contentUri);

            MovieDetailsFragment fragment = new MovieDetailsFragment();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                fragment = new MovieDetailsFragment();
            }
            fragment.setArguments(args);
            Log.d("Fel else ", "onItemSelected: da5alt el iffffff " + contentUri.toString());

            getFragmentManager().beginTransaction()
                                        .replace(R.id.movie_details_container, fragment, MovieDetails_TAG)
                                        .commit();
                    } else {
            Log.d("Fel else ", "onItemSelected: da5alt el elseeeeeeeeeeeee " + contentUri.toString());
                    Intent detail = new Intent();
                    detail.setClass(MainActivity.this, MovieDetails.class).putExtra(Intent.EXTRA_TEXT, contentUri.toString());
                    startActivity(detail);
                }

    }
//        public void onItemSelected(Uri contentUri) {
//                if (mTwoPane) {
//                        // In two-pane mode, show the detail view in this activity by
//                                // adding or replacing the detail fragment using a
//                                       // fragment transaction.
//                                               Bundle args = new Bundle();
//                       args.putParcelable(MovieDetailsFragment.DETAIL_URI, contentUri);
//
//                     MovieDetailsFragment fragment = new MovieDetailsFragment();
//                       fragment.setArguments(args);
//
//                                getSupportFragmentManager().beginTransaction()
//                                        .replace(R.id.movie_details_container, fragment, MovieDetails_TAG)
//                                        .commit();
//                    } else {
//                        Intent intent = new Intent(this, MovieDetails.class)
//                                        .setData(contentUri);
//                        startActivity(intent);
//                }
//           }
}
