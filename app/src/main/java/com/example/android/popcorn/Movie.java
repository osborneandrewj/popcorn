package com.example.android.popcorn;

import android.net.Uri;
import android.util.Log;

import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Andrew Osborne on 3/17/2017.
 *
 */

public class Movie {

    private String mMovieTitle;
    private String mPosterPath;
    private String mBackdropPath;
    private String mMovieSynopsis;
    private String mReleaseDate;
    private long mVoteAverage;

    /**
     * Create a new Movie object
     *
     * @param aTitle The title of the movie.
     * @param aPosterPath The path of the poster image. This is used to display the posters in the
     *                    gridview of the PostersActivity.
     *                    Example: "/45Y1G5FEgttPAwjTYic6czC9xCn.jpg"
     * @param aBackdropPath The path of the backdrop image. Similar to the poster image path above.
     * @param aMovieSynopsis The overview of the movie.
     * @param aReleaseDate The date the movie was released in a YYYY-MM-DD format.
     * @param aVoteAverage The average vote in a #.# format.
     */
    public Movie(String aTitle,
                 String aPosterPath,
                 String aBackdropPath,
                 String aMovieSynopsis,
                 String aReleaseDate,
                 long aVoteAverage) {

        mMovieTitle = aTitle;
        mPosterPath = aPosterPath;
        mBackdropPath = aBackdropPath;
        mMovieSynopsis = aMovieSynopsis;
        mReleaseDate = aReleaseDate;
        mVoteAverage = aVoteAverage;

    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public String getMovieSynopsis() {
        return mMovieSynopsis;
    }

    public String getVoteAverage() {
        return String.valueOf(mVoteAverage);
    }

    /**
     * Return the year of release formatted as "yyyy". Example: "2017".
     *
     * @return The year of release formatted as a String.
     */
    public String getReleaseYear() {
        SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat toFormat = new SimpleDateFormat("yyyy", Locale.US);
        String releaseYear = "";
        try {
            releaseYear = toFormat.format(fromFormat.parse(mReleaseDate));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return releaseYear;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    /**
     * Get a complete URL to the poster image
     *
     * @return The URL pointing to the poster image.
     */
    public Uri getPosterUri() {
        String baseUrl = "https://image.tmdb.org/t/p/w185";

        Uri posterUri = Uri.parse(baseUrl).buildUpon()
                .appendEncodedPath(getPosterPath())
                .build();

        return posterUri;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    /**
     * Get a complete URL to the backdrop image
     *
     * @return The URL pointing to the backdrop image.
     */
    public Uri getBackdropUri() {
        String baseUrl = "https://image.tmdb.org/t/p/w500";

        Uri backdropUri = Uri.parse(baseUrl).buildUpon()
                .appendEncodedPath(getBackdropPath())
                .build();

        return backdropUri;
    }
}