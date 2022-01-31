package com.ands.firebasechat.data.models

import java.io.Serializable

data class Messages(
        val user: String? = null,
        val userUid: String? = null,
        val message: String? = null,
        val time: String? = null,
        var messageId: String? = null
): Serializable
