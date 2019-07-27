package com.mark.moviesapp.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.Explode;
import androidx.transition.Fade;

import com.mark.moviesapp.AppExecutors;
import com.mark.moviesapp.R;
import com.mark.moviesapp.animation.DetailsTransition;
import com.mark.moviesapp.callback.MovieClickCallback;
import com.mark.moviesapp.databinding.FragmentMoviesBinding;
import com.mark.moviesapp.db.entity.MovieEntity;
import com.mark.moviesapp.repository.DataRepository;
import com.mark.moviesapp.utils.PaginationScrollListener;
import com.mark.moviesapp.view.activity.FavoritesActivity;
import com.mark.moviesapp.view.activity.MainActivity;
import com.mark.moviesapp.view.adapter.MoviesAdapter;
import com.mark.moviesapp.viewmodel.MainViewModel;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mark.moviesapp.view.activity.FavoritesActivity.IS_FAVORITE_CHANGED;

public class MoviesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MoviesFragment";
    private final int FAVORITE_REQUEST = 100;

    private MoviesAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private FragmentMoviesBinding mBinding;
    private MainViewModel mViewModel;

    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

//    private Map<Integer, MovieEntity> mMoviesMap;

    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.movies_menu, menu);
        menu.findItem(R.id.action_favorites).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_favorites) {
            onFavoritesMenuClick();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onFavoritesMenuClick() {
        Intent favoriteActivityIntent = new Intent(getContext(), FavoritesActivity.class);
        FragmentActivity activity = getActivity();
        if (activity != null)
            startActivityForResult(favoriteActivityIntent, FAVORITE_REQUEST);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        FragmentActivity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setTitle(R.string.app_name);
                activity.invalidateOptionsMenu();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mAdapter = new MoviesAdapter(getContext(), mMovieClickCallBack);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        Log.d(TAG, "onViewCreated: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mBinding.setViewModel(mViewModel);
        fetchMoviesFromRemote();
    }

    private void fetchMoviesFromRemote() {
        mViewModel.getLatestMovies(currentPage).observe(getViewLifecycleOwner(),
                moviesListResponse -> {
                    mBinding.progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "fetchMoviesFromRemote: ");
                    Log.d(TAG, "onChanged: moviesList = " + moviesListResponse.getTotalResults());
                    List<MovieEntity> moviesList = moviesListResponse.getResults();
                    if (moviesList.size() == 0) {
                        Toast.makeText(getContext(), "Empty list fetched", Toast.LENGTH_LONG).show();
                    }
                    mAdapter.addAll(moviesList);
                    mBinding.swipeRefresh.setRefreshing(false);
                    if (currentPage < totalPage) mAdapter.addLoading();
                    else isLastPage = true;
                    isLoading = false;
                    checkForFavoriteMovies(moviesList);
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: requestCode = " +
                "[" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        switch (resultCode) {
            case Activity.RESULT_OK: {
                switch (requestCode) {
                    case FAVORITE_REQUEST: {
                        if (data != null && data.getBooleanExtra(IS_FAVORITE_CHANGED, false)) {
                            updateFavoriteMovies();
                        }
                    }
                }
            }
        }
    }

    /**
     * There's been an update to favorite movies in DB, need to run over all adapter dataset
     */
    private void updateFavoriteMovies() {
        Log.d(TAG, "updateFavoriteMovies: ");
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<MovieEntity> faveMovieEntities = DataRepository.getInstance(getContext()).getAllMoviesSync();
            Map<Integer, MovieEntity> faveMoviesMap = faveMovieEntities.stream().collect(
                    Collectors.toMap(MovieEntity::getId, Function.identity()));
            List<MovieEntity> dataSet = mAdapter.getDataSet();

            FragmentActivity activity = getActivity();
            if (activity == null) return;

            for (int i = 0; i < dataSet.size(); i++) {
                MovieEntity currentMovie = dataSet.get(i);
                MovieEntity entity = faveMoviesMap.get(currentMovie.getId());
                currentMovie.setIsFavoriteMovie(entity != null);
                final int currentPosition = i;
                activity.runOnUiThread(() -> mAdapter.updateMovie(currentMovie, currentPosition));
            }
        });
    }

    /**
     * Cross list between remote moviesList data and local list of favorite movies.
     *
     * @param moviesList the fetched data from remote API.
     */
    private void checkForFavoriteMovies(List<MovieEntity> moviesList) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<MovieEntity> faveMovieEntities = DataRepository.getInstance(getContext()).getAllMoviesSync();
            Log.d(TAG, "checkForFavoriteMovies: moviesSync = " + faveMovieEntities);
            Map<Integer, MovieEntity> faveMoviesMap = faveMovieEntities.stream().collect(
                    Collectors.toMap(MovieEntity::getId, Function.identity()));

            FragmentActivity activity = getActivity();
            if (activity == null) return;

            for (MovieEntity movieEntity : moviesList) {
                // Check whether the moveEntity is a favorite movie
                MovieEntity entity = faveMoviesMap.get(movieEntity.getId());
                if (entity != null) {
                    // this is a favorite movie.
                    movieEntity.setIsFavoriteMovie(true);
                    activity.runOnUiThread(() -> mAdapter.updateMovie(movieEntity));
                }
            }
        });

    }

    private void initUI() {
        mRecyclerView = mBinding.moviesRecycler;
        mRecyclerView.setHasFixedSize(true);
        // Pagination variables
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mBinding.swipeRefresh.setOnRefreshListener(this);

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        mRecyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                fetchMoviesFromRemote();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private final MovieClickCallback mMovieClickCallBack = new MovieClickCallback() {
        @Override
        public void onMovieClick(final MovieEntity movie, final int position) {
            //TODO: user can spam click this and it'll open many fragments...
            Log.d(TAG, "onMovieClick() called with: movie = [" + movie + "]");
            FragmentActivity activity = getActivity();
            if (activity instanceof MainActivity) {

                MoviesAdapter.MovieViewHolder movieViewHolder =
                        (MoviesAdapter.MovieViewHolder) mBinding.moviesRecycler.findViewHolderForAdapterPosition(position);
                if (movieViewHolder != null) {
                    ImageView imageView = movieViewHolder.getImageView();
                    MovieDetailsFragment detailsFragment = MovieDetailsFragment.newInstance(movie);
//                    setAnimationToFragment(detailsFragment);


                    int containerID = ((MainActivity) activity).getContainerID();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .setReorderingAllowed(true)
//                            .addSharedElement(imageView, "posterImage")
                            .replace(containerID, detailsFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        }

        @Override
        public void onFavoriteClick(final MovieEntity movie, final int position) {
            Log.d(TAG, "onFavoriteClick() called with: movie = [" + movie + "]");
            if (movie.isFavoriteMovie()) {
                DataRepository.getInstance(getContext()).deleteFavoriteMovie(movie.getId());
                movie.setIsFavoriteMovie(false);
            } else {
                DataRepository.getInstance(getContext()).insertFavoriteMovie(movie);
                movie.setIsFavoriteMovie(true);
            }
            mAdapter.updateMovie(movie, position);
        }
    };

    private void setAnimationToFragment(MovieDetailsFragment detailsFragment) {
        detailsFragment.setSharedElementEnterTransition(new DetailsTransition());
        detailsFragment.setEnterTransition(new Explode());
        setExitTransition(new Fade());
        detailsFragment.setSharedElementReturnTransition(new DetailsTransition());
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        mAdapter.clear();
        fetchMoviesFromRemote();
    }
}
