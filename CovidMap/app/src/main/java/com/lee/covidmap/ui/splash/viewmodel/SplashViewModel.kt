package com.lee.covidmap.ui.splash.viewmodel

import android.util.Log
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

    private val _endProgress = MutableLiveData<Boolean>()
    val endProgress : LiveData<Boolean>
    get() = _endProgress

    private lateinit var insertJob : Job

    /**
     * Task 시작
     * **/
    fun startProgress() {
        viewModelScope.launch {
                flow{
                    for(i in 1.. 10){
                        emit(i)
                        delay(PROGRESS_DELAY)
                    }
                }.collect{ page ->
                    repository.getCovidCenter(page , PER_PAGE , BuildConfig.COVID_API_KEY).collect{ result ->
                        when(result){
                            is NetworkResult.Success -> insertCenterList(result.data)
                            is NetworkResult.Failure -> Log.d( TAG, "observeData: ${result.code}")
                            is NetworkResult.Exception -> Log.d( TAG , "observeData: ${result.errorMessage}")
                            is NetworkResult.Loading -> {}
                        }
                    }
                    val progress =  page * 10
                    _progress.value = progress
                    if(progress == STOP_PROGRESS){
                        if(::insertJob.isInitialized){
                            insertJob.join()
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
    private fun insertCenterList(list : List<Center>) {
        insertJob = CoroutineScope(Dispatchers.IO).launch {
            list.forEach{ center ->
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