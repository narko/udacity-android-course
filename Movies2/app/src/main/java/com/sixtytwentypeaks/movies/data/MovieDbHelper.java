package com.sixtytwentypeaks.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by narko on 17/02/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 3;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE_MOVIES =
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " ("
                        + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, "
                        + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                        + MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                        + MovieContract.MovieEntry.COLUMN_DATE + " TEXT NOT NULL, "
                        + MovieContract.MovieEntry.COLUMN_RATE + " TEXT NOT NULL, "
                        + MovieContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, "
                        + MovieContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_TABLE_MOVIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // In the future we will need to adapt this code if the new db version
        // contains schema changes.
        // The current method will just delete the table and create a new one.
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
