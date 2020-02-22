package com.a6020peaks.bakingapp.ui.steps;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.a6020peaks.bakingapp.data.RecipeRepository;
import com.a6020peaks.bakingapp.data.database.StepEntry;

/**
 * Created by narko on 28/09/17.
 */

public class StepDetailsFragmentViewModel extends ViewModel {
    private RecipeRepository mRepository;
    private LiveData<StepEntry> mStep;

    public StepDetailsFragmentViewModel(RecipeRepository repository, int stepId) {
        mRepository = repository;
        mStep = mRepository.getStep(stepId);
    }

    public LiveData<StepEntry> getStep() {
        return mStep;
    }
}
