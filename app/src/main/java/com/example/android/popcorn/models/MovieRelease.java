package com.example.android.popcorn.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Andrew Osborne on 5/4/17.
 *
 */

public class MovieRelease {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<MovieReleaseFeatures> results = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public MovieRelease() {
    }

    /**
     *
     * @param id
     * @param results
     */
    public MovieRelease(Integer id, List<MovieReleaseFeatures> results) {
        super();
        this.id = id;
        this.results = results;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieReleaseFeatures> getResults() {
        return results;
    }

    public void setResults(List<MovieReleaseFeatures> results) {
        this.results = results;
    }

}