package com.abdok.atmosphere.data.local

import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.abdok.atmosphere.data.local.room.LocalDao
import com.abdok.atmosphere.data.local.room.LocalDataBase
import com.abdok.atmosphere.data.local.sharedPreference.ISharedPreferences
import com.abdok.atmosphere.data.models.AlertDTO
import com.abdok.atmosphere.data.models.CombinedWeatherData
import com.abdok.atmosphere.data.models.FavouriteLocation
import com.abdok.atmosphere.enums.Alert
import com.google.android.gms.maps.model.LatLng
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class LocalDataSourceImplTest {


    private lateinit var database: LocalDataBase
    private lateinit var dao: LocalDao
    private lateinit var localDataSource : LocalDataSource
    private lateinit var sharedPreferences: ISharedPreferences


    @Before
    fun setup() {
        System.out.println("before")
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocalDataBase::class.java
        ).allowMainThreadQueries()
            .build()
        dao = database.localDao()
        sharedPreferences = mockk(relaxed = true)
        localDataSource = LocalDataSourceImpl(dao, sharedPreferences)
    }

    @After
    fun closeDatabase() =database.close()

    // ------------------------------------ Test Alerts ---------------------------------


    @Test
    fun insertAlert_getAllAlerts() = runTest {
        val alert = AlertDTO(id = 9, "12:40", "19:07", Alert.ALARM)
        val insertionResult = localDataSource.insertAlert(alert)
        val list = localDataSource.getAlerts().first()
        val result = list[0]
        assertTrue(insertionResult > 0)
        assertThat(list.size, `is`(1))
        assertThat(list, contains(alert))
        assertThat(result.id, `is`(alert.id))
        assertThat(result.selectedOption, `is`(alert.selectedOption))
        assertThat(result.startDuration, `is`(alert.startDuration))
        assertThat(result.endDuration, `is`(alert.endDuration))

    }

    @Test
    fun deleteAlert_shouldRemoveItem() = runTest {
        val alert = AlertDTO(id = 9, "12:40", "19:07", Alert.ALARM)
        localDataSource.insertAlert(alert)
        val deletionResult = localDataSource.deleteAlert(alert)
        val list = localDataSource.getAlerts().first()
        assertTrue(deletionResult > 0)
        assertTrue(list.isEmpty())
    }

    @Test
    fun deleteAlertById_shouldRemoveItem() = runTest {
        val alert = AlertDTO(id = 9, "12:40", "19:07", Alert.ALARM)
        localDataSource.insertAlert(alert)
        val deletionResult = localDataSource.deleteAlert(alert.id)
        val list = localDataSource.getAlerts().first()
        assertTrue(deletionResult > 0)
        assertTrue(list.isEmpty())
    }

    // --------------------------------------------------- Test Favourite ---------------------------------------------------

    @Test
    fun insertFavouriteLocation_and_getAllFavouriteLocations() = runTest {
        val fakeCombinedWeatherData : CombinedWeatherData = mockk(relaxed = true)

        val location = FavouriteLocation(
            cityName = "Cairo",
            countryName = "Egypt",
            location = LatLng(30.0444, 31.2357),
            combinedWeatherData = fakeCombinedWeatherData
        )

        val insertionResult =localDataSource.insertFavoriteLocation(location)
        val locations = localDataSource.getFavoriteLocations().first()
        val result = locations[0]

        assertTrue(insertionResult>0)
        assertThat(locations.size, `is`(1))
        assertThat(locations, contains(location))
        assertThat(result.cityName, `is`(location.cityName))
        assertThat(result.countryName, `is`(location.countryName))
        assertThat(result.location, `is`(location.location))
        assertThat(result.combinedWeatherData, `is`(location.combinedWeatherData))

    }

    @Test
    fun updateFavouriteLocation_and_getAllFavouriteLocations() = runTest {
        val fakeCombinedWeatherData: CombinedWeatherData = mockk(relaxed = true)

        val location = FavouriteLocation(
            cityName = "Cairo",
            countryName = "Egypt",
            location = LatLng(30.0444, 31.2357),
            combinedWeatherData = fakeCombinedWeatherData
        )

        localDataSource.insertFavoriteLocation(location)

        val newLatLng = LatLng(31.0444, 32.2357)
        val updatedLocation = location.copy(location = newLatLng)
        val updateResult = localDataSource.updateFavoriteLocation(updatedLocation)
        val locations = localDataSource.getFavoriteLocations().first()

        assertTrue(updateResult > 0)
        assertThat(locations.size, `is`(1))
        val result = locations[0]

        assertThat(result.location.latitude, `is`(newLatLng.latitude))
        assertThat(result.location.longitude, `is`(newLatLng.longitude))
    }


    @Test
    fun deleteFavouriteLocation_and_getAllFavouriteLocations() = runTest {

        val fakeCombinedWeatherData: CombinedWeatherData = mockk(relaxed = true)
        val location = FavouriteLocation(
            cityName = "Cairo",
            countryName = "Egypt",
            location = LatLng(30.0444, 31.2357),
            combinedWeatherData = fakeCombinedWeatherData
        )

        localDataSource.insertFavoriteLocation(location)

        val deletionResult = localDataSource.deleteFavoriteLocation(location)
        val list = localDataSource.getFavoriteLocations().first()

        assertTrue(deletionResult>0)
        assertTrue(list.isEmpty())

    }



}