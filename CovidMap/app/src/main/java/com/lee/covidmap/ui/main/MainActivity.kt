package com.lee.covidmap.ui.main

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.lee.covidmap.R
import com.lee.covidmap.common.Utils
import com.lee.covidmap.common.base.BaseActivity
import com.lee.covidmap.data.model.remote.Center
import com.lee.covidmap.databinding.ActivityMainBinding
import com.lee.covidmap.databinding.CenterInfoWindowBinding
import com.lee.covidmap.ui.main.viewmodel.MainViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
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
    private var naverMap : NaverMap? = null
    private var currentLocationMarker : Marker? = null
    private val markers = arrayListOf<Marker>()
    private var mapFragment : MapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMapView()
    }

    override fun onDestroy() {
        clearAllInstances()
        super.onDestroy()
    }

    /**
     * 모든 인스턴스 해제하는 함수
     * **/
    private fun clearAllInstances() {
        currentLocationMarker?.let { marker -> // 현재위치 마커
            marker.map = null
        }

        if(markers.isNotEmpty()){ // 접종센터 마커
            markers.forEach { marker ->
                marker.infoWindow?.close()
                marker.map = null
            }
            markers.clear()
        }

        mapFragment?.let { fragment -> // 네이버 맵
            supportFragmentManager.beginTransaction().remove(fragment)
        }

        currentLocationMarker = null
        naverMap = null
        mapFragment = null
    }

    /**
     * LiveData 관찰하는 함수
     * **/
    override fun observeData() {
        with(viewModel){
            centerList.observe(this@MainActivity){ list -> // 접종센터 목록
                Log.d(TAG, "observeData: ${list.size}")
                makeMarkers(list)
            }

            currentLocation.observe(this@MainActivity){ location -> // 현재위치
                setCurrentLocationMarker(location)
            }
        }
    }

    /**
     * Listener 등록 함수
     * **/
    override fun addListeners() {
        with(binding){
            currentLocationBtn.setOnClickListener { // 현재위치 버튼
                getCurrentLocation()
            }
        }
    }

    /**
     * MapView 초기화 하는 함수
     * **/
    private fun initMapView() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as MapFragment?
            ?: MapFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.mapView, it).commit()
            }
        mapFragment?.getMapAsync(MapReadyCallback())
    }

    /**
     * 현재 위치 받아오는 함수
     * **/
    private fun getCurrentLocation() {
        Utils.checkPermission(this@MainActivity , PermissionListener(this@MainActivity))
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        with(viewModel){
            gpsLocation?.let { location -> // GPS를 통해 현재위치를 정상적으로 받아왔을때
                setCurrentLocation(location)
            }?:let { // 현재위치를 받아오지 못했을때
                networkLocation?.let { location ->// 네트워크 Provider를 통해 위치 받음
                    setCurrentLocation(location)
                }?: setToastMessage(getString(R.string.fail_find_current_location)) // 둘 다 실패할 경우 toast message 띄움
            }
        }
    }

    /**
     * 현재위치 마커 표시하기
     * - location : 전달받은 현재위치
     * **/
    private fun setCurrentLocationMarker(location : Location) {
        if(currentLocationMarker == null){
            currentLocationMarker = Marker()
        }
        currentLocationMarker?.let { marker ->
            marker.apply {
                map = null
                position = LatLng(location.latitude , location.longitude)
                icon = OverlayImage.fromResource(R.drawable.ic_current_location_24)
                map = naverMap
            }
            markers.add(marker)
            val cameraUpdate = CameraUpdate.scrollTo(marker.position)
            naverMap?.let { map ->
                map.moveCamera(cameraUpdate)
            }
        }
    }

    /**
     * 지도에 마커 찍는 함수
     * - list : 전달 받은 접종센터 목록
     * **/
    private fun makeMarkers(list : List<Center>) {
        if(naverMap != null){
            Log.d(TAG, "makeMarkers()")
            lifecycleScope.launch{
                list.forEach { center ->
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
                adapter = CenterAdapter(this@MainActivity , viewModel, binding)
                open(selectedMarker)
            }
            val cameraUpdate = CameraUpdate.scrollTo(selectedMarker.position)
            val cameraZoom = CameraUpdate.zoomTo(16.0)
            naverMap?.let { map ->
                map.run {
                    moveCamera(cameraUpdate)
                    moveCamera(cameraZoom)
                }
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
            getCurrentLocation() // 시작시 바로 현재위치 찍음
            viewModel.getCenterList()
        }
    }

    /**
     * 정보창 Adapter
     * **/
    private inner class CenterAdapter(
        private val activity: MainActivity ,
        private val viewModel : MainViewModel ,
        private val binding: ActivityMainBinding ,
    ) : InfoWindow.DefaultViewAdapter(activity.baseContext){

        override fun getContentView(infoWindow: InfoWindow): View {
            val parent = binding.root as ViewGroup
            val infoBinding : CenterInfoWindowBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity.baseContext) , R.layout.center_info_window , parent , false
            )
            with(infoBinding){
                mainViewModel = viewModel
                lifecycleOwner = activity
            }
            return infoBinding.root
        }
    }
    /**
     * 권한을 확인하는 Listener
     * **/
    private class PermissionListener(private val context: Context) : com.gun0912.tedpermission.PermissionListener {
        override fun onPermissionGranted() {
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
}