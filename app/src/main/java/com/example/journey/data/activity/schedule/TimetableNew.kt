package com.example.journey.data.activity.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.journey.databinding.FragmentTimetableNewBinding

class TimetableNewFragment : Fragment() {

    private var _binding: FragmentTimetableNewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimetableNewBinding.inflate(inflater, container, false)
        // ‘저장’ 버튼 클릭 리스너 등 설정
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}