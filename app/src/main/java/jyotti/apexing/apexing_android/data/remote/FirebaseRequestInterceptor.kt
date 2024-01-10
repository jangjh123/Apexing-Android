package jyotti.apexing.apexing_android.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class FirebaseRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val isNotForFirebase = request.header(KEY_NOT_FOR_FIREBASE) == "true"
        val requestBuilder = request.newBuilder()
        val originalUrl = request.url

        val requestUrl: String = if (isNotForFirebase) {
            originalUrl.toString()
        } else {
            "$originalUrl$PATH_JSON"
        }

        val newRequest = requestBuilder
            .url(requestUrl)
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val KEY_NOT_FOR_FIREBASE = "notForFirebase"
        private const val PATH_JSON = "/.json"
    }
}