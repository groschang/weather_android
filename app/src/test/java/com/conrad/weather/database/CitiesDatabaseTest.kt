package com.conrad.weather.database

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import android.content.Context
import androidx.room.Room

import com.conrad.weather.local.cities.database.CitiesDatabase

import org.robolectric.annotation.Config

import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame

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