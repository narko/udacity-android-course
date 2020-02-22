/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
//      (21) Implement LoaderManager.LoaderCallbacks<Cursor>

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

//  (18) Create a String array containing the names of the desired data columns from our ContentProvider
    private static final String[] WEATHER_PROJECTION = new String[] {
        WeatherContract.WeatherEntry.COLUMN_DATE,
        WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
        WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
        WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
        WeatherContract.WeatherEntry.COLUMN_PRESSURE
    };
//  (19) Create constant int values representing each column name's position above
    public static final int COL_DATE_INDEX = 0;
    public static final int COL_DESCRIPTION_INDEX = 1;
    public static final int COL_MAX_INDEX = 2;
    public static final int COL_MIN_INDEX = 3;
    public static final int COL_HUMIDITY_INDEX = 4;
    public static final int COL_WIND_INDEX = 5;
    public static final int COL_PRESSURE_INDEX = 6;

//  (20) Create a constant int to identify our loader used in DetailActivity
    public final int FORECAST_LOADER_ID = 345;

    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

//  (15) Declare a private Uri field called mUri
    private Uri mUri;

//  (10) Remove the mWeatherDisplay TextView declaration

//  (11) Declare TextViews for the date, description, high, low, humidity, wind, and pressure
    @BindView(R.id.tv_date) TextView dateTextView;
    @BindView(R.id.tv_weather_description) TextView descriptionTextView;
    @BindView(R.id.tv_high_temp) TextView highTextView;
    @BindView(R.id.tv_low_temp) TextView lowTextView;
    @BindView(R.id.tv_humidity) TextView humidityTextView;
    @BindView(R.id.tv_wind) TextView windTextView;
    @BindView(R.id.tv_pressure) TextView pressureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
//      (12) Remove mWeatherDisplay TextView

//      (13) Find each of the TextViews by ID

//      (14) Remove the code that checks for extra text
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
//      (16) Use getData to get a reference to the URI passed with this Activity's Intent
            mUri = intentThatStartedThisActivity.getData();
            // (17) Throw a NullPointerException if that URI is null
            if (mUri == null) {
                throw new NullPointerException("The given uri is null");
            }
        }


//      (35) Initialize the loader for DetailActivity
        getSupportLoaderManager().initLoader(FORECAST_LOADER_ID, null, this);
    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     *
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.forecast, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     *
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

    // (22) Override onCreateLoader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // (23) If the loader requested is our detail loader, return the appropriate CursorLoader
        switch (id) {
            case FORECAST_LOADER_ID:
                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                return new CursorLoader(this,
                        mUri,
                        WEATHER_PROJECTION,
                        null,
                        null,
                        sortOrder);
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    //  (24) Override onLoadFinished
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//      (25) Check before doing anything that the Cursor has valid data
        if (data != null) {
            data.moveToFirst();
//      (26) Display a readable data string
            long normalizedDate = data.getLong(COL_DATE_INDEX);
            String date = SunshineDateUtils.getFriendlyDateString(this, normalizedDate, false);
            dateTextView.setText(date);
//      (27) Display the weather description (using SunshineWeatherUtils)
            int weatherId = data.getInt(COL_DESCRIPTION_INDEX);
            String description = SunshineWeatherUtils.getStringForWeatherCondition(this, weatherId);
            descriptionTextView.setText(description);
//      (28) Display the high temperature
            double maxTemp = data.getDouble(COL_MAX_INDEX);
            highTextView.setText(Double.toString(maxTemp));
//      (29) Display the low temperature
            double minTemp = data.getDouble(COL_MIN_INDEX);
            lowTextView.setText(Double.toString(minTemp));
//      (30) Display the humidity
            double humidity = data.getDouble(COL_HUMIDITY_INDEX);
            humidityTextView.setText(Double.toString(humidity));
//      (31) Display the wind speed and direction
            double wind = data.getDouble(COL_WIND_INDEX);
            windTextView.setText(Double.toString(wind));
//      (32) Display the pressure
            double pressure = data.getDouble(COL_PRESSURE_INDEX);
            pressureTextView.setText(Double.toString(pressure));
//      (33) Store a forecast summary in mForecastSummary
            String highAndLowTemperature =
                    SunshineWeatherUtils.formatHighLows(this, maxTemp, minTemp);
            mForecastSummary = date + " - " + description + " - " + highAndLowTemperature;
        }
    }

    //  (34) Override onLoaderReset, but don't do anything in it yet
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}