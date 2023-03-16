package com.lee.covidmap.data.repository

import com.lee.covidmap.R
import com.lee.covidmap.common.NetworkResult
import com.lee.covidmap.common.provider.ResourceProvider
import com.lee.covidmap.data.api.RestApi
import com.lee.covidmap.data.model.remote.Center
import com.lee.covidmap.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val restApi: RestApi ,
    private val resourceProvider: ResourceProvider
) : MainRepository {
    /**
     * 코로나 선별소 목록을 불러오는 함수
     * - page : 불러올 페이지
     * - perPage : 몇개씩 불러올건지 설정
     * - key : 인증키
     * **/
    override suspend fun getCovidCenter(
        page: Int,
        perPage: Int,
        key: String
    ): Flow<NetworkResult<List<Center>>> {
        return flow {
            emit(NetworkResult.Loading(true))
            val response = restApi.getCovidCenter(page, perPage, key)
            if(response.isSuccessful){
                emit(NetworkResult.Success(response.body()!!.list))
            } else {
                emit(NetworkResult.Failure(response.code()))
            }
        }.catch { exception ->
            when(exception){
                is SocketTimeoutException -> emit(NetworkResult.Exception(resourceProvider.getString(R.string.socket_timeout_exception)))
                else -> emit(NetworkResult.Exception(exception.message!!))
            }
        }
    }
}