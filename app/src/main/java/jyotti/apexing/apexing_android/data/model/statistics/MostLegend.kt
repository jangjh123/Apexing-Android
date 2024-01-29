package jyotti.apexing.apexing_android.data.model.statistics

data class MostLegend(
    val playCount: Int,
    val killAmount: Int,
    val damageAmount: Int
) {
    fun getKillAvgString(): String = String.format("%.2f", killAmount / playCount.toFloat())

    fun getDamageAvgString(): String = String.format("%.2f", damageAmount / playCount.toFloat())
}