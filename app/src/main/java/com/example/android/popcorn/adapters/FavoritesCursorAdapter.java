package com.example.android.popcorn.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.popcorn.R;
import com.example.android.popcorn.data.FavoritesContract;
import com.example.android.popcorn.utilites.MyNetworkUtils;

/**
 * Created by Andrew Osborne on 5/15/2017.
 *
 */

public class FavoritesCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = FavoritesCursorAdapter.class.getSimpleName();

    public FavoritesCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.test_favorites_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView id = (TextView)view.findViewById(R.id.movie_id);

        String nameString = cursor.getString(
                cursor.getColumnIndexOrThrow(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME));
        int idInt = cursor.getInt(
                cursor.getColumnIndexOrThrow(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID));
        String posterPath = cursor.getString(
                cursor.getColumnIndexOrThrow(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_POSTER_PATH));
        Uri posterUri = MyNetworkUtils.getUriFromPosterPath(posterPath);


        Log.v(LOG_TAG, "Got a movie here: " + posterUri);

        name.setText(nameString);
        id.setText(String.valueOf(posterUri));

    }
}
