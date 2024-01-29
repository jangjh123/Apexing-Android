package jyotti.apexing.apexing_android.data.repository

import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import jyotti.apexing.apexing_android.data.model.statistics.LegendNames
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels.Header
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels.Match
import jyotti.apexing.apexing_android.data.model.statistics.MostLegend
import jyotti.apexing.apexing_android.data.remote.ApexingApi
import jyotti.apexing.apexing_android.di.DefaultDispatcher
import jyotti.apexing.apexing_android.util.firstDecimalString
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StatisticsRepository @Inject constructor(
    private val apexingApi: ApexingApi,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend fun fetchStatistics(id: String): List<MatchModels> {
        val matchModels = mutableListOf<MatchModels>()

        val updateIndex = apexingApi.fetchUpdateIndex(id)
        val validMatches = apexingApi.fetchMatches(id)
            .asSequence()
            .filter { match ->
                match.mode != "UNKNOWN" &&
                        match.mode != "ARENA" &&
                        match.secs in 0..1800 &&
                        match.damage in 0..9999 &&
                        match.kill in 0..59
            }.map { match ->
                match.copy(isTracked = match.kill != 0 || match.damage != 0)
            }.toList()

        val header = withContext(defaultDispatcher) {
            buildHeader(
                updateIndex = updateIndex,
                matches = validMatches,
                recentMatches = if (validMatches.size >= 20) validMatches.subList(0, 20) else validMatches
            )
        }

        return matchModels.apply {
            add(header)
            addAll(validMatches)
        }
    }

    private fun buildHeader(
        updateIndex: Int,
        matches: List<Match>,
        recentMatches: List<Match>
    ): Header {
        val mostLegends = getMostLegends(matches)

        return Header(
            updateTimeLeft = updateIndex / UPDATE_PER_HOUR + 1,
            matches = matches,
            mostLegends = mostLegends,
            pieData = getPieData(mostLegends),
            killAvgAllString = getKillAvgAllString(matches),
            damageAvgAllString = getDamageAvgAllString(matches),
            killAvgRecentString = getKillAvgRecentString(recentMatches),
            damageAvgRecentString = getDamageAvgRecentString(recentMatches),
        )
    }

    private fun getMostLegends(matches: List<Match>): List<Pair<String, MostLegend>> {
        val mostLegendMap = HashMap<String, MostLegend>().apply {
            enumValues<LegendNames>().forEach {
                this[it.name] = MostLegend(0, 0, 0)
            }
        }

        matches.forEach { match ->
            mostLegendMap[match.legend]?.let {
                mostLegendMap[match.legend] = it.copy(
                    playCount = it.playCount + 1,
                    killAmount = it.killAmount + match.kill,
                    damageAmount = it.damageAmount + match.damage
                )
            }
        }

        return mostLegendMap.toList().sortedByDescending { it.second.playCount }.subList(0, 5)
    }

    /*
        PieChart
     */
    private fun getPieData(mostLegends: List<Pair<String, MostLegend>>): PieData {
        val pieEntries = mutableListOf<PieEntry>().apply {
            for (i in 0..4) {
                add(PieEntry(mostLegends[i].second.playCount.toFloat(), mostLegends[i].first))
            }
        }

        return PieData(PieDataSet(pieEntries, ""))
    }

    /*
        Basic Statistics
     */
    private fun getKillAvgAllString(matches: List<Match>): String {
        var kills = 0f
        matches.forEach {
            kills += it.kill
        }
        return (kills / matches.size).firstDecimalString()
    }

    private fun getDamageAvgAllString(matches: List<Match>): String {
        var damages = 0f
        matches.forEach {
            damages += it.damage
        }
        return (damages / matches.size).firstDecimalString()
    }

    private fun getKillAvgRecentString(recentMatches: List<Match>): String {
        var kills = 0f
        recentMatches.forEach {
            kills += it.kill
        }
        return (kills / recentMatches.size).firstDecimalString()
    }

    private fun getDamageAvgRecentString(recentMatches: List<Match>): String {
        var damages = 0f
        recentMatches.forEach {
            damages += it.damage
        }
        return (damages / recentMatches.size).firstDecimalString()
    }

    companion object {
        private const val UPDATE_PER_HOUR = 45
    }
}