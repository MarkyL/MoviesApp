package com.mark.moviesapp.repository;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    private static Retrofit retrofit;
    private static final String API_KEY = "d1dae9955c4a0b5de825e9e1d30fa1e7";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    /**
     * Shrink data for performance, cache requests, define timeouts.
     */
    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .build();

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static MoviesService moviesService = getRetrofitInstance().create(MoviesService.class);
}
