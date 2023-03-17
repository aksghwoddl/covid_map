package com.lee.covidmap.domain.repository

import com.lee.covidmap.common.NetworkResult
import com.lee.covidmap.data.model.local.CenterEntity
import com.lee.covidmap.data.model.remote.Center
import kotlinx.coroutines.flow.Flow


interface MainRepository {
    /**
     * 코로나 접종센터 목록을 불러오는 함수
     * - page : 불러올 페이지
     * - perPage : 몇개씩 불러올건지 설정
     * - key : 인증키
     * **/
    suspend fun getCovidCenter(
        page : Int ,
        perPage : Int ,
        key : String
    ) : Flow<NetworkResult<List<Center>>>

    /**
     * 코로나 접종센터를 insert하는 함수
     * - centerEntity : insert할 진료소의 Entity
     * * **/
    suspend fun insertCenter(center: CenterEntity)

    /**
     * 저장된 접종센터 목록을 불러오는 함수
     * **/
    suspend fun getCenterList() : Flow<List<Center>>
}