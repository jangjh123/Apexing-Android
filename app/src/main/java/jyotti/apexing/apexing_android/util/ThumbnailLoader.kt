package jyotti.apexing.apexing_android.util

import android.content.Context
import com.bumptech.glide.Glide

object ThumbnailLoader {
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
}