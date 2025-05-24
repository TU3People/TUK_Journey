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
import com.example.journey.data.api.RetrofitClient
import com.example.journey.databinding.FragmentNcafeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch


class NcafeFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentNcafeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CafeListAdapter
    private var isFirstLoad = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNcafeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CafeListAdapter()
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

    private fun checkLocationPermissionAndFetch(query: String = "카페") {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            getLastLocationAndFetchCafe(query)
        }
    }

    private fun getLastLocationAndFetchCafe(query: String = "카페") {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("NcafeFragment", "위치 권한 없음")
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude.toString()
                    val lng = location.longitude.toString()
                    Log.d("NcafeFragment", "현재 위치: $lat, $lng")
                    fetchCafeData(query, lng, lat)
                } else {
                    val defaultLat = "37.340174"
                    val defaultLng = "126.7335933"
                    Log.w("NcafeFragment", "위치 없음, 기본 위치 사용: $defaultLat, $defaultLng")
                    fetchCafeData(query, defaultLng, defaultLat)
                }
            }
            .addOnFailureListener {
                val defaultLat = "37.340174"
                val defaultLng = "126.7335933"
                Log.e("NcafeFragment", "위치 요청 실패, 기본 위치 사용: $defaultLat, $defaultLng (${it.message})")
                fetchCafeData(query, defaultLng, defaultLat)
            }
    }


    private fun fetchCafeData(query: String, lng: String, lat: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.kakaoApi.searchKeyword(
                    query = query,
                    longitude = lng,
                    latitude = lat
                )

                if (response.isSuccessful) {
                    response.body()?.documents?.let {
                        Log.d("NcafeFragment", "받은 카페 수: ${it.size}")
                        adapter.submitList(it)
                    }
                } else {
                    Log.e("NcafeFragment", "API 오류: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("NcafeFragment", "네트워크 오류: ${e.message}")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("NcafeFragment", "위치 권한 허용됨")
                getLastLocationAndFetchCafe()
            } else {
                Log.e("NcafeFragment", "위치 권한 거부됨")
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
