package jyotti.apexing.apexing_android.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkManager {
    companion object {
        private val apexingApi = Retrofit.Builder()
            .baseUrl("https://api.mozambiquehe.re/")
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApexingApi::class.java)
    }

    fun getClient(): ApexingApi = apexingApi
}