package com.mark.moviesapp.view.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.mark.moviesapp.R;
import com.mark.moviesapp.callback.IBackPressListener;
import com.mark.moviesapp.utils.FragmentNavigationManager;
import com.mark.moviesapp.view.fragment.MoviesFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FragmentNavigationManager mFragmentNavigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentNavigationManager = new FragmentNavigationManager(getSupportFragmentManager(), getContainerID());
        mFragmentNavigationManager.addFragment(MoviesFragment.newInstance());
    }

    public int getContainerID() {
        return R.id.fragments_container;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragments_container);
            if (!(fragment instanceof IBackPressListener) || !((IBackPressListener) fragment).onBackPressed()) {
                onBackPressed();
                return true;
            }
        }
        return false;
    }
}
