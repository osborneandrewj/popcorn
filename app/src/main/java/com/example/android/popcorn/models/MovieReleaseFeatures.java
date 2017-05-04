package com.example.android.popcorn.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Andrew Osborne on 5/4/17.
 *
 */

public class MovieReleaseFeatures {

    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("release_dates")
    @Expose
    private List<ReleaseDate> releaseDates = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public MovieReleaseFeatures() {
    }

    /**
     *
     * @param iso31661
     * @param releaseDates
     */
    public MovieReleaseFeatures(String iso31661, List<ReleaseDate> releaseDates) {
        super();
        this.iso31661 = iso31661;
        this.releaseDates = releaseDates;
    }

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public List<ReleaseDate> getReleaseDates() {
        return releaseDates;
    }

    public void setReleaseDates(List<ReleaseDate> releaseDates) {
        this.releaseDates = releaseDates;
    }

}


