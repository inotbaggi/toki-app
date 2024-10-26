package me.baggi.schedule.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GroupsViewModel(val id: Long) : ViewModel(){
    private val _dataFlow = MutableStateFlow<UiState<List<GroupDTO>>>(UiState.Loading)
    val dataFlow: StateFlow<UiState<List<GroupDTO>>> get() = _dataFlow

    init {
        viewModelScope.launch {
            loadData()
        }
    }

    private suspend fun loadData() {
        _dataFlow.value = UiState.Loading
        try {
            val response = Repository.getGroups(id)
            _dataFlow.value = UiState.Success(response)
        } catch (e: Exception) {
            _dataFlow.value = UiState.Error(e)
        }
    }
}