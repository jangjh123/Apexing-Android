package jyotti.apexing.apexing_android.util

import android.content.Context
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun getThumbnail(
    context: Context,
    srcUri: String
) = Glide.with(context)
    .load(srcUri)
    .sizeMultiplier(0.1f)
    .centerCrop()

fun getTimestampToDate(timestampStr: String): String {
    val timestamp = timestampStr.toLong()
    val date = Date(timestamp * 1000L)
    val sdf = SimpleDateFormat("MM월 dd일 HH시 mm분", Locale.KOREA)
    sdf.timeZone = TimeZone.getTimeZone("GMT+9")
    return sdf.format(date)
}