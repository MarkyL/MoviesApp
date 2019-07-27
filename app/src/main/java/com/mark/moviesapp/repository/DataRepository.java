package com.mark.moviesapp.repository;

import android.content.Context;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.mark.moviesapp.AppExecutors;
import com.mark.moviesapp.db.AppDatabase;
import com.mark.moviesapp.db.entity.MovieEntity;

import java.util.List;

/**
 * Repository handling the work with movies.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;

    // MediatorLiveData is used to work with multiple LivaData objects simultaneously.
    private MediatorLiveData<List<MovieEntity>> mObservableMovies;

    private DataRepository(final Context context) {
        mDatabase = AppDatabase.getInstance(context);
        mObservableMovies = new MediatorLiveData<>();

        mObservableMovies.addSource(mDatabase.theMovieDBResultItemDao().getAllMovies(),
            movieEntities -> {
                if (mDatabase.getDatabaseCreated().getValue() != null) {
                    mObservableMovies.postValue(movieEntities);
                }
            });
    }

    public static DataRepository getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of movies from the database and get notified when the data changes.
     */
    public MutableLiveData<List<MovieEntity>> getAllMovies() { return mObservableMovies; }

    public List<MovieEntity> getAllMoviesSync() {
        return mDatabase.theMovieDBResultItemDao().getAllMoviesSync();
    }

    public MutableLiveData<MovieEntity> getMovieById(final int movieID) {
        MutableLiveData<MovieEntity> movie = new MutableLiveData<>();

        AppExecutors.getInstance().diskIO().execute(()
                -> movie.postValue(mDatabase.theMovieDBResultItemDao().getMovieByID(movieID).getValue()));

        return movie;
    }

    public MutableLiveData<MovieEntity> getMovieByTitle(final String title) {
        MutableLiveData<MovieEntity> movie = new MutableLiveData<>();

        AppExecutors.getInstance().diskIO().execute(()
                -> movie.postValue(mDatabase.theMovieDBResultItemDao().getMovieByTitle(title).getValue()));

        return movie;
    }

    public void insertFavoriteMovie(final MovieEntity movie) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mDatabase.theMovieDBResultItemDao().insertFavoriteMovie(movie));
    }

    public void deleteFavoriteMovie(final int id) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mDatabase.theMovieDBResultItemDao().deleteMovieByID(id));
    }

}
