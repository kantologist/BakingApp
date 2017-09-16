package com.example.femi.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Created by user on 16/09/2017.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeDescriptionListActivityTest {

    private static final String INTRODUCTION = "Recipe Introduction";



//    @Rule
//    public ActivityTestRule<RecipeDescriptionListActivity> mActivityTestRule =
//            new ActivityTestRule<>(RecipeDescriptionListActivity.class);

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecyclerViewItem_OpensRecipeListActivity() {

        onData(anything()).inAdapterView(withId(R.id.recipe_list)).atPosition(0).perform(click());


        onView(withId(R.id.recipedescription_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click())
        );

        onView(withId(R.id.recipedescription_detail)).check(matches(withText(INTRODUCTION)));
    }
}
