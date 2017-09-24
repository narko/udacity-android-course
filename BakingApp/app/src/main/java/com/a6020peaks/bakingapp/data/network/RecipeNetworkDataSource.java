package com.a6020peaks.bakingapp.data.network;

/**
 * Created by narko on 19/09/17.
 */

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.a6020peaks.bakingapp.AppExecutors;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Provides an API for doing all operations with the server data
 */
public class RecipeNetworkDataSource {
    private static final String TAG = RecipeNetworkDataSource.class.getSimpleName();
    private static final String BAKING_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static RecipeNetworkDataSource sInstance;
    private static final Object LOCK = new Object();

    private Context mContext;
    private AppExecutors mExecutors;
    private MutableLiveData<BakingResponse> mRecipeData;


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
        Log.d(TAG, "fetch baking data started");
        mExecutors.networkIO().execute(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(BAKING_URL).build();
            try {
                Response okResponse = client.newCall(request).execute();
                BakingResponse bakingResponse = new BakingJsonParser().parse(okResponse.body().string());
                Log.d(TAG, "Finished parsing JSON data");
                if (bakingResponse != null) {
                    mRecipeData.postValue(bakingResponse);
                }
            } catch (IOException e) {
                Log.d(TAG, "error retrieving baking data: " + e.getMessage());
            } catch (JSONException e) {
                Log.d(TAG, "error parsing JSON data: " + e.getMessage());
            }
        });
    }

    public MutableLiveData<BakingResponse> getRecipeData() {
        return mRecipeData;
    }
}
