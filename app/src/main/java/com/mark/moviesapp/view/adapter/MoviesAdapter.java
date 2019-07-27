package com.mark.moviesapp.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mark.moviesapp.R;
import com.mark.moviesapp.callback.MovieClickCallback;
import com.mark.moviesapp.databinding.MovieItemBinding;
import com.mark.moviesapp.databinding.MovieItemLoadingBinding;
import com.mark.moviesapp.db.entity.MovieEntity;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = "MoviesAdapter";

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private List<MovieEntity> mMovieList;
    private Context mContext;

    @Nullable
    private final MovieClickCallback movieClickCallback;

    public MoviesAdapter(Context context, @Nullable MovieClickCallback callback) {
        this.mContext = context;
        movieClickCallback = callback;
        setHasStableIds(true);
        mMovieList = new ArrayList<>();
    }

    public void updateMovie(final MovieEntity newMovieData, final int position) {
        mMovieList.set(position, newMovieData);
        notifyItemChanged(position);
    }

    public void updateMovie(final MovieEntity newMovieData) {
        for (int i = 0; i < mMovieList.size(); i++) {
           if (mMovieList.get(i).getId() == newMovieData.getId()) {
               updateMovie(newMovieData, i);
               break;
           }
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                MovieItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.movie_item, parent, false);
                binding.setCallback(movieClickCallback);
                return new MovieViewHolder(binding);
            case VIEW_TYPE_LOADING:
                MovieItemLoadingBinding loadingBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.movie_item_loading, parent, false);
                return new FooterHolder(loadingBinding);
            default:
                loadingBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.movie_item_loading, parent, false);
                return new FooterHolder(loadingBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mMovieList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mMovieList == null ? 0 : mMovieList.size();
    }

    @Override
    public long getItemId(int position) {
        return mMovieList.get(position).getId();
    }

    public List<MovieEntity> getDataSet() {
        return mMovieList;
    }

    public void add(MovieEntity movie) {
        mMovieList.add(movie);
        notifyItemInserted(mMovieList.size() - 1);
    }

    public void addAll(List<MovieEntity> movies) {
        for (MovieEntity movieEntity : movies) {
            add(movieEntity);
        }
    }

    public void remove(MovieEntity movie) {
        int position = mMovieList.indexOf(movie);
        if (position > -1) {
            mMovieList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, 1);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new MovieEntity());
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mMovieList.size() - 1;
        MovieEntity item = getItem(position);
        if (item != null) {
            mMovieList.remove(position);
            notifyItemRemoved(position);
        }
    }

    MovieEntity getItem(int position) {
        return mMovieList.get(position);
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public class MovieViewHolder extends BaseViewHolder {

        final MovieItemBinding binding;

        public MovieViewHolder(MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ImageView getImageView() {
            return binding.moviePoster;
        }

        @Override
        protected void clear() {
            binding.moviePoster.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.loading));
        }

        public void onBind(int position) {
            super.onBind(position);
            MovieEntity movie = mMovieList.get(position);

            binding.setMovieEntity(movie);
            binding.setPosition(position);
            binding.executePendingBindings();

            final String posterPath = movie.getPosterPath();

            if (TextUtils.isEmpty(posterPath)) {
                Glide.with(mContext)
                        .load(R.drawable.loading)
                        .fitCenter()
                        .into(binding.moviePoster);
                binding.likeButton.setVisibility(View.GONE);
            } else {
                Glide.with(mContext)
                        .load(MovieEntity.getBaseImagePath() + posterPath)
                        .into(binding.moviePoster);
            }

        }
    }

    public class FooterHolder extends BaseViewHolder {

        final MovieItemLoadingBinding binding;

        FooterHolder(MovieItemLoadingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        protected void clear() {

        }
    }
}
