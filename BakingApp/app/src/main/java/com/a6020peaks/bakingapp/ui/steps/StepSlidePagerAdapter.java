package com.a6020peaks.bakingapp.ui.steps;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by narko on 28/09/17.
 */

public class StepSlidePagerAdapter extends FragmentStatePagerAdapter {
    private int stepAmount;
    private int stepId;
    private int stepRemoteId;

    public StepSlidePagerAdapter(FragmentManager fm, int stepId, int stepRemoteId, int stepAmount) {
        super(fm);
        this.stepId = stepId;
        this.stepRemoteId = stepRemoteId;
        this.stepAmount = stepAmount;
    }

    @Override
    public Fragment getItem(int position) {
        return StepDetailsFragment.create(stepId - stepRemoteId + position);
    }

    @Override
    public int getCount() {
        return stepAmount;
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof StepDetailsFragment) {
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }
    }
}
