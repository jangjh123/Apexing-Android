package jyotti.apexing.apexing_android.data.model.main.map

import com.google.gson.annotations.SerializedName

data class MapsResponse(
    @SerializedName("BR_normal") val brNormal: Map,
    @SerializedName("BR_rank") val brRank: Map,
    @SerializedName("AR_normal") val arNormal: Map,
    @SerializedName("AR_rank") val arRank: Map
)
