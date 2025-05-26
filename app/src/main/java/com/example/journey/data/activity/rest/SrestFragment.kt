package com.example.journey.data.activity.rest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journey.data.remote.network.RetrofitClient
import com.example.journey.databinding.FragmentScafeBinding
import kotlinx.coroutines.launch


class SrestFragment : Fragment() {

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
            fetchCafeData("맛집", "126.7426", "37.3514")
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
                        Log.d("SrestFragment", "받은 맛집 수: ${it.size}")
                        adapter.submitList(it)
                    }
                } else {
                    Log.e("SrestFragment", "API 오류: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("SrestFragment", "네트워크 오류: ${e.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

