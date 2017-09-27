package com.a6020peaks.bakingapp.ui.details

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.a6020peaks.bakingapp.R

/**
 * Created by narko on 25/09/17.
 */

class DetailsActivity : AppCompatActivity() {
    companion object {
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
}
