package com.mark.moviesapp.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.mark.moviesapp.R;
import com.mark.moviesapp.databinding.FragmentMovieDetailsBinding;
import com.mark.moviesapp.db.entity.MovieEntity;

public class MovieDetailsFragment extends Fragment {
    private static final String TAG = "MovieDetailsFragment";

    private static final String ARG_MOVIE_ENTITY = "movie_entity";

    private FragmentMovieDetailsBinding mBinding;
    private MovieEntity mMovieEntity;

    public static MovieDetailsFragment newInstance(MovieEntity movie) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE_ENTITY, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mMovieEntity = (MovieEntity) args.getSerializable(ARG_MOVIE_ENTITY);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_favorites).setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false);
        Log.d(TAG, "onCreateView: ");
        return mBinding.getRoot();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");

        String posterPath = mMovieEntity.getPosterPath();

        if (TextUtils.isEmpty(posterPath)) {
            Glide.with(this)
                    .load(R.drawable.no_image_available)
                    .fitCenter()
                    .into(mBinding.posterImage);
        } else {
            Glide.with(this)
                    .load(MovieEntity.getBaseImagePath() + posterPath)
                    .into(mBinding.posterImage);
        }

        mBinding.title.setText(mMovieEntity.getTitle());
        mBinding.userRatingTv.setText(String.format("%.1f", mMovieEntity.getVoteAverage()));
        mBinding.descriptionTv.setText(mMovieEntity.getOverview());
        mBinding.releaseDateTv.setText(mMovieEntity.getReleaseDate());
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentActivity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(mMovieEntity.getTitle());
                activity.invalidateOptionsMenu();
            }
        }
    }
}
