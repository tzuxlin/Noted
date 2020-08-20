package com.connie.noted.util

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import com.connie.noted.NotedApplication
import com.connie.noted.R

class ImageLeftOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        view.clipToOutline = true
        val radius = NotedApplication.instance.resources.getDimensionPixelSize(R.dimen.radius_image)
        outline.setRoundRect(0, view.top, view.right + radius, view.bottom, radius.toFloat())
    }
}