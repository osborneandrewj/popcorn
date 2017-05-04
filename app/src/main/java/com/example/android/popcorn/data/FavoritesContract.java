package com.example.android.popcorn.data;

import android.provider.BaseColumns;

/**
 * Created by Andrew Osborne on 5/3/17.
 *
 * This is a database which allows the user to keep track of their favorite movies from
 * TMDB selections.
 *
 */

public class FavoritesContract {

    private FavoritesContract() {
        // This class should never be initialized
    }

    public static class FavoritesEntry implements BaseColumns {

        /** Name of database table for items */
        public static final String TABLE_NAME = "favoriteMovies";

        /** Names of each column in the table. Note: _ID is assumed already by BaseColumns */
        public static final String COLUMN_MOVIE_NAME = "movie_name";
        public static final String COLUMN_MOVIE_ID = "movie_id";

        /** Used to create the table */
        public static final String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " +
                FavoritesEntry.TABLE_NAME + " (" +
                FavoritesEntry._ID + " INTEGER PRIMARY KEY," +
                FavoritesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL," +
                FavoritesEntry.COLUMN_MOVIE_ID + " INTEGET NOT NULL)";

        /** Used to delete the table */
        public static final String SQL_DELETE_INVENTORY_TABLE =
                "DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME;

    }
}
