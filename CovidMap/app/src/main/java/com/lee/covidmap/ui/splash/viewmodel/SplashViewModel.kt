package com.lee.covidmap.ui.splash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.covidmap.BuildConfig
import com.lee.covidmap.common.NetworkResult
import com.lee.covidmap.data.model.local.CenterEntity
import com.lee.covidmap.data.model.remote.Center
import com.lee.covidmap.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Splash 화면 ViewModel
 * **/

private const val TAG = "SplashViewModel"
private const val PER_PAGE = 10
private const val PROGRESS_DELAY = 200L
private const val STOP_PROGRESS = 80
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

    private val _endProgress = MutableLiveData<Boolean>()
    val endProgress : LiveData<Boolean>
    get() = _endProgress

    private lateinit var insertJob : Job

    /**
     * Task 시작
     * **/
    fun startProgress() {
        viewModelScope.launch {
            for(i in 1..10){
                flow{
                    emit(i)
                    delay(PROGRESS_DELAY)
                }.collect{ page ->
                    repository.getCovidCenter(page , PER_PAGE , BuildConfig.COVID_API_KEY).collect{ list ->
                        _covidList.value = list
                    }
                    val progress =  page * 10
                    _progress.value = progress
                    if(progress == STOP_PROGRESS){
                        if(::insertJob.isInitialized){
                            insertJob.join()
                        }
                    }
                }
            }
            _endProgress.value = true
        }
    }

    /**
     * 접종센터 목록 저장하기
     * list : 저장할 목록
     * **/
    fun insertCenterList(list : List<Center>) {
        val centerFlow = list.asFlow()
        insertJob = CoroutineScope(Dispatchers.IO).launch {
            centerFlow.collect{ center ->
                val centerEntity = CenterEntity(center.id , center)
                repository.insertCenter(centerEntity)
            }
        }
    }

    override fun onCleared() {
        if(::insertJob.isInitialized){
            insertJob.cancel()
        }
        super.onCleared()
    }

}