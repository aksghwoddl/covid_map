package com.lee.covidmap.data.repository

import com.lee.covidmap.R
import com.lee.covidmap.common.NetworkResult
import com.lee.covidmap.common.provider.ResourceProvider
import com.lee.covidmap.data.api.RestApi
import com.lee.covidmap.data.model.local.CenterEntity
import com.lee.covidmap.data.model.remote.Center
import com.lee.covidmap.data.room.dao.CenterDAO
import com.lee.covidmap.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val restApi: RestApi ,
    private val centerDAO: CenterDAO ,
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

    /**
     * 코로나 선별소를 insert하는 함수
     * - centerEntity : insert할 선별소의 Entity
     * - 반환값 : insert된 rowId
     * * **/
    override suspend fun insertCenter(center: CenterEntity){
        return centerDAO.insertCenter(center)
    }

    /**
     * 저장된 선별소 목록을 불러오는 함수
     * **/
    override suspend fun getCenterList(): Flow<List<Center>> {
        return flow {
            val centerEntity = centerDAO.getCenterList()
            val centerList = ArrayList<Center>()
            centerEntity.forEach { entity ->
                centerList.add(entity.center)
            }
            emit(centerList.toList())
        }
    }
}