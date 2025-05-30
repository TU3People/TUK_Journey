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

        var isSearching = false
        binding.searchButton.setOnClickListener {
            if(isSearching) return@setOnClickListener
            isSearching = false

            val keyword = binding.searchInput.text.toString().trim()
            if (keyword.isNotEmpty()) {
                historyManager.addKeyword(keyword)
                keywordAdapter.updateList(historyManager.getHistory())
                searchKeyword(keyword)
            }
            binding.searchButton.postDelayed({isSearching = false}, 1000)
        }
        binding.clearHistory.setOnClickListener {
            historyManager.clearHistory()
            keywordAdapter.updateList(emptyList())
        }

    }

    private fun searchKeyword(query: String) {
        lifecycleScope.launch {
            try {
                val lat = if (currentLat == 0.0) 37.340174 else currentLat
                val lng = if (currentLng == 0.0) 126.7335933 else currentLng

                if (currentLat == 0.0 || currentLng == 0.0) {
                    Toast.makeText(requireContext(), "위치 정보가 없어 기본 위치 사용", Toast.LENGTH_SHORT).show()
                }

                val response = RetrofitClient.kakaoApi.searchKeyword(
                    query = query,
                    longitude = lng.toString(),
                    latitude = lat.toString(),
                    radius = 1500,
                    sort = "accuracy"
                )

                if (response.isSuccessful) {
                    val placeList = response.body()?.documents.orEmpty()
                    Log.d("SearchDebug", "총 검색 결과 수: ${placeList.size}")

                    val filtered = placeList.filter { item: KakaoPlace ->
                        val x = item.x?.toDoubleOrNull()
                        val y = item.y?.toDoubleOrNull()
                        if (x == null || y == null) return@filter false

                        val dx = x - lng
                        val dy = y - lat
                        val distance = sqrt(dx * dx + dy * dy) * 111000
                        distance <= 1500
                    }

                    val top5 = filtered.sortedBy { item: KakaoPlace ->
                        val x = item.x?.toDoubleOrNull() ?: Double.MAX_VALUE
                        val y = item.y?.toDoubleOrNull() ?: Double.MAX_VALUE
                        val dx = x - lng
                        val dy = y - lat
                        dx * dx + dy * dy
                    }.take(5)

                    Log.d("SearchDebug", "반경 1500m 내 결과 수: ${filtered.size}")

                    if (top5.isNotEmpty()) {
                        val bundle = Bundle().apply {
                            putParcelableArrayList("places", top5.toCollection(ArrayList()))
                        }

                        parentFragmentManager.setFragmentResult("place_list_result", bundle)
                        parentFragmentManager.popBackStack()
                        return@launch
                    }





                    if (placeList.isNotEmpty()) {
                        Toast.makeText(requireContext(), "반경 내 결과는 없어 전국 결과를 표시합니다.", Toast.LENGTH_SHORT).show()
                        onSearchResultClicked(placeList[0])
                    } else {
                        Toast.makeText(requireContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
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
