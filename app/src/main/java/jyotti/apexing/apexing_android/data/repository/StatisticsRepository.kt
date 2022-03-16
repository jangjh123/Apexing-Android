package jyotti.apexing.apexing_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.insertFooterItem
import androidx.paging.insertHeaderItem
import com.github.mikephil.charting.data.*
import com.google.gson.JsonArray
import jyotti.apexing.apexing_android.BuildConfig.KEY_API
import jyotti.apexing.apexing_android.data.local.MatchDao
import jyotti.apexing.apexing_android.data.local.MatchPagingSource
import jyotti.apexing.apexing_android.data.model.statistics.LegendNames
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels
import jyotti.apexing.apexing_android.data.remote.NetworkManager
import jyotti.apexing.apexing_android.util.CustomBarDataSet
import jyotti.apexing.data_store.KEY_REFRESH_DATE
import jyotti.apexing.data_store.KEY_UID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class StatisticsRepository @Inject constructor(
    val networkManager: NetworkManager,
    private val dataStore: DataStore<Preferences>,
    private val matchDao: MatchDao,
    dispatcher: CoroutineDispatcher
) {

    private val uidFlow: Flow<String> = dataStore.data.map {
        it[KEY_UID] ?: ""
    }.flowOn(dispatcher)

    private val refreshDateFlow: Flow<Long> = dataStore.data.map {
        it[KEY_REFRESH_DATE] ?: 0
    }.flowOn(dispatcher)

    fun readStoredUid() = uidFlow
    fun readStoredRefreshDate() = refreshDateFlow

    inline fun sendMatchRequest(
        uid: String,
        start: Long,
        crossinline onSuccess: (List<MatchModels.Match>) -> Unit,
        crossinline onError: () -> Unit,
        crossinline onFailure: () -> Unit
    ) {
        networkManager.getClient().fetchMatch(KEY_API, uid, start, Int.MAX_VALUE).enqueue(object :
            Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                when (response.code()) {
                    200 -> {
                        onSuccess(setDamageAndKill(response.body()!!))

                    }
                    else -> {
                        onError()
                    }
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                onFailure()
            }
        })
    }

    fun setDamageAndKill(jsonArray: JsonArray): ArrayList<MatchModels.Match> {

        val matchList = ArrayList<MatchModels.Match>()

        if (!jsonArray.isEmpty) {
            jsonArray.forEach {
                with(it.asJsonObject) {
                    var kill = 0
                    var damage = 0

                    try {
                        if (this.get("gameData").asJsonArray.size() > 0) {
                            for (i in 0..2) {
                                if (get("gameData").asJsonArray[i].asJsonObject.get("key")
                                        .toString() == "\"kills\"" ||
                                    get("gameData").asJsonArray[i].asJsonObject.get("key")
                                        .toString() == "\"specialEvent_kills\""
                                ) {
                                    kill =
                                        get("gameData").asJsonArray[i].asJsonObject.get("value").asInt
                                } else if (get("gameData").asJsonArray[i].asJsonObject.get("key")
                                        .toString() == "\"damage\"" ||
                                    get("gameData").asJsonArray[i].asJsonObject.get("key")
                                        .toString() == "\"specialEvent_damage\""
                                )
                                    damage =
                                        get("gameData").asJsonArray[i].asJsonObject.get("value").asInt
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    matchList.add(
                        MatchModels.Match(
                            legendPlayed = when (get("legendPlayed").asString) {
                                "Mad Maggie" -> {
                                    "MadMaggie"
                                }
                                else -> {
                                    get("legendPlayed").asString
                                }
                            },
                            gameMode = get("gameMode").asString,
                            gameLengthSecs = get("gameLengthSecs").asInt,
                            gameStartTimestamp = get("gameStartTimestamp").asLong,
                            kill = kill,
                            damage = damage
                        )
                    )
                }
            }
        }
        return matchList
    }

    suspend fun storeRefreshDate(refreshDate: Long) {
        dataStore.edit {
            it[KEY_REFRESH_DATE] = refreshDate
        }
    }

    fun storeMatch(matchList: List<MatchModels.Match>) {
        matchList.forEach {
            matchDao.insert(it)
        }
    }

    fun readMatch() = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = {
            MatchPagingSource(matchDao)
        }
    ).flow.map {
        it
            .insertHeaderItem(
                item = setHeaderValue(
                    matchDao.getAll(),
                    refreshedDate = System.currentTimeMillis() / 1000
                )
            )
            .insertFooterItem(item = MatchModels.Footer("마지막 매치입니다."))
    }

    private fun setHeaderValue(matchList: List<MatchModels.Match>, refreshedDate: Long) =
        MatchModels.Header(
            pieData = getPieChart(matchList),
            killRvgAll = getKillRvgAll(matchList),
            damageRvgAll = getDamageRvgAll(matchList),
            killRvgRecent = getKillRvgRecent(matchList),
            damageRvgRecent = getDamageRvgRecent(matchList),
            refreshedDate = refreshedDate,
            radarDataSet = getRadarChart(matchList),
            barDataSet = getLineChartValue(matchList)
        )

    // PieChart
    private fun getPieChart(
        matchList: List<MatchModels.Match>
    ): PieData {
        val mostLegendMap = HashMap<String, Int>().apply {
            enumValues<LegendNames>().forEach {
                this[it.name] = 0
            }
        }

        for (i in matchList.indices) {
            mostLegendMap[matchList[i].legendPlayed] =
                mostLegendMap.getValue(matchList[i].legendPlayed) + 1
        }


        val sortedList = mostLegendMap.toList().sortedByDescending {
            it.second
        }

        val pieEntries: ArrayList<PieEntry> = ArrayList<PieEntry>().apply {
            for (i in 0..4) {
                add(PieEntry(sortedList[i].second.toFloat(), sortedList[i].first))
            }
        }

        return PieData(PieDataSet(pieEntries, ""))
    }

    // Basic Statistics
    private fun getKillRvgAll(matchList: List<MatchModels.Match>): Double {
        var kills = 0.0
        matchList.forEach {
            kills += it.kill
        }
        return kills / matchList.size
    }

    private fun getDamageRvgAll(matchList: List<MatchModels.Match>): Double {
        var damages = 0.0
        matchList.forEach {
            damages += it.damage
        }
        return damages / matchList.size
    }

    private fun getKillRvgRecent(matchList: List<MatchModels.Match>): Double {
        var kills = 0.0
        if (matchList.size > 19) {

            for (i in 0..19) {
                kills += matchList[i].kill
            }
        }
        return kills / 20
    }

    private fun getDamageRvgRecent(matchList: List<MatchModels.Match>): Double {
        var damages = 0.0
        if (matchList.size > 19) {

            for (i in 0..19) {
                damages += matchList[i].damage
            }
        }
        return damages / 20
    }

    private fun getRadarChart(
        matchList: List<MatchModels.Match>
    ): RadarDataSet {
        val label = ""
        val radarEntries = ArrayList<RadarEntry>().apply {
            add(RadarEntry(getRadarChartValue(matchList)[0]))
            add(RadarEntry(getRadarChartValue(matchList)[1]))
            add(RadarEntry(getRadarChartValue(matchList)[2]))
            add(RadarEntry(getRadarChartValue(matchList)[3]))
        }

        return RadarDataSet(radarEntries, label)
    }

    private fun getRadarChartValue(matchList: List<MatchModels.Match>): FloatArray {
        val data = FloatArray(4)

        var killCatch = 0f
        var survivalAbility = 0f
        var deal = 0f

        matchList.forEach {
            killCatch += it.kill
            survivalAbility += it.gameLengthSecs
            deal += it.damage
        }

        var killCatchData = killCatch / matchList.size
        killCatchData *= 20
        data[0] = killCatchData

        var survivalAbilityData = survivalAbility / matchList.size
        survivalAbilityData /= 12
        data[1] = survivalAbilityData

        for (i in matchList.indices) {
            deal += matchList[i].damage
        }
        var dealData = deal / matchList.size
        dealData /= 10
        data[2] = dealData

        data[3] = ((killCatchData + dealData) / survivalAbilityData) * 25

        return data
    }

    private fun getLineChartValue(matchList: List<MatchModels.Match>): List<BarDataSet> {
        val dealList = ArrayList<BarEntry>()
        val killList = ArrayList<BarEntry>()

        if (matchList.size > 19) {
            for (i in 0..19) {
                dealList.add(BarEntry(i.toFloat(), matchList[i].damage.toFloat()))
                killList.add(BarEntry(i.toFloat(), matchList[i].kill.toFloat()))
            }
        }

        return listOf(BarDataSet(dealList, "딜"), CustomBarDataSet(killList, "킬"))
    }
}