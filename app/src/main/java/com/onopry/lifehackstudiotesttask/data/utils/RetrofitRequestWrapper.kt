package com.onopry.lifehackstudiotesttask.data.utils

import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response

fun <T : Any> wrapRetrofitResponse(
    apiRequest: suspend () -> Response<T>
) = flow {
    try {
        val res = apiRequest()
        val body = res.body()
        if (res.isSuccessful && body != null)
            emit(ApiSuccess(data = body))
        else
            emit(ApiError(code = res.code(), message = res.message()))
    } catch (e: HttpException) {
        emit(ApiError(code = e.code(), message = e.message()))
    } /*catch (e: Throwable) {
        emit(ApiException(e))
    }*/
}