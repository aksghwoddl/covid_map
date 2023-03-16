package com.lee.covidmap.data.room

import com.google.gson.Gson
import com.lee.covidmap.data.model.remote.Center

/**
 * Room의 data를 converting하기위한 TypeConverter class
 * **/
class TypeConverter {
    @androidx.room.TypeConverter
    fun recentKeywordToJson(center : Center) : String = Gson().toJson(center)

    @androidx.room.TypeConverter
    fun jsonToRecentKeyword(json : String) : Center = Gson().fromJson(json , Center::class.java)
}