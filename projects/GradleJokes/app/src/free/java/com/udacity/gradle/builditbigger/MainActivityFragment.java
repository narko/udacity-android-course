package com.udacity.gradle.builditbigger;

import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends MainActivityFragmentBase {
    private final String AD_UNIT = "ca-app-pub-3940256099942544/1033173712";
    private InterstitialAd interstitialAd;

    @Override
    protected void customizeLayout(View root) {
        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        // Set up interstitial
        interstitialAd = new InterstitialAd(getContext());
        interstitialAd.setAdUnitId(AD_UNIT);
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                MainActivityFragment.super.displayJoke();
                // Load the next interstitial
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    @Override
    protected void displayJoke() {
        displayInterstitial();
    }

    private void displayInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Log.d(TAG, "The interstitial was not loaded yet");
        }
    }
}
