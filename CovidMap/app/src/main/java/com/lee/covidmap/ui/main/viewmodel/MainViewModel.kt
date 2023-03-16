package com.lee.covidmap.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.covidmap.data.model.remote.Center
import com.lee.covidmap.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Main화면 ViewModel
 * **/

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _centerList = MutableLiveData<List<Center>>()
    val centerList : LiveData<List<Center>>
    get() = _centerList

    /**
     * 저장된 선별소 목록 불러오기
     * **/
    fun getCenterList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCenterList().collect{ list ->
                _centerList.postValue(list)
            }
        }
    }
}