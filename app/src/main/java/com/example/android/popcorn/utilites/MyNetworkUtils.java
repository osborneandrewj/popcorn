package com.example.android.popcorn.utilites;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * Created by Andrew Osborne on 3/27/2017.
 *
 */

public class MyNetworkUtils {

    private MyNetworkUtils() {
        // Never needs to be created
    }

    /**
     * Check for internet connection.
     *
     * @param context The context.
     * @return The boolean "true" if internet connection exists.
     */
    public static boolean doesNetworkConnectionExist(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    /**
     * Get the movie poster URI from the poster path
     *
     * @param aPosterPath
     * @return
     */
    public static Uri getUriFromPosterPath(String aPosterPath) {
        String baseUrl = "https://image.tmdb.org/t/p/w185";

        Uri posterUri = Uri.parse(baseUrl).buildUpon()
                .appendEncodedPath(aPosterPath)
                .build();

        return posterUri;
    }
}
