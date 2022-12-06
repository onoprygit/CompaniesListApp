package com.onopry.lifehackstudiotesttask.app.presentation.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onopry.lifehackstudiotesttask.app.presentation.utils.debugLog
import com.onopry.lifehackstudiotesttask.data.repository.Repository
import com.onopry.lifehackstudiotesttask.data.utils.ApiError
import com.onopry.lifehackstudiotesttask.data.utils.ApiException
import com.onopry.lifehackstudiotesttask.data.utils.ApiSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private var loadJob: Job? = null

    private val screenStateMutableFlow = MutableStateFlow<ListState>(ListState.Empty)
    val screenState get() = screenStateMutableFlow.asStateFlow()

    private val isRefreshMutableStateFlow = MutableStateFlow(false)
    val isRefreshState get() = isRefreshMutableStateFlow.asStateFlow()

    private fun loadCompanies() {
        debugLog("Execute load process", tag = "ListViewModel")
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            screenStateMutableFlow.emit(ListState.Loading)
            when (val result = repository.getCompanies()) {
                is ApiSuccess -> {
                    isRefreshMutableStateFlow.emit(false)
                    screenStateMutableFlow.emit(ListState.Content(companies = result.data))
                }
                is ApiError -> {
                    isRefreshMutableStateFlow.emit(false)
                    screenStateMutableFlow.emit(
                        ListState.ErrorState.LoadingError(
                            msg = "Oops, unexpected error with code ${result.code}: \n${result.message}"
                        )
                    )
                }
                is ApiException -> {
                    isRefreshMutableStateFlow.emit(false)
                    screenStateMutableFlow.emit(
                        ListState.Exception(msg = "Oops, unexpected exception, please try later")
                    )
                }
            }
        }

        loadJob = null
    }

    fun loadRefreshState(state: Boolean) {
        viewModelScope.launch {
            isRefreshMutableStateFlow.emit(state)
        }
    }

    private fun refresh() {
        loadCompanies()
    }

    init {
        loadCompanies()
        isRefreshMutableStateFlow.onEach { isRefreshing ->
            debugLog("Refrash state is [$isRefreshing]")
            if (isRefreshing)
                refresh()
        }.launchIn(viewModelScope)

        screenStateMutableFlow.onEach { listState ->
            debugLog("ListState is [${listState::class.java.simpleName}]")
        }.launchIn(viewModelScope)
    }
}