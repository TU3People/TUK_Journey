package com.example.journey

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.journey.adapter.CafeListAdapter
import com.example.journey.api.RetrofitClient
import com.example.journey.databinding.FragmentScafeBinding
import com.example.journey.model.KakaoPlace
import kotlinx.coroutines.launch


class ScafeFragment : Fragment() {

    private var _binding: FragmentScafeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CafeListAdapter
    private var isFirstLoad = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentScafeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CafeListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            isFirstLoad = false
            fetchCafeData("카페", "126.7426", "37.3514")
        }
    }

    fun search(query: String) {
        fetchCafeData(query, "126.7426", "37.3514")
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
                        Log.d("ScafeFragment", "받은 카페 수: ${it.size}")
                        adapter.submitList(it)
                    }
                } else {
                    Log.e("ScafeFragment", "API 오류: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ScafeFragment", "네트워크 오류: ${e.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

