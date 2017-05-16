package com.example.android.popcorn.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Andrew Osborne on 5/3/17.
 *
 * This is a database which allows the user to keep track of their favorite movies from
 * TMDB selections.
 *
 */

public class FavoritesContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popcorn";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    private FavoritesContract() {
        // This class should never be initialized
    }

    public static class FavoritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FAVORITES);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY
                + "/" + PATH_FAVORITES;

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY
                + "/" + PATH_FAVORITES;

        /** Name of database table for items */
        public static final String TABLE_NAME = "favoriteMovies";

        /** Names of each column in the table. Note: _ID is assumed already by BaseColumns */
        public static final String COLUMN_MOVIE_NAME = "movie_name";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_POSTER_PATH = "movie_poster_path";

        /** Used to create the table */
        public static final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                FavoritesEntry.TABLE_NAME + " (" +
                FavoritesEntry._ID + " INTEGER PRIMARY KEY," +
                FavoritesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL," +
                FavoritesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                FavoritesEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL)";

        /** Used to delete the table */
        public static final String SQL_DELETE_FAVORITES_TABLE =
                "DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME;

    }
}
