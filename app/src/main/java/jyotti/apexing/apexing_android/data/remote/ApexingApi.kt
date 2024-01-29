package jyotti.apexing.apexing_android.data.remote

import com.google.gson.JsonObject
import jyotti.apexing.apexing_android.data.model.main.crafting.CraftingsResponse
import jyotti.apexing.apexing_android.data.model.main.map.MapsResponse
import jyotti.apexing.apexing_android.data.model.main.news.NewsesResponse
import jyotti.apexing.apexing_android.data.model.main.user.UserInfo
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels.Match
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url

interface ApexingApi {
    @Headers("notForFirebase: true")
    @GET
    suspend fun fetchAccount(@Url url: String): Response<JsonObject>

    @GET("VERSION/current")
    suspend fun fetchVersion(): String

    @GET("USER/PC/{id}/isDormancy")
    suspend fun fetchIsDormancy(@Path("id") id: String): Boolean

    @PUT("USER/PC/{id}/lastConnection")
    suspend fun fetchLastConnectedTime(
        @Path("id") id: String,
        @Body lastConnectedTime: String
    ): String

    @GET("MAPS")
    suspend fun fetchMaps(): MapsResponse

    @GET("VERSION/notice")
    suspend fun fetchNotice(): String

    @GET("Craftings")
    suspend fun fetchCraftings(): CraftingsResponse

    @GET("USER_INFO/{id}")
    suspend fun fetchUserInfo(@Path("id") id: String): UserInfo

    @GET("News")
    suspend fun fetchNewses(): NewsesResponse

    @GET("MATCH/{id}")
    suspend fun fetchMatches(@Path("id") id: String): List<Match>

    @GET("Index/{id}")
    suspend fun fetchUpdateIndex(@Path("id") id: String): Int
}