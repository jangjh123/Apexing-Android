package jyotti.apexing.apexing_android.data.model.main.map

import com.google.gson.annotations.SerializedName

data class Maps(
    @SerializedName(value = "battle_royale")
    val battleRoyal: Map,
    val arenas: Map,
    val ranked: Map,
    val arenasRanked: Map
)
