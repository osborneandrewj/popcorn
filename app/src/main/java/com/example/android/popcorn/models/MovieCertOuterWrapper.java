package com.example.android.popcorn.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Andrew Osborne on 5/4/17.
 *
 */

public class MovieCertOuterWrapper {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<MovieCertInnerWrapper> results = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public MovieCertOuterWrapper() {
    }

    /**
     *
     * @param id
     * @param results
     */
    public MovieCertOuterWrapper(Integer id, List<MovieCertInnerWrapper> results) {
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

    public List<MovieCertInnerWrapper> getResults() {
        return results;
    }

    public void setResults(List<MovieCertInnerWrapper> results) {
        this.results = results;
    }

}