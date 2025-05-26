package com.example.journey.data.activity.schedule.model

import androidx.lifecycle.ViewModel
import com.example.journey.data.activity.schedule.Slot

class TimetableViewModel @Inject constructor(
    private val repo: ScheduleRepository
) : ViewModel() {

    private val _slots = MutableLiveData<List<Slot>>()
    val slots: LiveData<List<Slot>> = _slots

    fun loadSchedule(id: Long) = viewModelScope.launch {
        repo.getSchedule(id)?.let { dto ->
            _slots.value = dto.scheduleData.slots.map { it.toDomain() }
        }
    }

    fun updateSlots(id: Long, newSlots: List<Slot>) = viewModelScope.launch {
        repo.upsertSlots(id, newSlots.map { it.toDto() })
        _slots.value = newSlots                    // UI 즉시 반영
    }
}