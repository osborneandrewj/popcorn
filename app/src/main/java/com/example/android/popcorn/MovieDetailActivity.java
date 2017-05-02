package com.example.android.popcorn;

import android.net.Uri;
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

    private CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Get intent extras
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            final String posterUrlString = extras.getString("EXTRA_MOVIE_POSTER");
            String backdropUrlString = extras.getString("EXTRA_MOVIE_BACKDROP");

            int movieId = extras.getInt("EXTRA_MOVIE_ID");

            // Setup the toolbar and the title of the activity
            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

            // Set the backdrop
            final ImageView backdropImage = (ImageView) findViewById(R.id.detail_movie_backdrop);

            // Set the poster image
            ImageView posterImage = (ImageView) findViewById(R.id.img_poster);
            Picasso.with(this).load(buildUri(posterUrlString)).into(posterImage);

            final TextView synopsis = (TextView) findViewById(R.id.tv_summary);

            // Test code for Retrofit
            TheMovieDbAPI service =
                    TheMovieDbApiClient.getClient().create(TheMovieDbAPI.class);

            Call<Movie> call = service.getMovieDetails(movieId, BuildConfig.THE_MOVIE_DB_API_KEY);
            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    Log.v("LOG TAG", "Got this: " + response.body().getBackdropPath());
                    synopsis.setText(response.body().getOverview());
                    collapsingToolbarLayout.setTitle(response.body().getTitle());

                    String backdropUrl = MyNetworkUtils.BACKDROP_BASE_URL + response.body().getBackdropPath();
                    Picasso.with(getApplicationContext())
                            .load(MyNetworkUtils.BACKDROP_BASE_URL + response.body().getBackdropPath())
                            .noFade()
                            .into(backdropImage);


                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {

                }
            });
        }
    }

    private Uri buildUri(String aUrlString) {
        Uri uri = Uri.parse(aUrlString)
                .buildUpon()
                .build();
        return uri;
    }
}
