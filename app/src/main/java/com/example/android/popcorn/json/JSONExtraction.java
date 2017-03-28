package com.example.android.popcorn.json;

import android.text.TextUtils;
import com.example.android.popcorn.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Andrew Osborne on 3/27/2017.
 *
 */

public class JSONExtraction {

    private final static String MOVIE_RESULTS = "results";
    private final static String MOVIE_TITLE = "title";
    private final static String MOVIE_POSTER_PATH = "poster_path";
    private final static String MOVIE_BACKDROP_PATH = "backdrop_path";
    private final static String MOVIE_OVERVIEW = "overview";
    private final static String MOVIE_RELEASE_DATE = "release_data";
    private final static String MOVIE_VOTE_AVERAGE = "vote_average";

    private JSONExtraction() {
        // No need to create
    }

    /**
     * This method parses the JSON for the appropriate data and returns an array of Movie
     * Objects.
     *
     * @param data The raw JSON data from the server.
     * @return The JSON parsed for information and formatted as an array of Movie objects.
     */
    public static ArrayList<Movie> extractFromJson(String data) {

        // Exit the method early if no results are provided
        if (TextUtils.isEmpty(data)) {
            return null;
        }

        // Create list to which the results will be added
        ArrayList<Movie> movieArrayList = new ArrayList<>();

        // Attempt to parse the JSON from the data
        try {
            JSONObject baseJsonObject = new JSONObject(data);
            JSONArray baseJsonArray = baseJsonObject.getJSONArray(MOVIE_RESULTS);

            // For each item in array...
            for (int i=0;i<baseJsonArray.length();i++) {
                JSONObject currentMovie = baseJsonArray.getJSONObject(i);

                String movieTitle = "";
                if (currentMovie.has(MOVIE_TITLE)) {
                    movieTitle = currentMovie.getString(MOVIE_TITLE);
                }

                String posterPath = "";
                if (currentMovie.has(MOVIE_POSTER_PATH)) {
                    posterPath = currentMovie.getString(MOVIE_POSTER_PATH);
                }

                String backdropPath = "";
                if (currentMovie.has(MOVIE_BACKDROP_PATH)) {
                    backdropPath = currentMovie.getString(MOVIE_BACKDROP_PATH);
                }

                String synopsis = "";
                if (currentMovie.has(MOVIE_OVERVIEW)) {
                    synopsis = currentMovie.getString(MOVIE_OVERVIEW);
                }

                String releaseDate = "";
                if (currentMovie.has(MOVIE_RELEASE_DATE)) {
                    releaseDate = currentMovie.getString(MOVIE_RELEASE_DATE);
                }

                long voteAverage = 0;
                if (currentMovie.has(MOVIE_VOTE_AVERAGE)) {
                    voteAverage = currentMovie.getLong(MOVIE_VOTE_AVERAGE);
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieArrayList;
    }
}
