package com.example.journey.data.activity.schedule


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.journey.databinding.FragmentTimetableListBinding

class TimetableListFragment : Fragment() {

    private var _binding: FragmentTimetableListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimetableListBinding.inflate(inflater, container, false)
        // RecyclerView 어댑터 등은 여기서 연결
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
