package com.lee.covidmap.data.api

import com.lee.covidmap.common.NetworkConst
import com.lee.covidmap.data.model.remote.CovidCenterResultDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApi {
    @GET(NetworkConst.COVID_CENTER_URL)
    suspend fun getCovidCenter(
        @Query(NetworkConst.PAGE) page : Int ,
        @Query(NetworkConst.PER_PAGE) perPage : Int ,
        @Query(NetworkConst.SERVICE_KEY) key : String
    ) : Response<CovidCenterResultDTO>
}