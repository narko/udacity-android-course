package com.a6020peaks.bakingapp.ui.steps;


import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a6020peaks.bakingapp.R;
import com.a6020peaks.bakingapp.data.database.StepEntry;
import com.a6020peaks.bakingapp.utils.InjectorUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
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
import com.squareup.picasso.Picasso;

/**
 * Created by narko on 27/09/17.
 */

public class StepDetailsFragment extends Fragment {
    private static final String TAG = StepDetailsFragment.class.getSimpleName();
    private static final String STEP_ID = "step_id";
    private StepDetailsFragmentViewModel mViewModel;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private ImageView mThumbView;
    private final String position = "position";
    private long videoPosition = C.TIME_UNSET;
    private View mRootView;
    private StepEntry mStep;


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
        if (savedInstanceState != null && savedInstanceState.containsKey(position)) {
            videoPosition = savedInstanceState.getLong(position, C.TIME_UNSET);
        }
        int stepId = getArguments().getInt(STEP_ID);
        mRootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        StepDetailsFragmentViewModelFactory factory = InjectorUtils.provideStepDetailsFragmentViewModelFactory(getContext(), stepId);
        mViewModel = ViewModelProviders.of(this, factory).get(StepDetailsFragmentViewModel.class);
        registerStepObserver();

        return mRootView;
    }

    private void registerStepObserver() {
        mViewModel.getStep().observe(this, step -> {
            mStep = step;
            updateView();
        });
    }

    private void updateView() {

        mPlayerView = mRootView.findViewById(R.id.view_player);
        mThumbView = mRootView.findViewById(R.id.view_thumb);
        if (!TextUtils.isEmpty(mStep.getVideoUrl())) {
            Uri mediaUri = Uri.parse(mStep.getVideoUrl());
            initializePlayer();
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            Log.d(TAG, "Playing " + mediaUri.toString());
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.seekTo(videoPosition);
            mPlayerView.setVisibility(View.VISIBLE);
            mThumbView.setVisibility(View.GONE);
            mPlayerView.setPlayer(mExoPlayer);
        } else { // No video available in JSON
            mPlayerView.setVisibility(View.GONE);
            mThumbView.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mStep.getThumbnailUrl())) {
                Picasso.with(getContext()).load(mStep.getThumbnailUrl()).error(R.drawable.default_placeholder).into(mThumbView);
            } else {
                Picasso.with(getContext()).load(R.drawable.default_placeholder).into(mThumbView);
            }
        }

        if (mRootView.findViewById(R.id.step_info) != null) {
            TextView title = mRootView.findViewById(R.id.step_title);
            title.setText(mStep.getShortDescription());
            TextView desc = mRootView.findViewById(R.id.step_desc);
            desc.setText(mStep.getDescription());
        }
    }

    private void initializePlayer() {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()), trackSelector, loadControl);
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
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        initializePlayer();
        registerStepObserver();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        saveState();
        releasePlayer();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putLong(position, videoPosition);
    }

    private void saveState() {
        if (mExoPlayer != null) {
            videoPosition = mExoPlayer.getCurrentPosition();
        }
    }
}
