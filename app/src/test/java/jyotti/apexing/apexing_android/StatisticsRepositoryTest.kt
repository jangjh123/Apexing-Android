package jyotti.apexing.apexing_android

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import jyotti.apexing.apexing_android.data.model.statistics.LegendNames
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels
import jyotti.apexing.apexing_android.data.model.statistics.MostLegend
import jyotti.apexing.apexing_android.di.NetworkModule

class StatisticsRepositoryTest : BehaviorSpec({
    val apexingApi = NetworkModule.provideApexingApi(
        NetworkModule.provideRetrofit(
            NetworkModule.provideOkHttpClient(NetworkModule.provideFirebaseRequestInterceptor()),
            NetworkModule.provideGsonConverterFactory()
        )
    )

    Given("주어진 리스트로 헤더를 생성해야 한다.") {
        val matches: List<MatchModels.Match> = apexingApi.fetchMatches("gn000123").asSequence()
            .map { match ->
                match.copy(
                    isValid = match.mode != "UNKNOWN" &&
                            match.mode != "ARENA" &&
                            match.secs in 0..1800 &&
                            match.damage in 0..9999 &&
                            match.kill in 0..59
                )
            }.filter {
                it.isValid
            }.toList()

        When("레전드 별 매치 수를 기록하기 위한 맵을 생성한다.") {
            val mostLegendMap = HashMap<String, Int>().apply {
                enumValues<LegendNames>().forEach {
                    this[it.name] = 0
                }
            }

            Then("리스트 순회하며 이름에 맞게 더한다.") {
                matches.forEach { match ->
                    mostLegendMap[match.legend]?.let {
                        mostLegendMap[match.legend] = it + 1
                    } ?: run {
                        mostLegendMap[match.legend] = 1
                    }
                }

                mostLegendMap["Bloodhound"] shouldBe 0
            }

            matches.forEach{
                println(it.kill)
            }

            val mostList = mostLegendMap.toList().sortedByDescending { it.second }.subList(0, 5)

            Then("매치 수가 많은 순서의 리스트를 생성한다. (size = 5)") {
                mostList shouldBe emptyList()
            }
        }

        When("레전드 이름이 Key, MostLegend 타입이 값인 맵을 생성한다.") {
            val mostLegendMap = HashMap<String, MostLegend>().apply {
                enumValues<LegendNames>().forEach {
                    this[it.name] = MostLegend(0, 0, 0)
                }
            }

            Then("유효 매치수, 유효 대미지, 유효 킬을 기록하고, 평균값을 저장한다.") {
                matches.forEach { match ->
                    mostLegendMap[match.legend]?.let {
                        mostLegendMap[match.legend] = it.copy(
                            it.playCount + 1,
                            it.killAmount + match.kill,
                            it.damageAmount + match.damage
                        )
                    }
                }
            }

            val mostList = mostLegendMap.toList().sortedByDescending { it.second.playCount }.subList(0, 5)
            Then("매치 수가 많은 순서대로 정렬한다.") {
                mostList shouldBe emptyList()
            }

            Then("평균값을 획득한다.") {
                mostList.forEach {
                    println("KILL ${it.second.getKillAvgString()}    DAMAGE ${it.second.getDamageAvgString()}")
                }
            }
        }
    }
})