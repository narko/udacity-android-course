package com.example.android.sunshine.sync;

import android.content.Context;
import android.content.Intent;

public class SunshineSyncUtils {
// (9) Create a class called SunshineSyncUtils
    //  (10) Create a public static void method called startImmediateSync
    //  (11) Within that method, start the SunshineSyncIntentService
    public static void startImmediateSync(Context context) {
        Intent intent = new Intent(context, SunshineSyncIntentService.class);
        context.startService(intent);
    }
}