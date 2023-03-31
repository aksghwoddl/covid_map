package com.lee.covidmap.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.lee.covidmap.R
import com.lee.covidmap.common.Utils
import com.lee.covidmap.common.base.BaseActivity
import com.lee.covidmap.databinding.ActivitySplashBinding
import com.lee.covidmap.ui.main.MainActivity
import com.lee.covidmap.ui.splash.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SplashActivity"
/**
 * Splash 화면
 * **/
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash){
    private val viewModel : SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkNetwork()
        binding.apply {
            splashViewModel = viewModel
            lifecycleOwner = this@SplashActivity
        }
    }

    /**
     * LiveData 관찰하는 함수
     * **/
    override fun observeData() {
        with(viewModel){
            endProgress.observe(this@SplashActivity){
                if(it){
                    startMainActivity()
                }
            }
        }
    }

    /**
     * Listener 등록 함수
     * **/
    override fun addListeners() {

    }

    /**
     * 네트워크 연결상태 확인 하는 함수
     * **/
    private fun checkNetwork() {
        if(Utils.checkNetworkConnection(this@SplashActivity)){
            Utils.checkPermission(this@SplashActivity , PermissionListener(viewModel , this@SplashActivity))
        } else {
            AlertDialog.Builder(this@SplashActivity)
                .setTitle(getString(R.string.network))
                .setMessage(getString(R.string.dialog_check_network))
                .setPositiveButton(getString(R.string.confirm)){ dialog , _ ->
                    checkNetwork()
                    dialog.dismiss()
                }.create().show()
        }
    }

    /**
     * 권한을 확인하는 Listener
     * **/
    private class PermissionListener(
        private val viewModel : SplashViewModel ,
        private val context : Context
        ) : com.gun0912.tedpermission.PermissionListener {
        override fun onPermissionGranted() {
            viewModel.startProgress()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.need_location_permission))
                .setPositiveButton(context.getString(R.string.confirm)){ dialog , _ ->
                    Utils.checkPermission(context , this)
                    dialog.dismiss()
                }
                .create().show()
        }
    }

    private fun startMainActivity() {
        with(Intent(this@SplashActivity , MainActivity::class.java)){
            startActivity(this)
            finish()
        }
    }
}