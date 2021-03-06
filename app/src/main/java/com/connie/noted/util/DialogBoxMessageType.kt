package com.connie.noted.util


enum class DialogBoxMessageType(val message: String) {
    SAVED_BOARD("已儲存"),
    UNSAVED_BOARD("取消儲存"),
    ERROR("上傳失敗"),
    SHARED("分享成功"),
    EDITED("修改成功"),
    NEW_NOTE("新筆記已新增"),
    NEW_BOARD("新增成功"),
    LOADING("呼嚕嚕⋯"),
    NOT_FOUND("找不到分類板")
}
