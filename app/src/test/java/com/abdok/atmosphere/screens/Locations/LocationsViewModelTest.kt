package com.abdok.atmosphere.screens.Locations

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.abdok.atmosphere.data.models.FavouriteLocation
import com.abdok.atmosphere.data.models.Response
import com.abdok.atmosphere.data.repository.Repository
import com.google.android.gms.maps.model.LatLng
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class LocationsViewModelTest{

    private lateinit var viewModel: LocationsViewModel
    private lateinit var repository: Repository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        repository = mockk(relaxed = true)
        viewModel = LocationsViewModel(repository)
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun getFavouriteLocations_should_change_favLocations_stateFlow()= runTest {

        assertThat(viewModel.favLocations.value , `is`(Response.Loading))

        val loc1  = FavouriteLocation("cairo" , "Egypt" , LatLng(30.0444 , 31.2357) , mockk(relaxed = true) )
        val loc2  = FavouriteLocation("giza" , "Egypt" , LatLng(30.0444 , 31.2357) , mockk(relaxed = true) )
        val list = listOf(loc1 , loc2)

        coEvery { repository.getFavoriteLocations() } returns flow{emit(list)}
        viewModel.getFavouriteLocations()
        val data = viewModel.favLocations.first{it is Response.Success}
        assertTrue(data is Response.Success)
        data as Response.Success
        assertThat(data.data , `is`(list))
    }

    @Test
    fun getFavouriteLocations_return_error_should_change_favLocations_stateFlow()= runTest {

        assertThat(viewModel.favLocations.value , `is`(Response.Loading))

        coEvery { repository.getFavoriteLocations() } throws Exception("error")

        viewModel.getFavouriteLocations()
        val data = viewModel.favLocations.first{it is Response.Error}
        assertTrue(data is Response.Error)

        data as Response.Error
        assertThat(data.exception , `is`("error"))
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteFavouriteLocation_should_change_message_sharedFlow()= runTest {
        val loc1  = FavouriteLocation("cairo" , "Egypt" , LatLng(30.0444 , 31.2357) , mockk(relaxed = true))
        coEvery { repository.deleteFavoriteLocation(loc1) } returns 1
        viewModel.deleteFavouriteLocation(loc1)
        advanceUntilIdle()
        val data = viewModel.message.first()
        assertThat(data , `is`("Location Deleted Successfully"))
    }

}