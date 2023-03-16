package com.lee.covidmap.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.lee.covidmap.R
import com.lee.covidmap.common.base.BaseActivity
import com.lee.covidmap.databinding.ActivityMainBinding
import com.lee.covidmap.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main 화면
 * **/

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main){
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCenterList()
    }

    /**
     * LiveData 관찰하는 함수
     * **/
    override fun observeData() {
        with(viewModel){
            centerList.observe(this@MainActivity){ list -> // 선별소 목록
                Log.d(TAG, "observeData: ${list.size}")
            }
        }
    }

    /**
     * Listener 등록 함수
     * **/
    override fun addListeners() {

    }
}