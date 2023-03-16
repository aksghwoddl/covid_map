package com.lee.covidmap.data.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lee.covidmap.data.model.local.CenterEntity
import com.lee.covidmap.data.room.TypeConverter
import com.lee.covidmap.data.room.dao.CenterDAO

@Database(entities = [CenterEntity::class] , exportSchema = false , version = 1)
@TypeConverters(TypeConverter::class)
abstract class CenterDatabase : RoomDatabase() {
    abstract fun recentDao() : CenterDAO
}