package com.connie.noted

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class EspressoTest {

    @Rule
    @JvmField var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)


    @Test
    fun testNavDrawerIsDisplayed() {

        activityRule.launchActivity(Intent())

        Espresso.onView(ViewMatchers.withId(R.id.icon_nav_drawer))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testChangeLayoutIcon() {

        // launch desired activity
        activityRule.launchActivity(Intent())

        Espresso.onView(ViewMatchers.withId(R.id.icon_2change_view_type))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}