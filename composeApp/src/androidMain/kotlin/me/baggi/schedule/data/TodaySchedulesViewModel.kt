package me.baggi.schedule.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TodaySchedulesViewModel : ViewModel(){
    private val _dataFlow = MutableStateFlow<UiState<ScheduleDayDTO?>>(UiState.Loading)
    val dataFlow: StateFlow<UiState<ScheduleDayDTO?>> get() = _dataFlow

    suspend fun loadData(id: Long) {
        _dataFlow.value = UiState.Loading
        try {
            val response = Repository.getScheduleToday(id)
            _dataFlow.value = UiState.Success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            _dataFlow.value = UiState.Error(e)
        }
    }
}