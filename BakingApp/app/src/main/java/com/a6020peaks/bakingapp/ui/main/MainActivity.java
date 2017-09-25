package com.a6020peaks.bakingapp.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.a6020peaks.bakingapp.R;
import com.a6020peaks.bakingapp.data.database.RecipeEntry;
import com.a6020peaks.bakingapp.utils.InjectorUtils;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private MainActivityViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recipe_rv);
        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        MainViewModelFactory factory = InjectorUtils.provideMainViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);
        mViewModel.getRecipes().observe(this, recipeEntries -> {
            mRecipeAdapter.swapData(recipeEntries);
        });
    }

    @Override
    public void onItemClick(RecipeEntry item) {

    }
}
