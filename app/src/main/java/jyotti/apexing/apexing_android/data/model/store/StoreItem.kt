package jyotti.apexing.apexing_android.data.model.store

data class StoreItem(
    val title: String,
    val expireTimestamp: Long,
    val shopType: String,
    val pricing: List<Payment>,
    val asset: String
)
