package com.connie.noted

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class FragmentNavTest {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun launchActivity() {
        activityRule.launchActivity(Intent())
    }

    @Test
    fun navigate2NoteFragmentAndVerifyUI() {

        /* Click bottom navigation view: Note button */
        onView(withId(R.id.navigation_noteFragment)).perform(click()).check(matches(isDisplayed()))

        /* Check toolbar title */
        checkToolbarTitle(R.string.note)

    }


    @Test
    fun navigate2BoardFragmentAndVerifyUI() {

        /* Click bottom navigation view: Board button */
        onView(withId(R.id.navigation_boardFragment)).perform(click()).check(matches(isDisplayed()))

        /* Check toolbar title */
        checkToolbarTitle(R.string.board)

    }

    @Test
    fun navigate2ExploreFragmentAndVerifyUI() {

        /* Click bottom navigation view: Explore button */
        onView(withId(R.id.navigation_exploreFragment)).perform(click())

        try {

            /* Close dialog and check toolbar title*/
            onView(withId(R.id.button_bottom_cancel)).inRoot(isDialog()).perform(click())
            checkToolbarTitle(R.string.explore)

        } catch (e: NoMatchingViewException) {

            /* No dialog, check toolbar title */
            checkToolbarTitle(R.string.explore)

        }


    }

    @Test
    fun navigate2ProfileFragmentAndVerifyUI() {

        /* Click bottom navigation view: Profile button */
        onView(withId(R.id.navigation_profileFragment)).perform(click())
            .check(matches(isDisplayed()))

        /* Check toolbar title */
        checkToolbarTitle(R.string.profile)

    }

    private fun checkToolbarTitle(resource: Int) {
        onView(withId(R.id.text_toolbar_title)).check(matches(withText(resource)))
    }


}





