package com.connie.noted.util

import com.connie.noted.R
import com.connie.noted.util.Util.getString

enum class CurrentFragmentType(val value: String) {
    NOTE(getString(R.string.note)),
    BOARD(getString(R.string.board)),
    EXPLORE(getString(R.string.explore)),
    PROFILE(getString(R.string.profile)),
    BOARDDETAIL(""),
    NOTEDETAIL("")

}
