package com.example.android.popcorn.utilites;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.popcorn.BuildConfig;
import com.example.android.popcorn.Movie;
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
    private static final int SORT_BY_POPULARITY = 0;
    private static final int SORT_BY_RATING = 1;

    /**
     * Private constructor as you never need to create a TMDbApiQuery object
     */
    private TMDbApiQuery() {}

    public static ArrayList<Movie> fetchMovieData(String aSortOrder) {

        Log.v(LOG_TAG, "Fetching data! ");

        String response = "";
        try {
            response = makeHttpRequest(createQueryUrl(aSortOrder));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Movie> movieArrayList = null;

        if (!TextUtils.isEmpty(response)) {
            movieArrayList = extractFromJson(response);
        }

        return movieArrayList;
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
     * Create the URL to be used by the HTTP request method
     *
     * @return
     */
    public static Uri createQueryUrl(String aSortOrder) {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("api.themoviedb.org");
        uriBuilder.appendPath("3");
        uriBuilder.appendPath("movie");
        uriBuilder.appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY);
        uriBuilder.appendQueryParameter("language", "en-US");
        uriBuilder.appendQueryParameter("page", "1");

        switch (Integer.valueOf(aSortOrder)) {
            case SORT_BY_POPULARITY:
                uriBuilder.appendPath("popular");
                break;
            case SORT_BY_RATING:
                uriBuilder.appendPath("top_rated");
                break;
            default:
                uriBuilder.appendPath("popular");
                break;
        }


        Log.v(LOG_TAG, "Uri: " + uriBuilder);
        return uriBuilder.build();
    }

    /**
     * Grab the information from the TMDb database and return it in JSON format
     *
     * @param uri The URI used to perform the search.
     * @return The information formatted in JSON.
     */
    private static String makeHttpRequest(Uri uri) throws IOException {
        String jsonResponse = "";
        URL url = null;

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        // Generate the search URL from the URI provided
        try {
            url = new URL(uri.toString());
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

    /**
     * This method parses the JSON for the appropriate data and returns an array of Movie
     * Objects.
     *
     * @param aJsonResponse The JSON data from the server.
     * @return The JSON parsed for information and formatted as an array of Movie objects.
     */
    private static ArrayList<Movie> extractFromJson(String aJsonResponse) {
        // Exit the method early if no results are provided
        if (TextUtils.isEmpty(aJsonResponse)) {
            return null;
        }

        // Create list to which the results will be added
        ArrayList<Movie> movieArrayList = new ArrayList<>();

        // Attempt to parse the JSON from the data
        try {
            JSONObject baseJsonObject = new JSONObject(aJsonResponse);
            JSONArray baseJsonArray = baseJsonObject.getJSONArray("results");

            // For each item in array...
            for (int i=0;i<baseJsonArray.length();i++) {
                JSONObject currentMovie = baseJsonArray.getJSONObject(i);

                String movieTitle = "";
                if (currentMovie.has("title")) {
                    movieTitle = currentMovie.getString("title");
                }

                String posterPath = "";
                if (currentMovie.has("poster_path")) {
                    posterPath = currentMovie.getString("poster_path");
                }

                String backdropPath = "";
                if (currentMovie.has("backdrop_path")) {
                    backdropPath = currentMovie.getString("backdrop_path");
                }

                String synopsis = "";
                if (currentMovie.has("overview")) {
                    synopsis = currentMovie.getString("overview");
                }

                String releaseDate = "";
                if (currentMovie.has("release_date")) {
                    releaseDate = currentMovie.getString("release_date");
                }

                long voteAverage = 0;
                if (currentMovie.has("vote_average")) {
                    voteAverage = currentMovie.getLong("vote_average");
                }

                // Now create a new Movie object
                Movie aMovie = new Movie(
                        movieTitle,
                        posterPath,
                        backdropPath,
                        synopsis,
                        releaseDate,
                        voteAverage);
                // Then add this object to the array
                movieArrayList.add(aMovie);
                Log.v(LOG_TAG, "Movie added: " + movieTitle + " " + posterPath);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieArrayList;
    }
}
