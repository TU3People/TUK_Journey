package com.example.journey

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journey.api.RetrofitClient
import com.example.journey.databinding.FragmentNcafeBinding
import com.example.journey.model.CafeItem
import com.example.journey.model.KakaoPlace
import kotlinx.coroutines.launch

class NcafeFragment : Fragment() {

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

        // 테스트용 고정 좌표 (서울시청)
        val lat = 37.5665
        val lng = 126.9780
        fetchCafeData("카페", lng.toString(), lat.toString())
    }

    fun search(query: String) {
        val lat = 37.5665
        val lng = 126.9780
        fetchCafeData(query, lng.toString(), lat.toString())
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




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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


