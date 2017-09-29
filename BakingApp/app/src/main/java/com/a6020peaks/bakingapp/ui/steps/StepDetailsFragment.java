package com.a6020peaks.bakingapp.ui.steps;


import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a6020peaks.bakingapp.R;
import com.a6020peaks.bakingapp.data.database.StepEntry;
import com.a6020peaks.bakingapp.utils.InjectorUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static android.support.v4.media.session.MediaSessionCompat.*;

/**
 * Created by narko on 27/09/17.
 */

public class StepDetailsFragment extends Fragment implements ExoPlayer.EventListener {
    private static final String TAG = StepDetailsFragment.class.getSimpleName();
    private static final String STEP_ID = "step_id";
    private StepDetailsFragmentViewModel mViewModel;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
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
        initializeMediaSession();

        StepDetailsFragmentViewModelFactory factory = InjectorUtils.provideStepDetailsFragmentViewModelFactory(getContext(), stepId);
        mViewModel = ViewModelProviders.of(this, factory).get(StepDetailsFragmentViewModel.class);
        mViewModel.getStep().observe(this, step -> {
            updateView(rootView, step);
        });

        return rootView;
    }

    private void updateView(View rootView, StepEntry step) {
        mPlayerView = rootView.findViewById(R.id.view_player);

        Uri videoUri = Uri.parse(step.getVideoUrl());
        Log.d(TAG, "Playing " + videoUri.toString());
        initializeMediaPlayer(videoUri);

        TextView desc = rootView.findViewById(R.id.step_desc);
        desc.setText(step.getDescription());
    }

    private void initializeMediaSession() {
        // Create a MediaSession
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Set flags
        mMediaSession.setFlags(FLAG_HANDLES_MEDIA_BUTTONS |
                FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player if app isn't visible
        mMediaSession.setMediaButtonReceiver(null);

        // Set PlaybackState
        mStateBuilder = new PlaybackStateCompat.Builder().setActions(PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mMediaSession.setPlaybackState(mStateBuilder.build());

        // Set up callbacks
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }

    private void initializeMediaPlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    private class MySessionCallback extends Callback {

        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }
    }
}
