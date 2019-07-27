package com.mark.moviesapp.model;

public interface Movie {
    int getId();
    String getOverview();
    String getOriginalLanguage();
    String getOriginalTitle();
    boolean isVideo();
    String getTitle();
    String getPosterPath();
    String getBackdropPath();
    String getReleaseDate();
    float getVoteAverage();
    double getPopularity();
    boolean isAdult();
    int getVoteCount();
}
