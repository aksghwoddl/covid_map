package com.lee.covidmap.common

import android.content.Context
import android.net.ConnectivityManager

/**
 * 본 앱에서 Common하게 사용할 Util들을 모아놓은 class
 * **/
class Utils {
    companion object{
        /**
         * 네트워크 상태 체크하는 함수
         * - context : 전달받은 context
         * **/
        fun checkNetworkConnection(context : Context) : Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetwork
            return networkInfo.toString() != "null"
        }
    }
}

/**
 * 네트워크 관련 const들을 모아놓은 class
 * **/
class NetworkConst {
    companion object{
        const val BASE_URL = "https://api.odcloud.kr/"
        const val COVID_CENTER_URL = "https://api.odcloud.kr/api/15077586/v1/centers"

        const val PAGE = "page"
        const val PER_PAGE = "perPage"
        const val SERVICE_KEY = "serviceKey"
    }
}

/**
 * Database 관련 const들을 모아놓은 class
 * **/
class DatabaseConst {
    companion object{
        const val DB_NAME = "center.db"
        const val TABLE_NAME = "center_tbl"
    }
}