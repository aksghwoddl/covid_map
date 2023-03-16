package com.lee.covidmap.common

import android.content.Context
import android.net.ConnectivityManager

class Utils {
    companion object{
        /**
         * 네트워크 상태 체크하는 함수
         * **/
        fun checkNetworkConnection(context : Context) : Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetwork
            return networkInfo.toString() != "null"
        }
    }
}

class NetworkConst {
    companion object{
        const val BASE_URL = "https://api.odcloud.kr/"
        const val COVID_CENTER_URL = "https://api.odcloud.kr/api/15077586/v1/centers"

        const val PAGE = "page"
        const val PER_PAGE = "perPage"
        const val SERVICE_KEY = "serviceKey"
    }
}