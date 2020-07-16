package com.connie.noted.data

data class User (
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var followingTags: MutableList<String>? = mutableListOf(),
    var image: String = ""
)