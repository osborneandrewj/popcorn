package com.example.android.popcorn.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Andrew Osborne on 5/15/2017.
 *
 */

public class FavoritesProvider extends ContentProvider {

    private static final String LOG_TAG = FavoritesProvider.class.getSimpleName();

    private FavoritesDBHelper mFavoritesDbHelper;
    private static final int FAVORITES = 100;
    private static final int FAVORITE_ITEM = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(
                FavoritesContract.CONTENT_AUTHORITY,
                FavoritesContract.PATH_FAVORITES,
                FAVORITES);
        sUriMatcher.addURI(
                FavoritesContract.CONTENT_AUTHORITY,
                FavoritesContract.PATH_FAVORITES + "/#",
                FAVORITE_ITEM);
    }

    @Override
    public boolean onCreate() {
        mFavoritesDbHelper = new FavoritesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        SQLiteDatabase database = mFavoritesDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                cursor = database.query(FavoritesContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_ITEM:
                selection = FavoritesContract.FavoritesEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(FavoritesContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query this: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                return insertFavoriteMovie(uri, contentValues);
            default:
                // We do not want this case
                throw new IllegalArgumentException("Insertion not supported for this URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String s,
                      @Nullable String[] strings) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                return deleteFavoriteMovie(uri, s, strings);
            case FAVORITE_ITEM:
                s = FavoritesContract.FavoritesEntry._ID + "=?";
                strings = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return deleteFavoriteMovie(uri, s, strings);
            default:
                throw new IllegalArgumentException("Deletion is not allowed for " + uri);
        }

    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues contentValues,
                      @Nullable String s,
                      @Nullable String[] strings) {
        return 0;
    }

    private Uri insertFavoriteMovie(Uri uri, ContentValues values) {

        SQLiteDatabase database = mFavoritesDbHelper.getWritableDatabase();

        long id_value = database.insert(FavoritesContract.FavoritesEntry.TABLE_NAME, null, values);

        if (id_value == -1) {
            Log.e(LOG_TAG, "Failed to insert new movie for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id_value);
    }

    private int deleteFavoriteMovie(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mFavoritesDbHelper.getWritableDatabase();

        int numberOfRowsDeleted = database.delete(
                FavoritesContract.FavoritesEntry.TABLE_NAME,
                selection + "=?",
                selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return numberOfRowsDeleted;
    }
}
