package com.example.journey

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journey.api.RetrofitClient
import com.example.journey.databinding.FragmentNcafeBinding
import com.example.journey.model.KakaoPlace
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class NcafeFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentNcafeBinding? = null
    private val binding get() = _binding!!

    private val cafeList = mutableListOf<KakaoPlace>()
    private lateinit var adapter: CafeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNcafeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CafeAdapter(cafeList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        checkLocationPermissionAndFetch()
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
                    Log.e("NcafeFragment", "위치 정보를 가져올 수 없음")
                }
            }
            .addOnFailureListener {
                Log.e("NcafeFragment", "위치 요청 실패: ${it.message}")
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
                        cafeList.clear()
                        cafeList.addAll(it)
                        adapter.notifyDataSetChanged()
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


class CafeAdapter(private val items: List<KakaoPlace>) :
    RecyclerView.Adapter<CafeAdapter.CafeViewHolder>() {

    inner class CafeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.cafeTitle)
        val address: TextView = view.findViewById(R.id.cafeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cafe_card, parent, false)
        return CafeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.name
        holder.address.text = item.roadAddress
    }

    override fun getItemCount(): Int = items.size
}
