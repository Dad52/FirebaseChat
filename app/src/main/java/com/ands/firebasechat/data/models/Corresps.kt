package com.ands.firebasechat.data.models

import java.io.Serializable

data class Corresps(
    val userName: String? = null,
    val photoUrl: String? = null,
    val lastMessage: String? = null,
    val time: String? = null,
    val userUid: String? = null
): Serializable
