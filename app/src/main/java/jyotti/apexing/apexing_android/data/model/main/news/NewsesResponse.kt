package jyotti.apexing.apexing_android.data.model.main.news

import com.google.gson.annotations.SerializedName

data class NewsesResponse(
    @SerializedName("News_0") val news0: News,
    @SerializedName("News_1") val news1: News,
    @SerializedName("News_2") val news2: News,
    @SerializedName("News_3") val news3: News,
    @SerializedName("News_4") val news4: News
)
