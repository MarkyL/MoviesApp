package com.mark.moviesapp.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.mark.moviesapp.model.Movie;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class MovieEntity implements Movie, Serializable {

    @SerializedName("id")
    @PrimaryKey
    private int id;

    @SerializedName("overview")
    private String overview;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("video")
    private boolean video;

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_average")
    private float voteAverage;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("vote_count")
    private int voteCount;

    @Ignore
    private boolean isFavoriteMovie = false;

    public MovieEntity() {
    }

    public MovieEntity(int id, String overview, String originalLanguage, String originalTitle, boolean video,
                       String title, String posterPath, String backdropPath, String releaseDate, float voteAverage,
                       double popularity, boolean adult, int voteCount, String baseImageUrl) {
        this.id = id;
        this.overview = overview;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.video = video;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.adult = adult;
        this.voteCount = voteCount;
        this.baseImageUrl = baseImageUrl;
    }

    public void setOverview(String overview){
        this.overview = overview;
    }

    public String getOverview(){
        return overview;
    }

    public void setOriginalLanguage(String originalLanguage){
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalLanguage(){
        return originalLanguage;
    }

    public void setOriginalTitle(String originalTitle){
        this.originalTitle = originalTitle;
    }

    public String getOriginalTitle(){
        return originalTitle;
    }

    public void setVideo(boolean video){
        this.video = video;
    }

    public boolean isVideo(){
        return video;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setPosterPath(String posterPath){
        this.posterPath = posterPath;
    }

    @Ignore
    private static String baseImageUrl = "http://image.tmdb.org/t/p/w500";

    public static String getBaseImagePath() {
        return baseImageUrl;
    }

    // contains only part of the poster path, please use with concatenation of getBaseImagePath as prefix.
    public String getPosterPath(){
        return posterPath;
    }

    public void setBackdropPath(String backdropPath){
        this.backdropPath = backdropPath;
    }

    public String getBackdropPath(){
        return backdropPath;
    }

    public void setReleaseDate(String releaseDate){
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate(){
        return releaseDate;
    }

    public void setVoteAverage(float voteAverage){
        this.voteAverage = voteAverage;
    }

    public float getVoteAverage(){
        return voteAverage;
    }

    public void setPopularity(double popularity){
        this.popularity = popularity;
    }

    public double getPopularity(){
        return popularity;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setAdult(boolean adult){
        this.adult = adult;
    }

    public boolean isAdult(){
        return adult;
    }

    public void setVoteCount(int voteCount){
        this.voteCount = voteCount;
    }

    public int getVoteCount(){
        return voteCount;
    }

    public boolean isFavoriteMovie() {
        return isFavoriteMovie;
    }

    public void setIsFavoriteMovie(boolean isFavorite) {
        isFavoriteMovie = isFavorite;
    }

    @Override
    public String toString(){
        return
                "MovieEntity{" +
                        "overview = '" + overview + '\'' +
                        ",original_language = '" + originalLanguage + '\'' +
                        ",original_title = '" + originalTitle + '\'' +
                        ",video = '" + video + '\'' +
                        ",title = '" + title + '\'' +
                        ",poster_path = '" + posterPath + '\'' +
                        ",backdrop_path = '" + backdropPath + '\'' +
                        ",release_date = '" + releaseDate + '\'' +
                        ",vote_average = '" + voteAverage + '\'' +
                        ",popularity = '" + popularity + '\'' +
                        ",id = '" + id + '\'' +
                        ",adult = '" + adult + '\'' +
                        ",vote_count = '" + voteCount + '\'' +
                        "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieEntity)) return false;
        MovieEntity that = (MovieEntity) o;
        return getId() == that.getId() &&
                isVideo() == that.isVideo() &&
                Float.compare(that.getVoteAverage(), getVoteAverage()) == 0 &&
                Double.compare(that.getPopularity(), getPopularity()) == 0 &&
                isAdult() == that.isAdult() &&
                getVoteCount() == that.getVoteCount() &&
                Objects.equals(getOverview(), that.getOverview()) &&
                Objects.equals(getOriginalLanguage(), that.getOriginalLanguage()) &&
                Objects.equals(getOriginalTitle(), that.getOriginalTitle()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getPosterPath(), that.getPosterPath()) &&
                Objects.equals(getBackdropPath(), that.getBackdropPath()) &&
                Objects.equals(getReleaseDate(), that.getReleaseDate()) &&
                Objects.equals(baseImageUrl, that.baseImageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOverview(), getOriginalLanguage(), getOriginalTitle(), isVideo(),
                getTitle(), getPosterPath(), getBackdropPath(), getReleaseDate(), getVoteAverage(),
                getPopularity(), isAdult(), getVoteCount(), baseImageUrl);
    }
}
