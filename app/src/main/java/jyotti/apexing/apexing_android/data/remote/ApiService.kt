package jyotti.apexing.apexing_android.data.remote

import com.google.gson.JsonObject
import jyotti.apexing.apexing_android.data.model.main.crafting.Crafting
import jyotti.apexing.apexing_android.data.model.main.map.Maps
import jyotti.apexing.apexing_android.data.model.main.news.News
import jyotti.apexing.apexing_android.data.model.main.user.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("bridge?version=5")
    fun fetchAccount(
        @Query("platform") platform: String,
        @Query("player") id: String,
        @Query("auth") key: String
    ): Call<JsonObject>

    @GET("bridge?version=5")
    fun fetchUser(
        @Query("platform") platform: String,
        @Query("player") id: String,
        @Query("auth") key: String
    ): Call<User>

    @GET("maprotation?version=2")
    fun fetchMap(
        @Query("auth") key: String
    ): Call<Maps>

    @GET("crafting?")
    fun fetchCrafting(
        @Query("auth") key: String
    ): Call<List<Crafting>>

    @GET("news?")
    fun fetchNews(
        @Query("lang") lang: String,
        @Query("auth") key: String
    ): Call<List<News>>
}