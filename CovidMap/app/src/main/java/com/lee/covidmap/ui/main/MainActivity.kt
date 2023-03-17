package com.lee.covidmap.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.lee.covidmap.R
import com.lee.covidmap.common.base.BaseActivity
import com.lee.covidmap.data.model.remote.Center
import com.lee.covidmap.databinding.ActivityMainBinding
import com.lee.covidmap.databinding.CenterInfoWindowBinding
import com.lee.covidmap.ui.main.viewmodel.MainViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

/**
 * Main 화면
 * **/

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main){
    private val viewModel : MainViewModel by viewModels()
    private lateinit var naverMap : NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCenterList()
        binding.mapView.getMapAsync(MapReadyCallback())
    }

    /**
     * LiveData 관찰하는 함수
     * **/
    override fun observeData() {
        with(viewModel){
            centerList.observe(this@MainActivity){ list ->
                Log.d(TAG, "observeData: ${list.size}")
                makeMarkers(list)
            }
        }
    }

    /**
     * Listener 등록 함수
     * **/
    override fun addListeners() {

    }

    /**
     * 지도에 마커 찍는 함수
     * - list : 전달 받은 선별소 목록
     * **/
    private fun makeMarkers(list : List<Center>) {
        if(::naverMap.isInitialized){
            lifecycleScope.launch{
                list.asFlow().collect{ center ->
                    val marker = Marker()
                    marker.apply { // 마커 설정
                        position = LatLng(center.lat.toDouble() , center.lng.toDouble()) // 위치

                        icon = when(center.centerType){ // 아이콘
                            getString(R.string.local) -> OverlayImage.fromResource(R.drawable.ic_local_loaction_24)
                            getString(R.string.central) -> OverlayImage.fromResource(R.drawable.ic_central_location_24)
                            else -> {
                                Log.d(TAG, "makeMarkers: not defined type ${center.centerType}")
                                OverlayImage.fromResource(R.drawable.ic_local_loaction_24)
                            }
                        }
                        onClickListener = Overlay.OnClickListener { overlay -> // 클릭리스너
                            val selectedMarker = overlay as Marker
                            viewModel.setSelectedCenter(center)
                            openToggleInfoWindow(selectedMarker)
                            true
                        }
                        map = naverMap // 맵
                    }
                }
            }
        }
    }

    /**
     * 정보창 열기 / 닫기 설정하는 함수
     * - selectedMarker : 현재 선택된 마커
     * **/
    private fun openToggleInfoWindow(selectedMarker : Marker){
        if(selectedMarker.infoWindow == null){ // 정보창이 열려 있지 않을때
            val infoWindow = InfoWindow()
            infoWindow.run {
                adapter = CenterAdapter(this@MainActivity)
                open(selectedMarker)
            }
            val cameraUpdate = CameraUpdate.scrollTo(selectedMarker.position)
            val cameraZoom = CameraUpdate.zoomTo(16.0)
            naverMap.run {
                moveCamera(cameraUpdate)
                moveCamera(cameraZoom)
            }
        } else { // 정보창이 열려있을때
            selectedMarker.infoWindow?.close()
        }
    }

    /**
     * Map 준비 콜백
     * **/
    private inner class MapReadyCallback : OnMapReadyCallback {
        override fun onMapReady(result : NaverMap) {
            Log.d(TAG, "onMapReady()")
            naverMap = result
        }
    }

    /**
     * 정보창 Adapter
     * **/
    private inner class CenterAdapter(private val ctx : Context) : InfoWindow.DefaultViewAdapter(ctx){

        override fun getContentView(infoWindow: InfoWindow): View {
            val parent = binding.root as ViewGroup
            val infoBinding : CenterInfoWindowBinding = DataBindingUtil.inflate(
                LayoutInflater.from(ctx) , R.layout.center_info_window , parent , false
            )
            with(infoBinding){
                val center = viewModel.selectedCenter.value!! // 선택된 마커
                addressTV.apply {
                    text = center.address
                    isSelected = true
                }
                centerNameTV.apply {
                    text = center.centerName
                    isSelected = true
                }

                facilityNameTV.run{
                    text = center.facilityName
                    isSelected = true
                }
                phoneNumberTV.run{
                    text = center.phoneNumber
                    isSelected = true
                }
                updateAtTV.run{
                    text = center.updatedAt
                    isSelected = true
                }
            }
            return infoBinding.root
        }
    }
}