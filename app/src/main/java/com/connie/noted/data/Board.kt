package com.connie.noted.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Board (
    var createdBy: String = "",
    var creatorName: String = "",
    var savedBy: MutableList<String?> = mutableListOf(),
    var title: String = "",
    var images: MutableList<String?> = mutableListOf(),
    var tags: MutableList<String?> = mutableListOf(),
    var isLiked: Boolean = false,
    var isPublic: Boolean = false,
    var notes: MutableList<String?> = mutableListOf()
): Parcelable