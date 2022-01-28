package com.ands.firebasechat.data

import java.text.SimpleDateFormat
import java.util.*

object GetCurrentTime {

        fun getCurrentDate(): String {
            val formatter = SimpleDateFormat("dd.MM.YY", Locale.getDefault())
            return formatter.format(Calendar.getInstance().time)
        }

        fun getCurrentTime(): String {
            val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
            return formatter.format(Calendar.getInstance().time)
        }

}