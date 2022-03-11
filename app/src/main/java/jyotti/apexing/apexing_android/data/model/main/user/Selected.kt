package jyotti.apexing.apexing_android.data.model.main.user

import com.google.gson.annotations.SerializedName

data class Selected(
    @SerializedName(value = "ImgAssets")
    val imageAsset: ImageAsset
)
