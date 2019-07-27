package com.mark.moviesapp.repository;

import com.mark.moviesapp.model.MoviesListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesService {

    // https://api.themoviedb.org/3/search/movies?query=android
    @GET("search/movies")
    Call<MoviesListResponse> searchMoviesByTitle(@Query("api_key") String apiKey, @Query("query") String title);

    // https://api.themoviedb.org/3/discover/movie?year=1993
    @GET("discover/movie")
    Call<MoviesListResponse> discoverMovies(@Query("api_key") String apiKey, @Query("year") int year);

    @GET("movie/top_rated")
    Call<MoviesListResponse> getTopRated(@Query("api_key") String apiKey);

    @GET("discover/movie?sort_by=release_date.desc&")
    Call<MoviesListResponse> discoverLatestMovies(@Query("api_key") String apiKey,
                                                  @Query("primary_release_date.lte") String todayDate,
                                                  @Query("page") int page);
}
