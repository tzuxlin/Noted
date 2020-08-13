package com.connie.noted

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.chip.Chip
import org.hamcrest.Matcher

object TestUtils {

    class ClickCloseIcon: ViewAction {

        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isAssignableFrom(Chip::class.java)
        }

        override fun getDescription(): String {
            return "click drawable "
        }

        override fun perform(uiController: UiController, view: View) {
            val chip = view as Chip
            chip.performCloseIconClick()
        }


    }
}