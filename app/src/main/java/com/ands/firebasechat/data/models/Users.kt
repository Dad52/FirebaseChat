package com.ands.firebasechat.data.models

import java.io.Serializable

data class Users(
        val uid: String? = null,
        val name: String? = null,
        val photoUrl: String? = null,
        val email: String? = null,
        val phoneNumber: String? = null,
        val customName: String? = null,
        val accCode: String? = null
): Serializable
