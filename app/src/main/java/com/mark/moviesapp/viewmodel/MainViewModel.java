package com.mark.moviesapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mark.moviesapp.db.entity.MovieEntity;
import com.mark.moviesapp.model.MoviesListResponse;
import com.mark.moviesapp.repository.DataRepository;
import com.mark.moviesapp.repository.NetworkClient;
import com.mark.moviesapp.utils.DateFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mark.moviesapp.BuildConfig.THE_MOVIE_DB_API_KEY;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";

    @Nullable
    private MutableLiveData<MoviesListResponse> mLatestMoviesLiveData;

    @Nullable
    private LiveData<List<MovieEntity>> mAllFavoriteMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<MoviesListResponse> getLatestMovies(final int page) {
        mLatestMoviesLiveData = new MutableLiveData<>();

        Call<MoviesListResponse> call = NetworkClient.moviesService.
                discoverLatestMovies(THE_MOVIE_DB_API_KEY, DateFactory.getCurrentDate(), page);
        call.enqueue(new Callback<MoviesListResponse>() {
            @Override
            public void onResponse(Call<MoviesListResponse> call, Response<MoviesListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mLatestMoviesLiveData.setValue(response.body());
                } else {
                    onFailure(call, new Throwable());
                }
            }

            @Override
            public void onFailure(Call<MoviesListResponse> call, Throwable t) {
                mLatestMoviesLiveData.setValue(new MoviesListResponse());
            }
        });
        return mLatestMoviesLiveData;
    }

    public LiveData<List<MovieEntity>> getAllFavoriteMovies() {
        if (mAllFavoriteMovies == null) {
            mAllFavoriteMovies = DataRepository.getInstance(getApplication()).getAllMovies();
        }
        return mAllFavoriteMovies;
    }

}
