package jyotti.apexing.apexing_android.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.UnknownHostException

fun LifecycleOwner.repeatCallDefaultOnStarted(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(state, block)
    }
}

fun Float.firstDecimalString(): String = String.format("%.1f", this)

inline fun ViewModel.getCoroutineExceptionHandler(
    crossinline onUnknownHostException: suspend () -> Unit,
    noinline onElse: (suspend (Throwable) -> Unit)? = null
): CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    viewModelScope.launch {
        when (throwable) {
            is UnknownHostException -> {
                onUnknownHostException()
            }

            else -> {
                onElse?.let { it(throwable) }
            }
        }
    }
}