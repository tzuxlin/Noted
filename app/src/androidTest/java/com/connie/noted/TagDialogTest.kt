package com.connie.noted

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class FragmentNavTest {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)


    @Test
    fun navigate2NoteFragmentAndVerifyUI() {

        // Note Fragment is default Fragment, verify toolbar text when launch Activity

        activityRule.launchActivity(Intent())

        Espresso.onView(ViewMatchers.withId(R.id.text_toolbar_title))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.note)))

        // Click bottom navigation button and verify toolbar text

        Espresso.onView(ViewMatchers.withId(R.id.navigation_noteFragment))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.text_toolbar_title))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.note)))


    }


    @Test
    fun navigate2BoardFragmentAndVerifyUI() {

        activityRule.launchActivity(Intent())

        Espresso.onView(ViewMatchers.withId(R.id.navigation_boardFragment))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.text_toolbar_title))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.board)))

    }

    @Test
    fun navigate2ExploreFragmentAndVerifyUI() {

        activityRule.launchActivity(Intent())

        Espresso.onView(ViewMatchers.withId(R.id.navigation_exploreFragment))
            .perform(ViewActions.click())


        Espresso.onView(ViewMatchers.withId(R.id.button_bottom_cancel))
            .inRoot(isDialog())
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.text_toolbar_title))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.explore)))

    }

    @Test
    fun navigate2ProfileFragmentAndVerifyUI() {

        activityRule.launchActivity(Intent())

        Espresso.onView(ViewMatchers.withId(R.id.navigation_profileFragment))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.text_toolbar_title))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.profile)))

    }

}