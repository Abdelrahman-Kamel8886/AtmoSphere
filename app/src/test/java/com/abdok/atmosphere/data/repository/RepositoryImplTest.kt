package com.abdok.atmosphere.data.repository

import com.abdok.atmosphere.data.local.LocalDataSource
import com.abdok.atmosphere.data.models.AlertDTO
import com.abdok.atmosphere.data.remote.RemoteDataSource
import com.abdok.atmosphere.enums.Alert
import org.junit.Before
import io.mockk.mockk
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Test


class RepositoryImplTest{

    private val alert1 = AlertDTO(id = 1, "12:40", "19:00", Alert.ALARM)
    private val alert2 = AlertDTO(id = 2, "9:05", "10:02", Alert.NOTIFICATION)
    private val alert3 = AlertDTO(id = 3, "13:08", "16:07", Alert.ALARM)


    private val  localList = mutableListOf(alert1,alert2,alert3)

    private lateinit var fakeLocalDataSource: LocalDataSource
    private lateinit var fakeRemoteDataSource: RemoteDataSource
    private lateinit var repository: Repository

    @Before
    fun setup() {
        fakeLocalDataSource = FakeLocalDataSource(localList)
        fakeRemoteDataSource = mockk()
        repository = RepositoryImpl(fakeRemoteDataSource, fakeLocalDataSource)
    }

    @Test
    fun getAlerts() = runTest {
        val list = repository.getAlerts().first()
        assertThat(list , IsEqual(localList))
    }

    @Test
    fun insertAlert_getAllAlerts() = runTest {
        val alert = AlertDTO(id = 9, "12:40", "19:07", Alert.ALARM)
        val alert1 = AlertDTO(id = 1, "12:40", "19:00", Alert.ALARM)
        val insertionResult = repository.insertAlert(alert)
        val insertionResult1 = repository.insertAlert(alert1)
        val list = repository.getAlerts().first()
        assertTrue(insertionResult > 0)
        assertTrue(insertionResult1 == 0L)
        assertTrue(list.contains(alert))
    }

    @Test
    fun deleteAlert_shouldRemoveItem() = runTest {
        val alert = AlertDTO(id = 9, "12:40", "19:07", Alert.ALARM)

        val deletionResult = repository.deleteAlert(alert1)
        val deletionResult1 = repository.deleteAlert(alert)

        val list = repository.getAlerts().first()
        assertTrue(deletionResult>0)
        assertTrue(deletionResult1 == 0)
        assertTrue(!list.contains(alert1))
    }

    @Test
    fun deleteAlertById_shouldRemoveItem() = runTest {
        val deletionResult = repository.deleteAlert(alert1.id)
        val deletionResult1 = repository.deleteAlert(50)

        val list = repository.getAlerts().first()
        assertTrue(deletionResult>0)
        assertTrue(deletionResult1 == 0)
        assertTrue(!list.contains(alert1))
    }

    @Test
    fun getWeatherLatLon() = runTest {
        coEvery{fakeRemoteDataSource.getWeatherLatLon(any(), any(), any(), any())} returns mockk(relaxed = true)
        val result = repository.getWeatherLatLon(10.0, 20.0, "metric" , "en")
        assertNotNull(result)
        coVerify { fakeRemoteDataSource.getWeatherLatLon(10.0, 20.0, "metric" , "en") }
    }
    @Test
    fun getForecastLatLon() = runTest {
        coEvery{fakeRemoteDataSource.getForecastLatLon(any(), any(), any(), any())} returns mockk(relaxed = true)
        val result = repository.getForecastLatLon(10.0, 20.0, "metric" , "en")
        assertNotNull(result)
        coVerify { fakeRemoteDataSource.getForecastLatLon(10.0, 20.0, "metric" , "en") }
    }

}