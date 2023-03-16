package com.lee.covidmap.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lee.covidmap.common.DatabaseConst
import com.lee.covidmap.data.model.remote.Center

@Entity(tableName = DatabaseConst.TABLE_NAME)
data class CenterEntity (
    @PrimaryKey(autoGenerate = true)
    var index : Int? ,
    val center : Center
    )