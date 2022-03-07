package jyotti.apexing.apexing_android.data.model.main.user

data class Global(
    val name: String,
    val level: Int,
    val toNextLevelPercent: Int,
    val rank: Rank,
    val arena: Rank
)
