package com.example.journey.data.activity.cafe

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journey.data.remote.model.cafe.KakaoPlace
import com.example.journey.data.remote.network.RetrofitClient
import com.example.journey.databinding.FragmentEntertainmentBinding
import com.example.journey.databinding.FragmentNcafeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch


class EntertainmentFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentEntertainmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ActivListAdapter
    private var isFirstLoad = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEntertainmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ActivListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            isFirstLoad = false
            checkLocationPermissionAndFetch()
        }
    }

    fun search(query: String) {
        checkLocationPermissionAndFetch(query)
    }

    private fun checkLocationPermissionAndFetch(query: String = "놀거리") {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            getLastLocationAndFetchCafe(query)
        }
    }

    private fun getLastLocationAndFetchCafe(query: String = "놀거리") {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("EntertainmentFragment", "위치 권한 없음")
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude.toString()
                    val lng = location.longitude.toString()
                    Log.d("EntertainmentFragment", "현재 위치: $lat, $lng")
                    fetchMultipleKeywords(lng, lat)
                } else {
                    val defaultLat = "37.340174"
                    val defaultLng = "126.7335933"
                    Log.w("EntertainmentFragment", "위치 없음, 기본 위치 사용: $defaultLat, $defaultLng")
                    fetchMultipleKeywords( defaultLng, defaultLat)
                }
            }
            .addOnFailureListener {
                val defaultLat = "37.340174"
                val defaultLng = "126.7335933"
                Log.e("EntertainmentFragment", "위치 요청 실패, 기본 위치 사용: $defaultLat, $defaultLng (${it.message})")
                fetchMultipleKeywords( defaultLng, defaultLat)
            }
    }


    private fun fetchMultipleKeywords(lng: String, lat: String) {
        lifecycleScope.launch {
            try {
                val resultList = mutableListOf<KakaoPlace>()

                val queries = listOf("놀거리", "오락시설","테마파크","전시","PC방")

                for (query in queries) {
                    val response = RetrofitClient.kakaoApi.searchKeyword(
                        query = query,
                        longitude = lng,
                        latitude = lat
                    )

                    if (response.isSuccessful) {
                        val list = response.body()?.documents.orEmpty()
                        resultList.addAll(list)
                    }
                }

                // 중복 제거 (장소 이름 + 주소 기준)
                val distinctList = resultList.distinctBy { it.name + it.roadAddress }

                Log.d("NatureFragment", "최종 결과 수: ${distinctList.size}")
                adapter.submitList(distinctList)

            } catch (e: Exception) {
                Log.e("NatureFragment", "네트워크 오류: ${e.message}")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("EntertainmentFragment", "위치 권한 허용됨")
                getLastLocationAndFetchCafe()
            } else {
                Log.e("EntertainmentFragment", "위치 권한 거부됨")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
