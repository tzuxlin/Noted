package com.connie.noted.util

import com.connie.noted.R
import com.connie.noted.util.Util.getString

enum class DialogBoxMessageType(val message: String) {
    SAVED_BOARD("已儲存"),
    UNSAVED_BOARD("取消儲存"),
    SHARED("分享成功"),
    EDITED("修改成功"),
    NEW_NOTE("新筆記已新增"),
    LOADING_NOTE("呼嚕嚕⋯"),
    NOT_FOUND("找不到分類板")
}
