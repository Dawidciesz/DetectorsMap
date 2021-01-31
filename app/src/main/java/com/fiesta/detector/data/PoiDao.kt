package com.fiesta.detector.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PoiDao {

    @Query("SELECT * FROM poi_table")
    fun getTasks(): Flow<List<Poi>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(poi: Poi)

    @Update
    suspend fun update(poi: Poi)

    @Delete
    suspend fun delete(poi: Poi)
}