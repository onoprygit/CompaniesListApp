package com.onopry.lifehackstudiotesttask.app.presentation.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onopry.lifehackstudiotesttask.app.presentation.utils.debugLog
import com.onopry.lifehackstudiotesttask.data.repository.Repository
import com.onopry.lifehackstudiotesttask.data.utils.ApiError
import com.onopry.lifehackstudiotesttask.data.utils.ApiException
import com.onopry.lifehackstudiotesttask.data.utils.ApiSuccess
import com.onopry.lifehackstudiotesttask.data.utils.coordinatesIsNotBlank
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val showMapMutableStateFlow = MutableStateFlow(true)
    val showMapFlow get() = showMapMutableStateFlow.asStateFlow()

    private fun loadDetails() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            companyId?.let {
                screenStateMutableFlow.emit(DetailsState.Loading)
                when (val result = repository.getDetailsById(it)) {
                    is ApiSuccess -> {
                        var phone = result.data.phone
                        var website = result.data.www
                        var address = result.data.address
                        if (phone == null || phone.isEmpty()) phone =
                            "Телефон организации недоступен"
                        if (website == null || website.isEmpty()) website =
                            "Организация не разместила свой сайт"
                        if (address == null || address.isEmpty()) address =
                            "Организация не разместила свой адрес"

                        val newResult =
                            result.data.copy(phone = phone, www = website, address = address)
                        screenStateMutableFlow.emit(DetailsState.Content(newResult))
                    }
                    is ApiError -> {
                        screenStateMutableFlow.emit(
                            DetailsState.Error(
                                msg = "Unexpected error: ${result.message} with code: ${result.code}"
                            )
                        )
                    }
                    is ApiException -> screenStateMutableFlow.emit(
                        DetailsState.Exception(
                            msg = "Exception: ${result.e.message ?: "Unexpected exception occurred"}"
                        )
                    )
                }

            }
        }
    }

    fun refresh() {
        loadDetails()
    }

    fun sendCompanyId(id: String) {
        companyId = id
        loadDetails()
    }

    init {
        screenStateMutableFlow.onEach { state ->
            debugLog(
                "details screen state is [${state::class.java.simpleName}]",
                "DetailsViewModel"
            )
            if (state is DetailsState.Content) {
                val lat = state.company.lat
                val lon = state.company.lon
                if (coordinatesIsNotBlank(lat, lon)) showMapMutableStateFlow.emit(true)
                else showMapMutableStateFlow.emit(false)
            }
        }.launchIn(viewModelScope)
    }
}