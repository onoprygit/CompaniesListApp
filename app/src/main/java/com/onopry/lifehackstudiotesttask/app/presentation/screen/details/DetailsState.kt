package com.onopry.lifehackstudiotesttask.app.presentation.screen.details

import com.onopry.lifehackstudiotesttask.data.model.CompanyDetails
import com.onopry.lifehackstudiotesttask.data.model.CompanyDetailsResponse

sealed class DetailsState {
    object Empty : DetailsState()
    object Loading : DetailsState()
    object Refreshing : DetailsState()
    class Content(val company: CompanyDetails) : DetailsState()
    class Exception(val msg: String) : DetailsState()
    sealed class ErrorState : DetailsState() {
        class LoadingError(val msg: String) : ErrorState()
        class InternetConnectionStateError(val msg: String) : ErrorState()
    }
}