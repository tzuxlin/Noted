package com.connie.noted.util

import com.connie.noted.R
import com.connie.noted.util.Util.getString

enum class CurrentFilterType(val type: String) {
    ALL("All"),
    LIKED("Liked"),
    ARTICLE("Article"),
    VIDEO("Video"),
    LOCATION("Location")
}
