package com.usefulmemo.memo.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class Util {
    companion object{

        fun getUnixTime(): Long {
            return System.currentTimeMillis()
        }

        private fun getYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

        private fun getMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1

        private fun getDay(): Int = Calendar.getInstance().get(Calendar.DATE)

        fun getDate() : String = String.format("%d. %d. %d", getYear(), getMonth(), getDay())

        @SuppressLint("SimpleDateFormat")
        fun formatDate(regDate : Long) : String{
            val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            val monthFormat = SimpleDateFormat("MM", Locale.getDefault())
            val dayFormat = SimpleDateFormat("dd", Locale.getDefault())

            val year = yearFormat.format(regDate).toInt()
            val month = monthFormat.format(regDate).toInt()
            val day = dayFormat.format(regDate).toInt()

            return String.format("%d. %d. %d", year,month,day)
        }

        fun formatTimeWithMarker(regDate : Long) : String{
            val dateFormat = SimpleDateFormat("a h:mm", Locale.getDefault())
            return dateFormat.format(regDate)
        }

    }
}