package com.surya.canadaholidays.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.surya.canadaholidays.di.DaggerHolidayListViewModelComponent
import com.surya.canadaholidays.model.*
import com.surya.canadaholidays.util.Constants
import com.surya.canadaholidays.util.getStringPreference
import com.surya.canadaholidays.util.getYear
import com.surya.canadaholidays.util.getYearPref
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HolidayListViewModel() : ViewModel() {
    val holidays by lazy { MutableLiveData<List<Holiday>>() }
    val loadError by lazy { MutableLiveData<String>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val disposable = CompositeDisposable()

    @Inject
    lateinit var apiService: ProvinceAPIService

    init {
        DaggerHolidayListViewModelComponent.create().inject(this)
    }

    fun refresh(provinceCode: String?) {
        loading.value = true
        getHolidays(provinceCode)
    }

    private fun getHolidays(provinceCode: String?) {
        disposable.add(
            apiService.getHolidays(provinceCode, getYearPref())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HolidayModel>() {
                    override fun onSuccess(list: HolidayModel) {
                        holidays.value = list.province.holidays
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        holidays.value = null
                        loadError.value = e.message
                    }
                })
        )
    }

    /**
     * To avoid memory leaks
     */
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}