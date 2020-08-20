package app.appworks.school.stylish.component

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import app.appworks.school.stylish.R
import app.appworks.school.stylish.StylishApplication

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class ProfileAvatarOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        view.clipToOutline = true
        val radius = StylishApplication.instance.resources.getDimensionPixelSize(R.dimen.radius_profile_avatar)
        outline.setOval(0, 0, radius, radius)
    }
}
