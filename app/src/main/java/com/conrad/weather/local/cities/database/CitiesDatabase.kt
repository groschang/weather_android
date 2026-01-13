package com.conrad.weather.local.cities.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.conrad.weather.local.cities.dao.CityDao
import com.conrad.weather.local.cities.entity.DBCity

@Database(entities = [DBCity::class], version = 1, exportSchema = false)
abstract class CitiesDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        private var Instance: CitiesDatabase? = null

        fun getDatabase(context: Context): CitiesDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CitiesDatabase::class.java,
                    "city_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                Instance = instance
                instance
            }
        }
    }
}

