package com.mark.moviesapp.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.moviesapp.AppExecutors;
import com.mark.moviesapp.R;
import com.mark.moviesapp.callback.IBackPressListener;
import com.mark.moviesapp.callback.MovieClickCallback;
import com.mark.moviesapp.databinding.FragmentFavoriteMoviesBinding;
import com.mark.moviesapp.db.entity.MovieEntity;
import com.mark.moviesapp.repository.DataRepository;
import com.mark.moviesapp.view.adapter.MoviesAdapter;
import com.mark.moviesapp.viewmodel.MainViewModel;

import java.util.List;

import static com.mark.moviesapp.view.activity.FavoritesActivity.IS_FAVORITE_CHANGED;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMoviesFragment extends Fragment implements IBackPressListener {

    private static final String TAG = "FavoriteMoviesFragment";

    private MoviesAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private FragmentFavoriteMoviesBinding mBinding;

    private boolean isFavoriteChanged = false;

    public FavoriteMoviesFragment() {
        // Required empty public constructor
    }

    public static FavoriteMoviesFragment newInstance() {
        FavoriteMoviesFragment fragment = new FavoriteMoviesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_movies, container, false);
        mAdapter = new MoviesAdapter(getContext(), mMovieClickCallBack);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
    }

    private void initUI(View view) {
        mRecyclerView = view.findViewById(R.id.moviesRecycler);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mBinding.setViewModel(viewModel);

        fetchFavoriteMoviesAndShow();
    }

    private void fetchFavoriteMoviesAndShow() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<MovieEntity> faveMovieEntities = DataRepository.getInstance(getContext()).getAllMoviesSync();
                    faveMovieEntities.forEach(movie -> movie.setIsFavoriteMovie(true));

            FragmentActivity activity = getActivity();
            if (activity != null) activity.runOnUiThread(() -> mAdapter.addAll(faveMovieEntities));
        });
    }

    private final MovieClickCallback mMovieClickCallBack = new MovieClickCallback() {

        @Override
        public void onMovieClick(MovieEntity movie, int position) {
            Log.d(TAG, "onMovieClick() called with: movie = [" + movie + "], position = [" + position + "]");
        }

        @Override
        public void onFavoriteClick(MovieEntity movie, final int position) {
            isFavoriteChanged = true;
            if (movie.isFavoriteMovie()) {
                DataRepository.getInstance(getContext()).deleteFavoriteMovie(movie.getId());
                movie.setIsFavoriteMovie(false);
                mAdapter.remove(movie);
            } else {
                // shouldn't get here ever because we are seeing only favorite movies anyway.
                Log.e(TAG, "onFavoriteClick: non-favorite movie clicked.", new Throwable());
            }
        }
    };

    @Override
    public boolean onBackPressed() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setResult(Activity.RESULT_OK, new Intent().putExtra(IS_FAVORITE_CHANGED, isFavoriteChanged));
            activity.finish();
            return true;
        }
        return false;
    }
}
