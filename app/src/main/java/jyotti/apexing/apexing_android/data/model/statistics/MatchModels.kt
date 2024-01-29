package jyotti.apexing.apexing_android.data.model.statistics

import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.RadarDataSet

sealed class MatchModels {
    data class Header(
        val updateTimeLeft: Int,
        val matches: List<Match>,
        val mostLegends: List<Pair<String, MostLegend>>,
        val pieData: PieData,
        val killAvgAllString: String,
        val damageAvgAllString: String,
        val killAvgRecentString: String,
        val damageAvgRecentString: String,
        val radarDataSet: RadarDataSet
    ) : MatchModels()

    data class Match(
        val legend: String,
        val mode: String,
        val secs: Int,
        val date: Long,
        val kill: Int,
        val damage: Int,
        val isValid: Boolean
    ) : MatchModels()
}