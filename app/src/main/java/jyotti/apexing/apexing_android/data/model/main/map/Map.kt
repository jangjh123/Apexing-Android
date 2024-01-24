package jyotti.apexing.apexing_android.data.model.main.map

import com.google.gson.annotations.SerializedName

data class Map(
    val typeStringId: Int,
    val asset: String,
    @SerializedName("map") val name: String,
    @SerializedName("end") val endTime: String
)