package com.connie.noted.board

import com.connie.noted.NotedApplication
import com.connie.noted.R

enum class BoardTypeFilter (val value: String) {

    ALL(NotedApplication.instance.resources.getString(R.string.all)),
    MINE(NotedApplication.instance.resources.getString(R.string.mine)),
    SAVED(NotedApplication.instance.resources.getString(R.string.saved))

}