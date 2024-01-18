package jyotti.apexing.apexing_android.util

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Utils {

    private val dec = DecimalFormat("###,###,###")

    fun getThumbnailWithCenterCrop(
        context: Context,
        imageUrl: String
    ) = Glide.with(context)
        .load(imageUrl)
        .sizeMultiplier(0.1f)
        .centerCrop()

    fun getThumbnail(context: Context, imageUrl: String) = Glide.with(context)
        .load(imageUrl)
        .sizeMultiplier(0.1f)

    fun getThumbnail(context: Context, imageUrl: Int) = Glide.with(context)
        .load(imageUrl)
        .sizeMultiplier(0.1f)

    fun setGradientText(
        textView: TextView,
        startColor: Int,
        endColor: Int
    ) {
        val paint = textView.paint
        val width = paint.measureText(textView.text as String?)
        val shader = LinearGradient(
            0f, 0f, width, textView.textSize,
            listOf(startColor, endColor).toIntArray(),
            null,
            Shader.TileMode.CLAMP
        )
        textView.paint.shader = shader
    }

    fun formatAmount(amount: Int): String = dec.format(amount)

    fun getTimestampToDate(timestampStr: String): String {
        val timestamp = timestampStr.toLong()
        val date = Date(timestamp * 1000L)
        val sdf = SimpleDateFormat("MM월 dd일 HH시 mm분", Locale.KOREA)
        sdf.timeZone = TimeZone.getTimeZone("GMT+9")
        return sdf.format(date)
    }
}

inline fun ViewModel.getCoroutineExceptionHandler(
    crossinline onUnknownHostException: suspend () -> Unit,
    noinline onElse: (suspend (Throwable) -> Unit)? = null
): CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    viewModelScope.launch {
        when (throwable) {
            is UnknownHostException -> {
                onUnknownHostException()
            }

            else -> {
                onElse?.let { it(throwable) }
            }
        }
    }
}