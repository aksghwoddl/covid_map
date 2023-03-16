package com.lee.covidmap.di

import android.content.Context
import androidx.room.Room
import com.lee.covidmap.BuildConfig
import com.lee.covidmap.common.DatabaseConst
import com.lee.covidmap.common.NetworkConst
import com.lee.covidmap.data.api.RestApi
import com.lee.covidmap.data.room.dao.CenterDAO
import com.lee.covidmap.data.room.db.CenterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvideModule  {
    /**
     * OkHttpClient를 주입하는 함수
     * **/
    @Provides
    @Singleton
    fun provideOkHttpClient() : OkHttpClient {
        return if(BuildConfig.DEBUG){ // BuildConfig가 Debug일때는 Interceptor를 추가한다.
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        } else { // BuildConfig가 Release일때는 Interceptor없이 OkHttp를 생성한다.
            OkHttpClient.Builder().build()
        }
    }

    /**
     * Rest Api를 주입하는 함수
     * **/
    @Provides
    @Singleton
    fun provideRestApi(okHttpClient: OkHttpClient) : RestApi {
        return Retrofit.Builder()
            .baseUrl(NetworkConst.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestApi::class.java)
    }

    /**
     * RoomDB 주입하는 함수
     * **/
    @Provides
    @Singleton
    fun provideDiaryDatabase(@ApplicationContext context : Context) : CenterDatabase {
        return Room.databaseBuilder(
            context ,
            CenterDatabase::class.java,
            DatabaseConst.DB_NAME,
        ).build()
    }

    /**
     * DiaryDAO 주입하는 함수
     * **/
    @Provides
    @Singleton
    fun provideDiaryDao(database : CenterDatabase) : CenterDAO {
        return database.recentDao()
    }
}