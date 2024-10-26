package me.baggi.schedule.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FacultiesViewModel : ViewModel(){
    private val _dataFlow = MutableStateFlow<UiState<List<FacultyDTO>>>(UiState.Loading)
    val dataFlow: StateFlow<UiState<List<FacultyDTO>>> get() = _dataFlow

    init {
        viewModelScope.launch {
            loadData()
        }
    }

    private suspend fun loadData() {
        _dataFlow.value = UiState.Loading
        try {
            val response = Repository.getFaculties()
            _dataFlow.value = UiState.Success(response)
        } catch (e: Exception) {
            _dataFlow.value = UiState.Error(e)
        }
    }
}