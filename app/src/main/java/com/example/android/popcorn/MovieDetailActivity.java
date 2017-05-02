package com.example.android.popcorn;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.popcorn.models.Movie;
import com.example.android.popcorn.retrofit.TheMovieDbAPI;
import com.example.android.popcorn.retrofit.TheMovieDbApiClient;
import com.example.android.popcorn.utilites.MyNetworkUtils;
import com.squareup.picasso.Picasso;
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
            int movieId = extras.getInt("EXTRA_MOVIE_ID");

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

            TheMovieDbAPI service =
                    TheMovieDbApiClient.getClient().create(TheMovieDbAPI.class);

            Call<Movie> call = service.getMovieDetails(movieId, BuildConfig.THE_MOVIE_DB_API_KEY);
            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {

                    // Set various text
                    synopsis.setText(response.body().getOverview());
                    collapsingToolbarLayout.setTitle(response.body().getTitle());

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
        } else {
            // Something went wrong! The intent did not have any extras.
            showErrorView();
        }
    }

    private void showErrorView() {
        // TODO: finish error view
    }
}
