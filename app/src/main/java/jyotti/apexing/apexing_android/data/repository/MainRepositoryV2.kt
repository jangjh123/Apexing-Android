package jyotti.apexing.apexing_android.data.repository

import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.data.model.main.crafting.Crafting
import jyotti.apexing.apexing_android.data.model.main.map.Map
import jyotti.apexing.apexing_android.data.model.main.news.News
import jyotti.apexing.apexing_android.data.model.main.user.UserInfo
import jyotti.apexing.apexing_android.data.remote.ApexingApi
import javax.inject.Inject

class MainRepositoryV2 @Inject constructor(
    private val apexingApi: ApexingApi
) {
    suspend fun fetchMaps(): List<Map> {
        val maps = apexingApi.fetchMaps()
        return listOf(
            maps.brNormal.copy(typeStringId = R.string.battle_royale_normal),
            maps.brRank.copy(typeStringId = R.string.battle_royale_rank),
            maps.arNormal.copy(typeStringId = R.string.arena_normal),
            maps.arRank.copy(typeStringId = R.string.arena_rank)
        )
    }

    suspend fun fetchNotice(): String = apexingApi.fetchNotice()

    suspend fun fetchCraftings(): List<Crafting> {
        val craftings = apexingApi.fetchCraftings()
        return listOf(
            craftings.daily0,
            craftings.daily1,
            craftings.weekly0,
            craftings.weekly1
        )
    }

    suspend fun fetchUserInfo(id: String): UserInfo = apexingApi.fetchUserInfo(id)

    suspend fun fetchNewses(): List<News> {
        val newses = apexingApi.fetchNewses()
        return listOf(
            newses.news0,
            newses.news1,
            newses.news2,
            newses.news3,
            newses.news4
        )
    }
}