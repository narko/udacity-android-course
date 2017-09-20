package com.a6020peaks.bakingapp.data.network;

/**
 * Created by narko on 19/09/17.
 */

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.a6020peaks.bakingapp.AppExecutors;

/**
 * Provides an API for doing all operations with the server data
 */
public class RecipeNetworkDataSource {
    private static final String TAG = RecipeNetworkDataSource.class.getSimpleName();
    private static RecipeNetworkDataSource sInstance;
    private static final Object LOCK = new Object();

    private Context mContext;
    private AppExecutors mExecutors;
    private MutableLiveData<RecipeResponse> mRecipeData;


    private RecipeNetworkDataSource(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mRecipeData = new MutableLiveData<>();
    }

    public RecipeNetworkDataSource getInstance(Context context, AppExecutors executors) {
        Log.d(TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipeNetworkDataSource(context, executors);
                Log.d(TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    /**
     * Gets the newest recipes from the server
     */
    void fetchRecipeData() {
        // TODO service call and data parsing
    }

    public MutableLiveData<RecipeResponse> getRecipeData() {
        return mRecipeData;
    }
}
