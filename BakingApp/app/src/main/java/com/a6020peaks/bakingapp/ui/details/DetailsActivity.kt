package com.a6020peaks.bakingapp.ui.details

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.FrameLayout
import com.a6020peaks.bakingapp.R
import com.a6020peaks.bakingapp.data.database.StepEntry
import com.a6020peaks.bakingapp.ui.details.RecipeDetailsFragment.OnStepItemClickListener
import com.a6020peaks.bakingapp.ui.steps.StepActivity
import com.a6020peaks.bakingapp.ui.steps.StepSlidePagerAdapter
import com.a6020peaks.bakingapp.utils.InjectorUtils

/**
 * Created by narko on 25/09/17.
 */

class DetailsActivity : AppCompatActivity(), OnStepItemClickListener {
    companion object {
        private val TAG = DetailsActivity.javaClass.simpleName
        val RECIPE_ID = "recipe_id"
    }

    var mTwoPane = false
    lateinit var mViewPager: ViewPager
    lateinit var mViewModel: RecipeDetailsFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (resources.getBoolean(R.bool.isTablet)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        setContentView(R.layout.activity_details)

        mTwoPane = findViewById<FrameLayout>(R.id.stepPager) != null

        val recipeId = intent.getIntExtra(RECIPE_ID, 0)
        val manager = supportFragmentManager
        manager.beginTransaction()
                .replace(R.id.details_fragment, RecipeDetailsFragment.create(recipeId))
                .commit()

        if (mTwoPane) {
            val factory = InjectorUtils.provideRecipeDetailsFragmentViewModelFactory(this, recipeId)
            mViewModel = ViewModelProviders.of(this, factory).get(RecipeDetailsFragmentViewModel::class.java)
            mViewModel.steps.observe(this, Observer<List<StepEntry>> {
                mViewPager = findViewById<ViewPager>(R.id.stepPager)
                val firstStep = it?.get(0)
                val pageAdapter = StepSlidePagerAdapter(supportFragmentManager, firstStep!!.id, firstStep.remoteId, it.size)
                mViewPager.adapter = pageAdapter
            })
        }
    }

    override fun onStepItemClick(item: StepEntry, stepAmount: Int) {
        if (!mTwoPane) {
            Log.d(TAG, "Starting activity for step " + item.id)
            val intent = Intent(this, StepActivity::class.java)
            intent.putExtra(StepActivity.STEP_ID, item.id)
            intent.putExtra(StepActivity.STEP_REMOTE_ID, item.remoteId)
            intent.putExtra(StepActivity.STEP_AMOUNT, stepAmount)
            startActivity(intent)
        } else {
            mViewPager.currentItem = item.remoteId
            mViewPager.adapter.notifyDataSetChanged()
        }
    }
}
