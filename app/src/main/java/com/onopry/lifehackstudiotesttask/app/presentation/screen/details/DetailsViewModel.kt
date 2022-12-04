package com.onopry.lifehackstudiotesttask.app.presentation.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onopry.lifehackstudiotesttask.data.repository.Repository
import com.onopry.lifehackstudiotesttask.data.utils.ApiError
import com.onopry.lifehackstudiotesttask.data.utils.ApiException
import com.onopry.lifehackstudiotesttask.data.utils.ApiSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private var loadJob: Job? = null
    private var companyId: String? = null

    private val screenStateMutableFlow = MutableStateFlow<DetailsState>(DetailsState.Empty)
    val screenState get() = screenStateMutableFlow.asStateFlow()

    private val isRefreshMutableStateFlow = MutableStateFlow(false)
    val isRefreshState get() = isRefreshMutableStateFlow.asStateFlow()

    private fun loadDetails(){
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            companyId?.let {
                repository.getDetailsById(it).collect{ result ->
                    when (result) {
                        is ApiSuccess -> screenStateMutableFlow.emit(DetailsState.Content(result.data))
                        is ApiError -> screenStateMutableFlow.emit(DetailsState.ErrorState.LoadingError(msg = result.message ?: "Unexpected error occurred"))
                        is ApiException -> screenStateMutableFlow.emit(DetailsState.ErrorState.LoadingError(msg = result.e.message ?: "Unexpected exception occurred"))
                    }
                }
            }
        }
    }

    fun sendCompanyId(id: String){
        companyId = id
        loadDetails()
    }

}