package com.example.android.popcorn;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    // TODO: Implement Collapsing Toolbar: http://antonioleiva.com/collapsing-toolbar-layout/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Get intent extras
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            String movieTitle = extras.getString("EXTRA_MOVIE_TITLE");
            String posterUrlString = extras.getString("EXTRA_MOVIE_POSTER");
            String backdropUrlString = extras.getString("EXTRA_MOVIE_BACKDROP");
            String movieSynopsis = extras.getString("EXTRA_MOVIE_SYNOPSIS");
            String releaseYear = extras.getString("EXTRA_MOVIE_RELEASE_YEAR");
            String voteAverage = extras.getString("EXTRA_MOVIE_VOTE_AVERAGE");

            // Set the title of the activity
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(movieTitle);
            }

            // Set the backdrop
            ImageView backdropImage = (ImageView) findViewById(R.id.img_backdrop);
            Picasso.with(this).load(buildUri(backdropUrlString)).into(backdropImage);

            // Set the poster image
            ImageView posterImage = (ImageView) findViewById(R.id.img_poster);
            Picasso.with(this).load(buildUri(posterUrlString)).into(posterImage);

            // Set the release year
            TextView releaseYearTextView = (TextView) findViewById(R.id.tv_release_year);
            releaseYearTextView.setText(releaseYear);

            // Set the vote average
            TextView voteAverageTextView = (TextView) findViewById(R.id.tv_vote_average);
            String voteAverageFinalText = voteAverage + "/10";
            voteAverageTextView.setText(voteAverageFinalText);

            // Set the movie synopsis
            TextView synopsis = (TextView) findViewById(R.id.tv_movie_synopsis);
            synopsis.setText(movieSynopsis);
        }
    }

    private Uri buildUri(String aUrlString) {
        Uri uri = Uri.parse(aUrlString)
                .buildUpon()
                .build();
        return uri;
    }
}
