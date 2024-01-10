package jyotti.apexing.apexing_android.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import jyotti.apexing.apexing_android.data.model.main.user.User
import jyotti.apexing.apexing_android.data.model.store.StoreItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApexingApi {
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

    @GET("store?")
    fun fetchStoreData(
        @Query("auth") key: String
    ): Call<List<StoreItem>>

    //

    @GET("VERSION/current")
    suspend fun fetchVersion(): String
}