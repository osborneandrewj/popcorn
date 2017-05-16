package com.example.android.popcorn;

/**
 * App made by Andrew Osborne, 2017
 *
 * Star icons by: "http://www.flaticon.com/authors/madebyoliver"
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.android.popcorn.adapters.CustomFragmentPagerAdapter;
import com.example.android.popcorn.adapters.PosterAdapter;
import com.example.android.popcorn.models.Movie;
import com.example.android.popcorn.models.MoviesResults;
import com.example.android.popcorn.retrofit.TheMovieDbAPI;
import com.example.android.popcorn.retrofit.TheMovieDbApiClient;
import com.example.android.popcorn.utilites.MyNetworkUtils;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Display a scrollable grid of movie posters to the user. It should look something like this:
 * <p>
 * *----------------------*----------------------*
 * |                      |                      |
 * |                      |                      |
 * |                      |                      |
 * |                      |                      |
 * |                      |                      |
 * |       Movie          |        Movie         |
 * |                      |                      |
 * |                      |                      |
 * |                      |                      |
 * |                      |                      |
 * |                      |                      |
 * |                      |                      |
 * *----------------------*----------------------*
 * |                      |                      |
 * |                      |                      |
 * |                      |                      |
 * |                      |                      |
 * |                      |                      |
 * |       Movie          |        Movie         |
 * |                      |                      |
 * |                      |                      |
 * |                      |                      |
 * |                      |                      |
 * |                      |                      |
 * |                      |                      |
 * *----------------------*----------------------*
 */


// TODO: Create a FavoritesFragment
// TODO: Move all dimensions to dimens.xml
// TODO: Create a tablet layout

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        CustomFragmentPagerAdapter adapter =
                new CustomFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.slidingTabs);
        tabLayout.setupWithViewPager(viewPager);

    }
}
