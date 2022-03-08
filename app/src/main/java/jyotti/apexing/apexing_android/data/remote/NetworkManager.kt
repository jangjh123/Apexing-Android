package jyotti.apexing.apexing_android.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkManager {
    companion object {
        private val apiService = Retrofit.Builder()
            .baseUrl("https://api.mozambiquehe.re/")
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }

    fun getClient(): ApiService = apiService
}