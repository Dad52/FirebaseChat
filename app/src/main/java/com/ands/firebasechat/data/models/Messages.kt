package com.ands.firebasechat.data.models

import android.graphics.Bitmap

data class Messages(
        val user: String? = null,//user: Users???
        val userUid: String? = null,
        val message: String? = null,
//        val userIcon: Bitmap? = null
)
