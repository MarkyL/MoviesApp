package com.mark.moviesapp.callback;

import com.mark.moviesapp.db.entity.MovieEntity;

public interface MovieClickCallback {
    void onMovieClick(final MovieEntity movie,final int position);
    void onFavoriteClick(final MovieEntity movie,final int position);
}
