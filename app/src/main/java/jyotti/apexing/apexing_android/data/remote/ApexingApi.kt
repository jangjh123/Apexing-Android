package jyotti.apexing.apexing_android.data.remote

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
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
}