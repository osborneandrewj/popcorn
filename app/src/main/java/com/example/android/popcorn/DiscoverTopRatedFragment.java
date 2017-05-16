package com.example.android.popcorn;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.android.popcorn.adapters.PosterAdapter;
import com.example.android.popcorn.models.Movie;
import com.example.android.popcorn.models.MoviesResults;
import com.example.android.popcorn.retrofit.TheMovieDbAPI;
import com.example.android.popcorn.retrofit.TheMovieDbApiClient;
import com.example.android.popcorn.utilites.MyNetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverTopRatedFragment extends Fragment implements PosterAdapter.PosterAdapterOnClickHandler{


    private static final String LOG_TAG = DiscoverPopularFragment.class.getSimpleName();

    private static final String DEFAULT_SORT_ORDER = "0";
    private static final String SORT_BY_POPULARITY = "0";
    private static final String SORT_BY_TOP_RATED = "1";

    private static final int TWO_POSTERS_WIDE = 2;
    private static final int THREE_POSTERS_WIDE = 3;

    private PosterAdapter mPosterAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyStateTextView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TheMovieDbAPI mService;


    public DiscoverTopRatedFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_posters, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_activity);
        // Get default search settings
        PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, false);

        // Hide the empty state TextView
        mEmptyStateTextView = (TextView) view.findViewById(R.id.tv_empty_state);

        // To improve performance...
        mRecyclerView.setHasFixedSize(true);

        // Use GridLayoutManger to display the grid of movie posters
        // Note: in landscape mode, there will be three columns, not two
        if (this.getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(getContext(), TWO_POSTERS_WIDE);
        } else {
            mLayoutManager = new GridLayoutManager(getContext(), THREE_POSTERS_WIDE);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Specify the adapter with empty ArrayList
        mPosterAdapter = new PosterAdapter(getContext(), new ArrayList<Movie>(), this);
        mRecyclerView.setAdapter(mPosterAdapter);

        // Swipe refresh functionality
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the loader if the network exists
                getMovieData();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });

        getMovieData();
        return view;
    }

    /**
     * Get the movie information from the online database and display it for the user using
     * RecyclerView.
     *
     * The switch statement is based on the sort order defined by the user in preferences.
     */
    public void getMovieData() {

        // Check if a network connection exists. If no network, show empty state
        if (mService == null) {
            mService = TheMovieDbApiClient.getClient().create(TheMovieDbAPI.class);
        }

        // Check for network
        if (!MyNetworkUtils.doesNetworkConnectionExist(getContext())) {
            showEmptyState();
            return;
        }

        // Now get the movie poster information
        Call<MoviesResults> callTopRated = mService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_KEY);
        callTopRated.enqueue(new Callback<MoviesResults>() {
            @Override
            public void onResponse(Call<MoviesResults> call, Response<MoviesResults> response) {
                List<Movie> movieList = response.body().getResults();
                mPosterAdapter.setMoviePosterData(movieList);
                Log.v(LOG_TAG, "Getting Top Rated Movies" );
                // Success.
                hideEmptyState();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MoviesResults> call, Throwable t) {
                Log.e(LOG_TAG, "Something went wrong here..." + t);
                showEmptyState();
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
     * This method was orverridden to handle RecyclerView poster (item) clicks.
     *
     * @param view        The view containing the poster item clicked.
     * @param aMovieTitle The movie name for the corresponding poster.
     */
    @Override
    public void onClick(View view, TextView aDetailTextView, String aMovieTitle) {
        // onClick button click simulation animation
        Animation animFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.poster_fade_in);
        view.startAnimation(animFadeIn);

        // Detail button animations
        if (aDetailTextView.getVisibility() == View.INVISIBLE) {
            // The user is clicking the poster for the first time. Slide the detail button into
            // view.
            aDetailTextView.setVisibility(View.VISIBLE);
            Animation slideIn = AnimationUtils.loadAnimation(getContext(), R.anim.detail_button_slide_down);
            aDetailTextView.startAnimation(slideIn);
        } else {
            // The user is clicking the poster for the second time. Slide the detail button out
            // of view.
            Animation slideOut = AnimationUtils.loadAnimation(getContext(), R.anim.detail_button_slide_up);
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
     * @param aMovieId The integer ID of the movie that was clicked.
     */
    @Override
    public void onDetailButtonClick(TextView aDetailTextView, int aMovieId) {
        // The user has clicked the detail button. Slide the detail button up to hide it.
        Animation slideOut = AnimationUtils.loadAnimation(getContext(), R.anim.detail_button_slide_up);
        aDetailTextView.startAnimation(slideOut);
        aDetailTextView.setVisibility(View.INVISIBLE);

        // Now open the MovieDetailActivity and pass the current movie information to it
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("EXTRA_MOVIE_ID", aMovieId);
        intent.putExtras(extras);
        startActivity(intent);
    }
}