package com.example.android.popcorn;

import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;

    // TODO: Implement Collapsing Toolbar: http://antonioleiva.com/collapsing-toolbar-layout/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Get intent extras
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            String movieTitle = extras.getString("EXTRA_MOVIE_TITLE");
            String posterUrlString = extras.getString("EXTRA_MOVIE_POSTER");
            String backdropUrlString = extras.getString("EXTRA_MOVIE_BACKDROP");
            String movieOverview = extras.getString("EXTRA_MOVIE_OVERVIEW");
            String releaseYear = extras.getString("EXTRA_MOVIE_RELEASE_YEAR");
            String voteAverage = extras.getString("EXTRA_MOVIE_VOTE_AVERAGE");

            // Setup the toolbar and the title of the activity
            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitle(movieTitle);
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

            // Set the backdrop
            ImageView backdropImage = (ImageView) findViewById(R.id.detail_movie_backdrop);
            Picasso.with(this).load(buildUri(backdropUrlString)).into(backdropImage);

            // Set the poster image
            ImageView posterImage = (ImageView) findViewById(R.id.img_poster);
            Picasso.with(this).load(buildUri(posterUrlString)).into(posterImage);

            // Set the release year
            //TextView releaseYearTextView = (TextView) findViewById(R.id.tv_release_year);
            //releaseYearTextView.setText(releaseYear);

            // Set the vote average
            //TextView voteAverageTextView = (TextView) findViewById(R.id.tv_vote_average);
            //String voteAverageFinalText = voteAverage + "/10";
            //voteAverageTextView.setText(voteAverageFinalText);

            // Set the movie synopsis
            TextView synopsis = (TextView) findViewById(R.id.tv_summary);
            synopsis.setText(movieOverview);



        }
    }

    private void shadeTheToolbar(Palette palette) {
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int primary = getResources().getColor(R.color.colorPrimary);

        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primaryDark));
    }

    private Uri buildUri(String aUrlString) {
        Uri uri = Uri.parse(aUrlString)
                .buildUpon()
                .build();
        return uri;
    }
}
