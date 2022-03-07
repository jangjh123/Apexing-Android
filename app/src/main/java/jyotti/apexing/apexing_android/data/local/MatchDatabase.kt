package jyotti.apexing.apexing_android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels

@Database(entities = [MatchModels.Match::class], version = 1)
abstract class MatchDatabase : RoomDatabase() {

    abstract fun getMatchDao(): MatchDao

    companion object {
        const val DATABASE_NAME: String = "match_db"
    }
}