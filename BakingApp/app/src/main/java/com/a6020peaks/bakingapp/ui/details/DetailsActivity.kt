package com.a6020peaks.bakingapp.ui.details

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.a6020peaks.bakingapp.R
import com.a6020peaks.bakingapp.data.database.StepEntry
import com.a6020peaks.bakingapp.ui.details.RecipeDetailsFragment.OnStepItemClickListener
import com.a6020peaks.bakingapp.ui.steps.StepActivity

/**
 * Created by narko on 25/09/17.
 */

class DetailsActivity : AppCompatActivity(), OnStepItemClickListener {
    companion object {
        private val TAG = DetailsActivity.javaClass.simpleName
        val RECIPE_ID = "recipe_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val recipeId = intent.getIntExtra(RECIPE_ID, 0)
        val manager = supportFragmentManager
        manager.beginTransaction()
                .add(R.id.details_fragment, RecipeDetailsFragment.create(recipeId))
                .commit()
    }

    override fun onStepItemClick(item: StepEntry) {
        Log.d(TAG, "Starting activity for step " + item.id)
        val intent = Intent(this, StepActivity::class.java)
        intent.putExtra(StepActivity.STEP_ID, item.id)
        startActivity(intent)
    }
}
