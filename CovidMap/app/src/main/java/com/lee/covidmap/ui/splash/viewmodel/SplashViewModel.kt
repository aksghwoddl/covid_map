package com.lee.covidmap.ui.splash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.covidmap.BuildConfig
import com.lee.covidmap.common.NetworkResult
import com.lee.covidmap.data.model.remote.Center
import com.lee.covidmap.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PER_PAGE = 10
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _progress = MutableLiveData<Int>()
    val progress : LiveData<Int>
    get() = _progress

    private val _covidList = MutableLiveData<NetworkResult<List<Center>>>()
    val covidList : LiveData<NetworkResult<List<Center>>>
    get() = _covidList

    fun startProgress() {
        viewModelScope.launch {
            for(i in 1..10){
                flow{
                    emit(i)
                    delay(200)
                }.collect{ page ->
                    repository.getCovidCenter(page , PER_PAGE , BuildConfig.COVID_API_KEY).collect{ list ->
                        _covidList.value = list
                    }
                    _progress.value = page * 10
                }
            }
        }
    }

}