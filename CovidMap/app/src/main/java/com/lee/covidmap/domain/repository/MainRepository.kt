package com.lee.covidmap.domain.repository

import com.lee.covidmap.common.NetworkResult
import com.lee.covidmap.data.model.remote.Center
import com.lee.covidmap.data.model.remote.CovidCenterResultDTO
import kotlinx.coroutines.flow.Flow


interface MainRepository {
    /**
     * 코로나 선별소 목록을 불러오는 함수
     * - page : 불러올 페이지
     * - perPage : 몇개씩 불러올건지 설정
     * - key : 인증키
     * **/
    suspend fun getCovidCenter(
        page : Int ,
        perPage : Int ,
        key : String
    ) : Flow<NetworkResult<List<Center>>>
}