package com.example.android.popcorn.loaders;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import com.example.android.popcorn.Movie;
import com.example.android.popcorn.utilites.MyNetworkUtils;
import com.example.android.popcorn.utilites.MyQueryUriBuilders;
import com.example.android.popcorn.utilites.TMDbApiQuery;

import java.util.ArrayList;

/**
 * Created by Andrew Osborne on 3/17/2017.
 *
 */

public class MovieResultsLoader extends AsyncTaskLoader<ArrayList<Movie>>{

    private Context mContext;
    /** Sort order preference for sorting movies */
    private String mSortOrder;

    public MovieResultsLoader(Context context, String aString) {
        super(context);
        mContext = context;
        mSortOrder = aString;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        // First check for internet connection
        if (MyNetworkUtils.doesNetworkConnectionExist(mContext)) {
            // Internet connection exists
            // Create query URI
            Uri queryUri = MyQueryUriBuilders.createQueryUri(mSortOrder);
            // Search the IMDb for movie data and return an arraylist of Movie objects

             return TMDbApiQuery.fetchMovieData(queryUri);
        } else {
            // No internet. Return null.
            return null;
        }
    }

}
