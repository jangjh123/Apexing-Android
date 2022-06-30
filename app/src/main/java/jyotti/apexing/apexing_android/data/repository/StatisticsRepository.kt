package jyotti.apexing.apexing_android.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.insertFooterItem
import androidx.paging.insertHeaderItem
import com.github.mikephil.charting.data.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import jyotti.apexing.apexing_android.data.local.MatchDao
import jyotti.apexing.apexing_android.data.local.MatchPagingSource
import jyotti.apexing.apexing_android.data.model.statistics.LegendNames
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels
import jyotti.apexing.apexing_android.data.remote.NetworkManager
import jyotti.apexing.apexing_android.util.CustomBarDataSet
import jyotti.apexing.data_store.KEY_ID
import jyotti.apexing.data_store.KEY_IS_RATED
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


class StatisticsRepository @Inject constructor(
    val networkManager: NetworkManager,
    private val dataStore: DataStore<Preferences>,
    val matchDao: MatchDao,
    dispatcher: CoroutineDispatcher
) {
    private val idFlow: Flow<String> = dataStore.data.map {
        it[KEY_ID] ?: ""
    }.flowOn(dispatcher)

    private val isRatedFlow: Flow<Boolean> = dataStore.data.map {
        it[KEY_IS_RATED] ?: false
    }.flowOn(dispatcher)

    val databaseInstance = FirebaseDatabase.getInstance()

    fun readStoredId() = idFlow
    fun readStoredRatingState() = isRatedFlow

    inline fun sendMatchRequest(
        id: String,
        crossinline onSuccess: (List<MatchModels.Match>) -> Unit,
        crossinline onComplete: () -> Unit,
        crossinline onFailure: () -> Unit
    ) {
        databaseInstance.getReference("MATCH").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val matchList = ArrayList<MatchModels.Match>()

                CoroutineScope(Dispatchers.IO).launch {
                    if (matchDao.getAll().isEmpty()) {
                        snapshot.children.forEach { match ->
                            matchList.add(
                                MatchModels.Match(
                                    0,
                                    match.child("legend").value.toString(),
                                    match.child("mode").value.toString(),
                                    match.child("secs").getValue<Int>()!!,
                                    match.child("date").value as Long,
                                    match.child("kill").getValue<Int>()!!,
                                    match.child("damage").getValue<Int>()!!,
                                )
                            )
                        }
                        onSuccess(matchList)
                    } else {
                        if (matchDao.getLastMatch().gameStartTimestamp == snapshot.child("0")
                                .child("date").value as Long
                        ) {
                            onComplete()
                        } else {
                            clearDatabase()
                            snapshot.children.forEach { match ->
                                matchList.add(
                                    MatchModels.Match(
                                        0,
                                        match.child("legend").value.toString(),
                                        match.child("mode").value.toString(),
                                        match.child("secs").getValue<Int>()!!,
                                        match.child("date").value as Long,
                                        match.child("kill").getValue<Int>()!!,
                                        match.child("damage").getValue<Int>()!!,
                                    )
                                )
                            }
                            onSuccess(matchList)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure()
            }

        })
    }

    fun storeMatch(matchList: List<MatchModels.Match>) {
        matchList.forEach {
            matchDao.insert(it)
        }
    }


    suspend fun clearDatabase() {
        matchDao.deleteAll()
    }

    fun readMatch() = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            maxSize = 30,
            prefetchDistance = 5,
            initialLoadSize = 10
        ),
        pagingSourceFactory = {
            MatchPagingSource(matchDao)
        }
    ).flow.map {
        it
            .insertHeaderItem(
                item = setHeaderValue(
                    matchDao.getAll(),
                    matchDao.getRecent(),
                    refreshedDate = System.currentTimeMillis() / 1000
                )
            )
            .insertFooterItem(item = MatchModels.Footer("마지막 매치입니다."))
    }

    private fun setHeaderValue(
        matchList: List<MatchModels.Match>,
        recentMatchList: List<MatchModels.Match>,
        refreshedDate: Long
    ) =
        MatchModels.Header(
            matchCount = matchList.size,
            pieData = getPieChart(matchList),
            killRvgAll = getKillRvgAll(matchList),
            damageRvgAll = getDamageRvgAll(matchList),
            killRvgRecent = getKillRvgRecent(recentMatchList),
            damageRvgRecent = getDamageRvgRecent(recentMatchList),
            refreshedDate = refreshedDate,
            radarDataSet = getRadarChart(matchList),
            barDataSet = getBarChartValue(recentMatchList)
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

    private fun getKillRvgRecent(recentMatchList: List<MatchModels.Match>): Double {
        var kills = 0.0
        if (recentMatchList.size > 19) {

            for (i in 0..19) {
                kills += recentMatchList[i].kill
            }
        }
        return kills / 20
    }

    private fun getDamageRvgRecent(recentMatchList: List<MatchModels.Match>): Double {
        var damages = 0.0
        if (recentMatchList.size > 19) {

            for (i in 0..19) {
                damages += recentMatchList[i].damage
            }
        }
        return damages / 20
    }


    // RadarChart
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
        killCatchData *= 40
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

    // BarChart
    private fun getBarChartValue(recentMatchList: List<MatchModels.Match>): List<BarDataSet> {
        val dealList = ArrayList<BarEntry>()
        val killList = ArrayList<BarEntry>()

        if (recentMatchList.size > 19) {
            for (i in 0..19) {
                dealList.add(BarEntry(i.toFloat(), recentMatchList[i].damage.toFloat()))
                killList.add(BarEntry(i.toFloat(), recentMatchList[i].kill.toFloat()))
            }
        }

        return listOf(BarDataSet(dealList, "딜"), CustomBarDataSet(killList, "킬"))
    }

    suspend fun storeRatingState() {
        dataStore.edit {
            it[KEY_IS_RATED] = true
        }
    }
}