import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journey.R
import com.example.journey.data.activity.main.KeywordAdapter
import com.example.journey.data.remote.network.RetrofitClient
import com.example.journey.data.remote.model.cafe.KakaoPlace
import com.example.journey.databinding.FragmentSearchBinding
import com.example.journey.util.SearchHistoryManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import kotlin.math.*

class SearchFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyManager: SearchHistoryManager
    private lateinit var keywordAdapter: KeywordAdapter

    private var currentLat: Double = 0.0
    private var currentLng: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationIcon(R.drawable.backicon)
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        currentLat = arguments?.getDouble("lat") ?: 0.0
        currentLng = arguments?.getDouble("lng") ?: 0.0

        historyManager = SearchHistoryManager(requireContext())

        keywordAdapter = KeywordAdapter(historyManager.getHistory()) { keyword ->
            binding.searchInput.setText(keyword)
            searchKeyword(keyword) // 기록 클릭 시 검색 실행
        }

        binding.searchResultRecycler.adapter = keywordAdapter
        binding.searchResultRecycler.layoutManager = LinearLayoutManager(requireContext())


        binding.searchButton.setOnClickListener {
            val keyword = binding.searchInput.text.toString().trim()
            if (keyword.isNotEmpty()) {
                historyManager.addKeyword(keyword)
                keywordAdapter.updateList(historyManager.getHistory())
                searchKeyword(keyword)
            }
        }
        binding.clearHistory.setOnClickListener {
            historyManager.clearHistory()
            keywordAdapter.updateList(emptyList())
        }

    }

    private fun searchKeyword(query: String) {
        lifecycleScope.launch {
            try {
                if (currentLat == 0.0 || currentLng == 0.0) {
                    Toast.makeText(requireContext(), "위치 정보를 가져오는 중입니다.", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val response = RetrofitClient.kakaoApi.searchKeyword(
                    query = query,
                    longitude = currentLng.toString(),
                    latitude = currentLat.toString(),
                    radius = 1500,
                    sort = "accuracy"
                )

                if (response.isSuccessful) {
                    val placeList = response.body()?.documents.orEmpty()
                    Log.d("SearchDebug", "총 검색 결과 수: ${placeList.size}")

                    // 위치 기반 거리 필터링
                    val filtered = placeList.filter {
                        val dx = it.x.toDouble() - currentLng
                        val dy = it.y.toDouble() - currentLat
                        val distance = sqrt(dx * dx + dy * dy) * 111000
                        distance <= 1500
                    }

                    Log.d("SearchDebug", "반경 1500m 내 결과 수: ${filtered.size}")

                    when {
                        filtered.isNotEmpty() -> {
                            onSearchResultClicked(filtered[0])
                        }
                        placeList.isNotEmpty() -> {
                            Toast.makeText(requireContext(), "반경 내 결과는 없어 전국 결과를 표시합니다.", Toast.LENGTH_SHORT).show()
                            onSearchResultClicked(placeList[0])
                        }
                        else -> {
                            Toast.makeText(requireContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(requireContext(), "API 호출 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("SearchDebug", "검색 예외 발생: ${e.message}")
                Toast.makeText(requireContext(), "검색 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun onSearchResultClicked(item: KakaoPlace) {
        val bundle = Bundle().apply {
            putString("name", item.name)
            putDouble("lat", item.y.toDouble())
            putDouble("lng", item.x.toDouble())
        }

        parentFragmentManager.setFragmentResult("place_result", bundle)
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val container = activity?.findViewById<FrameLayout>(R.id.search_fragment_container)
        container?.visibility = View.GONE
        _binding = null
    }
}
