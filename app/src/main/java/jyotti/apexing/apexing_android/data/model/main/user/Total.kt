package jyotti.apexing.apexing_android.data.model.main.user

import com.google.gson.annotations.SerializedName

data class Total(
    val kills: Record,
    val damage: Record,
    @SerializedName(value = "games_played")
    val gamesPlayed: Record?,
    val kd: RecordFloat
)
