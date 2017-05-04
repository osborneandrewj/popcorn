package com.example.android.popcorn.retrofit;

import com.example.android.popcorn.BuildConfig;
import com.example.android.popcorn.models.Movie;
import com.example.android.popcorn.models.MovieRelease;
import com.example.android.popcorn.models.MovieReleaseFeatures;
import com.example.android.popcorn.models.MoviesResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Andrew Osborne on 5/1/17.
 *
 */

public interface TheMovieDbAPI {

    // Base URLs for setting images in MovieDetailActivity
    String BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780";
    String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

    // Get a list of top-rated movies
    @GET("movie/top_rated")
    Call<MoviesResults> getTopRatedMovies(@Query("api_key") String apiKey);

    // Get a list of current most popular movies
    @GET("movie/popular")
    Call<MoviesResults> getPopularMovies(@Query("api_key") String apiKey);

    // Get movie details
    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(
            @Path("movie_id") int id, @Query("api_key") String apiKey);

    // Get movie content rating
    @GET("movie/{movie_id}/release_dates")
    Call<MovieRelease> getMovieRelease(
            @Path("movie_id") int id, @Query("api_key") String apiKey);
}
