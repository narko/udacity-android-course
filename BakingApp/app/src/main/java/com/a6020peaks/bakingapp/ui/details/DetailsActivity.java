package com.a6020peaks.bakingapp.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.a6020peaks.bakingapp.R;
import com.a6020peaks.bakingapp.data.database.StepEntry;
import com.a6020peaks.bakingapp.ui.details.RecipeDetailsFragment.OnStepItemClickListener;
import com.a6020peaks.bakingapp.ui.steps.StepActivity;
import com.a6020peaks.bakingapp.ui.steps.StepSlidePagerAdapter;
import com.a6020peaks.bakingapp.utils.InjectorUtils;

/**
 * Created by narko on 02/10/17.
 */

public class DetailsActivity extends AppCompatActivity implements OnStepItemClickListener {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    public static final String RECIPE_ID = "recipe_id";
    public static final String FRAGMENT = "fragment";
    private boolean mTwoPane = false;
    private ViewPager mViewPager;
    private RecipeDetailsFragmentViewModel mViewModel;
    private int mRecipeId;
    private RecipeDetailsFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getResources().getBoolean(R.bool.isTablet)) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTwoPane = findViewById(R.id.stepPager) != null;
        if (savedInstanceState != null && savedInstanceState.containsKey(FRAGMENT) && savedInstanceState.containsKey(RECIPE_ID)) {
            mFragment = (RecipeDetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT);
            mRecipeId = savedInstanceState.getInt(RECIPE_ID);
        } else {
            mRecipeId = getIntent().getIntExtra(RECIPE_ID, 0);
            mFragment = RecipeDetailsFragment.create(mRecipeId);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.details_fragment, mFragment)
                .commit();

        if (mTwoPane) {
            RecipeDetailsFragmentViewModelFactory factory = InjectorUtils.provideRecipeDetailsFragmentViewModelFactory(this, mRecipeId);
            mViewModel = ViewModelProviders.of(this, factory).get(RecipeDetailsFragmentViewModel.class);
            mViewModel.getSteps().observe(this, steps -> {
                mViewPager = findViewById(R.id.stepPager);
                StepEntry firstStep = steps.get(0);
                StepSlidePagerAdapter pagerAdapter = new StepSlidePagerAdapter(getSupportFragmentManager(), firstStep.getId(), firstStep.getRemoteId(), steps.size());
                mViewPager.setAdapter(pagerAdapter);
            });
        }
    }

    @Override
    public void onStepItemClick(StepEntry item, int stepAmount) {
        if (!mTwoPane) {
            Log.d(TAG, "Starting activity for step " + item.getId());
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(StepActivity.STEP_ID, item.getId());
            intent.putExtra(StepActivity.STEP_REMOTE_ID, item.getRemoteId());
            intent.putExtra(StepActivity.STEP_AMOUNT, stepAmount);
            startActivity(intent);
        } else {
            mViewPager.setCurrentItem(item.getRemoteId());
            mViewPager.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(RECIPE_ID, mRecipeId);
        getSupportFragmentManager().putFragment(outState, FRAGMENT, mFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
