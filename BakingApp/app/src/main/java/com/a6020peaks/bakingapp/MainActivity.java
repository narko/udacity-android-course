package com.a6020peaks.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.a6020peaks.bakingapp.model.Recipe;
import com.a6020peaks.bakingapp.ui.RecipeAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private List<Recipe> recipes; // TODO replace by cursor


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recipe_rv);
        initializeRecipes(); // TODO replace by cursor
        mRecipeAdapter = new RecipeAdapter(recipes, this);
        mRecyclerView.setAdapter(mRecipeAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeRecipes() {
        recipes = new ArrayList<>();
        recipes.add(new Recipe("Nutella Pie"));
        recipes.add(new Recipe("Brownies"));
        recipes.add(new Recipe("Yellow Cake"));
    }

    @Override
    public void onItemClick(Recipe item) {

    }
}
