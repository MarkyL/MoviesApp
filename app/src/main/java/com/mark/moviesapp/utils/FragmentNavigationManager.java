package com.mark.moviesapp.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentNavigationManager {

    private FragmentManager mFragmentManager;
    private int mContainerID;

    public FragmentNavigationManager(FragmentManager fragmentManager, int containerID) {
        mFragmentManager = fragmentManager;
        mContainerID = containerID;
    }

    public void addFragment(Fragment toLoad) {
        mFragmentManager
                .beginTransaction()
                .add(mContainerID, toLoad)
                .commit();
    }

    public void replaceFragment(Fragment toLoad) {
        mFragmentManager
                .beginTransaction()
                .replace(mContainerID, toLoad)
                .commit();
    }

}
