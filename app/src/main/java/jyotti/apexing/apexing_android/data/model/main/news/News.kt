package jyotti.apexing.apexing_android.data.model.main.news

import com.google.gson.annotations.SerializedName

data class News(
    val title: String,
    val link: String,
    val img: String,
    @SerializedName("short_desc") val shortDescription: String
)