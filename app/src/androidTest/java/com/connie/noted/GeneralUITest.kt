package com.connie.noted

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GeneralUITest {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun launchActivity() {
        activityRule.launchActivity(Intent())
    }



    @Test
    fun testNavDrawerIsDisplayed() {
        onView(withId(R.id.icon_nav_drawer)).perform(click()).check(matches(isDisplayed()))
    }

    @Test
    fun testChangeLayoutIcon() {
        onView(withId(R.id.icon_2change_view_type)).perform(click())
            .check(matches(isDisplayed()))
    }
}