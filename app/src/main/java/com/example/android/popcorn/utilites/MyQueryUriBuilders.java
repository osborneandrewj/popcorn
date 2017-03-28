package com.example.android.popcorn.utilites;

import android.net.Uri;
import android.util.Log;

import com.example.android.popcorn.BuildConfig;

/**
 * Created by Andrew Osborne on 3/27/2017.
 *
 */

public class MyQueryUriBuilders {
    private static final int SORT_BY_POPULARITY = 0;
    private static final int SORT_BY_RATING = 1;

    private MyQueryUriBuilders() {
        // Never needs to be created
    }

    public static Uri createQueryUri(String aSortOrder) {

        switch (Integer.valueOf(aSortOrder)) {
            case SORT_BY_POPULARITY:
                //uriBuilder.appendPath("popular");
                return createPopularQuery();
            case SORT_BY_RATING:
                //uriBuilder.appendPath("top_rated");
                return createHighestRatedQuery();
            default:
                //uriBuilder.appendPath("popular");
                return createPopularQuery();
        }
    }

    private static Uri createPopularQuery() {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("api.themoviedb.org");
        uriBuilder.appendPath("3");
        uriBuilder.appendPath("movie");
        uriBuilder.appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY);
        uriBuilder.appendQueryParameter("language", "en-US");
        uriBuilder.appendQueryParameter("page", "1");
        uriBuilder.appendPath("popular");

        return uriBuilder.build();
    }

    private static Uri createHighestRatedQuery() {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("api.themoviedb.org");
        uriBuilder.appendPath("3");
        uriBuilder.appendPath("movie");
        uriBuilder.appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY);
        uriBuilder.appendQueryParameter("language", "en-US");
        uriBuilder.appendQueryParameter("page", "1");
        uriBuilder.appendPath("top_rated");

        return uriBuilder.build();
    }
}
