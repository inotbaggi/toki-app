package me.baggi.schedule.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GroupsViewModel : ViewModel(){
    private val _dataFlow = MutableStateFlow<UiState<List<GroupDTO>>>(UiState.Loading)
    val dataFlow: StateFlow<UiState<List<GroupDTO>>> get() = _dataFlow

    suspend fun loadData(id: Long) {
        _dataFlow.value = UiState.Loading
        try {
            val response = Repository.getGroups(id)
            _dataFlow.value = UiState.Success(response)
        } catch (e: Exception) {
            _dataFlow.value = UiState.Error(e)
        }
    }
}