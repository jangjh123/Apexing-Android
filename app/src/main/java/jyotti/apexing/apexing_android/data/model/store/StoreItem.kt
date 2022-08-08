package jyotti.apexing.apexing_android.data.model.store

data class StoreItem(
    val title: String,
    val expireTimeStamp: Long,
    val shopTime: String,
    val pricing: List<Payment>,
    val asset: String
)
