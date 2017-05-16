package com.example.android.popcorn;


import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.popcorn.R;
import com.example.android.popcorn.adapters.FavoritesCursorAdapter;
import com.example.android.popcorn.adapters.FavoritesPosterAdapter;
import com.example.android.popcorn.adapters.PosterAdapter;
import com.example.android.popcorn.data.FavoritesContract;
import com.example.android.popcorn.models.Movie;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        FavoritesPosterAdapter.PosterAdapterOnClickHandler{

    private static final int TWO_POSTERS_WIDE = 2;
    private static final int THREE_POSTERS_WIDE = 3;

    private static final String LOG_TAG = DiscoverFavoritesFragment.class.getSimpleName();
    private FavoritesPosterAdapter mFavoritesPosterAdapter;
    private RecyclerView mRecyclerView;
    private static final int UNIQUE_ID_FOR_LOADER = 1249;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mEmptyStateTextView;






    public DiscoverFavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_posters, container, false);

        //ListView listView = (ListView)view.findViewById(R.id.list);
        //empty
        mFavoritesPosterAdapter = new FavoritesPosterAdapter(getContext(), null, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_activity);
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
        mRecyclerView.setAdapter(mFavoritesPosterAdapter);

        // Hide the empty state TextView
        mEmptyStateTextView = (TextView) view.findViewById(R.id.tv_empty_state);
        hideEmptyState();

        getActivity().getSupportLoaderManager().initLoader(UNIQUE_ID_FOR_LOADER, null, this);

        // Swipe refresh functionality
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the loader if the network exists
                // getMovieData();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "Resuming..." );
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                FavoritesContract.FavoritesEntry._ID,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_POSTER_PATH};

        return new CursorLoader(getActivity(),
                FavoritesContract.FavoritesEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        for (int i = 0; i <= data.getCount(); i++) {
            data.moveToPosition(i);
        }
        mFavoritesPosterAdapter.setMoviePosterData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //mFavoritesCursorAdapter.swapCursor(null);
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

    private void hideEmptyState() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyStateTextView.setVisibility(View.INVISIBLE);
    }

    private void showEmptyState() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyStateTextView.setVisibility(View.VISIBLE);
    }
}
