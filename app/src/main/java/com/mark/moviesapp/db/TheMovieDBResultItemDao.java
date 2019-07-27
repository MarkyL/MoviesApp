package com.mark.moviesapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mark.moviesapp.db.entity.MovieEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface TheMovieDBResultItemDao {

    @Query("SELECT * FROM MovieEntity")
    LiveData<List<MovieEntity>> getAllMovies();

    @Query("SELECT * FROM MovieEntity")
    List<MovieEntity> getAllMoviesSync();

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    LiveData<MovieEntity> getMovieByID(final int id);

    @Query("SELECT * FROM MovieEntity WHERE title = :title")
    LiveData<MovieEntity> getMovieByTitle(final String title);

    @Insert(onConflict = IGNORE)
    void insertFavoriteMovie(MovieEntity movie);

    @Query("DELETE FROM MovieEntity WHERE id = :id")
    void deleteMovieByID(final int id);

}
