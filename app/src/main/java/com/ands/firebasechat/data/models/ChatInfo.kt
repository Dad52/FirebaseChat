package com.ands.firebasechat.data.models

import java.io.Serializable

data class ChatInfo(
    val displayName: String? = null,
    val lastMessage: String? = null,
    val lastMessageTime: String? = null,
    val lastMessageUid: String? = null,
    var usersInfo: Users? = null
        //убрать и брать инфу из users или key
): Serializable
