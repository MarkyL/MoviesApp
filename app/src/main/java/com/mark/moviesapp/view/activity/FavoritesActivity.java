package com.mark.moviesapp.view.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.mark.moviesapp.R;
import com.mark.moviesapp.callback.IBackPressListener;
import com.mark.moviesapp.utils.FragmentNavigationManager;
import com.mark.moviesapp.view.fragment.FavoriteMoviesFragment;

public class FavoritesActivity extends AppCompatActivity {

    private static final String TAG = "FavoritesActivity";

    public static final String IS_FAVORITE_CHANGED = "is_favorite_changed";

    private FragmentNavigationManager mFragmentNavigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mFragmentNavigationManager = new FragmentNavigationManager(getSupportFragmentManager(), R.id.fragments_container);
        mFragmentNavigationManager.addFragment(FavoriteMoviesFragment.newInstance());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.app_name);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragments_container);
            if (!(fragment instanceof IBackPressListener) || !((IBackPressListener) fragment).onBackPressed()) {
                onBackPressed();
            }
        }
        return true;
    }
}
