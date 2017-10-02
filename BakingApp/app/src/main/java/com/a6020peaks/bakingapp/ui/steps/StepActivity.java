package com.a6020peaks.bakingapp.ui.steps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.a6020peaks.bakingapp.R;

/**
 * Created by narko on 01/10/17.
 */

public class StepActivity extends AppCompatActivity {

    public static final String STEP_ID = "step_id";
    public static final String STEP_AMOUNT = "step_amount";
    public static final String STEP_REMOTE_ID = "step_remote_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        int stepId = getIntent().getIntExtra(STEP_ID, 0);
        int stepRemoteId = getIntent().getIntExtra(STEP_REMOTE_ID, 0);
        int stepAmount = getIntent().getIntExtra(STEP_AMOUNT, 0);

        ViewPager viewPager = findViewById(R.id.stepPager);
        StepSlidePagerAdapter pageAdapter = new StepSlidePagerAdapter(getSupportFragmentManager(), stepId, stepRemoteId, stepAmount);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(stepRemoteId);
    }
}
