package com.example.android.background.sync;

// (1) Create a class called ReminderTasks

// (2) Create a public static constant String called ACTION_INCREMENT_WATER_COUNT

// (6) Create a public static void method called executeTask
// (7) Add a Context called context and String parameter called action to the parameter list

// (8) If the action equals ACTION_INCREMENT_WATER_COUNT, call this class's incrementWaterCount

// (3) Create a private static void method called incrementWaterCount
// (4) Add a Context called context to the argument list
// (5) From incrementWaterCount, call the PreferenceUtility method that will ultimately update the water count

import android.content.Context;

import com.example.android.background.utilities.PreferenceUtilities;

public class ReminderTasks {
    public static final String ACTION_INCREMENT_WATER_COUNT = "increment_water_count";

    public static void executeTast(Context context, String action) {
        if (action.equals(ACTION_INCREMENT_WATER_COUNT)) {
            incrementWaterCount(context);
        }
    }

    private static void incrementWaterCount(Context context) {
        PreferenceUtilities.incrementWaterCount(context);
    }

}