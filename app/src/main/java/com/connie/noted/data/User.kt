package com.connie.noted.data

data class User (
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var followingTags: List<String>? = listOf(),
    var image: String = ""
)