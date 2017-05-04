package com.example.android.popcorn.models;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Andrew Osborne on 3/17/2017.
 *
 */

public class Movie {

    @SerializedName("title")
    private String title;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("id")
    private int id;
    @SerializedName("runtime")
    private int runtime;

    /**
     * Create a new Movie object
     *
     * @param aTitle The title of the movie.
     * @param aPosterPath The path of the poster image. This is used to display the posters in the
     *                    gridview of the MainActivity.
     *                    Example: "/45Y1G5FEgttPAwjTYic6czC9xCn.jpg"
     * @param aBackdropPath The path of the backdrop image. Similar to the poster image path above.
     * @param aMovieOverview The overview of the movie.
     * @param aReleaseDate The date the movie was released in a YYYY-MM-DD format.
     * @param aVoteAverage The average vote in a #.# format.
     * @param aId The integer ID of the current movie (ex. 334509). This is used to get further
     *            details
     */
    public Movie(String aTitle,
                 String aPosterPath,
                 String aBackdropPath,
                 String aMovieOverview,
                 String aReleaseDate,
                 double aVoteAverage,
                 int aId,
                 int aRuntime) {

        title = aTitle;
        posterPath = aPosterPath;
        backdropPath = aBackdropPath;
        overview = aMovieOverview;
        releaseDate = aReleaseDate;
        voteAverage = aVoteAverage;
        id = aId;
        runtime = aRuntime;

    }


    /**
     * Return the year of release formatted as "yyyy". Example: "2017".
     *
     * @return The year of release formatted as a String.
     */
    public String getReleaseDate() {
        SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat toFormat = new SimpleDateFormat("yyyy", Locale.US);
        String releaseYear = "";
        try {
            releaseYear = toFormat.format(fromFormat.parse(releaseDate));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return releaseYear;
    }

    public String getPosterPath() {
        return posterPath;
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
        return backdropPath;
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

    /**
     * From here down are getter and setter methods that were automatically generated
     */

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(long voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
}
