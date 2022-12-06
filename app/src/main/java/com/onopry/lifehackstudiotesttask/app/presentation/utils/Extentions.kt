package com.onopry.lifehackstudiotesttask.app.presentation.utils

import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import kotlin.math.roundToInt

fun Fragment.safeObserveFlow(
     block: suspend () -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.INVISIBLE
}

fun View.gone(){
    visibility = View.GONE
}

fun Any.debugLog(msg: String, tag: String = this.javaClass.simpleName.toString()){
    Log.d("DEV_LOG_$tag", msg)
}

fun OkHttpClient.Builder.addQueryParam(
    name: String,
    key: String,
): OkHttpClient.Builder = this.addInterceptor { chain ->
    val request = chain.request()
    val url = request
        .url
        .newBuilder()
        .addQueryParameter(name, key)
        .build()
    val newRequest = request.newBuilder().url(url).build()
    chain.proceed(newRequest)
}

val Number.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics).roundToInt()