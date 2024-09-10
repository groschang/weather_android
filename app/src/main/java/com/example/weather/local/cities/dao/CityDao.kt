package com.example.weather.local.cities.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weather.local.cities.entity.DBCity
import kotlinx.coroutines.flow.Flow


@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: DBCity)

    @Update
    suspend fun update(item: DBCity)

    @Query("DELETE from cities WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * from cities WHERE id = :id")
    fun getCity(id: String): Flow<DBCity>

    @Query("SELECT * from cities ORDER BY uId ASC")
    fun getAllCities(): Flow<List<DBCity>>

    @Query("DELETE FROM cities")
    fun deleteAllCities()
}