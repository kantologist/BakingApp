package com.example.femi.bakingapp;

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
public class MainActivityTest {
    private static final String INGREDIENTS = "Ingredients";



    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickGridViewItem_OpensRecipeListActivity() {

        onData(anything()).inAdapterView(withId(R.id.recipe_list)).atPosition(1).perform(click());

        onView(withId(R.id.ingredient_header)).check(matches(withText(INGREDIENTS)));


    }
}
