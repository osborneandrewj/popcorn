package com.example.android.popcorn;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popcorn.adapters.ReviewAdapter;
import com.example.android.popcorn.data.FavoritesContract;
import com.example.android.popcorn.models.Movie;
import com.example.android.popcorn.models.MovieCertOuterWrapper;
import com.example.android.popcorn.models.MovieCertInnerWrapper;
import com.example.android.popcorn.models.MovieReviews;
import com.example.android.popcorn.models.MovieReviewsWrapper;
import com.example.android.popcorn.models.MovieVideoWrapper;
import com.example.android.popcorn.models.MovieVideos;
import com.example.android.popcorn.models.ReleaseInfo;
import com.example.android.popcorn.retrofit.TheMovieDbAPI;
import com.example.android.popcorn.retrofit.TheMovieDbApiClient;
import com.example.android.popcorn.utilites.MyDateAndTimeUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    private static final int FAVORITES_LOADER_ID = 1250;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String mYoutubeKey = "";
    private TheMovieDbAPI mService;
    private int mMovieId;
    private ImageView mBackdropImageView;
    private ImageView mPosterImage;
    private TextView mSynopsis;
    private TextView mReleaseDate;
    private TextView mRuntime;
    private TextView mCertification;
    private TextView mTrailerButton;
    private TextView mUserRatingInfo;
    private TextView mUserRatingTotal;
    private boolean mIsFavorite;
    private TextView mFavoriteButton;
    private ImageView mFavoriteStar;
    private String mMovieTitle;
    private String mPosterPath;

    private ReviewAdapter mReviewAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Details
        mBackdropImageView = (ImageView) findViewById(R.id.detail_movie_backdrop);
        mPosterImage = (ImageView) findViewById(R.id.img_poster);
        mSynopsis = (TextView) findViewById(R.id.tv_summary);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mRuntime = (TextView) findViewById(R.id.tv_runtime);
        mCertification = (TextView) findViewById(R.id.tv_movie_rating);
        mTrailerButton = (TextView) findViewById(R.id.tv_trailer_button);
        mUserRatingInfo = (TextView) findViewById(R.id.tv_user_rating_info);
        mUserRatingTotal = (TextView) findViewById(R.id.tv_ratings_total);


        // Reviews
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_reviews);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mReviewAdapter = new ReviewAdapter(this, new ArrayList<MovieReviews>());
        mRecyclerView.setAdapter(mReviewAdapter);

        // Favorites
        mFavoriteButton = (TextView) findViewById(R.id.tv_favorite_label);
        mFavoriteStar = (ImageView) findViewById(R.id.userRatingStar);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            mMovieId = extras.getInt("EXTRA_MOVIE_ID");
            Log.v(LOG_TAG, "movieId: " + mMovieId);

            mService = TheMovieDbApiClient.getClient().create(TheMovieDbAPI.class);

            setMovieDetails();
            setContentRating();
            enableTrailerButtonFunctionality();
            mTrailerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchYoutubeTrailer();
                }
            });
            setUserReviews();
            mFavoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addOrDeleteThisMovieFromFavorites();
                }
            });
            mFavoriteStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addOrDeleteThisMovieFromFavorites();
                }
            });
            getSupportLoaderManager().initLoader(FAVORITES_LOADER_ID, null, this);

        } else {
            // Something went wrong! The intent did not have any extras so no movie ID
            // was available to use
            showErrorView();
        }
    }

    /**
     * Get the movie details from TMDB using retrofit and use this information to populate the
     * details activity.
     */
    private void setMovieDetails() {
        // Get the movie details from TMDB API using retrofit

        Call<Movie> call = mService.getMovieDetails(mMovieId, BuildConfig.THE_MOVIE_DB_API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {

                mSynopsis.setText(response.body().getOverview());
                collapsingToolbarLayout.setTitle(response.body().getTitle());
                mReleaseDate.setText(response.body().getReleaseDate());
                String formattedRuntime = MyDateAndTimeUtils.GetFormattedRuntime(response.body().getRuntime());
                mRuntime.setText(formattedRuntime);
                mUserRatingInfo.setText(String.valueOf(response.body().getVoteAverage()));

                mMovieTitle = response.body().getTitle();
                mPosterPath = response.body().getPosterPath();

                // Load the backdrop image
                Picasso.with(getApplicationContext())
                        .load(TheMovieDbAPI.BACKDROP_BASE_URL + response.body().getBackdropPath())
                        .noFade()
                        .into(mBackdropImageView);

                // Load the poster image
                Picasso.with(getApplicationContext())
                        .load(TheMovieDbAPI.POSTER_BASE_URL + response.body().getPosterPath())
                        .noFade()
                        .into(mPosterImage);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e(LOG_TAG, "Retrofit failed to get information! Error: " + t);
                showErrorView();
            }
        });
    }

    /**
     * Get the movie certification (content rating, "R", "PG", etc) from TMDB and display this to
     * the user.
     */
    private void setContentRating() {

        Call<MovieCertOuterWrapper> featuresCall = mService.getMovieRelease(mMovieId,
                BuildConfig.THE_MOVIE_DB_API_KEY);
        featuresCall.enqueue(new Callback<MovieCertOuterWrapper>() {
            @Override
            public void onResponse(Call<MovieCertOuterWrapper> call, Response<MovieCertOuterWrapper> response) {

                List<MovieCertInnerWrapper> movieReleaseFeatures = response.body().getResults();
                mCertification.setText(getMovieCertification(movieReleaseFeatures));
            }

            @Override
            public void onFailure(Call<MovieCertOuterWrapper> call, Throwable t) {
                Log.e(LOG_TAG, "Retrofit failed to get information! Error: " + t);
                showErrorView();
            }
        });
    }

    /**
     * Get the user reviews for this movie from TMDB and populate the reviews section
     */
    private void setUserReviews() {
        Call<MovieReviewsWrapper> reviewsCall = mService.getMovieReviews(mMovieId,
                BuildConfig.THE_MOVIE_DB_API_KEY);
        reviewsCall.enqueue(new Callback<MovieReviewsWrapper>() {
            @Override
            public void onResponse(Call<MovieReviewsWrapper> call, Response<MovieReviewsWrapper> response) {
                List<MovieReviews> reviews = response.body().getResults();
                mReviewAdapter.setReviewsData(reviews);
                mUserRatingTotal.setText(String.valueOf(response.body().getTotalResults()));
            }

            @Override
            public void onFailure(Call<MovieReviewsWrapper> call, Throwable t) {

            }
        });

    }

    /**
     * Get the movie trailer Youtube key from TMDB and use it to enable the "Play Trailer" button
     * to send the user to the appropriate Youtube video.
     */
    private void enableTrailerButtonFunctionality() {
        // Get the trailer information
        // The goal of this section is to create a Youtube address that points to this movie's
        // trailer
        final String youtubeTrailerAddressString = "https:www.youtube.com/watch?v=";
        Call<MovieVideoWrapper> videosCall = mService.getMovieVideos(mMovieId,
                BuildConfig.THE_MOVIE_DB_API_KEY);
        videosCall.enqueue(new Callback<MovieVideoWrapper>() {
            @Override
            public void onResponse(Call<MovieVideoWrapper> call, Response<MovieVideoWrapper> response) {

                List<MovieVideos> videos = response.body().getResults();
                mYoutubeKey = getTrailerYoutubeKey(videos);
            }

            @Override
            public void onFailure(Call<MovieVideoWrapper> call, Throwable t) {
                Log.e(LOG_TAG, "Retrofit failed to get information! Error: " + t);
                showErrorView();
            }
        });

    }

    private void showErrorView() {
        // TODO: finish error view
    }

    /**
     * Get the movie content rating ("G", "R", "PG-13", etc) for a specific movie.
     * Note: this method only return the US content rating. For additional functionality this
     * should reference the user's location and attempt to get rating information from that.
     *
     * @param list
     * @return the String representation of this movie's content rating
     */
    private String getMovieCertification(List<MovieCertInnerWrapper> list) {

        List<ReleaseInfo> releaseInfo;
        String certification = "";

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIso31661().equals("US")) {
                releaseInfo = list.get(i).getReleaseInfos();
                certification = releaseInfo.get(0).getCertification();
            }
        }
        return certification;
    }

    /**
     * Get the movie Youtube key that we can use to complete the Youtube movie trailer address (i.e.
     * "https://www.youtube.com/watch?v=lEEmORyxpho" where "lEEmORyxpho" is the key).
     *
     * @param list is the list of movie information obtained from TMDB
     * @return the Youtube key which will be used to build a Youtube address
     */
    private String getTrailerYoutubeKey(List<MovieVideos> list) {
        String youtubeKey = "";

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType().equals("Trailer")) {
                youtubeKey = list.get(i).getKey();
                return youtubeKey;
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * Use the Youtube key to build the web address and launch the movie trailer.
     */
    private void launchYoutubeTrailer() {
        // Build the Uri String
        String youtubeAddress = "https:www.youtube.com/watch?v=";
        youtubeAddress = youtubeAddress + mYoutubeKey;

        Intent launchTrailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeAddress));
        startActivity(launchTrailerIntent);
    }

    private void updateFavoriteStarImage() {
        if (mIsFavorite) {
            mFavoriteStar.setImageResource(R.drawable.rating_star_yellow);
        } else if (!mIsFavorite) {
            mFavoriteStar.setImageResource(R.drawable.rating_star_outline);
        }

    }

    private void isThisMovieInFavorites(int inDatabase) {
        if (inDatabase >= 1) {
            mIsFavorite = true;
        } else {
            mIsFavorite = false;
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                FavoritesContract.FavoritesEntry._ID,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID};

        String selection = FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(mMovieId)};

        return new CursorLoader(this,
                FavoritesContract.FavoritesEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
    }

    /**
     * Look in the favorites database to see if this movie exists within it. If so, then this
     * movie is a favorite. Update the favorite star image accordingly.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int count = 0;
        if (data != null) {
            count = data.getCount();
            if (count > 0) {
                data.moveToFirst();
                data.getInt(
                        data.getColumnIndexOrThrow(FavoritesContract.FavoritesEntry._ID));
            }
            isThisMovieInFavorites(count);
            updateFavoriteStarImage();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void addOrDeleteThisMovieFromFavorites() {

        if (!mIsFavorite) {
            mIsFavorite = true;
            ContentValues values = new ContentValues();
            values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, mMovieId);
            values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME, mMovieTitle);
            values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_POSTER_PATH, mPosterPath);
            Uri newUri = getContentResolver().insert(
                    FavoritesContract.FavoritesEntry.CONTENT_URI,
                    values);
            updateFavoriteStarImage();

            Snackbar favoriteAddedSnackbar = Snackbar.make(findViewById(R.id.my_coordinator_layout),
                    R.string.snackbar_favorite_added, Snackbar.LENGTH_SHORT);
            favoriteAddedSnackbar.setAction(R.string.snackbar_undo_string, this);
            favoriteAddedSnackbar.show();

            return;
        }

        if (mIsFavorite) {
            mIsFavorite = false;
            String[] selectionArgs = {String.valueOf(mMovieId)};
            int numberOfDeletedMovies = getContentResolver().delete(
                    FavoritesContract.FavoritesEntry.CONTENT_URI,
                    FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID,
                    selectionArgs);
            updateFavoriteStarImage();

            Snackbar favoriteRemovedSnackbar = Snackbar.make(findViewById(R.id.my_coordinator_layout),
                    R.string.snackbar_favorite_removed, Snackbar.LENGTH_SHORT);
            favoriteRemovedSnackbar.setAction(R.string.snackbar_undo_string, this);
            favoriteRemovedSnackbar.show();
        }
    }

    @Override
    public void onClick(View view) {
        if (!mIsFavorite) {
            mIsFavorite = true;
            ContentValues values = new ContentValues();
            values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, mMovieId);
            values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME, mMovieTitle);
            values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_POSTER_PATH, mPosterPath);
            Uri newUri = getContentResolver().insert(
                    FavoritesContract.FavoritesEntry.CONTENT_URI,
                    values);
            updateFavoriteStarImage();
            return;
        }

        if (mIsFavorite) {
            mIsFavorite = false;
            String[] selectionArgs = {String.valueOf(mMovieId)};
            int numberOfDeletedMovies = getContentResolver().delete(
                    FavoritesContract.FavoritesEntry.CONTENT_URI,
                    FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID,
                    selectionArgs);
            updateFavoriteStarImage();
        }
    }
}
