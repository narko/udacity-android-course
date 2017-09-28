package com.a6020peaks.bakingapp.ui.steps;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a6020peaks.bakingapp.R;
import com.a6020peaks.bakingapp.data.database.StepEntry;
import com.a6020peaks.bakingapp.utils.InjectorUtils;

/**
 * Created by narko on 27/09/17.
 */

public class StepDetailsFragment extends Fragment {
    private static final String STEP_ID = "step_id";
    private StepDetailsFragmentViewModel mViewModel;


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

        StepDetailsFragmentViewModelFactory factory = InjectorUtils.provideStepDetailsFragmentViewModelFactory(getContext(), stepId);
        mViewModel = ViewModelProviders.of(this, factory).get(StepDetailsFragmentViewModel.class);
        mViewModel.getStep().observe(this, step -> {
            updateView(rootView, step);
        });

        return rootView;
    }

    private void updateView(View rootView, StepEntry step) {
        TextView desc = rootView.findViewById(R.id.step_desc);
        desc.setText(step.getDescription());
    }
}
