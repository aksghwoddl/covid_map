package com.lee.covidmap.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lee.covidmap.common.DatabaseConst
import com.lee.covidmap.data.model.local.CenterEntity

@Dao
interface CenterDAO {
    /**
     * 센터 데이터베이스에 insert하기
     * - centerEntity : insert할 접종센터의 Entity
     * **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCenter(centerEntity : CenterEntity)

    @Query("SELECT * FROM ${DatabaseConst.TABLE_NAME}")
    suspend fun getCenterList() : MutableList<CenterEntity>
}