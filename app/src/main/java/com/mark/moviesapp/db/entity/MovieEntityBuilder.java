package com.mark.moviesapp.db.entity;

public class MovieEntityBuilder {
    private int id;
    private String overview;
    private String originalLanguage;
    private String originalTitle;
    private boolean video;
    private String title;
    private String posterPath;
    private String backdropPath;
    private String releaseDate;
    private float voteAverage;
    private double popularity;
    private boolean adult;
    private int voteCount;
    private String baseImageUrl;

    public MovieEntityBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public MovieEntityBuilder setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public MovieEntityBuilder setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    public MovieEntityBuilder setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public MovieEntityBuilder setVideo(boolean video) {
        this.video = video;
        return this;
    }

    public MovieEntityBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public MovieEntityBuilder setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public MovieEntityBuilder setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public MovieEntityBuilder setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public MovieEntityBuilder setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public MovieEntityBuilder setPopularity(double popularity) {
        this.popularity = popularity;
        return this;
    }

    public MovieEntityBuilder setAdult(boolean adult) {
        this.adult = adult;
        return this;
    }

    public MovieEntityBuilder setVoteCount(int voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    public MovieEntityBuilder setBaseImageUrl(String baseImageUrl) {
        this.baseImageUrl = baseImageUrl;
        return this;
    }

    public MovieEntity createTheMovieDBResultEntity() {
        return new MovieEntity(id, overview, originalLanguage, originalTitle, video, title, posterPath, backdropPath, releaseDate, voteAverage, popularity, adult, voteCount, baseImageUrl);
    }
}