package jyotti.apexing.apexing_android

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import okhttp3.Request

class RetrofitTest : BehaviorSpec({
    val testPath = "https://test.com/"

    Given("모든 요청에는 /.json Path 가 필요하다") {
        val originalRequest = Request.Builder().url(testPath).build()
        val originalUrl = originalRequest.url

        When("OkHttp 를 통해 /.json Path 추가한다") {
            val newUrlBuilder = originalUrl.newBuilder()
            newUrlBuilder.addPathSegments(".json")

            Then("baseUrl 에 해당 Path 가 추가된다") {
                newUrlBuilder.build().toUrl().toString() shouldBe "https://test.com/.json"
            }
        }
    }

    Given("특정 요청에는 /.json Path 가 추가된다") {
        val path = ".path"
        val requestList = arrayOf(
            Pair(Request.Builder().url(testPath).build(), false),
            Pair(Request.Builder().url(testPath).build(), false)
        )

        When("0번 인덱스의 요청에만 추가한다") {
            requestList[0] = requestList[0].copy(second = true)

            Then("조건을 통해 Path 를 추가한다") {
                requestList.forEach { pair ->
                    val request = pair.first
                    val isPathNeeded = pair.second
                    if (isPathNeeded) {
                        request.newBuilder().url(request.url.toString() + path).build().url.toString() shouldBe "$testPath$path"
                    } else {
                        request.url.toString() shouldBe testPath
                    }
                }
            }
        }
    }
})