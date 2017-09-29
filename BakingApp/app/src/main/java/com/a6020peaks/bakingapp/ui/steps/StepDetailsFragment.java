package com.a6020peaks.bakingapp.ui.steps;


import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a6020peaks.bakingapp.R;
import com.a6020peaks.bakingapp.data.database.StepEntry;
import com.a6020peaks.bakingapp.utils.InjectorUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by narko on 27/09/17.
 */

public class StepDetailsFragment extends Fragment {
    private static final String TAG = StepDetailsFragment.class.getSimpleName();
    private static final String STEP_ID = "step_id";
    private StepDetailsFragmentViewModel mViewModel;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;


    public static StepDetailsFragment create(int stepId) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(STEP_ID, stepId);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!(getArguments() != null && getArguments().containsKey(STEP_ID))) {
            throw new RuntimeException("No step id given");
        }
        int stepId = getArguments().getInt(STEP_ID);
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        StepDetailsFragmentViewModelFactory factory = InjectorUtils.provideStepDetailsFragmentViewModelFactory(getContext(), stepId);
        mViewModel = ViewModelProviders.of(this, factory).get(StepDetailsFragmentViewModel.class);
        mViewModel.getStep().observe(this, step -> {
            updateView(rootView, step);
        });

        return rootView;
    }

    private void updateView(View rootView, StepEntry step) {
        mPlayerView = rootView.findViewById(R.id.view_player);
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);


            // Set the ExoPlayer.EventListener to this activity.
            //mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            Uri mediaUri = Uri.parse(step.getVideoUrl());
            boolean loadMediaUri = false;
            if (mediaUri == null) { // No video available in JSON
                mediaUri = Uri.parse(step.getThumbnailUrl());
                if (mediaUri == null) {
                    Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.default_placeholder);
                    mPlayerView.setDefaultArtwork(image);
                } else {
                    loadMediaUri = true;
                }
            } else {
                loadMediaUri = true;
            }
            if (loadMediaUri) {
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                        new DefaultDataSourceFactory(getContext(), userAgent),
                        new DefaultExtractorsFactory(), null, null);
                mExoPlayer.prepare(mediaSource);
                Log.d(TAG, "Playing " + mediaUri.toString());
                mExoPlayer.setPlayWhenReady(true);
            }
        }

        if (rootView.findViewById(R.id.step_info) != null) {
            TextView title = rootView.findViewById(R.id.step_title);
            title.setText(step.getShortDescription());
            TextView desc = rootView.findViewById(R.id.step_desc);
            desc.setText(step.getDescription());
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
