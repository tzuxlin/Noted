package com.connie.noted.data

data class Note (
    var type: String = "",
    var contentSource: String = "",
    var title: String = "",
    var images: MutableList<String> = mutableListOf(),
    var summary: String? = null,
    var tags: MutableList<String>? = null,
    var isLiked: Boolean = false
)