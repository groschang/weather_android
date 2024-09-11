package com.example.weather.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather.local.cities.database.CitiesDatabase
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest=Config.NONE)
class CitiesDatabaseTest {

    private lateinit var db: CitiesDatabase
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, CitiesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `getDatabase returns non null instance`() {
        // When
        val instance = CitiesDatabase.getDatabase(context)

        // Then
        assertNotNull(instance)
    }

    @Test
    fun `getDatabase returns same instance`() {
        // When
        val instance1 = CitiesDatabase.getDatabase(context)
        val instance2 = CitiesDatabase.getDatabase(context)

        // Then
        assertSame(instance1, instance2)
    }

    @Test
    fun `cityDao returns non null dao`() {
        // When
        val dao = db.cityDao()

        // Then
        assertNotNull(dao)
    }
}