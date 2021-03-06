package jyotti.apexing.apexing_android.data.model.statistics

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.RadarDataSet

sealed class MatchModels(var type: MatchModelType) {
    @Entity
    data class Match(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        val legendPlayed: String,
        val gameMode: String,
        val gameLengthSecs: Int,
        val gameStartTimestamp: Long,
        var kill: Int,
        var damage: Int
    ) : MatchModels(MatchModelType.DATA)

    data class Header(
        val matchCount: Int,
        val pieData: PieData,
        val killRvgAll: Double,
        val damageRvgAll: Double,
        val killRvgRecent: Double,
        val damageRvgRecent: Double,
        val radarDataSet: RadarDataSet,
        val barDataSet: List<BarDataSet>
    ) : MatchModels(MatchModelType.HEADER)

    data class Footer(
        val text: String
    ) : MatchModels(MatchModelType.FOOTER)

}

enum class MatchModelType {
    HEADER, DATA, FOOTER
}