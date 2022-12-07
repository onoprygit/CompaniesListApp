package com.onopry.lifehackstudiotesttask.app.presentation.screen.details

import com.onopry.lifehackstudiotesttask.data.model.CompanyDetails

sealed class DetailsState {
    object Empty : DetailsState()
    object Loading : DetailsState()
    class Content(val company: CompanyDetails) : DetailsState()
    class Exception(val msg: String) : DetailsState()
    class Error(val msg: String) : DetailsState()
}