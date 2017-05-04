package com.example.android.popcorn;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.popcorn.models.Movie;
import com.example.android.popcorn.models.MovieRelease;
import com.example.android.popcorn.models.MovieReleaseFeatures;
import com.example.android.popcorn.models.ReleaseDate;
import com.example.android.popcorn.retrofit.TheMovieDbAPI;
import com.example.android.popcorn.retrofit.TheMovieDbApiClient;
import com.example.android.popcorn.utilites.MyDateAndTimeUtils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    private CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Get intent extras
        // Note: If there are no extras, the error view will be shown
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            final int movieId = extras.getInt("EXTRA_MOVIE_ID");

            // Setup the toolbar and the title of the activity
            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

            final ImageView backdropImageView = (ImageView) findViewById(R.id.detail_movie_backdrop);
            final ImageView posterImage = (ImageView) findViewById(R.id.img_poster);
            final TextView synopsis = (TextView) findViewById(R.id.tv_summary);
            final TextView releaseDate = (TextView) findViewById(R.id.tv_release_date);
            final TextView runtime = (TextView) findViewById(R.id.tv_runtime);
            final TextView certification = (TextView) findViewById(R.id.tv_movie_rating);

            TheMovieDbAPI service =
                    TheMovieDbApiClient.getClient().create(TheMovieDbAPI.class);

            // Get the movie details from TMDB API using retrofit

            Call<Movie> call = service.getMovieDetails(movieId, BuildConfig.THE_MOVIE_DB_API_KEY);

            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {

                    // Set various text
                    synopsis.setText(response.body().getOverview());
                    collapsingToolbarLayout.setTitle(response.body().getTitle());
                    releaseDate.setText(response.body().getReleaseDate());
                    String formattedRuntime = MyDateAndTimeUtils.GetFormattedRuntime(response.body().getRuntime());
                    runtime.setText(formattedRuntime);

                    // Load the backdrop
                    Picasso.with(getApplicationContext())
                            .load(TheMovieDbAPI.BACKDROP_BASE_URL + response.body().getBackdropPath())
                            .noFade()
                            .into(backdropImageView);

                    // Load the poster image
                    Picasso.with(getApplicationContext())
                            .load(TheMovieDbAPI.POSTER_BASE_URL + response.body().getPosterPath())
                            .noFade()
                            .into(posterImage);
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    Log.e(LOG_TAG, "Retrofit failed to get information! Error: " + t);
                    showErrorView();
                }
            });

            // Get the movie certification (content rating, "R", "PG", etc) from the same API

            Call<MovieRelease> featuresCall = service.getMovieRelease(movieId,
                    BuildConfig.THE_MOVIE_DB_API_KEY);
            featuresCall.enqueue(new Callback<MovieRelease>() {
                @Override
                public void onResponse(Call<MovieRelease> call, Response<MovieRelease> response) {
                    Log.v(LOG_TAG, "Trying to get certification..." + response.body().getResults());

                    List<MovieReleaseFeatures> movieReleaseFeatures = response.body().getResults();
                    certification.setText(getCertification(movieReleaseFeatures));
                }

                @Override
                public void onFailure(Call<MovieRelease> call, Throwable t) {
                    Log.e(LOG_TAG, "Retrofit failed to get information! Error: " + t);
                    showErrorView();
                }
            });

        } else {
            // Something went wrong! The intent did not have any extras so no movie ID
            // was available to use
            showErrorView();
        }
    }

    private void showErrorView() {
        // TODO: finish error view
    }

    private String getCertification(List<MovieReleaseFeatures> list) {

        List<ReleaseDate> releaseDate;
        String certification = "";

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIso31661().equals("US")) {
                releaseDate = list.get(i).getReleaseDates();
                certification = releaseDate.get(0).getCertification();
            }
        }
        return certification;

    }
}
