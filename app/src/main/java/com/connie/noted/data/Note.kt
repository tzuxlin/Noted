package com.connie.noted.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note (
    var id: String = "",
    var url: String = "",
    var createdTime: Long = -1,
    var type: String = "",
    var contentSource: String = "",
    var title: String = "",
    var images: MutableList<String> = mutableListOf(),
    var summary: String? = null,
    var tags: MutableList<String>? = null,
    var isLiked: Boolean = false
): Parcelable