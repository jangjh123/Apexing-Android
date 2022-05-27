package jyotti.apexing.apexing_android.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
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

    @GET("games?")
    fun fetchMatch(
        @Query("auth") key: String,
        @Query("uid") uid: String,
        @Query("start") date: Long,
        @Query("limit") limit: Int
    ): Call<JsonArray>
}