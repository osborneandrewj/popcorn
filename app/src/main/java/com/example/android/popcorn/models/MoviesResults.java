package com.example.android.popcorn.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Andrew Osborne on 5/1/17.
 *
 */

public class MoviesResults {

    @SerializedName("results")
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

}
