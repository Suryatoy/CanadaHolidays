package com.surya.canadaholidays.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.surya.canadaholidays.di.DaggerProvinceListViewModelComponent
import com.surya.canadaholidays.model.Province
import com.surya.canadaholidays.model.ProvinceAPIService
import com.surya.canadaholidays.model.ProvinceModel
import com.surya.canadaholidays.util.getYear
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class ProvinceListViewModel() : ViewModel() {
    val provinces by lazy { MutableLiveData<List<Province>>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val disposable = CompositeDisposable()

    @Inject
    lateinit var apiService: ProvinceAPIService

    init {
        DaggerProvinceListViewModelComponent.create().inject(this)
    }

    fun refresh() {
        loading.value = true
        getProvinces()
    }

    private fun getProvinces() {
        disposable.add(
            apiService.getProvinces(getYear())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProvinceModel>() {
                    override fun onSuccess(list: ProvinceModel) {
                        loadError.value = false
                        provinces.value = list.provinces
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        provinces.value = null
                        loadError.value = true
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