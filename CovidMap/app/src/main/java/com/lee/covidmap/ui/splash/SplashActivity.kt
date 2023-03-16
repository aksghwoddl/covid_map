package com.lee.covidmap.ui.splash

import android.annotation.SuppressLint
import com.lee.covidmap.R
import com.lee.covidmap.common.base.BaseActivity
import com.lee.covidmap.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash){

    /**
     * LiveData 관찰하는 함수
     * **/
    override fun observeData() {

    }

    /**
     * Listener 등록 함수
     * **/
    override fun addListeners() {

    }
}