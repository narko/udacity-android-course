package com.a6020peaks.bakingapp.ui.steps

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.a6020peaks.bakingapp.R

class StepActivity : AppCompatActivity() {

    companion object {
        val STEP_ID = "step_id";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step)
    }
}
