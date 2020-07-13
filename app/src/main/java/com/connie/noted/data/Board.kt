package com.connie.noted.data


data class Board (
    var createdBy: String = "",
    var title: String = "",
    var images: MutableList<String> = mutableListOf(),
    var tags: MutableList<String>? = null,
    var isLiked: Boolean = false,
    var isPublic: Boolean = false,
    var notes: MutableList<Note> = mutableListOf()
)