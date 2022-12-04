package com.onopry.lifehackstudiotesttask.core

import com.onopry.lifehackstudiotesttask.app.presentation.screen.list.ListState

/*
sealed class ScreenState<T>{
    sealed class Empty: ScreenState<Any>
    object Loading: ScreenState<Nothing>
    class Content<T>(data: T): ScreenState<T>
    sealed interface Error<T>: ScreenState<T> {
        object InternetConnectionError: Error<Nothing>
        sealed class LoadingError<T>(msg: String): Error<T>
    }
}*/

