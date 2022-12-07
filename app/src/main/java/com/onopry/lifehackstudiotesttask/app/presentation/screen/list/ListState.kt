package com.onopry.lifehackstudiotesttask.app.presentation.screen.list

import com.onopry.lifehackstudiotesttask.data.model.CompanyItem

sealed class ListState {
    object Empty : ListState()
    object Loading : ListState()
    class Content(val companies: List<CompanyItem>) : ListState()
    class Exception(val msg: String) : ListState()
    sealed class ErrorState : ListState() {
        class LoadingError(val msg: String) : ErrorState()
    }
}