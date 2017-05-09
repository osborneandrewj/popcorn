package com.example.android.popcorn.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Andrew Osborne on 5/4/17.
 *
 */

public class MovieCertInnerWrapper {

    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("release_dates")
    @Expose
    private List<ReleaseInfo> releaseInfos = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public MovieCertInnerWrapper() {
    }

    /**
     *
     * @param iso31661
     * @param releaseInfos
     */
    public MovieCertInnerWrapper(String iso31661, List<ReleaseInfo> releaseInfos) {
        super();
        this.iso31661 = iso31661;
        this.releaseInfos = releaseInfos;
    }

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public List<ReleaseInfo> getReleaseInfos() {
        return releaseInfos;
    }

    public void setReleaseInfos(List<ReleaseInfo> releaseInfos) {
        this.releaseInfos = releaseInfos;
    }

}


