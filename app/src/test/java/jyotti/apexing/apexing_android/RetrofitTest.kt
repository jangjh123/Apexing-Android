package jyotti.apexing.apexing_android

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RetrofitTest : BehaviorSpec({
    Given("모든 요청에는 /.json Path 가 필요하다") {
        val originalRequest = okhttp3.Request.Builder().url("https://test.com/").build()
        val originalUrl = originalRequest.url

        When("OkHttp 를 통해 /.json Path 추가한다") {
            val newUrlBuilder = originalUrl.newBuilder()
            newUrlBuilder.addPathSegments(".json")

            Then("baseUrl 에 해당 Path 가 추가된다") {
                newUrlBuilder.build().toUrl().toString() shouldBe "https://test.com/.json"
            }
        }
    }
})