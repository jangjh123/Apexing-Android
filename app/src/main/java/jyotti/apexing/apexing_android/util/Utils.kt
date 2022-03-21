package jyotti.apexing.apexing_android.util

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object Utils {
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
}