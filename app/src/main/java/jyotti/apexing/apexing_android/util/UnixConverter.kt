package jyotti.apexing.apexing_android.util

import java.text.SimpleDateFormat
import java.util.*

class UnixConverter {
    fun getTimestampToDate(timestampStr: String): String {
        val timestamp = timestampStr.toLong()
        val date = Date(timestamp * 1000L)
        val sdf = SimpleDateFormat("MM월 dd일 HH시 mm분", Locale.KOREA)
        sdf.timeZone = TimeZone.getTimeZone("GMT+9")
        return sdf.format(date)
    }
}
