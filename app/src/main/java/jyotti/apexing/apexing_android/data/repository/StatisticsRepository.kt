package jyotti.apexing.apexing_android.data.repository

import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import jyotti.apexing.apexing_android.data.model.statistics.LegendNames
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels.Header
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels.Match
import jyotti.apexing.apexing_android.data.model.statistics.MostLegend
import jyotti.apexing.apexing_android.data.remote.ApexingApi
import jyotti.apexing.apexing_android.di.DefaultDispatcher
import jyotti.apexing.apexing_android.util.CustomBarDataSet
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
        val matches = apexingApi.fetchMatches(id)
        val validMatches = matches.asSequence().map { match ->
            match.copy(
                isValid = match.mode != "UNKNOWN" &&
                        match.mode != "ARENA" &&
                        match.secs in 0..1800 &&
                        match.damage in 0..9999 &&
                        match.kill in 0..59
            )
        }.filter {
            it.isValid
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
            addAll(matches)
        }
    }

    private fun buildHeader(
        updateIndex: Int,
        matches: List<Match>,
        recentMatches: List<Match>
    ): Header {
        val mostLegends = getMostLegends(matches)

        return Header(
            updateTimeLeft = updateIndex / 45 + 1,
            matches = matches,
            mostLegends = mostLegends,
            pieData = getPieData(mostLegends),
            killAvgAll = getKillAvgAll(matches),
            damageAvgAll = getDamageAvgAll(matches),
            killAvgRecent = getKillAvgRecent(matches),
            damageAvgRecent = getDamageAvgRecent(matches),
            radarDataSet = getRadarChart(matches),
            barDataSet = getBarChartValue(recentMatches)
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
    private fun getKillAvgAll(matches: List<Match>): Float {
        var kills = 0f
        matches.filter { it.isValid }.forEach {
            kills += it.kill
        }
        return kills / matches.size
    }

    private fun getDamageAvgAll(matches: List<Match>): Float {
        var damages = 0f
        matches.filter { it.isValid }.forEach {
            damages += it.damage
        }
        return damages / matches.size
    }

    private fun getKillAvgRecent(recentMatches: List<Match>): Float {
        var kills = 0f
        if (recentMatches.size > 19) {
            for (i in 0..19) {
                kills += recentMatches[i].kill
            }
        }
        return kills / recentMatches.size
    }

    private fun getDamageAvgRecent(recentMatches: List<Match>): Float {
        var damages = 0f
        if (recentMatches.size > 19) {
            for (i in 0..19) {
                damages += recentMatches[i].damage
            }
        }
        return damages / recentMatches.size
    }

    /*
        RadarChart
     */
    private fun getRadarChart(matches: List<Match>): RadarDataSet {
        val label = ""
        val radarEntries = mutableListOf<RadarEntry>().apply {
            add(RadarEntry(getRadarChartValue(matches)[0]))
            add(RadarEntry(getRadarChartValue(matches)[1]))
            add(RadarEntry(getRadarChartValue(matches)[2]))
            add(RadarEntry(getRadarChartValue(matches)[3]))
        }

        return RadarDataSet(radarEntries, label)
    }

    private fun getRadarChartValue(matches: List<Match>): FloatArray {
        val data = FloatArray(4)
        var killCatch = 0f
        var survivalAbility = 0f
        var deal = 0f
        val effectedMatchCnt = matches.count { it.isValid }

        matches.filter { it.isValid }.forEach {
            killCatch += it.kill
            survivalAbility += it.secs
            deal += it.damage
        }

        var killCatchData = killCatch / effectedMatchCnt
        killCatchData *= 40
        data[0] = killCatchData

        var survivalAbilityData = survivalAbility / effectedMatchCnt
        survivalAbilityData /= 12
        data[1] = survivalAbilityData

        for (i in matches.filter { it.isValid }.indices) {
            deal += matches[i].damage
        }
        var dealData = deal / effectedMatchCnt
        dealData /= 10

        data[2] = dealData

        data[3] = ((killCatchData + dealData) / survivalAbilityData) * 25

        return data
    }

    /*
        BarChart
     */
    private fun getBarChartValue(recentMatches: List<Match>): List<BarDataSet> {
        val dealList = mutableListOf<BarEntry>()
        val killList = mutableListOf<BarEntry>()

        if (recentMatches.filter { it.isValid }.size > 19) {
            for (i in 0..19) {
                dealList.add(BarEntry(i.toFloat(), recentMatches[i].damage.toFloat()))
                killList.add(BarEntry(i.toFloat(), recentMatches[i].kill.toFloat()))
            }
        }

        return listOf(BarDataSet(dealList, "딜"), CustomBarDataSet(killList, "킬"))
    }
}