package com.connie.noted

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.connie.noted.TestUtils.ClickCloseIcon
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TagDialogTest {

    companion object {
        const val NEW_TAG_NAME = "Kotlin"
    }

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun launchActivity() {
        activityRule.launchActivity(Intent())
    }



    @Test
    fun launchTagDialogFragmentAndVerifyUI() {

        /* Navigate to Tag Dialog */
        onView(withId(R.id.navigation_profileFragment)).perform(click())
        onView(withId(R.id.button_profile_add_tag)).perform(click())


        /* To add new tag from edit text view */
        val tagEditText = onView(withId(R.id.edit_tag_add2tag))
        tagEditText.perform(replaceText(NEW_TAG_NAME))

        onView(withId(R.id.button_tag_add2tag)).perform(click())


        /* Check if new tag exists */
        val chip = onView(Matchers.allOf(withText(NEW_TAG_NAME), withParent(withId(R.id.group_profile_tag))))
        chip.check(matches(isDisplayed()))


        /* Remove new tag and check it does not exist */
        chip.perform(click())
        chip.perform(ClickCloseIcon())
        chip.check(doesNotExist())

    }

}