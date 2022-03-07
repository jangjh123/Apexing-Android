package jyotti.apexing.apexing_android.data.model.statistics

class MostLegend(
    val legendName: String,
    var playCount: Int
) {
    fun addPlayCount() {
        this.playCount += 1
    }
}