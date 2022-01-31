package com.ands.firebasechat.data

import java.text.SimpleDateFormat
import java.util.*

object GetCurrentTime {



        fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val formatter = SimpleDateFormat("dd.MM.YY", Locale.getDefault())
            return formatter.format(Calendar.getInstance().time)
        }

//        fun getCurrentTime(): String {
//            val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
//            return formatter.format(Calendar.getInstance().time)
//        }

    fun getCurrentDateString(millis: Long): String {
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        return simpleDateFormat.format(millis).toString()
    }

        fun getCurrentTimeString(millis: Long): String {
            val simpleDateFormat = SimpleDateFormat("h:mm a")
            return simpleDateFormat.format(millis).toString()
        }

        fun getCurrentDateInMillis(): String {
            val calendar = Calendar.getInstance()
            return calendar.timeInMillis.toString()
//            val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
//            return formatter.format(Calendar.getInstance().time)
        }

}