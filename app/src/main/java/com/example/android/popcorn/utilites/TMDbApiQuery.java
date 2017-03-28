package com.example.android.popcorn.utilites;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.popcorn.BuildConfig;
import com.example.android.popcorn.Movie;
import com.example.android.popcorn.json.JSONExtraction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Andrew Osborne on 3/17/2017.
 *
 * Note: API keys may be obtained here:
 * https://www.themoviedb.org/documentation/api
 *
 */

public class TMDbApiQuery {

    private static final String LOG_TAG = TMDbApiQuery.class.getSimpleName();

    /**
     * Private constructor as you never need to create a TMDbApiQuery object
     */
    private TMDbApiQuery() {}

    public static ArrayList<Movie> fetchMovieData(Uri aUri) {

        Log.v(LOG_TAG, "Fetching data! ");

        String response = "";
        try {
            response = makeHttpRequest(aUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Movie> movieArrayList = null;

        if (!TextUtils.isEmpty(response)) {
            movieArrayList = JSONExtraction.extractFromJson(response);
        }

        return movieArrayList;
    }

    /**
     * Grab the information from the TMDb database and return it in JSON format
     *
     * @param aUri The URI used to perform the search.
     * @return The information formatted in JSON.
     */
    private static String makeHttpRequest(Uri aUri) throws IOException {
        String jsonResponse = "";
        URL url = null;

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        // Generate the search URL from the URI provided
        try {
            url = new URL(aUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // If the URL formation was successful, run a search of the API
        if (url != null) {
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.connect();

                if (httpURLConnection.getResponseCode() == 200) {
                    // Connection successful. Get the data
                    inputStream = httpURLConnection.getInputStream();
                    // Grab JSON info from data
                    jsonResponse = readFromStream(inputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Shut 'er down
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return jsonResponse;
    }

    /**
     * This gets called by the makeHttpRequest method when getting data from the server. This
     * method translates the raw data into a String variable.
     *
     * @param inputStream The raw data from the server that needs to be converted into a
     *                    String
     * @return The data in a String format
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
                    Charset.forName("UTF-8")));
            String line = reader.readLine();

            while (line != null) {
                // Grab all data until there is no other line
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
