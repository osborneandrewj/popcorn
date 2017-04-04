package com.example.android.popcorn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.example.android.popcorn.loaders.MovieResultsLoader;
import java.util.ArrayList;

/**
 * Display a scrollable grid of movie posters to the user. It should look something like this:
 *
 *                  *----------------------*----------------------*
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |       Movie          |        Movie         |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  *----------------------*----------------------*
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |       Movie          |        Movie         |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  |                      |                      |
 *                  *----------------------*----------------------*
 */

public class MainActivity extends AppCompatActivity implements PosterAdapter.PosterAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DEFAULT_SORT_ORDER = "0";
    private static final int TWO_POSTERS_WIDE = 2;
    private static final int THREE_POSTERS_WIDE = 3;

    private PosterAdapter mPosterAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyStateTextView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     *  Used to load data from the www.TMDb.com server
     */
    private android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Movie>>
    mLoaderCallbacks = new LoaderManager.LoaderCallbacks<ArrayList<Movie>>() {
        @Override
        public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
            return new MovieResultsLoader(getApplicationContext(), getSortOrder());
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
            if (data != null) {
                // Success. Show the movie posters.
                hideEmptyState();
                mPosterAdapter.setMoviePosterData(data);
                // Stop swipe refresh animation
                mSwipeRefreshLayout.setRefreshing(false);
            } else {
                // Something went wrong. Check network connection
                // Show empty state
                showEmptyState();
                // Stop refresh animation
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posters);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_activity);
        // Get default search settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Hide the empty state TextView
        mEmptyStateTextView = (TextView)findViewById(R.id.tv_empty_state);
        hideEmptyState();

        // To improve performance...
        mRecyclerView.setHasFixedSize(true);

        // Use GridLayoutManger to display the grid of movie posters
        // Note: in landscape mode, there will be three columns, not two
        if (this.getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(this, TWO_POSTERS_WIDE);
        } else {
            mLayoutManager = new GridLayoutManager(this, THREE_POSTERS_WIDE);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Specify the adapter with empty ArrayList
        mPosterAdapter = new PosterAdapter(this, new ArrayList<Movie>(), this);
        mRecyclerView.setAdapter(mPosterAdapter);

        // Initialize the loader
        getSupportLoaderManager().initLoader(1, null, mLoaderCallbacks);

        // Swipe refresh functionality
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the loader
                getSupportLoaderManager().restartLoader(1, null, mLoaderCallbacks);

            }
        });
    }

    private void hideEmptyState() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyStateTextView.setVisibility(View.INVISIBLE);
    }

    private void showEmptyState() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyStateTextView.setVisibility(View.VISIBLE);
    }


    /**
     * Overriding this method ensures that the poster dimensions are refreshed from the
     * PosterAdapter class
     */
    @Override
    protected void onResume() {
        super.onResume();
        mPosterAdapter.notifyDataSetChanged();
    }

    /**
     * This method was orverridden to handle RecyclerView poster (item) clicks.
     *
     * @param view The view contianing the poster item clicked.
     * @param aMovieTitle The movie name for the corresponding poster.
     */
    @Override
    public void onClick(View view, TextView aDetailTextView, String aMovieTitle) {
        // onClick button click simulation animation
        Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.poster_fade_in);
        view.startAnimation(animFadeIn);

        // Detail button animations
        if (aDetailTextView.getVisibility() == View.INVISIBLE) {
            // The user is clicking the poster for the first time. Slide the detail button into
            // view.
            aDetailTextView.setVisibility(View.VISIBLE);
            Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.detail_button_slide_down);
            aDetailTextView.startAnimation(slideIn);
        } else {
            // The user is clicking the poster for the second time. Slide the detail button out
            // of view.
            Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.detail_button_slide_up);
            aDetailTextView.startAnimation(slideOut);
            aDetailTextView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * This method was overridden to handle Detail button clicks on RecyclerView poster items.
     * This will pass all the movie object details into the MovieDetailActivity and launch that
     * activity.
     *
     * @param aDetailTextView The detail button for the corresponding poster.
     * @param aMovieTitle The name of the movie.
     */
    @Override
    public void onDetailButtonClick(TextView aDetailTextView, String aMovieTitle, Uri aPosterUri,
                                    Uri aBackdropUri, String aSynopsis, String aReleaseYear,
                                    String aVoteAverage) {
        // The user has clicked the detail button. Slide the detail button up to hide it.
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.detail_button_slide_up);
        aDetailTextView.startAnimation(slideOut);
        aDetailTextView.setVisibility(View.INVISIBLE);

        // Now open the MovieDetailActivity and pass the current movie information to it
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_MOVIE_TITLE", aMovieTitle);
        extras.putString("EXTRA_MOVIE_POSTER", aPosterUri.toString());
        extras.putString("EXTRA_MOVIE_BACKDROP", aBackdropUri.toString());
        extras.putString("EXTRA_MOVIE_SYNOPSIS", aSynopsis);
        extras.putString("EXTRA_MOVIE_RELEASE_YEAR", aReleaseYear);
        extras.putString("EXTRA_MOVIE_VOTE_AVERAGE", aVoteAverage);
        intent.putExtras(extras);
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent openSettingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(openSettingsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Restart the loader when resuming activity. This is required to reset detail button
     * animations.
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        getSupportLoaderManager().restartLoader(1, null, mLoaderCallbacks);
    }

    /**
     * Get the sort order preference from settings. If no preference exists, the default value of
     * sort by popularity will be used.
     */
    private String getSortOrder() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrderKey = getString(R.string.settings_sort_order_key);
        String sortOrderDefault = getString(R.string.settings_sort_order_default);
        String sortOrder = "";
        if (!TextUtils.isEmpty(sharedPref.getString(sortOrderKey, sortOrderDefault))) {
            sortOrder = sharedPref.getString(sortOrderKey, sortOrderDefault);
        } else {
            sortOrder = DEFAULT_SORT_ORDER;
        }

        Log.v(LOG_TAG, "sortOrder = " + sortOrder);
        return sortOrder;
    }
}
