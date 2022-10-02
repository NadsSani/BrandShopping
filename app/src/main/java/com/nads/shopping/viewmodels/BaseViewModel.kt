package com.nads.shopping.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

open class BaseViewModel() : ViewModel() {

//    protected val snackBarEventFire = LiveEvent<SnackBarEvent>()
//     val snackBarEvent = snackBarEventFire

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val coroutineMainContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    val scope = CoroutineScope(coroutineContext)
    val mainScope = CoroutineScope(coroutineMainContext)

    fun cancelAllRequests() = coroutineContext.cancel()
}