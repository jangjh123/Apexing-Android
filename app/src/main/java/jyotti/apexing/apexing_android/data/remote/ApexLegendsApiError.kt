package jyotti.apexing.apexing_android.data.remote

sealed class ApexLegendsApiError : Throwable() {
    object SlowDown : ApexLegendsApiError()
    object Unknown : ApexLegendsApiError()
    object PlayerNotFound : ApexLegendsApiError()
}