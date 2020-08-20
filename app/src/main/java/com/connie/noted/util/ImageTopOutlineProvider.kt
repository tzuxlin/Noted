package com.connie.noted.util

import android.graphics.Outline
import android.graphics.Rect
import android.view.View
import android.view.ViewOutlineProvider
import com.connie.noted.NotedApplication
import com.connie.noted.R

class ImageTopOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        view.clipToOutline = true
        val radius = NotedApplication.instance.resources.getDimensionPixelSize(R.dimen.radius_image)
        outline.setRoundRect(view.left, 0, view.right, view.bottom + radius, radius.toFloat())
    }
}
