package com.a6020peaks.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.a6020peaks.bakingapp.ui.list.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by narko on 01/10/17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeListTest {
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule(MainActivity.class);

    @Test
    public void clickRecipe_OpensDetailsActivity() throws Exception {
        onView(withId(R.id.recipe_rv)).perform(actionOnItemAtPosition(0, click()));
        // The test seems to run fine on Nexus 4, but not on other devices. Let's delay a bit.
        Thread.sleep(2000);
        onView(withId(R.id.recipe_details_rv)).check(matches(isDisplayed()));
    }
}
