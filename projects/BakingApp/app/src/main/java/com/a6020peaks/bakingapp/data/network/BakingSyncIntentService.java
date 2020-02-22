package com.a6020peaks.bakingapp.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.a6020peaks.bakingapp.utils.InjectorUtils;

/**
 * Created by narko on 24/09/17.
 */

public class BakingSyncIntentService extends IntentService {
    private static final String TAG = BakingSyncIntentService.class.getSimpleName();

    public BakingSyncIntentService() {
        super("BakingSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Intent service started");
        BakingNetworkDataSource networkDataSource = InjectorUtils.provideNetworkDataSource(getApplicationContext());
        networkDataSource.fetchRecipeData();
    }
}
