package jyotti.apexing.apexing_android.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels

@Dao
interface MatchDao {
    @Query("SELECT * FROM `match`")
    suspend fun getAll(): List<MatchModels.Match>

    @Query("SELECT * FROM 'match' " +
            "WHERE kill BETWEEN 0 AND 59" +
            " AND damage BETWEEN 0 AND 9999" +
            " AND gameLengthSecs BETWEEN 0 AND 1800" +
            " ORDER BY gameStartTimestamp DESC LIMIT 20")
    suspend fun getRecent(): List<MatchModels.Match>

    @Query("SELECT * FROM `match` ORDER BY gameStartTimestamp DESC LIMIT :loadSize OFFSET :index * :loadSize")
    suspend fun getPage(index: Int, loadSize: Int): List<MatchModels.Match>

    @Insert
    fun insert(list: MatchModels.Match)

    @Query("DELETE FROM 'match'")
    suspend fun deleteAll()

    @Query("SELECT * FROM `match` ORDER BY gameStartTimestamp DESC LIMIT 1")
    suspend fun getLastMatch(): MatchModels.Match
}