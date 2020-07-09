package com.surya.canadaholidays

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.surya.canadaholidays.di.DaggerHolidayListViewModelComponent
import com.surya.canadaholidays.model.*
import com.surya.canadaholidays.viewmodel.HolidayListViewModel
import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor

class HolidayListViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockProvinceAPIService: ProvinceAPIService

     private var holidayListViewModel = HolidayListViewModel()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

    }

    @Test
    fun getHolidayListSuccess() {

        val holidays = Holiday(
            "2020-01-01",
            1,
            1,
            "New Year’s Day",
            "Jour de l’An",
            "2020-01-01"
        )
        val nextHoliday = NextHoliday(
            "2020-01-01",
            1,
            23,
            "Labour Day",
            "Fête du travail",
            "2020-01-01"
        )
        val province = Province(listOf(holidays),"1","Ontario","French Ontario",nextHoliday,"Ontario Public holidays","https://www.ontario.ca/document/your-guide-employment-standards-act-0/public-holidays")

        holidayListViewModel.refresh("ON")

        Mockito.`when`(mockProvinceAPIService.getHolidays("ON",2020)).thenAnswer {
            return@thenAnswer Single.just(HolidayModel(province))
        }

        Assert.assertEquals(
            "New Year’s Day",
            holidayListViewModel.holidays.value?.get(0)?.nameEn
        )
    }

    @Before
    fun setupRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
            }

        }
        RxJavaPlugins.setIoSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }
}