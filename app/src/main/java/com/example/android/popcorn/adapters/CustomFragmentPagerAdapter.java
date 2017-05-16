package com.example.android.popcorn.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popcorn.DiscoverPopularFragment;
import com.example.android.popcorn.DiscoverTopRatedFragment;
import com.example.android.popcorn.DiscoverFavoritesFragment;

/**
 * Created by Andrew Osborne on 5/15/2017.
 *
 */

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

    public CustomFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DiscoverPopularFragment();
            case 1:
                return new DiscoverTopRatedFragment();
            default:
                return new DiscoverFavoritesFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Popular Now";
            case 1:
                return "Top Rated";
            default:
                return "My Favorites";
        }
    }
}
