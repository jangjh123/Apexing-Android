package jyotti.apexing.apexing_android.data.model.main.crafting

import com.google.gson.annotations.SerializedName

data class CraftingsResponse(
    @SerializedName("Daily_0") val daily0: Crafting,
    @SerializedName("Daily_1") val daily1: Crafting,
    @SerializedName("Weekly_0") val weekly0: Crafting,
    @SerializedName("Weekly_1") val weekly1: Crafting
)
